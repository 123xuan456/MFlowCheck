package com.mtm.flowcheck.bean;

/**
 * Created By WangYanBin On 2020\03\18 12:03.
 * <p>
 * （RequestResultBean）
 * 参考：
 * 描述：{"status":0,"error":{"code":"1000","message":"操作成功"},"data": null}
 */
public class RequestResultBean {

    private int status;// 状态
    private ErrorBean error;// 异常
    private String data;// 数据

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class ErrorBean {

        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
