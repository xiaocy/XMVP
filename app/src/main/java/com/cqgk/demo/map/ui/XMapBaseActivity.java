package com.cqgk.demo.map.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cqgk.demo.map.model.FarmInfo;
import com.cqgk.demo.map.present.PXBase;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/1/0001.
 */

public abstract class XMapBaseActivity extends XActivity<PXBase> {

    @Override
    public PXBase newP() {
        return new PXBase();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMapPermissions();
    }

    protected void requestMapPermissions(){
        requestRxPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public abstract void onPermissionGranted(Boolean granted);

    //请求权限
    private void requestRxPermissions(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean granted) throws Exception {
                onPermissionGranted(granted);
            }
        });
    }

    public void onError(NetError error){

    }

    public abstract void showData(FarmInfo farmInfo);
}
