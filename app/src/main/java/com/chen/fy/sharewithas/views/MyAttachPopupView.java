package com.chen.fy.sharewithas.views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chen.fy.sharewithas.R;
import com.lxj.xpopup.core.HorizontalAttachPopupView;

/**
 * 仿微信朋友圈点赞评论布局
 */
public class MyAttachPopupView extends HorizontalAttachPopupView {
    private Context mContext;

    public MyAttachPopupView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.attach_popup_view_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "赞", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        findViewById(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "评论", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }
}

