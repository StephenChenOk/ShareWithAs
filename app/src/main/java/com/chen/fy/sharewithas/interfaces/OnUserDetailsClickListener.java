package com.chen.fy.sharewithas.interfaces;

import android.content.Context;
import android.content.Intent;

import com.chen.fy.sharewithas.activities.UserDetailsActivity;
import com.chen.fy.sharewithas.model.ShareInfo;

/**
 * 点击用户头像和名称的点击事件
 */
public class OnUserDetailsClickListener implements OnShareItemClickListener {

    private Context mContext;

    public OnUserDetailsClickListener(Context context) {
        mContext = context;
    }

    @Override
    public void onItemClick(ShareInfo shareInfo, int position) {
        Intent intent = new Intent(mContext, UserDetailsActivity.class);
        intent.putExtra("userName",shareInfo.getUserName());
        intent.putExtra("headUrl",shareInfo.getHeadUrl());
        mContext.startActivity(intent);
    }
}
