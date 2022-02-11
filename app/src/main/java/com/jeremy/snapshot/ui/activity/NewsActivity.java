/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：NewsActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.NewsActivity
 * 当前修改时间：2021年12月19日 23:55:47
 * 上次修改时间：2021年12月19日 23:55:47
 */

package com.jeremy.snapshot.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremy.snapshot.R;

public class NewsActivity extends AppCompatActivity {

    // 预定为新闻标题
    private String[] newsTitle;
    // 预定为新闻大致内容
    private String[] newsInfo;
    // 预定为新闻日期
    private String[] newsDate;
    // 预定为新闻右侧图片
    private int[] newsImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);

        newsTitle = new String[15];
        newsInfo = new String[15];
        newsDate = new String[15];
        newsImg = new int[15];

        for (int i = 0; i < newsTitle.length; i++) {
            newsTitle[i] = "新闻标题";
            newsInfo[i] = "新闻内容\n新闻内容\n新闻内容\n新闻内容\n";
            newsDate[i] = "2000-01-01  00:00:00";
            newsImg[i] = R.drawable.add;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new HomeAdapter());

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.news_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.title.setText(newsTitle[position]);
            holder.info.setText(newsInfo[position]);
            holder.date.setText(newsDate[position]);
            holder.img.setImageResource(newsImg[position]);
        }

        @Override
        public int getItemCount() {
            return newsTitle.length;
        }


        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView info;
            TextView date;
            ImageView img;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.news_title);
                info = itemView.findViewById(R.id.news_info);
                date = itemView.findViewById(R.id.news_date);
                img = itemView.findViewById(R.id.news_img);
            }
        }
    }
}