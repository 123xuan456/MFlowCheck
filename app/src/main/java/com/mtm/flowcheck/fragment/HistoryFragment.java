package com.mtm.flowcheck.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.activity.SurveyTableActivity;
import com.mtm.flowcheck.adapter.NotStartAdapter;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.LinkBean;
import com.mtm.flowcheck.bean.NotStartBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.listeners.MyStringCallback;
import com.mtm.flowcheck.utils.DialogUtils;
import com.mtm.flowcheck.utils.HTTPUtils;
import com.mtm.flowcheck.utils.ToastUitl;
import com.sinosoft.key.SM2Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *  类描述：流行病学调查
 *  历史记录页
 * */
public class HistoryFragment extends Fragment {
    @BindView(R.id.not_rv)
    RecyclerView notRv;
    Unbinder unbinder;
    @BindView(R.id.nodata_tv)
    TextView nodataTv;
    NotStartAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_not_started_fragment, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
    }

    private void initview() {
        notRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotStartAdapter(R.layout.fragment_notstart);
        notRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),SurveyTableActivity.class);
                intent.putExtra("checkInfo",JSON.toJSONString(adapter.getData().get(position)));
                startActivity(intent);
            }
        });
//        initData();
    }

    private void initData() {
        DialogUtils.getInstance().showLoadingDialog(getActivity(),"正在加载数据...");
        try {
            List<CheckBean> queryList = DBHelper.getInstance(getContext()).getCheckTaskList(1);
            if(null != queryList && queryList.size()>0){
                adapter.setNewData(queryList);
                notRv.setVisibility(View.VISIBLE);
                nodataTv.setVisibility(View.GONE);
            }else {
                nodataTv.setVisibility(View.VISIBLE);
                notRv.setVisibility(View.GONE);
            }
            DialogUtils.getInstance().dismissLoadingDialog();
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.getInstance().dismissLoadingDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
