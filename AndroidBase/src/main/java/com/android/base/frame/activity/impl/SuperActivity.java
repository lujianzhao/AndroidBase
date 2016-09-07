package com.android.base.frame.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.View;

import com.android.base.common.logutils.LogUtils;
import com.android.base.common.utils.InputMethodUtils;
import com.android.base.frame.AppManager;
import com.android.base.frame.BaseApplication;
import com.android.base.frame.activity.IBaseActivity;
import com.android.base.netstate.NetWorkUtil;
import com.android.base.netstate.NetworkStateReceiver;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/13 16:27
 * 描述:
 */
public abstract class SuperActivity extends SupportActivity implements IBaseActivity, EasyPermissions.PermissionCallbacks{

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    protected abstract int getContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null)
            return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(getContentViewId());
        initActionBar();
        ButterKnife.bind(this);
        NetworkStateReceiver.registerNetworkStateReceiver(this);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }

    @Override
    @CallSuper
    protected void onPause() {
        InputMethodUtils.hideSoftInput(this);
        super.onPause();
    }


    @Override
    @CallSuper
    protected void onDestroy() {
        ButterKnife.unbind(this);
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (isFinishing()) {
            if (!AppManager.getAppManager().has()) {
                if (getApplication() instanceof BaseApplication) {
                    ((BaseApplication) getApplication()).onDestory();
                }
            }
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }


    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, boolean finish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }


    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, int flags, boolean finish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        intent.addFlags(flags);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.d("onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtils.d("onPermissionsDenied:" + requestCode + ":" + perms.size());
    }



    @Override
    public void onDisConnect() {

    }

    @Override
    public void onConnect(NetWorkUtil.NetWorkType type) {

    }


}
