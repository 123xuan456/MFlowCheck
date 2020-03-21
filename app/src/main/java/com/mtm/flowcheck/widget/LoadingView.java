package com.mtm.flowcheck.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mtm.flowcheck.R;

/**
 * Description : 加载时转圈圈
 */

public class LoadingView extends View {

    private final String TAG = "LoadingView";
    private Paint mTestPaint;
    private Paint mOuterPaint;
    private RectF mOuterRectF;
    private Paint mInnerPaint;
    private RectF mInnerRectF;
    int mStart = 0;
    int mSweep = 90;

    int mWidth, mHeight;
    int mArcLenght = 60;
    int mOuterWidth;
    int mInnerWidth;
    int mOuterColor;
    int mInnerColor;
    int mInnerRotatingSpeed = 1;
    LoadingMode mLoadingMode = LoadingMode.ARC;
    int mCircle = 0;

    int mCircleSpeed = 1;

    int mCustomDegree = 0;

    enum LoadingMode {
        ARC, CIRCLE, CUSTOM;
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);

    }

    private void setBounds() {
        mOuterRectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth()
                - getPaddingRight(), getHeight() - getPaddingBottom());
        mInnerRectF = new RectF(mOuterRectF);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setBounds();
        initPaint();
    }

    private void initPaint() {
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStyle(Style.STROKE);
        mOuterPaint.setStrokeWidth(mOuterWidth);
        //
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Style.STROKE);
        mInnerPaint.setStrokeWidth(mInnerWidth);
        //
        mTestPaint = new Paint();
        mTestPaint.setAntiAlias(true);
        mTestPaint.setColor(Color.BLACK);
        mTestPaint.setStyle(Style.STROKE);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array;
        array = context.obtainStyledAttributes(attrs,
                R.styleable.Loading);
        mOuterWidth = array.getDimensionPixelOffset(
                R.styleable.Loading_outer_width, 0);

//		mOuterColor = array.getColor(R.styleable.Loading_outer_color,
//				Color.GRAY);
        mOuterColor = Color.parseColor("#0670C8");
        mInnerWidth = array.getDimensionPixelOffset(
                R.styleable.Loading_inner_width,
                R.styleable.Loading_inner_width);
        mInnerColor = array.getColor(R.styleable.Loading_inner_color,
                Color.BLACK);
        mInnerRotatingSpeed = array.getInt(
                R.styleable.Loading_inner_rotating_speed, 1);
        if (array.getInt(R.styleable.Loading_mode, 0) == 1) {
            mLoadingMode = LoadingMode.ARC;
        } else if (array.getInt(R.styleable.Loading_mode, 0) == 2) {
            mLoadingMode = LoadingMode.CIRCLE;
            mCircleSpeed = array.getInt(R.styleable.Loading_inner_circle_speed,
                    1);
        } else if (array.getInt(R.styleable.Loading_mode, 0) == 3) {
            mLoadingMode = LoadingMode.CUSTOM;
            mCustomDegree = array.getInt(
                    R.styleable.Loading_inner_custom_degree, 1);
        }
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawRect(mOuterRectF, mTestPaint);
        canvas.drawArc(mOuterRectF, 360, 360, false, mOuterPaint);
        if (mLoadingMode == LoadingMode.ARC) {
            canvas.drawArc(mInnerRectF, mStart, mSweep + 2, false, mInnerPaint);
            // int d = mRandom.nextInt(8);
            mStart += mInnerRotatingSpeed;
            if (mStart > 360) {
                mStart -= 360;
            }
            invalidate();
        } else if (mLoadingMode == LoadingMode.CIRCLE) {
            canvas.drawArc(mInnerRectF, mStart, mCircle, false, mInnerPaint);
            mCircle += mCircleSpeed;
            if (mCircle > 360) {
                mCircle -= 360;
            }
            invalidate();
        } else if (mLoadingMode == LoadingMode.CUSTOM) {
            float f = (float) mCustomDegree / 100f;
            canvas.drawArc(mInnerRectF, mStart, 360f * f, false, mInnerPaint);
        }

    }
}