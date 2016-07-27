package com.example.xiaoyee.clipview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.xiaoyee.clipview.utils.Log;

import java.util.concurrent.TimeUnit;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {
    private static final int WHAT_SHAKED = 1988;
    //设当加速度的绝对值大于55时才算一次摇动
    private static final int SHAKE_SLOP = 55;
    //摇一摇两次之间的间隔至少为500ms
    private static final int SHAKE_TIME_SLOT = 500;
    //不重复震动
    private static final int DONT_REPEAT     = -1;
    //震动模式
    private static final long[] VIBRATOR_PATTERN = new long[]{
            //每两个数值为一个组合
            100, 300,//100为等待(停止)时间,300为震动时间
            200, 300 //200为等待(停止)时间,300为震动时间
    };
    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SHAKED:
                    Toast.makeText(ShakeActivity.this, "摇一摇。。。", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //单位是纳秒
    private long mLastTimeShake = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initSensor();
    }
    
    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
                                       );
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float   x      = values[0]; // x轴方向的重力加速度，向右为正
        float   y      = values[1]; // y轴方向的重力加速度，向前为正
        float   z      = values[2]; // z轴方向的重力加速度，向上为正
        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
        if (Math.abs(x) > SHAKE_SLOP || Math.abs(y) > SHAKE_SLOP || Math.abs(z) > SHAKE_SLOP) {
            log("x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            //使用此方法降低调用次数
            if (isTimeSlot(event.timestamp)) {
                mVibrator.vibrate(VIBRATOR_PATTERN, DONT_REPEAT);
                mHandler.sendEmptyMessage(WHAT_SHAKED);
            }
        }
    }
    
    /**
     * 间隔时间是否大于要求值;目前间隔时间为 {@link #SHAKE_TIME_SLOT 500ms}
     * @param currentTime   本次时间;单位为纳秒
     */
    private boolean isTimeSlot(long currentTime) {
        if (TimeUnit.NANOSECONDS.toMillis(currentTime - mLastTimeShake) > SHAKE_TIME_SLOT) {
            mLastTimeShake = currentTime;
            return true;
        }
        return false;
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }
    
    private void log(String info) {
        Log.d("ShakeActivity", info);
    }
    
}
