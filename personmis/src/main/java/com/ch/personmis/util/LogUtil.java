package com.ch.personmis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtil {
    
    /**
     * 获取日志记录器
     * @param clazz 类
     * @return Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * 获取日志记录器
     * @param name 名称
     * @return Logger
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
}