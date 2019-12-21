package com.chen.fy.sharewithas.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.fy.sharewithas.beans.BaseShareInfoResponse;
import com.chen.fy.sharewithas.beans.ShareInfo;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取动态信息
 */
public class GetShareInfoTask extends AsyncTask<Void, Integer, String> {

    private static final String TAG = "GetShareInfoTask";

    private WeakReference<Activity> activityWeakReference;

    private ArrayList<ShareInfo> shareInfos;

    private Handler handler;

    public GetShareInfoTask(Activity activity, Handler handler, ArrayList<ShareInfo> shareInfos) {
        activityWeakReference = new WeakReference<>(activity);
        this.handler = handler;
        this.shareInfos = shareInfos;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Activity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return "获取动态失败";
        }
        return doGetAllShareInfo();
    }

    @Override
    protected void onPostExecute(String result) {
        parseJson(result);
        handler.sendEmptyMessage(HomeFragment.UPDATE_UI_CODE);
    }

    /**
     * 进行图片上传
     */
    private String doGetAllShareInfo() {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        //setType(MultipartBody.FORM)
        String result = "error";
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constants.SHARE_INFO_URL)
                .get()
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析Json数据
     */
    private void parseJson(String result) {
        Gson gson = new Gson();
        Type jsonType = new TypeToken<List<BaseShareInfoResponse>>() {
        }.getType();
        List<BaseShareInfoResponse> list = gson.fromJson(result, jsonType);
        for (BaseShareInfoResponse shareInfo_base : list) {
            addShareInfoList(shareInfo_base);
        }
    }

    /**
     * 将从服务器获取到的数据新增到数据集中
     * 只添加需要显示的属性
     */
    private void addShareInfoList(BaseShareInfoResponse shareInfo_base) {
        ShareInfo shareInfo = new ShareInfo();

        //id
        shareInfo.setId(shareInfo_base.getId());
        //布局类型
        if (shareInfo_base.getImages().equals("1") || shareInfo_base.getImages().isEmpty() || shareInfo_base.getImages().equals("\"\"")) {
            shareInfo.setType(1);  //文字布局
        } else {
            shareInfo.setType(2);  //图片布局
        }
        //头像
        shareInfo.setHeadUrl("http://" + shareInfo_base.getUser().getHeadUrl());
        //昵称
        shareInfo.setUserName(shareInfo_base.getUser().getUserName());
        //内容
        shareInfo.setContent(shareInfo_base.getContent());
        //图片
        String photoString = shareInfo_base.getImages();
        if (!photoString.isEmpty()) {
            ArrayList<Object> photoList = new ArrayList<>();
            String[] photos = photoString.split(",");
            for (String photo_i : photos) {
                photo_i = "http://" + photo_i;
                photoList.add(photo_i);
            }
            shareInfo.setPhotos(photoList);
        }
        //时间
        shareInfo.setShareDate(shareInfo_base.getShareDate());
        //点赞
        setDoLike(shareInfo, shareInfo_base.getLikes());

        Log.d("shareInfo.Log", shareInfo.toString());
        shareInfos.add(shareInfo);
    }

    private void setDoLike(ShareInfo shareInfo, String likes) {
        Log.d("setDoLike", likes);
        if (likes.length() == 1 && likes.charAt(0) == ',') {
            likes = "";
        }
        if (!likes.isEmpty() && likes.charAt(0) == ',') {
            likes = likes.substring(1, likes.length() - 1);
        }
        if (!likes.isEmpty() && likes.charAt(likes.length() - 1) == ',') {
            likes = likes.substring(0, likes.length() - 2);
        }
        //添加点赞
        shareInfo.setLikes(likes);
    }
}
