package com.ljz.base.frame;

import android.app.Activity;
import android.app.Application;

import com.ljz.base.common.assist.Check;
import com.ljz.base.common.logutils.LogLevel;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.utils.HandlerUtil;
import com.ljz.base.frame.activity.IBaseActivity;
import com.ljz.base.netstate.NetChangeObserver;
import com.ljz.base.netstate.NetWorkUtil;
import com.ljz.base.netstate.NetworkStateReceiver;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {

    private NetChangeObserver mNetChangeObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        Base.initialize(this);

        initLogUtils();

        initRecovery();

        LogUtils.d("BaseApplication onCreate");

        AutoLayoutConifg.getInstance().useDeviceSize();

        registerNetWorkStateListener();// 注册网络状态监测器


    }

    /**
     * 初始化程序错误时,APP自动恢复
     */
    private void initRecovery() {
        Recovery.getInstance()
                .debug(isDebug())
                .recoverInBackground(false)
                .recoverStack(true)
                .callback(new RecoveryCallback(){

                    @Override
                    public void stackTrace(String stackTrace) {
                        onErrorHappens(stackTrace);
                    }

                    @Override
                    public void cause(String cause) {

                    }

                    @Override
                    public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {

                    }

                    @Override
                    public void throwable(Throwable throwable) {
                        LogUtils.e(throwable);
                    }
                })
                .silent(true, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this);


        List<Class<? extends Activity>> skipActivities = getSkipActivities();
        if (!Check.isEmpty(skipActivities)) {
            for (Class clazz : skipActivities) {
                Recovery.getInstance().skip(clazz);
            }
        }
    }

    /**
     *
     * @return 不进行错误捕获的 Activitys
     */
    public List<Class<? extends Activity>> getSkipActivities() {
        return new ArrayList<>();
    }

    /**
     * 当错误发生后
     * @param stackTrace
     */
    protected void onErrorHappens(String stackTrace) {

    }

    /**
     * 当前是否是Debug模式
     * @return
     */
    protected boolean isDebug() {
        return true;
    }

    /**
     * Log的TAG
     * @return
     */
    protected String getTag() {
        return "AndroidBase";
    }

    private void initLogUtils() {
        LogUtils.getLogConfig().configAllowLog(isDebug()).configTagPrefix(getTag()).configShowBorders(true).configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}").configLevel(LogLevel.TYPE_VERBOSE);
    }

    /**
     * 注册网络连接观察者
     */
    private void registerNetWorkStateListener() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetworkConnect(NetWorkUtil.NetWorkType type) {
                try {
                    BaseApplication.this.onNetWorkConnect(type);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onNetworkDisConnect() {
                try {
                    BaseApplication.this.onNetWorkDisConnect();
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
    private void onNetWorkDisConnect() {
        Activity mCurrentActivity = AppManager.getAppManager().topActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof IBaseActivity) {
                ((IBaseActivity) mCurrentActivity).onNetWorkDisConnect();
            }
        }
    }

    /**
     * 网络连接连接时通知
     */
    private void onNetWorkConnect(NetWorkUtil.NetWorkType type) {
        Activity mCurrentActivity = AppManager.getAppManager().topActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof IBaseActivity) {
                ((IBaseActivity) mCurrentActivity).onNetWorkConnect(type);
            }
        }
    }

    /**
     * 在程序内部关闭时，调用此方法
     */
    public void onDestory() {
        LogUtils.d("BaseApplication destory");

        HandlerUtil.removeCallbacksAndMessages();

        AppManager.getAppManager().appExit();

        //注销网络监听
        unRegisterNetWorkStateListener();

    }


}
