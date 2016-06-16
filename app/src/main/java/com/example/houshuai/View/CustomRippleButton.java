package com.example.houshuai.View;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/**
 * Created by HouShuai on 2016/6/15.
 */

public class CustomRippleButton extends FrameLayout {

    private int waveColor = Color.parseColor("#66666666");//波纹的颜色
    private Paint paint;
    private float maxRadius;
    private int drawAnimCount = 0;
    private float x;
    private float y;

    public CustomRippleButton(Context context) {
        super(context);
        init();
    }

    public CustomRippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * 设置波纹的颜色
     *
     * @param waveColor
     */
    public void setWaveColor(int waveColor) {
        this.waveColor = waveColor;
        paint.setColor(waveColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        maxRadius = (float) Math.sqrt(Math.pow(width, height));

    }

    /*当前是否正在绘制波纹*/
    public boolean isDrawingWave() {
        return drawAnimCount != 0;
    }
    private boolean drawWave = false;
    private ValueAnimator v;
    private float radius = 5;// 半径
    /*清楚水波纹*/
    public void clearWave() {
        if (v != null) {
            v.end();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();
                ValueAnimator valueAnimator = v.ofFloat(radius, maxRadius);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    radius = animatedValue;
                    postInvalidate();
                }
            });
                valueAnimator.setDuration(1000);
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        drawAnimCount++;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        drawWave = false;
                        radius = 5;
                        drawAnimCount--;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.start();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()||!drawWave) {
            return;
        }
        canvas.drawCircle(x,y,radius,paint);
    }
}
