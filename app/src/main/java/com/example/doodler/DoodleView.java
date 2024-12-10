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
import java.util.Stack;

public class DoodleView extends View {
    private Bitmap mBitmap;
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private boolean isEraserMode = false;
    private Stack<Bitmap> undoStack = new Stack<>();
    private Stack<Bitmap> redoStack = new Stack<>();

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
                saveState();
                path.reset();
                path.moveTo(x, y);
                if (isEraserMode) {
                    if (paint.getXfermode() == null) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    }
                } else {
                    if (paint.getXfermode() != null) {
                        paint.setXfermode(null);
                    }
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

    public int getPaintColor() {
        return paint.getColor();
    }

    public float getPaintStrokeWidth() {
        return paint.getStrokeWidth();
    }

    public int getPaintAlpha() {
        return Color.alpha(paint.getColor());
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

    private void saveState() {
        Bitmap snapshot = mBitmap.copy(mBitmap.getConfig(), true);
        undoStack.push(snapshot);
        redoStack.clear();
        ((MainActivity) getContext()).updateUndoRedoButtons();
    }

    public void undo() {
            redoStack.push(mBitmap.copy(mBitmap.getConfig(), true));
            mBitmap = undoStack.pop();
            canvas = new Canvas(mBitmap);
            invalidate();
    }

    public void redo() {
            undoStack.push(mBitmap.copy(mBitmap.getConfig(), true));
            mBitmap = redoStack.pop();
            canvas = new Canvas(mBitmap);
            invalidate();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}