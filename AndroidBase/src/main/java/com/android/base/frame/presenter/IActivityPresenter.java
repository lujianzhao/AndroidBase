package com.android.base.frame.presenter;

import android.content.Intent;

/**
 * Created by Administrator on 2016/5/13.
 */
public interface IActivityPresenter {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onBackPressed();

}
