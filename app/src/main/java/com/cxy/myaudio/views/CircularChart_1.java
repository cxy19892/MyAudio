package com.cxy.myaudio.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/3/28.
 */
public class CircularChart_1 extends View {

    private int mwidth, mhight, mradius, xmidpoint, ymidpoint;
    private float mDensity;
    private int TEXTSIZE = 10;
    private float fontHeight, textHight;//文字高度
    private Paint mCirclePaint, mScalePaint, centerPaint;
    private TextPaint mTvPaint, mScorePaint;
    private RectF mrectF;
    private int DBscore = 0;
    private Path path;
    private Paint paint;
    private LinearGradient  shader;


    public CircularChart_1(Context context) {
        super(context);
    }

    public CircularChart_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        initPaint();
    }

    public void setDBscore(int score){
        DBscore = score;
    }

    private void initPaint(){
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mScalePaint = new Paint();
        mScalePaint.setColor(Color.GREEN);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeCap(Paint.Cap.ROUND);
        mScalePaint.setStrokeWidth(2 * mDensity);

        paint = new Paint();
        path = new Path();

        mTvPaint = new TextPaint();
        // 去锯齿
        mTvPaint.setAntiAlias(true);
        // 防抖动
        mTvPaint.setDither(true);
        mTvPaint.setTextAlign(Paint.Align.CENTER);
        mTvPaint.setTextSize(TEXTSIZE * mDensity);
        mTvPaint.setColor(Color.GRAY);

        mScorePaint = new TextPaint(mTvPaint);
        mScorePaint.setTextSize(20 * mDensity);
        mScorePaint.setColor(Color.BLACK);
        Paint.FontMetrics mfontMetrics = mTvPaint.getFontMetrics();
        //计算文字高度
        fontHeight = mfontMetrics.bottom - mfontMetrics.top;
        textHight = mfontMetrics.descent;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mhight = bottom - top-getPaddingBottom()-getPaddingTop();
        mwidth = right - left - getPaddingLeft() - getPaddingRight();
        xmidpoint = mwidth / 2;
        ymidpoint = mhight / 2;
        int temp = mhight > mwidth? mwidth : mhight;
        mhight = mwidth = temp;
        mradius = mhight / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawbackground(canvas);
        drawScales(canvas);
        drawPointer(canvas, DBscore);
        drawCenter(canvas);
        drawText(canvas);
    }

    /**
     * 画背景
     * @param canvas
     */
    private void drawbackground(Canvas canvas){
        canvas.drawCircle(xmidpoint, ymidpoint, mradius, mCirclePaint);

        mrectF = new RectF(xmidpoint - mradius + 2 * mDensity, 2 * mDensity, xmidpoint + mradius - 2 * mDensity, mhight - 2 * mDensity);
        canvas.drawArc(mrectF, 150, 240, false, mScalePaint);
    }

    /**
     * 画刻度
     */
    private void drawScales(Canvas canvas){
        mScalePaint.setStrokeCap(Paint.Cap.SQUARE);
        for (int i = 0; i < 121; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(240 + (float)(240 / 120) * i, xmidpoint, ymidpoint);
            if(i % 20 == 0){
                mScalePaint.setStrokeWidth(4);
                canvas.drawLine(xmidpoint, ymidpoint - mradius + 2 * mDensity, xmidpoint, ymidpoint - mradius + 20 + 2 * mDensity, mScalePaint);
                canvas.drawText(i+"",xmidpoint, ymidpoint - mradius + 20 + 2 * mDensity + fontHeight, mTvPaint);
            }else {
                mScalePaint.setStrokeWidth(2);
                canvas.drawLine(xmidpoint, ymidpoint - mradius  + 2 * mDensity, xmidpoint, ymidpoint - mradius + 10 + 2 * mDensity, mScalePaint);
            }
            canvas.restore();//
        }
    }

    /**
     * 画圆心
     * @param canvas
     */
    private void drawCenter(Canvas canvas){
        shader = new LinearGradient(xmidpoint - mwidth/12, ymidpoint - mwidth /12, xmidpoint + mwidth/12,ymidpoint + mwidth / 12, new int[]{Color.WHITE, Color.GRAY}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        mCirclePaint.setShader(shader);
        canvas.drawCircle(xmidpoint, ymidpoint, mwidth / 12, mCirclePaint);
        canvas.drawCircle(xmidpoint, ymidpoint, 10, mScalePaint);
        mCirclePaint.setShader(null);
    }

    private void drawPointer(Canvas canvas, int DBdata){
        canvas.save();
        canvas.rotate(240 + (float) (240 / 120) * DBdata, xmidpoint, ymidpoint);
        int point1X = xmidpoint;
        int point2X = xmidpoint - mwidth/20;
        int point3X = xmidpoint + mwidth/20;
        int point1Y = (int) (ymidpoint - mradius + 20 + 2 * mDensity + fontHeight);
        int point2Y = ymidpoint;
        int point3Y = ymidpoint;
        paint.reset();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        path.reset();
        path.moveTo(point1X, point1Y);
        path.lineTo(point2X, point2Y);
        path.lineTo(point3X, point3Y);
        path.lineTo(point1X, point1Y);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void drawText(Canvas canvas){
        canvas.drawText(DBscore+ "  dB", xmidpoint, ymidpoint+ mradius/2, mScorePaint);
    }


}
