package com.mapbar.driver.utils.test;

import java.util.HashMap;

import org.junit.Test;

import com.mapbar.driver.utils.PinYinUtil;

public class PinYinUtilTest {
	@Test
	public void test() {
		String FirstPinYin = PinYinUtil.getPingYin("7").substring(0, 1);

		HashMap<String, String> map1 = new HashMap<String, String>();

		map1.put(FirstPinYin, "7");
		map1.put(FirstPinYin, "7");
		System.out.println(map1.get(FirstPinYin));

		String lowerCase = PinYinUtil.cn2Spell("中国人").toLowerCase();
		System.out.println(lowerCase);
	}

}
