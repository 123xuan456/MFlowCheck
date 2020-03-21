package com.mtm.flowcheck.bean;

import java.util.Date;

/**
 * 上传录音文件参数类
 */
public class UploadAudioAndTxtBean {
    private String userName;
    private String password;
    private String caseId;
    private int modelType;
    private String voiceFileName;
    private String voiceFileType;
    private String voiceByteStr;
    private String txtFileName;
    private String txtFileType;
    private String txtByteStr;
    private String createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }

    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName;
    }

    public String getVoiceFileType() {
        return voiceFileType;
    }

    public void setVoiceFileType(String voiceFileType) {
        this.voiceFileType = voiceFileType;
    }

    public String getVoiceByteStr() {
        return voiceByteStr;
    }

    public void setVoiceByteStr(String voiceByteStr) {
        this.voiceByteStr = voiceByteStr;
    }

    public String getTxtFileName() {
        return txtFileName;
    }

    public void setTxtFileName(String txtFileName) {
        this.txtFileName = txtFileName;
    }

    public String getTxtFileType() {
        return txtFileType;
    }

    public void setTxtFileType(String txtFileType) {
        this.txtFileType = txtFileType;
    }

    public String getTxtByteStr() {
        return txtByteStr;
    }

    public void setTxtByteStr(String txtByteStr) {
        this.txtByteStr = txtByteStr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }




}
