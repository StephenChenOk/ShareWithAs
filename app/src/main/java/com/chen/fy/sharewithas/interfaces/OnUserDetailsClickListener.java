package com.chen.fy.sharewithas.interfaces;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chen.fy.sharewithas.activities.UserDetailsActivity;

/**
 * 点击用户头像和名称的点击事件
 */
public class OnUserDetailsClickListener implements OnItemClickListener {

    private Context mContext;

    public OnUserDetailsClickListener(Context context) {
        mContext = context;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mContext, UserDetailsActivity.class);
        mContext.startActivity(intent);
    }
}
