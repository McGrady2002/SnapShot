/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：HomePageFragment.java
 * 包名：com.jeremy.snapshot.ui.fragment.HomePageFragment
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月27日 18:45:12
 */

package com.jeremy.snapshot.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.ui.activity.LoginActivity;
import com.jeremy.snapshot.ui.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView name;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
    public void onResume() {
        if (MainActivity.LOGINED == 1) {
            name.setText(MainActivity.account);
        } else {
            name.setText("点击登录");
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ImageView contact = (ImageView) view.findViewById(R.id.contact_image);
        name = (TextView) view.findViewById(R.id.name);
        if (MainActivity.LOGINED == 1) {
            name.setText(MainActivity.account);
        }
        TextView contactText = (TextView) view.findViewById(R.id.contact_us);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.settings_layout);
        linearLayout.setOnClickListener(this);
        contact.setOnClickListener(this);
        contactText.setOnClickListener(this);
        name.setOnClickListener(this);
        return view;
    }

    public static void contactQQ(Context context, String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            //uin是发送过去的qq号码
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
//            ToastUtils.showShortToast(context, "您还没有安装QQ，请先安装软件");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_us:
                contactQQ(getActivity(), "1574768045");
                break;
            case R.id.contact_image:
                contactQQ(getActivity(), "1574768045");
                break;
            case R.id.name:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.settings_layout:
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intent);
                new AlertView("设置", null, "取消", null,
                        new String[]{"退出登录", "删除账号信息并退出"},
                        getContext(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        switch (position) {
                            case 0:
                                if (MainActivity.LOGINED == 1) {
                                    startActivity(intent);
                                    MainActivity.LOGINED = 0;
                                }
                                break;
                            case 1:
                                if (MainActivity.LOGINED == 1) {
                                    SharedPreferences sp = getContext().getSharedPreferences("UserAccount",
                                            Context.MODE_PRIVATE);    //保存模式
                                    sp.edit().clear().commit();
                                    startActivity(intent);
                                    MainActivity.LOGINED = 0;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }).show();

                break;
            default:
                break;
        }
    }
}