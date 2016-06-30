package com.mapbar.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.mapbar.driver.utils.HttpReqUtil;
import com.mapbar.traffic.score.utils.HttpClientUtil;

public class MyTest {

	private static final Logger logger = Logger.getLogger(Test.class);

	@Test
	public void test() {
		// 测试token
		System.out.println(HttpClientUtil.checkToken("https://obduc.mapbar.com/user/auth_lock/android_obd", "KZPR+g1vHcSRilRZz9A+Cto7fYGtAF4KBPv/KRNKjpl/uaAT+Ld7mDcrkAqlgw07"));
		int tokenInt = HttpClientUtil.checkToken("http://uc.mapbar.com/user/auth_lock/" + "android_trinity", "cQmgIyEBAOoQA9FTwY/8OPk3YrknOyblAd/dc1AyDwcYCfIeUSsO0/Ofc0bXnUGp");
		logger.info(tokenInt);
	}

	// @org.junit.Test
	public void testWx() {
		// String abc = "{\"errcode\":40029,\"errmsg\":\"invalid code\"}";
		// JSONObject json = JSONObject.fromObject(abc);
		// https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxd7e8339df261e15d&secret=3a89892f3b66896084b2305da120b8e5&code=页面传进来&grant_type=authorization_code
		// String code = "0215b96447d98c790ed10e7dade2892V";
		// JSONObject jsonObject =
		// JSONObject.fromObject(getWeixinVerify("https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&appid=wxd7e8339df261e15d&secret=3a89892f3b66896084b2305da120b8e5&code="
		// + code));
		//
		// jsonObject =
		// JSONObject.fromObject("{\"access_token\": \"OezXcEiiBSKSxW0eoylIeOz0ZU71pcy72vRr6BsxKXGwOy1a4oijhPPENeNkVoTYwDjpSrv1-lNWrPVc9PduppdYsvrqUf9e2jTIF47QtD7dsZydq51GAnGw-0wEQG4p5dAsRGTaEd5IKANGZ7Uzrg\",\"expires_in\": 7200,\"refresh_token\": \"OezXcEiiBSKSxW0eoylIeOz0ZU71pcy72vRr6BsxKXGwOy1a4oijhPPENeNkVoTYll6Ok56r2j_zj30a7H8yH069d_2HW8A5BKuSR5dNR5_E3M9nUkzZwdJ5pfRjhYgjIgpEJ41ccr5VbxbTwCCPYw\",\"openid\": \"oTOWyjvJwgfqSj5C6Qq9l_3AnMhw\",\"scope\": \"snsapi_base\",\"unionid\": \"oy7BkuNluwLlebhQtGw3egDR0ask\" }");
		//
		// String access_token = jsonObject.getString("access_token");
		// String openid = jsonObject.getString("openid");
		// System.out.println(access_token + "\t" + openid);
		// String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
		// access_token + "&openid=" + openid + "&lang=zh_CN";
		// System.out.println(getWeixinVerify(url));

		// String token =
		// "KGsXwb7uCFv5+01jTVkMG/joInK4oC6AWXLD/1kgCihdQAeMalAVn27mOXOvdnZg";
		// System.out.println(checkToken(token));
		// System.out.println(getWeixinVerify("http://192.168.0.176:8026/wirelessactivity/exchangDiscount?mobile=13959456465&discount_type=PP租车"));
		String nicheng = "";
		System.out.println(HttpReqUtil.getWeixinNC(nicheng));
	}

	@Test
	public void test02() throws Exception {
		URL url = new URL("http://media.mongodb.org/zips.json");
		Document parse = Jsoup.parse(url, 60*10000);
		System.out.println(parse);
	}

	public static void deleteFile(String path) {
		deleteFile(new File(path));
	}

	public static void deleteFile(File file) {
		if (!(file.exists()))
			return;
		if (file.isDirectory()) {
			for (File sub : file.listFiles()) {
				deleteFile(sub);
			}
		}
		file.delete();
	}

	@Test
	public void bigFile() throws Exception {
		File theFile = new File("d:\\Users\\xubh\\Desktop\\nohup.out");
		LineIterator it = FileUtils.lineIterator(theFile, "UTF-8");
		try {
			String line = null;
			while (it.hasNext()) {
				line = it.nextLine();
				if (line.contains("main")) {
					logger.info(line);
					for (int i = 0; i < 100; i++) {
						logger.info(it.nextLine());
						if (i == 99) {
							logger.info("\r\n");
						}
					}
				}
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}

	@Test
	public void bigCsv() throws Exception {
		// File theFile = new File("d:\\Users\\xubh\\Desktop\\weizhang.csv");
		File theFile = new File("d:\\Users\\xubh\\Desktop\\weizhang11_12.csv");
		LineIterator it = FileUtils.lineIterator(theFile, "UTF-8");
		try {
			String line = null;
			while (it.hasNext()) {
				line = it.nextLine();
			}
			System.out.println(line);
		} finally {
			LineIterator.closeQuietly(it);
		}
	}

	@Test
	public void testDate() {
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.MONTH) + 1;
		System.out.println(i);
		// Date date = new Date();
		// int month = date.getMonth() + 1;
		// System.out.println(month);
		System.out.println(740122 / 10000);

	}

	@Test
	public void testDate1() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) - 3);
		Date startTime = cal.getTime();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
