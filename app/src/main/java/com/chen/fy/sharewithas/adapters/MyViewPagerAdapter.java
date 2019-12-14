package com.chen.fy.sharewithas.adapters;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chen.fy.sharewithas.fragments.HomeFragment;

public class MyViewPagerAdapter extends PagerAdapter {

    private Handler mHandler;

    public MyViewPagerAdapter(Handler handler){
        this.mHandler = handler;
    }

    /**
     * @return 图片总数
     */
    @Override
    public int getCount() {
        return 500;            //实现左右无限滑动   数值可以任意调动
    }

    /**
     * 比较两个页面是否为同一个
     *
     * @param view 当前页面
     * @param o    instantiateItem方法返回的页面
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * 初始化页面
     *
     * @param container viewPager自身
     * @param position  当前实例化页面位置
     * @return 返回初始化好的页面
     */
    @NonNull
    @Override
    @SuppressLint("ClickableViewAccessibility")   //抑制点击事件的警告，因为重写onTouch方法时有可能会和onClick事件冲突
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ImageView imageView = HomeFragment.imageList.get(position % HomeFragment.imageList.size());

        //设置触摸事件,当用户点击了页面时,页面不再自己往后动,应该做停留
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   //手指按下
                        mHandler.removeMessages(HomeFragment.BANNER_CODE);
                        break;
                    case MotionEvent.ACTION_MOVE:   //移动
                        break;
                    case MotionEvent.ACTION_UP:     //离开
                        mHandler.sendEmptyMessageDelayed(HomeFragment.BANNER_CODE, 3000);
                        break;
                }

                return false;    //返回false表示没有消费该事件,事件继续往下传递,onClick等事件仍然可以进行响应
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if (imageView.getParent() != null) {
            ((ViewPager) imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);   //把初始化好的页面加入到viewPager中
        return imageView;
    }

    /**
     * 释放资源
     *
     * @param container viewpager本身
     * @param position  要释放的位置
     * @param object    要释放的页面
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
