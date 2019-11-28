package com.chen.fy.sharewithas.beans;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享的信息类
 */
public class ShareInfo {

    private int type;  //布局的类型
    private Bitmap headIcon;
    private String name;
    private String content;
    private ArrayList<Bitmap> photos;

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

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.photos = photos;
    }
}
