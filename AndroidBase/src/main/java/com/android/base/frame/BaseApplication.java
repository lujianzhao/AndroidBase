package com.android.base.frame;

import android.app.Activity;
import android.app.Application;

import com.android.base.common.logutils.LogLevel;
import com.android.base.common.logutils.LogUtils;
import com.android.base.common.utils.HandlerUtil;
import com.android.base.frame.activity.IBaseActivity;
import com.android.base.netstate.NetChangeObserver;
import com.android.base.netstate.NetWorkUtil;
import com.android.base.netstate.NetworkStateReceiver;

public class BaseApplication extends Application {

    public static final boolean isAllowLog = true;

    private NetChangeObserver mNetChangeObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        Base.initialize(this);

        initLogUtils();
        LogUtils.d("BaseApplication onCreate");

        registerNetWorkStateListener();// 注册网络状态监测器

    }

    private void initLogUtils() {
        LogUtils.getLogConfig().configAllowLog(isAllowLog).configTagPrefix("Yike-").configShowBorders(true).configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}").configLevel(LogLevel.TYPE_VERBOSE);
    }

    /**
     * 注册网络连接观察者
     */
    private void registerNetWorkStateListener() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.NetWorkType type) {
                super.onConnect(type);
                try {
                    BaseApplication.this.onConnect(type);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                try {
                    BaseApplication.this.onDisConnect();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        NetworkStateReceiver.registerObserver(mNetChangeObserver);
    }

    /**
     * 注销网络连接观察者
     */
    private void unRegisterNetWorkStateListener() {
        if (mNetChangeObserver != null) {
            NetworkStateReceiver.removeRegisterObserver(mNetChangeObserver);
            mNetChangeObserver = null;
        }
    }

    /**
     * 当前没有网络连接通知
     */
    public void onDisConnect() {
        Activity mCurrentActivity = AppManager.getAppManager().topActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof IBaseActivity) {
                ((IBaseActivity) mCurrentActivity).onDisConnect();
            }
        }
    }

    /**
     * 网络连接连接时通知
     */
    protected void onConnect(NetWorkUtil.NetWorkType type) {
        Activity mCurrentActivity = AppManager.getAppManager().topActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof IBaseActivity) {
                ((IBaseActivity) mCurrentActivity).onConnect(type);
            }
        }
    }

    /**
     * 在程序内部关闭时，调用此方法
     */
    public void onDestory() {
        LogUtils.d("BaseApplication destory");
        unRegisterNetWorkStateListener();

        if (HandlerUtil.HANDLER != null) {
            HandlerUtil.HANDLER.removeCallbacksAndMessages(null);
        }
    }

}
