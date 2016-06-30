package com.mapbar.traffic.score.parser.test;

import org.junit.Test;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.logs.LogWorkThread;
import com.mapbar.traffic.score.mail.Mail;
import com.mapbar.traffic.score.mail.MailWorkThread;
import com.mapbar.traffic.score.relay.RelayProcess;
import com.mapbar.traffic.score.utils.ConfigUtils;

public class RelayProcessTest {
	@Test
	public void test() {
		try {
			LogWorkThread thread = new LogWorkThread();
			Mail mail = new Mail();
			MailWorkThread mt = new MailWorkThread(mail);
			thread.start();
			mt.start();

			RelayProcess wc = RelayProcess.getInstance();
			if (true) {
				// System.setProperty("http.proxyHost", "119.255.37.165");
				// System.setProperty("http.proxyPort", "18088");
				// System.setProperty("https.proxyHost", "119.255.37.165");
				// System.setProperty("https.proxyPort", "18088");
				DriverProfile driverProfile = new DriverProfile();
				// RedisDBUtil.del("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime");
				driverProfile.setCityPy("beijing");
				driverProfile.setCityName("北京");
				driverProfile.set("赵峰", "110102198403201553", "2007-08-02", "lssueArchive", "is_effective", "effective_date");
				String ret = wc.process(driverProfile);
				System.out.println("ret=====" + ret);
			}
			Thread.sleep(10000);
			thread.interrupt();
			thread.commitSql();
			Thread.sleep(1000);
			mt.interrupt();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConfigUtils.init();
	}
}
