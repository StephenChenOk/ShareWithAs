package com.chen.fy.sharewithas.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.fy.sharewithas.R;

import java.util.ArrayList;

public class PicturesGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> mPictures;

    PicturesGridViewAdapter(Context context) {
        mContext = context;
    }

    public void setPictures(ArrayList<Bitmap> list) {
        mPictures = list;
    }

    @Override
    public int getCount() {
        return (mPictures == null) ? 0 : mPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return mPictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("chenyisheng", "getView。。。。");
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
        viewHolder.ivPicture.setImageBitmap(mPictures.get(position));

        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        ImageView ivPicture;
    }

}
