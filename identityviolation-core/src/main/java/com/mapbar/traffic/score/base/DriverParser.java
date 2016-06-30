package com.mapbar.traffic.score.base;

import java.net.URLDecoder;
import java.util.Hashtable;

import com.mapbar.traffic.score.utils.StringUtil;

public class DriverParser {
	public static DriverProfile parseRequest(String strRequest) {
		DriverProfile driverProfile = new DriverProfile();

		try {
			if (StringUtil.isNotEmpty(strRequest)) {
				// province=%E5%8C%97%E4%BA%AC&city_pinyin=beijing&car_province=%E4%BA%AC&license_plate_num=NQK117&body_num=%E9%80%89%E5%A1%AB&engine_num=A19888
				Hashtable<String, String> htKVPs = parseKVPs(strRequest);

				String pro = parseValue(htKVPs.get("car_province"), "京");
				String cityName = parseValue(htKVPs.get("city"), "北京");
				String cityPy = parseValue(htKVPs.get("city_pinyin"), null);

				CityProfile city = ServiceConfig.getCityProfile(cityPy);
				if (city != null) {
					city.setPro(pro);
					city.setCityName(cityName);
				}
				// 参数赋值
				driverProfile.setCityName(cityName);
				driverProfile.setCityPy(cityPy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return driverProfile;
	}

	private static String parseValue(String strValue, String strDefaultValue) {
		try {
			if (StringUtil.isNotEmpty(strValue)) {
				if (StringUtil.isNotEmpty(strDefaultValue)) {
					strValue = URLDecoder.decode(strValue, "utf-8");
				} else {
					if (strValue.contains("%")) {
						strValue = null;
					}
				}
			} else {
				strValue = strDefaultValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strValue;
	}

	public static Hashtable<String, String> parseKVPs(String strData) {
		Hashtable<String, String> htKVPs = new Hashtable<String, String>();
       
		try {
			if (StringUtil.isNotEmpty(strData)) {
				String[] strItems = strData.split("&");
				for (int i = 0; i < strItems.length; i++) {
					if (StringUtil.isNotEmpty(strItems[i])) {
						String[] strElems = strItems[i].split("=");
						if (strElems.length >= 2) {
							htKVPs.put(strElems[0], strElems[1]);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htKVPs;
	}
}
