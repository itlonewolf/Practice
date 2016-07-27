package com.example.xiaoyee.clipview;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.xiaoyee.clipview.utils.Log;

public class ChangeOriActivity extends AppCompatActivity implements View.OnClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("on create");
        setContentView(R.layout.activity_change_ori);
        findViewById(R.id.btn2land).setOnClickListener(this);
        findViewById(R.id.btn2por).setOnClickListener(this);
        findViewById(R.id.btn2reverseLand).setOnClickListener(this);
        findViewById(R.id.btn2ReversePor).setOnClickListener(this);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        log("on configuration changed");
        log("new Config:" + newConfig.toString());
    }
    
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        log("on content changed");
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn2land:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.btn2por:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.btn2reverseLand:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case R.id.btn2ReversePor:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        log("onresume");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        log("on start");
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        log("onrestart");
    }
    
    
    
    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        log("on stop");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("on destroy");
    }
    
    private void log(String info){
        Log.d("ChangeOriActivity", info);
    }
}
