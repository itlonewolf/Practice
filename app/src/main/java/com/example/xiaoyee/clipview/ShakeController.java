package com.example.xiaoyee.clipview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaoyee on 16/7/28.
 * 摇一摇控制器
 * <p>
 *     代码主体参考http://blog.csdn.net/catoop/article/details/8051835
 *     看到http://104zz.iteye.com/blog/1694738对应的文章时,想着应该把此功能做一个封装
 * </p>
 *
 */
public class ShakeController implements SensorEventListener {
    
    public interface Amplitude{
    
        /**
         * 对于过山车而言,得很大的摇动才算事儿吧
         */
        int ROLLER_COASTER = 100;
        int SUV            = 55;
        int BIKE           = 38;
        /**
         * 很小的摇动对于轮椅来讲就是需要注意的了
         */
        int WHEELCHAIR     = 18;
    }
    
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
    //设当绝对值大于多少时才算一次摇动
    private int mShakeAmplitude = Amplitude.SUV;
    
    private OnShakedListener mOnShakedListener;
    
    /**
     * 设置摇动幅度为多少时,才算是摇动
     */
    public void setShakeAmplitude(@AmplitudeAnno int shakeAmplitude) {
        mShakeAmplitude = shakeAmplitude;
    }
    
    /**
     *
     * @param needVibrate 是否需要开启震动
     */
    public ShakeController(Context context, boolean needVibrate) {
        mNeedVibrate = needVibrate;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    /**
     * 默认开启震动
     */
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
        //验证传感器
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float   x      = values[0]; // x轴方向的重力加速度，向右为正
            float   y      = values[1]; // y轴方向的重力加速度，向前为正
            float   z      = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            if (Math.abs(x) > mShakeAmplitude || Math.abs(y) > mShakeAmplitude || Math.abs(z) > mShakeAmplitude) {
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
        //暂时不关心此更改
    }
    
    /**
     * 摇一摇监听器
     */
    public interface OnShakedListener{
        void onShake();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Amplitude.WHEELCHAIR, Amplitude.BIKE, Amplitude.SUV, Amplitude.ROLLER_COASTER})
    private @interface AmplitudeAnno{}
}
