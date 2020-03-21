package com.mtm.flowcheck.bean;

/**
 * @author Liwenqing
 * @date 2020/3/20
 */
public class DataMapUtil {
    public static String getConfirmedSources(String code){
        switch (code){
            case "1":
                return "大网确诊";
            case "2":
                return "专家确诊";
        }
        return "";
    }

    public static String getDiagnosisType(String code){
        switch (code){
            case "2":
                return "确诊病例";
            case "3":
                return "疑似病例";
            case "5":
                return "阳性检测";
        }
        return "";
    }

    public static String getGender(String code){
        switch (code){
            case "1":
                return "男";
            case "2":
                return "女";
        }
        return "";
    }

    public static String getProfessional(String code){
        switch (code){
            case "1":
                return "幼托儿童";
            case "2":
                return "散居儿童";
            case "3":
                return "学生";
            case "4":
                return "教师";
            case "5":
                return "保育员及保姆";
            case "6":
                return "餐饮食品业";
            case "7":
                return "公共场所服务员";
            case "8":
                return "商业服务";
            case "9":
                return "医务人员";
            case "16":
                return "工人";
            case "17":
                return "民工";
            case "18":
                return "农民";
            case "19":
                return "牧民";
            case "20":
                return "渔(船)民";
            case "21":
                return "海员及长途驾驶员";
            case "22":
                return "干部职员";
            case "23":
                return "离退人员";
            case "24":
                return "家务及待业";
            case "28":
                return "不详";
            case "29":
                return "其它";
        }
        return "";
    }

    public static String getClinicalSeverity(String code){
        switch (code){
            case "2":
                return "轻症病例";
            case "3":
                return "重症肺炎";
            case "4":
                return "危重症肺炎";
            case "5":
                return "无症状感染者";
            case "6":
                return "普通肺炎";
        }
        return "";
    }

    public static String getOutcome(String code){
        switch (code){
            case "1":
                return "痊愈";
            case "2":
                return "死亡";
        }
        return "";
    }

    public static String getSymptoms(String code){
        switch (code){
            case "1":
                return "寒战";
            case "2":
                return "干咳";
            case "3":
                return "咳痰";
            case "4":
                return "鼻塞";
            case "5":
                return "流涕";
            case "6":
                return "咽痛";
            case "7":
                return "头痛";
            case "8":
                return "乏力";
            case "9":
                return "肌肉酸痛";
            case "10":
                return "关节酸痛";
            case "11":
                return "气促";
            case "12":
                return "呼吸困难";
            case "13":
                return "胸闷";
            case "14":
                return "胸痛";
            case "15":
                return "结膜充血";
            case "16":
                return "恶心";
            case "17":
                return "呕吐";
            case "18":
                return "腹泻";
            case "19":
                return "腹痛";
        }
        return "";
    }

    public static String getIfFamily(String code){
        switch (code){
            case "1":
                return "是";
            case "2":
                return "否";
        }
        return "";
    }

    public static String getTrafficTools(String code){
        switch (code){
            case "1":
                return "火车";
            case "2":
                return "飞机";
            case "3":
                return "大巴";
            case "4":
                return "出租车";
            case "5":
                return "其他";
        }
        return "";
    }

    public static String getInfectOriginSort(String code){
        switch (code){
            case "1":
                return "确诊病例或阳性人员接触史";
            case "2":
                return "疑似病例接触史";
            case "3":
                return "京外居住史";
            case "4":
                return "京外旅行史";
            case "5":
                return "其他人员接触史";
        }
        return "";
    }

    public static String getInfectOrigTo(String code){
        switch (code){
            case "1":
                return "来京";
            case "2":
                return "返京";
        }
        return "";
    }

    public static String getCaseType(String code){
        if("".equals(code)){
            return "";
        }
        switch (code){
            case "2":
                return "确诊病例";
            case "3":
                return "疑似病例";
            case "5":
                return "阳性检测";
        }
        return "";
    }
}
