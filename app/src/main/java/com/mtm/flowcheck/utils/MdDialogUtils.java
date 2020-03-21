package com.mtm.flowcheck.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mtm.flowcheck.R;


/**
 * Created By WangYanBin On 2020\01\10 14:12.
 * <p>
 * 对话框工具类（DialogUtils）
 * 参考：
 * 描述：
 */
public class MdDialogUtils {

    private static MdDialogUtils instance;

    public static MdDialogUtils getInstance() {
        if (instance == null) {
            instance = new MdDialogUtils();
        }
        return instance;
    }

    private ProgressDialog mProgressDialog;

    /**
     * 显示加载弹窗
     *
     * @param mContext
     * @param hintContent
     */
    public void showLoadingDialog(Context mContext, String hintContent) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(hintContent);
        mProgressDialog.setIndeterminate(true);// 是否形成一个加载动画
        mProgressDialog.setCanceledOnTouchOutside(false);// 点击其他区域不可关闭
        mProgressDialog.setOnDismissListener((dialog -> dismissLoadingDialog()));
        mProgressDialog.show();
    }
    public void showLoadingDialog(Context mContext, String hintContent,boolean cancelable) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(hintContent);
        mProgressDialog.setIndeterminate(true);// 是否形成一个加载动画
        mProgressDialog.setCanceledOnTouchOutside(cancelable);
        mProgressDialog.setOnDismissListener((dialog -> dismissLoadingDialog()));
        mProgressDialog.show();
    }

    /**
     * 关闭加载弹窗
     */
    public void dismissLoadingDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    private MaterialDialog.Builder mBuilder;

    // -------------------------------------------------- 确认对话框 --------------------------------------------------

    /**
     * 创建确认对话框
     *
     * @param context
     * @param content
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String content) {
        return this.createAffirmDialog(context, content, null, null);
    }
    /**
     * 创建等待弹窗
     */
    public MaterialDialog.Builder createProgressDialog(Context context,String title, String content,boolean indeterminate) {

        return mBuilder;
    }
    /**
     * 创建确认对话框
     *
     * @param context
     * @param content
     * @param positiveText
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String content,
                                                     String positiveText) {
        return this.createAffirmDialog(context, content, positiveText, (@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> dialog.dismiss());
    }

    /**
     * 创建确认对话框
     *
     * @param context
     * @param content
     * @param positiveText
     * @param positiveCallback
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String content,
                                                     String positiveText, MaterialDialog.SingleButtonCallback positiveCallback) {
        return this.createAffirmDialog(context, content, positiveText, positiveCallback, null, null);
    }

    /**
     * 创建确认对话框
     *
     * @param context
     * @param content
     * @param positiveText
     * @param positiveCallback
     * @param negativeText
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String content,
                                                     String positiveText, MaterialDialog.SingleButtonCallback positiveCallback,
                                                     String negativeText) {
        return this.createAffirmDialog(context, content, positiveText, positiveCallback, negativeText, (@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> dialog.dismiss());
    }

    /**
     * 创建确认对话框
     *
     * @param context
     * @param content
     * @param positiveText
     * @param positiveCallback
     * @param negativeText
     * @param negativeCallback
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String content,
                                                     String positiveText, MaterialDialog.SingleButtonCallback positiveCallback,
                                                     String negativeText, MaterialDialog.SingleButtonCallback negativeCallback) {
        return this.createAffirmDialog(context, null, content, -1, positiveText, positiveCallback, negativeText, negativeCallback, null, null);
    }

    /**
     * 创建确认对话框
     *
     * @param context
     * @param title
     * @param content
     * @param iconRes
     * @param positiveText
     * @param positiveCallback
     * @param negativeText
     * @param negativeCallback
     * @param neutralText
     * @param neutralCallback
     * @return
     */
    public MaterialDialog.Builder createAffirmDialog(Context context, String title, String content, int iconRes,
                                                     String positiveText, MaterialDialog.SingleButtonCallback positiveCallback,
                                                     String negativeText, MaterialDialog.SingleButtonCallback negativeCallback,
                                                     String neutralText, MaterialDialog.SingleButtonCallback neutralCallback) {
        mBuilder = new MaterialDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) mBuilder.title(title);
        if (!TextUtils.isEmpty(content)) mBuilder.content(content);
        if (iconRes != -1) mBuilder.iconRes(iconRes);
        if (!TextUtils.isEmpty(positiveText)) {
            mBuilder.positiveText(positiveText);
            mBuilder.positiveColor(context.getResources().getColor(R.color.dialog_positive_color));
            if (positiveCallback != null) mBuilder.onPositive(positiveCallback);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            mBuilder.negativeText(negativeText);
            mBuilder.negativeColor(context.getResources().getColor(R.color.dialog_negative_color));
            if (negativeCallback != null) mBuilder.onNegative(negativeCallback);
        }
        if (!TextUtils.isEmpty(neutralText)) {
            mBuilder.neutralText(neutralText);
            mBuilder.neutralColor(context.getResources().getColor(R.color.dialog_neutral_color));
            if (neutralCallback != null) mBuilder.onNeutral(neutralCallback);
        }
        return mBuilder;
    }

    // -------------------------------------------------- 列表对话框 --------------------------------------------------

    /**
     * 创建列表对话框
     *
     * @param context
     * @param items
     * @param listCallback
     * @return
     */
    public MaterialDialog.Builder createListDialog(Context context, String[] items, MaterialDialog.ListCallback listCallback) {
        return createListDialog(context, null, null, -1, true, items, listCallback);
    }

    /**
     * 创建列表对话框
     *
     * @param context
     * @param title
     * @param items
     * @param listCallback
     * @return
     */
    public MaterialDialog.Builder createListDialog(Context context, String title,
                                                   String[] items, MaterialDialog.ListCallback listCallback) {
        return createListDialog(context, title, null, -1, true, items, listCallback);
    }

    /**
     * 创建列表对话框
     *
     * @param context
     * @param title
     * @param content
     * @param iconRes
     * @param autoDismiss
     * @param items
     * @param listCallback
     * @return
     */
    public MaterialDialog.Builder createListDialog(Context context, String title, String content, int iconRes, boolean autoDismiss,
                                                   String[] items, MaterialDialog.ListCallback listCallback) {
        mBuilder = new MaterialDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) mBuilder.title(title);
        if (!TextUtils.isEmpty(content)) mBuilder.content(content);
        if (iconRes != -1) mBuilder.iconRes(iconRes);
        mBuilder.autoDismiss(autoDismiss);
        if (items != null) {
            mBuilder.items(items);
            if (listCallback != null) mBuilder.itemsCallback(listCallback);
        }
        return mBuilder;
    }

    /**
     * 单选弹窗
     */
    public MaterialDialog.Builder createSingleDialog(Context context, String arr[], MaterialDialog.ListCallbackSingleChoice callback) {
        mBuilder = new MaterialDialog.Builder(context);
        mBuilder.items(arr);
        mBuilder.itemsCallbackSingleChoice(-1, callback);
        return mBuilder;
    }

    /**
     * 多选弹窗
     */
    public MaterialDialog.Builder createMultiSelect(Context context, String arr[], MaterialDialog.ListCallbackMultiChoice callback) {
        mBuilder = new MaterialDialog.Builder(context);
        mBuilder.items(arr);
        mBuilder.itemsCallbackMultiChoice(null, callback);
        return mBuilder;
    }


}
