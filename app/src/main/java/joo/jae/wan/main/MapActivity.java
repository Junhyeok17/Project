package joo.jae.wan.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import joo.jae.wan.R;
import kotlin.jvm.internal.Intrinsics;

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "streetLamps", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "create table tb_lamp ("+
                "_id integer primary key autoincrement, "+
                "latitude text, "+
                "longitude text)";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

public class MapActivity extends BaseActivity implements TMapGpsManager.onLocationChangedCallback{

    @Override
    int getContentViewId() {
        return R.layout.activity_map;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_map;
    }

    List<InputStream> inputStreamList = null; // 가로등 정보 파일 스트림 저장하는 객체들 저장
    TMapView tMapView = null; // 티맵 보여주는 객체
    List<TMapMarkerItem> itemMarkerList = null; // 위의 파일 스트림에서 추출한 지도상 마커 정보 가진 객체들 저장

    TMapGpsManager tMapGps = null;
    //경찰서 보여줄 tmapdata
    TMapData tmapdata = new TMapData();
    private LocationManager locationManager = null;

    // asset 파일에서 가로등 위치 정보 엑셀 파읿 불러오는 함수
    private void getData(){
        AssetManager manager = getResources().getAssets(); // 가로등 정보 파일 위치
        itemMarkerList = new LinkedList<>();
        try {
            inputStreamList = new LinkedList<>();

            InputStream inputStream = manager.open("서울특별시_동작구_보안등정보_20220707.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강남구_보안등정보_20211213_1639811421924_1283906.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강서구_보안등정보_20220316_1647408019525_1329351.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_광진구_보안등정보_20220701.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_구로구_보안등정보_20220531.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_금천구_보안등정보_20220226_1646216898840_927050.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_동작구_보안등정보_20220707.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_성북구_보안등정보_20220601.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_영등포구_보안등정보_20220627.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_종로구_보안등정보_20220708.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_도봉구_보안등정보_20180515.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_서대문구_보안등정보_20201020_1603176968804_1347837.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_관악구_보안등정보_20201207_1607497441004_1627562.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_중랑구_보안등정보_20201209_1607491212600_1153686.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_송파구_보안등정보_20210225_1614748045897_1117425.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_양천구_보안등정보_20210624_1624599558550_558983.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_마포구_보안등정보_20210812_1628746918141_722041.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_은평구_보안등정보_20210802_1629283244150_1158010.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강동구_보안등정보_20210825_1629871644483_1161326.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강북구_보안등정보_20210831_1630470954649_1644180.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_노원구_보안등정보_20210916_1631839126541_743398.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_용산구_보안등정보_20210927_1632816216661_1247006.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_성동구_보안등정보_20210930_1632987402974_910893.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_중구_보안등정보_20211018_1634524178367_920561.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_서초구_보안등정보_20211020_1634718575449_1768192.csv");
            inputStreamList.add(inputStream);

            inputStream = manager.open("경기도_화성시_보안등정보_20190503.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_부천시_보안등정보_20190910.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_용인시_보안등정보_20190711.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_광명시_보안등정보_20201214_1608099205674_357504.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_안성시_보안등정보_20210315_1615815438485_2410370.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_연천군_보안등정보_20211108_1636351841051_789503.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_군포시_보안등정보_20210525_1621922260247_334032.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_시흥시_보안등정보_20210601_1623126234274_1341587.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_오산시_보안등정보_20210810_1628584109091_947417.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_여주시_보안등정보_20210721_1628746859811_1202685.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_동두천시_보안등정보_20210901_1629943179698_382386.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_성남시_보안등정보_20210831_1631332561052_1540397.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_가평군_보안등정보_20210824_1631593363709_1138166.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_안산시_보안등정보_20210831_1632359235177_1703202.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_김포시_보안등정보_20210929_1632959767818_1149942.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_고양시_보안등정보_20210927_1632975501220_1552009.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_광주시_보안등정보_20210913_1631871201546_2108140.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_의정부시_보안등정보_20210930_1633418176723_573249.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_이천시_보안등정보_20211206_1639102716929_1478675.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_수원시_보안등정보_20220527.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_의왕시_보안등정보_20220613.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_구리시_보안등정보_20220614.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_양주시_보안등정보_20220620.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_평택시_보안등정보_20220613.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_하남시_보안등정보_20220704.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_과천시_보안등정보_20220718.csv");
            inputStreamList.add(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // tmap api 키 설정하고 저장해놓은 마커 정보들 지도 상에 표시
    private void mapload(){
        tMapView = new TMapView(ct);
        tMapView.setSKTMapApiKey("l7xx1ee83da12e334595b10d8658f0816106");

        int total = 0;
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.streetlamp);
        bitmap = Bitmap.createScaledBitmap(bitmap, 50,50,false);
        int sum1 = 0, sum2 = 0; // 파일에서 불러오다보니까 엑셀 내에 파손된 데이터 있길래 그거 필터링하고 남은 좌표 개수 세려고. 무시 가능
        try{
            for(int rot=0;rot<inputStreamList.size();rot++) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamList.get(rot), Charset.forName("UTF-8")));
                String line = "";
                StringTokenizer st = null;
                for (int i = 0; (line = bufferedReader.readLine()) != null; i++) {
                    if (i == 0)
                        continue;
                    st = new StringTokenizer(line, ",");

                    String tmp;
                    st.nextToken();
                    st.nextToken();
                    st.nextToken();
                    st.nextToken();

                    String x = st.nextToken();
                    String y = st.nextToken();

                    if(x.length() < 10 || y.length() < 11)
                        continue;

                    boolean check = false;
                    for(int dx=0;dx<x.length();dx++) {
                        if ('0' <= x.charAt(dx) && x.charAt(dx) <= '9' || x.charAt(dx) == '.')
                            check = false;
                        else
                            check = true;

                        if (check)
                            break;
                    }

                    if(check)
                        continue;

                    for(int dx=0;dx<y.length();dx++) {
                        if ('0' <= y.charAt(dx) && y.charAt(dx) <= '9' || y.charAt(dx) == '.')
                            check = false;
                        else
                            check = true;

                        if(check)
                            break;
                    }

                    if(check)
                        continue;

                    double dx = Double.parseDouble(x.substring(0, x.length()-1));
                    double dy = Double.parseDouble(y.substring(0, y.length()-1));

                    TMapPoint point = new TMapPoint(dx, dy);
                    TMapMarkerItem item = new TMapMarkerItem();
                    item.setTMapPoint(point);
                    item.setCanShowCallout(true); // 마커 표시 여부

                    item.setIcon(bitmap);
                    item.setCalloutTitle(dx+", "+dy); // 마커 클릭 시 풍선뷰
                    item.setVisible(TMapMarkerItem.VISIBLE);

                    total++;
                    itemMarkerList.add(item);
                    tMapView.addMarkerItem(String.valueOf(i), item);
                }
            }
            Log.d("total", total+"");
            Log.d("sum", sum1+"+"+sum2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void policeMarker(){
        //검색결과표시할때 쓰고 이건 경찰서
        new Thread(new Runnable() {
            @Override
            public void run() {
                tmapdata.findAllPOI("지구대", new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        TMapMarkerItem markerItem1;
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());




                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_p);
                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가
                        }
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                tmapdata.findAllPOI("파출소", new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        TMapMarkerItem markerItem1;
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());




                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_p);
                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가
                        }
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                tmapdata.findAllPOI("경찰서", new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        TMapMarkerItem markerItem1;
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());




                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_p);
                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가
                        }
                    }
                });
            }
        }).start();
    }
    // flash
    Context ct;
    private boolean powerState;
    public final boolean getPowerState() {
        return this.powerState;
    }
    public final void setPowerState(boolean tmp_powerState) {
        this.powerState = tmp_powerState;
    }
    // flash

    String search = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getData();
        FrameLayout linearLayoutTmap = (FrameLayout)findViewById(R.id.linearLayoutTmap);
        ct = MapActivity.this;
        mapload();
      //  policeMarker();

        locationManager = (LocationManager)ct.getSystemService(LOCATION_SERVICE);

        // 위치 정보 권한 허가
        if(ContextCompat.checkSelfPermission(ct, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            displayMyLocation();
        }

        // 최초 실행 시 DB 생성을 위해 20분 정도 기다린 후 logcat에 end 로그 찍히면 사용할 것...
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper helper = new DBHelper(MapActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                Cursor cursor = db.rawQuery("select * from tb_lamp", null);

                if(cursor.getCount()==0) {
                    for (int i = 0; i < itemMarkerList.size(); i++) {
                        TMapPoint point = itemMarkerList.get(i).getTMapPoint();
                        double dx = point.getLatitude();
                        double dy = point.getLongitude();
                        db.execSQL("insert into tb_lamp (latitude, longitude) values (?, ?)",
                                new String[]{String.valueOf(dx), String.valueOf(dy)});
                    }
                }
                db.close();
                Log.d("end", "end");
            }
        }).start();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        linearLayoutTmap.addView( tMapView );

        // flash button control
        View tmp_btn = this.findViewById(R.id.map_imageButton);
        Intrinsics.checkNotNullExpressionValue(tmp_btn, "findViewById(R.id.imageButton)");
        final ImageButton btn = (ImageButton)tmp_btn;
        btn.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                if (MapActivity.this.getPowerState()) {
                    MapActivity.this.setPowerState(false);
                    btn.setImageResource(R.drawable.flashlight_off);
                    try {
                        MapActivity.this.controlFlash(MapActivity.this.getPowerState());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    MapActivity.this.setPowerState(true);
                    btn.setImageResource(R.drawable.flashlight_on);
                    try {
                        MapActivity.this.controlFlash(MapActivity.this.getPowerState());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
        // flash button control

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);


        //검색
        //검색 넘기기
        EditText editText = (EditText)findViewById(R.id.edt_search);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch(keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        search = editText.getText().toString();
                        if ( search.length() == 0 ) {
                            return false;
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(MapActivity.this, SearchResultActivity.class);
                            intent.putExtra("searchData", search);
                            intent.putExtra("check","check");
                            startActivity(intent);
                        }
                }
                return false;
            }
        });

        //경찰서
        TMapPoint point= new TMapPoint(37.448355, 126.99538666666668);
        tmapdata.findAroundNamePOI(point, "경찰서", 1, 99,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            Log.d("편의시설","POI Name: " + item.getPOIName() + "," + "Address: "
                                    + item.getPOIAddress().replace("null", ""));
                            TMapPoint tMapPoint1 = item.getPOIPoint();
                            // 마커 아이콘
                            TMapMarkerItem markerItem1;
                            markerItem1 = new TMapMarkerItem();
                            Context context = getBaseContext();
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
                            bitmap = Bitmap.createScaledBitmap(bitmap, 50,50,false);

                            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            markerItem1.setName(item.getPOIName().toString()); // 마커의 타이틀 지정
                            tMapView.addMarkerItem(item.getPOIName().toString(), markerItem1); // 지도에 마커 추가
                        }
                    }
                });
    }

    // flash control
    public final void controlFlash(boolean mode) throws CameraAccessException {
        Object tmp_cameraM = this.getSystemService(Context.CAMERA_SERVICE);
        if (tmp_cameraM == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.camera2.CameraManager");
        } else {
            CameraManager cameraM = (CameraManager)tmp_cameraM;
            String cameraListId = cameraM.getCameraIdList()[0];

            try {
                cameraM.setTorchMode(cameraListId, mode);
            } catch (Exception e) {
                Toast toast = Toast.makeText(this, "Camera Flash Error", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }
    // flash control

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100)
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                displayMyLocation();
    }

    private void displayMyLocation(){
        Criteria criteria = buildCritera();
        String provider = chooseProviderBy(criteria);

        Location location = getLocationBy(provider);
        showLocation(location);

        if(ContextCompat.checkSelfPermission(ct, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(provider, 1000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    showLocation(location);
                }
            });
        }
    }

    private Criteria buildCritera(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private String chooseProviderBy(Criteria criteria){
        return locationManager.getBestProvider(criteria, true);
    }

    private Location getLocationBy(String provider){
        Location location = null;
        if(ContextCompat.checkSelfPermission(ct, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            location = locationManager.getLastKnownLocation(provider);
        }
        return location;
    }

    private void showLocation(Location location){
        tMapGps = new TMapGpsManager(ct);
        tMapGps.OpenGps();
        TMapMarkerItem mymarker = new TMapMarkerItem();

        mymarker.setTMapPoint(new TMapPoint(location.getLatitude(),
                location.getLongitude()));
        Log.d("latitude", tMapGps.getLocation().getLatitude()+"");
        Log.d("longitude", tMapGps.getLocation().getLongitude()+"");
        Log.d("latitude", location.getLatitude()+"");
        Log.d("longitude", location.getLongitude()+"");
        mymarker.setCanShowCallout(true); // 마커 표시 여부
        mymarker.setVisible(TMapMarkerItem.VISIBLE);
        tMapView.addMarkerItem(String.valueOf(-1), mymarker);
    }
}
