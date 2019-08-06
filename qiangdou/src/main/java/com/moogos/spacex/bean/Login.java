package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Created by xiaokewang on 2017/12/7.
 */

public class Login implements Serializable {


    /**
     * code : 200
     * msg : ok
     * result : {"user_id":"1","invitation_code":"23sa","user_phone":"18870857487","device_id":"2"}
     */

    private int code;
    private String msg;
    private UserInfo result;

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

    public UserInfo getResult() {
        return result;
    }

    public void setResult(UserInfo result) {
        this.result = result;
    }

    public static class UserInfo implements Serializable {
        /**
         * user_id : 1
         * invitation_code : 23sa
         * user_phone : 18870857487
         * device_id : 2
         */

        private String user_id;
        private String invitation_code;
        private String user_phone;
        private String device_id;

        private String vip_type;        //vip等级类型
        private String sum_inviter;     //已邀请人数
        private String vip_expire;
        private String get_record;      //领取记录

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getInvitation_code() {
            return invitation_code;
        }

        public void setInvitation_code(String invitation_code) {
            this.invitation_code = invitation_code;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }

        public String getSum_inviter() {
            return sum_inviter;
        }

        public void setSum_inviter(String sum_inviter) {
            this.sum_inviter = sum_inviter;
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
