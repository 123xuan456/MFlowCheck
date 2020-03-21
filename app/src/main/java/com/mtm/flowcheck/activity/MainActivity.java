package com.mtm.flowcheck.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.fragment.AnalysisFragment;
import com.mtm.flowcheck.fragment.FragmentFactory;
import com.mtm.flowcheck.fragment.HomeFragment;
import com.mtm.flowcheck.fragment.MineFragment;
import com.mtm.flowcheck.utils.AssetsDataUtils;
import com.mtm.flowcheck.utils.UIUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    RelativeLayout llHome;
    RelativeLayout llAnalysis;
    RelativeLayout llMy;
    ImageView ivHome;
    ImageView ivAnalysis;
    ImageView ivMy;
    TextView tHome;
    TextView tAnalysis;
    TextView tMy;

    private HomeFragment mHomeFragment;
    private AnalysisFragment mAnalysisFragment;
    private MineFragment mMineFragment;
    private FragmentManager fragmentManager;
    final int HOME = 0;
    final int ANALYSIS = 1;
    final int MINE = 2;
    int lastFragment = HOME;
    int state = HOME;//默认显示主页


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        llHome = (RelativeLayout) findViewById(R.id.footer_home_layout);
        llAnalysis = (RelativeLayout) findViewById(R.id.footer_analysis_layout);
        llMy = (RelativeLayout) findViewById(R.id.footer_my_layout);
        ivHome = (ImageView) findViewById(R.id.footer_iv_home);
        ivAnalysis = (ImageView) findViewById(R.id.footer_iv_analysis);
        ivMy = (ImageView) findViewById(R.id.footer_iv_my);
        tHome = (TextView) findViewById(R.id.footer_text_home);
        tAnalysis = (TextView) findViewById(R.id.footer_text_analysis);
        tMy = (TextView) findViewById(R.id.footer_text_my);
        initClick();
    }

    private void initClick() {
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = HOME;
                setFragmentLayout();
            }
        });
        llAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = ANALYSIS;
                setFragmentLayout();
            }
        });
        llMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MINE;
                setFragmentLayout();
            }
        });
    }

    @Override
    public void initData() {
        AssetsDataUtils.init(this);
        mHomeFragment = (HomeFragment) FragmentFactory.createFragment(HOME);
        mAnalysisFragment = (AnalysisFragment) FragmentFactory.createFragment(ANALYSIS);
        mMineFragment = (MineFragment) FragmentFactory.createFragment(MINE);
        fragmentManager = getSupportFragmentManager();
        UIUtils.addFragmentLayout(fragmentManager, R.id.main_content, mHomeFragment, "mHomeFragment");
        setFooterResource(HOME);
    }


    private void setFragmentLayout() {
        switch (lastFragment) {
            case HOME:
                UIUtils.hideFragmentLayout(fragmentManager, mHomeFragment);
                break;
            case ANALYSIS:
                UIUtils.hideFragmentLayout(fragmentManager, mAnalysisFragment);
                break;
            case MINE:
                UIUtils.hideFragmentLayout(fragmentManager, mMineFragment);
                break;
        }
        switch (state) {
            case HOME:
                UIUtils.addFragmentLayout(fragmentManager, R.id.main_content, mHomeFragment, "mHomeFragment");
                break;
            case ANALYSIS:
                UIUtils.addFragmentLayout(fragmentManager, R.id.main_content, mAnalysisFragment, "mAnalysisFragment");
                break;
            case MINE:
                UIUtils.addFragmentLayout(fragmentManager, R.id.main_content, mMineFragment, "mMineFragment");
                break;
        }
        lastFragment = state;
        setFooterResource(state);
    }

    /**
     * 设置底部按钮资源
     *
     * @param state 显示状态
     */
    @TargetApi(Build.VERSION_CODES.M)
    void setFooterResource(int state) {
        switch (state) {
            case HOME:
                ivHome.setImageResource(R.drawable.main_bottom_task_focus);
                ivAnalysis.setImageResource(R.drawable.main_bottom_emergency_normal);
                ivMy.setImageResource(R.drawable.main_bottom_mine_normal);
                tHome.setTextColor(getResources().getColor(R.color.main_color));
                tAnalysis.setTextColor(getResources().getColor(R.color.main_gray));
                tMy.setTextColor(getResources().getColor(R.color.main_gray));
                break;
            case ANALYSIS:
                ivHome.setImageResource(R.drawable.main_bottom_task_normal);
                ivAnalysis.setImageResource(R.drawable.main_bottom_emergency_focus);
                ivMy.setImageResource(R.drawable.main_bottom_mine_normal);
                tHome.setTextColor(getResources().getColor(R.color.main_gray));
                tAnalysis.setTextColor(getResources().getColor(R.color.main_color));
                tMy.setTextColor(getResources().getColor(R.color.main_gray));
                break;
            case MINE:
                ivHome.setImageResource(R.drawable.main_bottom_task_normal);
                ivAnalysis.setImageResource(R.drawable.main_bottom_emergency_normal);
                ivMy.setImageResource(R.drawable.main_bottom_mine_focus);
                tHome.setTextColor(getResources().getColor(R.color.main_gray));
                tAnalysis.setTextColor(getResources().getColor(R.color.main_gray));
                tMy.setTextColor(getResources().getColor(R.color.main_color));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }
}
