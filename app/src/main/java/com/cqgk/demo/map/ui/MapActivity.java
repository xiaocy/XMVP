package com.cqgk.demo.map.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cqgk.demo.map.R;
import com.cqgk.demo.map.permission.HintDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * Created by Administrator on 2017/11/26/0026.
 */

public class MapActivity extends XActivity implements AMap.OnMapClickListener, AMap.OnMapLoadedListener, AMap.OnMapLongClickListener
, AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener, HintDialogFragment.DialogFragmentCallback,AMapLocationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 地图view
    @BindView(R.id.map)
    MapView mMapView;

    // 地图控制
    private AMap aMap;

    // 地图定位蓝点
    MyLocationStyle myLocationStyle;

    // 地理位置解析
    GeocodeSearch geocoderSearch;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Button locbtn;
    private Button stobtn;
    private Marker locMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化高德地图
        initAMap(savedInstanceState);
    }

    @OnClick({R.id.locbtn, R.id.stobtn})
    void onClick(View view){
        switch (view.getId()){
            case  R.id.locbtn:
                checkLocationPermission();
                break;
            default:
                break;
        }
    }

    private void initAMap(Bundle savedInstanceState) {

        // 此方法必须重写，在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);// 显示定位蓝点
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        // 地理位置解析
        if(null == geocoderSearch){
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
        }

        initLocation();//初始化定位参数
        checkStoragePermission();//初始化请求权限，存储权限
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


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

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
    public void onMyLocationChange(android.location.Location location){
    //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        // 解析result获取地址描述信息
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        //RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);

        //geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private void checkLocationPermission() {
        // 检查是否有定位权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY","没有权限");
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            //已经获得权限，则执行定位请求。
            Toast.makeText(MapActivity.this, "已获取定位权限",Toast.LENGTH_SHORT).show();

            startLocation();

        }
    }

    private void checkStoragePermission() {
        // 检查是否有存储的读写权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY","没有权限");
            requestPermission(STORAGE_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            Toast.makeText(MapActivity.this, "已获取存储的读写权限",Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission(int permissioncode) {
        String permission = getPermissionString(permissioncode);
        if (!IsEmptyOrNullString(permission)){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if(permissioncode == LOCATION_PERMISSION_CODE) {
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                            R.string.location_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                } else if (permissioncode == STORAGE_PERMISSION_CODE) {
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                            R.string.storage_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                }


            } else {
                Log.i("MY","返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{permission}, permissioncode);
            }
        }
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
        mlocationClient.startLocation();
        Log.i("MY","startLocation");
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
            mlocationClient = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MapActivity.this,"定位权限已获取",Toast.LENGTH_SHORT).show();
                    Log.i("MY","定位权限已获取");
                    startLocation();
                } else {
                    Toast.makeText(MapActivity.this,"定位权限被拒绝",Toast.LENGTH_SHORT).show();
                    Log.i("MY","定位权限被拒绝");
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                        DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                                R.string.location_description_why_we_need_the_permission,
                                requestCode);
                        newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                        Log.i("MY","false 勾选了不再询问，并引导用户去设置中手动设置");

                        return;
                    }
                }
                return;
            }
            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MapActivity.this,"存储权限已获取",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapActivity.this,"存储权限被拒绝",Toast.LENGTH_SHORT).show();
                    Log.i("MY","定位权限被拒绝");
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                                R.string.storage_description_why_we_need_the_permission,
                                requestCode);
                        newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                        Log.i("MY","false 勾选了不再询问，并引导用户去设置中手动设置");
                    }
                    return;
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    @Override
    public void doNegativeClick(int requestCode) {

    }

    @Override
    public void doPositiveClick(int requestCode) {
        String permission = getPermissionString(requestCode);
        if (!IsEmptyOrNullString(permission)){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{permission},
                        requestCode);
            }else{
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private String getPermissionString(int requestCode){
        String permission = "";
        switch (requestCode){
            case LOCATION_PERMISSION_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }

}
