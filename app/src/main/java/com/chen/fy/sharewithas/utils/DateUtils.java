package com.chen.fy.sharewithas.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 时间格式转化2( 年.月.日 转化为毫秒数)
     *
     * @param time 当前评价的时间
     * @return 格式化好的毫秒数
     */
    public static long dateLongChange(String time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式需要一样
        Date date = null;
        try {
            date = dateFormat.parse(time);     //解析
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
