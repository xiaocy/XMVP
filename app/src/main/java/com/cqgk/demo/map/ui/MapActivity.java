package com.cqgk.demo.map.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cqgk.demo.map.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/26/0026.
 */

public class MapActivity extends XActivity implements LocationSource, AMapLocationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 地图view
    @BindView(R.id.map)
    MapView mMapView;

    // 地图控制
    private AMap aMap;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Marker locMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化高德地图
        initAMap(savedInstanceState);
    }

    /**
     * This is the fragment-orientated version of {@link #onResume()} that you
     * can override to perform operations in the Activity at the same point
     * where its fragments are resumed.  Be sure to always call through to
     * the super-class.
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }

    private void initAMap(Bundle savedInstanceState) {

        // 此方法必须重写，在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //myLocationStyle.showMyLocation(true);// 显示定位蓝点
        //aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        //aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);

        initLocation();//初始化定位参数

        checkLocationPermissions();
        startLocation();
    }

    //定位
    private void initLocation() {

        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位监听
        mlocationClient.setLocationListener(this);
        //定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置为单次定位
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(MapActivity.class)
                .data(new Bundle())
                .launch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        destroyLocation();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null) {
            mMapView.onPause();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolbar();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        getSupportActionBar().setTitle("地图");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            double longitude = aMapLocation.getLongitude();
            double latitude = aMapLocation.getLatitude();
            LatLng location = new LatLng(latitude, longitude);
            changeLocation(location);
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
            Toast.makeText(MapActivity.this, errText, Toast.LENGTH_LONG).show();
        }
    }

    private void changeLocation(LatLng location) {
        if (locMarker == null){
            locMarker = aMap.addMarker(new MarkerOptions().position(location));
        }else{
            locMarker.setPosition(location);
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        // 启动定位

        if(ContextCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            checkLocationPermissions();
        }else{
            mlocationClient.startLocation();
        }
    }
    /**
     * 停止定位
     */
    private void stopLocation() {
        // 停止定位
        mlocationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    private void checkLocationPermissions(){
        requestRxPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //请求权限
    private void requestRxPermissions(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean granted) throws Exception {
                if (granted){
                    Toast.makeText(MapActivity.this, "已获取权限", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MapActivity.this, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
