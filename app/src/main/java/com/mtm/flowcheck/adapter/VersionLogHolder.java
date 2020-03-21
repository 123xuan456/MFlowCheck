package com.mtm.flowcheck.adapter;

import android.view.View;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.VersionLogBean;
import com.mtm.flowcheck.utils.UIUtils;

public class VersionLogHolder extends BaseHolder {

    private TextView mInfoTv;
    private TextView mTitleTv;

    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.item_version_log);
        mTitleTv = (TextView) view.findViewById(R.id.version_log_title_tv);
        mInfoTv = (TextView)  view.findViewById(R.id.version_log_info_tv);
        return view;
    }

    @Override
    public void refreshView() {
        VersionLogBean info = (VersionLogBean) getData();
        mTitleTv.setText(info.getTitle());
        mInfoTv.setText(info.getInfo());
    }
}
