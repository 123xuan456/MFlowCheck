package com.mtm.flowcheck.bean;

import java.util.List;

/**
 *
 * 流行病学调查未开始页bean
 */
public class NotStartBean {
    private List<CheckBean> data;

    public List<CheckBean> getData() {
        return data;
    }

    public void setData(List<CheckBean> data) {
        this.data = data;
    }
}
