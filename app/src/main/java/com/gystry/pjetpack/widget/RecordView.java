package com.gystry.pjetpack.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gystry.pjetpack.R;

/**
 * @author gystry
 * 创建日期：2020/7/27 09
 * 邮箱：gystry@163.com
 * 描述：
 */
public class RecordView extends View implements View.OnClickListener, View.OnLongClickListener {

    /**
     * 更新进度条间隔得最小时间
     */
    private static final int PROGRESS_INTERVAL = 100;
    private float progressWidth;
    private int progressColor;
    private int fillColor;
    private float progressRadius;
    private int duration;
    private int progressMaxValue;
    private Paint fillPaint;
    private Paint progressPaint;
    private boolean isRecording;
    private int progressValue;
    private long StartRecordTime;
    private OnRecordListener mListener;

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView, defStyleAttr, defStyleRes);
        progressWidth = typedArray.getDimensionPixelOffset(R.styleable.RecordView_progress_width, 0);
        progressRadius = typedArray.getDimensionPixelOffset(R.styleable.RecordView_radius, 0);
        progressColor = typedArray.getColor(R.styleable.RecordView_progress_color, 0xff333333);
        fillColor = typedArray.getColor(R.styleable.RecordView_fill_color, 0xff333333);
        duration = typedArray.getInteger(R.styleable.RecordView_duration, 20);
        typedArray.recycle();

        setMaxDuration();

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(fillColor);
        fillPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);

        Handler handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                progressValue++;
                postInvalidate();
                if (progressValue <= progressMaxValue) {
                    sendEmptyMessageDelayed(0, PROGRESS_INTERVAL);
                } else {
                    finishRecord();
                }
            }
        };
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRecording = true;
                    StartRecordTime = System.currentTimeMillis();
                    handler.sendEmptyMessage(0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - StartRecordTime > ViewConfiguration.getLongPressTimeout()) {
                        finishRecord();
                    }
                    handler.removeCallbacksAndMessages(null);
                    isRecording = false;
                    StartRecordTime = 0;
                    progressValue = 0;
                    postInvalidate();
                }
                return false;
            }
        });

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    private void finishRecord() {
        if (mListener!=null) {
            mListener.finish();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (isRecording) {
            canvas.drawCircle(width / 2f, height / 2f, width / 2f, fillPaint);

            float sweepAngle = (progressValue / (progressMaxValue * 1.0f)) * 360f;
            canvas.drawArc(0, 0, width, height, -90, sweepAngle, false, progressPaint);
        } else {
            canvas.drawCircle(width / 2f, height / 2f, progressRadius, fillPaint);
        }
    }

    /**
     * 设置进度条最大值
     */
    private void setMaxDuration() {
        this.progressMaxValue = duration * 1000 / PROGRESS_INTERVAL;
    }

    public void setOnRecordListener(OnRecordListener listener){

        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener!=null) {
            mListener.click();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener!=null) {
            mListener.longClick();
        }
        return true;
    }


    public interface OnRecordListener{
        void click();
        void longClick();
        void finish();
    }

}
