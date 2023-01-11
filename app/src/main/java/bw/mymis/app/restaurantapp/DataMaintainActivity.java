package bw.mymis.app.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bw.mymis.app.restaurantapp.databinding.ActivityDataMaintain2Binding;
import bw.mymis.app.restaurantapp.util.JSonToDB;
import bw.mymis.app.restaurantapp.util.SimpleAPIWorker;
import okhttp3.Request;

public class DataMaintainActivity extends AppCompatActivity {
    ActivityDataMaintain2Binding binding;
    SharedPreferences activityPreference;
    SQLiteDatabase db;
    ExecutorService executor ;
    final static String createTable =
            "create table if not exists restaurant(" +
                    "_id text," +
                    "name text," +
                    "description text," +
                    "region text," +
                    "town text," +
                    "address text," +
                    "tel text," +
                    "opentime text);";
    // Handler 負責接收 下載工作的Thread 呼叫回傳資料
    Handler dataHandler = new Handler(Looper.getMainLooper()) {
        // 當 網路下載的執行緒 從觀光局網站下載 JSON後會傳到這個 Handler 進行處理
        // 1. 可以選擇在 Handler 進行轉換
        // 2. 可在 Thread 當下就進行寫入
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();
            int status = bundle.getInt("status");
            if( status == 200 ) {
                db.execSQL("drop table if exists restaurant;");
                db.execSQL(createTable);
                jsonString = bundle.getString("data");
                JSonToDB j2db = new JSonToDB( openOrCreateDatabase("restaurants", MODE_PRIVATE,null));
                j2db.writeToDatabase(jsonString);   // 如果資料量巨大  寫入超過時間 , Android ANR 又發生, 選擇 方法一 有執行緒的優點
            }
            // 取得資料後 進行轉換 : 作法 2
            Date now = new Date();
            activityPreference.edit().putString("lastUpdate", now.toString());
            binding.txtLastUpdate.setText(activityPreference.getString("lastUpdate","無更新"));
            binding.dwProgress.setVisibility(View.INVISIBLE);
        }
    };
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataMaintain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityPreference = this.getPreferences(MODE_PRIVATE);
        executor = Executors.newSingleThreadExecutor();
        db = openOrCreateDatabase("restaurants", MODE_PRIVATE, null);
        //
        getSupportActionBar().setTitle("資料更新服務");
        getSupportActionBar().setIcon(R.mipmap.logo_icon);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(getResources().getColor(R.color.brown)));
        //
        binding.btnResetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityPreference.edit()
                        .remove("isFirstTime")
                        .remove("lastUpdate")
                        .apply();
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.dwProgress.setVisibility(View.VISIBLE);
                // 網路下載 https://media.taiwan.net.tw/XMLReleaseALL_public/restaurant_C_f.json
                Request request = new Request.Builder()
                        .url("https://media.taiwan.net.tw/XMLReleaseALL_public/restaurant_C_f.json")
                        .build();

                SimpleAPIWorker simpleAPIWorker = new SimpleAPIWorker(request, dataHandler);
                executor.execute(simpleAPIWorker);
            }
        });
        checkA();
        binding.txtLastUpdate.setText( activityPreference.getString("lastUpdate","無").toUpperCase() );

    }

    // 積極式的檢查
    public void checkA() {
        // 標準流程
        // 記住 SharedPreference & activity Preference 的使用時機
        boolean needInitial = activityPreference.getBoolean("isFirstTime", true);
        if( needInitial ) {
            // 進行初始化
            // 建立資料庫 & restaurant Table
            db.execSQL(createTable);
            // 從 raw 讀取 restaurant.json 後 寫入 restaurant 資料表
            db.execSQL(createTable);
            Cursor cursor = db.rawQuery("select * from restaurant;",null);
            if( cursor == null || cursor.getCount() == 0 ) {
                //代表資料表 restaurant 內無資料 請從 raw 讀取 restaurant.json 並轉入 table
            }
            Date now = new Date();
            // 初始化資料庫後 代表 未來不會是第一次執行
            activityPreference.edit()
                    .putBoolean("isFirstTime", false)
                    .putString("lastUpdate", now.toString())
                    .apply();
        }
    }

    // 消極式的處理機制
    public void checkB() {
        db = openOrCreateDatabase("restaurants", MODE_PRIVATE, null); // 有開啟 沒有就建立
        String createTable =
                "create table if not exists restaurant(" +
                "_id text," +
                "name text," +
                "region text," +
                "town text," +
                "address text," +
                "tel text);";
        db.execSQL(createTable);
        Cursor cursor = db.rawQuery("select * from restaurant;",null);
        if( cursor == null || cursor.getCount() == 0 ) {
            //代表資料表 restaurant 內無資料 請從 raw 讀取 restaurant.json 並轉入 table
        }
    }
}