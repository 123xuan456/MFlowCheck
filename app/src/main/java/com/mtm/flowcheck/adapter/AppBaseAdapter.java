package com.mtm.flowcheck.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Description: Adapter基类
 * Android:minSdkVersion: API
 * Version:V1.0
 */

public abstract class AppBaseAdapter<Data> extends BaseAdapter {
    protected AbsListView mListView;//和该adapter关联的listView

    private List<Data> mDatas;
    public AppBaseAdapter(AbsListView listView, List<Data> datas) {

        mListView = listView;
        if (null != datas) {
            setData(datas);
        }
    }
    public AppBaseAdapter(AbsListView listView, List<Data> datas, String type) {

        mListView = listView;
        if (null != datas) {
            setData(datas);
        }
    }
    public List<Data> getData() {
        return mDatas;
    }

    public void setData(List<Data> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 追加数据
     * @param list
     */
    public void appendAll(List<Data> list) {
        this.mDatas.addAll(list);
        this.notifyDataSetChanged();
    }
    public void updateData(List<Data> list) {
        this.mDatas.clear();
        this.mDatas.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<Data> holder;
        if (convertView != null && convertView.getTag() instanceof BaseHolder) {
            holder = (BaseHolder<Data>) convertView.getTag();
        } else {
            holder = getHolder();
        }
        holder.setData(mDatas.get(position));
        return holder.getRootView();

    }
    protected abstract BaseHolder getHolder();
}
