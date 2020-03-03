package com.chen.fy.sharewithas.interfaces;

import com.chen.fy.sharewithas.model.ShareInfo;

/**
 * RecyclerView点击事件
 */
public interface OnShareItemClickListener {

    void onItemClick(ShareInfo shareInfo, int position);

}
