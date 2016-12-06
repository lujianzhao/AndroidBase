/**
 * Copyright 2015 ZhangQu Li
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ljz.base.http.progress;

import com.ljz.base.common.rx.RxUtil;
import com.ljz.base.http.progress.domain.ProgressRequest;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/14 15:51
 * 描述:
 */
public class ProgressRequestBody extends RequestBody {
    //实际的待包装请求体
    private final RequestBody mRequestBody;
    //上传进度
    private ProgressRequest mProgressRequest;
    //包装完成的BufferedSink
    private BufferedSink mBufferedSink;
    private PublishSubject<ProgressRequest> mPublishSubject;

    public ProgressRequestBody(String fileName, String filePath, RequestBody requestBody, Subscriber<ProgressRequest> subscriber) {
        this.mRequestBody = requestBody;
        this.mProgressRequest = new ProgressRequest(fileName, filePath);
        mPublishSubject = PublishSubject.create();
        mPublishSubject.compose(RxUtil.<ProgressRequest>applySchedulersProgress()).subscribe(subscriber);
    }


    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            //包装
            mBufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        mRequestBody.writeTo(mBufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        mBufferedSink.flush();

    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            private long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            private long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                    mProgressRequest.setContentLength(contentLength);
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if (mPublishSubject != null && mProgressRequest!=null) {
                    mProgressRequest.setCurrentBytes(bytesWritten);
                    mPublishSubject.onNext(mProgressRequest);
                    //判断是否上传完毕
                    if (bytesWritten == contentLength) {
                        //clean
                        mProgressRequest = null;
                        mPublishSubject = null;
                    }
                }

            }
        };
    }
}