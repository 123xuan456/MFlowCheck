package com.mtm.flowcheck.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.manager.EventManager;
import com.mtm.flowcheck.utils.FileUtils;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tvinitdata)
    TextView tvinitdata;
    private String serverUrl;

    @Override
    public int getLayoutId() {
        return R.layout.layout;
    }

    @Override
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initData() {
        postDelay();
    }

    /**
     * 获取服务器地址
     */
    private void initHttp() {

    }

    private void getLoginUserInfo(String u, String p){
        try {
            //读取存储到本地的User信息
            String filePath = FileUtils.loginPath(getApplicationContext());
            String str = FileUtils.readProperties(filePath, "USER", "");
            String date = FileUtils.readProperties(filePath, "DATE", "");
            Gson gson = new Gson();
            UserBean user = gson.fromJson(str, new TypeToken<UserBean>() {}.getType());
            //本地缓存有数据
            if (null != user) {
                EventManager.setUser(user);
                EventManager.setLoginDate(date);
                //放自己的的登录跳转
                postDelay();
            } else {//没有数据在线下载
                if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
//                    showInfoDialog("未登录", "请在监督执法桌面 - 我的 中进行人员认证后重试!");
                    //放自己的的登录跳转
                    postDelay();
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postDelay(){
        // 1秒后 切换显示数据
        mHandler.postDelayed(toMainActivityRunnable1(), 1000);
    }
    private Runnable toMainActivityRunnable1() {
        return new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.class);
            }
        };
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }

    };
}
