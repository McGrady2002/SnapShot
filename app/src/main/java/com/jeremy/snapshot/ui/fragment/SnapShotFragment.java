/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：SnapShotFragment.java
 * 包名：com.jeremy.snapshot.ui.fragment.SnapShotFragment
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月23日 22:32:59
 */

package com.jeremy.snapshot.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jeremy.snapshot.App;
import com.jeremy.snapshot.adapter.MyVp2Adapter;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.ui.activity.FeedbackActivity;
import com.jeremy.snapshot.ui.activity.HistoryRecordActivity;
import com.jeremy.snapshot.ui.activity.LoginActivity;
import com.jeremy.snapshot.ui.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SnapShotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SnapShotFragment extends Fragment implements View.OnClickListener {

    private ViewPager2 viewPager2;

    Context context;

    private int lastPosition;

    private LinearLayout indicatorContainer;            //填充指示点的容器
    private Handler mHandler = new Handler();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SnapShotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SnapShotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SnapShotFragment newInstance(String param1, String param2) {
        SnapShotFragment fragment = new SnapShotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_snap_shot, container, false);
        viewPager2 = (ViewPager2) view.findViewById(R.id.viewpage2);
        indicatorContainer = (LinearLayout) view.findViewById(R.id.container_indicator);
        initIndicatorDots();
        MyVp2Adapter myVp2Adapter = new MyVp2Adapter();
        viewPager2.setAdapter(myVp2Adapter);
        viewPager2.setCurrentItem(500);
        lastPosition = 500;
        ImageView takephoto=(ImageView)view.findViewById(R.id.takephoto);
        ImageView records=(ImageView)view.findViewById(R.id.records);
        takephoto.setOnClickListener(this);
        records.setOnClickListener(this);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //轮播时，改变指示点
                int current = position % 4;
                int last = lastPosition % 4;
                indicatorContainer.getChildAt(current).setBackgroundResource(R.drawable.shape_dot_selected);
                indicatorContainer.getChildAt(last).setBackgroundResource(R.drawable.shape_dot);
                lastPosition = position;
            }
        });
        return view;
    }

    //region 轮播图指示点初始化
    private void initIndicatorDots() {
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(getActivity());
            if (i == 0) imageView.setBackgroundResource(R.drawable.shape_dot_selected);
            else imageView.setBackgroundResource(R.drawable.shape_dot);
            //为指示点添加间距
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginEnd(4);
            imageView.setLayoutParams(layoutParams);
            //将指示点添加进容器
            indicatorContainer.addView(imageView);
        }
    }
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable, 5000);
    }

    /* 当应用被暂停时，让轮播图停止轮播 */
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //获得轮播图当前的位置
            int currentPosition = viewPager2.getCurrentItem();
            currentPosition++;
            viewPager2.setCurrentItem(currentPosition, true);
            mHandler.postDelayed(runnable, 5000);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takephoto:
                if (MainActivity.LOGINED == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.records:
                if (MainActivity.LOGINED == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), HistoryRecordActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}