package com.violin.views.views.fallingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.violin.views.R;

/**
 * Created by dingmouren on 2017/4/28.
 */

public class FallingView extends FrameLayout {
    private static final String TAG = FallingView.class.getName();
    private static final int DEFAULT_FLAKES_DENSITY = 80;
    private static final int DEFAULT_DELAY = 10;
    private int mFlakesDensity = DEFAULT_FLAKES_DENSITY;
    private int mDelay = DEFAULT_DELAY;
    private static final int DEFAULT_SCALE = 3;
    private Flake[] mFlakes;
    private Bitmap mFlakeBitmap;
    private Paint mPaint;
    private int mImgId;
    private int mScale;
    private int mWidth;
    private int mHeight;
    private int mRawWidth;

    public FallingView(Context context) {
        this(context, null);
    }

    public FallingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.TRANSPARENT);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FallingView);
            mImgId = a.getResourceId(R.styleable.FallingView_flakeSrc, R.drawable.boomb);
            mScale = a.getInt(R.styleable.FallingView_flakeScale, DEFAULT_SCALE);
            mFlakesDensity = a.getInt(R.styleable.FallingView_flakeDensity, DEFAULT_FLAKES_DENSITY);
            mDelay = a.getInt(R.styleable.FallingView_fallingDelay, DEFAULT_DELAY);
        } else {
            mImgId = R.drawable.boomb;
            mScale = DEFAULT_SCALE;
            mFlakesDensity = DEFAULT_FLAKES_DENSITY;
            mDelay = DEFAULT_DELAY;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            mWidth = w;
            mHeight = h;
            mRawWidth = initScale(mScale);
            initDenstity(w, h, mRawWidth);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (Flake flake : mFlakes) {
            flake.draw(canvas, mFlakeBitmap);
        }
        getHandler().postDelayed(mRunnable, mDelay);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private void initDenstity(int w, int h, int rawWidth) {
        mFlakes = new Flake[mFlakesDensity];
        for (int i = 0; i < mFlakesDensity; i++) {
            mFlakes[i] = Flake.create(w, h, mPaint, rawWidth / mScale);
        }
    }

    private int initScale(int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), mImgId, options);
        int rawWidth = options.outWidth;
        mRawWidth = rawWidth;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        mFlakeBitmap = BitmapFactory.decodeResource(getResources(), mImgId, options);
        return rawWidth;
    }

    public void setImageResource(int imgId) {
        this.mImgId = imgId;
        initScale(mScale);
    }

    public void setScale(int scale) {
        initScale(scale);
    }

    public void setDensity(int density) {
        this.mFlakesDensity = density;
        if (mWidth > 0 && mHeight > 0) {
            initDenstity(mWidth, mHeight, mRawWidth);
        }
    }

    public void setDelay(int delay) {
        this.mDelay = delay;
    }
}
