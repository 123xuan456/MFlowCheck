package com.mtm.flowcheck.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Created by mzp on 2014/6/8.
 * 操作 字符串
 */
public class StringUtils {
    public final static String UTF_8 = "utf-8";

    public final static String YES="YES";
    public final static String NO="NO";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }
    public static boolean isBlank(String obj) {
        if (obj == null || "".equals(obj) || "".equals(obj.trim())
                || "null".equals(obj.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(int resId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select_btn_prismatic href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></select_btn_prismatic>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }

    public static String defautString(String obj) {
        if (obj == null) {
            return "";
        } else {
            return obj;
        }
    }

    /**
     * 获取当前时间: 年 月 日
     */
    public static String getDate4YMD() {
        return String.format("%tF", System.currentTimeMillis());
    }

    /**
     * 获取当前时间: 时 分 秒
     */
    public static String getDate4HMM() {
        return String.format("%tT", System.currentTimeMillis());
    }


    /**
     * 拼接许可证号
     */
    public static String getXkzh(String w, String z, String k, String h) {
        return w + "卫" + z + "字[" + k + "]第" + h + "号";
    }

    /**
     * 拼接许可证号  生活饮用水
     */
    public static String getXkzhWater(String w, String k, String h) {
        return w + "卫水监  字[" + k + "]第" + h + "号";
    }

    /**
     * 获取字符串
     */
    public static String getStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * 单位信息 set 数据 处理
     * 1.去空 去null
     * 2.添加2个空格
     */
    public static String getDisStr(String str) {
        if (!TextUtils.isEmpty(str)) {
            return "  " + str.trim().replace("null", "");
        } else {
            return "  -";
        }
    }

    /**
     * 单位信息 set 数据 处理
     * 1.去空 去null
     * 2.添加2个空格
     */
    public static String isNoBlank(String str) {
        if (!TextUtils.isEmpty(str)) {
            return "" + str.trim().replace("null", "");
        } else {
            return str;
        }
    }

    /**
     * 获取经营状况
     */
    public static String getJyzk(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "1":
                type = "营业";
                break;
            case "2":
                type = "关闭";
                break;
            default:
                type = "-";
        }
        return type;
    }

    /**
     * 根据 1 or 0 得到 是 或 否
     */
    public static String getYesOrNo(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "1":
                type = "是";
                break;
            case "0":
                type = "否";
                break;
            default:
                type = "-";
        }
        return type;
    }

    /**
     * 根据 1 or 0 得到 否 或 是
     */
    public static String getNoOrYes(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "1":
                type = "否";
                break;
            case "0":
                type = "是";
                break;
            default:
                type = "-";
        }
        return type;
    }

    /**
     * 1 or 0 得到 有 或 无
     */
    public static String getHave(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "1":
                type = "有";
                break;
            case "0":
                type = "无";
                break;
            default:
                type = "-";
        }
        return type;
    }

    /**
     * 1 or 0 得到 无 或 有
     */
    public static String getNoHave(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "1":
                type = "无";
                break;
            case "0":
                type = "有";
                break;
            default:
                type = "-";
        }
        return type;
    }

    /**
     * 判断 一个 集合 是否为空 && size > 0
     */
    public static boolean isListEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }
    /**
     * 获取对象的一个字段 如果为空 返回null
     */

    /**
     * 数据库可以识别的不带 '-' 的uuid字符串
     *
     * @return String
     */
    public static String getUUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Float strToFloat(String number) {
        try {
            return Float.valueOf(number);
        } catch (Throwable ExecutionException) {
            return 0f;
        }
    }

    public static Integer strToInteger(String strNum) {
        try {
            return Integer.valueOf(strNum);
        } catch (Throwable ExecutionException) {
            return 0;
        }
    }

    /**
     * 风险类型
     */
    public  static String getFxlxCode(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "0":
                type = "上级交办";
                break;
            case "1":
                type = "媒体曝光";
                break;
            case "2":
                type = "先照后证";
                break;
            case "3":
                type = "多次举报";
                break;
            case "4":
                type = "多次处罚";
                break;
            case "5":
                type = "处罚金额超限";
                break;
            case "6":
                type = "责令改正";
                break;
            case "7":
                type = "举报监督查证";
                break;
            case "8":
                type = "难点领域";
                break;
            default:
                type = "-";
        }
        return type;
    }
    /**
     * 0合格 1不合格
     */
    public  static String getMete(String type, String data) {
        type = getDisStr(type).trim();
        data = getDisStr(data).trim();
        switch (type) {
            case "1":
                type = data;
                break;
            case "2":
                switch (data) {
                    case "01":
                        type = "合格";
                        break;
                    case "02":
                        type = "不合格";
                        break;
                    case "03":
                        type = "合理缺项";
                        break;
                    default:
                        type = data;
                        break;
                }
                break;
        }
        return type;
    }
    /**
     * 0未上报 1已上报
     */
    public  static String getReport(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "0":
                type = "";
                break;
            case "1":
                type = "√";
                break;
        }
        return type;
    }
    /**
     * 获取专业类别
     */
    public  static String getSpeCode(String type) {
        type = getDisStr(type).trim();
        switch (type) {
            case "01":
                type = "公共场所";
                break;
            case "02":
                type = "生活饮用水";
                break;
            case "03":
                type = "职业卫生";
                break;
            case "04":
                type = "放射卫生";
                break;
            case "05":
                type = "学校卫生";
                break;
            case "06":
                type = "医疗卫生";
                break;
            case "0701":
                type = "消毒产品单位";
                break;
            case "0703":
                type = "传染病防治";
                break;
            case "0704":
                type = "餐饮具集中消毒单位";
                break;
            case "08":
                type = "血液安全";
                break;
            case "09":
                type = "计划生育";
                break;
            default:
                type = "-";
        }
        return type;
    }
    /**
     * 获取 EditText 输入内容 去空
     */
    public static String getEditText(EditText mEdit){
        return mEdit.getText().toString().trim();
    }
    public static String getText(TextView mText){
        return mText.getText().toString().trim();
    }



}
