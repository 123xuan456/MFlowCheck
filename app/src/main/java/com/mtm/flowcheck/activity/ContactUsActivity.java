package com.mtm.flowcheck.activity;

import android.view.View;
import android.widget.TextView;

import com.mtm.flowcheck.R;

import butterknife.BindView;

/**
 * Description : 联系我们
 */

public class ContactUsActivity extends BaseActivity {
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.all_title)
    TextView allTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_contact_us;
    }

    @Override
    public void initView() {
        allTitle.setText("联系我们");
    }

    @Override
    public void initData() {

    }

}