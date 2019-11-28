package com.chen.fy.sharewithas.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

/**
 * 自定义GridView,重写onMeasure()方法，使WrapContent属性能够生效
 */
public class WrapContentGridView extends GridView {
    public WrapContentGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //设置图片格式
        int size = getAdapter().getCount();
        if (size == 1) {
            setNumColumns(1);
        } else if (size == 2 || size == 4) {
            setNumColumns(2);
        } else {
            setNumColumns(3);
        }

        int heightWrapContent = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightWrapContent);
    }
}
