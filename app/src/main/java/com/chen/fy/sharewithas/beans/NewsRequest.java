package com.chen.fy.sharewithas.beans;

import com.chen.fy.sharewithas.constants.Constants;

/**
 * 请求类，每一个接口需要的请求参数可能不同
 */
public class NewsRequest {

    private int num;        //每页显示数量
    private int page = -1;  //请求的页码（一般来说页码不同数据不同，按时间排序）
    private int rand;       //随机选取数据
    private String keyword; //Key
    private String word;    //关键字

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        String retValue;
        retValue = "?" + "&key=" + Constants.API_KEY + "&num=" + num;
        if (page != -1) {
            retValue += "&page=" + page;
        }
        return retValue;
    }
}