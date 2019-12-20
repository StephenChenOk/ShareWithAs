package com.chen.fy.sharewithas.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.LoginActivity;
import com.chen.fy.sharewithas.activities.MyInfoActivity;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.utils.UiUtils;

import java.io.File;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment implements View.OnClickListener {

    private final int LOGIN_REQUEST_CODE = 1;
    private final int MY_INFO_REQUEST_CODE = 2;

    public static final int RESULT_OUT_LOGIN = 3;

    private CircleImageView ivHeadIcon;
    private TextView tvUsername;
    private TextView tvUserInfo;

    private View mView;

    private int mId = -1;

    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.mine_fragment_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化View
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        //1 初始化登录状态
        initLoginState();
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

    private void initView() {
        //游客端view
        ivHeadIcon = mView.findViewById(R.id.head_icon_mine);
        tvUsername = mView.findViewById(R.id.user_name_mine);
        tvUserInfo = mView.findViewById(R.id.info_text_mine);

        ivHeadIcon.setOnClickListener(this);
        tvUsername.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);
    }

    /**
     * 初始化登录状态
     */
    private void initLoginState() {
        if (getContext() != null) {
            String spName = getResources().getString(R.string.userInfo_sp_name);
            SharedPreferences preferences = getContext().getSharedPreferences(spName, MODE_PRIVATE);

            String idKey = getResources().getString(R.string.id_sp_key);
            mId = preferences.getInt(idKey, -1);
            if (mId != -1) {  //已经登录
                String userNameKey = getResources().getString(R.string.userName_sp_key);
                String userName = preferences.getString(userNameKey, "后起之秀");

                tvUsername.setText(userName);
                tvUserInfo.setText("个人信息>");

            } else {
                tvUsername.setText("登入/注册");
                tvUserInfo.setText("我们的故事从拥有一个账号开始");
            }

            if (isFirst) {
                setHeadIcon(preferences);
                isFirst = false;
            }
        }
    }

    private void setHeadIcon(SharedPreferences preferences) {
        String headUrlKey = getResources().getString(R.string.headUrl_sp_key);
        String headUrl = preferences.getString(headUrlKey, "http://xiongtianmin.online/album/asserts/headImg/default_head.jpg");

        String mImageName = "headIcon" + ".jpg";
        File file = new File(getActivity().getExternalFilesDir(null), mImageName);
        Uri uri = Uri.fromFile(file);
        Drawable preImg = null;
        try {
            preImg = Drawable.createFromStream(getActivity().getContentResolver().openInputStream(uri), "headIcon.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(preImg)//图片加载出来前，显示的图片
                .fallback(R.drawable.img)  //url为空的时候,显示的图片
                .error(R.drawable.img);    //图片加载失败后，显示的图片

        Glide.with(getContext()).load(headUrl).apply(options).into(ivHeadIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_icon_mine:
            case R.id.user_name_mine:
            case R.id.info_text_mine:
                jumpActivity();
                break;
        }
    }

    /**
     * 根据登录与否跳转不同的界面
     */
    private void jumpActivity() {
        if (mId == -1) {   //当还没有登入账号,则进入登入界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        } else {                                        //已经登入账号,则进入显示个人信息界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivityForResult(intent, MY_INFO_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case LOGIN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String spName = getResources().getString(R.string.userInfo_sp_name);
                    SharedPreferences preferences = getContext().getSharedPreferences(spName, MODE_PRIVATE);
                    setHeadIcon(preferences);
                }
                break;
            case MY_INFO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    boolean isChange = data.getBooleanExtra(getResources().getString(R.string.head_icon_is_change), false);
                    if (isChange) {
                        //如果头像已经发生改变，则从本地直接获取头像，并更改头像
                        String headIconPath = data.getStringExtra(getResources().getString(R.string.head_icon_path));
                        Bitmap bitmap = BitmapFactory.decodeFile(headIconPath);
                        ivHeadIcon.setImageBitmap(bitmap);
                    }
                }
                if (resultCode == RESULT_OUT_LOGIN) {  //退出账号
                    if (getContext() != null) {
                        Glide.with(getContext()).load(R.drawable.img).into(ivHeadIcon);
                    }
                }
                break;
        }
    }
}
