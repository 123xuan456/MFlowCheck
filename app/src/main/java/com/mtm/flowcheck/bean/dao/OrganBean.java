package com.mtm.flowcheck.bean.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created By WangYanBin On 2020\03\20 16:52.
 * <p>
 * （OrganBean）
 * 参考：
 * 描述：{"ID":"1","ORG_NAME":"北京市鼓楼中医医院","ORG_CODE":"110101000","PID":"5"}
 */
@Entity
public class OrganBean {

    @Index(unique = true)
    private String ID;
    private String ORG_NAME;
    private String ORG_CODE;
    private String PID;

    @Generated(hash = 2103051759)
    public OrganBean() {
    }

    @Generated(hash = 1024433055)
    public OrganBean(String ID, String ORG_NAME, String ORG_CODE, String PID) {
        this.ID = ID;
        this.ORG_NAME = ORG_NAME;
        this.ORG_CODE = ORG_CODE;
        this.PID = PID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    public String getORG_CODE() {
        return ORG_CODE;
    }

    public void setORG_CODE(String ORG_CODE) {
        this.ORG_CODE = ORG_CODE;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }
}
