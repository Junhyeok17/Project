package joo.jae.wan.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import joo.jae.wan.R;

public class MainActivity extends AppCompatActivity {

    // 바텀 네비게이션
    BottomNavigationView bottomNavigationView;

    private String TAG = "메인";

    // 프래그먼트 변수
    Fragment fragment_map;
    Fragment fragment_police;
    Fragment fragment_report;
    Fragment fragment_search;
    Fragment fragment_streetlamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 생성
        fragment_map = new FragMap();
        fragment_police = new FragPolice();
        fragment_report = new FragReport();
        fragment_search = new FragSearch();
        fragment_streetlamp = new FragStreetlamp();

        // 바텀 내비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_map).commitAllowingStateLoss();

        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()) {
                    case R.id.map:
                        Log.i(TAG, "map 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_map).commitAllowingStateLoss();
                        return true;
                    case R.id.police:
                        Log.i(TAG, "police 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_police).commitAllowingStateLoss();
                        return true;
                    case R.id.report:
                        Log.i(TAG, "report 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_report).commitAllowingStateLoss();
                        return true;
                    case R.id.search:
                        Log.i(TAG, "search 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_search).commitAllowingStateLoss();
                        return true;
                    case R.id.streetlamp:
                        Log.i(TAG, "streetlamp 들어옴");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_streetlamp).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
    }
}