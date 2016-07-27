package com.example.xiaoyee.clipview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.example.xiaoyee.clipview.utils.Log;

/**
 * TODO: document your custom view class.
 */
public class DirtyView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int   mExampleColor     = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;
    
    private TextPaint mTextPaint;
    private float     mTextWidth;
    private float     mTextHeight;
    
    private DirtyBean mDirtyBean = new DirtyBean();;
    
    private final int[] mStateSetOn  = new int[]{android.R.attr.state_selected};
    private final int[] mStateSetOff = new int[]{android.R.attr.state_empty};
    
    private StateListDrawable switchDrawable;
    private final Rect mDirtyBounds   = new Rect();
    private final Rect mDrawingBounds = new Rect();
    
    private final Rect  switchBounds = new Rect();;
    private float switchPicHeight;
    private float switchPicWidth;
    
    private int[] mStateSetCurrent;
    //0000 0010
    private static final byte REFRESH_ALL = 0X01 << 1;
    //0000 0100
    private static final byte REFRESH_SWITCH = 0X01 << 2;
    
    private byte refreshType = REFRESH_ALL;
    
    private boolean isRefreshAll(){
        return (refreshType & REFRESH_ALL) != 0;
    }
    
    GestureDetector mGestureDetector;
    GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isSwitchTaped(e.getX(), e.getY())) {
                    toggleSwitch();
                    return true;
                }
            return false;
        }
    };
    
    
    public DirtyView(Context context) {
        super(context);
        init(null, 0);
    }
    
    public DirtyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        init(attrs, 0);
    }
    
    public DirtyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        init(attrs, defStyle);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    
    private void init(AttributeSet attrs, int defStyle) {
    
        mGestureDetector = new GestureDetector(mSimpleOnGestureListener);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DirtyView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.DirtyView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.DirtyView_exampleColor,
                mExampleColor
                                  );
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.DirtyView_exampleDimension,
                mExampleDimension
                                          );

        if (a.hasValue(R.styleable.DirtyView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.DirtyView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();
//
//        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
//
//        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        switchDrawable = (StateListDrawable)getResources().getDrawable(R.drawable.switch_selector);
        switchPicHeight = switchDrawable.getMinimumHeight();
        switchPicWidth = switchDrawable.getMinimumWidth();
    }
    
    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    
        drawSwitchDrawable(canvas);
        
//        canvas.drawRect(100,100,1000,1000, mTextPaint);
        
//        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft   = getPaddingLeft();
        int paddingTop    = getPaddingTop();
        int paddingRight  = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth  = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the example drawable on top of the text.
        imgBounds.set(paddingLeft, paddingTop,
                      paddingLeft + contentWidth / 2, paddingTop + contentHeight / 2
                     );
//        if (mExampleDrawable != null) {
//            if (!canvas.quickReject(new RectF(imgBounds), Canvas.EdgeType.AA)) {
//                log("在范围内");
//                mExampleDrawable.setBounds(imgBounds);
//                mExampleDrawable.draw(canvas);
//            } else {
//                log("不在范围内");
//            }
//        }
    
        mExampleDrawable.setBounds(imgBounds);
        mExampleDrawable.draw(canvas);
    }
    
    private final Rect imgBounds = new Rect();
    
    
    public boolean isSwitchTaped(float x, float y) {
        return switchBounds.contains((int) x, (int) y);
    }
    
    public void toggleSwitch() {
        final boolean lastPushable = mDirtyBean.isPushable();
        mDirtyBean.setPushable(!lastPushable);
        mDrawingBounds.set(switchBounds);
        log("toggleSwitch:" + switchBounds.toShortString());
        refreshType = REFRESH_SWITCH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setClipBounds(switchBounds);
        }
        invalidate();
    }
    
    private void drawSwitchDrawable(Canvas canvas) {
        if (mDirtyBean.isPushable()) {
            mStateSetCurrent = mStateSetOn;
        } else {
            mStateSetCurrent = mStateSetOff;
        }
        switchDrawable.setState(mStateSetCurrent);
        final Rect canvasBounds = canvas.getClipBounds();
        switchBounds.right = canvasBounds.right - 100;
        switchBounds.left = (int) (switchBounds.right - switchPicWidth);
        switchBounds.top = canvasBounds.top + 100;
        switchBounds.bottom = (int) (switchBounds.top + switchPicHeight);
        switchDrawable.setBounds(switchBounds);
        switchDrawable.draw(canvas);
        
        log("drawSwitchDrawable:" + switchBounds.toShortString());
        
        refreshType = REFRESH_ALL;
    }
    
    
    private void log(String infoToShow) {
        Log.d("DirtyView", infoToShow);
    }
    
}
