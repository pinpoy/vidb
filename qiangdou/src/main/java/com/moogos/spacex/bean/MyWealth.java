package com.moogos.spacex.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaokewang on 2017/12/18.
 */

public class MyWealth implements Serializable {


    /**
     * code : 200
     * msg : ok
     * result : {"sum_money":"56.51","records":[{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T03:02:44.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:47:14.000Z"},{"hongbao_channel":"qq","host_nick_name":"pinpo(晚上上线)","hongbao_money":0.01,"active_time":"2017-12-18T02:46:18.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:45:41.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:45:24.000Z"}]}
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
         * sum_money : 56.51
         * records : [{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T03:02:44.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:47:14.000Z"},{"hongbao_channel":"qq","host_nick_name":"pinpo(晚上上线)","hongbao_money":0.01,"active_time":"2017-12-18T02:46:18.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:45:41.000Z"},{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-18T02:45:24.000Z"}]
         */

        private String sum_money;
        private List<RecordsBean> records;

        public String getSum_money() {
            return sum_money;
        }

        public void setSum_money(String sum_money) {
            this.sum_money = sum_money;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean implements Serializable{
            /**
             * hongbao_channel : wechat
             * host_nick_name : 火火火
             * hongbao_money : 0.01
             * active_time : 2017-12-18T03:02:44.000Z
             */

            private String hongbao_channel;
            private String host_nick_name;
            private double hongbao_money;
            private String active_time;

            public String getHongbao_channel() {
                return hongbao_channel;
            }

            public void setHongbao_channel(String hongbao_channel) {
                this.hongbao_channel = hongbao_channel;
            }

            public String getHost_nick_name() {
                return host_nick_name;
            }

            public void setHost_nick_name(String host_nick_name) {
                this.host_nick_name = host_nick_name;
            }

            public double getHongbao_money() {
                return hongbao_money;
            }

            public void setHongbao_money(double hongbao_money) {
                this.hongbao_money = hongbao_money;
            }

            public String getActive_time() {
                return active_time;
            }

            public void setActive_time(String active_time) {
                this.active_time = active_time;
            }
        }
    }
}
