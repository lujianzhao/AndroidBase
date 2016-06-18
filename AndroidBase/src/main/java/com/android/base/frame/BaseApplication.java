package com.android.base.frame;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.android.base.common.logutils.LogLevel;
import com.android.base.common.logutils.LogUtils;
import com.android.base.common.utils.FileUtil;
import com.android.base.common.utils.HandlerUtil;
import com.android.base.frame.activity.IBaseActivity;
import com.android.base.netstate.NetChangeObserver;
import com.android.base.netstate.NetWorkUtil;
import com.android.base.netstate.NetworkStateReceiver;

import java.io.File;

public class BaseApplication extends Application {

    private final boolean isAllowLog = true;

    private NetChangeObserver mNetChangeObserver;

    public File mNetCacheFile;

    public int mMaxCacheSize = 50 * 1024 * 1024;

    @Override
    public void onCreate() {
        super.onCreate();

        Base.initialize(this);

        initLogUtils();
        LogUtils.d("BaseApplication onCreate");

        // 初始化App目录
        initAppDir();

        registerNetWorkStateListener();// 注册网络状态监测器
    }


    /* 文件缓存的目录 */
    public String mAppDir;
    public String mPicturesDir;
    public String mVoicesDir;
    public String mVideosDir;
    public String mFilesDir;

    private void initAppDir() {
        File file = getExternalFilesDir(null);
        if (file == null) {
            file = new File(FileUtil.getExternalStoragePath());
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        mAppDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file == null) {
            file = new File(FileUtil.getExternalStoragePath() + File.separator + Environment.DIRECTORY_PICTURES);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        mPicturesDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (file == null) {
            file = new File(FileUtil.getExternalStoragePath() + File.separator + Environment.DIRECTORY_MUSIC);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        mVoicesDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (file == null) {
            file = new File(FileUtil.getExternalStoragePath() + File.separator + Environment.DIRECTORY_MOVIES);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        mVideosDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (file == null) {
            file = new File(FileUtil.getExternalStoragePath() + File.separator + Environment.DIRECTORY_DOWNLOADS);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        mFilesDir = file.getAbsolutePath();
    }


    private void initLogUtils() {
        LogUtils.getLogConfig().configAllowLog(isAllowLog).configTagPrefix("yike-").configShowBorders(true)
                .configLevel(LogLevel.TYPE_VERBOSE);
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

    public File getNetCacheFile() {
        if (mNetCacheFile == null) {
            mNetCacheFile = new File(getCacheDir() + "/netcache");
        }
        if (!mNetCacheFile.exists()) {
            if (mNetCacheFile.mkdirs()) {
                LogUtils.d("创建了 mNetCacheFile 文件夹 成功 : " + mNetCacheFile.getAbsolutePath());
            } else {
                LogUtils.e("创建了 mNetCacheFile 文件夹 失败 : " + mNetCacheFile.getAbsolutePath());
            }
        }
        return mNetCacheFile;
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
