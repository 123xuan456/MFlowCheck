package com.mtm.flowcheck.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.adapter.LinkContentItemAdapter;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.CheckBean111;
import com.mtm.flowcheck.bean.DataMapUtil;
import com.mtm.flowcheck.bean.LinkBean;
import com.mtm.flowcheck.bean.item.LinkContentItemBean;
import com.mtm.flowcheck.bean.RequestResultBean;
import com.mtm.flowcheck.bean.UploadAudioAndTxtBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.listeners.MyStringCallback;
import com.mtm.flowcheck.service.NFCService;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.HTTPUtils;
import com.mtm.flowcheck.utils.LinkContentItemHelper;
import com.mtm.flowcheck.utils.LogUtils;
import com.mtm.flowcheck.utils.MdDialogUtils;
import com.mtm.flowcheck.utils.StringUtils;
import com.mtm.flowcheck.utils.ToastUitl;
import com.sinosoft.key.SM2Util;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.mtm.flowcheck.bean.LinkBean.LINK_BASE_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DETECTION_INFO;
import static com.mtm.flowcheck.bean.LinkBean.LINK_DIAGNOSIS_COURSE;
import static com.mtm.flowcheck.bean.LinkBean.LINK_EPIDEMIC_DISEASE_HISTORY;
import static com.mtm.flowcheck.bean.LinkBean.LINK_OSCULATION_CONTACT;
import static com.mtm.flowcheck.service.NFCService.COMMANDS_START_RECORD_AUDIO;
import static com.mtm.flowcheck.service.NFCService.COMMANDS_START_RECORD_VIDEO;
import static com.mtm.flowcheck.service.NFCService.COMMANDS_STOP_RECORD_AUDIO;
import static com.mtm.flowcheck.service.NFCService.COMMANDS_STOP_RECORD_VIDEO;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_NONE;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_STOP;

/**
 * Created By WangYanBin On 2020\03\16 13:56.
 * <p>
 * （CheckActivity）
 * 参考：
 * 描述：
 */
public class CheckActivity extends BaseAudioActivity implements View.OnClickListener {


    private int curwhich;
    private MaterialDialog uploadDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_check;
    }

    private List<LinkBean> mList = new ArrayList<>();
    private LinkContentItemHelper mLinkContentItemHelper;

    private void init() {
        checkInfo = JSON.parseObject(getIntent().getStringExtra("checkInfo"), CheckBean.class);
        mList.addAll(JSONArray.parseArray(getIntent().getStringExtra("lickList"), LinkBean.class));
        nowPosition = getIntent().getIntExtra("position", 0);
        mLinkContentItemHelper = new LinkContentItemHelper(this, checkInfo);
        HTTPUtils.uploadCheckInfoToFileServers(checkInfo);
    }

    private TextView linkTv;
    private TextView recordHintTv;
    private RecyclerView mRecyclerView;
    private LinkContentItemAdapter mAdapter;
    private CheckBox checkbox;
    private boolean isRecording = false;

    @Override
    public void initView() {
        init();
        FileUtils.createDirs(FileUtils.recordPath(mContext));
        ((TextView) findViewById(R.id.all_title)).setText(R.string.epidemic_disease_table_details);
        findViewById(R.id.iv_back).setOnClickListener((View v) -> finish());
        linkTv = findViewById(R.id.tv_link);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LinkContentItemAdapter(mLinkContentItemHelper);
        mAdapter.setOnItemChildClickListener((BaseQuickAdapter adapter, View view, int position) -> mLinkContentItemHelper.onItemChildClick(adapter, view, position, 0));
        mRecyclerView.setAdapter(mAdapter);
        recordHintTv = findViewById(R.id.tv_record_hint);

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_save_and_next).setOnClickListener(this);

        llReset = findViewById(R.id.ll_reset);
        ivController = findViewById(R.id.iv_controller);
        llFinish = findViewById(R.id.ll_finish);

        checkbox = findViewById(R.id.checkbox);
        recordEndHintEt = findViewById(R.id.et_end_record_hint);
        chronometer = findViewById(R.id.chronometer);
        tvAudioState = findViewById(R.id.tv_audio_state);
        tvAudioWords = findViewById(R.id.tv_audio_words);

        llReset.setOnClickListener(this);
        ivController.setOnClickListener(this);
        llFinish.setOnClickListener(this);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    ShareUtil.saveBooleanData(mContext,"isRecorderCheck",true);
                    recordHintTv.setVisibility(View.VISIBLE);
                    NFCService.startRecordVideo(checkInfo.getCaseId() + "@" + nowLinkInfo.getLinkType());
                } else {
//                    ShareUtil.saveBooleanData(mContext,"isRecorderCheck",false);
                    recordHintTv.setVisibility(View.GONE);
                    NFCService.cancelCommands();
                }
            }
        });
    }

    private LinkBean nowLinkInfo;

    /**
     * 切换环节
     *
     * @param position
     */
    public void changeLink(int position) {
        nowPosition = position;
        nowLinkInfo = mList.get(nowPosition);
        switch (nowLinkInfo.getLinkType()) {
            case LINK_BASE_INFO:// 基本信息
                linkTv.setText(R.string.link_base_info);
                break;
            case LINK_DIAGNOSIS_COURSE:// 发病就诊过程
                linkTv.setText(R.string.link_diagnosis_course);
                break;
            case LINK_OSCULATION_CONTACT:// 密接情况
                linkTv.setText(R.string.link_osculation_contact);
                break;
            case LINK_EPIDEMIC_DISEASE_HISTORY:// 流行病学史
                linkTv.setText(R.string.link_epidemic_disease_history);
                break;
            case LINK_DETECTION_INFO:// 实验室检测信息
                linkTv.setText(R.string.link_detection_info);
                break;
        }
        List<LinkContentItemBean> mList = mLinkContentItemHelper.getLinkContentItem(nowLinkInfo.getLinkType());
        mAdapter.setNewData(mList);
        if (checkbox.isChecked()) {
            NFCService.startRecordVideo(checkInfo.getCaseId() + "@" + nowLinkInfo.getLinkType());
        } else {
            NFCService.cancelCommands();
        }
        mAdapter.setNewData(mLinkContentItemHelper.getLinkContentItem(nowLinkInfo.getLinkType()));
        findViewById(R.id.btn_save_and_next).setVisibility(nowPosition < mList.size() - 1 ? View.VISIBLE : View.GONE);
        // 把初始时该显示的显示，该隐藏的都隐藏
        findViewById(R.id.ll_phone_audio).setVisibility(View.VISIBLE);
        recordHintTv.setVisibility(View.GONE);
        fileName = checkInfo.getCaseId() + "_" + nowPosition;
        llFinish.setClickable(false);
        initRecordManager();
        LogUtils.i("fileName" + fileName);
    }

    /**
     * 初始化录音服务
     */
    private void initRecordManager() {
        recordManager = RecordManager.getInstance();
        recordManager.init(getApplication(), true);
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);//可保存三种录音文件
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(8000));//音频采样率
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));//音频位宽
        recordManager.changeRecordDir(FileUtils.recordPath(mContext));//录音文件夹
        recordManager.changeRecordName(fileName);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_save:
//                finish();
//                break;
//            case R.id.btn_save_and_next:
//                changeLink(nowPosition + 1);
//                break;
            case R.id.ll_reset:
                llFinish.setClickable(true);
                onfinishAndReset();//取消重录
                break;
            case R.id.iv_controller:// 开始--暂停
                if (initAudioPermission()) {
                    llFinish.setClickable(true);
                    onAudioClick();
                } else {
                    ToastUitl.show("未开启录音权限，无法使用录音功能");
                }
                break;
            case R.id.ll_finish:
                if (checkbox.isChecked() && isRecording) {
                    ToastUitl.show("记录仪正在录制，请关闭之后保存");
                } else {
                    llFinish.setClickable(false);
                    onAudioClick();
                    showNextDialog();
                }
                break;
        }
    }

    private void showNextDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("录制完成，选择下一步操作")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curwhich = which;//-1
                        dialog.dismiss();
                        onAudioStopClick();//完成
                    }
                })
                .setNegativeButton("保存并进入下一流程", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curwhich = which;//-2
                        dialog.dismiss();
                        onAudioStopClick();//完成
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    /**
     * 结束录音/处理文件/上传文件
     */
    public void onAudioStopClick() {
        uploadDialog = new MaterialDialog.Builder(mContext).title("提示").content("上传数据中...").progress(true, 0).show();
        recordManager.stop();
        cancel();
        status = STATUS_NONE; // 识别结束，回到初始状态
        updateBtnTextByStatus(STATUS_STOP);
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                LogUtils.i("录音文件： " + result.getAbsolutePath());
                Observable
                        .create(new ObservableOnSubscribe<Map<String, String>>() {
                            @Override
                            public void subscribe(ObservableEmitter<Map<String, String>> e) throws Exception {
                                if (FileUtils.writeFile(recordEndHintEt.getText().toString(), getTxtFile(), false)) {
                                    Map<String, String> map = new HashMap<>();
                                    String byteStr = FileUtils.encodeBase64File(getTxtFile());
                                    String voiceByteStr = FileUtils.encodeBase64File(result.getAbsolutePath());
                                    map.put("byteStr", byteStr);
                                    map.put("voiceByteStr", voiceByteStr);
                                    e.onNext(map);
                                } else {
                                    e.onNext(null);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Map<String, String>>() {
                            @Override
                            public void accept(Map<String, String> map) throws Exception {
                                if (map != null) {
                                    String byteStr = map.get("byteStr");
                                    String voiceByteStr = map.get("voiceByteStr");
                                    UploadAudioAndTxtBean audioAndTxtBean = new UploadAudioAndTxtBean();
                                    //写入文本文件
                                    UserBean user = DBHelper.getInstance(mContext).getUser();
                                    audioAndTxtBean.setUserName("dctest");
                                    audioAndTxtBean.setPassword("sinosoft2020");
                                    audioAndTxtBean.setCaseId(checkInfo.getCaseId());
                                    audioAndTxtBean.setModelType(nowPosition+1);
                                    audioAndTxtBean.setVoiceFileName(fileName);
                                    audioAndTxtBean.setVoiceFileType("mp3");
                                    audioAndTxtBean.setVoiceByteStr(voiceByteStr);
                                    audioAndTxtBean.setTxtFileName(fileName);
                                    audioAndTxtBean.setTxtFileType("txt");
                                    audioAndTxtBean.setTxtByteStr(byteStr);
                                    audioAndTxtBean.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                                    uploadTask(audioAndTxtBean);
                                } else {
                                    uploadDialog.dismiss();
                                    ToastUitl.show("文件转换异常");
                                }
                            }

                        });
            }
        });

    }

    private void uploadTask(UploadAudioAndTxtBean audioAndTxtBean) throws Exception {
//        CheckBean111 checkBean=new CheckBean111();
//        checkBean.setCaseId(checkInfo.getCaseId());
//        checkBean.setConfirmedOrder(checkInfo.getConfirmedOrder());
//        checkBean.setConfirmedSources(checkInfo.getConfirmedSources());
//        checkBean.setDiagnosisType(checkInfo.getDiagnosisType());
//        checkBean.setPatientName(checkInfo.getPatientName());
//        checkBean.setGender(checkInfo.getGender());
//        checkBean.setIdCard(checkInfo.getIdCard());
//        checkBean.setBirthdayDate(checkInfo.getBirthdayDate());
//        checkBean.setAge(checkInfo.getAge());
//        checkBean.setTelecom(checkInfo.getTelecom());
//        checkBean.setRegionCase(checkInfo.getRegionCase());
//        checkBean.setAreaType(checkInfo.getAreaType());
//        checkBean.setAddrCode(checkInfo.getAddrCode());
//        checkBean.setUnit(checkInfo.getUnit());
//        checkBean.setProfessional(checkInfo.getProfessional());
//        checkBean.setEpiStartDate(checkInfo.getEpiStartDate());
//        checkBean.setDyqStartDate(checkInfo.getDyqStartDate());
//        checkBean.setClinicDate(checkInfo.getClinicDate());
//        checkBean.setDiagnoseDate(checkInfo.getDiagnoseDate());
//        checkBean.setIntoHospitalDate(checkInfo.getIntoHospitalDate());
//        checkBean.setIntoHospital(checkInfo.getIntoHospital());
//        checkBean.setReportHospital(checkInfo.getReportHospital());
//        checkBean.setClinicalSeverity(checkInfo.getClinicalSeverity());
//        checkBean.setOutcome(checkInfo.getOutcome());
//        checkBean.setOutcomeDate(checkInfo.getOutcomeDate());
//        checkBean.setCaseType(DataMapUtil.getCaseType(StringUtils.getDisStr(checkInfo.getCaseType())));
//        checkBean.setIfchronicDisease(checkInfo.getIfchronicDisease());
//        checkBean.setIchronicDiseaseNote(checkInfo.getIchronicDiseaseNote());
//        checkBean.setIfFever(checkInfo.getIfFever());
//        checkBean.setSymptoms(checkInfo.getSymptoms());
//        checkBean.setSymptomsOth(checkInfo.getSymptomsOth());
//        checkBean.setBodyTemp(checkInfo.getBodyTemp());
//        checkBean.setWbc(checkInfo.getWbc());
//        checkBean.setProvContNum(checkInfo.getProvContNum());
//        checkBean.setFamlContNum(checkInfo.getFamlContNum());
//        checkBean.setInfectCity(checkInfo.getInfectCity());
//        checkBean.setInfectOrigTo(checkInfo.getInfectOrigTo());
//        checkBean.setTrafficTools(DataMapUtil.getTrafficTools(checkInfo.getTrafficTools()));
//        checkBean.setSuspExposurehis(checkInfo.getSuspExposurehis());
//        checkBean.setIfFamily(DataMapUtil.getIfFamily(checkInfo.getIfFamily()));
//        checkBean.setRelationship(checkInfo.getRelationship());
//        checkBean.setInputLocal(checkInfo.getInputLocal());
//        checkBean.setInitialExposure(checkInfo.getInitialExposure());
//        checkBean.setLastExposure(checkInfo.getLastExposure());
//        checkBean.setDurationExten(checkInfo.getDurationExten());
//        checkBean.setReportUser(checkInfo.getReportUser());
//        checkBean.setCardCode(checkInfo.getCardCode());
//        checkBean.setInfectOriginSort(checkInfo.getInfectOriginSort());
//        checkBean.setInfectOrigTo(DataMapUtil.getInfectOrigTo(checkInfo.getInfectOrigTo()));

        //        checkBean.setIsDone(checkInfo.getIsDone());


//        HTTPUtils.uploadTask("dctest", "sinosoft2020", checkBean, new MyStringCallback() {
//                    @Override
//                    public void onSuccess(String response) {
//                        SM2Util sm2Util = new SM2Util();
//                        try {
//                            LogUtils.i("上传数据成功待解密："+response);
//                            response = sm2Util.decryptSM2(HTTPUtils.uploadDataPrik, response, "GBK");
//                            RequestResultBean resultInfo = JSON.parseObject(response, RequestResultBean.class);
//                            LogUtils.i("sssss："+response);
//                            if (resultInfo.getStatus()==0 && "1000".equals(resultInfo.getError().getCode())){
//                                LogUtils.i("上传数据成功解密成功："+response);
//                                uploadAudioAndTxt(audioAndTxtBean);
//                            }else{
//                                ToastUitl.show("数据上传失败");
//                            }
//                            uploadDialog.dismiss();
//                        } catch (Exception e) {
//                            uploadDialog.dismiss();
//                            LogUtils.i("上传数据解密失败：");
//                            ToastUitl.show("数据上传失败");
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(String response) {
//                        uploadDialog.dismiss();
//                        ToastUitl.show("数据上传失败");
//                    }
//                }
//        );
        List<UploadAudioAndTxtBean> list = new ArrayList<UploadAudioAndTxtBean>();
        list.add(audioAndTxtBean);
        uploadAudioAndTxt(list);
    }

    private void uploadAudioAndTxt(List<UploadAudioAndTxtBean> list) throws Exception {
        HTTPUtils.uploadAudioAndTxt(list, new MyStringCallback() {
            @Override
            public void onSuccess(String response) {
                SM2Util sm2Util = new SM2Util();
                try {
                    LogUtils.e("上传文件待解密："+response);
                    response = sm2Util.decryptSM2(HTTPUtils.uploadFilePrik, response, "GBK");
                    RequestResultBean resultInfo = JSON.parseObject(response, RequestResultBean.class);
                    LogUtils.e("上传文件待解密1111："+response);
                    if (resultInfo.getStatus()==0 && "1000".equals(resultInfo.getError().getCode())){
                        LogUtils.i("上传文件解密成功："+response);
                        ToastUitl.show("文件上传成功");
                        if (curwhich==-1){
                            finish();
                        }else if (curwhich==-2){
                            changeLink(nowPosition + 1);
                        }
                        LinkBean linkBean = DBHelper.getInstance(getBaseContext()).getTaskLinkInfo(list.get(0).getCaseId(),nowPosition + 1);
                        if(null != linkBean){
                            linkBean.setLinkValue(1);
                            DBHelper.getInstance(getBaseContext()).deleteTaskLinkInfo(list.get(0).getCaseId(),nowPosition+1);
                            DBHelper.getInstance(getBaseContext()).saveTaskLinkData(linkBean);
                        }
                        List<LinkBean> llist = DBHelper.getInstance(getBaseContext()).getTaskLinkList(list.get(0).getCaseId(),"1");
                        if(null != list&& list.size() == 5){
                            checkInfo.setIsDone(1);
                            DBHelper.getInstance(getBaseContext()).updateTask(checkInfo);
                        }

                        uploadDialog.dismiss();
                    }else {
                        uploadDialog.dismiss();
                        ToastUitl.show("文件上传失败");
                    }
                } catch (Exception e) {
                    uploadDialog.dismiss();
                    ToastUitl.show("文件上传失败");
                    LogUtils.i("上传文件解密失败：");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String response) {
                uploadDialog.dismiss();
            }
        });
    }


    // -------------------------------------------------- 加载数据 --------------------------------------------------

    @Override
    public void initData() {
        changeLink(nowPosition);
    }

    // -------------------------------------------------- EventBus --------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String commands) {
        switch (commands) {
            case COMMANDS_START_RECORD_VIDEO:// 开始录制视频
                isRecording = true;
                recordHintTv.setText("手持机轻触记录仪结束录制视频");
                NFCService.stopRecordVideo();
                break;
            case COMMANDS_STOP_RECORD_VIDEO:// 结束录制视频
                isRecording = false;
                recordHintTv.setText("手持机轻触记录仪开始录制视频");
                NFCService.startRecordVideo(checkInfo.getCaseId() + "@" + nowLinkInfo.getLinkType());
                break;
        }
    }
}
