package com.example.myapplication;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionModeActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_mode); // 使用修正后的 XML 布局

        listView = findViewById(R.id.action_mode_list_view);

        // 1. 准备数据源
        String[] data = {"One", "Two", "Three", "Four", "Five"};
        dataList = new ArrayList<>(Arrays.asList(data));

        // 2. 创建并设置 Adapter
        adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_action_mode,
                R.id.item_text,
                dataList
        );
        listView.setAdapter(adapter);

        // 3. 设置 MultiChoiceModeListener
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            /**
             * 当 ActionMode 第一次被创建时调用
             */
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // 加载上下文菜单，例如包含删除按钮的 context_menu.xml
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            /**
             * 当上下文菜单中的某一项被点击时调用
             */
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // 仅实现点击效果，不执行删除（根据你的要求）
                if (item.getItemId() == R.id.action_delete) {
                    Toast.makeText(ActionModeActivity.this, "已点击删除按钮，上下文操作模式关闭。", Toast.LENGTH_SHORT).show();
                    mode.finish(); // 关闭 ActionMode
                    return true;
                }
                return false;
            }

            /**
             * 当 ActionMode 被销毁时
             * 【重点】：此时系统会自动将顶部栏标题恢复为 Activity 的默认标题 ("My Application")。
             */
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // 无需额外操作，默认标题会自动恢复。
            }

            /**
             * 当列表项的选中状态发生变化时调用
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // 更新标题，显示选中的项目数量 (X selected)
                int checkedCount = listView.getCheckedItemCount();

                // 当 checkedCount 降到 0 时，ActionMode 会自动关闭。
                mode.setTitle(checkedCount + " selected");
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });
    }
}