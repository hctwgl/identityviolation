package com.mapbar.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.ServiceConfig;
import com.mapbar.traffic.score.utils.ConfigUtils;

public class Tools {

	private static Map<String, String> sourceMap = new HashMap<String, String>();
	static {
		// sourceMap.put("Car100Service", "车100");
		// WecheService
		// OpenCarHomeService
		sourceMap.put("WecheService", "微车");
		sourceMap.put("ChengduService", "成都交管所");
		sourceMap.put("ChongqingService", "重庆交管所");
		sourceMap.put("GuangzhouService", "广州交管所");
		sourceMap.put("HuBeiService", "湖北交管所");
		sourceMap.put("NanjingService", "南京交管所");
		sourceMap.put("ShanghaiService", "上海交管所");
		sourceMap.put("ShanxiService", "山西交管所");
		sourceMap.put("ShenyangService", "沈阳交管所");
		sourceMap.put("ShenzhenService", "深圳交管所");
		sourceMap.put("XiAnService", "西安交管所");
		sourceMap.put("YunnanService", "云南交管所");
		sourceMap.put("ZhejiangService", "浙江交管所");

		sourceMap.put("ShanxiService", "陕西交管所");
		sourceMap.put("GuizhouService", "贵州交管所");
		sourceMap.put("JilinService", "吉林交管所");
		sourceMap.put("AnhuiService", "安徽交管所");
		sourceMap.put("ZhejiangService", "浙江交管所");
		sourceMap.put("CityDalianService", "大连市交管所");

		sourceMap.put("HebeiService", "河北交管所");
		sourceMap.put("GansuService", "甘肃交管所");
		sourceMap.put("HainanService", "海南交管所");
		sourceMap.put("HeilongjiangService", "黑龙江交管所");
		sourceMap.put("NingxiaService", "宁夏交管所");
		sourceMap.put("XinjiangService", "新疆交管所");
		sourceMap.put("NingxiaService", "宁夏交管所");

		sourceMap.put("OpenCarHomeService", "汽车之家");
	}

	@Test
	public void test() {
		// printcsv();
		// merge();
		// Map<String,String> citys = ServiceConfig.getCityBaseRule();
		// System.out.println(citys.get("chengdu"));
		// for(Map.Entry<String, String> ent:citys.entrySet()){
		// if("chengdu".equals(ent.getKey())){
		//
		// }
		// }
		// matchConfig();
		// matchConfig2();
		// printcsv();
		// printcsv();
		// matchConfig_carHome();
		// matchConfiged();
		// wecarSqlMake();
		printSourceConfig();
		// matchConfig_carHome();
		// make_openHome_sql();
	}

	public static void printSourceConfig() {
		String oldConfig = "D:/cityconfig/MapCityList.txt";
		try {
			InputStream inputStream = new FileInputStream(new File(oldConfig));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			List<String> oldlist = new ArrayList<String>();
			while ((lineTxt = bufferedReader.readLine()) != null) {
				oldlist.add(lineTxt);
			}

			List<String> result = new ArrayList<String>();
			result.add("省,市,城市拼音,数据源中文,数据源Class名称");
			for (String old : oldlist) {
				String temp = "";

				String str[] = old.split(",");
				String n = str[0];
				String p = str[1];

				String py = str[3];
				// temp+=p+","+n+","+py;
				String sourceclasses = ConfigUtils.getCitySources(py) == null ? "" : ConfigUtils.getCitySources(py);
				String sourceClassName = "";
				String source = "";
				String classes[] = sourceclasses.split(",");
				for (String ccc : classes) {
					if (sourceMap.containsKey(ccc)) {
						source += ccc + "、";
						sourceClassName += sourceMap.get(ccc) + "、";
					}
				}
				System.out.println("sourceclasses==" + sourceclasses);
				System.out.println("sourceClassName===" + sourceclasses);
				temp += p + "," + n + "," + py + "," + source.substring(0, source.length() - 1) + "," + sourceClassName.substring(0, sourceClassName.length() - 1);
				result.add(temp);
			}

			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/citySourceConfig_20150414.csv");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				for (String s : result) {
					nomatchbw.write(s);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void wecarSqlMake() {
		String matchFile = "D:/cityconfig/MapCityList.txt";

		try {
			InputStream inputStream = new FileInputStream(new File(matchFile));
			InputStreamReader read;

			read = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			List<String> sqlList = new ArrayList<String>();
			int i = 2000;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				i++;
				String str[] = lineTxt.split(",");

				String sql = "insert into city_source_config  values (" + i + ",'" + str[0] + "','" + str[3] + "','WecarService','WecarService','',4,1);";
				sqlList.add(sql);
			}
			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/wecar.txt");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				for (String s : sqlList) {
					nomatchbw.write(s);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public ResultForCitys parserXml(String str) {
	// ResultForCitys citys = new ResultForCitys();
	// SAXReader saxReader = new SAXReader();
	// Document document;
	// try {
	// document = saxReader.read(str);
	// Element root =document.getRootElement();
	// Element citylist=root.element("citys");
	// List elist=citylist.elements("city");
	// for (Iterator it = elist.iterator(); it.hasNext();) {
	// Element elm = (Element) it.next();
	// citys.addObject(new ResultForCity(elm.getText()));
	// }
	// } catch (DocumentException e) {
	// e.printStackTrace();
	// }
	// return citys;
	// }

	public static void make_openHome_sql() {
		String oldConfig = "D:/cityconfig/MapCityList.txt";
		String openHome = "D:/cityconfig/cityRuleMatch_carhome.csv";
		String carhome = "D:/cityconfig/qichezhijia.txt";
		try {

			String citys = "新乡,开封,三门峡,周口,济源,济南,潍坊,无锡,乌鲁木齐,巴音郭楞,伊犁,克拉玛依,阿克苏,喀什,哈密,和田,昌吉,吐鲁番,阿勒泰,塔城,博尔塔拉,克孜勒苏,石河子,绍兴县,嘉兴,那曲,昆明,深圳,中山,珠海,潮州,益阳,乌海";
			Set<String> citySet = new HashSet<String>();
			for (String c : citys.split(",")) {
				citySet.add(c);
			}

			Map<String, String> cityInfo = new HashMap<String, String>();

			InputStream inputStream = new FileInputStream(new File(oldConfig));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] ss = lineTxt.split(",");
				cityInfo.put(ss[0], ss[3]);
			}

			String txt = null;

			InputStream inputStream1 = new FileInputStream(new File(carhome));
			List<String> newList = new ArrayList<String>();
			InputStreamReader read1 = new InputStreamReader(inputStream1, "gbk");
			BufferedReader bufferedReader1 = new BufferedReader(read1);

			StringBuffer bf = new StringBuffer();
			while ((txt = bufferedReader1.readLine()) != null) {
				bf.append(txt);
			}
			JSONObject json = JSONObject.parseObject(bf.toString());
			JSONObject res = json.getJSONObject("result");
			JSONArray jarr = res.getJSONArray("items");
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject job = jarr.getJSONObject(i);
				String pname = job.getString("provincename");
				String name = job.getString("cityname");
				String vin = job.getInteger("enginenumlen") + "";
				String cityCode = job.getString("cityid");
				String ein = job.getInteger("framenumlen") + "";
				if (vin.equals("99")) {
					vin = "-1";
				}
				if (ein.equals("99")) {
					ein = "-1";
				}
				newList.add(pname + "," + name + "," + vin + "," + ein + "," + cityCode);
			}

			InputStream inputStream0 = new FileInputStream(new File(openHome));
			InputStreamReader read0 = new InputStreamReader(inputStream0, "utf-8");
			BufferedReader bufferedReader0 = new BufferedReader(read0);
			String txt1 = null;
			int i = 3200;
			List<String> sqlList = new ArrayList<String>();
			while ((txt1 = bufferedReader0.readLine()) != null) {
				String str[] = txt1.split(",");
				System.out.println(txt1);
				// if("TRUE".equalsIgnoreCase(str[4]) && "TRUE".equalsIgnoreCase(str[9])){
				String cityName = str[1];

				if (citySet.contains(cityName)) {
					String py = cityInfo.get(cityName);
					System.out.println(cityName);
					String cityPro = str[0];
					String cf = "";
					// String config = configMap.get(cityName);
					for (String s : newList) {
						String sss[] = s.split(",");
						String cname = sss[1];
						if (cname.contains(cityName)) {
							cf = py + "," + sss[4] + "," + cityPro + "," + cityName + "," + sss[2] + "," + sss[3];
							break;
						}
					}
					// String sss[] = config.split(",");

					// OpenCarHomeService

					String sql = " insert into city_source_config  values (" + (i++) + ",'" + cityName + "','" + py + "','OpenCarHomeService','OpenCarHomeService','" + cf + "',3,1);";
					sqlList.add(sql);
				}
			}
			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/city_carhome3.sql");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				for (String s : sqlList) {
					nomatchbw.write(s);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
			bufferedReader1.close();
			bufferedReader0.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void matchConfig_carHome() {
		String oldConfig = "D:/cityconfig/MapCityList.txt";

		String carhome = "D:/cityconfig/qichezhijia.txt";

		try {
			List<String> newList = new ArrayList<String>();
			InputStream inputStream1 = new FileInputStream(new File(carhome));

			InputStreamReader read1;

			read1 = new InputStreamReader(inputStream1, "gbk");
			BufferedReader bufferedReader1 = new BufferedReader(read1);
			String txt = null;
			StringBuffer bf = new StringBuffer();
			while ((txt = bufferedReader1.readLine()) != null) {
				bf.append(txt);
			}
			JSONObject json = JSONObject.parseObject(bf.toString());
			JSONObject res = json.getJSONObject("result");
			JSONArray jarr = res.getJSONArray("items");
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject job = jarr.getJSONObject(i);
				String pname = job.getString("provincename");
				String name = job.getString("cityname");
				String vin = job.getInteger("enginenumlen") + "";
				String cityCode = job.getString("cityid");
				String ein = job.getInteger("framenumlen") + "";
				if (vin.equals("99")) {
					vin = "-1";
				}
				if (ein.equals("99")) {
					ein = "-1";
				}
				newList.add(pname + "," + name + "," + vin + "," + ein + "," + cityCode);
			}

			InputStream inputStream = new FileInputStream(new File(oldConfig));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			List<String> oldlist = new ArrayList<String>();
			while ((lineTxt = bufferedReader.readLine()) != null) {
				oldlist.add(lineTxt);
			}

			List<String> result = new ArrayList<String>();
			result.add("省,市,发动机号规则,车架号规则,是否有对应城市,汽车之家城市名,汽车之家发动机号规则,汽车之家车架号规则,城市ID,查询规则是否一致,");
			for (String old : oldlist) {
				String temp = "";

				String str[] = old.split(",");
				String n = str[0];
				String p = str[1];
				String vin = str[4];
				String ein = str[5];
				temp = p + "," + n + "," + (vin.equals("-1") ? "全部" : (vin.equals("0") ? "0" : ("后" + vin + "位"))) + "," + (ein.equals("-1") ? "全部" : (ein.equals("0") ? "0" : ("后" + ein + "位")));
				boolean isset = false;
				boolean ismatched = false;

				for (String sss : newList) {
					String ss[] = sss.split(",");
					String cname = ss[1];
					String nvin = ss[2];
					String nein = ss[3];
					String cityId = ss[4];
					if (n.equals(cname) || n.contains(cname) || cname.contains(n)) {
						if (vin.equals(nvin) && ein.equals(nein)) {
							ismatched = true;
						}
						temp += ",true," + cname + "," + (nvin.equals("-1") ? "全部" : (nvin.equals("0") ? "0" : ("后" + nvin + "位"))) + "," + (nein.equals("-1") ? "全部" : (nein.equals("0") ? "0" : ("后" + nein + "位"))) + "," + cityId;
						isset = true;
						newList.remove(sss);
						break;
					}
				}

				// temp = p+","+n+",后"+vin+"位,后"+ein+"位";
				if (!isset) {
					temp = p + "," + n + "," + (vin.equals("-1") ? "全部" : (vin.equals("0") ? "0" : ("后" + vin + "位"))) + "," + (ein.equals("-1") ? "全部" : (ein.equals("0") ? "0" : ("后" + ein + "位"))) + ",false,,,,";
				}
				if (ismatched) {
					temp += ",true";
				} else {
					temp += ",false";
				}
				// temp=temp.replaceAll("-1", "")
				result.add(temp);
			}
			if (newList.size() > 0) {
				for (String sss : newList) {
					String ss[] = sss.split(",");
					String pname = ss[0];
					String cname = ss[1];
					String nvin = ss[2];
					String nein = ss[3];
					String cid = ss[4];
					String temp = pname + "," + ",,,false" + "," + cname + "," + (nvin.equals("-1") ? "全部" : (nvin.equals("0") ? "0" : ("后" + nvin + "位"))) + "," + (nein.equals("-1") ? "全部" : (nein.equals("0") ? "0" : ("后" + nein + "位"))) + "," + cid + ",false";
					result.add(temp);
				}
			}
			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/cityRuleMatch_carhome.csv");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				for (String s : result) {
					nomatchbw.write(s);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
			bufferedReader1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void matchConfig() {
		String oldConfig = "D:/cityconfig/MapCityList.txt";

		String che100 = "D:/cityconfig/cityConfig.xml";

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(che100);
			Element root = document.getRootElement();
			// Element area=root.element("area");
			@SuppressWarnings("unchecked")
			List<Element> plist = root.elements("province");

			List<String> newList = new ArrayList<String>();
			for (int i = 0; i < plist.size(); i++) {
				Element pro = (Element) plist.get(i);
				String pname = pro.attributeValue("name");
				@SuppressWarnings("unchecked")
				List<Element> clist = pro.elements("city");
				for (int j = 0; j < clist.size(); j++) {
					Element city = (Element) clist.get(j);
					String name = city.attributeValue("name");
					// vin 发动机号
					String vin = city.attributeValue("vin") == null ? "0" : city.attributeValue("vin");

					// ein 车架号
					String ein = city.attributeValue("ein") == null ? "0" : city.attributeValue("ein");

					if (vin.equals("all")) {
						vin = "-1";
					}
					if (ein.equals("all")) {
						ein = "-1";
					}

					newList.add(pname + "," + name + "," + vin + "," + ein);
				}
			}

			InputStream inputStream = new FileInputStream(new File(oldConfig));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			List<String> oldlist = new ArrayList<String>();
			while ((lineTxt = bufferedReader.readLine()) != null) {
				oldlist.add(lineTxt);
			}

			List<String> result = new ArrayList<String>();
			result.add("省,市,发动机号规则,车架号规则,是否有对应城市,车100城市名,车100发动机号规则,车100车架号规则,查询规则是否一致");
			for (String old : oldlist) {
				String temp = "";

				String str[] = old.split(",");
				String n = str[0];
				String p = str[1];
				String vin = str[4];
				String ein = str[5];
				temp = p + "," + n + "," + (vin.equals("-1") ? "全部" : (vin.equals("0") ? "0" : ("后" + vin + "位"))) + "," + (ein.equals("-1") ? "全部" : (ein.equals("0") ? "0" : ("后" + ein + "位")));
				boolean isset = false;
				boolean ismatched = false;

				for (String sss : newList) {
					String ss[] = sss.split(",");
					String cname = ss[1];
					String nvin = ss[2];
					String nein = ss[3];
					if (n.equals(cname) || n.contains(cname) || cname.contains(n)) {
						if (vin.equals(nvin) && ein.equals(nein)) {
							ismatched = true;
						}
						temp += ",true," + cname + "," + (nvin.equals("-1") ? "全部" : (nvin.equals("0") ? "0" : ("后" + nvin + "位"))) + "," + (nein.equals("-1") ? "全部" : (nein.equals("0") ? "0" : ("后" + nein + "位")));
						isset = true;
						newList.remove(sss);
						break;
					}
				}

				// temp = p+","+n+",后"+vin+"位,后"+ein+"位";

				if (!isset) {
					temp = p + "," + n + "," + (vin.equals("-1") ? "全部" : (vin.equals("0") ? "0" : ("后" + vin + "位"))) + "," + (ein.equals("-1") ? "全部" : (ein.equals("0") ? "0" : ("后" + ein + "位"))) + ",false,,,";
				}
				if (ismatched) {
					temp += ",true";
				} else {
					temp += ",false";
				}
				// temp=temp.replaceAll("-1", "")
				result.add(temp);
			}
			if (newList.size() > 0) {
				for (String sss : newList) {
					String ss[] = sss.split(",");
					String pname = ss[0];
					String cname = ss[1];
					String nvin = ss[2];
					String nein = ss[3];

					String temp = pname + "," + ",,,false" + "," + cname + "," + (nvin.equals("-1") ? "全部" : (nvin.equals("0") ? "0" : ("后" + nvin + "位"))) + "," + (nein.equals("-1") ? "全部" : (nein.equals("0") ? "0" : ("后" + nein + "位"))) + ",false";
					result.add(temp);
				}
			}
			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/cityRuleMatch.csv");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				for (String s : result) {
					nomatchbw.write(s);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void matchConfiged() {
		String oldConfig = "D:/cityconfig/MapCityList.txt";
		String che100 = "D:/cityconfig/cityConfig.xml";
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(che100);
			Element root = document.getRootElement();
			// Element area=root.element("area");
			@SuppressWarnings("unchecked")
			List<Element> plist = root.elements("province");

			List<String> newList = new ArrayList<String>();
			for (int i = 0; i < plist.size(); i++) {
				Element pro = (Element) plist.get(i);
				String pname = pro.attributeValue("name");
				@SuppressWarnings("unchecked")
				List<Element> clist = pro.elements("city");
				for (int j = 0; j < clist.size(); j++) {
					Element city = (Element) clist.get(j);
					String name = city.attributeValue("name");
					// vin 发动机号
					String vin = city.attributeValue("vin") == null ? "0" : city.attributeValue("vin");
					// ein 车架号
					String ein = city.attributeValue("ein") == null ? "0" : city.attributeValue("ein");

					if (vin.equals("all")) {
						vin = "-1";
					}
					if (ein.equals("all")) {
						ein = "-1";
					}

					newList.add(pname + "," + name + "," + vin + "," + ein);
				}
			}

			InputStream inputStream = new FileInputStream(new File(oldConfig));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			List<String> oldlist = new ArrayList<String>();
			while ((lineTxt = bufferedReader.readLine()) != null) {
				oldlist.add(lineTxt);
			}

			List<String> result = new ArrayList<String>();
			// result.add("省,市,市拼音,发动机号规则,车架号规则,是否有对应城市,车100城市名,车100发动机号规则,车100车架号规则,查询规则是否一致");
			for (String old : oldlist) {
				String temp = "";

				String str[] = old.split(",");
				String n = str[0];
				String p = str[1];
				String vin = str[4];
				String ein = str[5];
				String py = str[3];
				temp = p + "," + n + "," + py + "," + vin + "," + ein;
				boolean isset = false;
				boolean ismatched = false;

				for (String sss : newList) {
					String ss[] = sss.split(",");
					String cname = ss[1];
					String nvin = ss[2];
					String nein = ss[3];
					if (n.equals(cname) || n.contains(cname) || cname.contains(n)) {
						if (vin.equals(nvin) && ein.equals(nein)) {
							ismatched = true;
						}
						temp += ",true," + cname + "," + nvin + "," + nein;
						isset = true;
						newList.remove(sss);
						break;
					}
				}

				// temp = p+","+n+",后"+vin+"位,后"+ein+"位";

				if (!isset) {
					temp = p + "," + n + "," + py + "," + vin + "," + ein + ",false,,,";
				}
				if (ismatched) {
					temp += ",true";
				} else {
					temp += ",false";
				}
				// temp=temp.replaceAll("-1", "")
				if (ismatched && isset) {
					result.add(temp);
				}

			}
			FileWriter nonewriter;
			try {
				nonewriter = new FileWriter("D:/cityconfig/cityRuleMatched.txt");
				BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
				int i = 1000;
				for (String s : result) {
					i++;
					String str[] = s.split(",");
					String rule = str[2] + "," + str[0] + "," + str[1] + "," + str[3] + "," + str[4];
					String sql = " insert into city_source_config  values (" + i + ",'" + str[1] + "','" + str[2] + "','Car100Service','Car100Service','" + rule + "',3,1);";
					nomatchbw.write(sql);
					nomatchbw.write("\r\n");
				}
				nomatchbw.flush();
				nomatchbw.close();
				nonewriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void printcsv() {
		String file = ServiceConfig.class.getClassLoader().getResource("").toString().replaceAll("file:/", "") + "/sohu.txt";

		List<String> resultlist = new ArrayList<String>();

		Set<String> sohuSet = new HashSet<String>();
		try {
			InputStream inputStream = new FileInputStream(new File(file));

			InputStreamReader read;

			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				sohuSet.add(lineTxt.split(",")[0]);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream inputStream2 = ServiceConfig.class.getClassLoader().getResourceAsStream("MapCityList.txt");
		InputStreamReader read2 = new InputStreamReader(inputStream2);
		BufferedReader bufferedReader2 = new BufferedReader(read2);

		String txt = null;
		try {
			while ((txt = bufferedReader2.readLine()) != null) {
				String str[] = txt.split(",");
				String line = str[0] + "," + str[1] + ",";
				if (sohuSet.contains(str[3])) {
					line += "搜狐源、微车源,";
				} else {
					line += "微车源,";
				}
				if (new Integer(str[4]) == 0) {
					line += " 发动机号 ";
				}
				if (new Integer(str[4]) > 0) {
					line += str[4] + "位发动机号 ";
				}
				if (new Integer(str[5]) == 0) {
					line += " 车架号 ";
				}
				if (new Integer(str[5]) > 0) {
					line += str[5] + "位车架号 ";
				}
				resultlist.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter nonewriter;
		try {
			nonewriter = new FileWriter("D:\\MapCityList.csv");
			BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
			for (String s : resultlist) {
				nomatchbw.write(s);
				nomatchbw.write("\r\n");
			}
			nomatchbw.flush();
			nomatchbw.close();
			nonewriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mkSohuSql() {
		// InputStream inputStream = ServiceConfig.class.getClassLoader()
		// .getResourceAsStream("sohu.cfg");
		// InputStreamReader read = new InputStreamReader(
		// inputStream);
		// BufferedReader bufferedReader = new BufferedReader(read);
		String file = ServiceConfig.class.getClassLoader().getResource("").toString().replaceAll("file:/", "") + "/cheshouye.txt";

		// .getResourceAsStream("MapCityList.txt");
		InputStreamReader read;
		String lineTxt = null;
		int i = 64;
		try {
			InputStream inputStream = new FileInputStream(new File(file));
			read = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String str[] = lineTxt.split(",");
				String py = str[0];
				StringBuffer sql = new StringBuffer();
				sql.append("insert into city_source_config  values (" + i + ",'" + str[1] + "','" + py + "','CheshouyeService','CheshouyeService','" + lineTxt + "',6,1);");
				System.out.println(sql.toString());
				i++;
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void merge() {
		List<String> plist = new ArrayList<String>();
		List<String> clist = new ArrayList<String>();

		InputStream inputStream = ServiceConfig.class.getClassLoader().getResourceAsStream("provinceMap.txt");
		InputStreamReader read = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(read);

		String lineTxt = null;
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				plist.add(lineTxt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				read.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		InputStream inputStream2 = ServiceConfig.class.getClassLoader().getResourceAsStream("MapCityList.txt");
		InputStreamReader read2 = new InputStreamReader(inputStream2);
		BufferedReader bufferedReader2 = new BufferedReader(read2);

		String txt = null;
		try {
			while ((txt = bufferedReader2.readLine()) != null) {
				clist.add(txt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader2.close();
				read2.close();
				inputStream2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> resultlist = new ArrayList<String>();
		for (String ct : clist) {
			System.out.println(ct);
			String cs[] = ct.split(",");

			for (String cp : plist) {
				String ps[] = cp.split(":");
				if (cs[1].equals(ps[0])) {
					String str = "";
					str += cs[0] + "," + cs[1] + "," + ps[1] + "," + cs[2] + "," + cs[3] + "," + cs[4];
					if (cs.length == 6) {
						str += "," + cs[5];
					}
					resultlist.add(str);
				}
			}
		}

		FileWriter nonewriter;
		try {
			nonewriter = new FileWriter("D:\\MapCityList.txt");
			BufferedWriter nomatchbw = new BufferedWriter(nonewriter);
			for (String s : resultlist) {
				nomatchbw.write(s);
				nomatchbw.write("\r\n");
			}
			nomatchbw.flush();
			nomatchbw.close();
			nonewriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
