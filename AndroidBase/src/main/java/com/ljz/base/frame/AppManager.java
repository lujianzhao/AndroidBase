package com.ljz.base.frame;

import android.app.Activity;

import com.ljz.base.frame.activity.IBaseActivity;

import java.util.Stack;

/**
 * Created by Administrator on 2016/5/13.
 */
public class AppManager {

    private static Stack<Activity> mActivityStack;
    private static AppManager mInstance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return mActivityStack.size();
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity getTopActivity() {
        if (mActivityStack == null) {
            throw new NullPointerException("Activity stack is Null,your Activity must extend KJActivity");
        }
        if (mActivityStack.isEmpty()) {
            return null;
        }
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 查找Activity 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : mActivityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activity instanceof IBaseActivity) {
                mActivityStack.remove(activity);
                activity = null;
            }
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for ( Activity activity : mActivityStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                (mActivityStack.get(i)).finish();
            }
        }
        mActivityStack.clear();
        mActivityStack = null;
    }

    /**
     *
     * @return 是否还有activity存活
     */
    public boolean has() {
        return mActivityStack !=null && mActivityStack.size() > 0;
    }

    /**
     * 应用程序退出
     */
    public void appExit() {
        try {
            finishAllActivity();
            mInstance = null;
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
}
