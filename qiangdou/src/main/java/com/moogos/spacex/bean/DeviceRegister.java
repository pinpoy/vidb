package com.moogos.spacex.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaokewang on 2017/12/17.
 */

public class DeviceRegister implements Serializable {


    /**
     * code : 200
     * msg : ok
     * result : {"device_id":"3","skip_times":"1","device_active_record":[{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-17T09:54:45.000Z"}]}
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
         * device_id : 3
         * skip_times : 1
         * device_active_record : [{"hongbao_channel":"wechat","host_nick_name":"火火火","hongbao_money":0.01,"active_time":"2017-12-17T09:54:45.000Z"}]
         */

        private String device_id;
        private String skip_times;
        private List<DeviceActiveRecordBean> device_active_record;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getSkip_times() {
            return skip_times;
        }

        public void setSkip_times(String skip_times) {
            this.skip_times = skip_times;
        }

        public List<DeviceActiveRecordBean> getDevice_active_record() {
            return device_active_record;
        }

        public void setDevice_active_record(List<DeviceActiveRecordBean> device_active_record) {
            this.device_active_record = device_active_record;
        }

        public static class DeviceActiveRecordBean implements Serializable{
            /**
             * hongbao_channel : wechat
             * host_nick_name : 火火火
             * hongbao_money : 0.01
             * active_time : 2017-12-17T09:54:45.000Z
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
