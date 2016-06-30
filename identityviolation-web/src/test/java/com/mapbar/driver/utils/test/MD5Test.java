package com.mapbar.driver.utils.test;

import org.junit.Test;

import com.mapbar.driver.utils.MD5Util;

public class MD5Test {
	@Test
	public void test() {
		long n = System.currentTimeMillis();
		System.out.println(MD5Util.encode("api_key=7ce27cbf5fb9422a8aa04ec630592aa1call_id=" + n + "city_code=0010type=1v=1.02642b1366cef4790a797c6df1164bef4"));
		System.out.println(n);
	}
}
