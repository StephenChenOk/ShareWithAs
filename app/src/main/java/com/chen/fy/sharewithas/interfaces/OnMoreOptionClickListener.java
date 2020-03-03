package com.chen.fy.sharewithas.interfaces;

import android.view.View;

import com.chen.fy.sharewithas.model.ShareInfo;

/**
 * 动态中更多选项的点击事件
 */
public interface OnMoreOptionClickListener {

    void onclick(int position, View view, ShareInfo shareInfo,boolean isLikes);

}
