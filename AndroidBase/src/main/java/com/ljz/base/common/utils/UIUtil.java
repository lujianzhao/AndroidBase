package com.ljz.base.common.utils;

///**
// * 作者: lujianzhao
// * 创建时间: 2016/05/09 15:24
// * 描述:
// */
//public class UIUtil {
//    /**
//     * 上下文的获取
//     *
//     * @return
//     */
//    public static BaseApplication getContext() {
//        return (BaseApplication)Base.getContext();
//    }
//
//    /**
//     * 获取资源
//     *
//     * @return
//     */
//    public static Resources getResources() {
//        return getContext().getResources();
//    }
//
//    public static String getPackageName() {
//        return getContext().getPackageName();
//    }
//
//
//    public static View inflate(int resId) {
//        return LayoutInflater.from(getContext()).inflate(resId, null);
//    }
//
//
//    public static String[] getStringArray(int resId) {
//        return getResources().getStringArray(resId);
//    }
//
//    /**
//     * 获取文字
//     */
//    public static String getString(int resId) {
//        return getResources().getString(resId);
//    }
//
//    /**
//     * 获取文字,使用占位符 例：获取改字符串<string name="app_detail_info_downloadnum">下载: %1s
//     * </string> 结果：下载：+ formatArgs
//     */
//    public static String getString(int resId, Object... formatArgs) {
//        return getResources().getString(resId, formatArgs);
//    }
//
//
//    /**
//     * 获取drawable
//     */
//    public static Drawable getDrawable(int resId) {
//        return getResources().getDrawable(resId);
//    }
//
//    /**
//     * 获取颜色
//     */
//    public static int getColor(int resId) {
//        return getResources().getColor(resId);
//    }
//
//    /**
//     * 获取颜色选择器
//     */
//    public static ColorStateList getColorStateList(int resId) {
//        return getResources().getColorStateList(resId);
//    }
//
//
//}
