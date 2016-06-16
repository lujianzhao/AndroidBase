package com.android.base.frame.activity.impl;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/5/13.
 */
public interface ActivityPresenterImpl {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onBackPressed();

}
