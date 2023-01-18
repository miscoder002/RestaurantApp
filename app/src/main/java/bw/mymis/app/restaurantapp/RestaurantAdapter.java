package bw.mymis.app.restaurantapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bw.mymis.app.restaurantapp.model.SimpleRestaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    // 製作資料 <--- SQLite restaurants.db
    SQLiteDatabase db;
    ArrayList<SimpleRestaurant> restAll;
    RestaurantItemClickListener listener;
    // 多接收一個 Listener , 當使用者 點選選項中的 餐廳名稱時 透過 listener 傳給 "老大" 通知
    public RestaurantAdapter(SQLiteDatabase db, RestaurantItemClickListener listener) {
        this.db = db;
        this.listener = listener;
        restAll = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from restaurant;", null );
        //請將資料轉成 adapter 本的資料結構備用 以免 db被系統回收關閉
        if( cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            do {
                SimpleRestaurant simpleRestaurant = new SimpleRestaurant(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(3),
                        cursor.getString(5)
                );
                restAll.add(simpleRestaurant);
            }while ( cursor.moveToNext());
        }
        cursor.close();
        // db.close();
    }
    public void setRegion(String region) {
        restAll.clear();;

        Cursor cursor;
        if( region.equals("all")) {
            cursor = db.rawQuery("select * from restaurant;" , null );
        } else {
            cursor = db.rawQuery("select * from restaurant where region = ?", new String[]{region});
        }

        //請將資料轉成 adapter 本的資料結構備用 以免 db被系統回收關閉
        if( cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            do {
                SimpleRestaurant simpleRestaurant = new SimpleRestaurant(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(3),
                        cursor.getString(5)
                );
                restAll.add(simpleRestaurant);
            }while ( cursor.moveToNext());
        }
        cursor.close();
    }

    // 從哪個 layout xml 建立 畫面UI ( 由 RecyclerView 呼叫(畫面捲動時)  開發者無法察覺)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent,false);
        return new ViewHolder(view);   // 回傳一個沒有資料的 ViewHolder 給 RecyclerView
    }

    // 將上面產生好的 UI　綁定資料 ( 由 RecyclerView 呼叫  開發者無法察覺)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText( restAll.get(position).getName() );
        holder.txtAddress.setText( restAll.get(position).getAddress() );
        holder.txtRegion.setText( restAll.get(position).getRegion() );
        // 此時 資料正是綁定(有資料位置的資訊 與資料內容)
        // 如果後續 app  被 Android System 進行暫時回收(onDestroy .. onResume ... onDestroy )
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // final int pos= position;
                final int pos = holder.getAdapterPosition(); // 正確做法
                String name = restAll.get(pos).getName();
                String addr = restAll.get(pos).getAddress();
                // 將事件控制權交回給 RestaurantListActivity 負責 不應在此處處理(可以這樣 但觀念錯)
                listener.onClick( pos, restAll.get(pos).getName() );
                //缺乏與 RestaturantActivity 的互動性
                // 以下做法 行為上無錯 可執行 但觀念是錯誤
                //Toast.makeText(holder.txtName.getContext(), "資料位置: " + pos, Toast.LENGTH_SHORT).show();
                //Toast.makeText(holder.txtName.getContext(), "餐廳名稱: " + name, Toast.LENGTH_SHORT).show();
                //Toast.makeText(RestaurantListActivity.this, "餐廳地址: " + addr, Toast.LENGTH_SHORT).show();
                //RestaurantListActivity.this.callByAdapter();
                // 此處 可以將畫面 轉換到 RestaurantDetailActivity 負責顯示餐廳詳細資訊的畫面 ( 非 ui 轉跳 另一個 ui ???? )
            }
        });
    }

    // 告訴 RecylerView 有多少筆資料
    @Override
    public int getItemCount() {
        return restAll.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtRegion;
        TextView txtAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.txtName = itemView.findViewById(R.id.txtName);
            this.txtRegion = itemView.findViewById(R.id.txtRegion);
            this.txtAddress = itemView.findViewById(R.id.txtAddress);
            // 此時 處於畫面產生中 尚未出現在使用者的 RecyclerView 內 也無資料
//            txtName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }
}
