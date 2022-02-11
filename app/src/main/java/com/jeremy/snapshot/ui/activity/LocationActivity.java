/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：LocationActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.LocationActivity
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月29日 00:02:20
 */


package com.jeremy.snapshot.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.adapter.PoiItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements OnGetGeoCoderResultListener, View.OnClickListener, PoiItemAdapter.MyOnItemClickListener {

    private MapView map;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private boolean isFirstLocate = true;
    private String currentPositio = "";
    private GeoCoder mGeoCoder;
    private TextView positionText;
    private Button located;
    private Button confirm;
    private ImageView back;
    private List<PoiInfo> poiInfos = new ArrayList<>();
    private RecyclerView poiRecycler;
    private PoiItemAdapter poiItemAdapter;
    private String curPosition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this);
        setContentView(R.layout.activity_location);
        located = (Button) findViewById(R.id.button_locate);
        //positionText = (TextView) findViewById(R.id.position_text);
        map = (MapView) findViewById(R.id.map);
        confirm = (Button) findViewById(R.id.btn_ok);
        back = (ImageView) findViewById(R.id.locate_back);
        poiRecycler = (RecyclerView) findViewById(R.id.recycler_poi);
        confirm.setOnClickListener(this);
        located.setOnClickListener(this);
        back.setOnClickListener(this);
        map.showZoomControls(true);
        poiItemAdapter = new PoiItemAdapter(poiInfos);
        poiItemAdapter.setOnItemClickListener(this);
        poiRecycler.setAdapter(poiItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        poiRecycler.setLayoutManager(layoutManager);
        //map.addView(located);
        //map.addView(positionText);
        //map.addView();
        baiduMap = map.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        //设置locationClientOption
        locationClient.setLocOption(option);
        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             *
             * @param point 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng point) {

                Log.d("locationTest", "aaa");
                reverseRequest(point);
                mapMoveCenter(point);
            }

            /**
             * 地图内 Poi 单击事件回调函数
             *
             * @param mapPoi 点击的 poi 信息
             */
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                mapMoveCenter(mapPoi.getPosition());
                reverseRequest(mapPoi.getPosition());
                //positionText.setText(mapPoi.getName());
                positionText.append(mapPoi.getName());
            }
        };
        //设置地图单击事件监听
        baiduMap.setOnMapClickListener(listener);

        locationClient.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                //返回地址
                Intent intent = new Intent();
                intent.putExtra("location_return", curPosition);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.button_locate:
                isFirstLocate = true;
                locationClient.start();
                break;
            case R.id.locate_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position, PoiInfo poiInfo) {
        if (null == poiInfo || null == poiInfo.getLocation()) {
            return;
        }
        if (position != 0)
            curPosition = poiInfo.address + "-" + poiInfo.name;
        //Log.d("testAAA", "success");
        mapMoveCenter(poiInfo.getLocation());
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || map == null) {
                return;
            }
            if (isFirstLocate) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(18f);
                baiduMap.animateMapStatus(update);
                isFirstLocate = false;
            }
            StringBuilder str = new StringBuilder();
            str.append(location.getProvince())
                    .append(location.getCity()).append(location.getDistrict())
                    .append(location.getStreet())
                    .append(location.getTown());
            String locationDescribe = location.getLocationDescribe();
            //positionText.setText(str);
            //Toast.makeText(LocationActivity.this, locationDescribe, Toast.LENGTH_SHORT).show();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            reverseRequest(latLng);
            locationClient.stop();
        }
    }

    private void reverseRequest(LatLng latLng) {
        if (null == latLng) {
            return;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng)
                .newVersion(1)
                .radius(500);
        if (null == mGeoCoder) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null == reverseGeoCodeResult) {
            return;
        }

        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult
                .getLocation()));
        poiInfos = reverseGeoCodeResult.getPoiList();
        PoiInfo curAddressPoiInfo = new PoiInfo();
        curAddressPoiInfo.address = reverseGeoCodeResult.getAddress();
        curAddressPoiInfo.location = reverseGeoCodeResult.getLocation();

        if (null == poiInfos) {
            poiInfos = new ArrayList<>(2);
        }
        curPosition = reverseGeoCodeResult.getAddress() + reverseGeoCodeResult.getSematicDescription();
        poiItemAdapter.descri = reverseGeoCodeResult.getSematicDescription();
        poiInfos.add(0, curAddressPoiInfo);

        poiItemAdapter.updateData(poiInfos);
        if (poiInfos != null && poiInfos.size() > 0)
            for (int i = 0; i < poiInfos.size(); i++) {
                Log.d("POI", poiInfos.get(i).getAddress() + "  " + poiInfos.get(i).getName());
            }
    }

    //将标记点移至中心
    private void mapMoveCenter(LatLng arg0) {

        baiduMap.clear();

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(arg0)
                //.zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.animateMapStatus(mMapStatusUpdate);
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
        //	MapStatusUpdate state = MapStatusUpdateFactory.zoomBy(4);
        //	mBaiduMap.animateMapStatus(state);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark);
        OverlayOptions option = new MarkerOptions().position(arg0).icon(mCurrentMarker);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
        // 获取位置名称
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        baiduMap.setMyLocationEnabled(false);
        map.onDestroy();
    }
}