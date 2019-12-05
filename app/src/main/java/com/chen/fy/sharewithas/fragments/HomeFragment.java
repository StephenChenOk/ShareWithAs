package com.chen.fy.sharewithas.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;
import com.chen.fy.sharewithas.activities.PublishActivity;
import com.chen.fy.sharewithas.adapters.MultipleStatesAdapter;
import com.chen.fy.sharewithas.adapters.MyViewPagerAdapter;
import com.chen.fy.sharewithas.beans.ShareInfo;

import com.chen.fy.sharewithas.interfaces.MyOnPicturesItemClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoFragment;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.PermissionManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends TakePhotoFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Context mContext;

    private static ViewPager mViewPager;
    private LinearLayout mPointBox;
    private TextView tvTitle;

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
    public static Handler handler = new Handler(Looper.getMainLooper()) {
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
     * 动态图片点击接口
     */
    private MyOnPicturesItemClickListener itemClickListener = new MyOnPicturesItemClickListener();

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

        if (getContext() != null) {
            mContext = getContext();
        } else {
            return;
        }
        initViewPager();
        initDataList();
        initTakePhoto();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Fragment在show与hide状态转换时调用此方法
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden && getActivity()!=null){
            UiUtils.changeStatusBarTextImgColor(getActivity(), false);
        }
    }
    private void initView(@NonNull View view) {
        //寻找控件
        mViewPager = view.findViewById(R.id.viewpager_home);
        tvTitle = view.findViewById(R.id.title_viewpager_home);
        mPointBox = view.findViewById(R.id.point_box_home);
        TextView tvSearch = view.findViewById(R.id.search_home);
        mRecyclerView = view.findViewById(R.id.rv_home);
        ImageView ivTakePhotoLogo = view.findViewById(R.id.take_photo_logo_home);

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
            if (mContext != null) {
                Glide.with(mContext).load(mImagesId[i]).into(imageView);
            }
            mImages.add(imageView);
            //添加点
            ImageView point = new ImageView(getContext());
            Glide.with(mContext).load(R.drawable.point_selctor).into(point);
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

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            getDataList(random);
        }

        MultipleStatesAdapter adapter = new MultipleStatesAdapter(mContext);
        adapter.setShareDataList(mShareInfos);
        adapter.setItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getDataList(Random random) {
        Bitmap bitmap;
        ArrayList<Object> list = new ArrayList<>();
        switch (random.nextInt(2)) {
            case 0:
                ShareInfo shareInfo1 = new ShareInfo();
                shareInfo1.setType(1);
                shareInfo1.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                shareInfo1.setName("文字");
                shareInfo1.setContent("文字布局，文字布局，文字布局，文字布局，文字布局，文字布局，文字布局，文字布局，文字布局");
                mShareInfos.add(shareInfo1);
                break;
            case 1:
                ShareInfo shareInfo2 = new ShareInfo();
                shareInfo2.setType(2);
                shareInfo2.setHeadIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img11));
                shareInfo2.setName("多图片");
                shareInfo2.setContent("多图片布局，多图片布局，多图片布局，多图片布局，多图片布局，多图片布局，多图片布局");
                for (int i = 0; i < 9; i++) {
                    if (i % 2 == 0) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);
                    } else {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img11);
                    }
                    list.add(bitmap);
                }
                shareInfo2.setPhotos(list);
                mShareInfos.add(shareInfo2);
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
        //图片文件名
        String mImageName = "sharePhoto.jpg";
        File file = new File(mContext.getExternalFilesDir(null), mImageName);
        mUri = Uri.fromFile(file);

        //进行图片剪切
        int size = Math.max(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        mCropOptions = new CropOptions.Builder().setOutputX(size).setOutputY(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具

        //进行图片压缩
        CompressConfig compressConfig = new CompressConfig.Builder().create();
//        //大小            像素
//        //setMaxSize(1024).setMaxPixel(1024*1024).create();
//
        /*
         * 启用图片压缩
         * @param config 压缩图片配置
         * @param showCompressDialog 压缩时是否显示进度对话框
         */
        mTakePhoto.onEnableCompress(compressConfig, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //以下代码为处理Android6.0、7.0动态权限所需(TakePhoto所需)
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, mInvokeParam, this);
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
        super.takeSuccess(result);
        Intent intent = new Intent(mContext, PublishActivity.class);
        intent.putExtra("ImagesSize", result.getImages().size());
        for (int i = 0; i < result.getImages().size(); i++) {
            intent.putExtra("ImagesURI" + i, result.getImages().get(i).getCompressPath());
        }
        startActivity(intent);
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
     * 防止内存泄漏
     * 1:定义为静态内部类
     * 2:持有外部弱引用
     */
    private class GetDataTask extends AsyncTask<String, Integer, String> {

        //获取外部弱引用
        private WeakReference<MainActivity> activityWeakReference;

        GetDataTask(MainActivity mainActivity) {
            activityWeakReference = new WeakReference<>(mainActivity);
        }

        /**
         * 后台任务开始之前调用，通常用来初始化界面操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 执行后台耗时操作，已在子线程中执行
         *
         * @return 对结果进行返回
         */
        @Override
        protected String doInBackground(String... params) {
            return "";
        }

        /**
         * 当后台任务执行完毕时调用
         *
         * @param result 后台执行任务的返回值
         */
        @Override
        protected void onPostExecute(String result) {
            //判断外部活动对象是否已经被销毁
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
    }

    private void toast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
