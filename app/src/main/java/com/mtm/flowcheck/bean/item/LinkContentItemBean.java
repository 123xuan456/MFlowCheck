package com.mtm.flowcheck.bean.item;

/**
 * Created By WangYanBin On 2020\03\19 09:19.
 * <p>
 * （LinkContentItemBean）
 * 参考：
 * 描述：
 */
public class LinkContentItemBean {

    public final static int OPERATION_TYPE_INPUT = 0x1;// 输入
    public final static int OPERATION_TYPE_SINGLE_CHOOSE = 0x2;// 单选
    public final static int OPERATION_TYPE_MULTIPLE_CHOOSE = 0x3;// 多选
    public final static int OPERATION_TYPE_TIME_CHOOSE = 0x4;// 时间选择
    public final static int OPERATION_TYPE_JUMP_ACTIVITY = 0x5;// 跳转页面

    private String fieldName;
    private String content;
    private String[] options;
    private int operationType;
    private int titleStrId;
    private int hintStrId;
    private boolean isEmphasis;

    public LinkContentItemBean(int titleStrId, int hintStrId, String fieldName, String content, int operationType) {
        this.titleStrId = titleStrId;
        this.hintStrId = hintStrId;
        this.fieldName = fieldName;
        this.content = content;
        this.operationType = operationType;
    }

    public LinkContentItemBean(int titleStrId, int hintStrId, String fieldName, String content, int operationType, boolean isEmphasis) {
        this.titleStrId = titleStrId;
        this.hintStrId = hintStrId;
        this.fieldName = fieldName;
        this.content = content;
        this.operationType = operationType;
        this.isEmphasis = isEmphasis;
    }

    public LinkContentItemBean(int titleStrId, int hintStrId, String fieldName, String content, String[] options, int operationType) {
        this.fieldName = fieldName;
        this.content = content;
        this.options = options;
        this.operationType = operationType;
        this.titleStrId = titleStrId;
        this.hintStrId = hintStrId;
    }

    public LinkContentItemBean(int titleStrId, int hintStrId, String fieldName, String content, String[] options, int operationType, boolean isEmphasis) {
        this.titleStrId = titleStrId;
        this.hintStrId = hintStrId;
        this.fieldName = fieldName;
        this.content = content;
        this.options = options;
        this.operationType = operationType;
        this.isEmphasis = isEmphasis;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getTitleStrId() {
        return titleStrId;
    }

    public void setTitleStrId(int titleStrId) {
        this.titleStrId = titleStrId;
    }

    public int getHintStrId() {
        return hintStrId;
    }

    public void setHintStrId(int hintStrId) {
        this.hintStrId = hintStrId;
    }

    public boolean isEmphasis() {
        return isEmphasis;
    }

    public void setEmphasis(boolean emphasis) {
        isEmphasis = emphasis;
    }
}
