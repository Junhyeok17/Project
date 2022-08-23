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

        /*  테스트 코드라 지우려면 지우시길~~
        List<NearLocation> rangePoints = getRangePoints(37.441284172, 127.0045544,
                37.447532449, 126.00906);

        TMapPoint endPoint = new TMapPoint(37.432663968, 126.991953881);
        TMapPoint startPoint = new TMapPoint(37.441284172, 127.0045544);

        Log.d("size", rangePoints.size()+"");

        for(int i=0;i<rangePoints.size();i++){
            TMapPoint destination = rangePoints.get(i).getPoint();
            rangePoints.get(i).setDistance(getDistance(startPoint, destination));
        }

        Collections.sort(rangePoints);

        for(int i=0;i<rangePoints.size();i++)
            Log.d(rangePoints.get(i).getPoint().getLatitude()+"",
                    rangePoints.get(i).getPoint().getLongitude()+" 거리 : "+rangePoints.get(i).getDistance()
            +", index : "+i);
*/

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
    
}
