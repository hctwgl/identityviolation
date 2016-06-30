package com.mapbar.traffic.score.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ShanghaiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class ShanghaiService implements Transfer {

	private static final String GET_DRIVERSCORE_URL = "http://www.shjtaq.com/zwfg/chafen.asp?id=1";
	private static final String STRING_REFERER = "http://www.shjtaq.com/zwfg/chafen.asp?id=1";
	private ShanghaiParser parser = new ShanghaiParser();

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

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {
				// act=search&licensenums=530400210732
				// gb2312 310001108502驾驶证编号
				//String postData = "act=search&licensenums=" + driverProfile.getLssueArchive();
                Map<String, String> params=new HashMap<>();
                params.put("act", "search");
                params.put("licensenums", driverProfile.getLssueArchive());
				String strResp = HttpClientUtil.postURLContentsToCheckForm(GET_DRIVERSCORE_URL, params, STRING_REFERER, "GB2312", next);
				//System.out.println("strResp=========" + strResp);
				if (StringUtil.isNotEmpty(strResp) && !strResp.contains("如果您的输入正确，那么您目前没有违法记录！")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶员档案编号错误！");
				}
				if (StringUtil.isNotEmpty(strResp) && (strResp.contains("如果您的输入正确，那么您目前没有违法记录！"))) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					ret = parser.parse(strResp,driverProfile);
					break;
				} else {
					if (strResp.contains("正在更新电子监控数据，请稍后访问")) {
						Thread.sleep(1000);
						continue;
					}
					strResp = "";
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
		return ret;
	}

}
