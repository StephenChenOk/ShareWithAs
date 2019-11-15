package com.chen.fy.sharewithas.beans;

import android.graphics.Bitmap;

/**
 * 分享的信息类
 */
public class ShareInfo {

    private int type;  //布局的类型
    private Bitmap headIcon;
    private String name;
    private String content;
    private Bitmap picture1;
    private Bitmap picture2;
    private Bitmap picture3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getPicture1() {
        return picture1;
    }

    public void setPicture1(Bitmap picture1) {
        this.picture1 = picture1;
    }

    public Bitmap getPicture2() {
        return picture2;
    }

    public void setPicture2(Bitmap picture2) {
        this.picture2 = picture2;
    }

    public Bitmap getPicture3() {
        return picture3;
    }

    public void setPicture3(Bitmap picture3) {
        this.picture3 = picture3;
    }
}
