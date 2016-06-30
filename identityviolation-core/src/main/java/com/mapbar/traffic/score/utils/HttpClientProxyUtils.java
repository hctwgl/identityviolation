package com.mapbar.traffic.score.utils;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpClientProxyUtils {

	public static HttpRequestBase setProxy(HttpRequestBase request, HttpHost proxy) {
		URI uri = request.getURI();
		try {
			if (proxy == null) {
				String proxyHost = System.getProperty("http.proxyHost");
				String proxyPort = System.getProperty("http.proxyPort");

				if (uri.getScheme().equalsIgnoreCase("https")) {
					proxyHost = System.getProperty("https.proxyHost");
					proxyPort = System.getProperty("https.proxyPort");
				}

				if (StringUtil.isNotEmpty(proxyHost)) {
					int iPort = 80;
					if (StringUtil.isNotEmpty(proxyPort)) {
						iPort = Integer.parseInt(proxyPort);
					}
					proxy = new HttpHost(proxyHost, iPort, uri.getScheme());
				}

			}

			if (proxy != null) {
				RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
				request.setConfig(config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;

	}
}
