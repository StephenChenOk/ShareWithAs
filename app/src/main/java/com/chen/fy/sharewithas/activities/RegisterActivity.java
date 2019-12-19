package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.beans.User;
import com.chen.fy.sharewithas.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity.Log";
    private EditText etUsername;
    private EditText etPwd;
    private EditText etPwd2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        initView();

    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        etPwd2 = findViewById(R.id.et_pwd_again);
        Button btnRegister = findViewById(R.id.btn_register);
        ImageView ivReturn = findViewById(R.id.iv_return_register);
        TextView tvReturn = findViewById(R.id.tv_return);

        btnRegister.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
        tvReturn.setOnClickListener(this);

        setEditTextInhibitInputSpace(etUsername);
        setEditTextInhibitInputSpace(etPwd);
        setEditTextInhibitInputSpace(etPwd2);
    }

    /**
     * 禁止EditText输入空格
     */
    private void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                requestRegister();
                break;
            case R.id.iv_return_register:
                finish();
                break;
            case R.id.tv_return:
                finish();
                break;
        }
    }

    /**
     * 请求注册
     */
    private void requestRegister() {
        String username = etUsername.getText().toString();
        String password1 = etPwd.getText().toString();
        String password2 = etPwd2.getText().toString();
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password1.equals(password2)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("account", username);
        map.put("password", password1);
        map.put("userName", "后起之秀");
        map.put("phoneNumber", "");

        final Gson gson = new Gson();
        String postData = gson.toJson(map);
        Log.d(TAG, postData);

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postData);
        Request request = new Request.Builder()
                .url(Constants.USER_SERVER_URL)
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

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonParser parser = new JsonParser();//Json解析
                final JsonObject jsonObject = (JsonObject) parser.parse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
