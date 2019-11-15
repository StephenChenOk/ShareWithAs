package com.chen.fy.sharewithas.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;
import com.chen.fy.sharewithas.beans.ShareInfo;

import java.util.ArrayList;
import java.util.List;

public class MultipleStatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ShareInfo> mDatas;

    private final int ONE_ITEM = 1;
    private final int TWO_ITEM = 2;
    private final int THREE_ITEM = 3;

    public MultipleStatesAdapter(Context context, ArrayList<ShareInfo> shareInfos) {
        this.context = context;
        mDatas = shareInfos;
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
                    .inflate(R.layout.one_picture_item_layout, viewGroup, false);
            return new OnePictureHolder(view);
        } else if (i == THREE_ITEM) {
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
            ((TextHolder) viewHolder).headIcon.setImageBitmap(shareInfo.getHeadIcon());
            ((TextHolder) viewHolder).name.setText(shareInfo.getName());
            ((TextHolder) viewHolder).content.setText(shareInfo.getContent());
        } else if (viewHolder instanceof OnePictureHolder) {
            ((OnePictureHolder) viewHolder).headIcon.setImageBitmap(shareInfo.getHeadIcon());
            ((OnePictureHolder) viewHolder).name.setText(shareInfo.getName());
            ((OnePictureHolder) viewHolder).content.setText(shareInfo.getContent());
            ((OnePictureHolder) viewHolder).picture.setImageBitmap(shareInfo.getPicture1());
        } else if (viewHolder instanceof MultiplePictureHolder) {
            ((MultiplePictureHolder) viewHolder).headIcon.setImageBitmap(shareInfo.getHeadIcon());
            ((MultiplePictureHolder) viewHolder).name.setText(shareInfo.getName());
            ((MultiplePictureHolder) viewHolder).content.setText(shareInfo.getContent());
            ((MultiplePictureHolder) viewHolder).picture1.setImageBitmap(shareInfo.getPicture1());
            ((MultiplePictureHolder) viewHolder).picture2.setImageBitmap(shareInfo.getPicture2());
            ((MultiplePictureHolder) viewHolder).picture3.setImageBitmap(shareInfo.getPicture3());
        }
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

    class OnePictureHolder extends RecyclerView.ViewHolder {

        private ImageView headIcon;
        private TextView name;
        private TextView content;
        private ImageView picture;

        OnePictureHolder(@NonNull View itemView) {
            super(itemView);

            headIcon = itemView.findViewById(R.id.head_icon_item);
            name = itemView.findViewById(R.id.tv_name_item);
            content = itemView.findViewById(R.id.tv_content_item);
            picture = itemView.findViewById(R.id.iv_picture_one_picture_item);
        }
    }

    class MultiplePictureHolder extends RecyclerView.ViewHolder {

        private ImageView headIcon;
        private TextView name;
        private TextView content;
        private ImageView picture1;
        private ImageView picture2;
        private ImageView picture3;

        MultiplePictureHolder(@NonNull View itemView) {
            super(itemView);

            headIcon = itemView.findViewById(R.id.head_icon_item);
            name = itemView.findViewById(R.id.tv_name_item);
            content = itemView.findViewById(R.id.tv_content_item);
            picture1 = itemView.findViewById(R.id.iv_picture1_more_pictures_item);
            picture2 = itemView.findViewById(R.id.iv_picture2_more_pictures_item);
            picture3 = itemView.findViewById(R.id.iv_picture3_more_pictures_item);
        }
    }
}


