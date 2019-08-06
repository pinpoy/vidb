package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Created by xiaokewang on 2017/12/15.
 */

public class MobileIp implements Serializable {

    /**
     * ip : 180.173.109.165
     * address : 上海市 电信
     */

    private String ip;
    private String address;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
