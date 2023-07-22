package com.itheima.reegie.common;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
@Slf4j
public class BaseContext {
    private static ThreadLocal<Long> threadLocal  = new ThreadLocal<>();
    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
        log.info("124575121" + id);
    }
    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}