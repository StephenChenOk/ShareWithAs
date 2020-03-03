package com.chen.fy.sharewithas.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.sharewithas.R;
import com.chen.fy.sharewithas.adapters.SearchAdapter;
import com.chen.fy.sharewithas.model.ShareInfo;
import com.chen.fy.sharewithas.fragments.HomeFragment;
import com.chen.fy.sharewithas.interfaces.MyOnPicturesItemClickListener;
import com.chen.fy.sharewithas.interfaces.OnUserDetailsClickListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etSearch;
    private TextView tvSearch;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;

    private ArrayList<ShareInfo> mSearchLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        initView();
    }

    private void initView() {
        etSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
        etSearch.addTextChangedListener(this);

        mRecyclerView = findViewById(R.id.rv_search);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);//1 表示列数
        mRecyclerView.setLayoutManager(layoutManager);

        mSearchAdapter = new SearchAdapter(this);
        mSearchLists = new ArrayList<>();
        mSearchAdapter.setShareDataList(mSearchLists);
        mSearchAdapter.setItemClickListener(new MyOnPicturesItemClickListener());
        //mSearchAdapter.setMoreOptionClickListener(new MyOnMoreOptionClickListener(this));
        mSearchAdapter.setUserDetailsClickListener(new OnUserDetailsClickListener(this));

        mRecyclerView.setAdapter(mSearchAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                String searchUsername = etSearch.getText().toString();
                doSearch(searchUsername.toString());
                break;
        }
    }

    private void doSearch(String s) {
        if (!mSearchLists.isEmpty()) {
            mSearchLists.clear();
        }
        if (s.isEmpty()) {
            mSearchLists.clear();
            mSearchAdapter.notifyDataSetChanged();
            return;
        }
        for (ShareInfo shareInfo : HomeFragment.mShareInfos) {
            if (shareInfo.getUserName().toLowerCase().contains(s.toLowerCase())) {
                mSearchLists.add(shareInfo);
                continue;
            }
            if (shareInfo.getContent().toLowerCase().contains(s.toLowerCase())) {
                mSearchLists.add(shareInfo);
            }
        }
        mSearchAdapter.setSearchString(s);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Log.d("beforeTextChanged", "" + s + "," + start + "," + count + "," + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        doSearch(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
