package com.android.base.frame.fragment.impl;

import android.content.Intent;

/**
 * Created by Administrator on 2016/5/17.
 */
public interface FragmentPresenterImpl {

    void onActivityCreated();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
