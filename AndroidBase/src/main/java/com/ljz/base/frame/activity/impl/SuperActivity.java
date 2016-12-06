package com.ljz.base.frame.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ljz.base.common.utils.InputMethodUtils;
import com.ljz.base.frame.AppManager;
import com.ljz.base.frame.BaseApplication;
import com.ljz.base.frame.activity.IBaseActivity;
import com.ljz.base.netstate.NetworkStateReceiver;
import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/13 16:27
 * 描述:
 */
public abstract class SuperActivity extends SupportActivity implements IBaseActivity, LifecycleProvider<ActivityEvent> {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();

    protected abstract int getContentViewId();

    protected abstract void onInitView(Bundle savedInstanceState);

    protected abstract void onInitData();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return mLifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(mLifecycleSubject);
    }


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
        ButterKnife.bind(this);
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        mLifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }


    @Override
    @CallSuper
    protected void onPause() {
        InputMethodUtils.hideSoftInput(this);
        super.onPause();
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this);

        ButterKnife.unbind(this);

        Glide.get(this).clearMemory();

        AppManager.getAppManager().finishActivity(this);
        if (isFinishing()) {
            if (!AppManager.getAppManager().has()) {
                if (getApplication() instanceof BaseApplication) {
                    ((BaseApplication) getApplication()).onDestory();
                }
            }
        }
    }

    public void finishActivity() {
        finish();
    }

    public void finishActivity(int resultCode) {
        setResult(resultCode);
        finish();
    }


    public void finishActivity(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void gotoActivity(Class<? extends Activity> clazz) {
        this.gotoActivity(clazz, null, false);
    }

    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        this.gotoActivity(clazz, bundle, false);
    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        this.gotoActivity(clazz, null, finish);
    }


    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, boolean finish) {
        this.gotoActivity(clazz, bundle, 0, finish);
    }


    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, int flags, boolean finish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        this.gotoActivityForResult(clazz, requestCode, null);
    }

    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle) {
        this.gotoActivityForResult(clazz, requestCode, bundle,0);
    }

    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle,int flags) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        startActivityForResult(intent, requestCode);
    }



}
