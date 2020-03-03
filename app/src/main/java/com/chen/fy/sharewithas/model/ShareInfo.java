package com.chen.fy.sharewithas.model;

import java.util.ArrayList;

/**
 * 分享的信息类
 */
public class ShareInfo {

    private int id;
    private int type;  //布局的类型
    private String headUrl;
    private String userName;
    private String content;
    private ArrayList<Object> photos;
    private String shareDate;
    private String likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Object> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Object> photos) {
        this.photos = photos;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "id=" + id +
                ", type=" + type +
                ", headUrl='" + headUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", photos=" + photos +
                ", shareDate='" + shareDate + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}
