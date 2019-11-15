package com.chen.fy.sharewithas.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.fragments.FoundFragment;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.chen.fy.sharewithas.fragments.MineFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private HomeFragment homeFragment;
    private FoundFragment foundFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将状态栏字体变为黑色
        //UiUtils.changeStatusBarTextImgColor(this, true);

        setContentView(R.layout.activity_main);

        //找到控件对象
        RadioGroup radioGroup = findViewById(R.id.rg_box_main);

        //初始化fragment
        homeFragment = new HomeFragment();
        foundFragment = new FoundFragment();
        mineFragment = new MineFragment();

        //第一次进入时显示home界面
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, homeFragment).
                commitAllowingStateLoss();

        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //底部导航栏按钮选中事件
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.rb_home_main:
                transaction.replace(R.id.fragment_main, homeFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.rb_found_main:
                transaction.replace(R.id.fragment_main, foundFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.rb_mine_main:
                transaction.replace(R.id.fragment_main, mineFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }

}
