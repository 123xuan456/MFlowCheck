package com.mtm.flowcheck.bean;

/**
 * @author Liwenqing
 * @date 2020/3/18
 * 登录返回信息
 */
public class LoginMessageBean {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

}
