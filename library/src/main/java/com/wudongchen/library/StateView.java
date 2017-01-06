package com.wudongchen.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * StateView
 *
 * @author dong-chen.wu
 * @version 1.0
 * @date 2016/12/22
 */
public class StateView extends View {

    /**
     * SUCCESS 成功
     * WARN 警告
     * FAILUE 失败
     * FORBID 禁止
     * ERROR 错误
     */
    public enum State {
        SUCCESS,
        WARN,
        FAILUE,
        FORBID,
        ERROR,
    }

    private State mState = State.SUCCESS;

    private Paint mPaint;
    private Paint mPaintInside;

    private Path mRoundPath;
    private Path mRoundPathInside;
    private Path mStateOnePath;
    private Path mStateTwoPath;

    private PathMeasure mPathMeasure;

    private ValueAnimator mRoundAnimator;
    private ValueAnimator mStateOneAnimator;
    private ValueAnimator mStateTwoAnimator;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    private float mRoundAnimatorValue;
    private float mStateOneAnimatorValue;
    private float mStateTwoAnimatorValue;

    private int mViewWidth;

    private long durationOne = 500;
    private long durationTwo = 500;

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public StateView(Context context) {
        this(context, State.SUCCESS);
    }

    public StateView(Context context, State state) {
        this(context, null, state);
    }

    public StateView(Context context, AttributeSet attrs, State state) {
        super(context, attrs);
        this.mState = state;
    }

    private void init() {
        initPaint();
        initPath();
        initListener();
        initAnimator();

        mRoundAnimator.start();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(dp2px(getContext(), 3));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mPaintInside = new Paint();
        mPaintInside.setStyle(Paint.Style.STROKE);
        mPaintInside.setColor(Color.TRANSPARENT);
        mPaintInside.setStrokeWidth(1);
        mPaintInside.setStrokeCap(Paint.Cap.ROUND);
        mPaintInside.setAntiAlias(true);
    }

    private List<float[]> percentage = new ArrayList<>();
    private List<float[]> percentageInside = new ArrayList<>();

    private void initPath() {
        mRoundPath = new Path();
        mRoundPathInside = new Path();
        mStateOnePath = new Path();
        mStateTwoPath = new Path();

        float cy;
        float cx = cy = getWidth() / 3;

        RectF rect = new RectF(-cx, -cy, cx, cy);
        mRoundPath.addArc(rect, 190, 359.9f);

        RectF rectInside = new RectF(-getWidth() / 5, -getWidth() / 5, getWidth() / 5, getWidth() / 5);
        mRoundPathInside.addArc(rectInside, 190, 359.9f);


        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mRoundPath, false);

        PathMeasure pathMeasureInside = new PathMeasure();
        pathMeasureInside.setPath(mRoundPathInside, false);

        for (int i = 0; i < 100; i++) {
            float[] is = new float[2];
            pathMeasureInside.getPosTan(i * pathMeasureInside.getLength() / 100, is, null);
            percentage.add(is);
        }

        for (int i = 0; i < 100; i++) {
            float[] is = new float[2];
            mPathMeasure.getPosTan(i * mPathMeasure.getLength() / 100, is, null);
            percentageInside.add(is);
        }

        initStatePath();
    }

    private void initStatePath() {

        switch (mState) {
            case SUCCESS:
                durationOne = 250;
                durationTwo = 250;

                mStateOnePath.moveTo(percentage.get(95)[0], percentage.get(95)[1]);
                mStateOnePath.lineTo(percentage.get(78)[0], percentage.get(62)[1]);
                mStateOnePath.lineTo(percentage.get(41)[0], percentage.get(41)[1]);

                break;
            case WARN:
                durationOne = 400;
                durationTwo = 100;

                mStateOnePath.moveTo(percentage.get(22)[0], percentage.get(22)[1]);
                mStateOnePath.lineTo(percentage.get(22)[0], percentage.get(55)[1]);
                mStateTwoPath.moveTo(percentage.get(22)[0], percentage.get(70)[1]);
                mStateTwoPath.lineTo(percentage.get(22)[0], percentage.get(71)[1]);

                break;
            case FAILUE:
                durationOne = 250;
                durationTwo = 250;

                mStateOnePath.moveTo(percentage.get(10)[0], percentage.get(10)[1]);
                mStateOnePath.lineTo(percentage.get(60)[0], percentage.get(60)[1]);
                mStateTwoPath.moveTo(percentage.get(35)[0], percentage.get(35)[1]);
                mStateTwoPath.lineTo(percentage.get(85)[0], percentage.get(85)[1]);
                break;
            case FORBID:
                durationOne = 250;
                durationTwo = 250;

                mStateOnePath.moveTo(percentageInside.get(10)[0], percentageInside.get(10)[1]);
                mStateOnePath.lineTo(percentageInside.get(60)[0], percentageInside.get(60)[1]);
                break;
            case ERROR:
                durationOne = 100;
                durationTwo = 400;

                mStateOnePath.moveTo(percentage.get(22)[0], percentage.get(22)[1]);
                mStateOnePath.lineTo(percentage.get(22)[0], percentage.get(21)[1]);
                mStateTwoPath.moveTo(percentage.get(22)[0], percentage.get(40)[1]);
                mStateTwoPath.lineTo(percentage.get(22)[0], percentage.get(71)[1]);

                break;
        }
    }

    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation == mRoundAnimator) {
                    mRoundAnimatorValue = (float) animation.getAnimatedValue();
                } else if (animation == mStateOneAnimator) {
                    mStateOneAnimatorValue = (float) animation.getAnimatedValue();
                } else if (animation == mStateTwoAnimator) {
                    mStateTwoAnimatorValue = (float) animation.getAnimatedValue();
                }
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animation == mRoundAnimator) {
                    mStateOneAnimator.start();
                } else if (animation == mStateOneAnimator) {
                    if (mState == State.ERROR || mState == State.WARN || mState == State.FAILUE) {
                        if (mStateTwoAnimator != null)
                            mStateTwoAnimator.start();
                    } else {
                        if (mCallback != null)
                            mCallback.onDismiss();
                    }
                } else if (animation == mStateTwoAnimator) {
                    if (mCallback != null)
                        mCallback.onDismiss();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initAnimator() {

        long duration = 500;
        mRoundAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        mStateOneAnimator = ValueAnimator.ofFloat(0, 1).setDuration(durationOne);

        mRoundAnimator.addUpdateListener(mUpdateListener);
        mRoundAnimator.addListener(mAnimatorListener);

        mStateOneAnimator.addUpdateListener(mUpdateListener);
        mStateOneAnimator.addListener(mAnimatorListener);

        if (mState == State.ERROR || mState == State.WARN || mState == State.FAILUE) {
            mStateTwoAnimator = ValueAnimator.ofFloat(0, 1).setDuration(durationTwo);
            mStateTwoAnimator.addUpdateListener(mUpdateListener);
            mStateTwoAnimator.addListener(mAnimatorListener);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawState(canvas);
    }

    private void drawState(Canvas canvas) {

        mPaint.setColor(Color.WHITE);

        canvas.translate(mViewWidth / 2, mViewWidth / 2);

        canvas.drawColor(Color.TRANSPARENT);

        mPathMeasure.setPath(mRoundPath, false);
        Path dst = new Path();
        mPathMeasure.getSegment(0, mPathMeasure.getLength() * mRoundAnimatorValue, dst, true);
        canvas.drawPath(dst, mPaint);

        mPathMeasure.setPath(mRoundPathInside, false);
        Path dstT = new Path();
        mPathMeasure.getSegment(0, mPathMeasure.getLength() * mRoundAnimatorValue, dstT, true);
        canvas.drawPath(dstT, mPaintInside);

        mPathMeasure.nextContour();
        mPathMeasure.setPath(mStateOnePath, false);
        Path dst2 = new Path();
        mPathMeasure.getSegment(0, mPathMeasure.getLength() * mStateOneAnimatorValue, dst2, true);
        canvas.drawPath(dst2, mPaint);

        if (mState == State.ERROR || mState == State.WARN || mState == State.FAILUE) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mStateTwoPath, false);
            Path dst3 = new Path();
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * mStateTwoAnimatorValue, dst3, true);
            canvas.drawPath(dst3, mPaint);
        }
    }

    interface Callback {
        void onDismiss();
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}