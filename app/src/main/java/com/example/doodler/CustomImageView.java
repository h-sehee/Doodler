package com.example.doodler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;
    private float strokeWidth;
    private int alpha;

    private int color;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        this.color = Color.BLACK;
    }

    public void updateCircle(float strokeWidth, int alpha, int color) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.alpha = alpha;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAlpha(alpha);
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float radius = strokeWidth / 2;

        canvas.drawCircle(cx, cy, radius, paint);
    }
}