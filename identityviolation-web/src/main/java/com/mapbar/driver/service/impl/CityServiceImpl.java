package com.mapbar.driver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.driver.bean.City;
import com.mapbar.driver.dao.CityDao;
import com.mapbar.driver.service.CityService;
import com.mapbar.driver.utils.StringUtil;

@Service
public class CityServiceImpl implements CityService {

	@Resource
	private CityDao cityDao;

	public String getCityList() {
		String re = "";
		try {
			List<String> list = new ArrayList<String>();
			list.add("BJ");
			list.add("SH");
			list.add("CQ");
			list.add("TJ");
			List<String> citylist = this.cityDao.getProPyList("SELECT province_pinyin FROM city_list WHERE province_pinyin NOT IN ('CQ', 'BJ', 'TJ', 'SH') GROUP BY province_pinyin");
			list.addAll(citylist);
			int listSize = list.size();
			JSONObject json = new JSONObject(true);
			for (int i = 0; i < listSize; i++) {
				Boolean isNull = Boolean.valueOf(true);

				List<City> cityMList = this.cityDao.getCityList("SELECT province_pinyin,province,pro,city_code,city_name,city_status,is_yzm,update_time,is_name,is_license,is_licensedate,is_archive,is_effective,effective_date FROM city_list WHERE province_pinyin ='" + (String) list.get(i) + "' GROUP BY city_name");

				JSONObject proJson = new JSONObject(true);
				List<JSONObject> citysList = new ArrayList<JSONObject>();
				String province = "";
				for (City cityM : cityMList) {
					province = cityM.getProvince();
					if ("1".equals(cityM.getCity_status())) {
						isNull = Boolean.valueOf(false);
						JSONObject cityJson = new JSONObject();
						cityJson.put("city_code", cityM.getCity_code());
						cityJson.put("city_name", cityM.getCity_name());
						cityJson.put("pro", cityM.getPro());
						// cityJson.put("city_abbr", "");
						cityJson.put("status", cityM.getCity_status());
						cityJson.put("is_name", cityM.getIs_name());
						cityJson.put("is_license", cityM.getIs_license());
						cityJson.put("is_licensedate", cityM.getIs_licensedate());
						cityJson.put("is_archive", cityM.getIs_archive());
						cityJson.put("is_effective", cityM.getIs_effective());
						cityJson.put("effective_date", cityM.getEffective_date());
						citysList.add(cityJson);
					}
					proJson.put("citys", citysList);
					proJson.put("province", province);
					proJson.put("index", String.valueOf(i));
				}
				if (!isNull.booleanValue())
					json.put((String) list.get(i), proJson);
			}
			re = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	public String getUpdateCityList(String update_time) {

		String re = "";
		String max_time = this.cityDao.getMaxUpdateTime();
		JSONObject retJson = new JSONObject(true);
		if (!StringUtil.isEmpty(update_time) && update_time.equals(max_time)) {
			retJson.put("is_update", "0");
			retJson.put("update_time", max_time);
			retJson.put("cityList", "");
			return retJson.toString();
		} else {
			retJson.put("is_update", "1");
			retJson.put("update_time", max_time);
		}
		try {
			List<String> list = new ArrayList<String>();
			list.add("BJ");
			list.add("SH");
			list.add("CQ");
			list.add("TJ");
			List<String> citylist = this.cityDao.getProPyList("SELECT province_pinyin FROM city_list WHERE province_pinyin NOT IN ('CQ', 'BJ', 'TJ', 'SH') GROUP BY province_pinyin");
			list.addAll(citylist);
			int listSize = list.size();
			JSONObject json = new JSONObject(true);
			// JSONArray jarray = new JSONArray();
			for (int i = 0; i < listSize; i++) {
				Boolean isNull = Boolean.valueOf(true);

				List<City> cityMList = this.cityDao.getCityList("SELECT province_pinyin,province,pro,city_code,city_name,city_status,is_yzm,update_time,is_name,is_license,is_licensedate,is_archive,is_effective,effective_date FROM city_list WHERE province_pinyin ='" + (String) list.get(i) + "' GROUP BY city_name");

				JSONObject proJson = new JSONObject(true);
				List<JSONObject> citysList = new ArrayList<JSONObject>();
				String province = "";
				for (City cityM : cityMList) {
					province = cityM.getProvince();
					if ("1".equals(cityM.getCity_status())) {
						isNull = Boolean.valueOf(false);
						JSONObject cityJson = new JSONObject();
						cityJson.put("city_code", cityM.getCity_code());
						cityJson.put("city_name", cityM.getCity_name());
						cityJson.put("pro", cityM.getPro());
						// cityJson.put("city_abbr", "");
						cityJson.put("status", cityM.getCity_status());
						cityJson.put("is_name", cityM.getIs_name());
						cityJson.put("is_license", cityM.getIs_license());
						cityJson.put("is_licensedate", cityM.getIs_licensedate());
						cityJson.put("is_archive", cityM.getIs_archive());
						cityJson.put("is_effective", cityM.getIs_effective());
						cityJson.put("effective_date", cityM.getEffective_date());
						citysList.add(cityJson);
					}
					proJson.put("citys", citysList);
					proJson.put("province", province);
					proJson.put("index", String.valueOf(i));
				}
				if (!isNull.booleanValue()) {
					json.put((String) list.get(i), proJson);
				}
			}
			// jarray.add(json);
			retJson.put("cityList", json);
			// System.out.println(JSON.toJSONString(retJson, SerializerFeature.DisableCircularReferenceDetect));
			re = retJson.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	public Map<String, City> getCityMap() {
		List<String> list = this.cityDao.getProPyList("SELECT province_pinyin FROM city_list GROUP BY province ");
		int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			List<City> cityMList = this.cityDao.getCityList("SELECT province_pinyin,province,pro,city_code,city_name,city_status,is_yzm,update_time,is_name,is_license,is_licensedate,is_archive,is_effective,effective_date FROM city_list WHERE province_pinyin ='" + (String) list.get(i) + "' GROUP BY city_name");
			for (City cityM : cityMList) {
				cityMap.put(cityM.getCity_code(), cityM);
				cityMap.put(cityM.getCity_name(), cityM);
			}
		}
		return cityMap;
	}

	public Map<String, String> getOldCityListMap() {
		oldCLMap.put("HN_SMX", "sanmenxia");
		oldCLMap.put("SH", "shanghai");
		oldCLMap.put("GD_DG", "dongguan");
		oldCLMap.put("SD_DY", "dongying");
		oldCLMap.put("GD_ZS", "zhongshan");
		oldCLMap.put("SD_LY", "linyi");
		oldCLMap.put("LN_DD", "dandong");
		oldCLMap.put("WLMQ", "wulumuqi");
		oldCLMap.put("GD_FS", "foshan");
		oldCLMap.put("HB_BD", "baoding");
		oldCLMap.put("LZ", "lanzhou");
		oldCLMap.put("BJ", "beijing");
		oldCLMap.put("JS_NJ", "nanjing");
		oldCLMap.put("JS_NT", "nantong");
		oldCLMap.put("FJ_XM", "shamen");
		oldCLMap.put("HF", "hefei");
		oldCLMap.put("JL", "jilin");
		oldCLMap.put("HHHT", "huhehaote");
		oldCLMap.put("SX_XY", "xianyang");
		oldCLMap.put("HEB", "haerbin");
		oldCLMap.put("HB_TS", "tangshan");
		oldCLMap.put("HN_SQ", "shangqiu");
		oldCLMap.put("ZJ_JX", "jiaxing");
		oldCLMap.put("LN_DL", "dalian");
		oldCLMap.put("TY", "taiyuan");
		oldCLMap.put("SD_WH", "weihai");
		oldCLMap.put("ZJ_NB", "ningbo");
		oldCLMap.put("SX_AK", "ankang");
		oldCLMap.put("JS_CZ", "changzhou");
		oldCLMap.put("HN_PDS", "pingdingshan");
		oldCLMap.put("GD_GZ", "guangzhou");
		oldCLMap.put("HB_LF", "langfang");
		oldCLMap.put("SX_YA", "yanan");
		oldCLMap.put("HB_ZJK", "zhangjiakou");
		oldCLMap.put("JS_XZ", "xuzhou");
		oldCLMap.put("SD_DZ", "dezhou");
		oldCLMap.put("SC_CD", "chengdu");
		oldCLMap.put("JS_YZ", "yangzhou");
		oldCLMap.put("HB_CD", "chengde");
		oldCLMap.put("LN_FS", "fushun");
		oldCLMap.put("JS_WX", "wuxi");
		oldCLMap.put("SD_RZ", "rizhao");
		oldCLMap.put("LN_CY", "zhaoyang");
		oldCLMap.put("ZJ_HZ", "hangzhou");
		oldCLMap.put("SD_ZZ", "zaozhuang");
		oldCLMap.put("WH", "wuhan");
		oldCLMap.put("LN_SY", "shenyang");
		oldCLMap.put("HB_CZ", "cangzhou");
		oldCLMap.put("SD_TA", "taian");
		oldCLMap.put("HN_LY", "luoyang");
		oldCLMap.put("SD_JINAN", "jinan");
		oldCLMap.put("SD_JN", "jining");
		oldCLMap.put("HN_JY", "jiyuan");
		oldCLMap.put("HK", "haikou");
		oldCLMap.put("SD_ZB", "zibo");
		oldCLMap.put("GD_SZ", "shenzhen");
		oldCLMap.put("ZJ_WZ", "wenzhou");
		oldCLMap.put("SX_WN", "weinan");
		oldCLMap.put("SD_BZ", "binzhou");
		oldCLMap.put("SD_WF", "weifang");
		oldCLMap.put("SD_YT", "yantai");
		oldCLMap.put("HN_JZ", "jiaozuo");
		oldCLMap.put("YX", "yuxi");
		oldCLMap.put("GD_ZH", "zhuhai");
		oldCLMap.put("HB_SJZ", "shijiazhuang");
		oldCLMap.put("HB_QHD", "qinhuangdao");
		oldCLMap.put("SD_LC", "liaocheng");
		oldCLMap.put("JS_SZ", "su1zhou");
		oldCLMap.put("SD_LW", "laiwu");
		oldCLMap.put("SD_HZ", "heze");
		oldCLMap.put("LN_YK", "yingkou");
		oldCLMap.put("LN_HLD", "huludao");
		oldCLMap.put("HB_HS", "hengshui");
		oldCLMap.put("QH_XN", "xining");
		oldCLMap.put("SX_XA", "xian");
		oldCLMap.put("HN_XC", "xuchang");
		oldCLMap.put("GY", "guiyang");
		oldCLMap.put("LN_LY", "liaoyang");
		oldCLMap.put("JS_LYG", "lianyungang");
		oldCLMap.put("HB_XT", "xingtai");
		oldCLMap.put("HB_HD", "handan");
		oldCLMap.put("CQ", "chongqing");
		oldCLMap.put("ZJ_JH", "jinhua");
		oldCLMap.put("YC", "yinchuan");
		oldCLMap.put("LN_JZ", "jinzhou");
		oldCLMap.put("JS_ZJ", "zhenjiang");
		oldCLMap.put("HUN_CS", "changsha");
		oldCLMap.put("LN_FX", "fuxin");
		oldCLMap.put("SD_QD", "qingdao");
		oldCLMap.put("LN_AS", "anshan");
		oldCLMap.put("HN_ZMD", "zhumadian");
		oldCLMap.put("HN_HB", "hebi");

		return oldCLMap;
	}
}