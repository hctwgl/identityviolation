package com.mapbar.traffic.score.utils;

public class StringUtil {

	public static boolean isNotEmpty(String strInput) {
		return ((strInput != null) && (strInput.trim().length() > 0));
	}

	public static String clipHeader(String strAddress, String strHeader) {
		String strReturn = strAddress;
		if ((strReturn != null) && (strReturn.length() > strHeader.length()) && (strReturn.startsWith(strHeader))) {
			strReturn = strReturn.substring(strHeader.length());
		}

		return strReturn;
	}

	public static String clipTail(String strAddress, String strTail) {
		String strReturn = strAddress;
		if ((strReturn != null) && (strReturn.length() > strTail.length()) && (strReturn.endsWith(strTail))) {
			strReturn = strReturn.substring(0, strReturn.length() - 1);
		}

		return strReturn;
	}

	public static String parseToken(String strData, String strStartKey, String strEndKey) {
		String strToken = null;
		try {
			int idx0 = strData.indexOf(strStartKey);
			int idx2 = strData.indexOf(strEndKey, idx0 + strStartKey.length());

			if ((idx0 >= 0) && (idx2 > idx0)) {
				strToken = strData.substring(idx0 + strStartKey.length(), idx2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strToken;
	}

	public static String parseToken(String strData, String strFromKey, String strStartKey, String strEndKey) {
		String strToken = null;
		try {
			int idxFrom = strData.indexOf(strFromKey);

			if (idxFrom >= 0) {
				int idx0 = strData.indexOf(strStartKey, idxFrom);
				int idx2 = strData.indexOf(strEndKey, idx0 + strStartKey.length());

				if ((idx0 >= 0) && (idx2 >= 0)) {
					strToken = strData.substring(idx0 + strStartKey.length(), idx2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strToken;
	}

	public static long ip2long(String ip) {
		String[] ips = ip.split("[.]");
		long num = 16777216L * Long.parseLong(ips[0]) + 65536L * Long.parseLong(ips[1]) + 256L * Long.parseLong(ips[2]) + Long.parseLong(ips[3]);
		return num;
	}

	public static String long2ip(long ipLong) {
		long[] mask = { 255L, 65280L, 16711680L, -16777216L };
		long num = 0L;
		StringBuffer ipInfo = new StringBuffer();
		for (int i = 0; i < 4; ++i) {
			num = (ipLong & mask[i]) >> i * 8;
			if (i > 0)
				ipInfo.insert(0, ".");
			ipInfo.insert(0, Long.toString(num, 10));
		}
		return ipInfo.toString();
	}

}