package org.cuatrovientos.blablacar.activities.profile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircularSlider extends View {

    private static final int SWEEP_ANGLE = 360;
    private static final int MAX_VALUE = 50;
    private static final int SNAP_INCREMENT = 5;
    private boolean isTracking = false; 

    
    private Paint circlePaintBase;
    private Paint circlePaintBaseBorder;
    private Paint circlePaintFilled;
    private Paint indicatorPaint;
    private Paint textPaint;

    
    private RectF circleBounds;

    
    private float sliderValue = 0;
    private float addingMoney = 0; 
    public CircularSlider(Context context) {
        super(context);
        init();
    }

    public CircularSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaintBase = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaintBase.setColor(Color.parseColor("#E3E4E6"));
        circlePaintBase.setStrokeWidth(50);
        circlePaintBase.setStyle(Paint.Style.STROKE);

        circlePaintBaseBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaintBaseBorder.setColor(Color.parseColor("#BDBFBE"));
        circlePaintBaseBorder.setStrokeWidth(55);
        circlePaintBaseBorder.setStyle(Paint.Style.STROKE);

        circlePaintFilled = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaintFilled.setColor(Color.parseColor("#4B815D"));
        circlePaintFilled.setStrokeWidth(50);
        circlePaintFilled.setStyle(Paint.Style.STROKE);
        circlePaintFilled.setStrokeCap(Paint.Cap.ROUND);

        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setStrokeWidth(1);
        indicatorPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        circleBounds = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        addingMoney = sliderValue * MAX_VALUE;

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        
        float minDimension = Math.min(centerX, centerY);
        float radiusReduction = minDimension * 0.5f;
        float radius = Math.min(centerX, centerY) - radiusReduction;
        circleBounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        
        float borderOffset = (circlePaintBaseBorder.getStrokeWidth() - circlePaintBase.getStrokeWidth()) / 2;
        float adjustedRadiusForBorder = radius - borderOffset;
        RectF borderBounds = new RectF(centerX - adjustedRadiusForBorder, centerY - adjustedRadiusForBorder,
                centerX + adjustedRadiusForBorder, centerY + adjustedRadiusForBorder);

        
        canvas.drawArc(borderBounds, -90, SWEEP_ANGLE, false, circlePaintBaseBorder);
        canvas.drawArc(circleBounds, -90, SWEEP_ANGLE, false, circlePaintBase);
        float sweepAngleFilled = sliderValue * SWEEP_ANGLE;
        canvas.drawArc(circleBounds, -90, sweepAngleFilled, false, circlePaintFilled);

        
        float indicatorPosX = (float) (centerX + radius * Math.cos(Math.toRadians(sliderValue * SWEEP_ANGLE - 90)));
        float indicatorPosY = (float) (centerY + radius * Math.sin(Math.toRadians(sliderValue * SWEEP_ANGLE - 90)));

        if (sliderValue == 0) {
            Paint smallGreenCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            smallGreenCirclePaint.setColor(Color.parseColor("#4B815D")); 
            smallGreenCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(indicatorPosX, indicatorPosY, 25, smallGreenCirclePaint); 

            Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            trianglePaint.setColor(Color.WHITE); 
            trianglePaint.setStyle(Paint.Style.FILL);
            Path trianglePath = new Path();
            trianglePath.moveTo(indicatorPosX - 10, indicatorPosY - 10); 
            trianglePath.lineTo(indicatorPosX + 15, indicatorPosY); 
            trianglePath.lineTo(indicatorPosX - 10, indicatorPosY + 10); 
            trianglePath.close();
            canvas.drawPath(trianglePath, trianglePaint);
        }  else if (sliderValue == 1) {
            
            Paint smallGreenCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            smallGreenCirclePaint.setColor(Color.parseColor("#4B815D")); 
            smallGreenCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(indicatorPosX, indicatorPosY, 25, smallGreenCirclePaint);

            
            Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            trianglePaint.setColor(Color.WHITE); 
            trianglePaint.setStyle(Paint.Style.FILL);
            Path trianglePath = new Path();
            trianglePath.moveTo(indicatorPosX + 10, indicatorPosY - 10); 
            trianglePath.lineTo(indicatorPosX - 15, indicatorPosY); 
            trianglePath.lineTo(indicatorPosX + 10, indicatorPosY + 10); 
            trianglePath.close();
            canvas.drawPath(trianglePath, trianglePaint);
        } else {
            
            canvas.drawCircle(indicatorPosX, indicatorPosY, 15, indicatorPaint);
        }

        
        Paint topLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topLabelPaint.setColor(Color.BLACK);
        topLabelPaint.setTextSize(25);
        topLabelPaint.setTextAlign(Paint.Align.CENTER);

        
        textPaint.setTextSize(85);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setColor(Color.parseColor("#4B815D"));
        String labelValue = String.format("€%.2f", addingMoney);
        canvas.drawText(labelValue, centerX, centerY, textPaint);

        
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#E3E4E6"));
        linePaint.setStrokeWidth(4);
        float lineLength = 200;
        canvas.drawLine(centerX - lineLength / 2, centerY + radius / 8,
                centerX + lineLength / 2, centerY + radius / 8, linePaint);
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - getWidth() / 2;
        float y = event.getY() - getHeight() / 2;
        double distanceFromCenter = Math.sqrt(x * x + y * y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                
                if (distanceFromCenter > (circleBounds.width() / 2 - circlePaintBase.getStrokeWidth()) &&
                        distanceFromCenter < (circleBounds.width() / 2 + circlePaintBase.getStrokeWidth())) {
                    isTracking = true;
                    updateSliderPosition(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTracking) {
                    updateSliderPosition(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTracking = false; 
                break;
        }
        return true; 
    }

    private void updateSliderPosition(float x, float y) {
        double angle = Math.toDegrees(Math.atan2(y, x)) + 90;
        angle = angle < 0 ? angle + 360 : angle;
        float newValue = (float) angle / SWEEP_ANGLE;

        
        float snappedValue = Math.round(newValue * MAX_VALUE / SNAP_INCREMENT) * SNAP_INCREMENT;
        
        if (snappedValue > MAX_VALUE) {
            snappedValue = MAX_VALUE;
        }
        newValue = snappedValue / MAX_VALUE;

        
        if (sliderValue == 1.0f && newValue < 0.5f) {
            return; 
        }

        
        if (sliderValue < 0.5f && newValue == 1.0f) {
            return; 
        }

        
        if (sliderValue == 0f && newValue > sliderValue && newValue > 0.5f) {
            return; 
        }

        sliderValue = newValue;

        invalidate(); 
    }



    public float getAddingMoney() {
        return this.addingMoney; 
    }
}
