package com.chen.fy.sharewithas.asynctasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.applications.ContextApplication;
import com.chen.fy.sharewithas.model.BasePublishResponse;
import com.chen.fy.sharewithas.model.User;
import com.chen.fy.sharewithas.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 发表动态
 */
public class PostShareInfoTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "PostShareInfoTask";

    private WeakReference<Activity> activityWeakReference;


    private ArrayList<Object> mImageList;

    public PostShareInfoTask(Activity activity, ArrayList<Object> mImageList) {
        activityWeakReference = new WeakReference<>(activity);
        this.mImageList = mImageList;
    }

    @Override
    protected String doInBackground(String... params) {

        return doPostHeadIcon(params[0]);
    }

    /**
     * 进行图片上传
     */
    private String doPostHeadIcon(String content) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Activity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return "发表动态失败";
        }

        String spNameKey = activity.getResources().getString(R.string.userInfo_sp_name);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spNameKey, MODE_PRIVATE);

        String idKey = activity.getResources().getString(R.string.id_sp_key);
        int id = sharedPreferences.getInt(idKey, -1);

        //setType(MultipartBody.FORM)
        String result = "error";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", String.valueOf(id))
                .addFormDataPart("content", content);

        int imageCount = mImageList.size();
        for (int i = 0; i < imageCount; i++) {
            int flag = i + 1;
            builder.addFormDataPart("img" + flag, (String) mImageList.get(i),
                    RequestBody.create(MediaType.parse("image/jpg"), new File((String) mImageList.get(i))));
        }

        RequestBody requestBody = builder.build();

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constants.SHARE_INFO_URL)
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

    @Override
    protected void onPostExecute(String result) {
        Activity activity = activityWeakReference.get();
        parseJson(result, activity);
    }

    private void parseJson(String result, Activity activity) {
        Gson gson = new Gson();
        Type jsonType = new TypeToken<BasePublishResponse>() {}.getType();
        BasePublishResponse basePublishResponse = gson.fromJson(result, jsonType);
        Log.d("basePublishResponse",basePublishResponse.toString());
        User user = basePublishResponse.getMoment().getUser();
        Log.d("getUser",user.toString());

        Toast.makeText(ContextApplication.getContext(),
                basePublishResponse.getMsg(),
                Toast.LENGTH_SHORT).show();
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }
}
