package com.chen.fy.sharewithas.asynctasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.applications.ContextApplication;
import com.chen.fy.sharewithas.constants.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 修改头像
 */
public class PostHeadIconTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "PostHeadIconTask";

    private WeakReference<Activity> activityWeakReference;

    public PostHeadIconTask(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... params) {

        return doPostHeadIcon(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Activity activity = activityWeakReference.get();
        parseJson(result, activity);
    }

    /**
     * 进行图片上传
     */
    private String doPostHeadIcon(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Activity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return "图片上传失败";
        }

        String spNameKey = activity.getResources().getString(R.string.userInfo_sp_name);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spNameKey, MODE_PRIVATE);

        String idKey = activity.getResources().getString(R.string.id_sp_key);
        int id = sharedPreferences.getInt(idKey, -1);

        //setType(MultipartBody.FORM)
        String result = "error";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", String.valueOf(id))
                .addFormDataPart("file", imagePath,                         //图片
                        RequestBody.create(MediaType.parse("image/jpg"), new File(imagePath)))
                .build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constants.HEAD_ICON_SERVER_URL)
                .post(requestBody)
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
        try {
            //1 获取JsonObject对象
            JsonObject jsonObject = (JsonObject) parser.parse(result);
            Log.d(TAG, jsonObject.toString());

            //2 分别取出数据
            String msg1 = jsonObject.get("msg").toString();
            String msg = msg1.substring(1, msg1.length() - 1);

            String newHeadUrl1 = jsonObject.get("newHeadUrl").toString();
            String newHeadUrl = newHeadUrl1.substring(1, newHeadUrl1.length() - 1);

            //3 保存数据
            SharedPreferences.Editor editorUserInfo = activity.getSharedPreferences("userInfo", MODE_PRIVATE).edit();
            editorUserInfo.putString("headUrl", "http://" + newHeadUrl);
            Log.d("editorUserInfo","http://" + newHeadUrl);
            editorUserInfo.apply();

            Toast.makeText(ContextApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
