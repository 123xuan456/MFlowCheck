package com.mtm.flowcheck.listeners;

import android.content.Context;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.mtm.flowcheck.utils.DialogUtils;
import com.mtm.flowcheck.utils.StringUtils;

public abstract class MyStringCallback extends StringCallback {

    private String hint;// 提示
    private Context mContext;// 上下文对象
    private boolean isShowDialog = false;// 是否显示弹窗

    public MyStringCallback() {
        this.isShowDialog = false;
    }

    public MyStringCallback(Context mContext, int hint) {
        this(mContext, mContext.getString(hint));
    }

    public MyStringCallback(Context mContext, String hint) {
        this.hint = hint;
        this.mContext = mContext;
        this.isShowDialog = true;
    }

    /**
     * 开始
     *
     * @param request
     */
    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        if (isShowDialog) {
            DialogUtils.getInstance().showLoadingDialog(mContext, hint);
        }
    }

    /**
     * 成功
     *
     * @param response
     */
    @Override
    public void onSuccess(Response<String> response) {
        if (!StringUtils.isEmpty(response.body())) {
            onSuccess(response.body());
        }
    }

    /**
     * 成功
     *
     * @param response
     */
    public abstract void onSuccess(String response);

    @Override
    public void onError(Response<String> response){
        if (!StringUtils.isEmpty(response.body())) {
            onSuccess(response.body());
        }
    }
    public abstract void onError(String response);
    /**
     * 结束
     */
    @Override
    public void onFinish() {
        super.onFinish();
        if (isShowDialog) {
            DialogUtils.getInstance().dismissLoadingDialog();
        }
    }
}
