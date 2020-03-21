package com.mtm.flowcheck.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created By WangYanBin On 2020\03\18 14:20.
 * <p>
 * （LinkBean）
 * 参考：
 * 描述：
 */
@Entity
public class LinkBean {

    public final static int LINK_BASE_INFO = 1;// 基本信息
    public final static int LINK_DIAGNOSIS_COURSE = 2;// 发病就诊过程
    public final static int LINK_OSCULATION_CONTACT = 3;// 密接情况
    public final static int LINK_EPIDEMIC_DISEASE_HISTORY = 4;// 流行病学史
    public final static int LINK_DETECTION_INFO = 5;// 实验室检测信息

    private String taskId; // 任务ID
    private int linkType; // 环节信息 1、基本信息；2、发病就诊过程；3、密接情况；4、流行病学史；5、实验室检测信息
    private int linkValue; // 0是未做，1是已做


    @Generated(hash = 479717789)
    public LinkBean(String taskId, int linkType, int linkValue) {
        this.taskId = taskId;
        this.linkType = linkType;
        this.linkValue = linkValue;
    }

    @Generated(hash = 1730739555)
    public LinkBean() {
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(int linkValue) {
        this.linkValue = linkValue;
    }

}
