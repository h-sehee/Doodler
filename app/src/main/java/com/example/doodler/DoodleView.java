package com.example.doodler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DoodleView extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private boolean isEraserMode = false;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
                if (isEraserMode) {
                    mPaint.setColor(Color.WHITE);
                }
                mCanvas.drawPoint(x, y, mPaint);
                break;
            case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
        }
        this.invalidate();
        return true;
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        mPaint.setStrokeWidth(width);
    }

    public void setPaintAlpha(int alpha) {
        int currentColor = mPaint.getColor();
        mPaint.setColor(Color.argb(alpha, Color.red(currentColor), Color.green(currentColor), Color.blue(currentColor)));
    }

    public int getPaintColor() {
        return mPaint.getColor();
    }

    public float getPaintStrokeWidth() {
        return mPaint.getStrokeWidth();
    }

    public int getPaintAlpha() {
        return Color.alpha(mPaint.getColor());
    }

    public void clear() {
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }
}