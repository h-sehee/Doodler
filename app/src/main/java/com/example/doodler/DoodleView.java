package com.example.doodler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DoodleView extends View {
    private Bitmap mBitmap;
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private boolean isEraserMode = false;
    private int previousColor = Color.BLACK;
    private int previousAlpha = 255;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(mBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int CLICK_THRESHOLD = 1;
        float x = event.getX();
        float y = event.getY();
        float startX = 0;
        float startY = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                if (isEraserMode) {
                    if (paint.getColor()!=0 && paint.getAlpha()!= 0) {
//                        previousColor = paint.getColor();
//                        previousAlpha = paint.getAlpha();
                    }
                    if (paint.getXfermode() == null) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    }
                } else {
                    if (paint.getXfermode() != null) {
                        paint.setXfermode(null);
                    }
//                    paint.setColor(previousColor);
//                    paint.setAlpha(previousAlpha);
                }
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                if (Math.abs(x - startX) < CLICK_THRESHOLD && Math.abs(y - startY) < CLICK_THRESHOLD) {
                    canvas.drawPoint(x, y, paint);
                } else {
                    canvas.drawPath(path, paint);
                }
                path.reset();
//                if (!isEraserMode) {
//                    paint.setColor(previousColor);
//                    paint.setAlpha(previousAlpha);
//                }
                break;
        }

        this.invalidate();
        return true;
    }


    public void setPaintColor(int color) {
        if (this.paint.getColor() != color) {
            this.paint.setColor(color);
            invalidate();
        }
    }

    public void setStrokeWidth(float width) {
        if (this.paint.getStrokeWidth() != width) {
            this.paint.setStrokeWidth(width);
        }
    }

    public void setPaintAlpha(int alpha) {
        if (this.paint.getAlpha() != alpha) {
            int currentColor = paint.getColor();
            paint.setColor(Color.argb(alpha, Color.red(currentColor), Color.green(currentColor), Color.blue(currentColor)));
        }
    }

    public void setPreviousColor(int color) {
        previousColor = color;
    }

    public void setPreviousAlpha(int alpha) {
        previousAlpha = alpha;
    }


    public int getPaintColor() {
        return paint.getColor();
    }

    public float getPaintStrokeWidth() {
        return paint.getStrokeWidth();
    }

    public int getPaintAlpha() {
        return Color.alpha(paint.getColor());
    }

    public int getPreviousAlpha() {
        return Color.alpha(previousAlpha);
    }

    public void setEraserMode(boolean isEraser) {
        if (this.isEraserMode != isEraser) {
            this.isEraserMode = isEraser;
            invalidate();
        }
    }
    public void clear() {
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }
}