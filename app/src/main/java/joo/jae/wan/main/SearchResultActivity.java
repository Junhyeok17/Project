package joo.jae.wan.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.*;
import com.skt.Tmap.poi_item.TMapPOIItem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.*;

import java.io.File;
import java.util.EventObject;

import java.util.ArrayList;

import joo.jae.wan.R;

public class SearchResultActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    double Distance;
    //TMapView tMapView = null;
    // T Map GPS
    // TMapGpsManager tMapGPS = null;

    EditText editText;


    String API_Key = "l7xx1ee83da12e334595b10d8658f0816106";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;


    String search_s_r;
    String search_e_r;
    String search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        search= intent.getStringExtra("searchData");
        Intent intent_s =getIntent();
        search_s_r= intent_s.getStringExtra("start");
        Intent intent_e =getIntent();
        search_e_r= intent_e.getStringExtra("end");

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        /* 줌레벨 */
        tMapView.setZoomLevel(16);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setHttpsMode(true);


        tMapGPS = new TMapGpsManager(this);
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(5);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        //실내일 때 유용합니다.
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); //gps로 현 위치를 잡습니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
        }
        //  LocationManager locationManager = (LocationManager)ct.getSystemService(LOCATION_SERVICE);

        // 위치 정보 권한 허가
        //    if(ContextCompat.checkSelfPermission(ct, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
        //      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        //  } else {
        //    displayMyLocation();
        // }
        tMapGPS.OpenGps();


        //현재위치 네임

        tMapView.setIconVisibility(true);
        /*  화면중심을 단말의 현재위치로 이동 */
        tMapView.setTrackingMode(true);
        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);

        Log.d(tMapView.getLocationPoint().toString(), "====================================");


        Log.d(search, "================================");
        TMapData tmapdata = new TMapData();

        if(search!=null) {

            tmapdata.findAllPOI(search, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(ArrayList poiItem) {
                    TMapMarkerItem markerItem1;
                    for (int i = 0; i < poiItem.size(); i++) {
                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                        Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                "Point: " + item.getPOIPoint().toString());

                        TMapPoint tMapPoint1 = item.getPOIPoint();
                        // 마커 아이콘
                        markerItem1 = new TMapMarkerItem();
                        Context context = getBaseContext();

                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
                        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                        markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                        markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                        tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가



                                /*그냥 이런식으로 목적지 등록후 경로
                                TMapPoint tMapPointStart = item.getPOIPoint(); // (출발지)
                                TMapPoint tMapPointEnd = new TMapPoint(37.544951, 126.951900); // (목적지)

                                TMapData tmapdata = new TMapData();

                                final ArrayList<TMapPoint> passList = new ArrayList<>();

                                tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart,
                                        tMapPointEnd, passList, 10, new TMapData.FindPathDataListenerCallback() {
                                            //이거 코드
                                            @Override
                                            public void onFindPathData(TMapPolyLine polyLine) {
                                                polyLine.setLineColor(Color.BLUE);
                                                polyLine.setLineWidth(20);
                                                polyLine.setLineAlpha(150);
                                                Distance = polyLine.getDistance();
                                                String d = Double.toString(Distance);
                                                Log.i("거리거리거리걱리거릭--------------------0-------------------------", d);
                                                //거리는 미터 단위로 나옴
                                                tMapView.addTMapPath(polyLine);// 이거 코드 보면 될듯

                                            }
                                        });*/
                    }
                }
            });
        }
        else{
            if(search_s_r!=null) {

                tmapdata.findAllPOI(search_s_r, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        TMapMarkerItem markerItem1;
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                    "Point: " + item.getPOIPoint().toString());

                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();

                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가




                                    /*그냥 이런식으로 목적지 등록후 경로
                                    TMapPoint tMapPointStart = item.getPOIPoint(); // (출발지)
                                    TMapPoint tMapPointEnd = new TMapPoint(37.544951, 126.951900); // (목적지)

                                    TMapData tmapdata = new TMapData();

                                    final ArrayList<TMapPoint> passList = new ArrayList<>();

                                    tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart,
                                            tMapPointEnd, passList, 10, new TMapData.FindPathDataListenerCallback() {
                                                //이거 코드
                                                @Override
                                                public void onFindPathData(TMapPolyLine polyLine) {
                                                    polyLine.setLineColor(Color.BLUE);
                                                    polyLine.setLineWidth(20);
                                                    polyLine.setLineAlpha(150);
                                                    Distance = polyLine.getDistance();
                                                    String d = Double.toString(Distance);
                                                    Log.i("거리거리거리걱리거릭--------------------0-------------------------", d);
                                                    //거리는 미터 단위로 나옴
                                                    tMapView.addTMapPath(polyLine);// 이거 코드 보면 될듯

                                                }
                                            });*/
                        }
                    }
                });

            }
            if(search_e_r!=null){

                tmapdata.findAllPOI(search_e_r, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        TMapMarkerItem markerItem1;
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                    "Point: " + item.getPOIPoint().toString());

                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();

                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가



                                /*그냥 이런식으로 목적지 등록후 경로
                                TMapPoint tMapPointStart = item.getPOIPoint(); // (출발지)
                                TMapPoint tMapPointEnd = new TMapPoint(37.544951, 126.951900); // (목적지)

                                TMapData tmapdata = new TMapData();

                                final ArrayList<TMapPoint> passList = new ArrayList<>();

                                tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart,
                                        tMapPointEnd, passList, 10, new TMapData.FindPathDataListenerCallback() {
                                            //이거 코드
                                            @Override
                                            public void onFindPathData(TMapPolyLine polyLine) {
                                                polyLine.setLineColor(Color.BLUE);
                                                polyLine.setLineWidth(20);
                                                polyLine.setLineAlpha(150);
                                                Distance = polyLine.getDistance();
                                                String d = Double.toString(Distance);
                                                Log.i("거리거리거리걱리거릭--------------------0-------------------------", d);
                                                //거리는 미터 단위로 나옴
                                                tMapView.addTMapPath(polyLine);// 이거 코드 보면 될듯

                                            }
                                        });*/
                        }
                    }
                });

            }

        }
    }

    @Override
    public void onLocationChange(Location location) {
        //  Log.d("현재 값 : ", address + " " + location.getLatitude() + " " + location.getLongitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        //현재위치 추적


    }

}