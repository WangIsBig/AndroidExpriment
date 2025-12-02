package com.example.myapplication; // 确保这是你的包名

import android.Manifest; // 导入 Manifest
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager; // 导入 PackageManager
import android.os.Build; // 导入 Build
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher; // 导入 ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts; // 导入 Contract
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat; // 导入 ContextCompat

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // ... (你的 animalNames 和 animalImages 数组保持不变) ...
    private String[] animalNames = {"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
    private int[] animalImages = {
            R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
            R.drawable.dog, R.drawable.cat, R.drawable.elephant
    };

    private ListView animalListView;
    private static final String CHANNEL_ID = "animal_channel";

    // 1. 声明权限请求启动器
    // registerForActivityResult 必须在 Activity 创建时 (例如 onCreate) 被调用
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // 权限被授予。用户现在可以收到通知了。
                    Toast.makeText(this, "通知权限已授予", Toast.LENGTH_SHORT).show();
                } else {
                    // 权限被拒绝。
                    // 你应该在这里告知用户，没有权限将无法接收通知。
                    Toast.makeText(this, "通知权限被拒绝", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        // 2. 在 Activity 启动时请求权限
        askForNotificationPermission();

        // ... (你设置 Adapter 的代码保持不变) ...
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < animalNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", animalNames[i]);
            item.put("image", animalImages[i]);
            dataList.add(item);
        }

        String[] from = {"name", "image"};
        int[] to = {R.id.animal_name, R.id.animal_image};

        SimpleAdapter adapter = new SimpleAdapter(
                this, dataList, R.layout.list_item_layout, from, to
        );

        animalListView = findViewById(R.id.animal_list_view);
        animalListView.setAdapter(adapter);

        // 点击事件监听器保持不变
        animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> selectedItem = dataList.get(position);
                String selectedName = (String) selectedItem.get("name");

                Toast.makeText(MainActivity.this, "你选择了: " + selectedName, Toast.LENGTH_SHORT).show();

                // 调用修改后的发送通知方法
                sendAnimalNotification(selectedName);
            }
        });
    }

    /**
     * 新增方法：用于请求通知权限
     */
    private void askForNotificationPermission() {
        // 权限只在 Android 13 (API 33) 及以上版本需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 检查权限是否已经被授予
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // 权限已被授予，什么都不用做
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // (可选) 向用户解释为什么你需要这个权限
                // 比如弹出一个对话框...
                // 然后再启动请求
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // 直接请求权限
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * 3. 修改后的 sendAnimalNotification 方法
     * @param animalName 动物名称 (列表项内容)
     */
    private void sendAnimalNotification(String animalName) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(animalName)
                .setContentText("这是关于 " + animalName + " 的一条通知。")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // 4. 在发送通知前，*再次*检查权限！
        // 这是为了防止用户在 Activity 启动后，到设置里手动关闭了权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限，就不发送通知，并可以给用户一个提示
                Toast.makeText(this, "没有通知权限，无法发送通知", Toast.LENGTH_SHORT).show();
                return; // 提前返回，不执行 notify()
            }
        }

        // 只有在权限被授予 (或系统版本低于TIRAMISU) 时，才执行这一行
        notificationManager.notify(1, builder.build()); // 这一行现在是安全的
    }

    /**
     * createNotificationChannel 方法保持不变
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "动物通知渠道";
            String description = "用于显示动物列表点击通知";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}