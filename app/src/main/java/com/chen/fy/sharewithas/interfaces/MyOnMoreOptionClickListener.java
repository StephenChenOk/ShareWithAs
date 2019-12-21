package com.chen.fy.sharewithas.interfaces;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.views.MyAttachPopupView;
import com.lxj.xpopup.XPopup;


/**
 * 动态更多选项的点击事件
 */
public class MyOnMoreOptionClickListener implements OnMoreOptionClickListener {

    private Context mContext;
    private Handler handler;

    public MyOnMoreOptionClickListener(Context context, Handler handler) {
        this.mContext = context;
        this.handler = handler;
    }

    @Override
    public void onclick(int position, View view, ShareInfo shareInfo, boolean isLikes) {
        new XPopup.Builder(mContext)
                .offsetX(-10) //往左偏移10
//                        .offsetY(10)  //往下偏移10
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                .hasShadowBg(false) // 去掉半透明背景
                .atView(view)
                .asCustom(new MyAttachPopupView(mContext, shareInfo, isLikes,handler))
                .show();
    }
}
