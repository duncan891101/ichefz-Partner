package com.playhut.partner.pullrefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.playhut.partner.utils.PartnerUtils;

/**
 * 下拉刷新时，动态的圆形视图
 *
 * @author zhangbh
 */
public class HeaderCircleAnimView extends View {

    private Paint mPaint; // 画笔

    private float cx, cy, radius, startAngle, sweepAngle;

    public HeaderCircleAnimView(Context context) {
        super(context);
        init(context);
    }

    public HeaderCircleAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        int strokeWidth = PartnerUtils.dip2px(context, 1);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#A3A3A3"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
    }

    /**
     * 更新圆形
     *
     * @param cx         圆心的X轴坐标
     * @param cy         圆心的Y轴坐标
     * @param radius     半径
     * @param startAngle 开始角度
     * @param sweepAngle 旋转角度
     */
    public void updateCircle(float cx, float cy, float radius,
                             float startAngle, float sweepAngle) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy
                + radius);
        canvas.drawArc(oval, startAngle, sweepAngle, false, mPaint);
        canvas.save();
    }
}
