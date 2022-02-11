/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：HistoryRecordActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.HistoryRecordActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */


package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.adapter.RecordAdapter;
import com.jeremy.snapshot.network.entity.Bean;
import com.jeremy.snapshot.network.entity.Record;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryRecordActivity extends AppCompatActivity {

    private RecyclerView records_recycler;
    private ImageView back;
    private RecordAdapter recordAdapter;
    private Record record = new Record();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record);
        init();
    }

    //region 初始化控件
    private void init() {
        records_recycler = (RecyclerView) findViewById(R.id.recycler_record);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recordAdapter = new RecordAdapter(record);
        records_recycler.setAdapter(recordAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        records_recycler.setLayoutManager(layoutManager);
        record.setData(new ArrayList<>());
        getRecord();
    }
    //endregion

    //region 网络请求
    private void getRecord() {
        OkGo.<String>get(HttpTool.FBRECORD_URL)
                .tag(this)
                .params("account", MainActivity.account)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        parseGson(response.body());
                    }
                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toasty.error(HistoryRecordActivity.this,"网络错误",Toasty.LENGTH_SHORT).show();
                    }
                });
    }
    //endregion

    //region 处理返回结果
    private void parseGson(String s) {
        Gson gson = new Gson();
        Type type = new TypeToken<Record>() {
        }.getType();
        Record record_index = gson.fromJson(s, type);
        record.getData().addAll(record_index.getData());
        recordAdapter.notifyDataSetChanged();
        for (int i = 0; i < record.getData().size(); i++) {
            Log.d("recordtest", record.getData().get(i).getTitle());
        }
    }
    //endregion

}