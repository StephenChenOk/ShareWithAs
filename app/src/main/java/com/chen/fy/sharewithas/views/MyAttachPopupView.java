package com.chen.fy.sharewithas.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.asynctasks.DoLikeTask;
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.lxj.xpopup.core.HorizontalAttachPopupView;


/**
 * 仿微信朋友圈点赞评论布局
 */
@SuppressLint("ViewConstructor")
public class MyAttachPopupView extends HorizontalAttachPopupView implements View.OnClickListener {

    private Context mContext;
    private ShareInfo shareInfo;

    private ImageView ivLikes;
    private TextView tvLikes;

    private boolean isLikes;

    private Handler handler;

    public MyAttachPopupView(@NonNull Context context, ShareInfo shareInfo, boolean isLikes, Handler handler) {
        super(context);
        this.mContext = context;
        this.shareInfo = shareInfo;
        this.isLikes = isLikes;
        this.handler = handler;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.attach_popup_view_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void initView() {
        ivLikes = findViewById(R.id.iv_likes);
        tvLikes = findViewById(R.id.tv_do_likes);

        if (isLikes) {  //已经点赞过了
            Glide.with(mContext).load(R.drawable.ic_favorite_red_16dp).into(ivLikes);
            tvLikes.setText("取消");
        } else {
            Glide.with(mContext).load(R.drawable.ic_favorite_border_white_16dp).into(ivLikes);
            tvLikes.setText("点赞");
        }

        ivLikes.setOnClickListener(this);
        tvLikes.setOnClickListener(this);
        findViewById(R.id.iv_comment).setOnClickListener(this);
        findViewById(R.id.tv_comment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_likes:      //点赞
            case R.id.tv_do_likes:
                if (HomeFragment.isLogin) {
                    if (isLikes) {    //已经点赞过了
                        DoLikeTask doLikeTask = new DoLikeTask((Activity) mContext, shareInfo, handler);
                        doLikeTask.execute("unlike");
                    } else {
                        DoLikeTask doLikeTask = new DoLikeTask((Activity) mContext, shareInfo, handler);
                        doLikeTask.execute("like");
                    }
                } else {
                    Toast.makeText(mContext, "请先登录再进行此操作", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;

            case R.id.iv_comment:      //评论
            case R.id.tv_comment:
                dismiss();
                break;
        }
    }
}

