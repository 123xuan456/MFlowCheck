package com.mtm.flowcheck.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.NotStartBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: MFlowCheck
 * @Package: com.mtm.flowcheck.adapter
 * @ClassName: NotStartAdapter
 * @Description: java类作用描述
 * 流行病学调查未开始页适配器
 */
public class NotStartAdapter extends BaseQuickAdapter<CheckBean, BaseViewHolder> {

    public NotStartAdapter(int layoutResId, @Nullable List<CheckBean> data) {
        super(layoutResId, data);
    }

    public NotStartAdapter(@Nullable List<CheckBean> data) {
        super(data);
    }

    public NotStartAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckBean item) {
        helper.setText(R.id.nostart_tv_title, item.getClinicalSeverity());
        if(item.getDiagnosisType()!=null && !"".equals(item.getDiagnosisType())&&"确诊病例".equals(item.getDiagnosisType())){
            helper.setText(R.id.nostart_img_title, "确诊");
        }
        helper.setText(R.id.nostart_tv_name, item.getPatientName());
        helper.setText(R.id.nostart_tv_time, item.getDyqStartDate());
        helper.setText(R.id.nostart_tv_address, item.getIntoHospital());

    }
}
