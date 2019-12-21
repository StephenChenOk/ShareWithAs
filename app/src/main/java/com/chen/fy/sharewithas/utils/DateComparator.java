package com.chen.fy.sharewithas.utils;

import android.util.Log;

import com.chen.fy.sharewithas.beans.ShareInfo;

import java.util.Comparator;

/**
 * 时间选择器
 */
public class DateComparator implements Comparator<ShareInfo> {

    @Override
    public int compare(ShareInfo o1, ShareInfo o2) {
        if (DateUtils.dateLongChange(o1.getShareDate()) < DateUtils.dateLongChange(o2.getShareDate())) {
            return 1;
        } else if (DateUtils.dateLongChange(o1.getShareDate()) == DateUtils.dateLongChange(o2.getShareDate())) {
            return 0;
        } else {
            return -1;
        }
    }
}
