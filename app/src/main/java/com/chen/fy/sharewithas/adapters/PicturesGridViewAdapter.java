package com.chen.fy.sharewithas.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;

import java.util.ArrayList;

public class PicturesGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> mPictures;

    PicturesGridViewAdapter(Context context) {
        mContext = context;
    }

    void setPictures(ArrayList<Bitmap> list) {
        mPictures = list;
    }

    @Override
    public int getCount() {
        return (mPictures == null) ? 0 : mPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder viewHolder;
        if (convertView == null) {      //判断缓冲池是否已经有view ,若有则可以直接用,不需要再继续反射
            view = LayoutInflater.from(mContext).inflate(R.layout.gv_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = view.findViewById(R.id.gv_iv_picture);

            view.setTag(viewHolder);
        } else {    //若缓冲池中已经有view则可以直接用holder对象
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (getCount() == 4 || getCount() == 2) {
            ViewGroup.LayoutParams params = viewHolder.ivPicture.getLayoutParams();
            params.width = MainActivity.width / 2 - MainActivity.width / 11;
            params.height = MainActivity.width / 2 - MainActivity.width / 11 - MainActivity.width / 9;
            viewHolder.ivPicture.setLayoutParams(params);
        }
        Glide.with(mContext).load(mPictures.get(position)).into(viewHolder.ivPicture);

        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        ImageView ivPicture;
    }

}
