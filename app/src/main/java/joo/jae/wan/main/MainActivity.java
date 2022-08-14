package joo.jae.wan.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

                int itemId = item.getItemId();
                if (itemId == R.id.map) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_map).commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.police) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_police).commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.report) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_report).commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_search).commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.streetlamp) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_streetlamp).commitAllowingStateLoss();
                    return true;
                }
                return true;
            }
        });
    }
}