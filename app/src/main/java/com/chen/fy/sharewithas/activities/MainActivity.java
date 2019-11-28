package com.chen.fy.sharewithas.activities;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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

    public static int width = 0;
    public static int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将状态栏字体变为黑色
        //UiUtils.changeStatusBarTextImgColor(this, true);
        initView();

        getWH();
    }

    /**
     * 获取屏幕宽高
     */
    private void getWH() {
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //获取屏幕对象
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕的宽、高
        width = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();
    }

    private void initView() {
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
