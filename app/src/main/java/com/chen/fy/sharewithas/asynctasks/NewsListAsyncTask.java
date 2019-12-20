package com.chen.fy.sharewithas.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.chen.fy.sharewithas.adapters.NewsAdapter;
import com.chen.fy.sharewithas.beans.BaseNewsResponse;
import com.chen.fy.sharewithas.beans.News;
import com.chen.fy.sharewithas.beans.NewsRequest;
import com.chen.fy.sharewithas.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用AsyncTask进行后台数据数据加载
 */
public class NewsListAsyncTask extends AsyncTask<Integer, Void, String> {

    private NewsAdapter mAdapter;
    private List<News> list;
    private WeakReference<SmartRefreshLayout> refreshWeakReference;

    public NewsListAsyncTask(NewsAdapter adapter, List<News> list, SmartRefreshLayout smartRefreshLayout) {
        this.mAdapter = adapter;
        this.list = list;
        this.refreshWeakReference = new WeakReference<>(smartRefreshLayout);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        Integer newsNum = integers[0];  //每页新闻数量
        Integer page = integers[1];     //当前页码

        //1 设置请求参数，不同的接口请求参数可能不一样
        NewsRequest requestObj = new NewsRequest();
        requestObj.setNum(newsNum);
        requestObj.setPage(page);
        String urlParams = requestObj.toString();

        Log.d("FoundFragment.Log", ":" + Constants.GENERAL_NEWS_URL + urlParams);

        //2 创建OkHttp请求对象
        Request request = new Request.Builder()
                .url(Constants.GENERAL_NEWS_URL + urlParams)
                .get().build();
        //3 开始发送同步请求,同步回阻塞当前线程直到服务器返回数据为止
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                return body;
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //对服务器返回的json文件进行解析，并将解析结果封装到一个指定的对象集合中
        Gson gson = new Gson();
        Type jsonType = new TypeToken<BaseNewsResponse<List<News>>>() {
        }.getType();
        BaseNewsResponse<List<News>> newsListResponse = gson.fromJson(s, jsonType);
        list.addAll(newsListResponse.getData());
        //添加新闻成功后，通知适配器更新
        mAdapter.notifyDataSetChanged();

        //取消显示刷新框
        refreshWeakReference.get().finishRefresh();
        refreshWeakReference.get().finishLoadMore();

        super.onPostExecute(s);
    }
}

