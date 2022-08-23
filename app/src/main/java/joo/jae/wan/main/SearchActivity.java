package joo.jae.wan.main;

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
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import joo.jae.wan.R;

public class SearchActivity extends BaseActivity{

    List<TMapPoint> resultRoutes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    //지정 좌표 사이 좌표값들 가져오는 함수
    public LinkedList<TMapPoint> getRangePoints(double s_latitude, double s_longitude, double e_latitude, double e_longitude){
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

        LinkedList<TMapPoint> tmpList = new LinkedList<>();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select latitude, longitude from tb_lamp where "+s_latitude+" <= latitude and latitude <= "+e_latitude+
                " and "+s_longitude+" <= longitude and longitude <= "+e_longitude+" order by latitude, longitude;", null);

        Log.d("size", cursor.getCount()+"");

        int i=0;
        while(cursor.moveToNext()){
            TMapPoint point = new TMapPoint(Double.parseDouble(cursor.getString(0)), Double.parseDouble(cursor.getString(1)));
            tmpList.add(point);
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
    
}
