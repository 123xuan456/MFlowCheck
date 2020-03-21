package com.mtm.flowcheck.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class UserBean {
    private String userName;
    private String password;
    private String realName;
    private String areaCode;
    private String areaName;
    private String orgCode;
    private String orgName;

    @Generated(hash = 561277060)
    public UserBean(String userName, String password, String realName,
            String areaCode, String areaName, String orgCode, String orgName) {
        this.userName = userName;
        this.password = password;
        this.realName = realName;
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.orgCode = orgCode;
        this.orgName = orgName;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}