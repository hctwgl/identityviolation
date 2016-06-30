package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.WxSuzhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.SuzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class CitySuzhouService implements Transfer{
	private static final String VALIDATE_IMAGE_URL = "http://221.224.161.122:8080/ShowValidateCode.aspx?source=VehicleQuery&rnd=";

	private static final String GET_VIOLATION_URL = "http://221.224.161.122:8080/ViolationQuery/VehicleQuery.aspx/QueryVehicle";

	private static final String STRING_REFERER = "http://221.224.161.122:8080/ViolationQuery/VehicleQuery.aspx";

	private static final String IMAGE_TYPE = "png";
	SuzhouParser parser = new SuzhouParser();
	
	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return strResult;
	}
	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret="";
	 
			
			String btmId = car.getCityPy();
			if (btmId.length() > 7) {
				btmId = btmId.substring(btmId.length() - 7, btmId.length());
			} else if (btmId.length() < 7) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}
		 
		
			
			 
			int Loop = 0;
			while(Loop<18){
			
				Loop++;
				try{
					List<String> vCookies = new ArrayList<String>();
				
					String valiDateImageUrl = VALIDATE_IMAGE_URL + "'"+Math.random()+"'";
					// 校验验证码存储目录是否存在  不存在则创建
					String filePath = FileUtils.checkDerictor(
							PropertiesUtils.getProValue("IMAGE_STORE_PATH"),
							car.getCityPy(),
							String.valueOf(DateUtils.getCurrentDate()));
					
					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					//得到图片
					HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies,
							STRING_REFERER, next, validataCodeImage, car.getCityPy());
					String newImagePath =  filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
					System.out.println(vCookies);	
					File ff = new File(validataCodeImage);
					if(!ff.exists()){
						
						continue;
					}
					WxSuzhouImageFilter imageFilter = new WxSuzhouImageFilter();
					imageFilter.dealImage2(validataCodeImage, newImagePath, IMAGE_TYPE);
					File valiDataCodeF = new File(newImagePath);
					if (!valiDataCodeF.exists()) {
						 
						continue;
					}
					String validataCode = "";
					try {
						validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE,
								codeFileP);
						//System.out.println("  validataCode=========" + validataCode);
						 
						validataCode = validataCode.toLowerCase();
						validataCode = validataCode.replaceAll("[^a-z]", "");
						System.out.println("  validataCode=========" + validataCode);
					} catch (Exception e) {
						 
						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
						System.out.println("CitySuzhouService 验证码识别失败   validataCode=========" + validataCode);
						//Thread.sleep(500);
						Loop--;
						continue;
					} else if (validataCode.length() > 4) {
						validataCode = validataCode.substring(0, 4);
						Loop--;
						continue;
					}
					 
					//__VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI%3D
					//&ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315&txtYzm=VRFS&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
					//plate1=%E8%8B%8F&plate2=E&plate3=03F13&vehicleType=02&last7id=7168224&txtVerifyCode=86DR&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...
					//String postDate = "{HPZL:'"+car.getCityPy()+"',HPHM:'"+car.getCityPy()+"',CLSBDH:'"+btmId+"',Validate:'"+validataCode+"'}";
					//String postDate=paramDate+"&plate1=%E8%8B%8F&plate2=E&plate3="+car.strRegId.substring(1)
						//	+"&last7id="+btmId+"&txtVerifyCode="+validataCode+"&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...";
					//System.out.println(postDate);	
					//System.out.println(vCookies);
					Map<String, String> params=new HashMap<String, String>();
					String strRep = HttpClientUtil.postURLContentsWithCookiesForAjax(GET_VIOLATION_URL, params, vCookies, STRING_REFERER, "UTF-8", next);
					//System.out.println(strRep);
					//System.out.println(vCookies);
					if(StringUtil.isNotEmpty(strRep) && strRep.contains("验证码错误，请重新输入")){
						continue;
					}
					if(strRep.contains("获取车辆基本信息失败")){
						LogUtil.doMkLogData_JGU_With_Msg(car, "err","信息输入错误");
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！"); 
					}
					JSONObject json = JSONObject.parseObject(strRep);
					String data = json.getString("d");
					//System.out.println(data);
					JSONObject json2 = JSONObject.parseObject(data);
					String vehicle = json2.getString("vehicle");
					//System.out.println(vehicle);
					String surveil = json2.getString("surveil");
					//System.out.println(surveil);
					JSONObject carInfo=JSONObject.parseObject(vehicle);
					if("违法未处理".equals(carInfo.getString("ZT_NAME"))){
						ret = parser.parse(surveil,car);
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok","");
						break;
					}else if("正常".equals(carInfo.getString("ZT_NAME"))){
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
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
				LogUtil.doMkLogData_JGU_With_Msg(car,"err","验证码识别失败");
			}
		return ret;
	}
}
