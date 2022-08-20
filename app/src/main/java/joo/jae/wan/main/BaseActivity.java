package joo.jae.wan.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import joo.jae.wan.R;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    FirebaseFirestore firestoreDatabase = FirebaseFirestore.getInstance(); // 가로등 고장 신고 접수 데이터 보관할 데이터베이스

    private EditText latitude = null; // 고장 가로등 신고 접수 시 사용자가 입력하는 위도 정보 추출
    private EditText longitude = null; // 고장 가로등 신고 접수 시 사용자가 입력하는 경도 정보 추출

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_map) {
                startActivity(new Intent(this, MapActivity.class));
                finish();
            }
            else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
            }
            else if (itemId == R.id.navigation_streetlamp) {
                Log.d("report", "report");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View customDialogView = inflater.inflate(R.layout.complain_dialog, null);

                latitude = (EditText)customDialogView.findViewById(R.id.xmap);
                longitude = (EditText)customDialogView.findViewById(R.id.ymap);

                builder.setView(customDialogView).setTitle("가로등 고장 접수")
                        .setMessage("고장 접수할 가로등 위치 정보를 입력해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("latitude", latitude.getText()+"");
                                Log.d("longitude", longitude.getText()+"");

                                Map<String, String> map = new HashMap<>();
                                map.put(latitude.getText().toString(), longitude.getText().toString()); // 입력 받은 위치 정보 map 자료구조에 저장

                                firestoreDatabase.collection("alert").document("location") // 정해놓은 firebase 위치에 저장
                                        .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Success", "success");
                                                Toast.makeText(BaseActivity.this, "정보가 전송되었습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Failure", "Failure");
                                                Toast.makeText(BaseActivity.this, "정보 전송에 실패했습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("취소", null);
                builder.create().show();
            }
            else if (itemId == R.id.navigation_police) {
                startActivity(new Intent(this, PoliceActivity.class));
                finish();
            }
            else if (itemId == R.id.navigation_report) {
                startActivity(new Intent(this, ReportActivity.class));
                finish();
            }
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

}