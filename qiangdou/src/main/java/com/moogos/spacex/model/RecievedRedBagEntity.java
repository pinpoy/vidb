package com.moogos.spacex.model;

/**
 * Created by zhangliang on 15/12/11.
 */

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.net.URLDecoder;

@Table(name = "recieved_redbag")
public class RecievedRedBagEntity {
    @Id(column = "id")
    private int id;
    @Column(column = "name")
    private String name;
    @Column(column = "time")
    private long time;
    @Column(column = "from1")
    private String from;

    @Override
    public String toString() {
        return "RecievedRedBagEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", coin='" + coin + '\'' +
                ", from='" + from + '\'' +
                '}';
    }

    @Column(column = "coin")
    private String coin;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
