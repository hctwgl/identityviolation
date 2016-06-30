package com.mapbar.city.deprecated;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.OpenCarHomeParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class OpenCarHomeService implements Transfer {

	public static String OPEN_CAR_GET_VIOLATION_URL = "http://open.wz.qichecdn.com/violaton/GetViolation";

	OpenCarHomeParser parser = new OpenCarHomeParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";
		String strResp = "";
		try {
			String strData = ConfigUtils.getCitySourceRole(car.getCityPy(), OpenCarHomeService.class.getSimpleName());

			int isEng = new Integer(strData.split(",")[4]);
			int isBtm = new Integer(new Integer(strData.split(",")[5]));
			String engId = car.getCityPy();
			String btmId = car.getCityPy();
			String cId = strData.split(",")[1];
			String VinAndEin = "";

			if (isEng != 0) {
				if (!StringUtil.isNotEmpty(engId) || engId.equals("选填")) {
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号为空！");
				}
				if (isEng != -1) {
					if (engId.length() < isEng) {
						return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
					}
					if (engId.length() > isEng) {
						engId = engId.substring(engId.length() - isEng, engId.length());
					}
					VinAndEin += "&enginenumber=" + engId;

				} else {
					VinAndEin += "&enginenumber=" + engId;

				}
			} else {
				VinAndEin += "&enginenumber=";
			}
			if (isBtm != 0) {
				if (!StringUtil.isNotEmpty(btmId) || btmId.equals("选填")) {
					return ResultCache.toErrJsonResult("车辆信息错误,车架号为空！");
				}
				if (isBtm != -1) {
					if (btmId.length() < isBtm) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
					}
					if (btmId.length() > isBtm) {
						btmId = btmId.substring(btmId.length() - isBtm, btmId.length());
					}
					VinAndEin += "&framenumber=" + btmId;

				} else {
					VinAndEin += "&framenumber=" + btmId;

				}
			} else {
				VinAndEin += "&framenumber=";
			}
			String postUrl = OPEN_CAR_GET_VIOLATION_URL;
			String postData = "platform_id=16&cityid=" + cId + "&cip=" + PropertiesUtils.getProValue("OPEN_CAR_HOME_CIP") + "&platenum=" + car.getCityPy() + car.getCityPy() + VinAndEin;
			// postData = postData.replaceAll("\n", "");
			// String postData = "platform_id=16&cityid=410100&cip="+PropertiesUtils.getProValue("OPEN_CAR_HOME_CIP")
			// +"&platenum=豫A567RN&framenumber=291315";
			System.out.println(postData);
			strResp = HttpsUtils.postURLContents(postUrl, postData);
			System.out.println(strResp);
			ret = parser.parse(strResp, car);
			// System.out.println(ret);
			// if(StringUtil.isValid(strResp)){
			// if(strResp.contains("\"returncode\":0")){
			// JSONObject jresp = new JSONObject(strResp);
			// JSONObject job = jresp.getJSONObject("result");
			// JSONArray citys = job.getJSONArray("citys");
			// for(int i=0;i<citys.length();i++){
			// JSONObject object = (JSONObject)citys.get(i);
			// JSONArray violationdata = object.getJSONArray("violationdata");
			// for(int j=0;j<violationdata.length();j++){
			// JSONObject vio = (JSONObject)violationdata.get(j);
			//
			// }
			// }
			//
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.doMkLogData_carhome("", car);
			return ret;
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_carhome("", car);
		} else {
			LogUtil.doMkLogData_carhome(strResp, car);
		}
		return ret;
	}

	@SuppressWarnings("unused")
	private String lookupViolation2(DriverProfile car) {
		String ret = "";
		// car.set("京", "Q93S23", "AB594017", "选填", "北京", "beijing");
		// car.set("京", "QH1W08", "86100560", "选填", "北京", "beijing");
		// 黑龙江 哈尔滨 haerbin 黑ACE271 车架号：Lkhgf1ah58ac31329 发动机号：选填

		String postUrl = "http://open.wz.qichecdn.com/violaton/GetViolation";
		// car.set("京", "PC3U67", "076078", "选填", "北京", "beijing");
		// String postData="platform_id=16&cityid=110100&cip=119.255.37.166&platenum=京PC3U67&enginenumber=076078&framenumber=";
		// car.set("鲁", "UT1761", "", "1022", "青岛", "qingdao");
		//String postData = "platform_id=16&cityid=110100&cip=119.255.37.166&platenum=京QH1W08&enginenumber=86100560&framenumber=";
		String postData = "platform_id=16&cityid=410100&cip=" + PropertiesUtils.getProValue("OPEN_CAR_HOME_CIP") + "&platenum=豫A567RN&framenumber=291315";
		// String postData="platform_id=16&cityid=370200&cip=119.255.37.166&platenum=鲁UT1761&enginenumber=&framenumber=1022";
		// String postData="platform_id=16&cityid=370200&cip=119.255.37.166&platenum=鲁UT1761&enginenumber=&framenumber=1021";
		// 黑龙江 哈尔滨 haerbin 黑A1P870 车架号：LBEHDAEB5DY064451

		// String postData="platform_id=16&cityid=230100&cip=119.255.37.166&platenum=黑A1P870&enginenumber=&framenumber=LBEHDAEB5DY064451";
		// String postData = "platform_id=16&cityid=370900&cip=119.255.37.166&platenum=鲁J6N587&enginenumber=&framenumber=180332";
		// car.set("鲁", "MC6821", "", "017372", "滨州", "binzhou");
		// String postData="platform_id=16&cityid=371600&cip=119.255.37.166&platenum=鲁MC6821&enginenumber=&framenumber=017372";
		// System.out.println(postData);
		// System.out.println(strResp);

		try {
			Map<String, String> postData1 = new HashMap<String, String>();
			String strResp = HttpClientUtil.postURLContents(postUrl, postData1);
			JSONObject jresp = JSONObject.parseObject(strResp);
			JSONObject job = jresp.getJSONObject("result");
			JSONArray citys = job.getJSONArray("citys");
			JSONObject object = (JSONObject) citys.get(0);
			if (object.getString("authimage") != null && !"".equals(object.getString("authimage"))) {
				String image = object.getString("authimage");
				// System.out.println(image);
				// BASE64Decoder decoder = new BASE64Decoder();
				Base64 decoder = new Base64();
				try {
					byte[] b = decoder.decode(image);
					for (int i = 0; i < b.length; ++i) {
						if (b[i] < 0) {// 调整异常数据
							b[i] += 256;
						}
					}
					// 生成jpeg图片
					String imgFilePath = "d://222.jpg";// 新生成的图片
					OutputStream out = new FileOutputStream(imgFilePath);
					out.write(b);
					out.flush();
					out.close();

					File img = new File(imgFilePath);
					img.length();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// BASE64Encoder encoder = new BASE64Encoder();
				// encoder.encode(arg0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
