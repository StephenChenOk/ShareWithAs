package com.chen.fy.sharewithas.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.PublishActivity;
import com.chen.fy.sharewithas.adapters.MultipleStatesAdapter;
import com.chen.fy.sharewithas.adapters.MyViewPagerAdapter;
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends TakePhotoFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Activity mActivity;

    private static ViewPager mViewPager;
    private LinearLayout mPointBox;
    private TextView tvTitle;
    private TextView tvSearch;
    private ImageView ivTakePhotoLogo;

    /**
     * 拍照控件
     */
    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;

    /**
     * 图片剪切以及图片地址
     */
    private CropOptions mCropOptions;
    private Uri mUri;


    private RecyclerView mRecyclerView;

    private ArrayList<ShareInfo> mShareInfos;
    private ArrayList<Bitmap> mPictures;

    //图片集合
    public static ArrayList<ImageView> mImages;

    //图片id
    private int[] mImagesId = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img6,
            R.drawable.img4,
            R.drawable.img5
    };
    //图片标题
    public static String[] mImagesTitle = {
            "img1",
            "img2",
            "img3",
            "img4",
            "img5"
    };

    //上一个被选中图片的位置
    private int mPrePosition = 0;
    //判断当前页面是否是滑动状态,解决当页面手动滑动后不能继续进行自动滑动
    private boolean mIsScroll = false;

    /**
     * 让图片自己动起来,采用异步handel,因为在Thread中不可以进行UI操作,所有可以用handel实行异步UI操作
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(item);

            //延迟发消息
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };
    /**
     * 图片文件名
     */
    private final String mImageName = "sharePhoto.jpg";
    /**
     * 图片地址
     */
    private String mImagePath;
    /**
     * 图片文件大小
     */
    private long mFileLength;

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

        if (getActivity() != null) {
            mActivity = getActivity();
        } else {
            return;
        }
        initViewPager();
        initDataList();
        initTakePhoto();
    }

    private void initView(@NonNull View view) {
        //寻找控件
        mViewPager = view.findViewById(R.id.viewpager_home);
        tvTitle = view.findViewById(R.id.title_viewpager_home);
        mPointBox = view.findViewById(R.id.point_box_home);
        tvSearch = view.findViewById(R.id.search_home);
        mRecyclerView = view.findViewById(R.id.rv_home);
        ivTakePhotoLogo = view.findViewById(R.id.take_photo_logo_home);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        mRecyclerView.setLayoutManager(layoutManager);

        //设置点击事件
        tvSearch.setOnClickListener(this);
        ivTakePhotoLogo.setOnClickListener(this);
    }

    private void initViewPager() {
        //1 设置顶部的自动轮转图
        mViewPager.setAdapter(new MyViewPagerAdapter(getContext()));
        //2 获取轮播图的图片和点
        mImages = new ArrayList<>();
        for (int i = 0; i < mImagesId.length; i++) {
            //添加图片
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(mImagesId[i]);
            if (mActivity != null) {
                Glide.with(mActivity).load(mImagesId[i]).into(imageView);
            }
            mImages.add(imageView);
            //添加点
            ImageView point = new ImageView(getContext());
            Glide.with(mActivity).load(R.drawable.point_selctor).into(point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25); //自定义一个布局
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 40;    //距离左边的间距
            }
            point.setLayoutParams(params);   //设置格式,间距等
            mPointBox.addView(point);
        }

        //3 设置当前页面在中间位置,保证可以实现左右滑动的效果
        mViewPager.setCurrentItem(250);           //要保证是实际图片数量的整数倍,也就是保证每次进入都是先显示的第一张图片
        tvTitle.setText(mImagesTitle[mPrePosition]);
        //4 设置状态改变监听
        mViewPager.addOnPageChangeListener(this);

        //5 第一次进入时延迟发消息
        handler.sendEmptyMessageDelayed(0, 3000);

    }

    private void initDataList() {
        if (mShareInfos == null) {
            mShareInfos = new ArrayList<>();
        }
        if (!mShareInfos.isEmpty()) {
            mShareInfos.clear();
        }
        if (mPictures == null) {
            mPictures = new ArrayList<>();
        }
        if (!mPictures.isEmpty()) {
            mPictures.clear();
        }

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            getDataList(random.nextInt(3));
        }

        for (int i = 0; i < 6; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
            mPictures.add(bitmap);
        }


        MultipleStatesAdapter adapter = new MultipleStatesAdapter(mActivity);
        adapter.setShareDataList(mShareInfos);
        adapter.setGridViewPictureList(mPictures);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getDataList(int n) {
        switch (n) {
            case 0:
                ShareInfo shareInfo1 = new ShareInfo();
                shareInfo1.setType(1);
                shareInfo1.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                shareInfo1.setName("一一");
                shareInfo1.setContent("第一个布局");
                mShareInfos.add(shareInfo1);
                break;
            case 1:
                ShareInfo shareInfo2 = new ShareInfo();
                shareInfo2.setType(2);
                shareInfo2.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                shareInfo2.setName("二二");
                shareInfo2.setContent("第二个布局第二个布局第二个布局第二个布局第二个布局");
                shareInfo2.setPicture1(BitmapFactory.decodeResource(getResources(), R.drawable.img1));
                mShareInfos.add(shareInfo2);
                break;
            case 2:
                ShareInfo shareInfo3 = new ShareInfo();
                shareInfo3.setType(3);
                shareInfo3.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                shareInfo3.setName("三三");
                shareInfo3.setContent("第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局第三个布局");
                shareInfo3.setPicture1(BitmapFactory.decodeResource(getResources(), R.drawable.img2));
                shareInfo3.setPicture2(BitmapFactory.decodeResource(getResources(), R.drawable.img3));
                shareInfo3.setPicture3(BitmapFactory.decodeResource(getResources(), R.drawable.img4));
                mShareInfos.add(shareInfo3);
                break;
        }
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
        tvTitle.setText(mImagesTitle[i % mImages.size()]);
        //把上一个高亮位置设置为灰色
        mPointBox.getChildAt(mPrePosition).setEnabled(false);
        //当前位置设置为高亮
        mPointBox.getChildAt(i % mImages.size()).setEnabled(true);
        mPrePosition = i % mImages.size();
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
            mIsScroll = true;
        } else if (i == ViewPager.SCROLL_STATE_SETTLING) {  //滑动状态

        } else if (i == ViewPager.SCROLL_STATE_IDLE) {   //静止状态
            mIsScroll = false;
            handler.removeCallbacksAndMessages(null);   //清除消息
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        //获得对象
        mTakePhoto = getTakePhoto();

        //获取外部存储位置的uri
        File file = new File(mActivity.getExternalFilesDir(null), mImageName);
        mUri = Uri.fromFile(file);
        mImagePath = mUri.getPath();
        mFileLength = file.length();

        //进行图片剪切
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        mCropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具

        //进行图片压缩
        CompressConfig compressConfig = new CompressConfig.Builder().
                //大小            像素
                        setMaxSize(512).setMaxPixel(200).create();
        /*
         * 启用图片压缩
         * @param config 压缩图片配置
         * @param showCompressDialog 压缩时是否显示进度对话框
         * @return
         */
        mTakePhoto.onEnableCompress(compressConfig, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //以下代码为处理Android6.0、7.0动态权限所需(TakePhoto所需)
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult((Activity) mActivity, type, mInvokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.mInvokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取照片成功后成功回调
     */
    @Override
    public void takeSuccess(TResult result) {
        //将拍摄的照片显示出来
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(mUri));
            Intent intent = new Intent(mActivity, PublishActivity.class);

            // intent.putExtra("bitmap", bitmap);
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                                                mTakePhoto.onPickFromGalleryWithCrop(mUri, mCropOptions);
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

    private void toast(String text) {
        Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
    }
}
