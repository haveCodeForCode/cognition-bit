package com.cognition.bit.system.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * session 监控实现
 *
 * @author LineInkBook
 */
public class BaseSessionListener implements SessionListener {

    /**
     * 设置sseion监控元素初始值
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 设置新增session 监控（数量1）
     *
     * @param session
     */
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
    }

    /**
     * 去掉session监听
     *
     * @param session
     */
    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
    }

    /**
     * 异常则清除session监听
     *
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
    }

    /**
     * 获取数量
     *
     * @return
     */
    public int getSessionCount() {
        return sessionCount.get();
    }
}
