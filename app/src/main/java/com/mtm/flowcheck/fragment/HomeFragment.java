package com.mtm.flowcheck.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.activity.ContactUsActivity;
import com.mtm.flowcheck.activity.LoginActivity;
import com.mtm.flowcheck.activity.TaskActivity;
import com.mtm.flowcheck.activity.VersionLogActivity;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.manager.EventManager;
import com.mtm.flowcheck.utils.ToastUitl;
import com.mtm.flowcheck.utils.UIUtils;

import butterknife.BindView;

/**
 * Description : 我的Fragment
 */

public class HomeFragment extends BaseFragment {
    TextView titleTextView;
    LinearLayout intelligentVisitLayout, imageRecordingLayout, directLayout, databaseLayout;

    @Override
    public View initView(LayoutInflater inflater) {
        view = UIUtils.inflate(R.layout.fragment_home);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTextView = (TextView) view.findViewById(R.id.home_title_tv);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceHanSansCN-Regular.otf");
        titleTextView.setTypeface(tf);
        intelligentVisitLayout = (LinearLayout) view.findViewById(R.id.home_intelligent_visit_layout);
        imageRecordingLayout = (LinearLayout) view.findViewById(R.id.home_image_recording_layout);
        directLayout = (LinearLayout) view.findViewById(R.id.home_direct_layout);
        databaseLayout = (LinearLayout) view.findViewById(R.id.home_database_layout);

        //智能访视
        intelligentVisitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                startActivity(intent);
            }
        });
        //影像记录
        imageRecordingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //协同指挥
        directLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstallApp(getActivity(), "com.wbapp.client")) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.wbapp.client", "com.wbapp.client.LoginActivity");
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUitl.showShort("未安装协同指挥插件");
                }
            }
        });
        //资料库
        databaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 是否安装应用
     *
     * @param mContext    上下文对象
     * @param packageName 包名
     * @return
     */
    public static boolean isInstallApp(Context mContext, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
