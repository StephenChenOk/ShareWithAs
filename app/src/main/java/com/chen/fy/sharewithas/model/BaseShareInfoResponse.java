package com.chen.fy.sharewithas.model;

import com.google.gson.annotations.Expose;

public class BaseShareInfoResponse {

    @Expose(serialize = false,deserialize = false)   //指定属性是否进行序列化或者接序列化
    private int id;
    private User user;
    private String content;
    private String images;
    private String likes;
    private String comments;
    private String shareDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    @Override
    public String toString() {
        return "BaseShareInfoResponse{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", images='" + images + '\'' +
                ", likes='" + likes + '\'' +
                ", comments='" + comments + '\'' +
                ", shareDate='" + shareDate + '\'' +
                '}';
    }
}
