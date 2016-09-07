package com.android.base.frame.fragment.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.common.logutils.LogUtils;
import com.android.base.frame.fragment.IBaseFragment;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.List;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/13 16:27
 * 描述:
 */
public abstract class SuperFragment extends SupportFragment  implements IBaseFragment,EasyPermissions.PermissionCallbacks, FragmentLifecycleProvider {
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    protected View mRootView = null;

    protected abstract int getContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return mLifecycleSubject.asObservable();
    }



    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(mLifecycleSubject);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        mLifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE);
        hideSoftInput();
        super.onPause();
    }

    @Override
    public void onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(_mActivity, clazz);
        startActivity(intent);
        if (finish) {
            _mActivity.finish();
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, boolean finish) {
        Intent intent = new Intent(_mActivity, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        if (finish) {
            _mActivity.finish();
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, int flags, boolean finish) {
        Intent intent = new Intent(_mActivity, clazz);
        if (bundle != null) intent.putExtras(bundle);
        intent.addFlags(flags);
        startActivity(intent);
        if (finish) {
            _mActivity.finish();
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



}
