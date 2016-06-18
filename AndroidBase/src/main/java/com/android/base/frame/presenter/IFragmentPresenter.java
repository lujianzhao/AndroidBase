package com.android.base.frame.presenter;

import android.content.Intent;

/**
 * Created by Administrator on 2016/5/17.
 */
public interface IFragmentPresenter {

    void onActivityCreated();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onBackPressed();

}
