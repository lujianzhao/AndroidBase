package com.ljz.androidbasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljz.androidbasedemo.db.view.DBActivity;
import com.ljz.androidbasedemo.http.HttpActivity;
import com.ljz.androidbasedemo.recycler.RecyclerActivity;
import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.utils.TelephoneUtil;
import com.ljz.base.frame.activity.impl.BaseActivity;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.container)
    LinearLayout mContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        TelephoneUtil.getDeviceId(this).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String deviceId) {
                        LogUtils.d("设备唯一ID：" + deviceId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("设备唯一ID出错:" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 权限请求的Demo,
     */
    private void demo() {
        //RxBinding
//        RxView.clicks(findViewById(enableCamera))
//                // Ask for permissions when button is clicked
//                .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
//                .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new Action1<Boolean>() {
//                               @Override
//                               public void call(Boolean granted) {
//                                   Log.i(TAG, "Permission result " + granted);
//                                   if (granted) {
//                                       releaseCamera();
//                                       camera = Camera.open(0);
//                                       try {
//                                           camera.setPreviewDisplay(surfaceView.getHolder());
//                                           camera.startPreview();
//                                       } catch (IOException e) {
//                                           Log.e(TAG, "Error while trying to display the camera preview", e);
//                                       }
//                                   } else {
//                                       Toast.makeText(MainActivity.this,
//                                               "Permission denied, can't enable the camera",
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           },
//                        new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable t) {
//                                Log.e(TAG, "onError", t);
//                            }
//                        },
//                        new Action0() {
//                            @Override
//                            public void call() {
//                                Log.i(TAG, "OnComplete");
//                            }
//                        });

        //RxPermissions
//        RxPermissions.getInstance(this)
//                .request(Manifest.permission.CAMERA)
//                .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new Action1<Boolean>() {
//                               @Override
//                               public void call(Boolean granted) {
//                                   Log.i(TAG, "Permission result " + granted);
//                                   if (granted) {
//                                       releaseCamera();
//                                       camera = Camera.open(0);
//                                       try {
//                                           camera.setPreviewDisplay(surfaceView.getHolder());
//                                           camera.startPreview();
//                                       } catch (IOException e) {
//                                           Log.e(TAG, "Error while trying to display the camera preview", e);
//                                       }
//                                   } else {
//                                       Toast.makeText(MainActivity.this,
//                                               "Permission denied, can't enable the camera",
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           },
//                        new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable t) {
//                                Log.e(TAG, "onError", t);
//                            }
//                        });
    }

    @Override
    protected void onInitData() {
        mTitle.setText("老哦 test");
        String[] bttxt = getResources().getStringArray(R.array.test_list);
        if (bttxt != null) {
            for (int i = 0; i < bttxt.length; i++) {
                 Button bt = new Button(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                lp.setMargins(margin, margin, margin, margin);
                bt.setId(i);
                bt.setText(bttxt[i]);
                bt.setOnClickListener(this);


                //RxBinding示例Demo
//                RxView.clicks(bt).throttleFirst(500, TimeUnit.MILLISECONDS)
//                        .subscribe(new Action1<Void>() {
//                            @Override
//                            public void call(Void aVoid) {
//
//                            }
//                        });



                bt.setLayoutParams(lp);
                mContainer.addView(bt);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case 0:
                gotoActivity(DBActivity.class, false);
//                Activity s = null;
//                s.startActivity(null);

                break;
            case 1:
                gotoActivity(HttpActivity.class, false);
                //gotoActivity(FragmentTabHostActivity.class, false);
                break;

            case 2:
                gotoActivity(RecyclerActivity.class, false);
                //gotoActivity(FragmentTabHostActivity.class, false);
                break;

            default:
                //showShortToast("还在开发中...");
                break;
        }

    }
}
