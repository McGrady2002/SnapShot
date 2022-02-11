/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：FbProgressActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.FbProgressActivity
 * 当前修改时间：2021年11月29日 00:01:41
 * 上次修改时间：2021年10月16日 17:31:11
 */

package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.entity.Process;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FbProgressActivity extends AppCompatActivity {

    private Process process;
    private ImageView back;

    private View line1;
    private View line2;
    private View line3;
    private ImageView commit;
    private ImageView wait;
    private ImageView handle;
    private ImageView finish;
    private TextView title_commit;
    private TextView time_commit;
    private TextView title_wait;
    private TextView time_wait;
    private TextView title_handle;
    private TextView time_handle;
    private TextView title_finish;
    private TextView time_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_progress);
        init();
    }

    //region 初始化控件
    private void init() {
        process = new Process();
        TextView title = (TextView) findViewById(R.id.back_title);
        line1 = (View) findViewById(R.id.line_1);
        line2 = (View) findViewById(R.id.line_2);
        line3 = (View) findViewById(R.id.line_3);
        commit = (ImageView) findViewById(R.id.commit_image);
        wait = (ImageView) findViewById(R.id.wait_image);
        handle = (ImageView) findViewById(R.id.handle_image);
        finish = (ImageView) findViewById(R.id.finish_image);
        title_commit = (TextView) findViewById(R.id.title_commit);
        time_commit = (TextView) findViewById(R.id.time_commit);
        title_wait = (TextView) findViewById(R.id.title_wait);
        time_wait = (TextView) findViewById(R.id.time_wait);
        title_handle = (TextView) findViewById(R.id.title_processing);
        time_handle = (TextView) findViewById(R.id.time_processing);
        title_finish = (TextView) findViewById(R.id.title_finish);
        time_finish = (TextView) findViewById(R.id.time_wait);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("查看进度");
        getProcess();
    }
    //endregion

    //region 网络请求
    private void getProcess() {
        OkGo.<String>get(HttpTool.PROCESS_URL)
                .tag(this)
                .params("feed_back_id", String.valueOf(AppraiseActivity.currentId))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Process>() {
                        }.getType();
                        Process p = new Process();
                        p = gson.fromJson(response.body(), type);
                        process.getData().addAll(p.getData());
                        for (int i = 0; i < process.getData().size(); i++)
                            handle(i + 1);
                    }
                });
        //<editor-fold desc="null">
        //        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request.Builder reqBuild = new Request.Builder();
//                    HttpUrl.Builder urlBuilder = HttpUrl.parse(HttpTool.PROCESS_URL)
//                            .newBuilder();
//                    urlBuilder.addQueryParameter("feed_back_id", String.valueOf(AppraiseActivity.currentId));
//                    reqBuild.url(urlBuilder.build());
//                    Request request = reqBuild.build();
//                    Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                            Log.d("TAG", "失败");
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                            //Log.d("TAGS",response.body().string());
//                            Gson gson = new Gson();
//                            Type type = new TypeToken<Process>() {
//                            }.getType();
//                            Process p = new Process();
//                            p = gson.fromJson(response.body().string(), type);
//                            process.getData().addAll(p.getData());
//                            updateUI();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        //</editor-fold>
    }
    //endregion

    //region 根据返回结果更新UI
    private void handle(int i) {
        switch (i) {
            case 1:  //提交
                Log.d("FBTEST", "1111");
                commit.setVisibility(View.VISIBLE);
                title_commit.setVisibility(View.VISIBLE);
                time_commit.setText(process.getData().get(0).getTime());
                time_commit.setVisibility(View.VISIBLE);
                break;
            case 2: //等待处理
                line1.setVisibility(View.VISIBLE);
                wait.setVisibility(View.VISIBLE);
                title_wait.setVisibility(View.VISIBLE);
                time_wait.setVisibility(View.VISIBLE);
                time_wait.setText(process.getData().get(1).getTime());
                Log.d("FBTEST", "2");
                break;
            case 3:  //处理中
                line2.setVisibility(View.VISIBLE);
                handle.setVisibility(View.VISIBLE);
                title_handle.setVisibility(View.VISIBLE);
                time_handle.setVisibility(View.VISIBLE);
                time_handle.setText(process.getData().get(2).getTime());
                Log.d("FBTEST", "3");
                break;
            case 4:  //处理完成
                line3.setVisibility(View.VISIBLE);
                finish.setVisibility(View.VISIBLE);
                title_finish.setVisibility(View.VISIBLE);
                time_finish.setVisibility(View.VISIBLE);
                time_finish.setText(process.getData().get(3).getTime());
                break;
            default:
                break;
        }
    }
    //endregion

}