package bw.mymis.app.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import bw.mymis.app.restaurantapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnABControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ActionBar ( 標題 , Home鍵 , 顏色背景 , logo,  顯示與否 , 搭配 RecyclerView捲動時消失....)
                // 內建 optionsMenu 由 onCreateOptions 負責...
                ActionBar actionBar = MainActivity.this.getSupportActionBar();
                if( actionBar.isShowing() ) {
                    Toast.makeText(MainActivity.this, "ActionBar 目前在顯示中,現在關閉", Toast.LENGTH_SHORT).show();
                    actionBar.hide();
                    // 呼叫 hide 將 actionbar 隱藏 第二版 2.0 修正某功能
            //        actionBar.setDisplayShowHomeEnabled(false);
                } else {
                    Toast.makeText(MainActivity.this, "ActionBar 已關閉,現在打開", Toast.LENGTH_SHORT).show();
                    // 顯示 action bar
                    actionBar.show();

                }
                // actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);  // 使用慣例圖示 向左箭頭
                // actionBar.setDefaultDisplayHomeAsUpEnabled(true);

            }
        });

    }

    //建立 OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 請出 Menu Inflater 出來製作選單
        // 須提供 xml
        this.getMenuInflater().inflate(R.menu.main_menu,menu);
        
        return super.onCreateOptionsMenu(menu);
    }
    // 接受並處理 OptionMenu 中的 OptionItem 的 click 事件


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.main_load:
                // 開啟 資料更新 Activity
                // 顯式意圖 : 從 MainActivity 跳至 DataMaintainActivity
                // 為何必須宣告 MainActivity.this ?  因為 DataMaintain 按下 back 鍵必須返回上一層
                Intent intentMaintain = new Intent(MainActivity.this, DataMaintainActivity.class);
               // intentMaintain.setAction(Intent.ACTION_VIEW);
                startActivity(intentMaintain);
                break;
            case R.id.main_list:
                // 開啟 餐廳 RecyclerView
                Intent intentRest = new Intent(MainActivity.this, RestaurantListActivity.class);
                startActivity(intentRest);
                break;
            case R.id.main_exit:
                //關閉
                this.finish();
                break;
            case android.R.id.home:
                // finish 是關閉(destroy 目前這個 Activity)
                // finish()
                // 只是要模擬 使用者按下返回鍵 back
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}