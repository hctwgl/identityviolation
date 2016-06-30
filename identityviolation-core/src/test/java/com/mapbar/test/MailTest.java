package com.mapbar.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.mapbar.traffic.score.mail.Mail;

public class MailTest {
	@Test
	public void testMail() {
		Mail themail = new Mail();
		try {
			themail.sendMail("驾驶证积分查询-服务告警", "违章数据源：广东交管网 查询 出现大量异常，请尽快处理!", null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
