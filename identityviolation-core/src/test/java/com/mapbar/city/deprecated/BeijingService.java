package com.mapbar.city.deprecated;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.jdesktop.swingx.util.OS;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.image.citys.BeijingImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.BeijingImageHash;
import com.mapbar.traffic.score.utils.BeijingOrcHelper;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class BeijingService implements Transfer {
	private static final String ROOT_URL = "http://www.bjjtgl.gov.cn/";
	private static final String GO_TO_DRIVER_RUL = "http://sslk.bjjtgl.gov.cn/jgjwwcx/jfcx/jfcx_result.jsp";

	private static final String VALIDATE_IMAGE_URL = "http://sslk.bjjtgl.gov.cn/jgjwwcx/servlet/YzmImg?type=1&t=";
	private static final String IMAGE_URL_REFER = "http://sslk.bjjtgl.gov.cn/jgjwwcx/yzmZl/MyYzm1.jsp";

	private static final String GET_DRIVER_URL = "http://sslk.bjjtgl.gov.cn/jgjwwcx/jfcx/getXx.action";

	private static final String STRING_REFERER = "http://sslk.bjjtgl.gov.cn/jgjwwcx/jfcx/jfcx_result2.jsp";

	private static final String IMAGE_TYPE = "jpg";

	static {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	}

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
		try {

			List<String> vCookies = new ArrayList<String>();
			// xmm=%E8%B5%B5%E9%94%8B&jszhph=110102198403201553&ip=117.114.151.179&yzm=723&cclzrq=2007-08-02
			// String rootDate = "xmn=" + driverProfile.getDriverName() + "&jszhph=" + driverProfile.getDriverLicense() + "&cclzrq=" + driverProfile.getLssueDate();
			// System.out.println(rootDate);
			Map<String, String> rootDate = new HashMap<String, String>();
			int Loop = 0;
			while (Loop < 10) {
				String indexStr = HttpClientUtil.postURLContentsWithCookies(GO_TO_DRIVER_RUL, rootDate, vCookies, ROOT_URL, "UTF-8", next);
				System.out.println("mycookies====" + vCookies);
				System.out.println(indexStr);
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));
				System.out.println(filePath);
				String uuid = UUID.randomUUID() + "";
				String codeFileP = filePath + "/" + uuid;
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				HttpClientUtil.getUrlImageWithCookies(VALIDATE_IMAGE_URL + Math.random(), vCookies, IMAGE_URL_REFER, next, validataCodeImage, driverProfile.getCityPy());
				System.out.println("mycookies====" + vCookies);
				File image = new File(validataCodeImage);
				if (image.exists() && image.getTotalSpace() > 0) {
					BeijingImageFilter imageFilter = new BeijingImageFilter();
					boolean ison = imageFilter.dealImage(validataCodeImage, filePath + "/", IMAGE_TYPE, uuid);
					if (!ison) {
						System.out.println("文件为null");
						continue;
					} else {
						Loop++;
					}
					String imageFilePath = filePath + "/" + uuid + "-1.jpg";
					String zhongwenpath = filePath + "/" + uuid + "-2.jpg";
					File zhongwenFile = new File(zhongwenpath);
					String imageHashCode = BeijingImageHash.getInstance().produceFingerPrint(imageFilePath);

					String imageDes = BeijingImageHash.getInstance().getImageDes(imageHashCode);
					if (imageDes == null || "".equals(imageDes)) {
						// String postData = "xmn=" + driverProfile.getDriverName() + "+&jszhph=" + driverProfile.getDriverLicense() + "&cclzrq=" + driverProfile.getLssueDate() + "&yzm=" + 705;
						Map<String, String> postData = new HashMap<String, String>();
						String strResp = HttpClientUtil.postURLContentsWithCookies(GET_DRIVER_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
						System.out.println("strResp=====" + strResp);
						return "";
					}
					imageDes = imageDes.replaceAll("[^\u4E00-\u9FA5]", "");
					System.out.println("imageDes====" + imageDes);

					String zhongwen = new OCR().recognizeText(zhongwenFile, IMAGE_TYPE, codeFileP, getOcrCommend(zhongwenFile, codeFileP));

					zhongwen = BeijingOrcHelper.dealZhongwen(zhongwen, imageDes);

					String zw[] = zhongwen.split(" ");
					System.out.println("zhongwen zhongwen.length() ==" + zhongwen.length());
					if (zhongwen.length() < 16) {
						System.out.println(" 中文识别错误 ");
						continue;
					}

					char des[] = imageDes.toCharArray();

					List<String> codelist = new ArrayList<String>();
					List<Integer> indexls = new ArrayList<Integer>();
					List<List<Integer>> indexstr = new ArrayList<List<Integer>>();
					for (int i = 0; i < des.length; i++) {
						String a = des[i] + "";
						int flag = 0;
						int index = 0;
						for (int j = 0; j < zw.length; j++) {
							String b = zw[j];
							if (flag == 2) {
								break;
							} else if (b.length() > 2) {
								continue;
							}
							if (b.equals(a)) {
								flag = 2;
								index = j;
							} else if (b.contains(a)) {
								flag = 1;
								index = j;
							}
						}
						if (flag > 0) {
							codelist.add(i + "," + index);
							if (indexls.contains(index)) {
								int n = indexls.indexOf(index);
								indexstr.get(n).add(i);
							} else {
								indexls.add(index);
								List<Integer> ins = new ArrayList<Integer>();
								ins.add(i);
								indexstr.add(ins);
							}

						}

					}
					System.out.println("codelist==" + codelist);
					System.out.println("indexls==" + indexls);
					System.out.println("indexstr===" + indexstr);

					int size = indexls.size();
					List<Set<Integer>> setlist = new ArrayList<Set<Integer>>();
					for (int i = 0; i < size; i++) {
						setlist.add(new HashSet<Integer>());
					}
					List<List<String>> result = new ArrayList<List<String>>();
					for (int i = 0; i < 100; i++) {
						List<String> reru = new ArrayList<String>();
						for (int a = 0; a < indexls.size(); a++) {
							int ro = indexls.get(a);
							List<Integer> ls = indexstr.get(a);
							if (ls.size() == 1) {
								reru.add(ro + "&&" + ls.get(0));
								setlist.get(a).add(0);
							} else {
								for (int m = 0; m < ls.size(); m++) {
									if (!setlist.get(a).contains(m)) {
										setlist.get(a).add(m);
										reru.add(ro + "&&" + ls.get(m));
										break;
									}
								}
								continue;
							}
						}
						if (reru.size() == indexls.size()) {
							// System.out.println("reru==="+reru);
							result.add(reru);
						} else {
							break;
						}
						int mmm = 0;
						for (int j = 0; j < size; j++) {
							if (setlist.get(j).size() == indexstr.get(j).size()) {
								mmm++;
							}
						}
						if (mmm == size) {
							break;
						}
					}

					// String httpSessionId = vCookies.get(0).split("=")[1];
					for (List<String> ls : result) {
						Collections.sort(ls, this.new SimpleCompare());
						System.out.println("ls===" + ls);
						/*
						 * String sssss = "324"; for (String s : ls) { sssss += s.split("&&")[0]; }
						 */

						// String checkStatus = NetHelper.click(sssss, httpSessionId, next.address().toString().replace("/", ""));

						// String postData = "xmn=" + driverProfile.getDriverName() + "+&jszhph=" + driverProfile.getDriverLicense() + "&cclzrq=" + driverProfile.getLssueDate() + "&yzm=" + sssss;
						Map<String, String> postData = new HashMap<String, String>();
						String strResp = HttpClientUtil.postURLContentsWithCookies(GET_DRIVER_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
						System.out.println("strResp====" + strResp);
						//
						System.out.println("postData===" + postData);
						// String strResp = NetHelper.postURLContentsWithCookies(
						// GET_VIOLATION_URL, postData, vCookies,
						// STRING_REFERER, "UTF-8", next);
						// System.out.println("mycookies===="+vCookies);
						// System.out.println(checkStatus);
						// if (checkStatus.contains("allowScriptTagRemoting is false")) {
						// System.out.println("验证码 验证失败");
						// continue;
						// } else {
						// System.out.println("验证码验证成功");
						// break;
						// }
					}
				} else {
					System.out.println("验证码文件为null");
				}

			}

			// String strResp = NetHelper.postURLContentsWithCookies( GET_VIOLATION_URL, postData, vCookies,STRING_REFERER,"UTF-8",next);

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanProv(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {

			LogUtil.doMkLogData_jiaoguanju(driverProfile, "err");
		}
		return ret;
	}

	private static List<String> getOcrCommend(File imageFile, String codeFilePath) {
		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(OCR.tessPath + "tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(OCR.tessPath + "tesseract.exe");
		}
		cmd.add(imageFile.getPath());
		cmd.add(codeFilePath);
		cmd.add("-psm");
		cmd.add("6");
		cmd.add(OCR.LANG_OPTION);
		cmd.add("chi_sim");
		System.out.println(cmd);
		return cmd;
	}

	public class SimpleCompare implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			String a[] = o1.split("&&");
			String b[] = o2.split("&&");
			if (new Integer(a[1]) < new Integer(b[1])) {
				return -1;
			} else {
				return 1;
			}

		}
	}
}
