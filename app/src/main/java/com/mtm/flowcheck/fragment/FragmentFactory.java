package com.mtm.flowcheck.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: Fragment 工厂类
 * Android:minSdkVersion: API
 * Version:V1.0
 */
public class FragmentFactory {

    /**
     * FRAG_MANAGER 界面的子 Fragment
     */
    public static final int FRAG_HOME = 0;//主页
    public static final int FRAG_ANALYSIS = 1;//应急指挥
    public static final int FRAG_MINE = 2;//我的

    /**
     * 记录所有的fragment，防止重复创建
     */
    private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<>();

    /**
     * 采用工厂类进行创建Fragment，便于扩展，已经创建的Fragment不再创建
     */
    public static BaseFragment createFragment(int index) {
        BaseFragment fragment = mFragmentMap.get(index);

        if (fragment == null) {
            switch (index) {
                case FRAG_HOME:
                    fragment = new HomeFragment();
                    break;
                case FRAG_ANALYSIS:
                    fragment = new AnalysisFragment();
                    break;
                case FRAG_MINE:
                    fragment = new MineFragment();
                    break;
            }
            mFragmentMap.put(index, fragment);
        }
        return fragment;
    }
}
