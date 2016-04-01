package com.cxy.myaudio.model;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * 自定义贝塞尔曲线估值器
 */
public class BezierEvaluator implements TypeEvaluator<Point> {
    Point mPoint1;
    Point mPoint2;

    public BezierEvaluator(Point mPointF1, Point mPointF2) {
        this.mPoint1 = mPointF1;
        this.mPoint2 = mPointF2;
    }

    @Override
    public Point evaluate(float t, Point point0, Point point3) {
        //t 百分比 0~1
        Point point = new Point();
        //套用公式进行计算
        point.x = (int) (point0.x * (1 - t) * (1 - t) * (1 - t)
                        + 3 * mPoint1.x * t * (1 - t) * (1 - t)
                        + 3 * mPoint2.x * t * t * (1 - t)
                        + point3.x * t * t * t);

        point.y = (int) (point0.y * (1 - t) * (1 - t) * (1 - t)
                        + 3 * mPoint1.y * t * (1 - t) * (1 - t)
                        + 3 * mPoint2.y * t * t * (1 - t)
                        + point3.y * t * t * t);
        return point;
    }
}
