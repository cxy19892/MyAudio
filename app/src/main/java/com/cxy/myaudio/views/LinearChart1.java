package com.cxy.myaudio.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/3/25.
 */
public class LinearChart1 extends View {



    /**存值的map <编号， 值>*/
    private HashMap<Double, Double> mMap;
    /**存编号的list*/
    private ArrayList<Double> mdlk;
    /**枚举实现坐标桌面的样式风格 直线， 曲线*/
    public static enum Mstyle
    {
        Line,Curve
    }
    private Paint bgPaint, linesPaint, dotlinePaint;
    private TextPaint mTPaint;
    private int mHight, mWidth;
    private float mDensity;
    private float widthspace, hightspace;
    private int TEXTSIZE = 10;
    private float fontHeight, textHight;//文字高度
    private boolean IsShowGrid = false;
    private boolean IsShowdBtv = true;
    private int totalvalue=100;
    private int pjvalue=20;
    private String xstr = "s",ystr="dB";
    private Point[] mPoints = new Point[90];
    private Mstyle mstyle=Mstyle.Line;

    private Path path = new Path();
    private Point startp=new Point();
    private Point endp=new Point();
    private Point p3=new Point();
    private Point p4=new Point();

    public Mstyle getMstyle() {
        return mstyle;
    }

    public void setMstyle(Mstyle mstyle) {
        this.mstyle = mstyle;
    }

    public HashMap<Double, Double> getmMap() {
        return mMap;
    }

    public void setmMap(HashMap<Double, Double> mMap) {
        this.mMap = mMap;
        postInvalidate();
    }


    public LinearChart1(Context context) {
        super(context);
    }

    public LinearChart1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        initPaints();
    }

    public LinearChart1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param map 需要的数据，虽然key是double，但是只用于排序和显示，与横向距离无关
     * @param totalvalue Y轴的最大值
     * @param pjvalue Y平均值
     * @param xstr X轴的单位
     * @param ystr Y轴的单位
     * @param isylineshow 是否显示网格
     * @return
     */
    public void SetTuView(HashMap<Double, Double> map,int totalvalue,int pjvalue,String xstr,String ystr,Boolean isylineshow)
    {
        this.mMap=map;
        this.totalvalue=totalvalue;
        this.pjvalue=pjvalue;
        this.xstr=xstr;
        this.ystr=ystr;
        this.IsShowGrid=isylineshow;
        //屏幕横向
//        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void initPaints(){
        bgPaint = new Paint();
        bgPaint.setColor(Color.GRAY);
        bgPaint.setStrokeWidth(mDensity);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        dotlinePaint = new Paint(bgPaint);
        dotlinePaint.setStrokeWidth(1);


        linesPaint = new Paint();
        linesPaint.setColor(Color.GREEN);
        linesPaint.setStrokeWidth(mDensity);
        linesPaint.setAntiAlias(true);
        linesPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        mTPaint = new TextPaint();
        // 去锯齿
        mTPaint.setAntiAlias(true);
        // 防抖动
        mTPaint.setDither(true);
        mTPaint.setTextAlign(Paint.Align.CENTER);
        mTPaint.setTextSize(TEXTSIZE * mDensity);
        mTPaint.setColor(Color.GRAY);

        Paint.FontMetrics mfontMetrics = mTPaint.getFontMetrics();
        //计算文字高度
        fontHeight = mfontMetrics.bottom - mfontMetrics.top;
        textHight = mfontMetrics.descent;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHight = bottom - top-getPaddingBottom()-getPaddingTop();
        mWidth = right - left - getPaddingLeft() - getPaddingRight();
        widthspace = (mWidth - Layout.getDesiredWidth("100dB", mTPaint)-1)/15;
        hightspace = (mHight - fontHeight)/10;
        if(mHight <= 6*fontHeight){
            IsShowdBtv = false;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawbg(canvas);
        drawChartView(canvas);
    }


    private void drawbg(Canvas canvas){
        //水平坐标轴
        canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint), mHight - fontHeight, mWidth, mHight - fontHeight, bgPaint);

        //竖直坐标轴
        canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint), 0, Layout.getDesiredWidth("100dB", mTPaint), mHight - fontHeight, bgPaint);

        //竖直坐标
        for(int i = 0 ; i < 6 ; i++){
            canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint), mHight - fontHeight - i * hightspace * 2, Layout.getDesiredWidth("100dB", mTPaint)+5*mDensity, mHight - fontHeight - i * hightspace * 2, bgPaint);
            if(IsShowdBtv) {
                if (i == 0)
                    canvas.drawText(20 * i + ystr, Layout.getDesiredWidth("100dB", mTPaint) / 2, mHight - fontHeight, mTPaint);
                else if (i == 5) {
                    canvas.drawText(20 * i + ystr, Layout.getDesiredWidth("100dB", mTPaint) / 2, fontHeight, mTPaint);
                } else {
                    canvas.drawText(20 * i + ystr, Layout.getDesiredWidth("100dB", mTPaint) / 2, mHight - fontHeight + textHight - i * hightspace * 2, mTPaint);
                }
            }
            if(IsShowGrid)//是否显示网格
                canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint), mHight - fontHeight - i * hightspace * 2, mWidth, mHight - fontHeight - i * hightspace * 2, dotlinePaint);
        }
        for (int j = 0 ; j < 16 ; j++){
            if(j%5 == 0) {
                if (j == 15) {
                    canvas.drawText(j/5 * 10 + xstr, Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace - Layout.getDesiredWidth("30s", mTPaint), mHight, mTPaint);
                } else
                    canvas.drawText(j/5 * 10 + xstr, Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace, mHight, mTPaint);
                canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace, mHight - fontHeight, Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace, mHight - fontHeight - 5*mDensity, bgPaint);
            }
            if(IsShowGrid)//是否显示网格
                canvas.drawLine(Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace, 0, Layout.getDesiredWidth("100dB", mTPaint) + j * widthspace, mHight - fontHeight, dotlinePaint);
        }
    }


    private void drawChartView(Canvas canvas){
        ArrayList<Integer> xlist=new ArrayList<Integer>();//记录每个x的值
        int pointXspace =  new BigDecimal((mWidth-Layout.getDesiredWidth("100dB", mTPaint)) / 90).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        for (int i = 0; i < 90; i++) {
            int x = (int)(Layout.getDesiredWidth("100dB", mTPaint) + i * pointXspace);
            xlist.add(x > mWidth ? mWidth : x);
        }

        mdlk = getintfrommap(mMap);
        mPoints = getpoints(mdlk, mMap, xlist, 100, (int)(mHight - fontHeight));

        if(mstyle==Mstyle.Curve)
            drawscrollline(mPoints, canvas, linesPaint);
        else
            drawline(mPoints, canvas, linesPaint);

    }

    /**
     * 通过传入的值获取对应的点坐标
     * @param dlk map对饮的key的list
     * @param map map<序号，实际的值>
     * @param xlist x轴坐标值的list
     * @param max 最大值 这里指设定的最大分贝
     * @param h   绘图区域的总高度
     * @return
     */
    private Point[] getpoints(ArrayList<Double> dlk,HashMap<Double, Double> map,ArrayList<Integer> xlist,int max,int h)
    {
        Point[] points=new Point[dlk.size()];
        for(int i=0;i<dlk.size();i++)
        {
            int ph=h-(int)(h*(map.get(dlk.get(i))/max));

            points[i]=new Point(xlist.get(i),ph);
        }
        return points;
    }

    /**
     * 根据 map 得到对应的key的升序排列的 list
     * @param map
     * @return
     */
    public ArrayList<Double> getintfrommap(HashMap<Double, Double> map)
    {
        ArrayList<Double> dlk=new ArrayList<Double>();
        int position=0;
        if(map==null)
            return null;
        Set set= map.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext())
        {
            Map.Entry mapentry  = (Map.Entry)iterator.next();
            dlk.add((Double)mapentry.getKey());
        }
        for(int i=0;i<dlk.size();i++)
        {
            int j=i+1;
            position=i;
            Double temp=dlk.get(i);
            for(;j<dlk.size();j++)
            {
                if(dlk.get(j)<temp)
                {
                    temp=dlk.get(j);
                    position=j;
                }
            }

            dlk.set(position,dlk.get(i));
            dlk.set(i,temp);
        }
        return dlk;
    }

    private void drawscrollline(Point[] ps, final Canvas canvas, final Paint paint)
    {

        for(int i=0;i<ps.length-1;i++)
        {
            startp=ps[i];
            endp=ps[i+1];
            int wt=(startp.x+endp.x)/2;

            p3.y=startp.y;
            p3.x=wt;
            p4.y=endp.y;
            p4.x=wt;

            path.reset();
            path.moveTo(startp.x,startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, paint);
            /*//通过贝塞尔曲线公式，自定义估值器
            final BezierEvaluator evaluator = new BezierEvaluator(startp, endp);
            //将估值器传入属性动画，不断的修改控件的坐标
            ValueAnimator animator = ValueAnimator.ofObject(evaluator, p3, p4);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Point pointf = (Point) animation.getAnimatedValue();
                    path.lineTo(pointf.x, pointf.y);
                    canvas.drawPath(path, paint);
                }
            });
            animator.setDuration(mWidth / 300);
            animator.start();*/
        }
    }


    private void drawline(Point[] ps,Canvas canvas,Paint paint)
    {
        for(int i=0;i<ps.length-1;i++)
        {
            startp=ps[i];
            endp=ps[i+1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, paint);
        }
    }
}
