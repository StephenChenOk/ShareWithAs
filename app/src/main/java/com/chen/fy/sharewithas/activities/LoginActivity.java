package com.chen.fy.sharewithas.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chen.fy.sharewithas.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private EditText etUsername;
    private EditText etPwd;
    private Button btnLogin;
    private TextView btnRegister;

    private boolean bPwdSwitch = false;
    private ImageView ivPwdSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initView();

        applyPermission();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_login_to_register);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ivPwdSwitch.setOnClickListener(this);
    }

    /**
     * 请求登录
     */
    private void requestLogin() {
        String username = etUsername.getText().toString();
        String password = etPwd.getText().toString();

    }

    /**
     * 动态申请危险权限
     */
    private void applyPermission() {
        //权限集合
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {  //如果有权限没有被授权,则请求权限
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this,
                    permissions, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(LoginActivity.this, "必须同意所有权限才可以使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                requestLogin();
                break;
            case R.id.btn_login_to_register:
                Intent intent2 = new Intent(this,RegisterActivity.class);
                startActivity(intent2);
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
