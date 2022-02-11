/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：RecordAdapter.java
 * 包名：com.jeremy.snapshot.adapter.RecordAdapter
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月28日 23:01:53
 */

package com.jeremy.snapshot.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.jeremy.snapshot.network.entity.Record;
import com.jeremy.snapshot.ui.activity.AppraiseActivity;
import com.jeremy.snapshot.ui.activity.FbProgressActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;

    private Record records;

    public RecordAdapter(Record records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        final RecordAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(records.getData().get(position).getTitle());
        holder.desc.setText(records.getData().get(position).getDesc());
        holder.time.setText(records.getData().get(position).getTime());
        holder.category.setText(records.getData().get(position).getCategory());
        holder.process.setText(records.getData().get(position).getProcess());
        if (!records.getData().get(position).getImageUrl().toString().equals(""))
            Glide.with(context).load(records.getData().get(position).getImageUrl()).into(holder.image);
        holder.access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评价
                AppraiseActivity.currentId = records.getData().get(position).getId();
                Intent intent = new Intent(context, AppraiseActivity.class);
                context.startActivity(intent);
                holder.access.reset();
            }
        });
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppraiseActivity.currentId = records.getData().get(position).getId();
                Intent intent = new Intent(context, FbProgressActivity.class);
                context.startActivity(intent);
                holder.check.reset();
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mItemView;
        TextView title;
        TextView desc;
        TextView time;
        TextView category;
        TextView process;
        ImageView image;
        SubmitButton check;
        SubmitButton access;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            title = (TextView) itemView.findViewById(R.id.record_title);
            desc = (TextView) itemView.findViewById(R.id.record_desc);
            time = (TextView) itemView.findViewById(R.id.record_time);
            category = (TextView) itemView.findViewById(R.id.record_category);
            process = (TextView) itemView.findViewById(R.id.record_process);
            image = (ImageView) itemView.findViewById(R.id.record_img);
            check = (SubmitButton) itemView.findViewById(R.id.check_process);
            access = (SubmitButton) itemView.findViewById(R.id.access);
        }
    }
}
