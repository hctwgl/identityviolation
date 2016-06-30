package com.mapbar.driver.utils;


public class HttpReqUtil {

	public static String getWeixinNC(String nick) {
		StringBuilder nicksb = new StringBuilder();
		int l = nick.length();
		for (int i = 0; i < l; i++) {
			char _s = nick.charAt(i);
			if (isEmojiCharacter(_s)) {
				nicksb.append(_s);
			}
		}
		return nicksb.toString();
	}

	public static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
}
