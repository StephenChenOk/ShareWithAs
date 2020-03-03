package com.chen.fy.sharewithas.asynctasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.model.ShareInfo;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 点赞
 */
public class DoLikeTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "PostHeadIconTask";

    private WeakReference<Activity> activityWeakReference;

    private ShareInfo shareInfo;

    private Handler handler;

    public DoLikeTask(Activity activity, ShareInfo shareInfo, Handler handler) {
        activityWeakReference = new WeakReference<>(activity);
        this.shareInfo = shareInfo;
        this.handler = handler;
    }


    @Override
    protected String doInBackground(String... param) {
        return doLike(param[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Activity activity = activityWeakReference.get();
        parseJson(result, activity);
    }

    /**
     * 点赞
     * 根据参数修改请求参数
     */
    private String doLike(String likes) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Activity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return "失败";
        }

        String spNameKey = activity.getResources().getString(R.string.userInfo_sp_name);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spNameKey, MODE_PRIVATE);

        String userNameKey = activity.getResources().getString(R.string.userName_sp_key);
        String userName = sharedPreferences.getString(userNameKey, "");
        int moment_id = shareInfo.getId();

        //setType(MultipartBody.FORM)
        String result = "error";

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constants.SHARE_INFO_URL + "/" + likes + "?moment_id=" + moment_id + "&user_name=" + userName)
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

    private void parseJson(String result, Activity activity) {
        JsonParser parser = new JsonParser();//Json解析
        final JsonObject jsonObject = (JsonObject) parser.parse(result);

        String msgKey = activity.getResources().getString(R.string.msg_key);

        //更新UI
        handler.sendEmptyMessage(HomeFragment.REFRESH_CODE);

        Toast.makeText(activity, jsonObject.get(msgKey).toString(), Toast.LENGTH_SHORT).show();
    }
}
