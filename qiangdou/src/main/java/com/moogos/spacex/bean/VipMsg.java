package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Desc
 * Created by xupeng on 2018/1/24.
 */

public class VipMsg implements Serializable{

    /**
     * code : 200
     * msg : ok
     * result : {"sum_inviter":"7","vip_type":"1","vip_expire":"2018-1-30 12:30:45","get_record":"1000"}
     */

    private int code;
    private String msg;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * sum_inviter : 7
         * vip_type : 1
         * vip_expire : 2018-1-30 12:30:45
         * get_record : 1000
         */

        private String sum_inviter;
        private String vip_type;
        private String vip_expire;
        private String get_record;

        public String getSum_inviter() {
            return sum_inviter;
        }

        public void setSum_inviter(String sum_inviter) {
            this.sum_inviter = sum_inviter;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }

        public String getVip_expire() {
            return vip_expire;
        }

        public void setVip_expire(String vip_expire) {
            this.vip_expire = vip_expire;
        }

        public String getGet_record() {
            return get_record;
        }

        public void setGet_record(String get_record) {
            this.get_record = get_record;
        }
    }
}
