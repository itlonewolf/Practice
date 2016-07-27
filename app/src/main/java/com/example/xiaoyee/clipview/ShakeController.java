package com.example.xiaoyee.clipview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiaoyee on 16/7/28.
 * 摇一摇控制器
 */
public class ShakeController implements SensorEventListener {
    
    /**
     * 设当加速度的绝对值大于55时才算一次摇动
     */
    private static final int SHAKE_SLOP = 55;
    /**
     * 摇一摇两次之间的间隔至少为500ms
     */
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
    private Vibrator      mVibrator;
    //是否需要震动,默认开启震动
    private boolean mNeedVibrate = true;
    //上一次震动的时间戳;单位纳秒
    private long mLastTimeShake = 0;
    
    private OnShakedListener mOnShakedListener;
    
    
    public ShakeController(Context context, boolean needVibrate) {
        mNeedVibrate = needVibrate;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    public ShakeController(Context context) {
        this(context, true);
    }
    
    public void startWatchShake(){
        if (mSensorManager != null) {
            mSensorManager.registerListener(
                    this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
                                           );
        }
    }
    
    public void stopWatchShake(){
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }
    
    public void setOnShakedListener(OnShakedListener onShakedListener) {
        mOnShakedListener = onShakedListener;
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float   x      = values[0]; // x轴方向的重力加速度，向右为正
        float   y      = values[1]; // y轴方向的重力加速度，向前为正
        float   z      = values[2]; // z轴方向的重力加速度，向上为正
        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
        if (Math.abs(x) > SHAKE_SLOP || Math.abs(y) > SHAKE_SLOP || Math.abs(z) > SHAKE_SLOP) {
            //使用此方法降低调用次数
            if (isTimeSlot(event.timestamp)) {
                if (mNeedVibrate) {
                    mVibrator.vibrate(VIBRATOR_PATTERN, DONT_REPEAT);
                }
    
                if (mOnShakedListener != null) {
                    mOnShakedListener.onShake();
                }
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
    
    /**
     * 摇一摇监听器
     */
    public interface OnShakedListener{
        void onShake();
    }
}
