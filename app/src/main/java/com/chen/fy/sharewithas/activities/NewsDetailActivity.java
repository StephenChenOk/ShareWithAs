package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.utils.UiUtils;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView wvDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);

        UiUtils.changeStatusBarTextImgColor(this, true);

        initView();
        initData();
    }

    private void initView() {
        wvDetail = findViewById(R.id.wv_news_detail);
        ImageView ivReturn = findViewById(R.id.iv_return_news_detail);

        ivReturn.setOnClickListener(this);
    }

    private void initData() {
        if (getIntent() != null) {
            //自己生成一个浏览器页面，而不是打开默认浏览器
            wvDetail.setWebViewClient(new WebViewClient());
            //加载网页
            wvDetail.loadUrl(getIntent().getStringExtra(Constants.NEWS_DETAIL_URL_KEY));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvDetail != null) {
            wvDetail.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_news_detail:
                finish();
                break;
        }
    }
}
