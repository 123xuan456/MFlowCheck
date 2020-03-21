package com.mtm.flowcheck.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.adapter.VersionLogAdapter;
import com.mtm.flowcheck.bean.VersionLogBean;

import java.util.ArrayList;

import butterknife.BindView;

public class VersionLogActivity extends BaseActivity {

    @BindView(R.id.all_title)
    TextView titleTv;
    @BindView(R.id.into_download)
    Button titleRightBtn;
    @BindView(R.id.lv_version_log_list)
    ListView lvVersionLogList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_versionlog;
    }

    @Override
    public void initView() {
        titleRightBtn.setVisibility(View.GONE);
        titleTv.setText("版本更新日志");
    }

    @Override
    public void initData() {
        ArrayList<VersionLogBean> mListData = new ArrayList<>();
        String prefix = "▪ ";
        mListData.add(new VersionLogBean("全程记录 V1.0", prefix + "2017年-月-日\n" + prefix + "全程记录 V1.0正式上线!"));
        VersionLogAdapter mAdapter = new VersionLogAdapter(lvVersionLogList, mListData);
        lvVersionLogList.setAdapter(mAdapter);
    }
}
