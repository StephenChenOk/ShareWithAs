package com.chen.fy.sharewithas.activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity.Log";
    private EditText etUsername;
    private EditText etPwd;

    private boolean bPwdSwitch = false;
    private ImageView ivPwdSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initView();

    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView btnRegister = findViewById(R.id.btn_login_to_register);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ImageView ivReturn = findViewById(R.id.iv_return_login);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ivPwdSwitch.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
    }

    /**
     * 请求登录
     */
    private void requestLogin() {

        String username = etUsername.getText().toString();
        String password1 = etPwd.getText().toString();
        if (username.isEmpty() || password1.isEmpty()) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("account", username);
        map.put("password", password1);

        final Gson gson = new Gson();
        String postData = gson.toJson(map);
        Log.d(TAG, postData);

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postData);
        Request request = new Request.Builder()
                .url(Constants.LOGIN_SERVER_URL)
                .post(requestBody)
                .build();

        //创建Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "连接失败");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJSON(response);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)   //需要的API等级
    private void parseJSON(Response response) {
        JsonParser parser = new JsonParser();//Json解析
        try {
            //1 获取JsonObject对象
            JsonObject jsonObject = (JsonObject) parser.parse(response.body().string());
            Log.d(TAG, jsonObject.toString());

            //2 分别取出数据
            int id = Integer.parseInt(jsonObject.get("id").toString());

            String account1 = jsonObject.get("account").toString();
            String account = account1.substring(1,account1.length()-1);

            String password1 = jsonObject.get("password").toString();
            String password = password1.substring(1,password1.length()-1);

            String userName1 = jsonObject.get("userName").toString();
            String userName = userName1.substring(1,userName1.length()-1);

            String headUrl1 = jsonObject.get("headUrl").toString();
            String headUrl = headUrl1.substring(1,headUrl1.length()-1);

            String phoneNumber1 = jsonObject.get("phoneNumber").toString();
            String phoneNumber = phoneNumber1.substring(1,phoneNumber1.length()-1);

            Log.d("chenyisheng", id + "," + account + "," + password + "," + userName + "," + headUrl + "," + phoneNumber);

            //3 保存数据
            SharedPreferences.Editor editorUserInfo = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
            editorUserInfo.putInt("id", id);
            editorUserInfo.putString("account", account);
            editorUserInfo.putString("password", password);
            editorUserInfo.putString("userName", userName);
            editorUserInfo.putString("headUrl", headUrl);
            editorUserInfo.putString("phoneNumber", phoneNumber);
            editorUserInfo.apply();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                requestLogin();
                break;
            case R.id.btn_login_to_register:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_return_login:
                finish();
                break;
            case R.id.iv_pwd_switch:
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_on_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);   //显示密码
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_off_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);     //隐藏密码
                    etPwd.setTypeface(Typeface.DEFAULT);    //设置字体样式
                }
                break;
        }
    }
}
