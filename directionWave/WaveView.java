package com.example.administrator.demomo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2018/3/25.
 */

public class WaveView extends View {
    private int  width = 0;
    private int height = 0;
    private int baseLine = 0;
    private Paint mPaint;
    private int waveHeight = 100;
    private int waveWidth  ;
    private float offset =0f;
    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void updateXControl(){
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0,waveWidth);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorValue = (float)animation.getAnimatedValue() ;
                offset = animatorValue;
                postInvalidate();
            }
        });
        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getPath(),mPaint);
    }
    private void initView(){
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(0,191,255));
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        waveWidth = width;
        baseLine = height/2;
        updateXControl();
    }
    private Path getPath() {
        Path mPath = new Path();
        Path dPath = new Path();
        Path xPath = new Path();
        int a = 0;
        OrientationActivity orientationActivity = new OrientationActivity();
        orientationActivity.calculateOrientation();
        if (a == 1) {
            baseLine = height/2;
            dPath.moveTo(0, baseLine*3/2);
            for (int i = 0; i < 5; i++) {
                dPath.quadTo(
                        i*width/2 + offset,
                        i*(baseLine/2),
                        (i+1)*width/2+ offset,
                        i*(baseLine/2)
                );
            }
            dPath.lineTo(width,height);
            dPath.lineTo(0,height);
            dPath.close();
            return  dPath;

        } else if (a == 2) {
            baseLine = height/2;
            xPath.moveTo(0, baseLine*3/2);
            for (int i = 0; i < 5; i++) {
                xPath.quadTo(
                        i*width/2 + offset,
                        i*(baseLine/2),
                        i*width/2+ offset,
                        i*(baseLine/2)
                );
            }
            xPath.lineTo(width,height);
            xPath.lineTo(0,height);
            xPath.close();
            return  xPath;

        } else {
            int itemWidth = waveWidth / 2;
            mPath.moveTo(-itemWidth * 3, baseLine);
            for (int i = -3; i < 2; i++) {
                int startX = i * itemWidth;
                mPath.quadTo(
                        startX + itemWidth / 2 + offset,
                        getWaveHeigh(i),
                        startX + itemWidth + offset,
                        baseLine
                );
            }
            mPath.lineTo(width, height);
            mPath.lineTo(0, height);
            mPath.close();
            return mPath;
        }
    }
    private int getWaveHeigh(int num){
        if(num % 2 == 0){
            return baseLine + waveHeight;
        }
        return baseLine - waveHeight;
    }

}
