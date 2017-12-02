package com.cqgk.demo.map.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30/0030.
 */

public class FarmInfo extends BaseModel {

    private String msg;
    private int code;

    private List<Item> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public static class Item {
        /**
         * longitude : 0
         * latitude : 0
         * count : 4
         * logoUrl : http://fs.51xnb.cn/19501757-11e8-43dd-82f9-cb304dd9ff2a.jpg
         * farmId :
         * lastFarmRecordId :
         * farmName :
         * farmType :
         * company :
         */

        private double longitude;
        private double latitude;
        private long count;
        private String logoUrl;
        private String farmId;
        private String lastFarmRecordId;
        private String farmName;
        private String farmType;
        private String company;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getFarmId() {
            return farmId;
        }

        public void setFarmId(String farmId) {
            this.farmId = farmId;
        }

        public String getLastFarmRecordId() {
            return lastFarmRecordId;
        }

        public void setLastFarmRecordId(String lastFarmRecordId) {
            this.lastFarmRecordId = lastFarmRecordId;
        }

        public String getFarmName() {
            return farmName;
        }

        public void setFarmName(String farmName) {
            this.farmName = farmName;
        }

        public String getFarmType() {
            return farmType;
        }

        public void setFarmType(String farmType) {
            this.farmType = farmType;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }
}
