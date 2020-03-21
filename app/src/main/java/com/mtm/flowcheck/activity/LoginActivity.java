package com.mtm.flowcheck.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.LoginMessageBean;
import com.mtm.flowcheck.bean.LoginUserBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.fragment.FragmentFactory;
import com.mtm.flowcheck.fragment.MineFragment;
import com.mtm.flowcheck.listeners.MyStringCallback;
import com.mtm.flowcheck.manager.EventManager;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.HTTPUtils;
import com.mtm.flowcheck.utils.StringUtils;
import com.mtm.flowcheck.utils.ToastUitl;
import com.sinosoft.key.SM2Util;

import org.w3c.dom.Entity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description : 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.unit_title0)
    TextView unitTitle0;
    @BindView(R.id.login_info_name)
    TextView loginInfoName;
    @BindView(R.id.login_info_sex)
    TextView loginInfoSex;
    @BindView(R.id.login_info_num)
    TextView loginInfoNum;
    @BindView(R.id.login_info_tel)
    TextView loginInfoTel;
    @BindView(R.id.login_info_id)
    TextView loginInfoId;
    @BindView(R.id.login_user_info)
    RelativeLayout loginUserInfo;
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_rl_mid)
    LinearLayout loginRlMid;
    @BindView(R.id.btn_login)
    Button mLogin;
    @BindView(R.id.linear_userInfo)
    LinearLayout linearUserInfo;
    @BindView(R.id.linear_login)
    LinearLayout linearLogin;
    private UserBean userInfo;
    static final int SUCCESS = 1;
    static final int LOGIN_ERROR = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        try {
            UserBean userBean = DBHelper.getInstance(LoginActivity.this).getUser();
            if (userBean == null) {
                linearLogin.setVisibility(View.VISIBLE);
                linearUserInfo.setVisibility(View.GONE);
                mLogin.setText("登录");
            } else {
                linearUserInfo.setVisibility(View.VISIBLE);
                linearLogin.setVisibility(View.GONE);
                mLogin.setText("注销用户");
                UserBean userInfo = EventManager.getUser();
                loginInfoName.setText("姓名:" + StringUtils.getDisStr(userInfo.getUserName()));
                loginInfoNum.setText("所属机构:" + StringUtils.getDisStr(userInfo.getOrgName()));
            }
        } catch (Exception e) {
        }

    }

    static class MyHandler extends Handler {
        WeakReference<LoginActivity> con;

        public MyHandler(LoginActivity activity) {
            con = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (con != null) {
                try {
                    LoginActivity mThis = con.get();
                    switch (msg.what) {
                        case SUCCESS:
                            MineFragment myUserFrag = (MineFragment) FragmentFactory.createFragment(FragmentFactory.FRAG_MINE);
                            myUserFrag.setUserInfoLayout();
                            mThis.finish();
                            break;
                        case LOGIN_ERROR:
                            ToastUitl.showShort("登录失败");
                            break;
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    MyHandler handler = new MyHandler(this);

    @OnClick(R.id.btn_login)
    public void onClick() {
        try {
            if ("登录".equals(mLogin.getText().toString())) {
                checkAccount(loginUsername.getText().toString().trim(), loginPassword.getText().toString().trim());
            } else {
                new AlertView("确认退出登录?", null, "确认", new String[]{"取消"}, null, LoginActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == -1) {
                            MineFragment myUserFrag = (MineFragment) FragmentFactory.createFragment(FragmentFactory.FRAG_MINE);
                            myUserFrag.tvusername.setText("请登录");
                            DBHelper.getInstance(getApplicationContext()).deleteUser();
                            finish();
                        }
                    }
                }).show();
            }
        } catch (Exception e) {
        }

    }

    /**
     * 登录校验
     */
    public void checkAccount(final String username, final String password) {
        try {
//            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//                ToastUitl.showShort("帐号或密码不能为空");
//            } else {
                SM2Util sm2Util = new SM2Util();
                String encryName = sm2Util.encryptSM2(HTTPUtils.loginPubk, "dctest");
                String encryPassword = sm2Util.encryptSM2(HTTPUtils.loginPubk, "sinosoft2020");
                HTTPUtils.userLogin(encryName, encryPassword, new MyStringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            SM2Util sm2Util = new SM2Util();
                            response = sm2Util.decryptSM2(HTTPUtils.lgonPrik, response, "GBK");
                            LoginUserBean loginUserBean = JSON.parseObject(response, LoginUserBean.class);
                            LoginMessageBean loginType = loginUserBean.getError();
                            if (null != loginType && !HTTPUtils.OPERATION_SUCCESS.equals(loginType.getCode())) {
                                handler.sendEmptyMessage(LOGIN_ERROR);
                            }
                            userInfo = loginUserBean.getData();
                            EventManager.setUser(userInfo);
                            //将User存本地
                            DBHelper.getInstance(LoginActivity.this).deleteUser();
                            DBHelper.getInstance(LoginActivity.this).saveUser(userInfo);
                            handler.sendEmptyMessage(SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String response) {
                        ToastUitl.showShort("网络异常,请重试");
                        handler.sendEmptyMessage(LOGIN_ERROR);
                    }
                });
//            }
        }catch (Exception e){
            Log.e("response",e.getMessage().toString()+"  ccc");
        }
    }
}
