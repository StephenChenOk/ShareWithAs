package com.chen.fy.sharewithas.adapters;

import android.content.Context;
import android.icu.util.LocaleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;

import java.util.ArrayList;

public class PublishGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Object> mUriList;

    public PublishGridViewAdapter(Context context) {
        mContext = context;
    }

    public void setUris(ArrayList<Object> list) {
        mUriList = list;
    }

    /**
     * 决定了ListView中一共有多少个Item
     */
    @Override
    public int getCount() {
        return (mUriList == null) ? 0 : mUriList.size();
    }

    /**
     * 它也不会被自动调用，它是用来在我们设置setOnItemClickListener、setOnItemLongClickListener、
     * setOnItemSelectedListener的点击选择处理事件中方便地调用来获取当前行数据的。
     * 官方解释:Implementers can call getItemAtPosition(position) if they need to access the data
     */
    @Override
    public Object getItem(int position) {
        return mUriList.get(position);
    }

    /**
     * 返回的是该position对应item的id,adapterView也有类似方法：
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
            view = LayoutInflater.from(mContext).inflate(R.layout.gv_publish_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = view.findViewById(R.id.gv_publish_iv_picture);

            view.setTag(viewHolder);
        } else {    //若缓冲池中已经有view则可以直接用holder对象
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

//        ViewGroup.LayoutParams params = viewHolder.ivPicture.getLayoutParams();
//        params.width = MainActivity.width / 3 - MainActivity.width / 10;
//        params.height = MainActivity.width / 3 - MainActivity.width / 10;
//        viewHolder.ivPicture.setLayoutParams(params);
        if (mUriList.size() != 9 && position == mUriList.size() - 1){
            Glide.with(mContext)
                    .load(Integer.valueOf(String.valueOf(mUriList.get(position))))
                    .apply(new RequestOptions().fitCenter())
                    .into(viewHolder.ivPicture);
        } else{
            Glide.with(mContext)
                    .load(mUriList.get(position))
                    .into(viewHolder.ivPicture);
        }
        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        ImageView ivPicture;
    }

}
