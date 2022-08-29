package joo.jae.wan.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

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

    ArrayList<TMapPoint> midList = new ArrayList<>(); // 경유지 모아놓는 리스트

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
        int time = 0;
        spendTime2.setText("소요시간 : "+time);
        double distance = 0;
        distance2.setText("거리 : "+distance);

        numOfLamps2.setText("가로등 개수 : "+midList.size());
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
        Cursor cursor = db.rawQuery("select latitude, longitude from tb_lamp where "+s_latitude+" <= latitude and latitude <= "+e_latitude+
                " and "+s_longitude+" <= longitude and longitude <= "+e_longitude+" order by latitude, longitude;", null);

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