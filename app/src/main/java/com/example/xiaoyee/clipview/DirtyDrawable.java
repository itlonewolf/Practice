package com.example.xiaoyee.clipview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;

/**
 * Created by xiaoyee on 16/6/29.
 * 自定义 drawable ,试验 clipRect 和 quickReject 和 dirtyBounds
 */
public class DirtyDrawable extends Drawable {
    private Context mContext;

    private DirtyBean mDirtyBean;

    private final int[] mStateSetOn  = new int[]{android.R.attr.state_selected};
    private final int[] mStateSetOff = new int[]{android.R.attr.state_empty};

    private StateListDrawable switchDrawable;

    private final Rect mDirtyBounds = new Rect();
    private final Rect mDrawingBounds = new Rect();

    private final Rect  switchBounds = new Rect();;
    private float switchPicHeight;
    private float switchPicWidth;

    private int[] mStateSetCurrent;

    String infoToShow = "DIRTY DRAWABLE";
    TextPaint mTextPaint;
    private int mTextSize;
    
    //0000 0010
    private byte REFRESH_ALL = 0X01 << 1;
    //0000 0100
    private byte REFRESH_SWITCH = 0X01 << 2;
    
    private byte refreshType = REFRESH_ALL;
    
    private boolean isRefreshAll(){
        return (refreshType & REFRESH_ALL) != 0;
    }

    DirtyDrawable(Context context){
        this.mContext = context;
        mDirtyBean = new DirtyBean();
        initRes();
    }

    private void initRes() {

        switchDrawable = (StateListDrawable) mContext.getResources().getDrawable(R.drawable.switch_selector);
        switchPicHeight = switchDrawable.getMinimumHeight();
        switchPicWidth = switchDrawable.getMinimumWidth();

        mTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.text);
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLUE);

    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getDirtyBounds();
    
        canvas.save();
    
        canvas.clipRect(bounds);
        canvas.drawText(infoToShow, 300, 400, mTextPaint);
        if (!isRefreshAll()) {
            canvas.drawRect(bounds, mTextPaint);
        }
        drawSwitchDrawable(canvas);
        
        
        canvas.restore();
    }


    public boolean isSwitchTaped(float x, float y) {
        return switchBounds.contains((int) x, (int) y);
    }

    public void toggleSwitch() {
        final boolean lastPushable = mDirtyBean.isPushable();
        mDirtyBean.setPushable(!lastPushable);
        mDrawingBounds.set(switchBounds);
        refreshType = REFRESH_SWITCH;
        invalidateSelf();
    }

    private void drawSwitchDrawable(Canvas canvas) {
        if (mDirtyBean.isPushable()) {
            mStateSetCurrent = mStateSetOn;
        } else {
            mStateSetCurrent = mStateSetOff;
        }
        switchDrawable.setState(mStateSetCurrent);
        final Rect canvasBounds = canvas.getClipBounds();
        switchBounds.right = canvasBounds.right;
        switchBounds.left = (int) (switchBounds.right - switchPicWidth);
        switchBounds.top = canvasBounds.top ;
        switchBounds.bottom = (int) (switchBounds.top + switchPicHeight);
        switchDrawable.setBounds(switchBounds);
        switchDrawable.draw(canvas);
        refreshType = REFRESH_ALL;
    }

    @Override
    public Rect getDirtyBounds() {
        if (!isRefreshAll()) {
            final Rect drawingBounds = mDrawingBounds;
            final Rect dirtyBounds = mDirtyBounds;
            dirtyBounds.set(drawingBounds);
            drawingBounds.setEmpty();
            return dirtyBounds;
        } else {
            return getBounds();
        }
    }
    
    private void clearDirtyBounds(){
        mDirtyBounds.setEmpty();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
