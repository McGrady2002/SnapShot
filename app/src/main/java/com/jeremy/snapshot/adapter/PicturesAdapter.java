/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：PicturesAdapter.java
 * 包名：com.jeremy.snapshot.adapter.PicturesAdapter
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月31日 15:37:42
 */

package com.jeremy.snapshot.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeremy.snapshot.R;

import java.util.List;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {

    private List<Uri> uris;

    private List<String> path;

    private Context context;

    public PicturesAdapter(List<String> path) {
        this.path = path;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_item, parent, false);
        context = parent.getContext();
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Bitmap bitmap = BitmapFactory.decodeFile(path.get(position));
//        holder.picture.setImageBitmap(bitmap);
        Glide.with(context).load(path.get(position)).into(holder.picture);
        holder.picDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("delete1", "success");
                delete(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    private void delete(int i) {
        path.remove(i);
    }

    @Override
    public int getItemCount() {
        return path.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        ImageView picDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.pic_item);
            picDelete = (ImageView) itemView.findViewById(R.id.pic_delete);
        }
    }


}
