package com.ljz.base.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringDef;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    @StringDef({STATUS_BAR_HEIGHT, NAVIGATION_BAR_HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DimenName {}

    /**
     * 系统状态栏高度
     */
    public static final String STATUS_BAR_HEIGHT = "status_bar_height";

    /**
     * 系统底部导航栏高度
     */
    public static final String NAVIGATION_BAR_HEIGHT = "navigation_bar_height";

    /**
     * 反射获取系统的属性
     * @param context  context
     * @param dimenName visibility One of {@link #STATUS_BAR_HEIGHT}, {@link #NAVIGATION_BAR_HEIGHT}.
     * @return 对应的系统属性
     */
    public static int getSystemComponentDimen(Context context, @DimenName String dimenName){
        int statusHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String heightStr = clazz.getField(dimenName).get(object).toString();
            float height = Float.parseFloat(heightStr);
            statusHeight = dip2px(context,height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 只支持API 17或以上的版本，而且只考虑竖屏的结果
     * @param wm WindowManager
     * @return 是否有NavigationBar显示
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static  boolean hasNavigationBarShow(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        //获取整个屏幕的高度
        display.getRealMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels;
        //获取内容展示部分的高度
        outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int heightPixels2 = outMetrics.heightPixels;
        int h = heightPixels - heightPixels2;
        return  h > 0;
    }
}