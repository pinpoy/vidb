package com.moogos.spacex.util;


import android.content.Context;
import android.os.PowerManager;

public class ManageScreen {
    private static PowerManager myPM = null;
    private static boolean screenOn = false;

    private static PowerManager.WakeLock wl = null;

    public static synchronized void initialize(Context context) {
        if (myPM == null) {
            myPM = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wl = myPM.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        }
    }

    public static synchronized void lightOn(Context context) {
        initialize(context);
        if (!myPM.isScreenOn()) {
            wl.acquire();
//            Log.d("ManageScreen isScreenOn: false" );
            screenOn = true;
        }else{
//            Log.d("ManageScreen isScreenOn: true" );
        }
    }

    public static synchronized void lightOff(Context context) {
        initialize(context);
        if (screenOn) {
            wl.release();
            screenOn = false;
        }
    }
}