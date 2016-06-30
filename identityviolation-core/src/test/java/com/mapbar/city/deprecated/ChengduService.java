package com.mapbar.city.deprecated;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.ImageIOHelper;
import com.mapbar.traffic.score.image.citys.ChengduImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ChengduParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class ChengduService implements Transfer {

	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=1417069983578
	private static final String VALIDATE_IMAGE_URL = "http://www.cdjg.gov.cn:8088/WebService/Yzm.aspx?val=";

	private static final String GET_VIOLATION_URL = "http://www.cdjg.gov.cn/WebService/OnlineWork/QueryDrvOrVeh/QueryVehDetail.aspx";
	private static final String STRING_REFERER = "http://www.cdjg.gov.cn/WebService/OnlineWork/QueryDrvOrVeh/QueryVeh.aspx?code=1";
	private static final String IMAGE_TYPE = "gif";
	private ChengduParser parse = new ChengduParser();

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
		String strResp = null;
		try {
			Vector<String> vCookies = new Vector<String>();
			String btmId = "";
			if (btmId.length() > 8) {
				btmId = btmId.substring(btmId.length() - 8, btmId.length());
			} else if (btmId.length() < 8) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}
			int Loop = 0;
			while (Loop < 0) {
				Loop++;
				// vCookies.clear();

				try {
					String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
					// System.out.println(indexStr);
					Map<String, String> hiddenMap = parse.parseHidden(indexStr);

					String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
					String paramDate = parse.getParamData(hiddenMap);
					// 校验验证码存储目录是否存在 不存在则创建
					String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					// 得到图片
					HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

					File ff = new File(validataCodeImage);
					if (!ff.exists()) {
						continue;
					}
					String checkCode = getCheckCode(validataCodeImage, filePath);
					if (checkCode.length() < 4) {

						continue;
					}

					paramDate = paramDate + "&ctl00$ContentPlaceHolder1$ddlHpzl=02&ctl00$ContentPlaceHolder1$txtHphm=" + "" + "&ctl00$ContentPlaceHolder1$txtClsbdh=" + "" + "&ctl00$ContentPlaceHolder1$txtYzm=" + checkCode + "&ctl00$ContentPlaceHolder1$hidCode=code%3D1";
					Map<String, String> params = new HashMap<String, String>();
					String tempRepStr = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, vCookies, GET_VIOLATION_URL, "UTF-8", next);
					// System.out.println("tempRepStr1========"+tempRepStr);
					if (tempRepStr.contains("车辆识别代号错误")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					}
					if (tempRepStr.contains("__EVENTVALIDATION") && tempRepStr.contains("")) {

						if (tempRepStr.contains("<span id=\"lblJdczt\">正常</span>")) {
							LogUtil.doMkLogData_jiaoguanju(car, "ok");
							return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						}

						Map<String, String> hiddenMap2 = parse.parseHidden(tempRepStr);

						String postDate = parse.getParamData(hiddenMap2);

						postDate=postDate += "&ctl00$ContentPlaceHolder1$ibtnWf.x=0&ctl00$ContentPlaceHolder1$ibtnWf.y=0";
						Map<String, String> params1 = new HashMap<String, String>();
						// System.out.println("postDate========"+postDate);
						tempRepStr = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params1, vCookies, GET_VIOLATION_URL, "UTF-8", next);
						// System.out.println("tempRepStr2========"+tempRepStr);
						strResp = parse.parse(tempRepStr, car);
						if (StringUtil.isNotEmpty(strResp)) {
							LogUtil.doMkLogData_jiaoguanju(car, "ok");
							return strResp;
						}

					} else {
						strResp = "";
					}
					Thread.sleep(400);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.doMkLogData_jiaoguanju(car, "err");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
		}
		if (!StringUtil.isNotEmpty(strResp)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return strResp;
	}

	private String getCheckCode(String validataCodeImage, String filePath) {
		String valCode = "";
		String newFile = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
		// System.out.println("newFile========"+newFile);
		// String newFile2 = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
		String codeFileP = filePath + "/" + UUID.randomUUID();
		ChengduImageFilter filter = new ChengduImageFilter();
		BufferedImage bufferedImage = null;
		BufferedImage binaryBufferedImage = null;
		File ff = null;
		try {
			ff = new File(validataCodeImage);
			bufferedImage = ImageIOHelper.getImage(ff);

			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);

					if (x <= 1 || x >= (w - 2)) {
						argb = -1;
					}
					if (y <= 1 || y >= (h - 2)) {
						argb = -1;
					}

					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;

					int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					gray[x][y] = grayPixel;

				}
			}
			// 二值化
			int threshold = filter.ostu(gray, w, h);

			binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
			int[][] gray2 = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					// System.out.println("x=="+x + "  y=="+y);
					if (gray[x][y] > threshold) {
						gray2[x][y] = 0x00FFFF; // 白色// 白色
					} else {
						gray2[x][y] = 0x00FFFF; // 白色// 白色

						if ((y + 1) < h && (y - 1) >= 0) {
							if (gray[x][y + 1] <= threshold && gray[x][y - 1] <= threshold) {
								gray2[x][y] = 0xFF0000;
							}
						}
					}
					// 16711680 黑色
					// 65535 白色
					// System.out.println("x==="+x+"  , y===="+y + ", argb =="+gray[x][y]);

					// binaryBufferedImage.setRGB(x, y, gray2[x][y]);
				}
			}
			// ImageIO.write(binaryBufferedImage, "jpg", new File(
			// newFile));
			int[][] gray3 = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					// System.out.println("x=="+x + "  y=="+y);

					if ((y + 1) < h && (y - 1) >= 0) {
						if (gray2[x][y + 1] == 65535 && gray2[x][y - 1] == 65535) {
							gray3[x][y] = 65535;
						} else {
							gray3[x][y] = gray2[x][y];
						}
					} else {
						gray3[x][y] = gray2[x][y];
					}
					if ((x + 1) < w && (x - 1) >= 0) {
						if (gray2[x + 1][y] == 65535 && gray2[x - 1][y] == 65535) {
							gray3[x][y] = 65535;
						} else {
							gray3[x][y] = gray2[x][y];
						}
					} else {
						gray3[x][y] = gray2[x][y];
					}
					// 16711680 黑色
					// 65535 白色
					// System.out.println("x==="+x+"  , y===="+y + ", argb =="+gray[x][y]);

					binaryBufferedImage.setRGB(x, y, gray3[x][y]);
				}
			}
			ImageIO.write(binaryBufferedImage, "jpg", new File(newFile));
			valCode = new OCR().recognizeText(new File(newFile), "jpg", codeFileP);
			valCode.replace("¥", "F");
			System.out.println("valCode=====" + valCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ff != null) {
				ff.deleteOnExit();
			}
			if (bufferedImage != null) {
				bufferedImage.flush();
			}
			if (binaryBufferedImage != null) {
				binaryBufferedImage.flush();
			}
		}

		return valCode;
	}
}
