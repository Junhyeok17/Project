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
            navigationView=(BottomNavigationView)findViewById(R.id.navigation);
            navigationView.setOnNavigationItemSelectedListener(this);
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

        navigationView=(BottomNavigationView)findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }
}