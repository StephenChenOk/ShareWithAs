package com.chen.fy.sharewithas.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.activities.NewsDetailActivity;
import com.chen.fy.sharewithas.adapters.NewsAdapter;
import com.chen.fy.sharewithas.asynctasks.NewsListAsyncTask;
import com.chen.fy.sharewithas.model.News;
import com.chen.fy.sharewithas.constants.Constants;
import com.chen.fy.sharewithas.interfaces.OnNewsItemClickListener;
import com.chen.fy.sharewithas.utils.UiUtils;
import com.chen.fy.sharewithas.vassonic.SonicJavaScriptInterface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class FoundFragment extends Fragment implements OnNewsItemClickListener {

    private static final int MODE_SONIC = 1;

    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;

    private String DEMO_URL;

    private View mView;

    private NewsAdapter mNewsAdapter;
    private List<News> newsList;
    private RecyclerView rvNewsList;
    private SmartRefreshLayout mRefreshLayout;

    private int page = 1;

    //第一次进入界面
    private boolean isFirstEnter = true;

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
        mRefreshLayout = mView.findViewById(R.id.refreshLayout_found);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
        rvNewsList.setLayoutManager(layoutManager);
    }

    private void initData() {
        newsList = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getContext(), R.layout.news_list_item_layout);
        mNewsAdapter.setNewsList(newsList);
        mNewsAdapter.setOnItemClickListener(this);
        rvNewsList.setAdapter(mNewsAdapter);

        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新
        }

        //顶部下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });

        //底部下拉刷新，加载更多新闻
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mNewsAdapter.getItemCount() > 5 * Constants.NEWS_NUM) {
                            Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            addMoreNews();
                        }
                    }
                }, 0);
            }
        });
    }

    /**
     * 顶部下拉刷新，重新刷新新闻
     */
    private void refreshData() {
        newsList.clear();
        page = 1;
        //创建一个AsyncTask对象进行网络请求
        NewsListAsyncTask newsListAsyncTask = new NewsListAsyncTask(mNewsAdapter, newsList, mRefreshLayout);
        newsListAsyncTask.execute(new Integer[]{Constants.NEWS_NUM, page});
        page++;
    }

    /**
     * 底部下拉刷新。添加新闻
     */
    private void addMoreNews() {
        //创建一个AsyncTask对象进行网络请求
        NewsListAsyncTask newsListAsyncTask = new NewsListAsyncTask(mNewsAdapter, newsList, mRefreshLayout);
        newsListAsyncTask.execute(new Integer[]{Constants.NEWS_NUM, page});
        //请求页码++
        page++;
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

    @Override
    public void onItemClick(int position) {
        startNewsDetailActivity(MODE_SONIC, position);
    }
}


