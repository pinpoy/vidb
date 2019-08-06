package com.moogos.spacex.manager;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ProjectName: ActivityStack
 * Description: activity 栈管理
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/23 14:49
 */
@SuppressWarnings("ALL")
public class ActivityStack {
    public List<Activity> activityList = new ArrayList<Activity>();

    // static innerClass
    private static class ActivityStackHolder {
        private static final ActivityStack INSTANCE = new ActivityStack();
    }

    private ActivityStack() {
    }

    /**
     * single instance
     *
     * @return
     */
    public static ActivityStack getInstanse() {
        return ActivityStackHolder.INSTANCE;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void removeAll() {
        for (Activity aty : activityList) {
            if (null != aty) {
                aty.finish();
            }
        }
    }

    /**
     * 退出APP
     */
    public void exit() {
        removeAll();
        exitApp();
    }

    private void exitApp() {
        Process.killProcess(Process.myPid());
        System.exit(0);
    }


    /**
     * 根据class name获取activity
     *
     * @param name
     * @return
     */
    public Activity getActivityByClassName(String name) {
        for (Activity ac : activityList) {
            if (ac.getClass().getName().indexOf(name) >= 0) {
                return ac;
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public Activity getActivityByClass(Class cs) {
        for (Activity ac : activityList) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        removeActivity(activity);
        activity.finish();
    }


    /**
     * 弹出activity到
     *
     * @param cs
     */
    @SuppressWarnings("rawtypes")
    public void popUntilActivity(Class... cs) {
        List<Activity> list = new ArrayList<Activity>();
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity ac = activityList.get(i);
            boolean isTop = false;
            for (int j = 0; j < cs.length; j++) {
                if (ac.getClass().equals(cs[j])) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            } else break;
        }
        for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext(); ) {
            Activity activity = iterator.next();
            popActivity(activity);
        }
    }
}
