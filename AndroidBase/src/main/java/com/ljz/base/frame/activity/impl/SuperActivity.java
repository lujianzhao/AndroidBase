package com.ljz.base.frame.activity.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.ljz.base.common.utils.DensityUtil;
import com.ljz.base.common.utils.InputMethodUtils;
import com.ljz.base.frame.AppManager;
import com.ljz.base.frame.BaseApplication;
import com.ljz.base.frame.activity.IBaseActivity;
import com.ljz.base.netstate.NetworkStateReceiver;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportActivity;

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
        return mLifecycleSubject.hide();
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //新版本的转场动画
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        onBeforeSetContentView();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        mLifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    protected void onBeforeSetContentView() {
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
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();

    }

    @Override
    @CallSuper
    protected void onStop() {
        mLifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        ButterKnife.unbind(this);
        Glide.get(this).clearMemory();
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();

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

    public boolean isOnTaskTop() {
        return AppManager.getAppManager().getTopActivity() == this;
    }


    /**
     * 沉浸式，必须在setContentView调用该方法
     */
    protected void initTranslucent() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置虚拟导航栏为透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 沉浸式
     *
     * @param toolbar                 ToolBar的view
     * @param bottomNavigationBar     自定义BottomNavigationBar的View
     * @param translucentPrimaryColor 颜色
     */
    @SuppressLint("NewApi")
    protected void setOrChangeTranslucentColor(View toolbar, View bottomNavigationBar, int translucentPrimaryColor) {
        //判断版本,如果[4.4,5.0)就设置状态栏和导航栏为透明
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (toolbar != null) {
                //1.先设置toolbar的高度
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                int statusBarHeight = DensityUtil.getSystemComponentDimen(this, DensityUtil.STATUS_BAR_HEIGHT);
                params.height += statusBarHeight;
                toolbar.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                //设置顶部的颜色
                toolbar.setBackgroundColor(translucentPrimaryColor);
            }
            if (bottomNavigationBar != null) {
                //解决低版本4.4+的虚拟导航栏的
                if (DensityUtil.hasNavigationBarShow(getWindowManager())) {
                    ViewGroup.LayoutParams p = bottomNavigationBar.getLayoutParams();
                    p.height += DensityUtil.getSystemComponentDimen(this, DensityUtil.NAVIGATION_BAR_HEIGHT);
                    bottomNavigationBar.setLayoutParams(p);
                    //设置底部导航栏的颜色
                    bottomNavigationBar.setBackgroundColor(translucentPrimaryColor);
                } else {
                    ViewGroup.LayoutParams p = bottomNavigationBar.getLayoutParams();
                    p.height = 0;
                    bottomNavigationBar.setLayoutParams(p);
                }
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(translucentPrimaryColor);
            getWindow().setStatusBarColor(translucentPrimaryColor);
        }
    }

}
