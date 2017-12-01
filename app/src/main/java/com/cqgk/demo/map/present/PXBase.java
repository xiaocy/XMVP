package com.cqgk.demo.map.present;

import com.cqgk.demo.map.model.FarmInfo;
import com.cqgk.demo.map.net.Api;
import com.cqgk.demo.map.ui.XMapBaseActivity;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by Administrator on 2017/12/1/0001.
 */

public class PXBase extends XPresent<XMapBaseActivity> {

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
    public void loadFarmInfos(double longitude, double latitude, double maxLongitude, double maxLatitude, double minLongitude, double minLatitude){
        Api.getXnbService().getFarmInfos(maxLongitude, minLongitude, maxLatitude, minLatitude, longitude, latitude)
                .compose(XApi.<FarmInfo>getApiTransformer())
                .compose(XApi.<FarmInfo>getScheduler())
                .compose(getV().<FarmInfo>bindToLifecycle())
                .subscribe(new ApiSubscriber<FarmInfo>() {
                    @Override
                    protected void onFail(NetError error) {
                        getV().onError(error);
                    }

                    @Override
                    public void onNext(FarmInfo results) {
                        getV().showData(results);
                    }
                });
    }
}
