/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：LoginActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.LoginActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */


package com.jeremy.snapshot.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.snapshot.Base64Util;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.jeremy.snapshot.sqlite.MyDateBaseHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox remember;
    private CheckBox autoLogin;

    private MyDateBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        setContentView(R.layout.login_main);
        Button btnLog = (Button) findViewById(R.id.btn_log);
        TextView registerText = (TextView) findViewById(R.id.register);
        btnLog.setOnClickListener(this);
        registerText.setOnClickListener(this);
        accountEdit = (EditText) findViewById(R.id.log_account);
        passwordEdit = (EditText) findViewById(R.id.log_password);
        dbHelper = new MyDateBaseHelper(this, "SnapShot.db", null, 2);
        remember = (CheckBox) findViewById(R.id.rememberPw);
        autoLogin = (CheckBox) findViewById(R.id.auto_login);
        autoLogin.setOnClickListener(this);
        //query(); //sqlite
        getAccount(); //sp
    }

    //region Description
//    private void init() {
//        userName =
//
//                findViewById(R.id.user_name);
//
//        userPassword =
//
//                findViewById(R.id.user_password);
//
//        loginButton =
//
//                findViewById(R.id.login_button);
//
//        registerButton =
//
//                findViewById(R.id.register_button);
//
//        // 按钮添加监听器
//        loginButton.setOnClickListener(new
//
//                LoginButtonListener());
//        registerButton.setOnClickListener(new
//
//                RegisterButtonListener());
//    }
//
//    class LoginButtonListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            String name = userName.getText().toString();
//            String pssword = userPassword.getText().toString();
//
//            // 判断账号密码是否一致
//            // if (...)
//            // 进入主界面
//
//            // 如果不是，提示错误信息
//            // else
//            Toast.makeText(LoginActivity.this, "账号或密码错误!", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    class RegisterButtonListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            // 打开注册界面
//            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(intent);
//        }
//    }
    //endregion

    //region onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log:
                if (accountEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals(""))
                    Toasty.warning(LoginActivity.this, "请输入账号和密码", Toasty.LENGTH_SHORT).show();
                else
                    login();
                break;
            case R.id.register:
                //我要注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.auto_login:
                SharedPreferences sp = getSharedPreferences("UserAccount",
                        Context.MODE_PRIVATE);    //保存模式
                sp.edit().putBoolean("auto", autoLogin.isChecked()).commit();
                boolean index = sp.getBoolean("auto", false);
                Log.d("Tracy", String.valueOf(index));
                break;
            default:
                break;
        }
    }
    //endregion

    //region 登录网络请求
    private void login() {
        OkGo.<String>get(HttpTool.LOGIN_URL)
                .tag(this)
                .params("account", accountEdit.getText().toString())
                .params("password", passwordEdit.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        //region 根据返回数据判断是否登录成功
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            if (code == 200) {
                                MainActivity.LOGINED = 1;
                                Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT, true).show();
                                MainActivity.account = accountEdit.getText().toString();
                                //sp
                                if (remember.isChecked()) {
                                    SavePassword();
                                }
                                finish();
                            } else if (code == 400) {
                                Toasty.error(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //endregion
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toasty.error(LoginActivity.this, "网络错误，请检查网络连接", Toasty.LENGTH_SHORT).show();
                    }
                });
    }
    //endregion

    //region SP方案+base64加密保存账号密码
    //保存账号密码并加密
    private void SavePassword() {
        SharedPreferences sp = getSharedPreferences("UserAccount",
                Context.MODE_PRIVATE);    //保存模式
        sp.edit().putString("account", accountEdit.getText().toString())
                .putString("password", Base64Util.encode(passwordEdit.getText().toString().getBytes()))
                .commit();
    }

    //提取账号密码
    private void getAccount() {
        try {
            SharedPreferences sp = getSharedPreferences("UserAccount", Context.MODE_PRIVATE);
            accountEdit.setText(sp.getString("account", null));  //取出账号
            String str = new String(Base64Util.decode(sp.getString("password", null)));//取出密码，此时的密码是经过Base64编码后的，需要解码
            passwordEdit.setText(str);
            if (!accountEdit.getText().toString().equals("") && !passwordEdit.getText().toString().equals("")) {
                remember.setChecked(true);
            }
            boolean index = sp.getBoolean("auto", false);
            if (index)
                autoLogin.setChecked(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region SQlite实现账号密码存取
    //插入数据
    private void insert(String account, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("password", password);
        db.insert("user", null, values);
        db.close();
    }

    //查找数据
    private void query() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                accountEdit.setText(cursor.getString(cursor.getColumnIndex("account")));
                passwordEdit.setText(cursor.getString(cursor.getColumnIndex("password")));
            }
        }
        cursor.close();
        db.close();
    }
    //endregion

}