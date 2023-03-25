package com.example.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * @PackageName: com.example.clockview
 * @ClassName: ClockView
 * @Author: winwa
 * @Date: 2023/3/25 10:16
 * @Description:
 **/
public class ClockView extends View {
    private Paint mPaint;

    private int hour;
    private int minute;
    private int second;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        int color = typedArray.getColor(R.styleable.ClockView_ClockColor, Color.BLACK);
        mPaint.setColor(color);
        typedArray.recycle();

        setTime();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPadding(20, 20, 20, 20);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeWidth(8);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 20, mPaint);

        mPaint.setStrokeWidth(4);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 30, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 10, mPaint);

        for (int i = 0; i < 12; i++) {
            canvas.save();
            canvas.rotate(360 / 12 * i, getWidth() / 2, getHeight() / 2);
            canvas.drawLine(getWidth() / 2, 40, getWidth() / 2, 50, mPaint);
            canvas.restore();
        }

        mPaint.setStrokeWidth(8);
        canvas.save();
        canvas.rotate(30 * hour + 0.5f * minute, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 5, mPaint);
        canvas.restore();

        mPaint.setStrokeWidth(5);
        canvas.save();
        canvas.rotate(6 * minute, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 4, mPaint);
        canvas.restore();

        mPaint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(6 * second, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - getHeight() / 3, mPaint);
        canvas.restore();

        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            size = Math.min(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            size = widthSize;
        } else if (heightMode == MeasureSpec.EXACTLY) {
            size = heightSize;
        } else {
            size = 400;
        }

        setMeasuredDimension(size, size);
    }

    public void setTime() {
        Calendar instance = Calendar.getInstance();
        hour = instance.get(Calendar.HOUR);
        minute = instance.get(Calendar.MINUTE);
        second = instance.get(Calendar.SECOND);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setTime();
                invalidate();
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };
}
