
package com.jrm.skype.util;

import java.util.Timer;
import java.util.TimerTask;
import com.jrm.skype.ui.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool SOUND_POOL = null;

    private static int SOUND_MSG_ID = -1;

    private static int SOUND_CALLIN_ID = -1;

    private static int SOUND_CALLOUT_ID = -1;

    private static int SOUND_REQUEST_ID = -1;

    private static int STREAM_CALLIN_ID = -1;

    private static int STREAM_CALLOUT_ID = -1;

    private static boolean IS_MSG_RINGING = false;

    private final static long MSG_TIME_SPACE = 4000;

    private static Timer SOUND_TIMER = null;

    public static void loadSound(Context contex) {
        if(null == contex){
            return;
        }
        
        if (null == SOUND_POOL) {
            SOUND_POOL = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
            SOUND_MSG_ID = SOUND_POOL.load(contex, R.raw.msg, 1);
            SOUND_CALLIN_ID = SOUND_POOL.load(contex, R.raw.callin, 1);
            SOUND_REQUEST_ID = SOUND_POOL.load(contex, R.raw.request, 1);
            SOUND_CALLOUT_ID = SOUND_POOL.load(contex, R.raw.callout, 1);
        }
    }

    /**
     * play when msg or vm come ,and 4 seconds space
     * @param contex
     */
    public static void playMsgSound(Context contex) {
        if (IS_MSG_RINGING) {
            return;
        }
        IS_MSG_RINGING = true;

        if (null == SOUND_POOL) {
            loadSound(contex);
        }
        SOUND_POOL.play(SOUND_MSG_ID, 1, 1, 0, 0, 1);

        if (null == SOUND_TIMER) {
            SOUND_TIMER = new Timer();
        }

        SOUND_TIMER.schedule(new TimerTask() {

            @Override
            public void run() {
                IS_MSG_RINGING = false;
                SOUND_TIMER = null;
            }
        }, MSG_TIME_SPACE, MSG_TIME_SPACE);
    }

    public static void playRequestSound(Context contex) {
        if (null == SOUND_POOL) {
            loadSound(contex);
        }
        SOUND_POOL.play(SOUND_REQUEST_ID, 1, 1, 0, 1, 1);
    }

    public static void playCallinSound(Context contex) {
        if (null == SOUND_POOL) {
            loadSound(contex);
        }
        float volume = (float) (SkypePrefrences.getRingVolume(contex) * 1.0 / 100);
        STREAM_CALLIN_ID = SOUND_POOL.play(SOUND_CALLIN_ID, volume, volume, 0, -1, 1);
    }

    public static void stopCallinSound() {
        if (null == SOUND_POOL) {
            return;
        }
        SOUND_POOL.stop(STREAM_CALLIN_ID);
    }

    public static void playCalloutSound(Context contex ) {
        if (null == SOUND_POOL) {
            loadSound(contex);
        }
        STREAM_CALLOUT_ID = SOUND_POOL.play(SOUND_CALLOUT_ID, 1, 1, 0, -1,1);
    }

    public static void stopCalloutSound() {
        if (null == SOUND_POOL) {
            return;
        }
        SOUND_POOL.stop(STREAM_CALLOUT_ID);
    }

    public static void release() {
        if (null != SOUND_POOL) {
            SOUND_POOL.release();
            SOUND_POOL = null;
            SOUND_MSG_ID = -1;
            SOUND_CALLIN_ID = -1;
            SOUND_CALLOUT_ID = -1;
            SOUND_REQUEST_ID = -1;
        }
    }

    public static void setRingVol(int value) {
        if (null != SOUND_POOL && 0 <= value && value <= 100 && STREAM_CALLIN_ID != -1) {
            float volume = (float) (value * 1.0 / 100);
            SOUND_POOL.setVolume(STREAM_CALLIN_ID, volume, volume);
        }
    }
}
