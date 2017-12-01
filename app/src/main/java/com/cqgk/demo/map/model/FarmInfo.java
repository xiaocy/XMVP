package com.cqgk.demo.map.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30/0030.
 */

public class FarmInfo extends BaseModel {

    private String msg;
    private int code;

    public FarmData getData() {
        return data;
    }

    public void setData(FarmData data) {
        this.data = data;
    }

    private FarmData data;

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

    public static class FarmData{
        private List<Item> all;
        private List<Item> nearby;

        public List<Item> getAll() {
            return all;
        }

        public void setAll(List<Item> all) {
            this.all = all;
        }

        public List<Item> getNearby() {
            return nearby;
        }

        public void setNearby(List<Item> nearby) {
            this.nearby = nearby;
        }

        public static class Item {
            /**
             * id : 3629b9d5-f455-4bb6-b13a-7d7075b0f844
             * farmName : 眉山新都化工复合肥有限公司
             * longitude : 103.785012
             * latitude : 30.047036
             * userName : 王磊
             * address : 中国四川省眉山市东坡区
             * areaName :
             * areaId :
             * favorite : true
             * imgUrl : http://fs.51xnb.cn/52b3a014-272f-42ad-b668-90f6d5a03397.jpg
             * farmRecordId : 41e04f96-1dc7-4186-933f-00089ff70190
             * farmType : 1
             * company : 蛙宝生物
             * latestDate : 2017-11-15 13:22:42
             * ismine : false
             * recordDetailUrl :
             * recordCount : 1
             * areaCount :
             */

            private String id;
            private String farmName;
            private double longitude;
            private double latitude;
            private String userName;
            private String address;
            private String areaName;
            private String areaId;
            private boolean favorite;
            private String imgUrl;
            private String farmRecordId;
            private int farmType;
            private String company;
            private String latestDate;
            private boolean ismine;
            private String recordDetailUrl;
            private int recordCount;
            private String areaCount;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFarmName() {
                return farmName;
            }

            public void setFarmName(String farmName) {
                this.farmName = farmName;
            }

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

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public boolean isFavorite() {
                return favorite;
            }

            public void setFavorite(boolean favorite) {
                this.favorite = favorite;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getFarmRecordId() {
                return farmRecordId;
            }

            public void setFarmRecordId(String farmRecordId) {
                this.farmRecordId = farmRecordId;
            }

            public int getFarmType() {
                return farmType;
            }

            public void setFarmType(int farmType) {
                this.farmType = farmType;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getLatestDate() {
                return latestDate;
            }

            public void setLatestDate(String latestDate) {
                this.latestDate = latestDate;
            }

            public boolean isIsmine() {
                return ismine;
            }

            public void setIsmine(boolean ismine) {
                this.ismine = ismine;
            }

            public String getRecordDetailUrl() {
                return recordDetailUrl;
            }

            public void setRecordDetailUrl(String recordDetailUrl) {
                this.recordDetailUrl = recordDetailUrl;
            }

            public int getRecordCount() {
                return recordCount;
            }

            public void setRecordCount(int recordCount) {
                this.recordCount = recordCount;
            }

            public String getAreaCount() {
                return areaCount;
            }

            public void setAreaCount(String areaCount) {
                this.areaCount = areaCount;
            }
        }
    }
}
