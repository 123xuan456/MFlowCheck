package com.mtm.flowcheck.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.adapter.MyPagerAdapter;
import com.mtm.flowcheck.fragment.HistoryFragment;
import com.mtm.flowcheck.fragment.NotStartedFragment;
import com.mtm.flowcheck.fragment.UnderwayFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.mtm.flowcheck.utils.UIUtils.dip2px;


/**
 * Description : 流转任务的Fragment
 */

public class TaskActivity extends BaseActivity {

    TextView titleTextView;
    ImageView backImageView;
    @BindView(R.id.task_tbl)
    TabLayout tabLayout;
    @BindView(R.id.task_vp)
    ViewPager taskVp;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void initView() {
        titleTextView = findViewById(R.id.all_title);
        backImageView = findViewById(R.id.iv_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText("流调检查");
    }

    @Override
    public void initData() {
        fragments.add(new NotStartedFragment());
//        fragments.add(new UnderwayFragment());
        fragments.add(new HistoryFragment());
        datas.add("进行中");
        datas.add("已完成");
//        datas.add("历史记录");
        initviews();
    }

    private void initviews() {
        //循环注入标签
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                taskVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), datas, fragments);
        taskVp.setAdapter(adapter);
        tabLayout.setupWithViewPager(taskVp);
        reflex(tabLayout);//设置tablayout下划线长度
    }

    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    int dp10 = dip2px(tabLayout.getContext(), 20);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }




}
