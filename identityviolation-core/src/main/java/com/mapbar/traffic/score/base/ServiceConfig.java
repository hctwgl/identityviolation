package com.mapbar.traffic.score.base;

import java.util.Map;

import com.mapbar.traffic.score.db.DbHelp;
import com.mapbar.traffic.score.utils.StringUtil;

public class ServiceConfig {

	public static Map<String, CityProfile> htCitylist = null;

	public static void refreshCitys() {
		try {
			Map<String, CityProfile> map = DbHelp.getAllCity();
			htCitylist.clear();
			htCitylist = map;
			// loadCheShouYe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			htCitylist = DbHelp.getAllCity();
			// loadCheShouYe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static CityProfile getCityProfile(String strCityPy) {
		CityProfile city = null;
		if (StringUtil.isNotEmpty(strCityPy)) {
			city = htCitylist.get(strCityPy);
		}
		return city;
	}

	public static String toKey(String strState, String strCity) {
		return strState + "," + strCity;
	}

}
