package com.mtm.flowcheck.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.utils.UIUtils;

/**
 * Description : 我的Fragment
 */

public class AnalysisFragment extends BaseFragment {


    @Override
    public View initView(LayoutInflater inflater) {
        view = UIUtils.inflate(R.layout.fragment_analysis);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

}
