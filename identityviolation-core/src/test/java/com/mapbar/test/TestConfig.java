package com.mapbar.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class TestConfig {
	private static String loadConfig(String basePath) {
		String ret = "";
		InputStream inputStream = null;
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		try {
			inputStream = new FileInputStream(new File(basePath + "/citylist.txt"));
			// InputStream inputStream = ServiceConfig.class.getClassLoader().getResourceAsStream("MapCityList.txt");
			read = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(read);
			ret = bufferedReader.readLine();
			ret = ret.replaceAll("\\[\\[", "\\[\\[\\[");
			ret = ret.replaceAll("\\]\\]", "\\]\\]\\]");
			in = new InputStreamReader(new FileInputStream(basePath + "/provinceMap.txt"), "utf-8");
			reader = new BufferedReader(in);
			String list = reader.readLine();
			while (list != null) {
				String[] s = list.split(":");
				ret = ret.replace("" + s[0] + "\": [", "" + s[0] + "\": [\"" + s[1] + "\",");
				list = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		return ret;
	}

	@Test
	public void test01() {
		loadConfig("");
	}
}
