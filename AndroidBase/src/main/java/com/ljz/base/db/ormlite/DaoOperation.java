package com.ljz.base.db.ormlite;


public interface DaoOperation {

    /**
     * 插入
     */
    int INSERT = 1;
    /**
     * 删除
     */
    int DELETE = 2;
    /**
     * 更新
     */
    int UPDATE = 3;
    /**
     * 查询
     */
    int SELECT = 4;
    /**
     * 批量插入
     */
    int INSERT_BATCH = 5;
    /**
     * 批量删除
     */
    int DELETE_BATCH = 6;
    /**
     * 批量更新
     */
    int UPDATE_BATCH = 7;
}
