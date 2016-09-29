package com.android.base.frame;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Base helps to get {@link Context}, {@link Resources}, {@link AssetManager}, {@link Configuration} and {@link DisplayMetrics} in any class.
 *
 * @author Leonardo Taehwan Kim
 */
public class Base {

    private static Context mContext;

    public static void initialize(@NonNull Context context) {
        mContext = context;
    }

    public static void release() {
        mContext = null;
    }

    public static Context getContext() {
        synchronized (Base.class) {
            return mContext.getApplicationContext();
        }
    }

    public static Resources getResources() {
        return Base.getContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return Base.getContext().getTheme();
    }

    public static AssetManager getAssets() {
        return Base.getContext().getAssets();
    }

    public static Configuration getConfiguration() {
        return Base.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return Base.getResources().getDisplayMetrics();
    }
}
// TODO: Thread safety
// TODO: ripple, bitmap, time, contact list, picture list, video list, connectivity, wake lock, screen lock/off/on, get attributes, cookie, audio
// TODO: keystore
// TODO: http://jo.centis1504.net/?p=1189
// TODO: Test codes