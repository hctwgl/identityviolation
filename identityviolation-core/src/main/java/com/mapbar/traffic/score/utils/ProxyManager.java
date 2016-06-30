package com.mapbar.traffic.score.utils;

import org.apache.http.HttpHost;

public class ProxyManager {

	protected static String strProxyFile = System.getProperty("PROXY_FILE", "https_proxy.txt");
	protected static HttpClientProxyManager httpClientProxyManager = new HttpClientProxyManager(strProxyFile);
	protected static HttpsProxyManager httpsProxyManager = new HttpsProxyManager(strProxyFile);

	public static HttpHost next(boolean isForced) {
		return httpClientProxyManager.next(isForced);
	}

	public static HttpsProxy nextHttpsProxy(boolean isForced) {
		return httpsProxyManager.next(isForced);
	}
}
