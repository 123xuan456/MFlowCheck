package com.mtm.flowcheck.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.manager.BaseApplication;

/**
 * Description : 统一管理类
 */
public class ToastUitl {


    public static Toast toast;
    private static Toast toast2;

    private static Toast initToast(final CharSequence message, final int duration) {
        if (UIUtils.isRunInMainThread()) {
            showToastSafe(message,duration);
        } else {
            UIUtils.post(new Runnable() {
                @Override
                public void run() {
                     showToastSafe(message,duration);
                }
            });
        }

        return toast;
    }
    private static Toast initToast1(final CharSequence message, final int duration) {
        if (UIUtils.isRunInMainThread()) {
            showToastSafe(message,duration);
        } else {
            UIUtils.post(new Runnable() {
                @Override
                public void run() {
                    showToastSafe(message,duration);
                }
            });
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    /**
     * 任意线程里都可以使用
     * @param message 内容
     * @param duration 时间
     */
    private static Toast showToastSafe(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getApplication(), message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast
     *
     */
    public static void showShort(int strResId) {
//		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        initToast(BaseApplication.getApplication().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     */
    public static void showLong(CharSequence message) {
        initToast1(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     */
    public static void showLong(int strResId) {
        initToast1(BaseApplication.getApplication().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    public static void show(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }

    /**
     * 自定义显示Toast时间
     *
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }

    /**
     * 显示有image的toast
     *
     * @param tvStr 内容
     * @param imageResource 图片id
     * @return Toast
     */
    public static Toast showToastWithImg(final String tvStr, final int imageResource) {
        if (toast2 == null) {
            toast2 = new Toast(BaseApplication.getApplication());
        }
        View view = UIUtils.inflate(R.layout.layout_toast);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);
        ImageView iv = (ImageView) view.findViewById(R.id.toast_custom_iv);
        if (imageResource > 0) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imageResource);
        } else {
            iv.setVisibility(View.GONE);
        }
        toast2.setView(view);
        toast2.setGravity(Gravity.CENTER, 0, 0);
        toast2.show();
        return toast2;
    }

}
