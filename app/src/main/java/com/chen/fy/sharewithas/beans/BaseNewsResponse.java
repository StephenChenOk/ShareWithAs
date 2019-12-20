package com.chen.fy.sharewithas.beans;

import com.google.gson.annotations.SerializedName;

/**
 * HTTP响应体:
 * {
 * "code": 200,
 * "msg": "success",
 * "newslist":[
 * { },{ }      //数据集合
 * ]
 * }
 */
public class BaseNewsResponse<T> {
    // 通过泛型T，实现将此类定义为一个模板类，
    // 当接口A需要的是一个List集合时，T数据就是一个集合；
    // 当接口B需要的时一个对象时则T可以是对象

    private int code;       //请求码
    private String msg;     //返回信息

    public final static int RESPONSE_SUCCESS = 0;

    @SerializedName("newslist")   //返回的请求体中的数据集合属性名称
    private T data;

    public BaseNewsResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
