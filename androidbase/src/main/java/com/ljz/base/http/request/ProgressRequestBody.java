package com.ljz.base.http.request;

import com.ljz.base.common.logutils.LogUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 包装的请求体，处理进度，可以处理任何的 RequestBody，
 * 但是一般用在 multipart requests 上传大文件的时候
 */
public class ProgressRequestBody extends RequestBody {

    private RequestBody mDelegate;  //实际的待包装请求体
    private Listener mListener;     //进度回调接口
    private static final int REFRESH_TIME = 100;                       //回调刷新时间（单位ms）

    public ProgressRequestBody(RequestBody delegate) {
        this.mDelegate = delegate;
    }

    public ProgressRequestBody(RequestBody delegate, Listener listener) {
        this.mDelegate = delegate;
        this.mListener = listener;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     */
    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (IOException e) {
            LogUtils.e(e);
            return -1;
        }
    }

    /**
     * 重写进行写入
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        mDelegate.writeTo(bufferedSink);
        bufferedSink.flush();  //必须调用flush，否则最后一部分数据可能不会被写入
    }

    /**
     * 包装
     */
    private final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;   //当前写入字节数
        private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
        private long lastRefreshUiTime;  //最后一次刷新的时间
        private long lastWriteBytes;     //最后一次写入字节数据

        CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (contentLength <= 0)
                contentLength = contentLength(); //获得contentLength的值，后续不再调用
            bytesWritten += byteCount;

            long curTime = System.currentTimeMillis();
            //每100毫秒刷新一次数据
            if (curTime - lastRefreshUiTime >= REFRESH_TIME || bytesWritten == contentLength) {
                //计算下载速度
                long diffTime = (curTime - lastRefreshUiTime) / 1000;
                if (diffTime == 0) {
                    diffTime += 1;
                }
                long diffBytes = bytesWritten - lastWriteBytes;
                long networkSpeed = diffBytes / diffTime;
                if (mListener != null) {
                    mListener.onRequestProgress(bytesWritten, contentLength, networkSpeed);
                }
                lastRefreshUiTime = System.currentTimeMillis();
                lastWriteBytes = bytesWritten;
            }
        }
    }

    /**
     * 回调接口
     */
    public interface Listener {
        /**
         *
         * @param bytesWritten  当前写入字节数
         * @param contentLength 总字节长度
         * @param networkSpeed  当前上传的网速（单位秒）
         */
        void onRequestProgress(long bytesWritten, long contentLength, long networkSpeed);
    }
}