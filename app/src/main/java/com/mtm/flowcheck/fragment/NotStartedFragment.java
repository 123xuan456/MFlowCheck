package com.mtm.flowcheck.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述：流行病学调查
 * 未开始页
 */
public class NotStartedFragment extends Fragment {

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
        SM2Util sm2Util = new SM2Util();
        Map<String, String> map = new HashMap<String, String>();
        try {
            UserBean user = DBHelper.getInstance(getContext()).getUser();
            map.put("userName", sm2Util.encryptSM2(HTTPUtils.taskDownLoadPubk, user.getUserName()));
            map.put("password", sm2Util.encryptSM2(HTTPUtils.taskDownLoadPubk, "sinosoft2020"));
            map.put("areaCode", sm2Util.encryptSM2(HTTPUtils.taskDownLoadPubk, user.getAreaCode()));
            List<CheckBean> dataList = new ArrayList<CheckBean>();
            HTTPUtils.downloadTask(map, new MyStringCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if (null != response) {
                            SM2Util sm2Util = new SM2Util();
                            response = sm2Util.decryptSM2(HTTPUtils.taskDownLoadPrik, response, "GBK");
                            NotStartBean nBean = JSON.parseObject(response, NotStartBean.class);
                            List<CheckBean> checkBeanList = nBean.getData();
                            if (null != checkBeanList && checkBeanList.size() > 0) {
                                for(int i=0;i<checkBeanList.size();i++) {
                                    CheckBean cInfo = checkBeanList.get(i);
                                    cInfo.setIsDone(0);
                                    if(null == DBHelper.getInstance(getContext()).getTaskByCase(cInfo.getCaseId())) {
                                        DBHelper.getInstance(getContext()).saveTask(cInfo);
                                        for (int j = 1; j <= 5; j++) {
                                            LinkBean linkBean = new LinkBean();
                                            linkBean.setTaskId(checkBeanList.get(i).getCaseId());
                                            linkBean.setLinkType(j);
                                            linkBean.setLinkValue(0);
                                            DBHelper.getInstance(getContext()).saveTaskLinkData(linkBean);
                                        }
                                    }
                                }
                            }
                            List<CheckBean> queryList = DBHelper.getInstance(getContext()).getCheckTaskList(0);
                            if(null != queryList && queryList.size()>0){
                                dataList.addAll(queryList);
                                adapter.setNewData(dataList);
                                notRv.setVisibility(View.VISIBLE);
                                nodataTv.setVisibility(View.GONE);
                            }else {
                                nodataTv.setVisibility(View.VISIBLE);
                                notRv.setVisibility(View.GONE);
                            }
                        }
                        DialogUtils.getInstance().dismissLoadingDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogUtils.getInstance().dismissLoadingDialog();
                        List<CheckBean> queryList = DBHelper.getInstance(getContext()).getCheckTaskList(0);
                        if(null != queryList && queryList.size()>0){
                            dataList.addAll(queryList);
                            adapter.setNewData(dataList);
                            notRv.setVisibility(View.VISIBLE);
                            nodataTv.setVisibility(View.GONE);
                        }else {
                            nodataTv.setVisibility(View.VISIBLE);
                            notRv.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(String response) {
                    ToastUitl.showShort("网络异常,请重试");
                    DialogUtils.getInstance().dismissLoadingDialog();
                    List<CheckBean> queryList = DBHelper.getInstance(getContext()).getCheckTaskList(0);
                    if(null != queryList && queryList.size()>0){
                        dataList.addAll(queryList);
                        adapter.setNewData(dataList);
                        notRv.setVisibility(View.VISIBLE);
                        nodataTv.setVisibility(View.GONE);
                    }else {
                        nodataTv.setVisibility(View.VISIBLE);
                        notRv.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.getInstance().dismissLoadingDialog();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),SurveyTableActivity.class);
                intent.putExtra("taskId",((CheckBean)adapter.getData().get(position)).getCaseId());
                CheckBean checkBean= (CheckBean) adapter.getData().get(position);
                intent.putExtra("checkInfo", JSON.toJSONString(checkBean));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
