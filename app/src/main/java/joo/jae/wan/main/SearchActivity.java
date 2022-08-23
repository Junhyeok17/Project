package joo.jae.wan.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import joo.jae.wan.R;

public class SearchActivity extends BaseActivity{


    @Override
    int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_search;
    }

    String search_s = null;
    String search_e = null;
    String search_s_r = null;
    String search_e_r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //검색 넘기기


        //   Intent intent_s =getIntent();
//        search_s_r= intent_s.getStringExtra("start");

        //      Intent intent_e =getIntent();
        //    search_e_r= intent_e.getStringExtra("end");


        EditText editText_s = (EditText) findViewById(R.id.edt_start);

        if (search_s_r != null) {
            editText_s.setText(search_s_r);
        }
        editText_s.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        search_s = editText_s.getText().toString();
                        if (search_s == null) {
                            return false;
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(SearchActivity.this, SearchResultActivity.class);
                            intent.putExtra("start", search_s);
                            startActivity(intent);
                        }
                }
                return false;
            }
        });
        EditText editText_e = (EditText) findViewById(R.id.edt_end);
        if (search_e_r != null) {
            editText_e.setText(search_e_r);
        }
        editText_e.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        search_e = editText_e.getText().toString();
                        if (search_e == null) {
                            return false;
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(SearchActivity.this, SearchResultActivity.class);
                            intent.putExtra("end", search_e);
                            startActivity(intent);
                        }
                }
                return false;
            }
        });


        if (search_s_r == null || search_e_r == null) {
            return;
        }
        else {
          /*
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, Route.class);
                intent.putExtra("end", search_e_r);
                intent.putExtra("start", search_s_r);
                startActivity(intent);
*/
        }
    }
}





/*package joo.jae.wan.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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


public class SearchActivity extends BaseActivity{

    List<TMapPoint> resultRoutes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    public double getDistance(TMapPoint start, TMapPoint end){
        return Math.sqrt(Math.pow(start.getLatitude()-end.getLatitude(), 2)+Math.pow(start.getLongitude()-end.getLongitude(), 2));
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
            tmpList.add(nearLocation);
        }
        db.close();
        return tmpList; // 좌표들 모아놓은 리스트 리턴
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_search;
    }
    
}*/
