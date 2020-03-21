package com.mtm.flowcheck.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.CalendarView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.activity.AreaSelectActivity;
import com.mtm.flowcheck.adapter.LinkContentItemAdapter;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.item.LinkContentItemBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mtm.flowcheck.bean.LinkBean.LINK_BASE_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DETECTION_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DIAGNOSIS_COURSE;
import static com.mtm.flowcheck.bean.LinkBean.LINK_EPIDEMIC_DISEASE_HISTORY;
import static com.mtm.flowcheck.bean.LinkBean.LINK_OSCULATION_CONTACT;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_INPUT;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_JUMP_ACTIVITY;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_MULTIPLE_CHOOSE;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_SINGLE_CHOOSE;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_TIME_CHOOSE;

/**
 * Created By WangYanBin On 2020\03\17 10:52.
 * <p>
 * （LinkContentHelper）
 * 参考：
 * 描述：
 */
public class LinkContentItemHelper implements LinkContentItemAdapter.TextWatcherCallback {

    private Context mContext;
    private CheckBean checkInfo;

    public LinkContentItemHelper(Context context, CheckBean checkInfo) {
        this.mContext = context;
        this.checkInfo = checkInfo;
    }

    public List<LinkContentItemBean> getLinkContentItem(int linkCode) {
        List<LinkContentItemBean> mList = new ArrayList<>();
        switch (linkCode) {
            case LINK_BASE_INFO:// 基本信息
                mList.addAll(getBaseInfoLinkItem());
                break;
            case LINK_DIAGNOSIS_COURSE:// 发病就诊过程
                mList.addAll(getDiagnosisCourseLinkItem());
                break;
            case LINK_OSCULATION_CONTACT:// 密接情况
                mList.addAll(getOsculationContactLinkItem());
                break;
            case LINK_EPIDEMIC_DISEASE_HISTORY:// 流行病学史
                mList.addAll(getEpidemicDiseaseHistoryLinkItem());
                break;
            case LINK_DETECTION_INFO:// 实验室检测信息
                mList.addAll(getDetectionInfoLinkItem());
                break;
        }
        return mList;
    }

    /**
     * 获取基本信息条目
     *
     * @return
     */
    public List<LinkContentItemBean> getBaseInfoLinkItem() {
        List<LinkContentItemBean> mList = new ArrayList<>();
        mList.add(new LinkContentItemBean(R.string.diagnose_order, R.string.name_hint, "confirmedOrder", checkInfo.getConfirmedOrder(), OPERATION_TYPE_INPUT));
        String[] confirmedSourcesOptions = new String[]{"大网确诊", "专家确诊"};
        mList.add(new LinkContentItemBean(R.string.diagnose_source, R.string.name_select, "confirmedSources", checkInfo.getConfirmedSources(), confirmedSourcesOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        String[] diagnosisTypeOptions = new String[]{"确诊病例", "疑似病例", "阳性检测"};
        mList.add(new LinkContentItemBean(R.string.case_type, R.string.name_select, "diagnosisType", checkInfo.getDiagnosisType(), diagnosisTypeOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.name, R.string.name_hint, "patientName", checkInfo.getPatientName(), OPERATION_TYPE_INPUT));
        mList.add(new LinkContentItemBean(R.string.id_number, R.string.name_hint, "idCard", checkInfo.getIdCard(), OPERATION_TYPE_INPUT));
        String[] genderOptions = new String[]{"男", "女"};
        mList.add(new LinkContentItemBean(R.string.sex, R.string.name_select, "gender", checkInfo.getGender(), genderOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.birthday, R.string.name_hint, "birthdayDate", checkInfo.getBirthdayDate(), OPERATION_TYPE_TIME_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.age, R.string.name_hint, "age", checkInfo.getAge(), OPERATION_TYPE_INPUT));
        mList.add(new LinkContentItemBean(R.string.phone, R.string.name_hint, "telecom", checkInfo.getTelecom(), OPERATION_TYPE_INPUT));
        mList.add(new LinkContentItemBean(R.string.case_report_area, R.string.name_select, "regionCase", checkInfo.getRegionCase(), OPERATION_TYPE_JUMP_ACTIVITY));
        String[] areaTypeOptions = new String[]{"中国籍", "外籍"};
        mList.add(new LinkContentItemBean(R.string.patients_belong, R.string.name_select, "areaType", checkInfo.getAreaType(), areaTypeOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.area, R.string.name_select, "addrCode", checkInfo.getAddrCode(), OPERATION_TYPE_JUMP_ACTIVITY));
        mList.add(new LinkContentItemBean(R.string.work_unit, R.string.name_hint, "unit", checkInfo.getUnit(), OPERATION_TYPE_INPUT));
        return mList;
    }

    /**
     * 获取发病就诊过程条目
     *
     * @return
     */
    public List<LinkContentItemBean> getDiagnosisCourseLinkItem() {
        List<LinkContentItemBean> mList = new ArrayList<>();
        String[] professionalOptions = new String[]{"幼托儿童", "散居儿童", "学生", "教师", "保育员及保姆", "餐饮食品业", "公共场所服务员", "商业服务", "医务人员",
                "工人", "民工", "农民", "牧民", "渔(船)民", "海员及长途驾驶员", "干部职员", "离退人员", "家务及待业", "不详", "其它"};
        mList.add(new LinkContentItemBean(R.string.staff, R.string.name_select, "professional", checkInfo.getProfessional(), professionalOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.morbidity_date, R.string.name_select, "epiStartDate", checkInfo.getEpiStartDate(), OPERATION_TYPE_TIME_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.clathria_morbidity_date, R.string.name_select, "dyqStartDate", checkInfo.getDyqStartDate(), OPERATION_TYPE_TIME_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.just_copy_date, R.string.name_select, "clinicDate", checkInfo.getClinicDate(), OPERATION_TYPE_TIME_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.confirmed_date, R.string.name_select, "diagnoseDate", checkInfo.getDiagnoseDate(), OPERATION_TYPE_TIME_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.report_hospital, R.string.name_select, "reportHospital", checkInfo.getReportHospital(), OPERATION_TYPE_JUMP_ACTIVITY));
        mList.add(new LinkContentItemBean(R.string.into_hospital, R.string.name_select, "intoHospital", checkInfo.getIntoHospital(), OPERATION_TYPE_JUMP_ACTIVITY));
        mList.add(new LinkContentItemBean(R.string.tohospital_date, R.string.name_select, "intoHospitalDate", checkInfo.getIntoHospitalDate(), OPERATION_TYPE_TIME_CHOOSE, true));
        String[] clinicalSeverityOptions = new String[]{"轻症病例", "重症肺炎", "危重症肺炎", "无症状感染者", "普通肺炎"};
        mList.add(new LinkContentItemBean(R.string.clinic, R.string.name_select, "clinicalSeverity", checkInfo.getClinicalSeverity(), clinicalSeverityOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        String[] outcomeOptions = new String[]{"痊愈", "死亡"};
        mList.add(new LinkContentItemBean(R.string.vest, R.string.name_select, "outcome", checkInfo.getOutcome(), outcomeOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        mList.add(new LinkContentItemBean(R.string.vest_date, R.string.name_select, "outcomeDate", checkInfo.getOutcomeDate(), OPERATION_TYPE_TIME_CHOOSE));
        String[] caseTypeOptions = new String[]{"确诊病例", "疑似病例", "阳性检测"};
        mList.add(new LinkContentItemBean(R.string.case_type, R.string.name_select, "caseType", checkInfo.getCaseType(), caseTypeOptions, OPERATION_TYPE_SINGLE_CHOOSE));
        String[] ifchronicDiseaseOptions = new String[]{"有", "无"};
        mList.add(new LinkContentItemBean(R.string.chronic_disease, R.string.name_select, "ifchronicDisease", checkInfo.getIfchronicDisease(), ifchronicDiseaseOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.ichronic_disease_note, R.string.name_select, "ichronicDiseaseNote", checkInfo.getIchronicDiseaseNote(), OPERATION_TYPE_INPUT, true));
        mList.add(new LinkContentItemBean(R.string.wbc, R.string.name_hint, "wbc", checkInfo.getWbc(), OPERATION_TYPE_INPUT, true));
        String[] ifFeverOptions = new String[]{"是", "否"};
        mList.add(new LinkContentItemBean(R.string.if_fever, R.string.name_select, "ifFever", checkInfo.getIfFever(), ifFeverOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.body_temp, R.string.name_select, "bodyTemp", checkInfo.getBodyTemp(), OPERATION_TYPE_INPUT, true));
        String[] symptomsOptions = new String[]{"寒战", "干咳", "咳痰", "鼻塞", "流涕", "咽痛", "头痛", "乏力", "肌肉酸痛", "关节酸痛", "气促", "呼吸困难", "胸闷", "胸痛", "结膜充血", "恶心", "呕吐", "腹泻", "腹痛"};
        mList.add(new LinkContentItemBean(R.string.symptoms, R.string.name_select, "symptoms", checkInfo.getSymptoms(), symptomsOptions, OPERATION_TYPE_MULTIPLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.symptoms_oth, R.string.name_select, "symptomsOth", checkInfo.getSymptomsOth(), OPERATION_TYPE_INPUT, true));
        return mList;
    }

    /**
     * 获取密接情况条目
     *
     * @return
     */
    public List<LinkContentItemBean> getOsculationContactLinkItem() {
        List<LinkContentItemBean> mList = new ArrayList<>();
        mList.add(new LinkContentItemBean(R.string.provinces_contact, R.string.name_hint, "provContNum", checkInfo.getProvContNum(), OPERATION_TYPE_INPUT, true));
        mList.add(new LinkContentItemBean(R.string.family_contact, R.string.name_hint, "famlContNum", checkInfo.getFamlContNum(), OPERATION_TYPE_INPUT, true));
        return mList;
    }

    /**
     * 获取流行病学史条目
     *
     * @return
     */
    public List<LinkContentItemBean> getEpidemicDiseaseHistoryLinkItem() {
        List<LinkContentItemBean> mList = new ArrayList<>();
        String[] infectOriginSortOptions = new String[]{"确诊病例或阳性人员接触史", "疑似病例接触史", "京外居住史", "京外旅行史", "其他人员接触史"};
        mList.add(new LinkContentItemBean(R.string.infect_origin_sort, R.string.name_select, "infectOriginSort", checkInfo.getInfectOriginSort(), infectOriginSortOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.infect_city, R.string.name_select, "infectCity", checkInfo.getInfectCity(), OPERATION_TYPE_JUMP_ACTIVITY, true));
        String[] infectOrigToOptions = new String[]{"来京", "返京"};
        mList.add(new LinkContentItemBean(R.string.infect_orig_to, R.string.name_select, "infectOrigTo", checkInfo.getInfectOrigTo(), infectOrigToOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        String[] trafficToolsOptions = new String[]{"火车", "飞机", "大巴", "出租车", "其他"};
        mList.add(new LinkContentItemBean(R.string.transport, R.string.name_select, "trafficTools", checkInfo.getTrafficTools(), trafficToolsOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.reveal_history, R.string.name_hint, "suspExposurehis", checkInfo.getSuspExposurehis(), OPERATION_TYPE_INPUT, true));
        String[] ifFamilyOptions = new String[]{"是", "否"};
        mList.add(new LinkContentItemBean(R.string.is_famlily_member, R.string.name_select, "ifFamily", checkInfo.getIfFamily(), ifFamilyOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.previous_ralationship, R.string.name_hint, "relationship", checkInfo.getRelationship(), OPERATION_TYPE_INPUT, true));
        String[] inputLocalOptions = new String[]{"输入性", "本地病例"};
        mList.add(new LinkContentItemBean(R.string.case_style, R.string.name_select, "inputLocal", checkInfo.getInputLocal(), inputLocalOptions, OPERATION_TYPE_SINGLE_CHOOSE, true));
        mList.add(new LinkContentItemBean(R.string.first_expose, R.string.name_select, "initialExposure", checkInfo.getInitialExposure(), OPERATION_TYPE_INPUT, true));
        mList.add(new LinkContentItemBean(R.string.last_expose, R.string.name_select, "lastExposure", checkInfo.getLastExposure(), OPERATION_TYPE_INPUT, true));
        mList.add(new LinkContentItemBean(R.string.passage_duration, R.string.name_hint, "durationExten", checkInfo.getDurationExten(), OPERATION_TYPE_INPUT, true));
        return mList;
    }

    /**
     * 获取实验室检测信息条目
     *
     * @return
     */
    public List<LinkContentItemBean> getDetectionInfoLinkItem() {
        List<LinkContentItemBean> mList = new ArrayList<>();
        return mList;
    }

    /**
     * 设置值
     *
     * @param fieldName
     * @param value
     */
    public void setValue(String fieldName, String value) {
        switch (fieldName) {
            case "confirmedOrder":// 确诊顺序
                checkInfo.setConfirmedOrder(value);
                break;
            case "confirmedSources":// 确诊来源
                checkInfo.setConfirmedSources(value);
                break;
            case "diagnosisType":// 病例类型
                checkInfo.setDiagnosisType(value);
                break;
            case "patientName":// 姓名
                checkInfo.setPatientName(value);
                break;
            case "idCard":// 身份证号
                checkInfo.setIdCard(value);
                break;
            case "gender":// 性别
                checkInfo.setGender(value);
                break;
            case "birthdayDate":// 出生日期
                checkInfo.setBirthdayDate(value);
                break;
            case "age":// 年龄
                checkInfo.setAge(value);
                break;
            case "telecom":// 联系电话
                checkInfo.setTelecom(value);
                break;
            case "regionCase":// 病例报告地区（8位地区编码）
                checkInfo.setRegionCase(value);
                break;
            case "areaType":// 病人属于
                checkInfo.setAreaType(value);
                break;
            case "addrCode":// 所在区县/街道（8位地区编码）
                checkInfo.setAddrCode(value);
                break;
            case "unit":// 工作单位
                checkInfo.setUnit(value);
                break;
            case "professional":// 人员分类
                checkInfo.setProfessional(value);
                break;
            case "epiStartDate":// 流调发病日期
                checkInfo.setEpiStartDate(value);
                break;
            case "dyqStartDate":// 大网发病日期
                checkInfo.setDyqStartDate(value);
                break;
            case "clinicDate":// 就诊日期
                checkInfo.setClinicDate(value);
                break;
            case "diagnoseDate":// 确诊日期
                checkInfo.setDiagnoseDate(value);
                break;
            case "reportHospital":// 报告医院
                checkInfo.setReportHospital(value);
                break;
            case "intoHospital":// 转入医院
                checkInfo.setIntoHospital(value);
                break;
            case "intoHospitalDate":// 入院日期
                checkInfo.setIntoHospitalDate(value);
                break;
            case "clinicalSeverity":// 临床严重程度
                checkInfo.setClinicalSeverity(value);
                break;
            case "outcome":// 转归
                checkInfo.setOutcome(value);
                break;
            case "outcomeDate":// 转归日期
                checkInfo.setOutcomeDate(value);
                break;
            case "caseType":// 病例类型
                checkInfo.setCaseType(value);
                break;
            case "ifchronicDisease":// 有无慢性病
                checkInfo.setIfchronicDisease(value);
                break;
            case "ichronicDiseaseNote":// 有慢性病（慢性病名称）
                checkInfo.setIchronicDiseaseNote(value);
                break;
            case "wbc":// WBC（*109）
                checkInfo.setWbc(value);
                break;
            case "ifFever":// 是否发热
                checkInfo.setIfFever(value);
                break;
            case "bodyTemp":// 体温（℃）
                checkInfo.setBodyTemp(value);
                break;
            case "symptoms":// 症状（以||分隔）
                checkInfo.setSymptoms(value);
                break;
            case "symptomsOth":// 其他症状名称
                checkInfo.setSymptomsOth(value);
                break;
            case "provContNum":// 外省密接数
                checkInfo.setProvContNum(value);
                break;
            case "famlContNum":// 家庭密接人数
                checkInfo.setFamlContNum(value);
                break;
            case "infectOriginSort":// 感染来源分类
                checkInfo.setInfectOriginSort(value);
                break;
            case "infectCity":// 感染来源省市（8位地区编码）
                checkInfo.setInfectCity(value);
                break;
            case "infectOrigTo":// 感染来源去向
                checkInfo.setInfectOrigTo(value);
                break;
            case "trafficTools":// 交通工具
                checkInfo.setTrafficTools(value);
                break;
            case "suspExposurehis":// 可疑暴露史
                checkInfo.setSuspExposurehis(value);
                break;
            case "ifFamily":// 是否家庭成员
                checkInfo.setIfFamily(value);
                break;
            case "relationship":// 与上一代病例关系
                checkInfo.setRelationship(value);
                break;
            case "inputLocal":// 输入性/本地病例
                checkInfo.setInputLocal(value);
                break;
            case "initialExposure":// 初次暴露日期
                checkInfo.setInitialExposure(value);
                break;
            case "lastExposure":// 末次暴露日期
                checkInfo.setLastExposure(value);
                break;
            case "durationExten":// 传代时长
                checkInfo.setDurationExten(value);
                break;
        }
    }

    // -------------------------------------------------- 监听器 --------------------------------------------------

    @Override
    public void afterTextChanged(BaseQuickAdapter adapter, int position, Editable s) {
        LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
        setValue(itemInfo.getFieldName(), s.toString());
    }

    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position, int linkCode) {
        LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
        switch (itemInfo.getOperationType()) {
            case OPERATION_TYPE_SINGLE_CHOOSE:// 单选
                showSingleChooseDialog(adapter, position);
                break;
            case OPERATION_TYPE_MULTIPLE_CHOOSE:// 多选
                showMultipleChooseDialog(adapter, position);
                break;
            case OPERATION_TYPE_TIME_CHOOSE:// 时间选择
                showTimeChooseDialog(adapter, position);
                break;
            case OPERATION_TYPE_JUMP_ACTIVITY:// 跳转页面
                jumpActivity(adapter, position, linkCode);
                break;
        }
    }

    /**
     * 显示单选弹窗
     *
     * @param adapter
     * @param position
     */
    private void showSingleChooseDialog(BaseQuickAdapter adapter, int position) {
        LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
        String[] options = itemInfo.getOptions();
        MdDialogUtils.getInstance().createSingleDialog(mContext, options,
                (MaterialDialog dialog, View view, int which, CharSequence text) -> {
                    itemInfo.setContent(options[which]);
                    adapter.notifyItemChanged(position, itemInfo);
                    setValue(itemInfo.getFieldName(), options[which]);
                    return true;
                }
        ).show();
    }

    /**
     * 显示多选弹窗
     *
     * @param adapter
     * @param position
     */
    private void showMultipleChooseDialog(BaseQuickAdapter adapter, int position) {
        LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
        String[] options = itemInfo.getOptions();
        MdDialogUtils.getInstance().createMultiSelect(mContext, options,
                (MaterialDialog dialog, Integer[] which, CharSequence[] text) -> {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < which.length; i++) {
                        sb.append(options[which[i]]).append("||");
                    }
                    sb = new StringBuffer(sb.subSequence(0, sb.length() - 2));
                    itemInfo.setContent(sb.toString());
                    adapter.notifyItemChanged(position, itemInfo);
                    setValue(itemInfo.getFieldName(), sb.toString());
                    return true;
                }
        ).positiveText("确认").show();
    }

    /**
     * 显示时间选择弹窗
     *
     * @param adapter
     * @param position
     */
    private void showTimeChooseDialog(BaseQuickAdapter adapter, int position) {
        View view = View.inflate(mContext, R.layout.viewsub_calendar, null);
        CalendarView mCalendarView = view.findViewById(R.id.calendar_view);
        mCalendarView.setOnDateChangeListener((@NonNull CalendarView v, int year, int month, int dayOfMonth) -> {
                    LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v.getDate());
                    itemInfo.setContent(date);
                    adapter.notifyItemChanged(position, itemInfo);
                    setValue(itemInfo.getFieldName(), date);
                }
        );
        new MaterialDialog.Builder(mContext).customView(view, true).show();
    }

    /**
     * 跳转页面
     *
     * @param adapter
     * @param position
     */
    private void jumpActivity(BaseQuickAdapter adapter, int position, int linkCode) {
        LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
        switch (itemInfo.getFieldName()) {
            case "regionCase":// 病例报告地区
            case "addrCode":// 所在区县/街道
            case "reportHospital":// 报告医院
            case "intoHospital":// 转入医院
            case "infectCity":// 感染来源省/市
                Intent intent = new Intent(mContext, AreaSelectActivity.class);
                intent.putExtra("linkCode", linkCode);
                intent.putExtra("position", position);
                intent.putExtra("fieldName", itemInfo.getFieldName());
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_REGION);
                break;
        }
    }

    public final static int REQUEST_CODE_REGION = 0x1;// 地区

    public void onActivityResult(BaseQuickAdapter adapter, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_REGION:
                int position = data.getIntExtra("position", -1);
                String content = data.getStringExtra("content");
                if (position != -1) {
                    LinkContentItemBean itemInfo = (LinkContentItemBean) adapter.getItem(position);
                    itemInfo.setContent(content);
                    adapter.notifyItemChanged(position, itemInfo);
                    setValue(itemInfo.getFieldName(), content);
                }
                break;
        }
    }
}
