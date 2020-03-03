package com.chen.fy.sharewithas.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.MainActivity;
import com.chen.fy.sharewithas.model.ShareInfo;
import com.chen.fy.sharewithas.interfaces.OnMoreOptionClickListener;
import com.chen.fy.sharewithas.interfaces.OnPicturesItemClickListener;
import com.chen.fy.sharewithas.interfaces.OnUserDetailsClickListener;
import com.chen.fy.sharewithas.utils.SearchUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ShareInfo> mDatas;
    private final int ONE_ITEM = 1;
    private final int TWO_ITEM = 2;

    private String searchString;

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

    public SearchAdapter(Context context) {
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

    public void setSearchString(String searchString) {
        this.searchString = searchString;
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
    private void onClick(ImageView ivHeadIcon, TextView tvName, RelativeLayout rlMoreOptionBox,
                         final ShareInfo shareInfo, final int position, final Boolean isLikes) {
        ivHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDetailsClickListener.onItemClick(shareInfo, position);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDetailsClickListener.onItemClick(shareInfo, position);
            }
        });
        //点赞，评论按钮
        rlMoreOptionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mMoreOptionClickListener.onclick(position, v, shareInfo, isLikes);
            }
        });
    }

    /**
     * 纯文字子布局
     */
    private void setTextItemView(@NonNull TextHolder viewHolder, ShareInfo shareInfo, final int position) {
        //Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.ivHeadIcon);
        setHeadIcon(shareInfo, viewHolder.ivHeadIcon);

        //设置搜索高亮
        if (shareInfo.getUserName().toLowerCase().contains(searchString.toLowerCase())) {
            viewHolder.tvName.setText(SearchUtils.highLightText(shareInfo.getUserName(), searchString));
        } else {
            viewHolder.tvName.setText(shareInfo.getUserName());
        }
        if (shareInfo.getContent().toLowerCase().contains(searchString.toLowerCase())) {
            viewHolder.tvContent.setText(SearchUtils.highLightText(shareInfo.getContent(), searchString));
        } else {
            viewHolder.tvContent.setText(shareInfo.getContent());
        }

        viewHolder.tvShareDate.setText(shareInfo.getShareDate());
        //点赞
        boolean isLikes = setDoLike(shareInfo, viewHolder.llLikeBox, viewHolder.tvLikesNumber);

        onClick(viewHolder.ivHeadIcon, viewHolder.tvName, viewHolder.rlMoreOptionBox, shareInfo, position, isLikes);
    }

    /**
     * 点赞操作设置
     *
     * @return 该用户是否已经进行过点赞
     */
    private boolean setDoLike(ShareInfo shareInfo, LinearLayout llLikeBox, TextView tvLikesNumber) {
        //1 根据动态是否有点赞信息从而选择是否隐藏点赞
        String likes = shareInfo.getLikes();
        Log.d("Likes", ":--》" + likes);
        if (likes.isEmpty()) {
            llLikeBox.setVisibility(View.GONE);
            return false;
        } else {
            llLikeBox.setVisibility(View.VISIBLE);
            tvLikesNumber.setText(likes);
        }

        //2 获取当前用户id
        String sp_user_info = mContext.getResources().getString(R.string.userInfo_sp_name);
        String id_sp_key = mContext.getResources().getString(R.string.id_sp_key);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(sp_user_info, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(id_sp_key, -1);

        //3 判断是否已经进行登录
        if (userId == -1) {
            return false;
        }

        //4 判断当前用户是否已经点赞过
        String[] likeStrs = likes.split(",");
        for (String str : likeStrs) {
            if (str == null || str.isEmpty()) {
                continue;
            }
            if (str.equals(String.valueOf(userId))) {
                Log.d("点赞", "用户已经进行过点赞");
                return true;
            }
        }
        return false;
    }

    /**
     * 多图片子布局
     */
    private void setMultiplePicturesItemView(@NonNull MultiplePictureHolder viewHolder, final ShareInfo shareInfo, final int position) {
        // Glide.with(mContext).load(shareInfo.getHeadIcon()).into(viewHolder.ivHeadIcon);
        setHeadIcon(shareInfo, viewHolder.ivHeadIcon);

        //设置搜索高亮
        if (shareInfo.getUserName().toLowerCase().contains(searchString.toLowerCase())) {
            viewHolder.tvName.setText(SearchUtils.highLightText(shareInfo.getUserName(), searchString));
        } else {
            viewHolder.tvName.setText(shareInfo.getUserName());
        }
        if (shareInfo.getContent().toLowerCase().contains(searchString.toLowerCase())) {
            viewHolder.tvContent.setText(SearchUtils.highLightText(shareInfo.getContent(), searchString));
        } else {
            viewHolder.tvContent.setText(shareInfo.getContent());
        }

        viewHolder.tvShareDate.setText(shareInfo.getShareDate());
        viewHolder.tvLikesNumber.setText(shareInfo.getLikes());
        if (shareInfo.getPhotos().size() == 2 || shareInfo.getPhotos().size() == 4) {
            ViewGroup.LayoutParams params = viewHolder.rlBox.getLayoutParams();
            params.width = (int) (MainActivity.width / 1.9);
            viewHolder.rlBox.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = viewHolder.rlBox.getLayoutParams();
            params.width = (int) (MainActivity.width / 1.2);
            viewHolder.rlBox.setLayoutParams(params);
        }
        //点赞
        boolean isLikes = setDoLike(shareInfo, viewHolder.llLikeBox, viewHolder.tvLikesNumber);

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
        onClick(viewHolder.ivHeadIcon, viewHolder.tvName, viewHolder.rlMoreOptionBox, shareInfo, position, isLikes);
    }

    private void setHeadIcon(ShareInfo shareInfo, ImageView ivHeadIcon) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions
                .bitmapTransform(roundedCorners)
                .placeholder(R.drawable.img)//图片加载出来前，显示的图片
                .fallback(R.drawable.img)  //url为空的时候,显示的图片
                .error(R.drawable.img);    //图片加载失败后，显示的图片
        Glide.with(mContext).load(shareInfo.getHeadUrl()).apply(options).into(ivHeadIcon);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {

        private ImageView ivHeadIcon;
        private TextView tvName;
        private TextView tvContent;
        private TextView tvShareDate;
        private TextView tvLikesNumber;
        private RelativeLayout rlMoreOptionBox;
        private LinearLayout llLikeBox;

        TextHolder(@NonNull View itemView) {
            super(itemView);

            ivHeadIcon = itemView.findViewById(R.id.head_icon_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvContent = itemView.findViewById(R.id.tv_content_item);
            tvShareDate = itemView.findViewById(R.id.tv_time_bottom_item);
            tvLikesNumber = itemView.findViewById(R.id.tv_likes_number);
            rlMoreOptionBox = itemView.findViewById(R.id.rl_more_option_box);
            llLikeBox = itemView.findViewById(R.id.ll_like_box_text);
        }
    }

    class MultiplePictureHolder extends RecyclerView.ViewHolder {

        private ImageView ivHeadIcon;
        private TextView tvName;
        private TextView tvContent;
        private RelativeLayout rlBox;
        private GridView gvPictures;
        private TextView tvShareDate;
        private TextView tvLikesNumber;
        private RelativeLayout rlMoreOptionBox;
        private LinearLayout llLikeBox;

        MultiplePictureHolder(@NonNull View itemView) {
            super(itemView);

            ivHeadIcon = itemView.findViewById(R.id.head_icon_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvContent = itemView.findViewById(R.id.tv_content_item);
            rlBox = itemView.findViewById(R.id.box_gv_pictures);
            gvPictures = itemView.findViewById(R.id.gv_box_multiple_pictures);
            tvShareDate = itemView.findViewById(R.id.tv_time_bottom_item);
            tvLikesNumber = itemView.findViewById(R.id.tv_likes_number);
            rlMoreOptionBox = itemView.findViewById(R.id.rl_more_option_box);
            llLikeBox = itemView.findViewById(R.id.ll_like_box_pictures);
        }
    }
}


