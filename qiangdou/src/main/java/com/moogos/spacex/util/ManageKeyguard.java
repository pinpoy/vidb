package com.moogos.spacex.util;


import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;

public class ManageKeyguard {
    private static KeyguardManager myKM = null;
    private static KeyguardLock myKL = null;
    private static boolean disLocked = false;


    public static synchronized void initialize(Context context) {
        if (myKM == null) {
            myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        }
    }

    public static synchronized void disableKeyguard(Context context) {
        // myKM = (KeyguardManager)
        // context.getSystemService(Context.KEYGUARD_SERVICE);
        initialize(context);

        if (myKM.inKeyguardRestrictedInputMode()) {
            myKL = myKM.newKeyguardLock("ManageKeyGuard");
            myKL.disableKeyguard();
            disLocked = true;

        } else {
            myKL = null;
        }
    }

    public static synchronized boolean inKeyguardRestrictedInputMode(Context context) {
        initialize(context);
        return myKM.inKeyguardRestrictedInputMode();


    }

    public static synchronized void reenableKeyguard() {
        if (disLocked && myKM != null) {
            if (myKL != null) {
                myKL.reenableKeyguard();
                myKL = null;
                disLocked = false;
            }
        }
    }

    public static synchronized boolean unlockByMe(){
        return disLocked;
    }
//    public static synchronized void exitKeyguardSecurely(
//            final LaunchOnKeyguardExit callback) {
//        if (inKeyguardRestrictedInputMode()) {
//            myKM.exitKeyguardSecurely(new OnKeyguardExitResult() {
//                public void onKeyguardExitResult(boolean success) {
//                    reenableKeyguard();
//                    if (success) {
//                        callback.LaunchOnKeyguardExitSuccess();
//                    } else {
//
//                    }
//                }
//            });
//        } else {
//            callback.LaunchOnKeyguardExitSuccess();
//        }
//    }
//
//    public interface LaunchOnKeyguardExit {
//        public void LaunchOnKeyguardExitSuccess();
//    }
}