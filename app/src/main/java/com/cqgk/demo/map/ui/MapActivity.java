package com.cqgk.demo.map.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cqgk.demo.map.R;
import com.cqgk.demo.map.model.FarmInfo;
import com.cqgk.demo.map.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.router.Router;


/**
 * Created by Administrator on 2017/11/26/0026.
 */

public class MapActivity extends XMapBaseActivity implements LocationSource, AMapLocationListener, AMap.OnMyLocationChangeListener,
        AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 地图view
    @BindView(R.id.map)
    MapView mMapView;

    private TextView popTitle;
    private TextView popDesc;
    private ImageView popLogo;

    private View popupWindow;

    // 地图控制
    private AMap aMap;

    private Bitmap markerBmp = null;
    private Resources resources;

    private OnLocationChangedListener  mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    // 默认紫光信息港
    private LatLng centerPoint = new LatLng(22.5549830000,113.9477080000);// 中心点

    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Marker locMarker;

    MyLocationStyle myLocationStyle;
    Boolean isFirstLoc = true;

    int getZoomB = 18;

    private Map<MarkerOptions, Marker> markerMap = new HashMap<MarkerOptions, Marker>();
    private Map<Marker, FarmInfo.Item> markerItemMap = new HashMap<Marker, FarmInfo.Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化高德地图
        initAMap(savedInstanceState);

        resources = getResources();
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);// 显示定位蓝点

        //精度圆圈的自定义：
        //精度圈颜色自定义方法如下
        myLocationStyle.strokeColor(Color.argb(100, 0,40,40));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(60,0,200,100));//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.strokeWidth(0.1f);//设置定位蓝点精度圈的边框宽度的方法。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        // 位置改变监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);

        startLocation();

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupWindow = layoutInflater.inflate(R.layout.map_popupwindow, null);
        popLogo = (ImageView) popupWindow.findViewById(R.id.iv_farm_logo);
        popTitle = (TextView)popupWindow.findViewById(R.id.tv_farm_name);
        popDesc = (TextView)popupWindow.findViewById(R.id.tv_farm_desc);

        mMapView.addView(popupWindow, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL));
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

       /* //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng ll = new LatLng(latitude, longitude);

        //定位成功回调信息，设置相关消息
        LatLng rightBottom = aMap.getProjection().fromScreenLocation(new Point(mMapView.getRight(), mMapView.getHeight()));
        LatLng leftTop = aMap.getProjection().fromScreenLocation(new Point(0, 60));

        getP().loadFarmInfos(4, longitude, latitude, leftTop.latitude, rightBottom.latitude, rightBottom.longitude, leftTop.longitude);*/
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){

                /*double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                LatLng location = new LatLng(latitude, longitude);
                changeLocation(location);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                StringBuilder stringBuilder = new StringBuilder();
                //定位成功回调信息，设置相关消息
                int type = aMapLocation.getLocationType();
                String address = aMapLocation.getAddress();
                stringBuilder.append(type+address);
                Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();*/

                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    //aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                    /*StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();*/
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
               /* Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();*/
            }
        }
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        // 启动定位

        if(ContextCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestMapPermissions();
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

    @Override
    public void onPermissionGranted(Boolean granted) {
        if (granted){
            if(null != mapLocationClient){
                mapLocationClient.startLocation();
            }
        }else {
            Toast.makeText(MapActivity.this, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        centerPoint = cameraPosition.target;
        Toast.makeText(this, "当前缩放级别" + cameraPosition.zoom, Toast.LENGTH_SHORT).show();
        if (getZoomB != (int)cameraPosition.zoom){

            // 缩放比例改变
            getZoomB = (int)cameraPosition.zoom;

            //定位成功回调信息，设置相关消息
            LatLng rightBottom = aMap.getProjection().fromScreenLocation(new Point(mMapView.getRight(), mMapView.getHeight()));
            LatLng leftTop = aMap.getProjection().fromScreenLocation(new Point(0, 60));

            AMapLocation location = mapLocationClient.getLastKnownLocation();
            getP().loadFarmInfos((int)cameraPosition.zoom, rightBottom.longitude, leftTop.longitude,
                    leftTop.latitude, rightBottom.latitude, location.getLongitude(),
                    location.getLatitude());
        }
    }

    private void clearMarker(){
        for (Map.Entry<MarkerOptions, Marker> entry : markerMap.entrySet()) {
            if(null != entry.getValue()){
                entry.getValue().remove();
            }
        }
    }

    @Override
    public void showData(FarmInfo farmInfo) {
        clearMarker();
        FarmInfo fif = farmInfo;
        List<FarmInfo.Item> items = fif.getData();
        if(Utils.isCollectionNotEmpty(items)){
            ArrayList<MarkerOptions> options = new ArrayList<MarkerOptions>();
            for (FarmInfo.Item item: items) {
                MarkerOptions markerOption = new MarkerOptions();
                LatLng ll = new LatLng(item.getLatitude(), item.getLongitude());
                markerOption.position(ll);
                markerOption.draggable(false);//设置Marker可拖动
                //markerOption.icon(BitmapDescriptorFactory.fromBitmap(getBitmap((int)it.getCount(), "")));
                View v = getLogoView(this, item.getLogoUrl(), markerOption);
                markerOption.icon(BitmapDescriptorFactory.fromView(v));
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                markerOption.setFlat(false);//设置marker平贴地图效果

                Marker mkk = aMap.addMarker(markerOption);
                markerItemMap.put(mkk, item);
                markerMap.put(markerOption, mkk);
            }
            //aMap.addMarkers(options, false);

            zoomToSpan(items);
        }

        //aMap.moveCamera(CameraUpdateFactory.zoomTo(5));
    }

    /**
     *  缩放移动地图，保证所有自定义marker在可视范围中。
     */
    public void zoomToSpan(List<FarmInfo.Item> items) {
        if (Utils.isCollectionNotEmpty(items) && items.size() > 1) {
            if (aMap == null)
                return;
            //centerMarker.setVisible(false);
            LatLngBounds bounds = getLatLngBounds(centerPoint, items);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<FarmInfo.Item> items) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null){
            for (int i = 0; i < items.size(); i++) {
                LatLng p = new LatLng(items.get(i).getLatitude(), items.get(i).getLongitude());
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     * 根据自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds( List<FarmInfo.Item> items) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < items.size(); i++) {
            LatLng p = new LatLng(items.get(i).getLatitude(), items.get(i).getLongitude());
            b.include(p);
        }
        return b.build();
    }

    public View getLogoView(final Context mContext, final String imgUrl, final MarkerOptions markerOption ) {
        final LayoutInflater lf = this.getLayoutInflater();
        final View view = this.getLayoutInflater().inflate(R.layout.marker,null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.civ_logo_new);

        /*
        imageView.setTag(markerOption);
        ILoader.Options options = new ILoader.Options(ILoader.Options.RES_NONE, ILoader.Options.RES_NONE);
        ILFactory.getLoader().loadNet(mContext, imgUrl, options, new LoadCallback(){

            @Override
            public void onLoadReady(Bitmap bitmap){
                MarkerOptions markerOption = (MarkerOptions)imageView.getTag();
                LatLng ll = markerOption.getPosition();
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                aMap.addMarker(markerOption);
            };
        });*/

        SimpleTarget target = new SimpleTarget<GlideDrawable>(40,40) {
            @Override
            public void onResourceReady(GlideDrawable bitmap, GlideAnimation glideAnimation) {
                // do something with the bitmap
                // for demonstration purposes, let's just set it to an ImageView
                imageView.setImageDrawable(bitmap);
                if (markerMap.containsKey(markerOption)){
                    Marker mk = markerMap.get(markerOption);
                    FarmInfo.Item item = markerItemMap.get(mk);
                    mk.remove();
                    markerItemMap.remove(mk);

                    MarkerOptions mkNew = new MarkerOptions();
                    LatLng ll = new LatLng(markerOption.getPosition().latitude, markerOption.getPosition().longitude);
                    mkNew.position(ll);
                    mkNew.title(markerOption.getTitle());
                    mkNew.snippet(markerOption.getSnippet());
                    mkNew.draggable(false);//设置Marker可拖动
                    //markerOption.icon(BitmapDescriptorFactory.fromBitmap(getBitmap((int)it.getCount(), "")));

                    View rootView = lf.inflate(R.layout.marker,null);
                    //final CircleImageView iv = (CircleImageView) rootView.findViewById(R.id.civ_logo);
                    final RoundedImageView iv = (RoundedImageView) rootView.findViewById(R.id.iv_logo);
                    iv.setImageDrawable(bitmap);
                    TextView tv = (TextView) rootView.findViewById(R.id.civ_num);
                    if(item.getCount() > 1){
                        tv.setText(item.getCount()+"");
                        tv.setVisibility(View.VISIBLE);
                    }

                    mkNew.icon(BitmapDescriptorFactory.fromView(rootView));
                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                    mkNew.setFlat(false);//设置marker平贴地图效果

                    Marker mll = aMap.addMarker(mkNew);
                    markerMap.put(mkNew, mll);
                    markerItemMap.put(mll, item);
                }
            }
        };

        Glide.with(mContext)
                .load(imgUrl)
                //等待状态图片
                .placeholder(R.mipmap.location)
                //加载失败图片
                .error(R.mipmap.location)
                .override(100, 100)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);

        return view;
    }

    /**
     * 去掉MarkerOverlay上所有的Marker。
     */
    public void removeFromMap() {
        /*for (Marker mark : mMarkers) {
            mark.remove();
        }
        centerMarker.remove();*/
    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter() {
       /* if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            centerMarker.setVisible(true);
            centerMarker.showInfoWindow();
            LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        FarmInfo.Item item = markerItemMap.get(marker);
        if(!Utils.isStringEmpty(item.getLogoUrl())){
            ILFactory.getLoader().loadNet(popLogo, item.getLogoUrl(), null);
        }

        if(!Utils.isStringEmpty(item.getFarmName())){
            popTitle.setText(item.getFarmName());
        }

        popDesc.setText(String.valueOf(item.getCount()));
        //infoPopupWindow.showAtLocation(markerView.get(marker), Gravity.CENTER, 0, 0);
        popupWindow.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(null != popupWindow){
            if(View.VISIBLE == popupWindow.getVisibility()){
                popupWindow.setVisibility(View.INVISIBLE);
            }
        }
    }
}
