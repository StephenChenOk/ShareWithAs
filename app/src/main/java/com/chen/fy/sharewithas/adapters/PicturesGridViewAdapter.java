package com.chen.fy.sharewithas.adapters;

import android.content.Context;
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
    private ArrayList<Object> mPictures;

    PicturesGridViewAdapter(Context context) {
        mContext = context;
    }

    void setPictures(ArrayList<Object> list) {
        mPictures = list;
    }

    /**
     * 决定了ListView中一共有多少个Item
     */
    @Override
    public int getCount() {
        return (mPictures == null) ? 0 : mPictures.size();
    }

    /**
     * 它也不会被自动调用，它是用来在我们设置setOnItemClickListener、setOnItemLongClickListener、
     * setOnItemSelectedListener的点击选择处理事件中方便地调用来获取当前行数据的。
     * 官方解释:Implementers can call getItemAtPosition(position) if they need to access the data
     */
    @Override
    public Object getItem(int position) {
        return mPictures.get(position);
    }

    /**
     * 返回的是该postion对应item的id,adapterview也有类似方法：
     * 某些方法（如onClickListener的onclick方法）有id这个参数，而这个id参数就是取决于getItemId()这个返回值的
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 决定了每个Item布局所显示的View
     */
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

//        int width = parent.getWidth();
//        int height = parent.getHeight();
//
//        Log.d("WH:", width + "," + height);
//        Log.d("WH:", String.valueOf(MainActivity.width));
//        Log.d("WH:", String.valueOf(MainActivity.height));

        int whTemp = MainActivity.width;

        //根据图片数量动态设置图片的大小，以满足更好的视觉效果
        ViewGroup.LayoutParams params = viewHolder.ivPicture.getLayoutParams();
        if (getCount() == 1) {
            params.width = (int) (whTemp / 2.7);
            params.height = (int) (whTemp / 2.7);
        } else if (getCount() == 2 || getCount() == 4) {
            whTemp = (int) (MainActivity.width / 1.7);
            params.width = (int) (whTemp / 2);
            params.height = (int) (whTemp / 2);
        } else {
            params.width = (int) (whTemp / 3);
            params.height = (int) (whTemp / 3);
        }
        viewHolder.ivPicture.setLayoutParams(params);
        Glide.with(mContext).load(mPictures.get(position)).into(viewHolder.ivPicture);

        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        ImageView ivPicture;
    }

}
