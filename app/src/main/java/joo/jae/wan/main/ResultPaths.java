package joo.jae.wan.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

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

public class ResultPaths extends AppCompatActivity {

    private TextView startArea = null;
    private TextView endArea = null;

    private RadioGroup radioGroup = null;

    // 최단 경로
    private LinearLayout shortestPathLayout = null;
    private LinearLayout shortestPathMap = null;

    // 밝은 경로
    private LinearLayout lightPathLayout = null;
    private LinearLayout lightPathMap = null;

    // 최단경로
    private TextView spendTime1 = null;
    private TextView distance1 = null;

    // 밝은경로
    private TextView spendTime2 = null;
    private TextView distance2 = null;
    private TextView numOfLamps2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_paths);

        startArea = (TextView) findViewById(R.id.start_area);
        endArea = (TextView) findViewById(R.id.arrive_area);

        // 출발지, 도착지 설정
        String startLocation = null;
        String endLocation = null;

        Intent intent_s = getIntent();
        startLocation= intent_s.getStringExtra("start");
        Intent intent_e = getIntent();
        endLocation= intent_e.getStringExtra("end");

        startArea.setText("출발지 : "+startLocation);
        endArea.setText("도착지 : "+endLocation);

        shortestPathLayout = (LinearLayout)findViewById(R.id.shortestpathLayout);
        shortestPathMap = (LinearLayout)findViewById(R.id.shortestpathMap);

        lightPathLayout = (LinearLayout)findViewById(R.id.lightpathLayout);
        lightPathMap = (LinearLayout)findViewById(R.id.lightpathMap);

        // 최단경로
        spendTime1 = (TextView) findViewById(R.id.spendTime1);
        distance1 = (TextView) findViewById(R.id.distance1);

        // 밝은경로
        spendTime2 = (TextView) findViewById(R.id.spendTime2);
        distance2 = (TextView) findViewById(R.id.distance2);
        numOfLamps2 = (TextView) findViewById(R.id.numOfLamp2);

        showShortestPath();
        showLightPath();

        radioGroup = (RadioGroup) findViewById(R.id.paths);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        radioGroup.check(R.id.shortpath);
    }

    // 여기다 최단 경로 알고리즘 코드 삽입
    private void showShortestPath(){
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1ee83da12e334595b10d8658f0816106");
        // 출발지로 지도 중심점 세팅해주세여
        shortestPathMap.addView(tMapView);
        int time = 0;
        spendTime1.setText("소요시간 : "+time);
        double distance = 0;
        distance1.setText("거리 : "+distance);
    }

    // 여기다 밝은 경로 알고리즘 코드 삽입
    private void showLightPath(){
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1ee83da12e334595b10d8658f0816106");
        // 출발지로 지도 중심점 세팅해주세여
        lightPathMap.addView(tMapView);

        // Test 용 (나중에 검색 결과를 startLocation, endLocation 으로 받으면 그 장소의 좌표를 넣어주면 됨)
        TMapPoint startPoint = new TMapPoint(37.494380, 126.960485);
        TMapPoint endPoint = new TMapPoint(37.497404, 126.952077);

        // 마커 bitmap
        Bitmap bitmap_start = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.start_point);
        bitmap_start = Bitmap.createScaledBitmap(bitmap_start, 40,50,false);
        Bitmap bitmap_end = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.end_point);
        bitmap_end = Bitmap.createScaledBitmap(bitmap_end, 40,50,false);
        Bitmap bitmap_lamp = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.streetlamp);
        bitmap_lamp = Bitmap.createScaledBitmap(bitmap_lamp, 50,50,false);

        // startPoint 마커 찍기
        TMapMarkerItem start_mark = new TMapMarkerItem();
        start_mark.setTMapPoint(startPoint);
        start_mark.setCanShowCallout(true); // 마커 표시 여부
        start_mark.setCalloutTitle("출발"); // 마커 클릭 시 풍선뷰
        start_mark.setIcon(bitmap_start);
        start_mark.setVisible(TMapMarkerItem.VISIBLE);
        tMapView.addMarkerItem("start", start_mark);
        // endPoint 마커 찍기
        TMapMarkerItem end_mark = new TMapMarkerItem();
        end_mark.setTMapPoint(endPoint);
        end_mark.setCanShowCallout(true); // 마커 표시 여부
        end_mark.setCalloutTitle("도착"); // 마커 클릭 시 풍선뷰
        end_mark.setIcon(bitmap_end);
        end_mark.setVisible(TMapMarkerItem.VISIBLE);
        tMapView.addMarkerItem("end", end_mark);

        // 경유 가로등 포인트 구하기
        TMapPoint[] lamp_arr = new TMapPoint[100];      // 경유지 모아놓는 배열
        int lamp_num=0;     // 경유 가로등 개수
        lamp_arr[0] = getLightPath(startPoint, endPoint, tMapView);
        for(int i=1; i<100; i++){
            lamp_arr[i] = getLightPath(lamp_arr[i-1], endPoint, tMapView);
            if( (lamp_arr[i].getLatitude()==0.0)&&(lamp_arr[i].getLongitude()==0.0) ) {
                // startPoint 와 endPoint 사이에 가로등이 없는 경우, 반복 종료
                lamp_num = i;
                break;
            }
        }
        Log.i("lamp_num",""+lamp_num);

        // 경유 가로등 마커 찍기
        for(int i=0; i<lamp_num; i++){
            TMapMarkerItem lamp = new TMapMarkerItem();
            lamp.setTMapPoint(lamp_arr[i]);
            lamp.setPosition(0.5f, 1.0f);
            lamp.setVisible(TMapMarkerItem.VISIBLE);
            lamp.setIcon(bitmap_lamp);
            lamp.setCanShowCallout(true);
            lamp.setCalloutTitle((i+1)+"번 가로등");
            tMapView.addMarkerItem("lamp "+i, lamp);
        }

        double total_sec = light_distance/1.35;     // 성인 평균 보행속도 1.35m/s
        int min = (int)total_sec/60;
        double sec = total_sec - min*60;
        spendTime2.setText("소요시간 : "+min+"분 "+(int)sec+"초");
        distance2.setText("거리 : "+String.format("%.2f",light_distance));
        numOfLamps2.setText("가로등 개수 : "+lamp_num);
    }

    int line_id = 1;            // 밝은 경로 line ID
    double light_distance = 0;  // 밝은 경로 총 보행 거리
    // startPoint 와 endPoint 사이의 최적의 가로등을 선택하고, 최적의 가로등까지 경로를 그린 후, 선택한 가로등 좌표 리턴
    public TMapPoint getLightPath(TMapPoint startPoint, TMapPoint endPoint, TMapView tMapView){
        List<NearLocation> rangePoints = getRangePoints(startPoint.getLatitude(), startPoint.getLongitude(),
                endPoint.getLatitude(), endPoint.getLongitude());
        // startPoint, endPoint 사이의 모든 좌표 리스트 rangePoints

        // rangePoints 정렬
        for(int i=0;i<rangePoints.size();i++){
            TMapPoint destination = rangePoints.get(i).getPoint();
            rangePoints.get(i).setDistance(getDistance(startPoint, destination));
        }
        Collections.sort(rangePoints);

        // rangePoints 중복 좌표 제거
        for(int i=0; i<rangePoints.size()-1; i++){
            if( (rangePoints.get(i).getPoint().getLatitude() == rangePoints.get(i+1).getPoint().getLatitude())
                    && (rangePoints.get(i).getPoint().getLongitude() == rangePoints.get(i+1).getPoint().getLongitude()) ) {
                rangePoints.remove(i + 1);
                i--;
            }
        }
        Log.d("func1 size", rangePoints.size()+"");

        TMapData tmapdata = new TMapData();
        if(rangePoints.size()==0) {     // startPoint, endPoint 사이에 가로등이 없는 경우
            // startPoint 에서 endPoint 까지 보행 경로 그리기
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
                        tMapPolyLine.setLineColor(Color.GREEN);
                        tMapPolyLine.setOutLineColor(Color.GREEN);
                        tMapPolyLine.setLineAlpha(150);
                        tMapPolyLine.setOutLineAlpha(150);
                        tMapPolyLine.setLineWidth(15);
                        tMapPolyLine.setOutLineAlpha(15);
                        tMapView.addTMapPolyLine("line "+ (line_id++), tMapPolyLine);
                        light_distance = light_distance + tMapPolyLine.getDistance();   // 밝은 경로 총 보행 거리에 이번에 그린 경로의 거리 더하기
//                        Log.i("line " + (line_id-1) + "'s distance ",""+tMapPolyLine.getDistance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new TMapPoint(0.0, 0.0);
        }

        int minIndex = getBestLamp(startPoint, endPoint, rangePoints);   // 최적의 가로등 인덱스

        // startPoint 에서 선택한 가로등 까지 보행 경로 그리기
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, rangePoints.get(minIndex).getPoint());
                    tMapPolyLine.setLineColor(Color.GREEN);
                    tMapPolyLine.setOutLineColor(Color.GREEN);
                    tMapPolyLine.setLineAlpha(150);
                    tMapPolyLine.setOutLineAlpha(150);
                    tMapPolyLine.setLineWidth(15);
                    tMapPolyLine.setOutLineAlpha(15);
                    tMapView.addTMapPolyLine("line "+ (line_id++), tMapPolyLine);
                    light_distance = light_distance + tMapPolyLine.getDistance();   // 밝은 경로 총 보행 거리에 이번에 그린 경로의 거리 더하기
//                    Log.i("line " + (line_id-1) + "'s distance ",""+tMapPolyLine.getDistance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rangePoints.get(minIndex).getPoint();   // 다음 시작지점이 될 좌표 (선택한 최적의 가로등)
    }

    // rangePoints 에 있는 가로등 중 startPoint 와 가장 가까운 가로등 3개 선택 (0, 1, 2)
    // 가로등 3개 중 어느 가로등을 경유해서 endPoint 까지 가야 가장 짧게 걸리는지 비교 후 최적의 가로등 인덱스 리턴
    public int getBestLamp(TMapPoint startPoint, TMapPoint endPoint, List<NearLocation> rangePoints) {
        TMapData tmapdata = new TMapData();
        int num = rangePoints.size();
        if(num >= 3)    // rangePoints 에 있는 가로등이 3개 이상인 경우 3개만 비교
            num = 3;

        final double[] distance = new double[num];      // 가로등을 경유해서 도착하는데 걸리는 총 거리
        if(num >= 1) {
//            Log.i("","11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    try {
                        ArrayList<TMapPoint> passList = new ArrayList<>();
                        passList.add(rangePoints.get(0).getPoint());
                        // passList에 rangePoints의 좌표 하나 넣기 (0)
//                        Log.i(0 + "'s passList", "" + passList);
                        TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 10);
                        distance[0] = tMapPolyLine.getDistance();
//                        Log.i(0 + "'s distance >> ", "" + tMapPolyLine.getDistance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(num >= 2) {
//            Log.i("","22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    try {
                        ArrayList<TMapPoint> passList = new ArrayList<>();
                        passList.add(rangePoints.get(1).getPoint());
                        // passList에 rangePoints의 좌표 하나 넣기 (1)
//                        Log.i(1 + "'s passList", "" + passList);
                        TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 10);
                        distance[1] = tMapPolyLine.getDistance();
//                        Log.i(1 + "'s distance >> ", "" + tMapPolyLine.getDistance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread2.start();
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(num >= 3) {
//            Log.i("","33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
            Thread thread3 = new Thread() {
                @Override
                public void run() {
                    try {
                        ArrayList<TMapPoint> passList = new ArrayList<>();
                        passList.add(rangePoints.get(2).getPoint());
                        // passList에 rangePoints의 좌표 하나 넣기 (2)
//                        Log.i(2 + "'s passList", "" + passList);
                        TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 10);
                        distance[2] = tMapPolyLine.getDistance();
//                        Log.i(2 + "'s distance >> ", "" + tMapPolyLine.getDistance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread3.start();
            try {
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double min = 9999999.0;   // 최솟값
        int minIndex = 0;        // 최솟값의 인덱스
        for(int i=0; i<distance.length; i++){
            if(distance[i]==0.0)
                Log.i("Forbidden",i+"'s");
            else if(distance[i] < min){
                min = distance[i];
                minIndex = i;
            }
        }
//        Log.i("인덱스",""+minIndex);
//        Log.i("최솟값",""+min);

        return minIndex;        // 최적의 가로등 인덱스 리턴
    }

    private double getDistance(TMapPoint a, TMapPoint b){
        return Math.sqrt(Math.pow(a.getLatitude()-b.getLatitude(), 2)+Math.pow(a.getLongitude()-b.getLongitude(), 2));
    }

    //지정 좌표 사이 좌표값들 가져오는 함수
    private List<NearLocation> getRangePoints(double s_latitude, double s_longitude, double e_latitude, double e_longitude){
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
        Cursor cursor = db.rawQuery("select latitude, longitude from tb_lamp where "+s_latitude+" < latitude and latitude < "+e_latitude+
                " and "+s_longitude+" < longitude and longitude < "+e_longitude+";", null);

        //Cursor cursor = db.rawQuery("select latitude, longitude from tb_lamp", null);
        Log.d("size", cursor.getCount()+"");

        int i=0;
        while(cursor.moveToNext()){
            TMapPoint point = new TMapPoint(Double.parseDouble(cursor.getString(0)), Double.parseDouble(cursor.getString(1)));
            NearLocation nearLocation = new NearLocation(point);
//            Log.d("near loc", nearLocation.getPoint().getLatitude()+", "+ nearLocation.getPoint().getLongitude());
            tmpList.add(nearLocation);
        }

        db.close();
        return tmpList; // 좌표들 모아놓은 리스트 리턴
    }

    RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i==R.id.shortpath){
                shortestPathLayout.setVisibility(View.VISIBLE);
                lightPathLayout.setVisibility(View.GONE);
            }
            else if(i==R.id.lightpath){
                shortestPathLayout.setVisibility(View.GONE);
                lightPathLayout.setVisibility(View.VISIBLE);
            }
        }
    };
}