package com.mtm.flowcheck.adapter;

import android.view.View;
import android.widget.AbsListView;

/**
 * Holder 基类
 */
public abstract class BaseHolder<Data> {
    protected View mRootView;
    protected int mPosition;
    protected Data mData;
    private AbsListView listView;

    public BaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);
    }

    public View getRootView() {
        return mRootView;
    }

    public void setData(Data data) {
        mData = data;
        refreshView();
    }

    public Data getData() {
        return mData;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }


    /**
     * 子类必须覆盖用于实现UI初始化
     */
    protected abstract View initView();

    /**
     * 子类必须覆盖用于实现UI刷新
     */
    public abstract void refreshView();


    public void setListView(AbsListView listView) {
        this.listView = listView;
    }

}
