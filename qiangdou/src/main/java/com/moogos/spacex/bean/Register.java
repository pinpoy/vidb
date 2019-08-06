package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Created by xiaokewang on 2017/12/11.
 */

public class Register implements Serializable {


    /**
     * code : 200
     * msg : ok
     * result : {"user_id":"3","invitation_code":"0126","device_id":"3"}
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
         * user_id : 3
         * invitation_code : 0126
         * device_id : 3
         */

        private String user_id;
        private String invitation_code;
        private String device_id;

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

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }
    }
}
