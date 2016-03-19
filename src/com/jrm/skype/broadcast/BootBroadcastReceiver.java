
package com.jrm.skype.broadcast;

import com.jrm.skype.constant.SKYPECONSTANT;
import com.jrm.skype.service.SkypeService;
import com.jrm.skype.util.SkypePrefrences;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * @author andy.liu 
 * A receiver starts when start the System, it will sign in skype by background if user set the option
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (SkypePrefrences.isStartSKTWhenTVStart(context)) {
                    intent.setClass(context, SkypeService.class);
                    context.startService(intent);
                    context.startService(new Intent(
                            SKYPECONSTANT.SKYPEACTION.SKYPE_EXCEPTION_SERVICE));
                }
            }
        }, 5000);

    }
}
