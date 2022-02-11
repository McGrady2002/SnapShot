/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：NewsAdapter.java
 * 包名：com.jeremy.snapshot.adapter.NewsAdapter
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月16日 19:37:20
 */

package com.jeremy.snapshot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.entity.Bean;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;

    private List<Bean.News> newsList;

    public NewsAdapter(List<Bean.News> newsList) {
        this.newsList = newsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View newsView;
        TextView title;
        TextView desc;
        TextView account;
        TextView date;
        TextView time;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsView=itemView;
            title=(TextView)itemView.findViewById(R.id.news_title);
            desc=(TextView)itemView.findViewById(R.id.news_desc);
            account=(TextView)itemView.findViewById(R.id.publish_account);
            date=(TextView)itemView.findViewById(R.id.publish_date);
            time=(TextView)itemView.findViewById(R.id.publish_time);
            image=(ImageView)itemView.findViewById(R.id.news_image);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //详情页面
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bean.News news=newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.desc.setText(news.getDesc());
        holder.account.setText(news.getPublishAccount());
        holder.date.setText(news.getDate());
        holder.time.setText(news.getTime());
        Log.d("NewsActivity",news.getImageUrl().toString());
        Glide.with(context).load(news.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
