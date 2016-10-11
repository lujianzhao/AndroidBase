package com.android.base.frame.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/5/18.
 */
public interface IBaseView {

    void gotoActivity(Class<? extends Activity> clazz);

    void gotoActivity(Class<? extends Activity> clazz, Bundle bundle);

    void gotoActivity(Class<? extends Activity> clazz, boolean finish);

    void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, boolean finish);

    void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, int flags, boolean finish);

    void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode);

    void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle);

    void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle, int flags);

    void showLoadingView();

    void showEmptyView();

    void showErrorView();

    void showContentView();
}
