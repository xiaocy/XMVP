package com.cqgk.demo.map.net;

import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api {
    public static final String API_BASE_URL = "http://gank.io/api/";

    public static final String API_XNB_BASE_URL = "http://192.168.1.220:9007/market/";


    private static GankService gankService;

    public static GankService getGankService() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = XApi.getInstance().getRetrofit(API_BASE_URL, true).create(GankService.class);
                }
            }
        }
        return gankService;
    }


    private static XnbService xnbService;

    public static XnbService getXnbService() {
        if (xnbService == null) {
            synchronized (Api.class) {
                if (xnbService == null) {
                    xnbService = XApi.getInstance().getRetrofit(API_XNB_BASE_URL, true).create(XnbService.class);
                }
            }
        }
        return xnbService;
    }
}
