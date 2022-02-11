/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：PoiItemAdapter.java
 * 包名：com.jeremy.snapshot.adapter.PoiItemAdapter
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月30日 16:00:34
 */

package com.jeremy.snapshot.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jeremy.snapshot.R;

import org.w3c.dom.Text;

import java.util.List;

public class PoiItemAdapter extends RecyclerView.Adapter<PoiItemAdapter.Viewholder> {

    List<PoiInfo> poiInfos;
    private int mCurSelectPos = 0; // 当前选中的item pos
    public String descri="";

    private MyOnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_item, parent, false);
        final Viewholder viewHolder = new Viewholder(view);
        return viewHolder;
    }

    public PoiItemAdapter(List<PoiInfo> poiInfos) {
        this.poiInfos = poiInfos;
    }

    public void updateData(List<PoiInfo> poiInfos) {
        this.poiInfos = poiInfos;
        notifyDataSetChanged();
        mCurSelectPos = 0;
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if (position < 0) {
            return;
        }
        final Viewholder myViewHolder = (Viewholder) holder;
        if (null == myViewHolder) {
            return;
        }

        modifyItemSelectState(myViewHolder, position);
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos == mCurSelectPos) {
                    return;
                }
                mCurSelectPos = pos;
                notifyDataSetChanged();
                if (null != poiInfos && pos < poiInfos.size()) {
                    PoiInfo poiInfo = poiInfos.get(pos);
                    mOnItemClickListener.onItemClick(pos, poiInfo);
                }
            }
        });
        mbindViewHolder(myViewHolder, position);
    }

    private void mbindViewHolder(Viewholder viewHolder, int position) {
        if (null == poiInfos || position >= poiInfos.size()) {
            return;
        }

        if (0 == position) {
            PoiInfo poiInfo = poiInfos.get(0);
            bindAddressInfo(viewHolder, poiInfo);
        } else {
            bindPoiInfo(viewHolder, position);
        }
    }

    private void bindPoiInfo(Viewholder viewHolder, int position) {
        PoiInfo poiInfo = poiInfos.get(position);
        if (null == poiInfo) {
            return;
        }

        String name = poiInfo.getName();

        viewHolder.poiName.setText(name);
        viewHolder.poiAddr.setText(poiInfo.getAddress());
        String poiAddress = poiInfo.getAddress();
        if (!TextUtils.isEmpty(poiAddress)) {
            viewHolder.poiAddr.setText(poiAddress);
        } else {
            viewHolder.poiAddr.setText(descri);
        }
    }

    private void bindAddressInfo(Viewholder viewHolder, PoiInfo poiInfo) {
        String name = "【" + poiInfo.getAddress() + "】";
        viewHolder.poiName.setText(name);
        viewHolder.poiAddr.setText(descri);
    }


    private void modifyItemSelectState(Viewholder myViewHolder, int position) {
        if (position != mCurSelectPos) {
            myViewHolder.poiCheck.setVisibility(View.INVISIBLE);
        } else if (position == mCurSelectPos) {
            myViewHolder.poiCheck.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 定义item点击回调接口
     */
    public interface MyOnItemClickListener {
        void onItemClick(int position, PoiInfo poiInfo);
    }
    @Override
    public int getItemCount() {
        return poiInfos.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        View mItemView;
        TextView poiName;
        TextView poiAddr;
        ImageView poiCheck;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mItemView=itemView;
            poiAddr = (TextView) itemView.findViewById(R.id.poi_addr);
            poiName = (TextView) itemView.findViewById(R.id.poi_name);
            poiCheck = (ImageView) itemView.findViewById(R.id.image_check);
        }
    }
}
