package com.chen.fy.sharewithas.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;
import com.chen.fy.sharewithas.activities.PublishActivity;
import com.chen.fy.sharewithas.adapters.MultipleStatesAdapter;
import com.chen.fy.sharewithas.adapters.MyViewPagerAdapter;
import com.chen.fy.sharewithas.asynctasks.GetShareInfoTask;
import com.chen.fy.sharewithas.beans.ShareInfo;

import com.chen.fy.sharewithas.interfaces.MyOnPicturesItemClickListener;
import com.chen.fy.sharewithas.interfaces.OnMoreOptionClickListener;
import com.chen.fy.sharewithas.interfaces.OnUserDetailsClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.chen.fy.sharewithas.views.MyAttachPopupView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoFragment;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends TakePhotoFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Context mContext;

    private static ViewPager mViewPager;
    private LinearLayout mPointBox;
    private TextView tvTitle;
    private SmartRefreshLayout mRefreshLayout;

    private MultipleStatesAdapter mMultipleStatesAdapter;

    /**
     * 拍照控件
     */
    private TakePhoto mTakePhoto;

    /**
     * 图片剪切以及图片地址
     */
    private CropOptions mCropOptions;
    private Uri mUri;

    private RecyclerView mRecyclerView;

    private ArrayList<ShareInfo> mShareInfos;

    public static ArrayList<ImageView> imageList;
    //图片标题
    private String[] mTitles;

    //上一个被选中图片的位置
    private int mPrePosition = 0;

    //自动轮播
    public static final int BANNER_CODE = 0;
    //刷新
    private final int REFRESH_CODE = 1;
    //更新UI
    public static final int UPDATE_UI_CODE = 2;
    //发表动态
    private final int REQUEST_PUBLISH_CODE = 3;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BANNER_CODE:    //让图片自己动起来
                    int item = mViewPager.getCurrentItem() + 1;
                    mViewPager.setCurrentItem(item);
                    //自动轮播
                    // mHandler.sendEmptyMessageDelayed(BANNER_CODE, 3000);
                    break;
                case REFRESH_CODE:
                    mRefreshLayout.finishRefresh();
                    break;
                case UPDATE_UI_CODE:
                    if (mMultipleStatesAdapter == null) {
                        mMultipleStatesAdapter = new MultipleStatesAdapter(mContext);
                        mMultipleStatesAdapter.setShareDataList(mShareInfos);
                        mMultipleStatesAdapter.setItemClickListener(new MyOnPicturesItemClickListener());
                        mMultipleStatesAdapter.setMoreOptionClickListener(new MyOnMoreOptionClickListener());
                        mMultipleStatesAdapter.setUserDetailsClickListener(new OnUserDetailsClickListener(mContext));
                        mRecyclerView.setAdapter(mMultipleStatesAdapter);
                    }
                    mMultipleStatesAdapter.notifyDataSetChanged();

                    mHandler.sendEmptyMessageDelayed(REFRESH_CODE, 300);
                    break;
            }
        }
    };

    private boolean isFirstEnter = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getContext() != null) {
            mContext = getContext();
        } else {
            return;
        }

        initViewPager();
    }

    /**
     * Fragment在show与hide状态转换时调用此方法
     *
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getActivity() != null) {
            UiUtils.changeStatusBarTextImgColor(getActivity(), false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        initTakePhoto();
    }

    private void initView(@NonNull View view) {
        //寻找控件
        mViewPager = view.findViewById(R.id.viewpager_home);
        tvTitle = view.findViewById(R.id.title_viewpager_home);
        mPointBox = view.findViewById(R.id.point_box_home);
        TextView tvSearch = view.findViewById(R.id.search_home);
        mRecyclerView = view.findViewById(R.id.rv_home);
        ImageView ivTakePhotoLogo = view.findViewById(R.id.take_photo_logo_home);
        mRefreshLayout = view.findViewById(R.id.refreshLayout_home);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        mRecyclerView.setLayoutManager(layoutManager);

        //设置点击事件
        tvSearch.setOnClickListener(this);
        ivTakePhotoLogo.setOnClickListener(this);
        ivTakePhotoLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(),PublishActivity.class);
                startActivity(intent);
                return true;
            }
        });

        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新
        }

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initDataList();
                    }
                }).start();
            }
        });
    }

    private void initViewPager() {
        //1 获取图片和图片标题资源
        //图片
        TypedArray mImages = getResources().obtainTypedArray(R.array.images);
        mTitles = getResources().getStringArray(R.array.titles);
        imageList = new ArrayList<>();
        //2 设置顶部的自动轮转图
        mViewPager.setAdapter(new MyViewPagerAdapter(mHandler));
        //3 获取轮播图的图片和点
        for (int i = 0; i < mTitles.length; i++) {
            //添加图片
            ImageView imageView = new ImageView(getContext());
            if (mContext != null) {
                Glide.with(mContext).load(mImages.getResourceId(i, -1)).apply(new RequestOptions().centerCrop()).into(imageView);
            }
            imageList.add(imageView);
            //添加点
            ImageView point = new ImageView(getContext());
            point.setBackgroundResource(R.drawable.point_selctor);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20); //自定义一个布局
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 30;    //距离左边的间距
            }
            point.setLayoutParams(params);   //设置格式,间距等
            mPointBox.addView(point);
        }

        //4 设置当前页面在中间位置,保证可以实现左右滑动的效果
        mViewPager.setCurrentItem(250);           //要保证是实际图片数量的整数倍,也就是保证每次进入都是先显示的第一张图片
        tvTitle.setText(mTitles[mPrePosition]);
        //5 设置状态改变监听
        mViewPager.addOnPageChangeListener(this);

        //6 第一次进入时延迟发消息
        mHandler.sendEmptyMessageDelayed(BANNER_CODE, 3000);
    }

    private void initDataList() {
        if (mShareInfos == null) {
            mShareInfos = new ArrayList<>();
        }
        if (!mShareInfos.isEmpty()) {
            mShareInfos.clear();
        }

        getShareInfo();

        mHandler.sendEmptyMessage(UPDATE_UI_CODE);
    }

    private void getShareInfo() {
        GetShareInfoTask getShareInfoTask = new GetShareInfoTask(getActivity(), mHandler, mShareInfos);
        getShareInfoTask.execute();
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
        tvTitle.setText(mTitles[i % mTitles.length]);
        //把上一个高亮位置设置为灰色
        mPointBox.getChildAt(mPrePosition).setEnabled(false);
        //当前位置设置为高亮
        mPointBox.getChildAt(i % mTitles.length).setEnabled(true);
        mPrePosition = i % mTitles.length;
    }

    /**
     * 当页面状态改变时回调
     * 静止-滚动
     * 滚动-静止
     * 静止-拖拽
     * @param i 当前状态
     */
    @Override
    public void onPageScrollStateChanged(int i) {
        //判断当前页面是否是滑动状态,解决当页面手动滑动后不能继续进行自动滑动
        if (i == ViewPager.SCROLL_STATE_DRAGGING) {   //拖拽状态
        } else if (i == ViewPager.SCROLL_STATE_SETTLING) {  //滑动状态

        } else if (i == ViewPager.SCROLL_STATE_IDLE) {   //静止状态
            mHandler.removeMessages(BANNER_CODE);
            mHandler.sendEmptyMessageDelayed(BANNER_CODE, 3000);
        }
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        if (mTakePhoto == null) {
            Log.d("chensheng", "initTakePhoto");
            //获得对象
            mTakePhoto = getTakePhoto();
            //图片文件名
            String mImageName = "sharePhoto" + new Random(1).nextInt() + ".jpg";
            File file = new File(mContext.getExternalFilesDir(null), mImageName);
            mUri = Uri.fromFile(file);

            //进行图片剪切
            int size = Math.max(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
            mCropOptions = new CropOptions.Builder().setOutputX(size).setOutputY(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具

        }
        //进行图片压缩
        CompressConfig compressConfig = new CompressConfig.Builder().create();
//        //大小            像素
        //setMaxSize(1024).setMaxPixel(1024*1024).create();
//
        /*
         * 启用图片压缩
         * @param config 压缩图片配置
         * @param showCompressDialog 压缩时是否显示进度对话框
         */
        mTakePhoto.onEnableCompress(compressConfig, true);
    }


    /**
     * 获取照片成功后成功回调
     */
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        if (!result.getImages().isEmpty()) {
            Intent intent = new Intent(mContext, PublishActivity.class);
            intent.putExtra("ImagesSize", result.getImages().size());
            for (int i = 0; i < result.getImages().size(); i++) {
                intent.putExtra("ImagesURI" + i, result.getImages().get(i).getCompressPath());
            }
            startActivity(intent);
        } else {
            Log.d("HomeFragmentLog", "isEmpty!!!!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_logo_home:
                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .asCenterList("", new String[]{"拍照", "从相册选择"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        switch (position) {
                                            case 0:         //拍照
                                                mTakePhoto.onPickFromCaptureWithCrop(mUri, mCropOptions);

                                                break;
                                            case 1:         //相册
                                                //mTakePhoto.onPickFromGalleryWithCrop(mUri, mCropOptions);
                                                mTakePhoto.onPickMultiple(9);
                                                //testMatisse();
                                                break;
                                        }
                                    }
                                })
                        .show();
                break;
            case R.id.search_home:
                break;
        }
    }

    /**
     * 动态更多选项的点击事件
     */
    private class MyOnMoreOptionClickListener implements OnMoreOptionClickListener {
        @Override
        public void onclick(int position, View view) {
            new XPopup.Builder(getContext())
                    .offsetX(-10) //往左偏移10
//                        .offsetY(10)  //往下偏移10
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(view)
                    .asCustom(new MyAttachPopupView(getContext()))
                    .show();
        }
    }
}
