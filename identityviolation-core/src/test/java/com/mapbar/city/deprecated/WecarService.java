package com.mapbar.city.deprecated;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverParser;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.HttpClientProxyManager;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class WecarService implements Transfer {
	protected static String strProxyFile = System.getProperty("PROXY_FILE", "https_proxy.txt");
	protected static HttpClientProxyManager pMgr = new HttpClientProxyManager(strProxyFile);

	private static String CHK_URL = "http://chan.weiche.me/";

	private static Hashtable<String, String> htKeys = new Hashtable<String, String>();

	// private static TrafficDBAdapter dbAdapter = TrafficDBAdapter.getInstance();

	static {
		htKeys.put("违章时间", "违法时间");
		htKeys.put("违章地点", "违法地点");
		htKeys.put("违章行为", "违法内容");
	}

	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		// ResultCache.toJsonResult("服务维护中, 请稍候再试.","err");

		try {
			if (car == null)
				return ResultCache.toErrJsonResult("信息不完整");

			String strCarKey = car.toCacheKey();

			if (!StringUtil.isNotEmpty(strCarKey))
				return ResultCache.toErrJsonResult("信息不完整");

			// //获取缓存之中的查询结果 36个小时为过期期限
			// strResult = checkDatabase(car);

			String strRequest = ConfigUtils.getCitySourceRole(car.getCityPy(), WecarService.class.getSimpleName());
			if (!StringUtil.isNotEmpty(strRequest)) {
				strRequest = car.toWecarString();
			}
			// province=%E5%AE%89%E5%BE%BD&city_pinyin=hefei&car_province=%E7%9A%96&license_plate_num=AL2M38&body_num=216662&engine_num=%E9%80%89%E5%A1%AB&save=1&captcha=%E9%AA%8C%E8%AF%81%E7%A0%81
			// province=%E5%8C%97%E4%BA%AC&city_pinyin=beijing&car_province=%E4%BA%AC&license_plate_num=QH1W08&body_num=%E9%80%89%E5%A1%AB&engine_num=86100560&save=1&captcha=%E9%AA%8C%E8%AF%81%E7%A0%81
			if (!strRequest.contains("&save=")) {
				strRequest = strRequest.trim() + "&save=1&captcha=%E9%AA%8C%E8%AF%81%E7%A0%81";
			}
			// System.out.println("strRequest======="+strRequest);
			List<String> vCookies = new ArrayList<String>();
			String strReferer = "http://chan.weiche.me/";

			int lp = 0;
			String strJob = "";
			while (vCookies.isEmpty() && lp < 4) {
				lp++;
				Map<String, String> postData = new HashMap<String, String>();
				strJob = HttpClientUtil.postURLContentsWithCookies(CHK_URL, postData, vCookies, strReferer, pMgr.next(true));
				// System.out.println(vCookies);
			}

			LogUtil.doMkLogData_wecar(strJob, car);

			int iLoop = 0;
			while (!StringUtil.isNotEmpty(strJob) && iLoop < 3) {
				iLoop++;
				Map<String, String> postData = new HashMap<String, String>();
				strJob = HttpClientUtil.postURLContentsWithCookies(CHK_URL, postData, vCookies, strReferer, pMgr.next(true));
				System.out.println("strJob=======" + strJob);
				LogUtil.doMkLogData_wecar(strJob, car);
			}

			if (StringUtil.isNotEmpty(strJob)) {
				String strResp = strJob;
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("data")) {
					JSONObject jresp = JSONObject.parseObject(strResp);
					String strMsg = jresp.getString("data");
					System.out.println("strMsg=======" + strMsg);
					if (StringUtil.isNotEmpty(strMsg)) {
						if (!strMsg.contains("请在浏览器设置中启用cookie后再试！")) {
							// strResult =
							// ResultCache.toJsonResult(strMsg.trim(), "ok");
							strResult = parseData(strMsg); // ResultCache.toJsonResult(strMsg.trim(),
							// break; // "ok");

							// updateDatabase(strRequest, strResult);
						}
						return strResult;
					}
				}

				JSONObject job = JSONObject.parseObject(strJob);
				String strJobId = job.getString("job_id");

				if (StringUtil.isNotEmpty(strJobId)) {
					Thread.sleep(1000);

					// job_id=493a5b3f-4f0e-4757-a17b-bd0343107381
				//	String strJobData = "job_id=" + strJobId;
					strResp = HttpClientUtil.postURLContentsWithCookies(CHK_URL, null, vCookies, strReferer, pMgr.next(true));

					LogUtil.doMkLogData_wecar(strResp, car);
					iLoop = 0;
					while ((!StringUtil.isNotEmpty(strResp) || !strResp.contains("data")) && iLoop < 10) {
						iLoop++;
						Thread.sleep(1000);

						strResp = HttpClientUtil.postURLContentsWithCookies(CHK_URL, null, vCookies, strReferer, pMgr.next(true));
						System.out.println("strResp=======" + strResp);
						LogUtil.doMkLogData_wecar(strResp, car);
					}

					if (StringUtil.isNotEmpty(strResp) && strResp.contains("data")) {
						// System.out.println("strResp======="+strResp);
						JSONObject jresp = JSONObject.parseObject(strResp);
						String strMsg = jresp.getString("data");

						if (StringUtil.isNotEmpty(strMsg)) {
							if (!strMsg.contains("请在浏览器设置中启用cookie后再试！")) {

								System.out.println("wecar  result ====================" + strMsg);
								if (strMsg.contains("恭喜！您暂时还没有违章")) {
									strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
								} else {
									strResult = parseData(strMsg);
								}
								// ResultCache.toJsonResult(strMsg.trim(),
								// "ok");
								// break;
								// updateDatabase(strRequest, strResult);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	public static void updateDatabase(String strRequest, String strResponse) {
		try {
			// if and only if ok result here
			if (StringUtil.isNotEmpty(strRequest) && StringUtil.isNotEmpty(strResponse) && strResponse.contains("\"status\":\"ok\"")) {
				DriverProfile car = DriverParser.parseRequest(strRequest);

				Hashtable<String, Object> htVCs = new Hashtable<String, Object>();
				htVCs.put(DriverCase.KEY_CASES, strResponse);
				htVCs.put(DriverCase.KEY_UPDATED, new Date());

				// dbAdapter.upsert(car, htVCs);

				ResultCache.put(car.toCacheKey(), strResponse, System.currentTimeMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String checkDatabase(DriverProfile car) {
		String strResult = null;

		try {

			String strCacheKey = car.toCacheKey();

			// 获取缓存之中的查询结果 36个小时为过期期限
			strResult = ResultCache.check(strCacheKey);

			// if(!StringUtil.isValid(strResult))
			// {
			// VehicleProfile peer = dbAdapter.get(car.strRegId);
			// if(car.equals(peer))
			// {
			// if(peer.vc.toString().contains("ok"))
			// {
			// if( ( System.currentTimeMillis() - peer.vc.updated.getTime() ) < 86400000L)
			// {
			// strResult = peer.vc.toString();
			// }
			// }
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private static String parseData(String strMsg) {
		String strResult = "";

		try {
			Document doc = Jsoup.parse(strMsg);

			if (StringUtil.isNotEmpty(strMsg) && strMsg.contains("table")) {
				try {
					DriverCase vc = new DriverCase();

					JSONArray jcases = new JSONArray();
					// parse violation cases
					Element thead = doc.getElementsByTag("thead").first();
					Element tbody = doc.getElementsByTag("tbody").first();

					Elements titles = thead.getElementsByTag("td");

					Elements cases = tbody.getElementsByTag("tr");
					if (cases.size() > 0) {
						for (int i = 0; i < cases.size(); i++) {
							Elements items = cases.get(i).getElementsByTag("td");
							if (items.size() == titles.size()) {
								JSONObject jcase = new JSONObject();
								for (int k = 0; k < titles.size(); k++) {
									String strKey = titles.get(k).text();
									if (htKeys.containsKey(strKey))
										strKey = htKeys.get(strKey);
									String strValue = items.get(k).text();
									jcase.put(strKey, strValue);
								}
								jcases.add(jcase);
							} else {
								strMsg = tbody.text();
								vc.json.put("msg", strMsg);
							}
						}

						if (jcases.size() > 0) {
							vc.json.put("data", jcases);
						}
					} else {
						strMsg = tbody.text();
						vc.json.put("msg", strMsg);
					}

					JSONArray jarray = null;

					if (vc.json.containsKey("data"))
						jarray = vc.json.getJSONArray("data");

					int iCount = 0;
					if (jarray != null)
						iCount = jarray.size();
					vc.json.put("count", iCount);

					vc.json.put("status", "ok");

					strResult = vc.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			// else
			// {
			// String strStatus = "ok";
			// if(strMsg.contains("error")) strStatus = "err";
			// strResult = ResultCache.toJsonResult(doc.text(), strStatus);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}
}
