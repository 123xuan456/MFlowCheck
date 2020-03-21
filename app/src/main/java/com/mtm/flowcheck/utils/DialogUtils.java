package com.mtm.flowcheck.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogUtils {

    private static DialogUtils instance;

    public static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    private AlertDialog.Builder mBuilder;
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
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissLoadingDialog();
            }
        });
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

    /**
     * 显示确认弹窗
     *
     * @param mContext    上下文对象
     * @param message     提示内容
     * @param affirmClick 确认事件
     */
    public void showAffirmDialog(Context mContext, int message, DialogInterface.OnClickListener affirmClick) {
        showAffirmDialog(mContext, mContext.getString(message), affirmClick);
    }

    /**
     * 显示确认弹窗
     *
     * @param mContext    上下文对象
     * @param message     提示内容
     * @param affirmClick 确认事件
     */
    public void showAffirmDialog(Context mContext, String message, DialogInterface.OnClickListener affirmClick) {
        mBuilder = new AlertDialog.Builder(mContext).setMessage(message)
                .setPositiveButton("确认", affirmClick)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mBuilder.create().show();
    }

    /**
     * 显示列表弹窗
     *
     * @param mContext
     * @param contents
     * @param mOnClickListener
     */
    public void showListDialog(Context mContext, String[] contents, DialogInterface.OnClickListener mOnClickListener) {
        mBuilder = new AlertDialog.Builder(mContext)
                .setItems(contents, mOnClickListener);
        mBuilder.create().show();
    }

    /**
     * 显示多选弹窗
     *
     * @param mContext    上下文对象
     * @param contents    内容数组
     * @param choiceClick 选择事件
     * @param affirmClick 确认事件
     */
    public void showMultipleSelectDialog(Context mContext, String[] contents, DialogInterface.OnMultiChoiceClickListener choiceClick,
                                         DialogInterface.OnClickListener affirmClick) {
        boolean[] selected = new boolean[contents.length];// 是否选中
        mBuilder = new AlertDialog.Builder(mContext)
                .setMultiChoiceItems(contents, selected, choiceClick)
                .setPositiveButton("确定", affirmClick);
        mBuilder.create().show();
    }
}
