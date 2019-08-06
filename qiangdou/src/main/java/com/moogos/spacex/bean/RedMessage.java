package com.moogos.spacex.bean;

import java.io.Serializable;

/**
 * Desc
 * Created by xupeng on 2017/12/21.
 */

public class RedMessage implements Serializable {

    private String channel;
    private String money;
    private String nickName;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }




}
