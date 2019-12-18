package com.chen.fy.sharewithas.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;
import com.chen.fy.sharewithas.activities.UserDetailsActivity;
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.interfaces.OnMoreOptionClickListener;
import com.chen.fy.sharewithas.interfaces.OnPicturesItemClickListener;
import com.chen.fy.sharewithas.interfaces.OnUserDetailsClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultipleStatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ShareInfo> mDatas;
    private final int ONE_ITEM = 1;
    private final int TWO_ITEM = 2;

    /**
     * GridView中图片的点击事件
     */
    private OnPicturesItemClickListener mItemClickListener;
    /**
     * 动态的更多选项点击事件
     */
    private OnMoreOptionClickListener mMoreOptionClickListener;

    /**
     * 头像和名称的点击事件
     */
    private OnUserDetailsClickListener mUserDetailsClickListener;

    public MultipleStatesAdapter(Context context) {
        this.mContext = context;
    }

    public void setShareDataList(ArrayList<ShareInfo> shareInfos) {
        mDatas = shareInfos;
    }

    public void setItemClickListener(OnPicturesItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setMoreOptionClickListener(OnMoreOptionClickListener moreOptionClickListener) {
        this.mMoreOptionClickListener = moreOptionClickListener;
    }

    public void setUserDetailsClickListener(OnUserDetailsClickListener onUserDetailsClickListener) {
        this.mUserDetailsClickListener = onUserDetailsClickListener;
    }

    /**
     * 决定每个item的布局类型
     *
     * @param position item
     * @return 布局类型
     */
    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        if (i == ONE_ITEM) {     //第一个布局
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.text_item_layout, viewGroup, false);
            return new TextHolder(view);
        } else if (i == TWO_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.multiple_pictures_item_layout, viewGroup, false);
            return new MultiplePictureHolder(view);
        } else {    //当布局位置错误时
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.text_item_layout, viewGroup, false);
            return new TextHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ShareInfo shareInfo = mDatas.get(i);
        if (viewHolder instanceof TextHolder) {
            setTextItemView((TextHolder) viewHolder, shareInfo, i);
        } else if (viewHolder instanceof MultiplePictureHolder) {
            setMultiplePicturesItemView((MultiplePictureHolder) viewHolder, shareInfo, i);
        }
    }

    /**
     * 设置点击事件
     */
    private void onClick(ImageView ivHeadIcon, TextView tvName, RelativeLayout rlMoreOptionBox, final int position) {
        ivHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDetailsClickListener.onItemClick(position);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDetailsClickListener.onItemClick(position);
            }
        });
        rlMoreOptionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreOptionClickListener.onclick(position, v);
            }
        });
    }

    /**
     * 纯文字子布局
     */
    private void setTextItemView(@NonNull TextHolder viewHolder, ShareInfo shareInfo, final int position) {
        Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.ivHeadIcon);
        viewHolder.tvName.setText(shareInfo.getName());
        viewHolder.tvContent.setText(shareInfo.getContent());
        onClick(viewHolder.ivHeadIcon, viewHolder.tvName, viewHolder.rlMoreOptionBox, position);
    }

    /**
     * 多图片子布局
     */
    private void setMultiplePicturesItemView(@NonNull MultiplePictureHolder viewHolder, final ShareInfo shareInfo, final int position) {
        Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.ivHeadIcon);
        viewHolder.tvName.setText(shareInfo.getName());
        viewHolder.tvContent.setText(shareInfo.getContent());
        if (shareInfo.getPhotos().size() == 2 || shareInfo.getPhotos().size() == 4) {
            ViewGroup.LayoutParams params = viewHolder.rlBox.getLayoutParams();
            params.width = (int) (MainActivity.width / 1.9);
            viewHolder.rlBox.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = viewHolder.rlBox.getLayoutParams();
            params.width = (int) (MainActivity.width / 1.2);
            viewHolder.rlBox.setLayoutParams(params);
        }
        PicturesGridViewAdapter adapter = new PicturesGridViewAdapter(mContext);
        adapter.setPictures(shareInfo.getPhotos());
        viewHolder.gvPictures.setAdapter(adapter);
        viewHolder.gvPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //GridVie点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GridView子布局的最外层布局
                RelativeLayout relativeLayout = (RelativeLayout) parent.getAdapter().
                        getView(position, view, null);
                mItemClickListener.onPicturesItemClick(relativeLayout, position, shareInfo.getPhotos());
            }
        });
        onClick(viewHolder.ivHeadIcon, viewHolder.tvName, viewHolder.rlMoreOptionBox, position);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {

        private ImageView ivHeadIcon;
        private TextView tvName;
        private TextView tvContent;
        private RelativeLayout rlMoreOptionBox;

        TextHolder(@NonNull View itemView) {
            super(itemView);

            ivHeadIcon = itemView.findViewById(R.id.head_icon_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvContent = itemView.findViewById(R.id.tv_content_item);
            rlMoreOptionBox = itemView.findViewById(R.id.rl_more_option_box);
        }
    }

    class MultiplePictureHolder extends RecyclerView.ViewHolder {

        private ImageView ivHeadIcon;
        private TextView tvName;
        private TextView tvContent;
        private RelativeLayout rlBox;
        private GridView gvPictures;
        private RelativeLayout rlMoreOptionBox;

        MultiplePictureHolder(@NonNull View itemView) {
            super(itemView);

            ivHeadIcon = itemView.findViewById(R.id.head_icon_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvContent = itemView.findViewById(R.id.tv_content_item);
            rlBox = itemView.findViewById(R.id.box_gv_pictures);
            gvPictures = itemView.findViewById(R.id.gv_box_multiple_pictures);
            rlMoreOptionBox = itemView.findViewById(R.id.rl_more_option_box);
        }
    }
}


