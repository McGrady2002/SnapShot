/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：MyVp2Adapter.java
 * 包名：com.jeremy.snapshot.adapter.MyVp2Adapter
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月16日 19:37:20
 */

package com.jeremy.snapshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jeremy.snapshot.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class MyVp2Adapter extends RecyclerView.Adapter<MyVp2Adapter.ViewHolder>{

//    private List<Integer> colors;
    Context context;

    public MyVp2Adapter(){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_item,parent,false));
    }


    @Override
    public int getItemCount() {
        //实现无限轮播
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        RoundedImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            imageView=itemView.findViewById(R.id.vp_image);
//            titleTv = itemView.findViewById(R.id.tv_title);
        }
    }
    public void onBindViewHolder(@NonNull MyVp2Adapter.ViewHolder holder, int position) {
//        RequestOptions options = new RequestOptions().error(R.drawable.add).bitmapTransform(new RoundedCorners(90));//图片圆角为30
        RequestOptions myOptions = new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),new RoundedCorners(10)));

        int i = position % 4;
        if (i==0) {
            holder.imageView.setImageResource(R.drawable.fzu_vp);
//            Glide.with(context).load("https://img2.baidu.com/it/u=275740832,3340042073&fm=26&fmt=auto").into(holder.imageView);
        }else if (i==1){
            Glide.with(context).load("https://img1.baidu.com/it/u=928280453,788254649&fm=26&fmt=auto").into(holder.imageView);
        }else if (i==2){
            Glide.with(context).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdimg01.c-ctrip.com%2Fimages%2Ffd%2Ftg%2Fg3%2FM02%2F08%2FEA%2FCggYGVbTXnqAV5IvAAK3ld9N10s467_R_710_10000_Q90.jpg&refer=http%3A%2F%2Fdimg01.c-ctrip.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636725504&t=2c2dc892c86809a081d7870dd558a473").into(holder.imageView);
        }else if (i==3){
            holder.imageView.setImageResource(R.drawable.fzu_vp3);
        }
//        holder.titleTv.setText("item " + i);
//        holder.container.setBackgroundColor(colors.get(i));
    }
}
