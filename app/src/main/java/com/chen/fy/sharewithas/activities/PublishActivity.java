package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent;
    private ImageView ivPicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);

        initView();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_publish);
        Button ivPublish = findViewById(R.id.btn_publish);
        tvContent = findViewById(R.id.tv_content_publish);
        ivPicture = findViewById(R.id.iv_picture_publish);

        ivReturn.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_publish:
                finish();
                break;
            case R.id.btn_publish:
                finish();
                toast("发表");
                break;
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
