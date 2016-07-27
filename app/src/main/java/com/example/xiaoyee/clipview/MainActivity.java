package com.example.xiaoyee.clipview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

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
    
    ShakeController mShakeController;

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
    
        mShakeController = new ShakeController(this);
        mShakeController.setOnShakedListener(new ShakeController.OnShakedListener() {
            @Override
            public void onShake() {
                Toast.makeText(MainActivity.this, "yaoyiyao", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        mShakeController.startWatchShake();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mShakeController.stopWatchShake();
    }
}
