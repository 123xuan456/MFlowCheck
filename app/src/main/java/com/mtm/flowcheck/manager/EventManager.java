package com.mtm.flowcheck.manager;
import com.mtm.flowcheck.bean.UserBean;

public class EventManager {

    public static String serverUrl;
    public static String loginDate;//登录时间
    public static String URL; //登录URL
    public static String LOGINURL; //登录URL
    public static String WEBURL; //查看附件
    public static String tableId; //自检表Id
    private static String token;

    //存放全局的 用户对象信息
    public static UserBean user;

    public static void setUser(UserBean userInfo) {
        EventManager.user = userInfo;
    }

    public static UserBean getUser() {
        return user;
    }
    public static void setLoginDate(String loginDate) {
        EventManager.loginDate = loginDate;
    }
}
