package com.yisingle.study.map.one.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.yisingle.amapview.lib.base.view.marker.AbstractMarkerView;
import com.yisingle.amapview.lib.base.view.marker.BaseMarkerView;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.amapview.lib.viewholder.MapInfoWindowViewHolder;
import com.yisingle.study.map.one.R;
import com.yisingle.study.map.one.TestDataUtils;
import com.yisingle.study.map.one.base.BaseMapActivity;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;


/**
 * @author jikun
 * Created by jikun on 2018/4/27.
 * 平滑移动效果
 */
public class PointMarkUseMoveActivty extends BaseMapActivity {

    private TextureMapView textureMapView;


    private PointMarkerView<String> moveMarkerView;


    private List<LatLng> nowListPoints = TestDataUtils.readLatLngsnow();

    private List<LatLng> resumeListPoints = TestDataUtils.readLatLngsresume();


    @Override
    protected void afterMapViewLoad() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_marker_map);
        textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);

        //移动并画移动的坐标点线
        moveToCameraAndDrawLine();


        moveMarkerView = new PointMarkerView.Builder(getApplicationContext(), getAmap())
                //这里要设置锚点在markrer的中间
                .setAnchor(0.5f, 0.5f)
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car)).create();

        moveMarkerView.bindInfoWindowView(new BaseMarkerView.BaseInfoWindowView<String>(R.layout.info_window, "") {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, String data) {

                viewHolder.setText(R.id.tvInfoWindow, data);

            }
        });

        moveMarkerView.setMoveListener(new AbstractMarkerView.OnMoveListener() {
            @Override
            public void onMove(LatLng latLng) {

            }
        });


        moveMarkerView.showInfoWindow("infoWindow显示");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        moveMarkerView.destory();
    }


    public void startMove(View view) {


        moveCamre(nowListPoints);
        /**
         *      * @param list
         * @param isResume  是否延续运动 true 如果marker正在运动不会打断,而是将这个坐标点加入到运动的中轨迹坐标数组里
         *                              false 立即在新的运动轨迹数组里运动。
         */
        moveMarkerView.startMove(nowListPoints, false);

    }

    public void stopMove(View view) {


        moveMarkerView.stopMove();
    }

    public void resumeMove(View view) {

        moveCamre(TestDataUtils.readLatLngsAll());
        /**
         *      * @param list
         * @param isResume  是否延续运动 true 如果marker正在运动不会打断,而是将这个坐标点加入到运动的中轨迹坐标数组里
         *                              false 立即在新的运动轨迹数组里运动。
         */
        moveMarkerView.startMove(resumeListPoints, true);

    }


    public void startOtherMove(View view) {


        moveCamre(resumeListPoints);
        /**
         *      * @param list
         * @param isResume  是否延续运动 true 如果marker正在运动不会打断,而是将这个坐标点加入到运动的中轨迹坐标数组里
         *                              false 立即在新的运动轨迹数组里运动。
         */
        moveMarkerView.startMove(resumeListPoints, false);


    }


    public void startOnlyOne(View view) {

        List<LatLng> list = new ArrayList<>();
        list.add(new LatLng(30.554803, 104.068991));
        list.add(new LatLng(30.556669, 104.068991));
        moveCamre(list);
        List<LatLng> oneList = new ArrayList<>();
        oneList.add(new LatLng(30.554803, 104.068991));
        moveMarkerView.startMove(oneList, true);

    }


    public void startOtherOnlyOne(View view) {


        List<LatLng> list = new ArrayList<>();
        list.add(new LatLng(30.554803, 104.068991));
        list.add(new LatLng(30.556669, 104.068991));
        moveCamre(list);
        List<LatLng> oneList = new ArrayList<>();
        oneList.add(new LatLng(30.556669, 104.068991));
        /**
         *      * @param list
         * @param isResume  是否延续运动 true 如果marker正在运动不会打断,而是将这个坐标点加入到运动的中轨迹坐标数组里
         *                              false 立即在新的运动轨迹数组里运动。
         */
        moveMarkerView.startMove(oneList, true);

    }


    public void moveToCameraAndDrawLine() {


        // 获取轨迹坐标点
        List<LatLng> points = TestDataUtils.readLatLngsnow();

        List<LatLng> points1 = TestDataUtils.readLatLngsresume();

        //setCustomTextureList(bitmapDescriptors)
        getMapView().getMap().addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_smooth))
                .addAll(points)
                .useGradient(true)
                .width(30));

        //setCustomTextureList(bitmapDescriptors)
        getMapView().getMap().addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_slow))
                .addAll(points1)
                .useGradient(true)
                .width(30));

        LatLngBounds.Builder b = LatLngBounds.builder();
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.addAll(points);
        latLngList.addAll(points1);
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    private void moveCamre(List<LatLng> latLngList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        LatLngBounds bounds = b.build();
        getMapView().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
