package com.lzq.wanandroid.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.lzq.wanandroid.api.Contract;
import com.lzq.wanandroid.api.FlowTagCallBack;
import com.lzq.wanandroid.api.WebTask;
import com.lzq.wanandroid.base.BaseFragment;
import com.lzq.wanandroid.model.Children;
import com.lzq.wanandroid.model.Data;
import com.lzq.wanandroid.model.Event;
import com.lzq.wanandroid.presenter.TreePresenter;
import com.lzq.wanandroid.R;
import com.lzq.wanandroid.utils.StringUtils;
import com.lzq.wanandroid.view.adapter.NaviAdapter;
import com.lzq.wanandroid.view.WebActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NaviFragment extends BaseFragment implements Contract.TreeView, FlowTagCallBack {
    private static final String TAG = "NaviFragment";
    @BindView(R.id.tree_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.tree_refresh)
    SmartRefreshLayout mRefreshView;
    private Contract.TreePresenter mPresenter;
    private List<Data> mList = new ArrayList<>();
    private NaviAdapter mAdapter;
    private View mView;

    public NaviFragment() {
    }

    public static NaviFragment newInstance() {
        return new NaviFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree, container, false);
        ButterKnife.bind(this, view);
        mView = view;
        if (mPresenter == null) {
            WebTask mTask = WebTask.getInstance();
            mPresenter = TreePresenter.createPresenter(this, mTask);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mPresenter.initView(StringUtils.TYPE_TREE_NAVI);
        mPresenter.loadOnline(StringUtils.TYPE_TREE_NAVI);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.loadOnline(StringUtils.TYPE_TREE_NAVI);
                if (onFinishLoad()) {
                    mRefreshView.finishRefresh();
                }
            }
        });

        return view;
    }

    @Override
    public void setPresenter(Contract.TreePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initDataList(int type, List<Data> data) {
        mAdapter = new NaviAdapter(R.layout.rv_tree_item, data, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onlineLoad(int type) {
        mPresenter.loadOnline(type);
    }


    @Override
    public void onLoadTreeData(int type, List<Data> data) {
        mAdapter = new NaviAdapter(R.layout.rv_tree_item, data, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onFinishLoad() {
        return true;
    }

    @Override
    public void goWebActivity(String URL) {
        Intent intent = new Intent(ActivityUtils.getActivityByView(mView), WebActivity.class);
        intent.putExtra("URL", URL);
        startActivity(mView, intent);
    }

    @Override
    public void getTreeLink(String URL) {
        Intent intent = new Intent(ActivityUtils.getActivityByView(mView), WebActivity.class);
        intent.putExtra("URL", URL);
        startActivity(mView, intent);
    }

    @Override
    public void getTreeArticles(int ID, int position, String title, List<Children> children) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.target == Event.TARGET_RESFRESH) {
            mRefreshView.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
