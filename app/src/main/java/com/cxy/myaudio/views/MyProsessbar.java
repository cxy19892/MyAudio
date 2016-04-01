package com.cxy.myaudio.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyProsessbar extends View {

    private Paint bgPaint, proPaint;
    private TextPaint tvPaint;
    private int mwidth, mhight;
    private float fontHeight, textHight;
    private int TEXTSIZE = 10;
    private float mDensity;
    private int COLOR_BG = 0xffedeced;
    private int COLOR_PRO= 0xffff6d3a;
    private int CLOOR_TV = 0xffff6d3a;
    private int proScore = 0;


    public MyProsessbar(Context context) {
        super(context);
    }

    public MyProsessbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        initPaints();
    }

    public void setValue(int value){
        if(value < 0){
            value = 0;
        }else if(value > 100){
            value = 100;
        }
        proScore = value;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mhight = bottom - top-getPaddingBottom()-getPaddingTop();
        mwidth = right - left - getPaddingLeft() - getPaddingRight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgPaint.setColor(COLOR_BG);
        proPaint.setColor(COLOR_PRO);
        tvPaint.setColor(CLOOR_TV);
        canvas.drawLine(0, mhight / 2, mwidth - Layout.getDesiredWidth("100%", tvPaint), mhight / 2, bgPaint);
        canvas.drawLine(proScore > 0 ? proPaint.getStrokeWidth()/2 : 0, mhight / 2, (mwidth - Layout.getDesiredWidth("100%", tvPaint))*proScore/100, mhight/2, proPaint);
        canvas.drawText(proScore+"%",mwidth, mhight / 2 + textHight, tvPaint);
    }

    private void initPaints(){
        bgPaint = new Paint();
        bgPaint.setColor(COLOR_BG);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeWidth(2 * mDensity);

        proPaint = new Paint(bgPaint);
        proPaint.setColor(COLOR_PRO);
        proPaint.setStrokeWidth(6 * mDensity);
        proPaint.setStrokeCap(Paint.Cap.ROUND);

        tvPaint = new TextPaint();
        // 去锯齿
        tvPaint.setAntiAlias(true);
        // 防抖动
        tvPaint.setDither(true);
        tvPaint.setTextAlign(Paint.Align.RIGHT);
        tvPaint.setTextSize(TEXTSIZE * mDensity);
        tvPaint.setColor(CLOOR_TV);
        Paint.FontMetrics mfontMetrics = tvPaint.getFontMetrics();
        fontHeight = mfontMetrics.bottom - mfontMetrics.top;
        textHight = mfontMetrics.descent;
    }







    public int getTEXTSIZE() {
        return TEXTSIZE;
    }

    public void setTEXTSIZE(int TEXTSIZE) {
        this.TEXTSIZE = TEXTSIZE;
    }

    public int getCOLOR_BG() {
        return COLOR_BG;
    }

    public void setCOLOR_BG(int COLOR_BG) {
        this.COLOR_BG = COLOR_BG;
    }

    public int getCOLOR_PRO() {
        return COLOR_PRO;
    }

    public void setCOLOR_PRO(int COLOR_PRO) {
        this.COLOR_PRO = COLOR_PRO;
    }

    public int getCLOOR_TV() {
        return CLOOR_TV;
    }

    public void setCLOOR_TV(int CLOOR_TV) {
        this.CLOOR_TV = CLOOR_TV;
    }

    public int getProScore() {
        return proScore;
    }

    public void setProScore(int proScore) {
        this.proScore = proScore;
    }
}
