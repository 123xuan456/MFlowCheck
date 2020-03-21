package com.mtm.flowcheck.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.adapter.SurveyTableAdapter;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.utils.printer.MicroBrotherPrinterActivity;

/**
 * 类描述：流行病学调查表
 */
public class SurveyTableActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_survey_table;
    }

    private CheckBean checkInfo;

    private void init() {
        checkInfo = JSON.parseObject(getIntent().getStringExtra("checkInfo"), CheckBean.class);
    }

    private RecyclerView mRecyclerView;
    private SurveyTableAdapter mAdapter = new SurveyTableAdapter();

    @Override
    public void initView() {
        init();
        ((TextView) findViewById(R.id.all_title)).setText(R.string.epidemic_disease_table);
        findViewById(R.id.iv_back).setOnClickListener((View v) -> finish());
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position) -> {
            Intent intent = new Intent(SurveyTableActivity.this, CheckActivity.class);
            intent.putExtra("checkInfo", JSON.toJSONString(checkInfo));
            intent.putExtra("lickList", JSON.toJSONString(mAdapter.getData()));
            intent.putExtra("position", position);
            startActivity(intent);
        });
        Button btn = (Button)findViewById(R.id.into_download);
        btn.setText("打印");
        btn.setOnClickListener((View v) -> startActivity(new Intent(this, MicroBrotherPrinterActivity.class)));
    }


    @Override
    public void initData() {
        mAdapter.setNewData(DBHelper.getInstance(this).getLinkInfoByTaskId(checkInfo.getCaseId()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }
}
