package com.example.myapplication; // 替换为你的包名

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MenuTestActivity extends AppCompatActivity {

    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);

        // 获取要操作的 TextView
        testTextView = findViewById(R.id.text_for_testing);
    }

    // 1. 重写此方法来加载你的菜单XML文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu); // 加载 res/menu/main_menu.xml
        return true;
    }

    // 2. 重写此方法来处理菜单项的点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 推荐做法：在方法开始时获取一次 ID
        int id = item.getItemId();

        // 改用 if-else if 结构来处理菜单项点击
        if (id == R.id.font_small) {
            testTextView.setTextSize(10); // 设置为10sp
            return true;
        } else if (id == R.id.font_medium) {
            testTextView.setTextSize(16); // 设置为16sp
            return true;
        } else if (id == R.id.font_large) {
            testTextView.setTextSize(20); // 设置为20sp
            return true;
        } else if (id == R.id.font_red) {
            testTextView.setTextColor(Color.RED); // 设置为红色
            return true;
        } else if (id == R.id.font_black) {
            testTextView.setTextColor(Color.BLACK); // 设置为黑色
            return true;
        } else if (id == R.id.menu_normal_item) {
            Toast.makeText(this, "点击了普通菜单项", Toast.LENGTH_SHORT).show(); // 弹出Toast
            return true;
        }

        // 处理其他未被捕获的菜单项
        return super.onOptionsItemSelected(item);
    }
}
