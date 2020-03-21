package com.mtm.flowcheck.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created By WangYanBin On 2020\03\17 10:54.
 * <p>
 * （CheckBean）
 * 参考：
 */
public class CheckBean111 {
    private String caseId;// 主键id
    // 基本信息
    private String confirmedOrder;// 确诊顺序
    private String confirmedSources;// 确诊来源
    private String diagnosisType;// 病例类型
    private String patientName;// 姓名
    private String idCard;// 身份证号
    private String gender;// 性别
    private String birthdayDate;// 出生日期
    private String age;// 年龄
    private String telecom;// 联系电话
    private String regionCase;// 病例报告地区（8位地区编码）
    private String areaType;// 病人属于
    private String addrCode;// 所在区县/街道（8位地区编码）
    private String unit;// 工作单位
    // 发病就诊过程
    private String professional;// 人员分类
    private String epiStartDate;// 流调发病日期
    private String dyqStartDate;// 大网发病日期
    private String clinicDate;// 就诊日期
    private String diagnoseDate;// 确诊日期
    private String reportHospital;// 报告医院
    private String intoHospital;// 转入医院
    private String intoHospitalDate;// 入院日期
    private String clinicalSeverity;// 临床严重程度
    private String outcome;// 转归
    private String outcomeDate;// 转归日期
    private String caseType;// 病例类型
    private String ifchronicDisease;// 有无慢性病
    private String ichronicDiseaseNote;// 有慢性病（慢性病名称）
    private String wbc;// WBC（*109）
    private String ifFever;// 是否发热
    private String bodyTemp;// 体温（℃）
    private String symptoms;// 症状（以||分隔）
    private String symptomsOth;// 其他症状名称
    // 密接情况
    private String provContNum;// 外省密接数
    private String famlContNum;// 家庭密接人数
    // 流行病学史
    private String infectOriginSort;// 感染来源分类
    private String infectCity;// 感染来源省市（8位地区编码）
    private String infectOrigTo;// 感染来源去向
    private String trafficTools;// 交通工具
    private String suspExposurehis;// 可疑暴露史
    private String ifFamily;// 是否家庭成员
    private String relationship;// 与上一代病例关系
    private String inputLocal;// 输入性/本地病例
    private String initialExposure;// 初次暴露日期
    private String lastExposure;// 末次暴露日期
    private String durationExten;// 传代时长
    // 其他
    private String reportUser;// 填报人（登录用户名）
    private String cardCode;// 病例卡片编号



    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getConfirmedOrder() {
        return this.confirmedOrder;
    }

    public void setConfirmedOrder(String confirmedOrder) {
        this.confirmedOrder = confirmedOrder;
    }

    public String getConfirmedSources() {
        return this.confirmedSources;
    }

    public void setConfirmedSources(String confirmedSources) {
        this.confirmedSources = confirmedSources;
    }

    public String getDiagnosisType() {
        return this.diagnosisType;
    }

    public void setDiagnosisType(String diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public String getPatientName() {
        return this.patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirthdayDate() {
        return this.birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTelecom() {
        return this.telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public String getRegionCase() {
        return this.regionCase;
    }

    public void setRegionCase(String regionCase) {
        this.regionCase = regionCase;
    }

    public String getAreaType() {
        return this.areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAddrCode() {
        return this.addrCode;
    }

    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProfessional() {
        return this.professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getEpiStartDate() {
        return this.epiStartDate;
    }

    public void setEpiStartDate(String epiStartDate) {
        this.epiStartDate = epiStartDate;
    }

    public String getDyqStartDate() {
        return this.dyqStartDate;
    }

    public void setDyqStartDate(String dyqStartDate) {
        this.dyqStartDate = dyqStartDate;
    }

    public String getClinicDate() {
        return this.clinicDate;
    }

    public void setClinicDate(String clinicDate) {
        this.clinicDate = clinicDate;
    }

    public String getDiagnoseDate() {
        return this.diagnoseDate;
    }

    public void setDiagnoseDate(String diagnoseDate) {
        this.diagnoseDate = diagnoseDate;
    }

    public String getIntoHospitalDate() {
        return this.intoHospitalDate;
    }

    public void setIntoHospitalDate(String intoHospitalDate) {
        this.intoHospitalDate = intoHospitalDate;
    }

    public String getIntoHospital() {
        return this.intoHospital;
    }

    public void setIntoHospital(String intoHospital) {
        this.intoHospital = intoHospital;
    }

    public String getReportHospital() {
        return this.reportHospital;
    }

    public void setReportHospital(String reportHospital) {
        this.reportHospital = reportHospital;
    }

    public String getClinicalSeverity() {
        return this.clinicalSeverity;
    }

    public void setClinicalSeverity(String clinicalSeverity) {
        this.clinicalSeverity = clinicalSeverity;
    }

    public String getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeDate() {
        return this.outcomeDate;
    }

    public void setOutcomeDate(String outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getIfchronicDisease() {
        return this.ifchronicDisease;
    }

    public void setIfchronicDisease(String ifchronicDisease) {
        this.ifchronicDisease = ifchronicDisease;
    }

    public String getIchronicDiseaseNote() {
        return this.ichronicDiseaseNote;
    }

    public void setIchronicDiseaseNote(String ichronicDiseaseNote) {
        this.ichronicDiseaseNote = ichronicDiseaseNote;
    }

    public String getIfFever() {
        return this.ifFever;
    }

    public void setIfFever(String ifFever) {
        this.ifFever = ifFever;
    }

    public String getSymptoms() {
        return this.symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSymptomsOth() {
        return this.symptomsOth;
    }

    public void setSymptomsOth(String symptomsOth) {
        this.symptomsOth = symptomsOth;
    }

    public String getBodyTemp() {
        return this.bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getWbc() {
        return this.wbc;
    }

    public void setWbc(String wbc) {
        this.wbc = wbc;
    }

    public String getProvContNum() {
        return this.provContNum;
    }

    public void setProvContNum(String provContNum) {
        this.provContNum = provContNum;
    }

    public String getFamlContNum() {
        return this.famlContNum;
    }

    public void setFamlContNum(String famlContNum) {
        this.famlContNum = famlContNum;
    }

    public String getInfectOriginSort() {
        return this.infectOriginSort;
    }

    public void setInfectOriginSort(String infectOriginSort) {
        this.infectOriginSort = infectOriginSort;
    }

    public String getInfectCity() {
        return this.infectCity;
    }

    public void setInfectCity(String infectCity) {
        this.infectCity = infectCity;
    }

    public String getInfectOrigTo() {
        return this.infectOrigTo;
    }

    public void setInfectOrigTo(String infectOrigTo) {
        this.infectOrigTo = infectOrigTo;
    }

    public String getTrafficTools() {
        return this.trafficTools;
    }

    public void setTrafficTools(String trafficTools) {
        this.trafficTools = trafficTools;
    }

    public String getSuspExposurehis() {
        return this.suspExposurehis;
    }

    public void setSuspExposurehis(String suspExposurehis) {
        this.suspExposurehis = suspExposurehis;
    }

    public String getIfFamily() {
        return this.ifFamily;
    }

    public void setIfFamily(String ifFamily) {
        this.ifFamily = ifFamily;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getInputLocal() {
        return this.inputLocal;
    }

    public void setInputLocal(String inputLocal) {
        this.inputLocal = inputLocal;
    }

    public String getInitialExposure() {
        return this.initialExposure;
    }

    public void setInitialExposure(String initialExposure) {
        this.initialExposure = initialExposure;
    }

    public String getLastExposure() {
        return this.lastExposure;
    }

    public void setLastExposure(String lastExposure) {
        this.lastExposure = lastExposure;
    }

    public String getDurationExten() {
        return this.durationExten;
    }

    public void setDurationExten(String durationExten) {
        this.durationExten = durationExten;
    }

    public String getReportUser() {
        return this.reportUser;
    }

    public void setReportUser(String reportUser) {
        this.reportUser = reportUser;
    }

    public String getCardCode() {
        return this.cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

}
