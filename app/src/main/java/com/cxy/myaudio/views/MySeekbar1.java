package com.cxy.myaudio.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MySeekbar1 extends View {

    private int DEFAULT_WIDTH  = 100;
    private int DEFAULT_HEIGHT = 1;
    private Paint bgPaint, proPaint;
    private TextPaint tvPaint;
    private int mwidth, mhight;
    private float fontHeight, textHight;
    private int TEXTSIZE = 10;
    private float mDensity;

    public MySeekbar1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        init();
    }

    private void init() {
        proPaint = new Paint();
        proPaint.setColor(Color.BLACK);
        proPaint.setStrokeWidth(10 * mDensity);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = measureWidth(DEFAULT_WIDTH, widthMeasureSpec);
        int height = measureHeight(DEFAULT_HEIGHT, heightMeasureSpec);
        mwidth = width;
        mhight = height;
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int default_height, int heightMeasureSpec) {
        int h = default_height;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            h = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
            h = specSize;
        }

        return h;
    }

    private int measureWidth(int default_width, int widthMeasureSpec) {
        int w = default_width;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            w = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            w = specSize;
        }

        return w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        canvas.drawLine(mwidth/2, 0, mwidth/2, mhight,proPaint);
    }

}
