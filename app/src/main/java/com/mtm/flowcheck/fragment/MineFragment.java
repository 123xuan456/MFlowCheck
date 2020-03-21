package com.mtm.flowcheck.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.activity.ContactUsActivity;
import com.mtm.flowcheck.activity.LoginActivity;
import com.mtm.flowcheck.activity.VersionLogActivity;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.utils.UIUtils;

import butterknife.BindView;

/**
 * Description : 我的Fragment
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.ivfwq)
    ImageView ivfwq;
    @BindView(R.id.tvfwq)
    TextView tvfwq;
    @BindView(R.id.rlfwqsz)
    RelativeLayout rlfwqsz;
    @BindView(R.id.ivyjfk)
    ImageView ivyjfk;
    @BindView(R.id.tvyjfk)
    TextView tvyjfk;
    @BindView(R.id.rlyjfk)
    RelativeLayout rlyjfk;
    @BindView(R.id.ivgywm)
    ImageView ivgywm;
    @BindView(R.id.tvgywm)
    TextView tvgywm;
    @BindView(R.id.tvfwqdz)
    TextView tvfwqdz;
    @BindView(R.id.linear_phone)
    public LinearLayout linearPhone;
    @BindView(R.id.img_tx)
    ImageView imgTx;
    @BindView(R.id.tvusername)
    public TextView tvusername;
    @BindView(R.id.tvphone)
    TextView tvphone;
    @BindView(R.id.jdymc_1)
    TextView jdymc1;
    @BindView(R.id.jdymc_2)
    TextView jdymc2;
    @BindView(R.id.liner_dl)
    LinearLayout linerDl;
    @BindView(R.id.login_in)
    RelativeLayout loginIn;
    @BindView(R.id.linear_user_top)
    LinearLayout linearUserTop;
    @BindView(R.id.ivjbxx)
    ImageView ivjbxx;
    @BindView(R.id.tvjbxx)
    TextView tvjbxx;
    @BindView(R.id.rljbxx)
    RelativeLayout rljbxx;
    @BindView(R.id.ivxtbj)
    ImageView ivxtbj;
    @BindView(R.id.tvxtbj)
    TextView tvxtbj;
    @BindView(R.id.rlbbrz)
    RelativeLayout rlbbrz;
    @BindView(R.id.rlgywm)
    RelativeLayout rlgywm;
    private UserBean userInfo;
    @Override
    public View initView(LayoutInflater inflater) {
        view = UIUtils.inflate(R.layout.fragment_mine);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        userInfo = DBHelper.getInstance(getContext()).getUser();
        if (userInfo != null) {
            tvusername.setText(userInfo.getUserName());
        } else {
            tvusername.setText("请登录");
            linearPhone.setVisibility(View.GONE);
        }
        setListener();

    }


    private void setListener() {
        //基本信息
        rljbxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginActivity.class);

            }
        });
        //联系我们
        rlgywm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ContactUsActivity.class);

            }
        });
        //版本日志
        rlbbrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VersionLogActivity.class);

            }
        });
        //登录
        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginActivity.class, null);
            }

        });

    }
    public void setUserInfoLayout() {
        userInfo = DBHelper.getInstance(getContext()).getUser();
        if(null != userInfo) {
            tvusername.setText(userInfo.getUserName());
        }
    }

}
