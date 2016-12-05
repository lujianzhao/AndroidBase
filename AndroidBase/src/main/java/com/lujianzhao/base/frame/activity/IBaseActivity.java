package com.lujianzhao.base.frame.activity;

import com.lujianzhao.base.netstate.NetWorkUtil;

/**
 * Created by Administrator on 2016/5/13.
 */
public interface IBaseActivity {
    /**
     * 网络断开
     */
    void onNetWorkDisConnect();

    /**
     * 网络连接上
     *
     * @param type 当前的网络状态:
     *             UnKnown(-1),没有网络
     *             Wifi(1),WIFI网络
     *             Net2G(2),2G网络
     *             Net3G(3),3G网络
     *             Net4G(4);4G网络
     */
    void onNetWorkConnect(NetWorkUtil.NetWorkType type);
}
