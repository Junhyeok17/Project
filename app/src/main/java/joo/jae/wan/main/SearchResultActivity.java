package joo.jae.wan.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.*;
import com.skt.Tmap.poi_item.TMapPOIItem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import java.util.Collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import joo.jae.wan.R;

class NearLocation implements Comparable{
    private TMapPoint point;
    private double distance; // 현 위치와 근접한 좌표까지의 거리

    public NearLocation(TMapPoint point){
        this.point = point;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public TMapPoint getPoint(){
        return point;
    }
    public double getDistance(){
        return distance;
    }

    @Override
    public int compareTo(Object o) {
        NearLocation location = (NearLocation) o;
        if(this.distance > location.getDistance())
            return 1;
        else if(this.distance < location.getDistance())
            return -1;
        else
            return 0;
    }
}

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
    RecyclerView recyclerView;
    SearchAdapter adapter;

    ArrayList<TMapPoint> midList = new ArrayList<>(); // 경유지 모아놓는 리스트
    int rotations = 0; // 경유지 알고리즘 실행 시 사용하는 반복문 변수
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


        //리사이클러
        recyclerView = findViewById(R.id.search_result);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter();

        //현재위치 네임

        tMapView.setIconVisibility(true);
        /*  화면중심을 단말의 현재위치로 이동 */
        tMapView.setTrackingMode(true);
        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);

        Log.d(tMapView.getLocationPoint().toString(), "====================================");


        Log.d(search, "================================");
        TMapData tmapdata = new TMapData();

        TMapPoint tMapPointStart = new TMapPoint(37.451772605, 126.999302798);
        TMapPoint tMapPointEnd = new TMapPoint(37.44905232, 127.011678186); // (목적지)

        List<NearLocation> rangePoints = getRangePoints(37.451772605, 126.999302798,
                37.44905232, 127.011678186);

        for(int i=0;i<rangePoints.size();i++){
            TMapPoint destination = rangePoints.get(i).getPoint();
            Log.d("location", destination.getLatitude()+", "+destination.getLongitude());
            rangePoints.get(i).setDistance(getDistance(tMapPointStart, destination));
        }

        Collections.sort(rangePoints);

        for(int i=0;i<rangePoints.size();i++){
            TMapMarkerItem item = new TMapMarkerItem();
            item.setTMapPoint(rangePoints.get(i).getPoint());
            item.setVisible(TMapMarkerItem.VISIBLE);
            item.setCalloutTitle(i+"번");
            tMapView.addMarkerItem(i+"", item);
        }
        linearLayoutTmap.addView(tMapView);
        //findAllLayovers(tMapPointStart, tMapPointEnd);


        if(search!=null) {
            tmapdata.findAllPOI(search, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(ArrayList poiItem) {
                    TMapMarkerItem markerItem1;

                    //TMapPOIItem item = (TMapPOIItem) poiItem.get(0);

                    for (int i = 0; i < poiItem.size(); i++) {
                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                        Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                "Point: " + item.getPOIPoint().toString());

                        //리사이클러
                        adapter.addItem(new Search(item.getPOIName().toString(), item.getPOIAddress().replace("null", "")));

                        TMapPoint tMapPoint1 = item.getPOIPoint();
                        // 마커 아이콘
                        markerItem1 = new TMapMarkerItem();
                        Context context = getBaseContext();

                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);

                        bitmap = Bitmap.createScaledBitmap(bitmap, 50,50,false);
                        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                        markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                        markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                        tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가

                        /*
                        //그냥 이런식으로 목적지 등록후 경로
                        //TMapPoint tMapPointStart = item.getPOIPoint(); // (출발지)
                        TMapPoint tMapPointStart = new TMapPoint(37.451772605, 126.999302798);
                        TMapPoint tMapPointEnd = new TMapPoint(37.44905232, 127.011678186); // (목적지)

                        TMapData tmapdata = new TMapData();

                        final ArrayList<TMapPoint> passList = midList;

                        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart,
                                tMapPointEnd, passList, 10, new TMapData.FindPathDataListenerCallback() {
                                    //이거 코드
                                    @Override
                                    public void onFindPathData(TMapPolyLine polyLine) {
                                        polyLine.setLineColor(Color.BLUE);
                                        polyLine.setLineWidth(20);
                                        polyLine.setLineAlpha(150);
                                        //거리는 미터 단위로 나옴
                                        tMapView.addTMapPath(polyLine);// 이거 코드 보면 될듯
                                    }
                                }
                        );
                         */
                    }


                }
            });
            recyclerView.setAdapter(adapter);

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

                            //리사이클러
                            adapter.addItem(new Search(item.getPOIName().toString(), item.getPOIAddress().replace("null", "")));


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
                        }
                    }
                });
                recyclerView.setAdapter(adapter);

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


                            //리사이클러
                            adapter.addItem(new Search(item.getPOIName().toString(), item.getPOIAddress().replace("null", "")));


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


/*
                                //그냥 이런식으로 목적지 등록후 경로
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
                recyclerView.setAdapter(adapter);


            }
        }
    }

    private double getDistance(TMapPoint a, TMapPoint b){
        return Math.sqrt(Math.pow(a.getLatitude()-b.getLatitude(), 2)+Math.pow(a.getLongitude()-b.getLongitude(), 2));
    }

    @Override
    public void onLocationChange(Location location) {
        //  Log.d("현재 값 : ", address + " " + location.getLatitude() + " " + location.getLongitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        //현재위치 추적
    }

    // 경유지 찾는 함수
    public void findAllLayovers(TMapPoint start, TMapPoint end){
        List<NearLocation> rangePoints = getRangePoints(start.getLatitude(), start.getLongitude(),
                end.getLatitude(), end.getLongitude());

        Log.d("size", rangePoints.size()+"");

        TMapData tmapdata = new TMapData();
        final ArrayList<TMapPoint> passList = new ArrayList<>();

        for(rotations=0;rotations<rangePoints.size();rotations++) {
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start, end,
                    passList, 10, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            rangePoints.get(rotations).setDistance(tMapPolyLine.getDistance());

                            if(rotations==rangePoints.size()){

                            }
                        }
                    });
        }
    }

    //지정 좌표 사이 좌표값들 가져오는 함수
    public List<NearLocation> getRangePoints(double s_latitude, double s_longitude, double e_latitude, double e_longitude){
        if(e_latitude < s_latitude){
            double tmp = e_latitude;
            e_latitude = s_latitude;
            s_latitude = tmp;
        }

        if(e_longitude < s_longitude){
            double tmp = e_longitude;
            e_longitude = s_longitude;
            s_longitude = tmp;
        }

        List<NearLocation> tmpList = new LinkedList<>();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select latitude, longitude from tb_lamp where "+s_latitude+" <= latitude and latitude <= "+e_latitude+
                " and "+s_longitude+" <= longitude and longitude <= "+e_longitude+" order by latitude, longitude;", null);

        Log.d("size", cursor.getCount()+"");

        int i=0;
        while(cursor.moveToNext()){
            TMapPoint point = new TMapPoint(Double.parseDouble(cursor.getString(0)), Double.parseDouble(cursor.getString(1)));
            NearLocation nearLocation = new NearLocation(point);
            Log.d("near loc", nearLocation.getPoint().getLatitude()+", "+
                    nearLocation.getPoint().getLongitude());
            tmpList.add(nearLocation);
        }
        db.close();
        return tmpList; // 좌표들 모아놓은 리스트 리턴
    }
}