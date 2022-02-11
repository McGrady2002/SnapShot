/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：RegisterActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.RegisterActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */

package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private String account;

    private String password;

    private String confirmPw;

    private EditText accountEdit;

    private EditText passwordEdit;

    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        accountEdit = (EditText) findViewById(R.id.reg_account);
        passwordEdit = (EditText) findViewById(R.id.reg_password);
        confirmPassword = (EditText) findViewById(R.id.reg_confirm_pw);
        Button regButton = (Button) findViewById(R.id.btn_register);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account=accountEdit.getText().toString();
                password=passwordEdit.getText().toString();
                confirmPw=confirmPassword.getText().toString();
                if(password.equals(confirmPw)){
                    register();
                    Log.d("TAG","进入注册");
                }
                else {
                    Toasty.warning(RegisterActivity.this,"两次输入密码不一致！",Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    //region 注册的网络请求
    private void register() {
        OkGo.<String>get(HttpTool.SAVE_URL)
                .tag(this)
                .params("account", account)
                .params("password", password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        //region 根据返回数据判断是否注册成功
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            String message=jsonObject.getString("message");
                            if (code == 200) {
                                showResponse("注册成功",1);
                            } else {
                                showResponse(message,0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //endregion
                    }
                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toasty.error(RegisterActivity.this, "网络错误，请检查网络连接", Toasty.LENGTH_SHORT).show();
                    }
                });
    }
    //endregion

    //region ui更新
    private void showResponse(String str,int i){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i==1) {
                    Toasty.success(RegisterActivity.this, str, Toasty.LENGTH_SHORT).show();
                    new AlertView("注册成功", null, null, null,
                            new String[]{"返回登录界面", "立即登录"},
                            RegisterActivity.this, AlertView.Style.Alert, new OnItemClickListener(){
                        public void onItemClick(Object o,int position){
                            switch (position){
                                case 0:
                                    finish();
                                    break;
                                case 1:
                                    MainActivity.account=account;
                                    MainActivity.LOGINED=1;
                                    Toasty.success(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT, true).show();
                                    Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                            }
                        }
                    }).show();
                }else if (i==0)
                    Toasty.error(RegisterActivity.this,str,Toasty.LENGTH_SHORT).show();
            }
        });
    }
    //endregion
}