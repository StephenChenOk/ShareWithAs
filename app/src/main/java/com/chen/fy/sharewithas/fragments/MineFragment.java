package com.chen.fy.sharewithas.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.LoginActivity;
import com.chen.fy.sharewithas.activities.MyInfoActivity;
import com.chen.fy.sharewithas.beans.User;
import com.chen.fy.sharewithas.utils.UiUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment implements View.OnClickListener {

    private CircleImageView ivHeadIcon;
    private TextView tvUsername;
    private TextView tvUserInfo;

    private View mView;

    private String mUsername;

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

        //1 获取之前的登入状态
        getLoginState();
        //2 根据登录与否显示不同的界面
        showUserInfo();
        //3 进行头像加载
        loadHeadIcon();

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
     * 显示游客一些简单的信息
     */
    private void showUserInfo() {
        if (mUsername != null && !mUsername.isEmpty()) {
            List<User> users = LitePal.where("userId = ?", mUsername).find(User.class);
            for (User user : users) {
                tvUsername.setText(user.getNickname());
                tvUserInfo.setText("个人信息>");
            }
        } else {
            tvUsername.setText("登入/注册");
            tvUserInfo.setText("");
        }
    }

    /**
     * 获取之前的登入状态
     */
    private void getLoginState() {
        if (getContext() != null) {
            SharedPreferences preferences = getContext().getSharedPreferences("login_state", MODE_PRIVATE);
            mUsername = preferences.getString("username", "");
        }
    }

    /**
     * 进行头像加载
     */
    private void loadHeadIcon() {
        if (mUsername != null && !mUsername.isEmpty()) {
            if (getActivity() != null) {
                //头像加载
                File file = new File(getActivity().getExternalFilesDir(null), mUsername + "_headIcon.jpg");
                Uri headIconUri = Uri.fromFile(file);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(headIconUri));
                    ivHeadIcon.setImageBitmap(bitmap);                    //如果上面产生文件存在异常，则不执行
                } catch (FileNotFoundException e) {
                    ivHeadIcon.setImageResource(R.drawable.img);   //捕获异常后，设置头像为默认头像，程序继续执行
                }
            } else {
                ivHeadIcon.setImageResource(R.drawable.img);
            }
        } else {
            ivHeadIcon.setImageResource(R.drawable.img);
        }
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
        if (mUsername == null || mUsername.isEmpty()) {   //当还没有登入账号,则进入登入界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        } else {                                        //已经登入账号,则进入显示个人信息界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                intent.putExtra("userId", mUsername);
                startActivityForResult(intent, 2);
            }
        }
    }
}
