package com.mapbar.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;

public class TestUtil {
	@Test
	public void testConn() {
		HttpURLConnection conn = null;
		URL url = null;
		try {
			url = new URL("http://www.stc.gov.cn:8082/szwsjj_web/ImgServlet.action?rnd=0.14756181254051626");
			conn = (HttpURLConnection) url.openConnection();
			// conn.setRequestProperty("Connection", "close");
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(3000);
			conn.connect();
			int stateCode = conn.getResponseCode();
			System.out.println(stateCode);
			// if (stateCode == 200) {
			// InputStream input = conn.getInputStream();
			// JSONObject result = new JSONObject();
			// result.put("success", false);
			// if (result.getInt("code") == 200) {
			// result.put("success", true);
			// }
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test02() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "11");
		String string = JSONObject.toJSONString(map);
		System.out.println(string);
	}

	@Test
	public void testFileUtils() {
		String date = String.valueOf(DateUtils.getCurrentDate());
		String cityPy = "guangzhou";
		String path = FileUtils.checkDerictor("D:/validateImage/chengdu/", cityPy, date);
		System.out.print(path);
	}
}
