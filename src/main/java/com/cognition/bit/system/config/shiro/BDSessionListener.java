package com.cognition.bit.system.config.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * session线程
 * @author taoya
 */
public class BDSessionListener implements SessionListener {

	private final AtomicInteger sessionCount = new AtomicInteger(0);

	/**
	 * 原子组增加
	 * @param session
	 */
	@Override
	public void onStart(Session session) {
		sessionCount.incrementAndGet();
	}

	/**
	 * 原子组减少
	 * @param session
	 */
	@Override
	public void onStop(Session session) {
		sessionCount.decrementAndGet();
	}

	@Override
	public void onExpiration(Session session) {
		sessionCount.decrementAndGet();

	}

	public int getSessionCount() {
		return sessionCount.get();
	}

}
