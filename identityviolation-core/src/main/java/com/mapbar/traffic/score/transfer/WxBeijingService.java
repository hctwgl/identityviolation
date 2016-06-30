package com.mapbar.traffic.score.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.WxBeijingImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.WxBeijingParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class WxBeijingService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://bjjj.bjjtgl.gov.cn/jgjapp/ui/driver/createCode.htm?date=";
	// 支付宝接口
	// private static final String GET_VIOLATION_URL = "http://123.56.44.212/server_bj/bj_traffic/service/CW0101";
	// private static final String STRING_REFERER1 = "http://123.56.44.212/beijing/app_driver_query/driver_query_1.html";
	// 微信接口
	private static final String GET_VIOLATION_URL = "http://bjjj.bjjtgl.gov.cn/jgjapp/ui/driver/selectDriver.htm?";
	private static final String STRING_REFERER = "http://bjjj.bjjtgl.gov.cn/jgjapp/ui/driver/toDriver.htm?userId=ovhaojsPfm0TPyaFz4FQq-KazHMg&snsId=6e47af3b61d7468c8417c9ad45de6983";
	private static final String IMAGE_TYPE = "jpg";
	WxBeijingParser parser = new WxBeijingParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";

		List<String> vCookies = new ArrayList<String>();

		int Loop = 0;

		while (Loop < 3) {
			Loop++;
			try {

				HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "UTF-8");
				// System.out.println(index);
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());
				// System.out.println("my vCookies==="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}

				WxBeijingImageFilter imageFilter = new WxBeijingImageFilter();
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.toUpperCase();
					code = code.replaceAll("}", "J");
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println(" WxBeijingService validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println(" WxBeijingService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				// 微信请求参数
				String urlData = "driverName=" + URLEncoder.encode(driverProfile.getDriverName(), "UTF-8") + "&licenseNO=" + driverProfile.getDriverLicense() + "&licenseDate=" + driverProfile.getLssueDate() + "&yanzhengma=" + code + "&date=" + Math.random();
				// JSONObject json = new JSONObject(true);
				// JSONObject body = new JSONObject(true);
				// JSONObject head = new JSONObject(true);
				// body.put("driverName", driverProfile.driverName);
				// body.put("licenseNO", driverProfile.driverLicense);
				// head.put("siteid", "371000");
				// head.put("appid", "WX-0631-0001");
				// head.put("sign", "4fb3b993a455ae2d9a443eb774bbcf39859c1751dbcefbb0afe2b9033a47a877ef5f4ce33622550e");
				// head.put("version", "2.0");
				// json.put("head", head);
				// json.put("body", body);
				// 支付宝请求参数
				// String urlData=json.toString();
				// String postDate = "CXLXMC=jdcjtwf&YANZHEN="+code+"&CPHM="+car.strState+car.strRegId+"&CJH="+btmId+"&JDCLX=02";
				// System.out.println("postDate=========" + urlData);
				// 微信接口参数错误：{"success":false,"msg":"查询驾驶员违法数据失败","flag":"-1"}
				// {"flag":"-1","msg":"输入信息不匹配","success":false}
				// {"success":false,"msg":"验证码输入有误","flag":"-2"}
				// 微信接口参数成功：
				// {"flag":"0","data":{"carType":"C1","clsj":"2007-08-02","driverIllegalRecords":[],"driverid":"110102198403******","driverstate":"正常","integral":"0分","m12fsj":"","name":"赵峰","returnStatus":"SUCCESS","returnStatusMsg":"","scqfsj":"2015-08-01","tjrq":"2023-08-02","validity":"从2013-08-02至2023-08-02"},"msg":"暂无违法记录","success":true}
				// 支付宝接口参数错误： {"head":{"rtnMsg":"验签失败","rtnCode":"900901"},"body":{}}
				// 支付宝接口请求成功：
				// {"head":{"rtnMsg":"本次请求成功!","rtnCode":"000000"},"body":[{"name":"赵峰","driverid":"110102198403201553","driverstate":"正常","validity":"从2013-08-02至2023-08-02","clsj":"2007-08-02","scqfsj":"2015-08-01","integral":"0","m12fsj":"","carType":"C1","tjrq":"2023-08-02","error_message":1}]}
				String repStr = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL + urlData, vCookies, STRING_REFERER, next);// 微信请求
				// String repStr = NetHelper.postURLContentsWithCookiesForAjax(GET_VIOLATION_URL,urlData, vCookies, STRING_REFERER1,"UTF-8" ,next);//支付宝请求
				// System.out.println(repStr);
				if (StringUtil.isNotEmpty(repStr) && repStr.contains("\"flag\":\"-1\"") && repStr.contains("输入信息不匹配")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或者初次领证日期错误！");
				}
				if (StringUtil.isNotEmpty(repStr) && (repStr.contains("\"flag\":\"0\"") || repStr.contains("\"flag\":\"1\"")) && repStr.contains("\"success\":true")) {
					ret = parser.parse(repStr, driverProfile);
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(repStr) && !repStr.contains("\"flag\":\"-1\"")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
					return "";
				}

			} catch (SocketTimeoutException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接超时");

				e.printStackTrace();
			} catch (FileNotFoundException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}

}
