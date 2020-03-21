package com.mtm.flowcheck.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.adapter.LinkContentItemAdapter;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.utils.LinkContentItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mtm.flowcheck.bean.LinkBean.LINK_BASE_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DETECTION_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DIAGNOSIS_COURSE;
import static com.mtm.flowcheck.bean.LinkBean.LINK_EPIDEMIC_DISEASE_HISTORY;
import static com.mtm.flowcheck.bean.LinkBean.LINK_OSCULATION_CONTACT;
import static com.mtm.flowcheck.utils.LinkContentItemHelper.REQUEST_CODE_REGION;

/**
 * 流行病学调查表详情
 */
public class CheckTableDetailsActivity extends BaseActivity {

    private LinearLayout contentLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_table_details;
    }

    private CheckBean checkInfo;
    private LinkContentItemHelper mLinkContentItemHelper;

    private void init() {
        checkInfo = JSON.parseObject(getIntent().getStringExtra("checkInfo"), CheckBean.class);
        mLinkContentItemHelper = new LinkContentItemHelper(this, checkInfo);
    }

    @Override
    public void initView() {
        init();
        ((TextView) findViewById(R.id.all_title)).setText(R.string.epidemic_disease_table_details);
        findViewById(R.id.iv_back).setOnClickListener((View v) -> finish());
        contentLayout = findViewById(R.id.ll_content);
        contentLayout.removeAllViews();
        contentLayout.addView(getLinkView(LINK_BASE_INFO, R.string.link_base_info));
        contentLayout.addView(getLinkView(LINK_DIAGNOSIS_COURSE, R.string.link_diagnosis_course));
        contentLayout.addView(getLinkView(LINK_OSCULATION_CONTACT, R.string.link_osculation_contact));
        contentLayout.addView(getLinkView(LINK_EPIDEMIC_DISEASE_HISTORY, R.string.link_epidemic_disease_history));
        contentLayout.addView(getLinkView(LINK_DETECTION_INFO, R.string.link_detection_info));
    }

    private List<Integer> linkList = new ArrayList<>();
    private Map<Integer, TextView> linkTvMap = new HashMap<>();
    private Map<Integer, LinearLayout> linkItemLayoutMap = new HashMap<>();
    private Map<Integer, LinkContentItemAdapter> adapterMap = new HashMap<>();
    private Map<Integer, Boolean> isUnfoldMap = new HashMap<>();

    private View getLinkView(final int linkCode, int linkStrId) {
        View view = LayoutInflater.from(this).inflate(R.layout.check_table_details_link, null);
        TextView linkTv = view.findViewById(R.id.tv_link);// 环节
        linkTv.setText(linkStrId);
        linkTv.setOnClickListener((View v) -> unfoldLinkItem(linkCode));
        LinearLayout itemLayout = view.findViewById(R.id.ll_item);// 条目布局
        RecyclerView mRecyclerView = view.findViewById(R.id.mRecyclerView);// 列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        LinkContentItemAdapter mAdapter = new LinkContentItemAdapter(linkCode, mLinkContentItemHelper);
        mAdapter.setOnItemChildClickListener((BaseQuickAdapter adapter, View v, int position) -> mLinkContentItemHelper.onItemChildClick(adapter, v, position, linkCode));
        mAdapter.setNewData(mLinkContentItemHelper.getLinkContentItem(linkCode));
        mRecyclerView.setAdapter(mAdapter);
        ImageButton unfoldBtn = view.findViewById(R.id.ib_unfold);// 展开按钮
        if (mAdapter.getItemCount() > 7) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
            params.height = dip2px(this, 357);
            mRecyclerView.setLayoutParams(params);
            unfoldBtn.setSelected(false);
        } else {
            unfoldBtn.setVisibility(View.GONE);
        }
        isUnfoldMap.put(linkCode, false);
        unfoldBtn.setOnClickListener((View v) -> {
            boolean isUnfold = isUnfoldMap.get(linkCode);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
            params1.height = isUnfold ? dip2px(this, 357) : WRAP_CONTENT;
            mRecyclerView.setLayoutParams(params1);
            isUnfoldMap.put(linkCode, !isUnfold);
            unfoldBtn.setSelected(!isUnfold);
        });
        // 列出，以备后用
        linkList.add(linkCode);
        linkTvMap.put(linkCode, linkTv);
        linkItemLayoutMap.put(linkCode, itemLayout);
        adapterMap.put(linkCode, mAdapter);
        return view;
    }

    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    private void unfoldLinkItem(int linkCode) {
        for (int link : linkList) {
            linkTvMap.get(link).setSelected(link == linkCode);
            linkItemLayoutMap.get(link).setVisibility(link == linkCode ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void initData() {
        unfoldLinkItem(LINK_BASE_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_REGION:// 地区
                    int linkCode = data.getIntExtra("linkCode", -1);
                    if (linkCode != -1) {
                        BaseQuickAdapter adapter = adapterMap.get(linkCode);
                        mLinkContentItemHelper.onActivityResult(adapter, requestCode, resultCode, data);
                    }
                    break;
            }
        }
    }
}
