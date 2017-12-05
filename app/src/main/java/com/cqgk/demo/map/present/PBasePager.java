package com.cqgk.demo.map.present;

import com.cqgk.demo.map.model.FarmInfo;
import com.cqgk.demo.map.model.GankResults;
import com.cqgk.demo.map.net.Api;
import com.cqgk.demo.map.ui.BasePagerFragment;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by wanglei on 2016/12/31.
 */

public class PBasePager extends XPresent<BasePagerFragment> {
    protected static final int PAGE_SIZE = 10;


    public void loadData(String type, final int page) {
        Api.getGankService().getGankData(type, PAGE_SIZE, page)
                .compose(XApi.<GankResults>getApiTransformer())
                .compose(XApi.<GankResults>getScheduler())
                .compose(getV().<GankResults>bindToLifecycle())
                .subscribe(new ApiSubscriber<GankResults>() {
                    @Override
                    protected void onFail(NetError error) {
                        getV().showError(error);
                    }

                    @Override
                    public void onNext(GankResults gankResults) {
                        getV().showData(page, gankResults);
                    }
                });
    }

    /**
     * 获取地图数据
     *
     * @param longitude
     * @param latitude
     * @param maxLongitude
     * @param maxLatitude
     * @param minLongitude
     * @param minLatitude
     */
    public void loadFarmInfos(Integer zoomLevel ,double longitude, double latitude, double maxLongitude, double maxLatitude, double minLongitude, double minLatitude){
        Api.getXnbService().getFarmInfos(zoomLevel, maxLongitude, minLongitude, maxLatitude, minLatitude, longitude, latitude)
                .compose(XApi.<FarmInfo>getApiTransformer())
                .compose(XApi.<FarmInfo>getScheduler())
                .compose(getV().<FarmInfo>bindToLifecycle())
                .subscribe(new ApiSubscriber<FarmInfo>() {
                    @Override
                    protected void onFail(NetError error) {
                        getV().showError(error);
                    }

                    @Override
                    public void onNext(FarmInfo results) {
                        getV().showFarmInfo(results);
                    }
                });
    }
}
