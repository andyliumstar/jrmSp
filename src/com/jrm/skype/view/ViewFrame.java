package com.jrm.skype.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ViewFrame extends View {   
    private int mStartOfX;

    private int mStopOfX;

    private int mStartOfY;

    private int mStopOfY;

    private Paint mPaint;
    
    public ViewFrame(Context context,int x ,int y, int l) {
        super(context);
        mPaint = new Paint();
        mStartOfX = x;
        mStartOfY = y;
        mStopOfX = x + l;
        mStopOfY = y + l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x00000000); 
        
        mPaint.setColor(0xff800080);  
        mPaint.setStrokeWidth(2);  
        
        canvas.drawLine(mStartOfX, mStartOfY, mStopOfX, mStartOfY, mPaint);  
        canvas.drawLine(mStopOfX, mStartOfY, mStopOfX, mStopOfY, mPaint);  
        canvas.drawLine(mStartOfX, mStartOfY, mStartOfX, mStopOfY, mPaint);  
        canvas.drawLine(mStartOfX, mStopOfY, mStopOfX, mStopOfY, mPaint);  
    }

}
