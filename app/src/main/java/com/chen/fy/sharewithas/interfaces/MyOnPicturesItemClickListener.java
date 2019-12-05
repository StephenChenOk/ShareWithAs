package com.chen.fy.sharewithas.interfaces;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chen.fy.sharewithas.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * 分享动态中的图片点击类
 */
public class MyOnPicturesItemClickListener implements OnPicturesItemClickListener {

    @Override
    public void onPicturesItemClick(final RelativeLayout relativeLayout, int position, ArrayList<Object> list) {
        //1 获取要显示的图片对象
        final ImageView imageView = (ImageView) relativeLayout.getChildAt(position);
        //2 使用XPopup开始显示放大后的图片
        new XPopup.Builder(relativeLayout.getContext()).asImageViewer(imageView, position, list, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                //3 放大效果时滑动后显示的图片
                GridView gridView = (GridView) relativeLayout.getParent();
                popupView.updateSrcView((ImageView) ((RelativeLayout) gridView.getChildAt(position)).getChildAt(0));
            }
        }, new ImageLoader())
                .show();
    }

    /**
     * 加载放大后的图片
     */
    public static class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

