package com.cqgk.demo.map.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.cqgk.demo.map.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/26/0026.
 */

public class MapActivity extends XActivity implements LocationSource, AMapLocationListener, AMap.OnMyLocationChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 地图view
    @BindView(R.id.map)
    MapView mMapView;

    // 地图控制
    private AMap aMap;

    private OnLocationChangedListener  mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Marker locMarker;

    MyLocationStyle myLocationStyle;

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

        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 小飞机的那个图标
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);// 显示定位蓝点

        //精度圆圈的自定义：
        //精度圈颜色自定义方法如下
        myLocationStyle.strokeColor(Color.argb(180, 0,40,40));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(180,0,200,100));//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.strokeWidth(0.1f);//设置定位蓝点精度圈的边框宽度的方法。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        // 位置改变监听
        aMap.setOnMyLocationChangeListener(this);

        checkLocationPermissions();
        startLocation();
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
    public void onMyLocationChange(Location location) {

        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng ll = new LatLng(latitude, longitude);
        changeLocation(ll);

        StringBuilder stringBuilder = new StringBuilder();

        //定位成功回调信息，设置相关消息
        //stringBuilder.append(type+address);
        Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){

                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                LatLng location = new LatLng(latitude, longitude);
                changeLocation(location);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                StringBuilder stringBuilder = new StringBuilder();
                //定位成功回调信息，设置相关消息
                int type = aMapLocation.getLocationType();
                String address = aMapLocation.getAddress();
                stringBuilder.append(type+address);
                Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
            }else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
                Log.i("erro info：",aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());
            }
        }
    }



    private void changeLocation(LatLng location) {
//        if (locMarker == null){
//            locMarker = aMap.addMarker(new MarkerOptions().position(location));
//        }else{
//            locMarker.setPosition(location);
//        }
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
            mapLocationClient.startLocation();
        }
    }
    /**
     * 停止定位
     */
    private void stopLocation() {
        // 停止定位
        mapLocationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mapLocationClient) {
            mapLocationClient.stopLocation();
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mapLocationClient.onDestroy();
            mapLocationClient = null;
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
                    mapLocationClient.startLocation();
                }else {
                    Toast.makeText(MapActivity.this, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mapLocationClient==null){
            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getApplicationContext());

            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位精度
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是否返回地址信息
            mLocationOption.setNeedAddress(true);
            //是否只定位一次
            mLocationOption.setOnceLocation(true);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //是否允许模拟位置
            mLocationOption.setMockEnable(false);
            //定位时间间隔
            //mLocationOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mLocationOption);
            //绑定监听
            mapLocationClient.setLocationListener(this);
            //开启定位
            mapLocationClient.startLocation();
        }

    }
    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }
}
