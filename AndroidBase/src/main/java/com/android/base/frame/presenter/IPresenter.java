package com.android.base.frame.presenter;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface IPresenter {

    void onCreate(Object view, Context context);

    void onDestroy(boolean isFinal);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onBackPressed();

    void onResume();

    void onPause();
}
