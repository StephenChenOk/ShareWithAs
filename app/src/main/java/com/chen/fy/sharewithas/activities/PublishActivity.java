package com.chen.fy.sharewithas.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.adapters.PublishGridViewAdapter;
import com.chen.fy.sharewithas.asynctasks.PostShareInfoTask;
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.interfaces.MyOnPicturesItemClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.PermissionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PublishActivity extends TakePhotoActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText etContent;
    private GridView gvPictures;
    private PublishGridViewAdapter adapter;

    private ArrayList<Object> mUriList;
    private ArrayList<Object> mExpandList;

    private LinearLayout llLocationBox;
    private LinearLayout llAlarmBox;
    private LinearLayout llPersonBox;

    /**
     * 拍照控件
     */
    private TakePhoto mTakePhoto;
    /**
     * 图片剪切以及图片地址
     */
    private CropOptions mCropOptions;
    private Uri mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);

        UiUtils.changeStatusBarTextImgColor(this, true);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTakePhoto();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_publish);
        Button ivPublish = findViewById(R.id.btn_publish);
        etContent = findViewById(R.id.et_content_publish);
        gvPictures = findViewById(R.id.gv_box_public);
        llLocationBox = findViewById(R.id.ll_location_publish_box);
        llAlarmBox = findViewById(R.id.ll_alarm_publish_box);
        llPersonBox = findViewById(R.id.ll_person_publish_box);

        gvPictures.setOnItemClickListener(this);
        ivReturn.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
        llLocationBox.setOnClickListener(this);
        llAlarmBox.setOnClickListener(this);
        llPersonBox.setOnClickListener(this);
    }

    public void initData() {
        if (mUriList == null) {
            mUriList = new ArrayList<>();
        }
        if (!mUriList.isEmpty()) {
            mUriList.clear();
        }
        if (mExpandList == null) {
            mExpandList = new ArrayList<>();
        }
        if (!mExpandList.isEmpty()) {
            mExpandList.clear();
        }
        if (adapter == null) {
            adapter = new PublishGridViewAdapter(this);
        }
        if (getIntent() != null) {
            //发表的图片数量
            int imageCounts = getIntent().getIntExtra("ImagesSize", 0);
            for (int i = 0; i < imageCounts; i++) {
                mUriList.add(getIntent().getStringExtra("ImagesURI" + i));
                mExpandList.add(getIntent().getStringExtra("ImagesURI" + i));

            }
            if (imageCounts != 0 && imageCounts != 9) {
                mUriList.add(String.valueOf(R.drawable.ic_add_black_72dp));
            } else {
                adapter.setAdd(false);
            }
        }
        adapter.setUris(mUriList);
        gvPictures.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        //获得对象
        mTakePhoto = getTakePhoto();
        //图片文件名
        String mImageName = "sharePhoto" + new Random(1).nextInt() + ".jpg";
        File file = new File(this.getExternalFilesDir(null), mImageName);
        mUri = Uri.fromFile(file);

        //进行图片剪切
        int size = Math.max(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        mCropOptions = new CropOptions.Builder().setOutputX(size).setOutputY(size).setWithOwnCrop(false).create();  //true表示使用TakePhoto自带的裁剪工具


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_publish:
                finish();
                break;
            case R.id.btn_publish:
                String content = etContent.getText().toString();
                postShareInfo(content);
                break;
            case R.id.ll_location_publish_box:
                toast("所在位置");
                break;
            case R.id.ll_alarm_publish_box:
                toast("提醒谁看");
                break;
            case R.id.ll_person_publish_box:
                toast("谁可以看");
                break;
        }
    }

    /**
     * 发表动态
     */
    private void postShareInfo(String content) {
        PostShareInfoTask postShareInfoTask = new PostShareInfoTask(this, mExpandList);
        postShareInfoTask.execute(content);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mUriList.size() != 10 && position == mUriList.size() - 1) {
            showSelectDialog();
        } else {
            zoomPicture(parent, view, position);
        }
    }

    /**
     * 显示拍照，相册选取对话框
     */
    private void showSelectDialog() {
        new XPopup.Builder(this)
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
                                        mTakePhoto.onPickMultiple(9 - mExpandList.size());
                                        //testMatisse();
                                        break;
                                }
                            }
                        })
                .show();
    }

    /**
     * 点击放大图片
     */
    private void zoomPicture(AdapterView<?> parent, View view, int position) {
        //1 获取要显示的图片对象
        final RelativeLayout relativeLayout = (RelativeLayout) parent.getAdapter().
                getView(position, view, null);
        final ImageView imageView = (ImageView) relativeLayout.getChildAt(position);
        //2 使用XPopup开始显示放大后的图片
        new XPopup.Builder(relativeLayout.getContext()).asImageViewer(imageView, position, mExpandList, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                //3 放大效果时滑动后显示的图片
                GridView gridView = (GridView) relativeLayout.getParent();
                popupView.updateSrcView((ImageView) ((RelativeLayout) gridView.getChildAt(position)).getChildAt(0));
            }
        }, new MyOnPicturesItemClickListener.ImageLoader())
                .show();
    }

    /**
     * 获取照片成功后成功回调
     */
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        if (!result.getImages().isEmpty()) {
            mUriList.remove(mUriList.size() - 1);
            for (int i = 0; i < result.getImages().size(); i++) {
                mUriList.add(result.getImages().get(i).getCompressPath());
                mExpandList.add(result.getImages().get(i).getCompressPath());
            }
            if (mUriList.size() < 9) {
                mUriList.add(String.valueOf(R.drawable.ic_add_black_72dp));
                adapter.setAdd(true);
            } else {
                adapter.setAdd(false);
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.d("chenyisheng2", "isEmpty!!!!");
        }
    }

}
