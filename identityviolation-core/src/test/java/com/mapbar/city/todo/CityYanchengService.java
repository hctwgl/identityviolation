package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.YanchengParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityYanchengService  implements Transfer{
	private static final String GET_VIOLATION_URL = "http://www.ycga.gov.cn/zworacle.php?";
	 
	 
	private static final String STRING_REFERER = "http://www.ycga.gov.cn/default.php?mod=c&s=sscfe6a82";
	
	YanchengParser parser = new YanchengParser();
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
	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret="";
		 
			 
			String engId = car.getCityPy();
			if (engId.length() > 4) {
				engId = engId.substring(engId.length() -4, engId.length());
			} else if (engId.length() < 4) {
				return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
			} 
			
			int Loop = 0;
			while(Loop<10){
				Loop++;
				
				try{
					Vector<String> vCookies = new Vector<String>();

					String strResp = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL+"&id=02&id2="+car.getCityPy()+"&id3="+engId+"&type=2", vCookies, STRING_REFERER, next);
					System.out.println("strResp===="+strResp);
					
					if(StringUtil.isNotEmpty(strResp) && strResp.contains("未查到相关信息，请检查确认是否输入的是正确完整的资料")){
						LogUtil.doMkLogData_JGU_With_Msg(car, "err","信息输入错误");
						return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！"); 
					}else if(strResp.contains("正在查询，请稍候......")){
						 Thread.sleep(2500);
						 continue;
					}else if(strResp.contains("<td align=\"right\">车辆状态：</td>") && strResp.contains("<td align=\"left\">正常</td>")){
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok","");
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}else if(strResp.contains("<td align=\"right\">车辆状态：</td>") && strResp.contains("<td align=\"left\">违法未处理</td>")){
						ret = parser.parse(strResp,car);
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok","");
						break;
					}
					
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
			LogUtil.doMkLogData_JGU_With_Msg(car, "err","其他错误");
		}
		return ret;
	}
	
}
