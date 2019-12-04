package com.chen.fy.sharewithas.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.NewsDetailActivity;
import com.chen.fy.sharewithas.adapters.NewsAdapter;
import com.chen.fy.sharewithas.beans.BaseResponse;
import com.chen.fy.sharewithas.beans.News;
import com.chen.fy.sharewithas.beans.NewsRequest;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.interfaces.OnItemClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.chen.fy.sharewithas.vassonic.SonicJavaScriptInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoundFragment extends Fragment implements OnItemClickListener {

    private static final int MODE_SONIC = 1;

    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;

    private String DEMO_URL;

    private NewsAdapter mNewsAdapter;
    private List<News> newsList;
    private RecyclerView rvNewsList;

    private View mView;

    private int page = 1;

    private int mCurrentColIndex = 3;

    /**
     * 请求的数量
     */
    private int[] mCols = new int[]{Constants.NEWS_COL5, Constants.NEWS_COL7, Constants.NEWS_COL8,
            Constants.NEWS_COL10, Constants.NEWS_COL11};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.found_fragment_layout, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    /**
     * Fragment在show与hide状态转换时调用此方法
     *
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getActivity() != null) {
            UiUtils.changeStatusBarTextImgColor(getActivity(), true);
        }
    }

    private void initView() {
        rvNewsList = mView.findViewById(R.id.rv_news);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        rvNewsList.setLayoutManager(layoutManager);
    }

    private void initData() {
        newsList = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getContext(), R.layout.news_list_item_layout);
        mNewsAdapter.setNewsList(newsList);
        mNewsAdapter.setOnItemClickListener(this);
        rvNewsList.setAdapter(mNewsAdapter);
        refreshData(page);
    }

    /**
     * 使用AsyncTask发起请求获取新闻数据
     *
     * @param page 请求的页面
     */
    private void refreshData(final int page) {

        //构建AsyncTask对象，传入请求需要的参数，再开启AsyncTask
        new NewsListAsyncTask(mNewsAdapter, newsList).execute(new Integer[]{
                mCols[mCurrentColIndex],
                Constants.NEWS_NUM, page});

    }

    @Override
    public void onItemClick(int position) {
        startNewsDetailActivity(MODE_SONIC, position);
    }

    //跳转新闻详情界面
    private void startNewsDetailActivity(int mode, int position) {
        News news = newsList.get(position);
        DEMO_URL = news.getContentUrl();
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.PARAM_URL, DEMO_URL);
        intent.putExtra(NewsDetailActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        startActivityForResult(intent, -1);
    }
}

/**
 * 使用AsyncTask进行后台数据数据加载
 */
class NewsListAsyncTask extends AsyncTask<Integer, Void, String> {

    private NewsAdapter mAdapter;
    private List<News> list;

    NewsListAsyncTask(NewsAdapter adapter, List<News> list) {
        this.mAdapter = adapter;
        this.list = list;
    }

    /**
     * 后台任务开始之前调用，通常用来任务的初始化操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 执行后台耗时操作，已在子线程中执行
     *
     * @param integers 构造对象时传进来的第一个参数
     * @return 返回结果
     */
    @Override
    protected String doInBackground(Integer... integers) {
        Integer col = integers[0];      //请求API所需的频道
        Integer newsNum = integers[0];  //每页新闻数量
        Integer page = integers[0];     //当前页码

        //1 设置请求参数，不同的接口请求参数可能不一样
        NewsRequest requestObj = new NewsRequest();
        requestObj.setCol(col);
        requestObj.setNum(newsNum);
        requestObj.setPage(page);
        String urlParams = requestObj.toString();

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

    /**
     * 后台任务执行完毕时调用,此时已经回到UI线程
     *
     * @param s 后台执行任务完成的返回值
     */
    @Override
    protected void onPostExecute(String s) {
        //对服务器返回的json文件进行解析，并将解析结果封装到一个指定的对象集合中
        Gson gson = new Gson();
        Type jsonType = new TypeToken<BaseResponse<List<News>>>() {
        }.getType();
        BaseResponse<List<News>> newsListResponse = gson.fromJson(s, jsonType);
        list.addAll(newsListResponse.getData());
        //添加新闻成功后，通知适配器更新
        mAdapter.setNewsList(list);
        mAdapter.notifyDataSetChanged();
        super.onPostExecute(s);
    }
}


