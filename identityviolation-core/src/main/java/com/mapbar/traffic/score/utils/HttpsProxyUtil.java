package com.mapbar.traffic.score.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpsProxyUtil {
	private static final Logger logger = Logger.getLogger(HttpsProxyUtil.class);

	static {
		initialize();
	}

	public static URLConnection openConnection(URL url) {
		return openConnection(url, null);
	}

	public static URLConnection openConnection(URL url, Proxy proxy) {
		URLConnection conn = null;
		try {
			if (proxy == null) {
				String strProxyHost = System.getProperty("http.proxyHost");
				String strProxyPort = System.getProperty("http.proxyPort");

				if (url.getProtocol().equalsIgnoreCase("https")) {
					strProxyHost = System.getProperty("https.proxyHost");
					strProxyPort = System.getProperty("https.proxyPort");
				}

				if (StringUtil.isNotEmpty(strProxyHost)) {
					int iPort = 80;
					if (StringUtil.isNotEmpty(strProxyPort))
						iPort = Integer.parseInt(strProxyPort);
					proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(strProxyHost, iPort));
				}
			}

			if (proxy != null) {
				conn = url.openConnection(proxy);
			}

			conn = url.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static void initialize() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");

			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					if (logger.isDebugEnabled()) {
						logger.info("getAcceptedIssuers =============");
					}
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
					if (!logger.isDebugEnabled()) {
						return;
					}
					logger.info("checkClientTrusted =============" + authType);
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					if (!logger.isDebugEnabled()) {
						return;
					}
					logger.info("checkServerTrusted =============" + authType);
				}
			} }, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String paramString, SSLSession paramSSLSession) {
					if (logger.isDebugEnabled()) {
						logger.info("verify ============" + paramString);
					}
					return true;
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}