package com.lujianzhao.base.http.progress.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/06/23 09:55
 * 描述:
 */
public class ProgressRequest implements Serializable, Parcelable {
    private String name;       //文件的名字
    private String path;       //文件路径
    private long currentBytes; //当前长度
    private long contentLength; //总长度

    public ProgressRequest() {
    }

    public ProgressRequest(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public ProgressRequest(String name, String path,long currentBytes,long contentLength) {
        this.name = name;
        this.path = path;
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.currentBytes);
        dest.writeLong(this.contentLength);
    }



    protected ProgressRequest(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.currentBytes = in.readLong();
        this.contentLength = in.readLong();
    }

    public static final Parcelable.Creator<ProgressRequest> CREATOR = new Parcelable.Creator<ProgressRequest>() {
        @Override
        public ProgressRequest createFromParcel(Parcel source) {
            return new ProgressRequest(source);
        }

        @Override
        public ProgressRequest[] newArray(int size) {
            return new ProgressRequest[size];
        }
    };
}
