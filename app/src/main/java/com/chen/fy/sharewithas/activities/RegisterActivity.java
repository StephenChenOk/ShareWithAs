package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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

    }
}
