package com.mtm.flowcheck.utils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Response;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.CheckBean111;
import com.mtm.flowcheck.bean.UploadAudioAndTxtBean;
import com.mtm.flowcheck.bean.json.GetFileServersTokenJson;
import com.mtm.flowcheck.listeners.StandardStringCallback;
import com.mtm.flowcheck.listeners.MyStringCallback;
import com.sinosoft.key.SM2Util;

import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class HTTPUtils {

    // -------------------------------------------------- 初始化 --------------------------------------------------

    public static void init(Application application) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);// log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO); // log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);// 非必要情况，不建议使用，第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        OkGo.getInstance().init(application).setOkHttpClient(builder.build());// 必须调用初始化
    }

    // -------------------------------------------------- 接口 --------------------------------------------------

    public final static String URL_ADDRESS_SERVER = "http://118.190.134.46:8888/services/reportService";// URL地址 - 服务器
    public final static String NAME_SPACE = "http://sinosoft.iig.com/";
    public final static String URL_ADDRESS_LOGIN = "checkUserInfo";// URL地址 - 登录
    public final static String URL_DOWNLOAD_TASK = "tEpiNcovInfoList";// URL地址 - 下载任务
    public final static String URL_UPLOAD_TASK =  "inputNcovInfo";// URL地址 - 上传任务
    public final static String URL_UPLOAD_AUDIO_AND_TXT = "upLoadFile";// URL地址 - 上传音频与文本文件
    public final static String URL_UPLOAD_OTHER_ATTACHMENT = URL_ADDRESS_SERVER + "upLoadOtherFile";// URL地址 - 上传其他附件

    //登录公钥
    public final static String loginPubk="044ECA6E410E278A53292B8A1803A5E7A6517ECB52AD01DF9D2BEEF2AC0A1601B02E74E5FD39DF4017C61BA5DE57879EF4C195208F6CD7F3AE47E875856E2193F8";
    //登录私钥
    public final static String lgonPrik="269D6E421EA66BDEA78C911CA3B8EADCFE23E574994DAC9DE025318DD99EE288";


    //语音和文本上传公钥
    public final static String uploadFilePubk="048B012B29157B15B3FE94DC53E4FE20868AFD80E380204E070FD2CE09387B716831D8D3CC3AB0D140850D0E9DA25487C80984078AD0C00416FF23FAC2B7738FE1";
    //语音和文本上传私钥
    public final static String uploadFilePrik="009B7ED51EBEF8962EFB09864F0D21CCC7433128761AF74C00E77355BACFDEA5D5";

    //流调数据上传公钥
    public final static String uploadDataPubk="046B75B6882290AB0770C0D5C82501732D72DCC33F7B0BFCE2F2F2C10D8754CCD89620D337DCA19BE2CF004D715A278179137706864CC4516F6A2028BCA8FEF1AE";
    //流调数据上传私钥
    public final static String uploadDataPrik="00B7A5F5C72CB28C7969C37FD0FB2FD9E4F7348881303327B3B91E9602A7A4DED5";

    //流调任务下载公钥
    public final static String taskDownLoadPubk="049FF0AAFF3B18B21B433C3BCF8BECFA5EED4919706D5ED53B8C67F1339AD40B960E00E39283CC27FDBA1E56F3EEBB4BD1C3167BF74BEA020FD8B2C11B372A562F";
    //流调任务下载私钥
    public final static String taskDownLoadPrik="44F0C00A8965816588E6F9CA3EDCBDFC701601E7BB0EFF6CC07C81B278EB140B";



    public final static String SUCCESS = "0"; // 成功
    public final static String FAILURE = "1"; // 失败

    public final static String REQUEST_FAILURE = "-1";// 请求失败
    public final static String UNKNOWN_ERROR = "-2";// 未知异常
    public final static String OPERATION_SUCCESS = "1000";// 操作成功
    public final static String ADD_FATILED = "1001";// 添加失败
    public final static String DELETE_SUCCESS = "1002";// 删除成功
    public final static String MODIFY_FATILED = "1003";// 修改失败
    public final static String QUERY_FATILED = "1004";// 查询失败


    public static boolean isNetworkAvailable(Context ctx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 用户登录
     *
     * @param username        用户名
     * @param password        密码
     * @param mStringCallback
     */
    public static void userLogin(String username, String password, MyStringCallback mStringCallback) {
        WebserviceUtil w = new WebserviceUtil();//实例化webservice+ksoap工具类
        w.setMethodName(URL_ADDRESS_LOGIN);//设置webservice请求方法
        w.setUrl(URL_ADDRESS_SERVER);
        w.addParams("arg0", username);
        w.addParams("arg1", password);
        w.exec(mStringCallback);//请求
    }

    /**
     * 下载任务
     *
     * @param mStringCallback
     */
    public static void downloadTask(Map<String, String> map, MyStringCallback mStringCallback) {
        WebserviceUtil w = new WebserviceUtil();//实例化webservice+ksoap工具类
        w.setMethodName(URL_DOWNLOAD_TASK);//设置webservice请求方法
        w.setUrl(URL_ADDRESS_SERVER);
        w.addParams("arg0", map.get("userName"));
        w.addParams("arg1", map.get("password"));
        w.addParams("arg2", map.get("areaCode"));
        w.exec(mStringCallback);//请求
    }

    /**
     * 上传任务
     *
     * @param checkInfo
     * @param mStringCallback
     */
    public static void uploadTask(String userName, String passWord, CheckBean111 checkInfo, MyStringCallback mStringCallback) throws Exception {
        SM2Util mSM2Util = new SM2Util();
        WebserviceUtil mUtil = new WebserviceUtil();
        mUtil.setUrl(URL_ADDRESS_SERVER);
        mUtil.setMethodName(URL_UPLOAD_TASK);
        String userName1 = "dctest";
        String password1 = "sinosoft2020";
        String s=JSON.toJSONString(checkInfo);
        mUtil.addParams("arg0",mSM2Util.encryptSM2(uploadDataPubk, JSON.toJSONString(checkInfo)));
        mUtil.addParams("arg1",mSM2Util.encryptSM2(uploadDataPubk, userName1));
        mUtil.addParams("arg2",mSM2Util.encryptSM2(uploadDataPubk, password1));
//
//        mUtil.addParams("userName", mSM2Util.encryptSM2(uploadDataPubk, userName));// 用户名
//        mUtil.addParams("password", mSM2Util.encryptSM2(uploadDataPubk, passWord));// 密码
//        mUtil.addParams("caseId", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getCaseId()));// 主键id
//        mUtil.addParams("confirmedOrder", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getConfirmedOrder()));// 确诊顺序
//        mUtil.addParams("confirmedSources", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getConfirmedSources()));// 确诊来源
//        mUtil.addParams("diagnosisType", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getDiagnosisType()));// 病例类型
//        mUtil.addParams("patientName", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getPatientName()));// 姓名
//        mUtil.addParams("gender", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getGender()));// 性别
//        mUtil.addParams("idCard", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIdCard()));// 身份证号
//        mUtil.addParams("birthdayDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getBirthdayDate()));// 出生日期
//        mUtil.addParams("age", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getAge()));// 年龄
//        mUtil.addParams("telecom", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getTelecom()));// 联系电话
//        mUtil.addParams("regionCase", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getRegionCase()));// 病例报告地区（8位地区编码）
//        mUtil.addParams("areaType", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getAreaType()));// 病人属于
//        mUtil.addParams("addrCode", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getAddrCode()));// 所在区县/街道（8位地区编码）
//        mUtil.addParams("unit", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getUnit()));// 工作单位
//        mUtil.addParams("professional", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getProfessional()));// 人员分类
//        mUtil.addParams("epiStartDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getEpiStartDate()));// 流调发病日期
//        mUtil.addParams("dyqStartDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getDyqStartDate()));// 大网发病日期
//        mUtil.addParams("clinicDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getClinicDate()));// 就诊日期
//        mUtil.addParams("diagnoseDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getDiagnoseDate()));// 确诊日期
//        mUtil.addParams("intoHospitalDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIntoHospitalDate()));// 入院日期
//        mUtil.addParams("intoHospital", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIntoHospital()));// 转入医院
//        mUtil.addParams("reportHospital", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getReportHospital()));// 报告医院
//        mUtil.addParams("clinicalSeverity", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getClinicalSeverity()));// 临床严重程度
//        mUtil.addParams("outcome", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getOutcome()));// 转归
//        mUtil.addParams("outcomeDate", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getOutcomeDate()));// 转归日期
//        mUtil.addParams("caseType", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getCaseType()));// 病例类型
//        mUtil.addParams("ifchronicDisease", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIfchronicDisease()));// 有无慢性病
//        mUtil.addParams("ichronicDiseaseNote", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIchronicDiseaseNote()));// 有慢性病（慢性病名称）
//        mUtil.addParams("ifFever", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIfFever()));// 是否发热
//        mUtil.addParams("symptoms", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getSymptoms()));// 症状（以||分隔）
//        mUtil.addParams("symptomsOth", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getSymptomsOth()));// 其他症状名称
//        mUtil.addParams("bodyTemp", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getBodyTemp()));// 体温（℃）
//        mUtil.addParams("wbc", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getWbc()));// WBC（*109）
//        mUtil.addParams("provContNum", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getProvContNum()));// 外省密接数
//        mUtil.addParams("famlContNum", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getFamlContNum()));// 家庭密接人数
//        mUtil.addParams("infectOriginSort", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getInfectOriginSort()));// 感染来源分类
//        mUtil.addParams("infectCity", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getInfectCity()));// 感染来源省市（8位地区编码）
//        mUtil.addParams("infectOrigTo", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getInfectOrigTo()));// 感染来源去向
//        mUtil.addParams("trafficTools", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getTrafficTools()));// 交通工具
//        mUtil.addParams("suspExposurehis", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getSuspExposurehis()));// 可疑暴露史
//        mUtil.addParams("ifFamily", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getIfFamily()));// 是否家庭成员
//        mUtil.addParams("relationship", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getRelationship()));// 与上一代病例关系
//        mUtil.addParams("inputLocal", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getInputLocal()));// 输入性/本地病例
//        mUtil.addParams("initialExposure", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getInitialExposure()));// 初次暴露日期
//        mUtil.addParams("lastExposure", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getLastExposure()));// 末次暴露日期
//        mUtil.addParams("durationExten", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getDurationExten()));// 传代时长
//        mUtil.addParams("reportUser", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getReportUser()));// 填报人（登录用户名）
//        mUtil.addParams("cardCode", mSM2Util.encryptSM2(uploadDataPubk, checkInfo.getCardCode()));// 病例卡片编号
        mUtil.exec(mStringCallback);// 请求
    }

    /**
     * 上传音频与文本文件
     *
     * @param mStringCallback
     */
    public static void uploadAudioAndTxt(List<UploadAudioAndTxtBean> list, MyStringCallback mStringCallback) throws IOException {
        WebserviceUtil w=new WebserviceUtil();//实例化webservice+ksoap工具类
        w.setUrl(URL_ADDRESS_SERVER);
        w.setMethodName(URL_UPLOAD_AUDIO_AND_TXT);//设置webservice请求方法
        Log.e("JSON.toJSONString(list)",JSON.toJSONString(list));
        String json = new SM2Util().encryptSM2(uploadFilePubk,JSON.toJSONString(list));
        w.addParams("arg0", json);
        w.exec(mStringCallback);//请求
    }

    /**
     * 上传其他附件
     *
     * @param caseId
     * @param attachmentPath
     * @param mStringCallback
     */
    private void uploadOtherAttachment(String caseId, String attachmentPath, StandardStringCallback mStringCallback) {
        OkGo.<String>get(URL_UPLOAD_OTHER_ATTACHMENT)
                .params("userName", "")// 用户名
                .params("password", "")// 密码
                .params("caseId", caseId)// 流调表主键
                .params("fileName", "")// 文件名称
                .params("fileType", "")// 文件类型，支持jpg、png格式
                .params("byteStr", "")// Base64加密后的文件字节流
                .params("fileUrl", "")// 上传视频时，对应的可访问url
                .params("createTime", "")// 上传时间（yyyy-MM-dd hh:mm:ss）
                .execute(mStringCallback);
    }

    // -------------------------------------------------- 文件服务器相关 --------------------------------------------------

    public final static String URL_FILE_SERVERS = "http://60.247.49.237:8088/ldms/mtm/api";// URL地址 - 文件服务器

    /**
     * 获取文件服务器Token
     *
     * @param mCallback
     */
    public static void getFileServersToken(StringCallback mCallback) {
        OkGo.<String>post(URL_FILE_SERVERS + "/getToken")
                .upJson("{\"appKey\":\"9c3d77f18e4848d095e626e9b3a009a3\",\"appSecret\":\"ff265c879c4ac08028e77a6c66078f9ce81c15b6fbc76c18f7a12a97c859c92b\"}")
                .execute(mCallback);
    }

    /**
     * 上传检查信息到文件服务器
     *
     * @param checkInfo
     */
    public static void uploadCheckInfoToFileServers(CheckBean checkInfo) {
        getFileServersToken(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    GetFileServersTokenJson json = JSON.parseObject(response.body(), GetFileServersTokenJson.class);
                    OkGo.<String>post(URL_FILE_SERVERS + "/updateEpi?" + "token=" + json.getData().getAccess_token())
                            .upJson(JSON.toJSONString(checkInfo).replace("caseId", "id"))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
