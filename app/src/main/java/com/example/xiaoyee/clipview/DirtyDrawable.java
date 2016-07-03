package com.example.xiaoyee.clipview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

/**
 * Created by xiaoyee on 16/6/29.
 * 自定义 drawable ,试验 clipRect 和 quickReject 和 dirtyBounds
 */
public class DirtyDrawable extends Drawable {
    Context mContext;

    Drawable mDrawableOn;
    Drawable mDrawableOff;

    TextPaint mTextPaint;
    Rect mDirtyBounds;
    Rect mSwitchBounds;

    String infoToShow;

    DirtyDrawable(Context context){
        this.mContext = context;
        init();
    }

    private void init() {
        mDrawableOff = mContext.getResources().getDrawable(R.mipmap.switch_off);
        mDrawableOn = mContext.getResources().getDrawable(R.mipmap.switch_on);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.getClipBounds();
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
