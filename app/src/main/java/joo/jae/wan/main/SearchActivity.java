package joo.jae.wan.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
   // String search_s_r = null;
   // String search_e_r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        if(editText_s.length() == 0){
            String search_e_r=null;
            Intent intent = getIntent();
            search_e_r= intent.getStringExtra("address");
            //메인에서 바로 넘어갈때
            if(search_e_r!=null) {

                Log.d("aaaaaaaaaaaaa", intent.getStringExtra("address"));
                Log.d("eeeeeeeeeeeeeeeeeeeeeeeeeeeeee", editText_s.getText() + "");
                EditText editText = findViewById(R.id.edt_start);
                editText.setText("현재위치");
                EditText s = (EditText) findViewById(R.id.edt_end);
                s.setText(search_e_r);

            }/*
            else{
                // 길찾기 누르고 둘다 비었을때
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

            }*/
        }
        else if(editText_e.length()==0){
            String search_s_r=null;
            Intent intent = getIntent();
            search_s_r= intent.getStringExtra("address");
            //길찾기 출발
            if(search_s_r!=null) {

                EditText e= (EditText) findViewById(R.id.edt_start);
                e.setText(search_s_r);
            }
            /*
            editText_e.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            EditText edit =(EditText) findViewById(R.id.edt_end);
                            search_e = edit.getText().toString();
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
            */


        }
        else if(editText_s.length()!=0 && editText_e.length()!=0){
            String search_e_r=null;
            Intent intent = getIntent();
            search_e_r= intent.getStringExtra("address");
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