package com.mtm.flowcheck.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.LinkBean;

import static com.mtm.flowcheck.bean.LinkBean.LINK_BASE_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DETECTION_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DIAGNOSIS_COURSE;
import static com.mtm.flowcheck.bean.LinkBean.LINK_EPIDEMIC_DISEASE_HISTORY;
import static com.mtm.flowcheck.bean.LinkBean.LINK_OSCULATION_CONTACT;

/**
 * Created By WangYanBin On 2020\03\16 13:33.
 * <p>
 * （SurveyTableAdapter）
 * 参考：
 * 描述：
 */
public class SurveyTableAdapter extends BaseQuickAdapter<LinkBean, BaseViewHolder> {

    public SurveyTableAdapter() {
        super(R.layout.survey_table_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkBean item) {
        switch (item.getLinkType()) {
            case LINK_BASE_INFO:// 基本信息
                helper.setImageResource(R.id.iv_number, R.drawable.ic_link_base_info_selector);
                helper.setText(R.id.tv_link, R.string.link_base_info);
                break;
            case LINK_DIAGNOSIS_COURSE:// 发病就诊过程
                helper.setImageResource(R.id.iv_number, R.drawable.ic_link_diagnosis_course_selector);
                helper.setText(R.id.tv_link, R.string.link_diagnosis_course);
                break;
            case LINK_OSCULATION_CONTACT:// 密接情况
                helper.setImageResource(R.id.iv_number, R.drawable.ic_link_osculation_contact_selector);
                helper.setText(R.id.tv_link, R.string.link_osculation_contact);
                break;
            case LINK_EPIDEMIC_DISEASE_HISTORY:// 流行病学史
                helper.setImageResource(R.id.iv_number, R.drawable.ic_link_epidemic_disease_history_selector);
                helper.setText(R.id.tv_link, R.string.link_epidemic_disease_history);
                helper.setText(R.id.tv_hint, R.string.link_epidemic_disease_history_hint);
                break;
            case LINK_DETECTION_INFO:// 实验室检测信息
                helper.setImageResource(R.id.iv_number, R.drawable.ic_link_detection_info_selector);
                helper.setText(R.id.tv_link, R.string.link_detection_info);
                break;
        }
        helper.getView(R.id.iv_number).setSelected(item.getLinkValue() == 1);
    }
}
