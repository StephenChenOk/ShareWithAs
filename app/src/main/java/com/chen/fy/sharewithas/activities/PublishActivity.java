package com.chen.fy.sharewithas.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.adapters.PublishGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent;
    private GridView gvPictures;

    private ArrayList<String> mUriList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);

        initView();
        initData();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_publish);
        Button ivPublish = findViewById(R.id.btn_publish);
        tvContent = findViewById(R.id.tv_content_publish);
        gvPictures = findViewById(R.id.gv_box_public);

        ivReturn.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
    }

    public void initData() {
        if (mUriList == null) {
            mUriList = new ArrayList<>();
        }
        if (!mUriList.isEmpty()) {
            mUriList.clear();
        }
        int count;
        if (getIntent() != null) {
            count = getIntent().getIntExtra("ImagesSize", 0);
            for (int i = 0; i < count; i++) {
                mUriList.add(getIntent().getStringExtra("ImagesURI" + i));
            }
            if (count != 9) {
                mUriList.add(String.valueOf(R.drawable.ic_add_black_72dp));
            }
        }
        PublishGridViewAdapter adapter = new PublishGridViewAdapter(this);
        adapter.setUris(mUriList);
        gvPictures.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_publish:
                finish();
                break;
            case R.id.btn_publish:

                toast("发表");
                break;
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
