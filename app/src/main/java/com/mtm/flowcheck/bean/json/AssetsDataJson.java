package com.mtm.flowcheck.bean.json;

/**
 * Created By WangYanBin On 2020\03\20 17:01.
 * <p>
 * （AssetsDataJson）
 * 参考：
 * 描述：
 * region：{"updateTime":"20200320","data":[{"ID":"1","AREA_CODE":"00000000","AREA_NAM":"全国","ALL_NAME":"全国"}]}
 * organ：{"updateTime":"20200320","data":[{"ID":"1","ORG_NAME":"北京市鼓楼中医医院","ORG_CODE":"110101000","PID":"5"}]}
 */
public class AssetsDataJson {

    private String updateTime;
    private String data;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
