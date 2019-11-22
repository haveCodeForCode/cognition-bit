/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.cognition.bit.common.until;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.cognition.bit.common.until.codegenerate.IdGen;

import java.util.concurrent.*;

/**
 * 线程相关工具类.
 *
 * @author calvin
 * @version 2013-01-15
 */
public class ThreadsTools {

    /**
     * 线程工厂
     * @param factoryName
     * @return
     */
    public static ThreadFactory buildThreadFactory(String factoryName) {
        ThreadFactory newNameThreadFactory;
        if (StringUtils.isNotEmpty(factoryName)) {
            newNameThreadFactory = new ThreadFactoryBuilder().setNameFormat(factoryName).build();
        } else {
            newNameThreadFactory = new ThreadFactoryBuilder().setNameFormat(IdGen.uuid()).build();
        }
        return newNameThreadFactory;
    }



    /**
     * sleep等待,单位为毫秒,忽略InterruptedException.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore.
            consoleOut(e.getMessage());
        }
    }

    /**
     * sleep等待,忽略InterruptedException.
     */
    public static void sleep(long duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
            // Ignore.
            consoleOut(e.getMessage());
        }
    }

    private static void consoleOut(String e){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=============================\n");
        stringBuilder.append(e).append("\n");
        stringBuilder.append("=============================");
        System.err.println(stringBuilder);
    }
}
