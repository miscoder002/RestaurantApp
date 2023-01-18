package bw.mymis.app.restaurantapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import bw.mymis.app.restaurantapp.databinding.ActivityRestaurantListBinding;

public class RestaurantListActivity extends AppCompatActivity {

    ActivityRestaurantListBinding binding;
    RestaurantItemClickListener restaurantItemClickListener;
    RestaurantAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        SQLiteDatabase db = openOrCreateDatabase("restaurants",MODE_PRIVATE,null);

        // ActionBar 客製化
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.restaurant_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.rest_list);
        getSupportActionBar().setSubtitle("花蓮縣");
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(getResources().getColor(R.color.teal_700)));
        // getSupportActionBar().setHideOnContentScrollEnabled(true);


        // 預先綁定 RecyclerView 內的清單被點選後的處理機制
        restaurantItemClickListener = new RestaurantItemClickListener() {
            @Override
            public void onClick(int position, String restaurantName) {
                Toast.makeText(RestaurantListActivity.this, "資料位置: " + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(RestaurantListActivity.this, "餐廳名稱: " + restaurantName, Toast.LENGTH_SHORT).show();
                // 此處 可以將畫面 轉換到 RestaurantDetailActivity 負責顯示餐廳詳細資訊的畫面 ( ui 轉 ui )
            }
        };
        adapter = new RestaurantAdapter(db ,  restaurantItemClickListener);
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adapter.setRegion(binding.txtRegion.getText().toString());
                //binding.txtRegion.setText("臺中市");
                adapter.setRegion(binding.txtRegion.getText().toString());
                getSupportActionBar().setSubtitle(binding.txtRegion.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        //每一個 UI元件都必須要與上層綁定　( Layout 則必須與有關的 Activity 綁定 )
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.restaurantListView.setLayoutManager(linearLayoutManager);
        binding.restaurantListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 資料的來源 ?  RecyclerView 指定需要與 RecyclerView.Adapter 物件綁定 提供資料與 細部的
        // ViewHolder Layout 一起呈現
        // Adapter 要負責 載入所有資料 並建立一個 UI View 容器 並把指定的資料填入 UI後 交給 RecyclerView顯示

        binding.restaurantListView.setAdapter( adapter );
        binding.txtRegion.setText("臺中市");

        // 對話框 範例
        binding.btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 對話框物件 AlertDialog 但 本身只單純有屬性 與許多待設定選項
                // Android 透過 Builder 輔助開發者設計畫面

                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantListActivity.this);
                builder.setTitle("是否確認下訂單?");
                builder.setMessage("這是 飲料的訂單 內容");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RestaurantListActivity.this, "任何button 都會之後都換關閉對話框", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RestaurantListActivity.this, "使用者按下否", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("繼續逛逛", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RestaurantListActivity.this, "繼續下單", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    // 處理 Home鍵返回上一Activity

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() ) {
            case android.R.id.home:
                // finish 是關閉(destroy 目前這個 Activity)
                // finish()
                // 只是要模擬 使用者按下返回鍵 back
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // 等著被 Adapter 呼叫
    public void callByAdapter(int pos , String name) {

    }
}