package com.example.myapplication; // 替换为你的包名

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        Button showDialogButton = findViewById(R.id.show_dialog_button);

        showDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        // 1. 创建 AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. 加载自定义布局
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_login, null);

        // 3. 将布局设置给 Builder [cite: 28]
        builder.setView(dialogView);

        // 4. 获取自定义布局中的视图
        EditText username = dialogView.findViewById(R.id.edit_text_username);
        EditText password = dialogView.findViewById(R.id.edit_text_password);
        Button btnCancel = dialogView.findViewById(R.id.button_cancel);
        Button btnSignIn = dialogView.findViewById(R.id.button_sign_in);

        // 5. 创建 AlertDialog
        AlertDialog dialog = builder.create();

        // 6. 为对话框中的按钮设置点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 关闭对话框
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                Toast.makeText(AlertDialogActivity.this, "Username: " + user, Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 关闭对话框
            }
        });

        // 7. 显示对话框
        dialog.show();
    }
}