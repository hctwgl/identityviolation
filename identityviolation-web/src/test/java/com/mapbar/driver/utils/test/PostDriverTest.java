package com.mapbar.driver.utils.test;

import org.junit.Test;

import com.mapbar.traffic.score.logs.LogWorkThread;

public class PostDriverTest {
	@Test
	public void test() {

		LogWorkThread thread = new LogWorkThread();
		thread.start();

		System.setProperty("http.proxyHost", "10.10.21.111");
		System.setProperty("http.proxyPort", "8088");
		System.setProperty("https.proxyHost", "10.10.21.111");
		System.setProperty("https.proxyPort", "8088");

		try {
			String html = "";
			// html=getViolation(driverName, driverLicense, lissueDate, lissueArchive, province, city_pinyin, isEffective, sid, cs, isyzm, effectiveDate, vcode, refresh);
			System.out.println(html);
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		thread.interrupt();
		thread.commitSql();

		System.exit(0);
	}
}
