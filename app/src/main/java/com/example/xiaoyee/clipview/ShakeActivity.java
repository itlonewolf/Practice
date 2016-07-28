package com.example.xiaoyee.clipview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.xiaoyee.clipview.utils.Log;

public class ShakeActivity extends AppCompatActivity {
    
    private ShakeController mShakeController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
    
        mShakeController = new ShakeController(this);
        mShakeController.setOnShakedListener(new ShakeController.OnShakedListener() {
            @Override
            public void onShake() {
                Toast.makeText(ShakeActivity.this, "摇一摇", Toast.LENGTH_SHORT).show();
            }
        });
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
