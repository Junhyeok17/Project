package joo.jae.wan.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import joo.jae.wan.R;
import joo.jae.wan.databinding.ActivityPoliceBinding;

public class PoliceActivity extends BaseActivity {
    private ActivityPoliceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getContentViewId());
        TMapView tMap = new TMapView(this);
        tMap.setSKTMapApiKey("l7xx1ee83da12e334595b10d8658f0816106");
        binding.tMapView.addView(tMap);

        TMapPoint tMapPointStart = new TMapPoint(37.5806, 126.9115);
        TMapPoint tMapPointEnd = new TMapPoint(37.582989, 126.912888);
        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart,
                tMapPointEnd, new ArrayList<>(), 10, polyLine -> {
                    polyLine.setLineColor(Color.BLUE);
                    polyLine.setLineWidth(20);
                    tMap.addTMapPath(polyLine);
                });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_police;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_police;
    }

}
