package com.chen.fy.sharewithas.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public interface OnPicturesItemClickListener {

    /**
     * 分享图片时点击放大图片
     * @param relativeLayout     GridView的子布局的最外层布局
     * @param position          子布局所在位置
     * @param list              图片集合
     */
    void onPicturesItemClick(RelativeLayout relativeLayout, int position, ArrayList<Object> list);

}
