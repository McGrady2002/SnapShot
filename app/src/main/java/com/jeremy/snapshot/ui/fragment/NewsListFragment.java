/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：NewsListFragment.java
 * 包名：com.jeremy.snapshot.ui.fragment.NewsListFragment
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月28日 11:56:26
 */

package com.jeremy.snapshot.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremy.snapshot.network.entity.Bean;
import com.jeremy.snapshot.adapter.NewsAdapter;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment {

    private List<Bean.News> newsList = new ArrayList<>();

    private NewsAdapter newsAdapter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int index=0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
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
        View view=inflater.inflate(R.layout.fragment_news_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_recycler);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        init();
        return view;
    }

    private void initNews() {
        OkGo.<String>get(HttpTool.NEWS_URL)
                .tag(this)
                .cacheKey("newsFragment")
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //UI线程
                        parseGson(response.body());
                        newsAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        onSuccess(response);
                        Toasty.warning(getContext(),"网络连接失败，显示缓存数据").show();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        Toasty.error(getContext(),"网络错误,读取缓存失败").show();
                    }
                });
    }

    private void parseGson(String s) {
        //Log.d("MainActivity",s);
        Gson gson = new Gson();
        Type type = new TypeToken<Bean>() {
        }.getType();
        Bean bean = gson.fromJson(s, type);
        newsList.clear();
        newsList.addAll(bean.data);
        for (Bean.News news : newsList) {
            Log.d("NewsActivity", news.getDesc());
        }
    }

    private void init(){
        Log.d("NewsActivity", "aaa");
        newsAdapter = new NewsAdapter(newsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
        initNews();
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("NewsActivity","onRefresh");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        refresh();
                    }
                }).start();
            }
        });
    }

    private void refresh(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsList.removeAll(newsList);
                initNews();
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}