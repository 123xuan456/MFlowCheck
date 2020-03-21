package com.mtm.flowcheck.bean.json;

public class GetFileServersTokenJson {


    /**
     * message : Success
     * data : {"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ2NzcxNDA1NDYsInBheWxvYWQiOiJ7XCJhcHBJRFwiOlwiNDg0ODRhMzI1ZjM3ODRiODAxNWYzNzg0ZjZhNDAwMDBcIixcImFwcEtleVwiOlwiOWMzZDc3ZjE4ZTQ4NDhkMDk1ZTYyNmU5YjNhMDA5YTNcIixcImFwcFNlY3JldFwiOlwiZmYyNjVjODc5YzRhYzA4MDI4ZTc3YTZjNjYwNzhmOWNlODFjMTViNmZiYzc2YzE4ZjdhMTJhOTdjODU5YzkyYlwiLFwiaXNEZWxldGVcIjowfSJ9.ey-e6lnx7JnVZeGkLsvrirRWh8ZTCIgC3fHzKzyg7vg","refresh_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ3NTYzNDA1NzksInBheWxvYWQiOiJcIjQ4NDg0YTMyNWYzNzg0YjgwMTVmMzc4NGY2YTQwMDAwXCIifQ.ytr4Ny-44o4SMBpW_eJWFt2xQR-pFaEqueT6xU7HOj8","expires_in":7200}
     * code : 200
     */

    private String message;
    private DataBean data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ2NzcxNDA1NDYsInBheWxvYWQiOiJ7XCJhcHBJRFwiOlwiNDg0ODRhMzI1ZjM3ODRiODAxNWYzNzg0ZjZhNDAwMDBcIixcImFwcEtleVwiOlwiOWMzZDc3ZjE4ZTQ4NDhkMDk1ZTYyNmU5YjNhMDA5YTNcIixcImFwcFNlY3JldFwiOlwiZmYyNjVjODc5YzRhYzA4MDI4ZTc3YTZjNjYwNzhmOWNlODFjMTViNmZiYzc2YzE4ZjdhMTJhOTdjODU5YzkyYlwiLFwiaXNEZWxldGVcIjowfSJ9.ey-e6lnx7JnVZeGkLsvrirRWh8ZTCIgC3fHzKzyg7vg
         * refresh_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ3NTYzNDA1NzksInBheWxvYWQiOiJcIjQ4NDg0YTMyNWYzNzg0YjgwMTVmMzc4NGY2YTQwMDAwXCIifQ.ytr4Ny-44o4SMBpW_eJWFt2xQR-pFaEqueT6xU7HOj8
         * expires_in : 7200
         */

        private String access_token;
        private String refresh_token;
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
