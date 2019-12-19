package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.interfaces.MyOnPicturesItemClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.lxj.xpopup.XPopup;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivHeadIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_layout);
        UiUtils.changeStatusBarTextImgColor(this, true);
        initView();
        setHeadIcon();
    }

    private void initView() {
        ImageView ivReturn = findViewById(R.id.iv_return_user_detail);
        ImageView ivMoreOption = findViewById(R.id.iv_more_option_user_detail);
        ivHeadIcon = findViewById(R.id.iv_headIcon_user_detail);
        RelativeLayout rlRemarkBox = findViewById(R.id.rl_remark_box);
        RelativeLayout rlAuthorityBox = findViewById(R.id.rl_authority_box);
        RelativeLayout rlShareInfoBox = findViewById(R.id.rl_share_info_box);
        RelativeLayout rlMoreInfoBox = findViewById(R.id.rl_more_info_box);
        LinearLayout llChartBox = findViewById(R.id.ll_chart_box);

        ivReturn.setOnClickListener(this);
        ivMoreOption.setOnClickListener(this);
        ivHeadIcon.setOnClickListener(this);
        rlRemarkBox.setOnClickListener(this);
        rlAuthorityBox.setOnClickListener(this);
        rlShareInfoBox.setOnClickListener(this);
        rlMoreInfoBox.setOnClickListener(this);
        llChartBox.setOnClickListener(this);
    }

    private void setHeadIcon(){
        //设置图片圆角角度
        RoundedCorners roundedCorners= new RoundedCorners(15);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners);

        Glide.with(this).load(R.drawable.img).apply(options).into(ivHeadIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_user_detail:
                finish();
                break;
            case R.id.iv_more_option_user_detail:
                break;
            case R.id.iv_headIcon_user_detail:
                zoomPicture();
                break;
            case R.id.rl_remark_box:
                break;
            case R.id.rl_authority_box:
                break;
            case R.id.rl_share_info_box:
                break;
            case R.id.rl_more_info_box:
                break;
            case R.id.ll_chart_box:
                break;
        }
    }

    /**
     * 点击放大图片
     */
    private void zoomPicture() {
        new XPopup.Builder(this)
                .asImageViewer(ivHeadIcon, R.drawable.img, false
                        , -1
                        , -1
                        , -1
                        , false,
                        new MyOnPicturesItemClickListener.ImageLoader()).show();
    }
}
