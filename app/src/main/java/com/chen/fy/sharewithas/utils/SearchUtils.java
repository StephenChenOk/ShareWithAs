package com.chen.fy.sharewithas.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.chen.fy.sharewithas.R;

public class SearchUtils {

    /**
     * 搜索时显示高亮
     */
    public static SpannableStringBuilder highLightText(String str, String key){
        int start = str.toLowerCase().indexOf(key.toLowerCase());
        int end = start+key.length();

        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(
                new ForegroundColorSpan(Color.RED), //设置高亮的颜色
                start,   //起始坐标
                end,     //终止坐标
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE   //旗帜  一般不用改变
        );
        return sb;
    }

}
