package com.chen.fy.sharewithas.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.fragments.FoundFragment;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.chen.fy.sharewithas.fragments.MineFragment;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private final int REQUEST_CODE = 1;

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
        applyPermission();
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

        //第一次进入时显示home界面
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, homeFragment).
//                commitAllowingStateLoss();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.fragment_main, homeFragment);
        }
        hideFragment(transaction);
        transaction.show(homeFragment).commit();

        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 使用replace时每次都会销毁并重建Fragment，而使用show、hide则避免了此问题
     * 隐藏所有的Fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (foundFragment != null) {
            transaction.hide(foundFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //底部导航栏按钮选中事件
        RadioButton radioButton = group.findViewById(checkedId);
        radioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.rb_home_main:
//                transaction.replace(R.id.fragment_main, homeFragment);
//                transaction.commitAllowingStateLoss();
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_main, homeFragment);
                }
                hideFragment(transaction);
                transaction.show(homeFragment).commit();
                break;
            case R.id.rb_found_main:
//                transaction.replace(R.id.fragment_main, foundFragment);
//                transaction.commitAllowingStateLoss();
                if (foundFragment == null) {
                    foundFragment = new FoundFragment();
                    transaction.add(R.id.fragment_main, foundFragment);
                }
                hideFragment(transaction);
                transaction.show(foundFragment).commit();
                break;
            case R.id.rb_mine_main:
//                transaction.replace(R.id.fragment_main, mineFragment);
//                transaction.commitAllowingStateLoss();
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fragment_main, mineFragment);
                }
                hideFragment(transaction);
                transaction.show(mineFragment).commit();
                break;
        }
    }

    /**
     * 动态申请危险权限
     */
    private void applyPermission() {
        //权限集合
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必须同意所有权限才可以使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    List<Uri> mSelected;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            mSelected = Matisse.obtainResult(data);
//            Log.d("Matisse", "mSelected: " + mSelected);
//        }
//    }
}
