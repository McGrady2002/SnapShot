/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：AppraiseActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.AppraiseActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */


package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppraiseActivity extends AppCompatActivity {

    public static long currentId;  //当前点击的记录ID

    private RatingBar ratingBar_result;
    private RatingBar ratingBar_velocity;
    private EditText commend_text;
    private SubmitButton submitButton;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        init();
    }

    //region 初始化控件
    private void init() {
        TextView title = (TextView) findViewById(R.id.feed_back_title);
        title.setText("发表评价");
        ratingBar_result = (RatingBar) findViewById(R.id.ratingBar_result);
        ratingBar_velocity = (RatingBar) findViewById(R.id.ratingBar_velocity);
        commend_text = (EditText) findViewById(R.id.appraise_text);
        submitButton = (SubmitButton) findViewById(R.id.btn_sub_feedback);
        back = (ImageView) findViewById(R.id.fb_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvaluate();
            }
        });
    }
    //endregion

    //region 发表评价的网络请求
    private void postEvaluate() {
        MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        try {
            json.put("id", null);
            json.put("feedBackId", currentId);
            json.put("ratingResult", (int) ratingBar_result.getRating());
            json.put("ratingSpeed", (int) ratingBar_velocity.getRating());
            json.put("commend", commend_text.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        OkGo.<String>post(HttpTool.EVALUATE_URL)
                .tag(this)
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                showResponse("评价成功", 1);
                            } else {
                                showResponse("评价失败", 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        showResponse("评价失败，请检查网络", 0);
                    }
                });
    }
    //endregion

    //region 相应UI响应
    private void showResponse(String str, int i) {
                if (i == 1) {
                    submitButton.doResult(true);
                    Toasty.success(AppraiseActivity.this, str, Toasty.LENGTH_SHORT).show();
                } else
                    Toasty.error(AppraiseActivity.this, str, Toasty.LENGTH_SHORT).show();
            }
    //endregion

}