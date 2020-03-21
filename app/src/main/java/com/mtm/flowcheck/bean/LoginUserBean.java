package com.mtm.flowcheck.bean;

public class LoginUserBean {
    private String status;
    private LoginMessageBean error;
    private UserBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginMessageBean getError() {
        return error;
    }

    public void setError(LoginMessageBean error) {
        this.error = error;
    }

    public UserBean getData() {
        return data;
    }

    public void setData(UserBean data) {
        this.data = data;
    }
}