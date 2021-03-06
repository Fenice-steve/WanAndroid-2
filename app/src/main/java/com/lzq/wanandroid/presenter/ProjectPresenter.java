package com.lzq.wanandroid.presenter;

import com.lzq.wanandroid.api.Contract;
import com.lzq.wanandroid.api.LoadTasksCallBack;
import com.lzq.wanandroid.api.WebTask;
import com.lzq.wanandroid.base.BasePresenter;
import com.lzq.wanandroid.model.ProjectTree;
import com.lzq.wanandroid.utils.StringUtils;

import java.util.List;

public class ProjectPresenter extends BasePresenter implements Contract.ProjectPresenter, LoadTasksCallBack<List<ProjectTree.DataBean>> {
    private static final String TAG = "WebPresenter";
    private Contract.ProjectView mView;
    private WebTask mTask;

    public static ProjectPresenter createPresenter(Contract.ProjectView mView, WebTask mTask) {
        return new ProjectPresenter(mView, mTask);
    }

    public ProjectPresenter(Contract.ProjectView mView, WebTask mTask) {
        this.mView = mView;
        this.mTask = mTask;
    }


    @Override
    public void initView() {
        String[] title = new String[]{"完整项目", "跨平台应用", "资源聚合类", "动画","RV列表动效","项目基础功能"};
        mView.setEmptyTabView(title);
    }

    @Override
    public void initTabView() {
        mTask.execute(this, StringUtils.TYPE_PROJECT_TREE);
    }

    @Override
    public void initProjectData() {

    }

    @Override
    public void onSuccess(List<ProjectTree.DataBean> data,int...params) {
        switch (params[0]) {
            case StringUtils.TYPE_PROJECT_TREE:
                mView.setTabView(data);
                break;
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public void onFinish() {

    }
}
