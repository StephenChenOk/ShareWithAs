package com.chen.fy.sharewithas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyInfoActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final String TAG = "MyInfoActivity.Log";
    private ImageView ivHeadIcon;
    private TextView tvUsername;
    private TextView tvAccount;
    private TextView tvPhoneNumber;

    /**
     * 拍照控件
     */
    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;

    /**
     * 图片剪切以及图片地址
     */
    private CropOptions mCropOptions;
    public static Uri mUri;

    private boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_layout);

        UiUtils.changeStatusBarTextImgColor(this, true);
        initView();
        initTakePhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_my_info);
        LinearLayout headIconBox = findViewById(R.id.ll_head_icon_box_my_info);
        LinearLayout usernameBox = findViewById(R.id.ll_username_box_my_info);
        LinearLayout accountBox = findViewById(R.id.ll_account_box_my_info);
        LinearLayout phoneBox = findViewById(R.id.ll_phone_box_my_info);
        Button btnOutLogin = findViewById(R.id.btn_out_login_my_info);
        ivHeadIcon = findViewById(R.id.iv_head_icon_my_info);
        tvUsername = findViewById(R.id.tv_username_my_info);
        tvAccount = findViewById(R.id.tv_account_my_info);
        tvPhoneNumber = findViewById(R.id.tv_phone_number_my_info);

        ivReturn.setOnClickListener(this);
        headIconBox.setOnClickListener(this);
        usernameBox.setOnClickListener(this);
        accountBox.setOnClickListener(this);
        phoneBox.setOnClickListener(this);
        btnOutLogin.setOnClickListener(this);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "匿名网友");
        String account = sharedPreferences.getString("account", "123");
        String phoneNumber = sharedPreferences.getString("phoneNumber", "18074845457");

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img)//图片加载出来前，显示的图片
                .fallback(R.drawable.img)  //url为空的时候,显示的图片
                .error(R.drawable.img);    //图片加载失败后，显示的图片

        tvUsername.setText(userName);
        tvAccount.setText(account);
        tvPhoneNumber.setText(phoneNumber);

        if (isFirst) {
            String headUrl = sharedPreferences.getString("headUrl", "Constants.LOGIN_SERVER_URL + \"/asserts/headImg/default_head.jpg\"");
            Glide.with(this).load(headUrl).apply(options).into(ivHeadIcon);
            isFirst = false;
        }
    }

    /**
     * 初始化TakePhoto开源库,实现拍照以及从相册中选择图片
     */
    private void initTakePhoto() {
        //获得对象
        mTakePhoto = getTakePhoto();
        //图片文件名
        String mImageName = "headIcon" + new Random(1).nextInt() + ".jpg";
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

    /**
     * 获取照片成功后成功回调
     */
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute(mUri.getPath());
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
            ivHeadIcon.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_my_info:
                finish();
                break;
            case R.id.ll_head_icon_box_my_info:
                showPictureSelectDialog();
                break;
            case R.id.ll_username_box_my_info:
                Intent intent1 = new Intent(this, ModifyUsernameActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_account_box_my_info:

                break;
            case R.id.ll_phone_box_my_info:
                Intent intent2 = new Intent(this, ModifyPhoneNumberActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_out_login_my_info:
                clearLoginState();
                finish();
                break;

        }
    }

    /**
     * 显示图片选择对话框
     */
    private void showPictureSelectDialog() {
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
                                        mTakePhoto.onPickFromGalleryWithCrop(mUri, mCropOptions);
                                        break;
                                }
                            }
                        })
                .show();
    }

    /**
     * 清除登录状态
     */
    private void clearLoginState() {
        SharedPreferences.Editor editorUserInfo = getSharedPreferences("userInfo", MODE_PRIVATE).edit();

        editorUserInfo.clear();
        editorUserInfo.apply();
    }

    /**
     * 进行图片上传
     */
    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        //setType(MultipartBody.FORM)
        String result = "error";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", String.valueOf(id))
                .addFormDataPart("file", imagePath,                         //图片
                        RequestBody.create(MediaType.parse("image/jpg"), new File(imagePath)))
                .build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constants.HEAD_ICON_SERVER_URL)
                .post(requestBody)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

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
            return doPost(params[0]);
        }

        /**
         * 当后台任务执行完毕时调用
         *
         * @param result 后台执行任务的返回值
         */
        @Override
        protected void onPostExecute(String result) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        JsonParser parser = new JsonParser();//Json解析
        try {
            //1 获取JsonObject对象
            JsonObject jsonObject = (JsonObject) parser.parse(result);
            Log.d(TAG, jsonObject.toString());

            //2 分别取出数据
            String msg1 = jsonObject.get("msg").toString();
            String msg = msg1.substring(1, msg1.length() - 1);

            String newHeadUrl1 = jsonObject.get("newHeadUrl").toString();
            String newHeadUrl = newHeadUrl1.substring(1, newHeadUrl1.length() - 1);

            //3 保存数据
            SharedPreferences.Editor editorUserInfo = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
            editorUserInfo.putString("headUrl", newHeadUrl);
            editorUserInfo.apply();

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
