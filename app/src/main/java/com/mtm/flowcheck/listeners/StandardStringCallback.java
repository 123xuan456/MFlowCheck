package com.mtm.flowcheck.listeners;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.mtm.flowcheck.bean.RequestResultBean;
import com.mtm.flowcheck.utils.DialogUtils;
import com.mtm.flowcheck.utils.ToastUitl;

import static com.mtm.flowcheck.utils.HTTPUtils.UNKNOWN_ERROR;

/**
 * Created By WangYanBin On 2020\03\18 11:49.
 * <p>
 * （StandardStringCallback）
 * 参考：
 * 描述：
 */
public abstract class StandardStringCallback extends StringCallback {

    private Context mContext;
    private String hintStr;// 提示字符
    private boolean isShowDialog = false;// 是否显示弹窗

    public StandardStringCallback() {
    }

    public StandardStringCallback(Context mContext, String hintStr) {
        this.mContext = mContext;
        this.hintStr = hintStr;
        this.isShowDialog = true;
    }

    public abstract void onSuccess(String dataJson);

    public abstract void onError(String errorCode);

    // -------------------------------------------------- 原有 --------------------------------------------------

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        if (isShowDialog) DialogUtils.getInstance().showLoadingDialog(mContext, hintStr);
    }

    private final int SUCCESS = 0;// 成功

    @Override
    public void onSuccess(Response<String> response) {
        try {
            RequestResultBean resultInfo = JSON.parseObject(response.body(), RequestResultBean.class);
            if (resultInfo.getStatus() == SUCCESS) {
                onSuccess(resultInfo.getData());
            } else {
                RequestResultBean.ErrorBean errorInfo = resultInfo.getError();
                if (errorInfo != null) {
                    ToastUitl.showShort(errorInfo.getMessage());
                    onError(errorInfo.getCode());
                } else {
                    onError(UNKNOWN_ERROR);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(UNKNOWN_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        ToastUitl.showShort("网络异常,请重试");
        onError(UNKNOWN_ERROR);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (isShowDialog) DialogUtils.getInstance().dismissLoadingDialog();
    }
}
