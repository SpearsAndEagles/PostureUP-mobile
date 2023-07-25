package com.example.postureup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class AngleView extends View {

    private Paint mPaint;
    private float mAngle;

    public AngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = Math.min(cx, cy) - 20;

        // Draw the circle
        canvas.drawCircle(cx, cy, radius, mPaint);

        // Draw the filled arc
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(new RectF(cx - radius, cy - radius, cx + radius, cy + radius), -90, mAngle, true, mPaint);
    }

    public void setAngle(float angle) {
        mAngle = angle;
        invalidate();
    }
}
