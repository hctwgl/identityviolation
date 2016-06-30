package com.mapbar.driver.utils.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.mapbar.driver.utils.DateUtil;

public class DateUtilTest {
	@Test
	public void test() {
		System.out.println(DateUtil.getCurrentDate());
		double d = (DateUtil.stringToDate(DateUtil.currentTime()).getTime()) - (DateUtil.stringToDate("2009-08-27 16:00:00").getTime());
		double dd = d / 3600000;
		System.out.println(d + "  " + dd + " " + DateUtil.disTime("2009-08-27 16:00:00"));
		System.out.println(DateUtil.parseDate("2009年11月12日 12时:12分:22秒"));
		TimeZone default1 = TimeZone.getDefault();
		System.out.println(default1);
		TimeZone zone = TimeZone.getTimeZone("GMT+08:00");
		System.out.println(zone);
	}

	@Test
	public void testSy() throws Exception {
		Callable<Date> task = new Callable<Date>() {
			public Date call() throws Exception {
				// return DateUtil.parseDate("2010-08-11");
				return DateUtil.StrToDate("2010-08-11 20:01:10");
			}
		};
		// 让我们尝试2个线程的情况
		ExecutorService exec = Executors.newFixedThreadPool(2);
		List<Future<Date>> results = new ArrayList<Future<Date>>();
		// 实现5次日期转换
		for (int i = 0; i < 5; i++) {
			results.add(exec.submit(task));
		}
		exec.shutdown();
		// 查看结果
		for (Future<Date> result : results) {
			System.out.println(result.get());
		}
	}
}
