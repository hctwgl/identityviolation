package com.mapbar.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import com.mapbar.traffic.score.utils.HttpsProxyUtil;

public class SSLContextTest {
	@Test
	public void testSSL() {
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "8087");
		System.setProperty("https.proxyHost", "116.213.115.59");
		System.setProperty("https.proxyPort", "5708");
		System.setProperty("https.proxyHost", "112.65.233.224");
		System.setProperty("https.proxyPort", "8088");
		System.setProperty("https.proxyHost", "61.147.119.135");
		System.setProperty("https.proxyPort", "80");
		try {
			String strUrl = "https://itunes.apple.com/WebObjects/MZStore.woa/wa/storeFront";
			URL url = new URL(strUrl);
			URLConnection conn = HttpsProxyUtil.openConnection(url, null);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}