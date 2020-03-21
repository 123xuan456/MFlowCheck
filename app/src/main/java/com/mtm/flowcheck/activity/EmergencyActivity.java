package com.mtm.flowcheck.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtm.flowcheck.R;

/**
 * Description : 应急指挥
 */

public class EmergencyActivity extends BaseActivity {
    TextView titleTextView;
    ImageView backImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_emergency;
    }

    @Override
    public void initView() {
        titleTextView =(TextView) findViewById(R.id.all_title);
        backImageView = (ImageView) findViewById(R.id.iv_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText("应急指挥");
    }

    @Override
    public void initData() {

    }
}
