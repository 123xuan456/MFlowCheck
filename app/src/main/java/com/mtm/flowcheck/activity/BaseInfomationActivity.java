package com.mtm.flowcheck.activity;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtm.flowcheck.R;

import butterknife.BindView;

/**
 * Description : 基本信息
 */
public class BaseInfomationActivity extends BaseActivity {

    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.unit_title1)
    TextView unitTitle1;
    @BindView(R.id.linear_jbxx)
    LinearLayout linearJbxx;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_base_infomation;
    }

    @Override
    public void initView() {
        allTitle.setText("基本信息");

    }

    @Override
    public void initData() {

    }

}
