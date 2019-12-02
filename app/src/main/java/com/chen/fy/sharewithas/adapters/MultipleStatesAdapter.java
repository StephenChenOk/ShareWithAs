package com.chen.fy.sharewithas.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.interfaces.OnPicturesItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultipleStatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ShareInfo> mDatas;
    private final int ONE_ITEM = 1;
    private final int TWO_ITEM = 2;

    private OnPicturesItemClickListener mItemClickListener;


    public MultipleStatesAdapter(Context context) {
        this.mContext = context;
    }

    public void setShareDataList(ArrayList<ShareInfo> shareInfos) {
        mDatas = shareInfos;
    }

    public void setItemClickListener(OnPicturesItemClickListener listener) {
        this.mItemClickListener = listener;
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
            setTextItemView((TextHolder) viewHolder, shareInfo);
        } else if (viewHolder instanceof MultiplePictureHolder) {
            setMultiplePicturesItemView((MultiplePictureHolder) viewHolder, shareInfo);
        }
    }

    /**
     * 纯文字子布局
     */
    private void setTextItemView(@NonNull TextHolder viewHolder, ShareInfo shareInfo) {
        Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.headIcon);
        viewHolder.name.setText(shareInfo.getName());
        viewHolder.content.setText(shareInfo.getContent());
    }

    /**
     * 多图片子布局
     */
    private void setMultiplePicturesItemView(@NonNull MultiplePictureHolder viewHolder, final ShareInfo shareInfo) {
        Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.headIcon);
        viewHolder.name.setText(shareInfo.getName());
        viewHolder.content.setText(shareInfo.getContent());
        if (shareInfo.getPhotos().size() == 2 || shareInfo.getPhotos().size() == 4) {
            ViewGroup.LayoutParams params = viewHolder.gvBox.getLayoutParams();
            params.width = (int) (MainActivity.width / 1.9);
            viewHolder.gvBox.setLayoutParams(params);
        }
        PicturesGridViewAdapter adapter = new PicturesGridViewAdapter(mContext);
        adapter.setPictures(shareInfo.getPhotos());
        viewHolder.pictures.setAdapter(adapter);
        viewHolder.pictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GridView子布局的最外层布局
                RelativeLayout relativeLayout = (RelativeLayout) parent.getAdapter().
                        getView(position, view, null);
                mItemClickListener.onPicturesItemClick(relativeLayout, position, shareInfo.getPhotos());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {

        private ImageView headIcon;
        private TextView name;
        private TextView content;

        TextHolder(@NonNull View itemView) {
            super(itemView);

            headIcon = itemView.findViewById(R.id.head_icon_item);
            name = itemView.findViewById(R.id.tv_name_item);
            content = itemView.findViewById(R.id.tv_content_item);
        }
    }

    class MultiplePictureHolder extends RecyclerView.ViewHolder {

        private ImageView headIcon;
        private TextView name;
        private TextView content;
        private RelativeLayout gvBox;
        private GridView pictures;

        MultiplePictureHolder(@NonNull View itemView) {
            super(itemView);

            headIcon = itemView.findViewById(R.id.head_icon_item);
            name = itemView.findViewById(R.id.tv_name_item);
            content = itemView.findViewById(R.id.tv_content_item);
            gvBox = itemView.findViewById(R.id.box_gv_pictures);
            pictures = itemView.findViewById(R.id.gv_box_multiple_pictures);
        }
    }
}


