package com.chen.fy.sharewithas.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 服务器返回的Json类型接收对象
 *
 * HTTP响应体:
 * {
 * "code": 200, "msg": "success",
 * "newslist": [
 *
 * { "ctime": "2019-09-07 16:49", "title": "阿里巴巴20周年给家乡的一封信：谢谢你，杭州",
 *   "description": "网易互联网", "picUrl": "http://cms.ws.126.net/2019/09/07/842bf50.png,
 *   "url": "https://tech.163.com/19/0907/16/EOG30A9LD.html" },
 *
 * { "ctime": "2019-09-07 17:43", "title": "2018年度中国零售百强：7家企业销售规模过千亿",
 *   "description": "网易互联网", "picUrl": "http://cms.ws.126.net/2019/09/07/4e5e26a.png,
 *   "url": "https://tech.163.com/19/0907/17/EOC097U7R.html" },
 *    ...
 *  ]
 * }
 */
public class News {

    @Expose(serialize = false,deserialize = false)   //指定属性是否进行序列化或者接序列化
    private Integer mId;

    @SerializedName("title")    //当类中的属性名与Json中字符串的属性名不同时使用
    private String mTitle;

    @SerializedName("description")      //发布者
    private String mSource;

    @SerializedName("picUrl")           //图片
    private String mPicUrl;

    @SerializedName("url")              //内容url
    private String mContentUrl;

    @SerializedName("ctime")            //发布时间
    private String mPublishTime;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String mPicUrl) {
        this.mPicUrl = mPicUrl;
    }

    public String getContentUrl() {
        return mContentUrl;
    }

    public void setContentUrl(String mContentUrl) {
        this.mContentUrl = mContentUrl;
    }

    public String getPublishTime() {
        return mPublishTime;
    }

    public void setPublishTime(String mPublishTime) {
        this.mPublishTime = mPublishTime;
    }
}
