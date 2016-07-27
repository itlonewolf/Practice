package com.example.xiaoyee.clipview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    DirtyDrawable backgroundDrawable;
    GestureDetector mGestureDetector;
    GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (backgroundDrawable != null) {
                if (backgroundDrawable.isSwitchTaped(e.getX(), e.getY())) {
                    backgroundDrawable.toggleSwitch();
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DirtyView layout = (DirtyView) findViewById(R.id.layout);
        findViewById(R.id.btnChangeOri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChangeOriActivity.class));
            }
        });
        findViewById(R.id.btn2Shake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShakeActivity.class));
            }
        });

//        mGestureDetector = new GestureDetector(this, mSimpleOnGestureListener);
//        backgroundDrawable = new DirtyDrawable(this);
//        layout.setBackgroundDrawable(backgroundDrawable);
//        layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
