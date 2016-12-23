package com.ljz.base.widget.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ljz.base.common.logutils.LogUtils;
import com.ljz.base.common.photo.ImageUtils;
import com.ljz.base.common.utils.FileUtil;
import com.ljz.base.frame.fragment.impl.BaseFragment;
import com.ljz.base.widget.R;

import java.io.File;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/05/31 09:46
 * 描述:
 */
public class WebViewFragment extends BaseFragment {


    public interface OnWebViewListener{
        void onPageFinished(WebView view, String url);
        boolean shouldOverrideUrlLoading(WebView view, String url);
    }



    /**
     * 5.0以上的系统
     */
    public static final int INPUT_FILE_REQUEST_CODE = 1;

    /**
     * 5.0以下的系统
     */
    public final static int FILECHOOSER_RESULTCODE = 2;

    public WebView mWebView;

    public ProgressBar mProgressBar;

    private ValueCallback<Uri> mUploadMessage;

    private ValueCallback<Uri[]> mFilePathCallback;

    private String mCameraPhotoPath;

    private boolean hasInited = false;

    private OnWebViewListener mListener;

    public void setOnWebViewListener(OnWebViewListener listener) {
        mListener = listener;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frag_webview;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        initView(mRootView);
        initWebView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView = null;
        mProgressBar = null;
        mUploadMessage = null;
        mFilePathCallback = null;
        mCameraPhotoPath = null;
        mListener = null;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onBackPressedSupport();
    }

    /**
     * 开始加载url
     *
     * @param url
     */
    public void startLoadUrl(String url) {
        if (null != mWebView && !hasInited) {
            hasInited = true;
            mWebView.loadUrl(url);
        }
    }

    /**
     * 刷新url
     * @param url
     */
    public void refreshLoadUrl(String url) {
        hasInited = true;
        mWebView.loadUrl(url);
    }

    protected void initView(View view) {
        mWebView = (WebView) view.findViewById(R.id.wv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
        settings.setAppCachePath(FileUtil.getCacheDir());
        settings.setJavaScriptEnabled(true);


        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressBar != null) {
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }


            // android 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
//                        photoFile = CameraUtil.getOutputMediaFile(CameraUtil.MEDIA_TYPE_IMAGE);
                        photoFile = new File(FileUtil.getIconDir());
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (Exception ex) {
                        // Error occurred while creating the File
                        Log.e("WebViewSetting", "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);


                return true;
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);

            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return mListener != null && mListener.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }

                if (mListener != null) {
                    mListener.onPageFinished(view,url);
                }
            }
        });

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            //5.0以下的回调
            if (null == mUploadMessage)
                return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (result != null) {
                String imagePath = ImageUtils.getPath(getActivity(), result);
                if (!TextUtils.isEmpty(imagePath)) {
                    result = Uri.parse("file:///" + imagePath);
                }
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
            // 5.0的回调
            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        LogUtils.d("camera_photo_path : " + mCameraPhotoPath);
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    LogUtils.d("camera_dataString : " + dataString);
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    } else {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
