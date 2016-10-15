package com.playhut.partner.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.playhut.partner.debug.MyLog;
import com.playhut.partner.utils.PartnerUtils;

/**
 *
 */
public class FinanceChartView extends View {

    private float[] mData;

    private Paint mPaint;

    private float mRadius; // 点的半径

    private int mYMaxPixel; // Y轴的高度

    private int mYMaxPointPixel; // Y轴最大点的像素

    private int mXMarginPixel; // X方向点之间间距

    private Paint mLinePaint;

    private Paint mPricePaint;

    private int mPriceYMargin;

    private Paint mMonthPaint;

    private String[] mMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    private int mMonthYMargin;

    private int mFirstPointMarginLeft;

    public FinanceChartView(Context context) {
        super(context);
        init(context);
    }

    public FinanceChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FinanceChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#0099CC"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mYMaxPixel = PartnerUtils.dip2px(context, 140);
        mYMaxPointPixel = (int) (mYMaxPixel * 0.8f);
        mRadius = PartnerUtils.dip2px(context, 6);
        mXMarginPixel = PartnerUtils.dip2px(context, 50);
        mFirstPointMarginLeft = PartnerUtils.dip2px(context, 35);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#0099CC"));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(PartnerUtils.dip2px(context, 1)); // 线宽
        // 实线和空格的长度设置
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{PartnerUtils.dip2px(context, 8), PartnerUtils.dip2px(context, 3)}, 0);
        mLinePaint.setPathEffect(dashPathEffect);

        mPricePaint = new Paint();
        mPricePaint.setColor(Color.parseColor("#333333"));
        mPricePaint.setAntiAlias(true);
        mPricePaint.setTextAlign(Paint.Align.CENTER);
        mPricePaint.setTextSize(PartnerUtils.dip2px(context, 13));

        mPriceYMargin = PartnerUtils.dip2px(context, 10);

        mMonthPaint = new Paint();
        mMonthPaint.setColor(Color.parseColor("#666666"));
        mMonthPaint.setAntiAlias(true);
        mMonthPaint.setTextAlign(Paint.Align.CENTER);
        mMonthPaint.setTextSize(PartnerUtils.dip2px(context, 13));

        mMonthYMargin = PartnerUtils.dip2px(context, 20);
    }

    public void setData(float[] data) {
        mData = data;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = 0;
        if (mData != null){
            widthSize = mXMarginPixel * mData.length + mFirstPointMarginLeft;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

        float maxData = getMaxValue(mData); // 获取数据中的最大值
        float sizeOfPixel = 0;
        if (maxData != 0) {
            sizeOfPixel = mYMaxPointPixel / maxData;
        }

        // 1、画蓝色实心点
        for (int i = 0; i < mData.length; i++) {
            canvas.drawCircle(mFirstPointMarginLeft + i * mXMarginPixel, mYMaxPixel - sizeOfPixel * mData[i], mRadius, mPaint);
        }
        // 2、画线
        for (int i = 0; i < mData.length; i++) {
            if (i < mData.length - 1) {
                Path path = new Path();
                path.moveTo(mFirstPointMarginLeft + i * mXMarginPixel, mYMaxPixel - sizeOfPixel * mData[i]);
                path.lineTo(mFirstPointMarginLeft + (i + 1) * mXMarginPixel, mYMaxPixel - sizeOfPixel * mData[i + 1]);
                canvas.drawPath(path, mLinePaint);
            }
        }
        // 3、画价格
        for (int i = 0; i < mData.length; i++) {
            canvas.drawText("$" + mData[i], mFirstPointMarginLeft + i * mXMarginPixel, mYMaxPixel - sizeOfPixel * mData[i] - mPriceYMargin, mPricePaint);
        }
        // 4、画月份
        for (int i = 0; i < mData.length; i++) {
            canvas.drawText(mMonth[i], mFirstPointMarginLeft + i * mXMarginPixel, mYMaxPixel - sizeOfPixel * mData[i] + mMonthYMargin, mMonthPaint);
        }
    }

    private float getMaxValue(float[] data) {
        float max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

}
