package com.chen.fy.sharewithas.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.adapters.MultipleStatesAdapter;
import com.chen.fy.sharewithas.adapters.MyViewPagerAdapter;
import com.chen.fy.sharewithas.beans.ShareInfo;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static ViewPager viewPager;
    private LinearLayout pointBox;
    private TextView title;
    private TextView search_tv;

    private RecyclerView recyclerView;

    private ArrayList<ShareInfo> shareInfos;

    //图片集合
    public static ArrayList<ImageView> images;

    //图片id
    private int[] imagesId = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
    };
    //图片标题
    public static String[] imagesTitle = {
            "img1",
            "img2",
            "img3",
            "img4",
            "img5"
    };

    //上一个被选中图片的位置
    private int prePosition = 0;
    //判断当前页面是否是滑动状态,解决当页面手动滑动后不能继续进行自动滑动
    private boolean isScroll = false;

    /**
     * 让图片自己动起来,采用异步handel,因为在Thread中不可以进行UI操作,所有可以用handel实行异步UI操作
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(item);

            //延迟发消息
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        //设置手机应用内部状态栏字体图标为白色
        //changeStatusBarTextImgColor(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        //1 设置顶部的自动轮转图
        viewPager.setAdapter(new MyViewPagerAdapter(getContext()));
        viewPager.addOnPageChangeListener(this);
        //设置当前页面在中间位置,保证可以实现左右滑动的效果
        viewPager.setCurrentItem(250);           //要保证是实际图片数量的整数倍,也就是保证每次进入都是先显示的第一张图片
        title.setText(imagesTitle[prePosition]);
        //第一次进入时延迟发消息
        handler.sendEmptyMessageDelayed(0, 3000);

    }

    private void initView(@NonNull View view) {
        //寻找控件
        viewPager = view.findViewById(R.id.viewpager_home);
        title = view.findViewById(R.id.title_viewpager_home);
        pointBox = view.findViewById(R.id.point_box_home);
        search_tv = view.findViewById(R.id.search_home);
        recyclerView = view.findViewById(R.id.rv_home);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        recyclerView.setLayoutManager(layoutManager);

        //设置点击事件
//        search_tv.setOnClickListener(this);
//        weather_tv.setOnClickListener(this);
//        translation_home_tv.setOnClickListener(this);
//        wc_tv.setOnClickListener(this);
    }

    private void initData() {
        //1 获取轮播图的图片和点
        images = new ArrayList<>();
        for (int i = 0; i < imagesId.length; i++) {
            //添加图片
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imagesId[i]);
            if (getActivity() != null) {
                Glide.with(getActivity()).load(imagesId[i]).into(imageView);
            }
            images.add(imageView);
            //添加点
            ImageView point = new ImageView(getContext());
            Glide.with(getActivity()).load(R.drawable.point_selctor).into(point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25); //自定义一个布局
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 40;    //距离左边的间距
            }
            point.setLayoutParams(params);   //设置格式,间距等
            pointBox.addView(point);
        }

        if (shareInfos == null) {
            shareInfos = new ArrayList<>();
        }
        if (!shareInfos.isEmpty()) {
            shareInfos.clear();
        }
        ShareInfo shareInfo1 = new ShareInfo();
        shareInfo1.setType(1);
        shareInfo1.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
        shareInfo1.setName("一一");
        shareInfo1.setContent("第一个布局");
        shareInfos.add(shareInfo1);

        ShareInfo shareInfo2 = new ShareInfo();
        shareInfo2.setType(2);
        shareInfo2.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
        shareInfo2.setName("二二");
        shareInfo2.setContent("第二个布局第二个布局第二个布局第二个布局第二个布局");
        shareInfo2.setPicture1(BitmapFactory.decodeResource(getResources(), R.drawable.img1));
        shareInfos.add(shareInfo2);

        ShareInfo shareInfo3 = new ShareInfo();
        shareInfo3.setType(3);
        shareInfo3.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
        shareInfo3.setName("三三");
        shareInfo3.setContent("第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局");
        shareInfo3.setPicture1(BitmapFactory.decodeResource(getResources(), R.drawable.img2));
        shareInfo3.setPicture2(BitmapFactory.decodeResource(getResources(), R.drawable.img3));
        shareInfo3.setPicture3(BitmapFactory.decodeResource(getResources(), R.drawable.img4));
        shareInfos.add(shareInfo3);

        MultipleStatesAdapter adapter = new MultipleStatesAdapter(getActivity(), shareInfos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 当页面滚动时回调这个方法
     *
     * @param i  当前页面的位置
     * @param v  滑动页面的百分比
     * @param i1 在屏幕上滑动的像素
     */
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    /**
     * 当前页面被选中时回调
     *
     * @param i 被选中页面的位置
     */
    @Override
    public void onPageSelected(int i) {
        //设置位置
        title.setText(imagesTitle[i % images.size()]);
        //把上一个高亮位置设置为灰色
        pointBox.getChildAt(prePosition).setEnabled(false);
        //当前位置设置为高亮
        pointBox.getChildAt(i % images.size()).setEnabled(true);
        prePosition = i % images.size();
    }

    /**
     * 当页面状态改变时回调
     * 静止-滚动
     * 滚动-静止
     * 静止-拖拽
     *
     * @param i 当前状态
     */
    @Override
    public void onPageScrollStateChanged(int i) {
        //判断当前页面是否是滑动状态,解决当页面手动滑动后不能继续进行自动滑动
        if (i == ViewPager.SCROLL_STATE_DRAGGING) {   //拖拽状态
            isScroll = true;
        } else if (i == ViewPager.SCROLL_STATE_SETTLING) {  //滑动状态

        } else if (i == ViewPager.SCROLL_STATE_IDLE) {   //静止状态
            isScroll = false;
            handler.removeCallbacksAndMessages(null);   //清除消息
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }
}
