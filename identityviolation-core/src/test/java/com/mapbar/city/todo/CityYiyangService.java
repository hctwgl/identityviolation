package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.YiyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityYiyangService implements Transfer{
	private static final String GET_VIOLATION_URL = "http://www.jtjc.gov.cn/hnwsyy/jdcwfcx.do";
	 
	 
	private static final String STRING_REFERER = "http://www.jtjc.gov.cn/hnwsyy/jdcwfcx.do";
	YiyangParser parser = new YiyangParser();
	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strResult;
	}
	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret="";
	 
			String btmId = car.getCityPy();
			if (btmId.length() > 4) {
				btmId = btmId.substring(btmId.length() - 4, btmId.length());
			} else if (btmId.length() < 4) {
				return ResultCache.toErrJsonResult("车辆信息错误，请输入车架号后4位！");
			}
			Vector<String> vCookies = new Vector<String>();
			
			int Loop = 0;
			while(Loop<3){
				Loop++;
				
				try{
					
					 
					//String postData="hpzl="+car.getCityPy()+"&hphm="+car.getCityPy()+"&clsbdh="+btmId+"&type=cx&state=&ddbj=";
					//System.out.println("postData==="+postData);
					HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
					System.out.println(vCookies);
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "GBK", next);
					System.out.println("strResp===="+strResp);
					if(StringUtil.isNotEmpty(strResp) && strResp.contains("提示信息:没有违法信息")){
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok","");
					    return  ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}else if(strResp.contains("提示信息:内网提示信息1：车辆识别代号输入错误")){
						LogUtil.doMkLogData_JGU_With_Msg(car, "err","信息输入错误");
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					}else if(StringUtil.isNotEmpty(strResp) && strResp.contains("id=\"table2\"")){
						ret = parser.parse(strResp,car);
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok","");
						break;
					}
					Thread.sleep(1000);
					//break;
					
				}catch(SocketTimeoutException e){
					 
					LogUtil.doMkLogData_JGU_With_Msg(car, "err","网络连接超时");
					
					e.printStackTrace();
				}
				catch(FileNotFoundException e){
					 
					LogUtil.doMkLogData_JGU_With_Msg(car, "err","网络连接异常");
					e.printStackTrace();
				}catch(IOException e){
					 
					LogUtil.doMkLogData_JGU_With_Msg(car, "err","网络连接异常");
					e.printStackTrace();
				}catch(Exception e){
					 
					LogUtil.doMkLogData_JGU_With_Msg(car, "err","其他错误");
					e.printStackTrace();
				}
			}
			
			
		 
			if("".equals(ret)){
				LogUtil.doMkLogData_JGU_With_Msg(car, "err","验证码识别失败");
			}
		return ret;
	}
}
