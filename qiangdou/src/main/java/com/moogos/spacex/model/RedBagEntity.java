package com.moogos.spacex.model;

import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.NetUtils;
import com.moogos.spacex.util.Util;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.UUID;

public class RedBagEntity implements Serializable{

    public static final String DEFAULT_MONEY = "-1";
    private String nickname;
    private String redbagid;
    private String send_nickname;       //昵称
    private String from;                //渠道
    private String money = DEFAULT_MONEY;
    private int status = 1;  //1未初始化 0成功
    private String reason;

    public void reset() {
        nickname = "";
        if (User.isLogin) {
            redbagid = (Util.getCurTime() + User.getUserId()).substring(4);
        } else {
            redbagid = (Util.getCurTime() + "" + UUID.randomUUID().hashCode()).substring(4);
        }
        send_nickname = "";
        from = "";
        money = DEFAULT_MONEY;
        status = 1;
        reason = "";
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public RedBagEntity() {
        if (User.isLogin) {
            redbagid = (Util.getCurTime() + User.getUserId()).substring(4);
        } else {
            redbagid = (Util.getCurTime() + "" + UUID.randomUUID().hashCode()).substring(4);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSend_nickname() {
        return send_nickname;
    }

    public void setSend_nickname(String send_nickname) {
        this.send_nickname = send_nickname;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "nickname=" + nickname + "&redbagid=" + redbagid
                + "&send_nickname=" + send_nickname + "&money=" + money + "&status=" + status + "&from=" + from + "&reason=" + reason;
    }

    public void sendRedBag(int type) {
        if (!money.equalsIgnoreCase(DEFAULT_MONEY)) {
            StringBuilder sb = GlobalConfig.getCommonInfo(type);

            sb.append(redbagid).append("|");
            sb.append(nickname).append("|");
            sb.append(send_nickname).append("|");
            sb.append(money).append("|");
            sb.append(status).append("|");
            sb.append(from).append("|");


            NetUtils.postLog(sb.toString());
        }
    }


}
