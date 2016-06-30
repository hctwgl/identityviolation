package com.mapbar.traffic.score.utils;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.log4j.Logger;

public class HttpClientIdelThread extends Thread {

	private static Logger logger = Logger.getLogger(HttpClientIdelThread.class);
	private final HttpClientConnectionManager connMgr;
	private static HttpClientIdelThread instance = null;
	private boolean shutdown = true;

	public static HttpClientIdelThread newInstance(HttpClientConnectionManager connMgr) {
		if (instance == null) {
			synchronized (HttpClientIdelThread.class) {
				if (instance == null) {
					instance = new HttpClientIdelThread(connMgr);
				}
			}
		}
		return instance;
	}

	private HttpClientIdelThread(HttpClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (shutdown) {
				synchronized (this) {
					TimeUnit.SECONDS.sleep(5);
					// 关闭失效的连接
					connMgr.closeExpiredConnections();
					// 可选的, 关闭60秒内不活动的连接
					connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			logger.info("IdleConnectionMonitorThread InterruptedException exits!");
		}
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}
}