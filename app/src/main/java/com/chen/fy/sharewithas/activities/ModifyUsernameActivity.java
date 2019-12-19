package com.chen.fy.sharewithas.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.utils.UiUtils;
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

public class ModifyUsernameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UsernameActivity";
    private EditText etUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_username_layout);

        UiUtils.changeStatusBarTextImgColor(this, true);
        initView();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_modify_username);
        TextView tvSave = findViewById(R.id.tv_save_username);
        etUsername = findViewById(R.id.et_modify_name);

        ivReturn.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_modify_username:
                finish();
                break;
            case R.id.tv_save_username:
                saveUsername();
                break;
        }
    }

    /**
     * 更改昵称
     */
    private void saveUsername() {
        String userName = etUsername.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //1 更新服务器数据
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName_old = sharedPreferences.getString("userName", "");
        if (userName.equals(userName_old)) {   //只有真正更好时才放松请求
            Toast.makeText(this, "昵称已相同", Toast.LENGTH_SHORT).show();
        }else {
            updateServerData(sharedPreferences, userName);
        }

        //2 更新本地数据SharedPreferences表
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    private void updateServerData(SharedPreferences sharedPreferences, String userName) {
        int id = sharedPreferences.getInt("id", -1);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userName", userName);
        map.put("phoneNumber", phoneNumber);

        final Gson gson = new Gson();
        String postData = gson.toJson(map);
        Log.d(TAG, postData);

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postData);
        Request request = new Request.Builder()
                .url(Constants.USER_SERVER_URL)
                .put(requestBody)
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
                JsonParser parser = new JsonParser();//Json解析
                final JsonObject jsonObject = (JsonObject) parser.parse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyUsernameActivity.this, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
