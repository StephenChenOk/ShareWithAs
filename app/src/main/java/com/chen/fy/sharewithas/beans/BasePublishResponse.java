package com.chen.fy.sharewithas.beans;

public class BasePublishResponse {

    private String msg;
    private BaseShareInfoResponse moment;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseShareInfoResponse getMoment() {
        return moment;
    }

    public void setMoment(BaseShareInfoResponse moment) {
        this.moment = moment;
    }

    @Override
    public String toString() {
        return "BasePublishResponse{" +
                "msg='" + msg + '\'' +
                ", moment=" + moment +
                '}';
    }
}
