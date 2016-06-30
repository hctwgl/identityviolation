package com.mapbar.traffic.score.transfer.citys;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.PanjinParser;
import com.mapbar.traffic.score.transfer.FreshYzm;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityPanjinService implements Transfer, FreshYzm {
	private static final String VALIDATE_IMAGE_URL = "http://jjd.panjin.gov.cn/verifycode.php";
	// http://jjd.panjin.gov.cn/mainquery.php?txtcph=%C1%C9L80B87&verify0=7a9q&txtclsbh=1863&chd=%D0%A1%D0%CD%C6%FB%B3%B5&jszh=&dah=&verify1=&hType=w
	private static final String GET_VIOLATION_URL = "http://jjd.panjin.gov.cn/mainquery.php?";

	private static final String STRING_REFERER = "http://jjd.panjin.gov.cn/";
	private static final String IMAGE_TYPE = "png";
	private static final int width = 80;
	private static final int hight = 40;
	PanjinParser parser = new PanjinParser();

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

	@Override
	public String refreshYzm(String cookie) {
		Vector<String> vCookies = new Vector<String>();
		try {
			if (StringUtil.isNotEmpty(cookie)) {
				vCookies.add(cookie);
				byte[] bt;
				bt = HttpsUtils.getUrlImageByteWithCookies(VALIDATE_IMAGE_URL, vCookies, STRING_REFERER, null);

				System.out.println("vCookies====" + vCookies);
				// BASE64Encoder encoder = new BASE64Encoder();
				// String str = encoder.encode(bt);
				Base64 encoder = new Base64();
				String str = encoder.encodeAsString(bt);
				JSONObject jcases = new JSONObject();
				jcases.put("msg", "违章查询-刷新验证码");
				jcases.put("source", str);
				jcases.put("width", width);
				jcases.put("height", hight);
				jcases.put("sid", URLEncoder.encode(vCookies.get(0), "UTF-8"));
				jcases.put("cs", this.getClass().getSimpleName());
				return jcases.toString();
				// return YzmParser.yzmparseForFresh("违章查询-刷新验证码", str, cookie,this.getClass().getSimpleName(), width, hight);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";
		try {
			Vector<String> vCookies = new Vector<String>();
			String btmId = "";
			if (btmId.length() > 4) {
				btmId = btmId.substring(btmId.length() - 4, btmId.length());
			} else if (btmId.length() < 4) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}
			JSONObject jcases = null;
			System.out.println(car.getSession() == null ? "session is null" : car.getSession());
			if (StringUtil.isNotEmpty(car.getSession()) && StringUtil.isNotEmpty(car.getValCode())) {
				car.setSession(URLDecoder.decode(car.getSession(), "UTF-8"));
				vCookies.add(car.getSession());
				// http://jjd.panjin.gov.cn/mainquery.php?txtcph=%C1%C9L80B87&verify0=7a9q&txtclsbh=1863&chd=%D0%A1%D0%CD%C6%FB%B3%B5&jszh=&dah=&verify1=&hType=w
				// String urlData="txtcph="+URLEncoder.encode(car.strState, "GBK")+car.strRegId+"&verify0="+car.valCode+"&txtclsbh="+btmId+"&chd=%D0%A1%D0%CD%C6%FB%B3%B5&jszh=&dah=&verify1=&hType=w";
				String urlData = "";
				String result = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + urlData, vCookies, "http://jjd.panjin.gov.cn/index.php", next, "GBK");
				System.out.println(result);
				if (result.contains("车牌号与识别号不相符，请核对后查询")) {
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或者车牌号错误！");
				}
				if (result.contains("请正确输入验证码")) {
					System.out.println("验证码输入错误");
					byte[] bt = HttpsUtils.getUrlImageByteWithCookies(VALIDATE_IMAGE_URL, vCookies, STRING_REFERER, next);
					System.out.println("vCookies====" + vCookies);
					// BASE64Encoder encoder = new BASE64Encoder();
					// String str = encoder.encode(bt);
					Base64 encoder = new Base64();
					String str = encoder.encodeAsString(bt);
					jcases = new JSONObject();
					jcases.put("msg", "验证码验证失败，请您重新输入!");
					jcases.put("source", str);
					jcases.put("width", width);
					jcases.put("height", hight);
					jcases.put("sid", URLEncoder.encode(vCookies.get(0), "UTF-8"));
					jcases.put("cs", this.getClass().getSimpleName());
					return jcases.toString();
					// return YzmParser.yzmparseForErr("验证码验证失败", str, car.session,this.getClass().getSimpleName() , width, hight);
				} else {
					if (result.contains("最新更新时间:") && result.contains("违法行为")) {
						System.out.println("查询成功");
						ret = parser.parse(result);
						return ret;

					}
				}

			} else {
				if (StringUtil.isNotEmpty(car.getSession())) {
					car.setSession(URLDecoder.decode(car.getSession(), "UTF-8"));
					vCookies.add(car.getSession());
				}
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				byte[] bt = HttpsUtils.getUrlImageByteWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next);
				if (bt == null) {
					return ResultCache.toErrJsonResult("验证码获取失败，请稍后再试！");
				}
				System.out.println("vCookies====" + vCookies);
				// BASE64Encoder encoder = new BASE64Encoder();
				// String str = encoder.encode(bt);
				Base64 encoder = new Base64();
				String str = encoder.encodeAsString(bt);
				// System.out.println(str);

				// BASE64Decoder decoder = new BASE64Decoder();
				try {
					byte[] b = encoder.decode(str);
					for (int i = 0; i < b.length; ++i) {
						if (b[i] < 0) {// 调整异常数据
							b[i] += 256;
						}
					}
					// 生成jpeg图片
					// String imgFilePath = validataCodeImage";//新生成的图片
					OutputStream out = new FileOutputStream(validataCodeImage);
					out.write(b);
					out.flush();
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				jcases = new JSONObject();
				jcases.put("msg", "该城市查询需要支持验证码，如未弹出验证码图片，请更新系统到最新版本，给您带来的不变请您谅解，谢谢!");
				jcases.put("source", str);
				jcases.put("width", width);
				jcases.put("height", hight);
				jcases.put("sid", URLEncoder.encode(vCookies.get(0), "UTF-8"));
				jcases.put("cs", this.getClass().getSimpleName());
				return jcases.toString();
				// return YzmParser.yzmparseForInit("验证码", str, URLEncoder.encode(vCookies.get(0), "UTF-8"), this.getClass().getSimpleName(), width, hight);
			}

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
