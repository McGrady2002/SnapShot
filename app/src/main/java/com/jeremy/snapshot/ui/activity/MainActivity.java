/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：MainActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.MainActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */

package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jeremy.snapshot.Base64Util;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.jeremy.snapshot.ui.fragment.HomePageFragment;
import com.jeremy.snapshot.ui.fragment.NewsListFragment;
import com.jeremy.snapshot.ui.fragment.SnapShotFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private List<Fragment> fragments;

    NewsListFragment newsListFragment;
    SnapShotFragment snapShotFragment;
    HomePageFragment homePageFragment;

    private Toolbar toolbar;
    private TextView toolbartitle;
//    private int currentId = 0;
//    private int tabHome = 0;

    public static int LOGINED = 0;  //登录状态 未登录：0   已登录：1

    public static String account;
    public static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    //region 初始化控件
    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        fragments = new ArrayList<>();
        newsListFragment = new NewsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, newsListFragment);
        transaction.commit();
        switchFragment(0);
        SharedPreferences sp = getSharedPreferences("UserAccount",
                Context.MODE_PRIVATE);    //保存模式
        boolean index = sp.getBoolean("auto", false);
        if (LOGINED == 0 && index)
            login();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                tabHome = 0;
                switch (item.getItemId()) {
                    case R.id.tab_news:
//                        currentId = 0;
                        switchFragment(0);
                        break;
                    case R.id.tab_snapshot:
//                        currentId = 1;
                        switchFragment(1);
                        break;
                    case R.id.tab_mine:
//                        if (LOGINED == 1)
                            switchFragment(2);
//                        else {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            switchFragment(2);
//                            tabHome = 1;
//                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        //startActivity(intent);
    }
    //endregion

    //region 碎片切换
    private void switchFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragments(transaction);
        switch (index) {
            case 0:
                if (newsListFragment != null)
                    transaction.show(newsListFragment);
                else {
                    newsListFragment = new NewsListFragment();
                    transaction.add(R.id.fragment_container, newsListFragment);
                }
                toolbartitle.setText("校园新闻");
                break;
            case 1:
                if (snapShotFragment != null)
                    transaction.show(snapShotFragment);
                else {
                    snapShotFragment = new SnapShotFragment();
                    transaction.add(R.id.fragment_container, snapShotFragment);
                }
                toolbartitle.setText("文明福大，你我共创");
                break;
            case 2:
                if (homePageFragment != null)
                    transaction.show(homePageFragment);
                else {
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.fragment_container, homePageFragment);
                }
                toolbartitle.setText("");
                break;
        }
        transaction.commit();
    }
    //endregion

    //region 隐藏所有碎片
    private void hideAllFragments(FragmentTransaction fragmentTransaction) {
        if (newsListFragment != null)
            fragmentTransaction.hide(newsListFragment);
        if (snapShotFragment != null)
            fragmentTransaction.hide(snapShotFragment);
        if (homePageFragment != null)
            fragmentTransaction.hide(homePageFragment);
    }
    //endregion

    //region 自动登录
    private void login() {
        try {
            SharedPreferences sp = getSharedPreferences("UserAccount", Context.MODE_PRIVATE);
            account = sp.getString("account", "");  //取出账号
            String str = new String(Base64Util.decode(sp.getString("password", "")));//取出密码，此时的密码是经过Base64编码后的，需要解码
            password = str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!account.equals("") && !password.equals("")) {
            OkGo.<String>get(HttpTool.LOGIN_URL)
                    .tag(this)
                    .params("account", account)
                    .params("password", password)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.body().toString());
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    LOGINED = 1;
                                    Toasty.success(MainActivity.this, "登录成功", Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
    //endregion


}