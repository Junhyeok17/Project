package joo.jae.wan.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    int check;
   // String search_s_r = null;
   // String search_e_r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //어디에서 오는 지 채크
        Intent intent_c = getIntent();
        check=intent_c.getIntExtra("check", 0);

      /*  Intent intent = getIntent();
        search_e_r= intent.getStringExtra("address");
        Log.d("aaaaaaaaaaaaa", intent.getStringExtra("address"));*/
        //검색 넘기기


        //   Intent intent_s =getIntent();
//        search_s_r= intent_s.getStringExtra("start");

        //      Intent intent_e =getIntent();
        //    search_e_r= intent_e.getStringExtra("end");

        EditText editText_e = (EditText) findViewById(R.id.edt_end);

        EditText editText_s = (EditText) findViewById(R.id.edt_start);
        if(check ==1 ) {
            if (editText_s.length() == 0) {
                String search_e_r = null;
                Intent intent = getIntent();
                search_e_r = intent.getStringExtra("address");
                //메인에서 바로 넘어갈때
                if (search_e_r != null) {

                    Log.d("aaaaaaaaaaaaa", intent.getStringExtra("address"));
                    Log.d("eeeeeeeeeeeeeeeeeeeeeeeeeeeeee", editText_s.getText() + "");
                    EditText editText = findViewById(R.id.edt_start);
                    editText.setText("현재위치");
                    EditText s = (EditText) findViewById(R.id.edt_end);
                    s.setText(search_e_r);
                    String end = search_e_r;
                    Button button = findViewById(R.id.btn_find);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setClass(SearchActivity.this, ResultPaths.class);
                            intent.putExtra("start","현재위치" );
                            intent.putExtra("end",end);
                            startActivity(intent);
                        }
                    });

                }
            }
        }
        else{
            String search_s_r=null;
            Intent intent = getIntent();
            search_s_r= intent.getStringExtra("address");
            //길찾기 출발
            if(search_s_r!=null) {

                EditText e= (EditText) findViewById(R.id.edt_start);
                e.setText(search_s_r);
            }

            String search_e_r=null;
            Intent intent_e = getIntent();
            search_e_r= intent_e.getStringExtra("address");
            //길찾기 출발
            if(search_e_r!=null) {

                EditText e= (EditText) findViewById(R.id.edt_end);
                e.setText(search_e_r);
            }
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



        navigationView=(BottomNavigationView)findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }
}