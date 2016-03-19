
package com.jrm.skype.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author andy.liu 
 * A TextView for record the time
 */
public class SKTimerTextView extends TextView {
    private Handler mHandler;
    
    private Timer mTimer;

    private long mDuration;
    
    private boolean mStarted;

    private String mDurationStr;

    private Calendar mCalendar;

    private java.text.SimpleDateFormat mDateFormat;

    public SKTimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler();
        mTimer = new Timer();
        mDuration = 0;

        mCalendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("HH:mm:ss");
        mStarted = false;
    }

    /**
     * create a timer to record the time
     */
    public void startRecordTime() {
        if (mStarted)
            return;
        else
            mStarted = true;
        
        if (null == mTimer)
            mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*
                 * seconds to string
                 */
                mDuration++;

                mCalendar.setTimeInMillis(1000 * mDuration);
                mCalendar.set(Calendar.HOUR_OF_DAY, (int) mDuration / 3600);

                mDurationStr = mDateFormat.format(mCalendar.getTime());

                // change the UI must in the UI thread
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        SKTimerTextView.this.setText(mDurationStr);
                    }
                });
            }
        }, 0, 1000);
    }

    /**
     * cancel the timer and reset the time
     */
    public void stopRecordTime() {
        if (null != mTimer)
            mTimer.cancel();
        mTimer = null;
        mDuration = 0;
        mStarted = false;
    }

    /**
     * @return the record duration in long
     */
    public long getDurationLong() {
        return mDuration;
    }

    /**
     * @return the record duration in Sring
     */
    public String getDurationStr() {
        return mDurationStr;
    }
}
