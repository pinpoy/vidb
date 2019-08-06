package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Created by xiaokewang on 2017/12/7.
 */

public class Response implements Serializable {

    /**
     * code : 200
     * msg : ok
     */

    private int code;
    private String msg;

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
}
