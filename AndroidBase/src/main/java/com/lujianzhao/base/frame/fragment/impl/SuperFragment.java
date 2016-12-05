package com.lujianzhao.base.frame.fragment.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lujianzhao.base.frame.fragment.IBaseFragment;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/13 16:27
 * 描述:
 */
public abstract class SuperFragment extends SupportFragment implements IBaseFragment, LifecycleProvider<FragmentEvent> {

    protected View mRootView = null;

    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    /**
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 初始化View
     * @param savedInstanceState
     */
    protected abstract void onInitView(Bundle savedInstanceState);

    /**
     * 开始加载数据
     */
    protected abstract void onInitData();

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
        return RxLifecycleAndroid.bindFragment(mLifecycleSubject);
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
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(getContentViewId(), container, false);
        }
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
        super.onPause();
    }


    @Override
    public void onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        ButterKnife.unbind(this);
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mRootView = null;
        mLifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    public void finishActivity() {
        getActivity().finish();
    }

    public void finishActivity(int resultCode) {
        getActivity().setResult(resultCode);
        getActivity().finish();
    }

    public void finishActivity(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
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
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        startActivity(intent);
        if (finish) {
            getActivity().finish();
        }
    }


    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        gotoActivityForResult(clazz, requestCode, null);
    }

    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle) {
        gotoActivityForResult(clazz, requestCode, bundle, 0);
    }

    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle, int flags) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        startActivityForResult(intent, requestCode);
    }

}
