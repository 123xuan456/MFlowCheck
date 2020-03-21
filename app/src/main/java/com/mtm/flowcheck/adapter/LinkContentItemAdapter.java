package com.mtm.flowcheck.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.item.LinkContentItemBean;

import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_INPUT;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_JUMP_ACTIVITY;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_MULTIPLE_CHOOSE;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_SINGLE_CHOOSE;
import static com.mtm.flowcheck.bean.item.LinkContentItemBean.OPERATION_TYPE_TIME_CHOOSE;

/**
 * Created By WangYanBin On 2020\03\19 10:20.
 * <p>
 * （LinkContentItemAdapter）
 * 参考：
 * 描述：
 */
public class LinkContentItemAdapter extends BaseQuickAdapter<LinkContentItemBean, BaseViewHolder> {

    private int linkStrId = -1;
    private boolean isEdit = true;
    private TextWatcherCallback mCallback;

    public LinkContentItemAdapter(TextWatcherCallback mCallback) {
        super(R.layout.check_table_details_item);
        this.mCallback = mCallback;
    }

    public LinkContentItemAdapter(int linkStrId, TextWatcherCallback mCallback) {
        this(mCallback);
        this.linkStrId = linkStrId;
    }

    public LinkContentItemAdapter(int linkStrId, TextWatcherCallback mCallback, boolean isEdit) {
        this(mCallback);
        this.linkStrId = linkStrId;
        this.isEdit = isEdit;
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkContentItemBean item) {
        helper.setText(R.id.tv_title, item.getTitleStrId());
        EditText contentEt = helper.getView(R.id.et_content);
        if (item.isEmphasis()) helper.setTextColor(R.id.tv_title, Color.RED);
        if (item.getHintStrId() != 0) contentEt.setHint(item.getHintStrId());
        if (isEdit) {
            switch (item.getOperationType()) {
                case OPERATION_TYPE_INPUT:// 输入
                    initInputView(helper, item);
                    break;
                case OPERATION_TYPE_SINGLE_CHOOSE:// 单选
                    initSingleChooseView(helper, item);
                    break;
                case OPERATION_TYPE_MULTIPLE_CHOOSE:// 多选
                    initMultipleChooseView(helper, item);
                    break;
                case OPERATION_TYPE_TIME_CHOOSE:// 时间选择
                    initTimeChooseView(helper);
                    break;
                case OPERATION_TYPE_JUMP_ACTIVITY:// 跳转
                    initJumpActivityView(helper);
                    break;
            }
        } else {
            forbidInput(helper);
            helper.setGone(R.id.iv_next, false);
        }
    }

    /**
     * 初始化输入视图
     *
     * @param helper
     */
    private void initInputView(BaseViewHolder helper, LinkContentItemBean item) {
        EditText contentEt = helper.getView(R.id.et_content);
        contentEt.setText(item.getContent());
        contentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCallback.afterTextChanged(LinkContentItemAdapter.this, helper.getAdapterPosition(), s);
            }
        });
        helper.setGone(R.id.iv_next, false);
    }

    /**
     * 禁止输入
     *
     * @param helper
     */
    private void forbidInput(BaseViewHolder helper) {
        EditText contentEt = helper.getView(R.id.et_content);
        contentEt.setFocusable(false);//不可编辑
        contentEt.setFocusableInTouchMode(false);//不可编辑
    }

    /**
     * 初始化单选视图
     *
     * @param helper
     * @param item
     */
    private void initSingleChooseView(BaseViewHolder helper, LinkContentItemBean item) {
        forbidInput(helper);
        helper.addOnClickListener(R.id.et_content);
        helper.setText(R.id.et_content, item.getContent());
    }

    /**
     * 初始化多选视图
     *
     * @param helper
     * @param item
     */
    private void initMultipleChooseView(BaseViewHolder helper, LinkContentItemBean item) {
        forbidInput(helper);
        helper.addOnClickListener(R.id.et_content);
        helper.setText(R.id.et_content, item.getContent());
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(item.getContent())) {
            String[] contents = item.getContent().split("\\|\\|");
            for (int i = 0; i < contents.length; i++) {
                sb.append(contents[i]).append(",");
            }
            sb = new StringBuffer(sb.subSequence(0, sb.length() - 1));
        }
        helper.setText(R.id.et_content, sb.toString());
    }

    /**
     * 初始化时间选择视图
     *
     * @param helper
     */
    private void initTimeChooseView(BaseViewHolder helper) {
        forbidInput(helper);
        helper.addOnClickListener(R.id.et_content);
    }

    /**
     * 初始化跳转页面视图
     *
     * @param helper
     */
    private void initJumpActivityView(BaseViewHolder helper) {
        forbidInput(helper);
        helper.addOnClickListener(R.id.et_content);
    }

    public interface TextWatcherCallback {

        void afterTextChanged(BaseQuickAdapter adapter, int position, Editable s);
    }
}
