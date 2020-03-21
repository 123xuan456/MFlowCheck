package com.mtm.flowcheck.bean.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created By WangYanBin On 2020\03\20 16:52.
 * <p>
 * （RegionBean）
 * 参考：
 * 描述：{"ID":"2","PID":"1","AREA_CODE":"11000000","AREA_NAM":"北京市","ALL_NAME":"北京市"}
 */
@Entity
public class RegionBean {

    @Index(unique = true)
    private String ID;
    private String PID;
    private String AREA_CODE;
    private String AREA_NAM;
    private String ALL_NAME;

    @Generated(hash = 1853684008)
    public RegionBean() {
    }

    @Generated(hash = 1935178917)
    public RegionBean(String ID, String PID, String AREA_CODE, String AREA_NAM, String ALL_NAME) {
        this.ID = ID;
        this.PID = PID;
        this.AREA_CODE = AREA_CODE;
        this.AREA_NAM = AREA_NAM;
        this.ALL_NAME = ALL_NAME;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPID() {
        return this.PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getAREA_CODE() {
        return this.AREA_CODE;
    }

    public void setAREA_CODE(String AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getAREA_NAM() {
        return this.AREA_NAM;
    }

    public void setAREA_NAM(String AREA_NAM) {
        this.AREA_NAM = AREA_NAM;
    }

    public String getALL_NAME() {
        return this.ALL_NAME;
    }

    public void setALL_NAME(String ALL_NAME) {
        this.ALL_NAME = ALL_NAME;
    }

}
