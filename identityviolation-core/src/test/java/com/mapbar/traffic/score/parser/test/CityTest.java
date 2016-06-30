package com.mapbar.traffic.score.parser.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mapbar.city.deprecated.CityWuxiService;
import com.mapbar.city.deprecated.CityWxSuzhouService;
import com.mapbar.city.todo.CityAnyangService;
import com.mapbar.city.todo.CityBenxiService;
import com.mapbar.city.todo.CityChangzhouService;
import com.mapbar.city.todo.CityChaoyangService;
import com.mapbar.city.todo.CityFushunService;
import com.mapbar.city.todo.CityFuxinService;
import com.mapbar.city.todo.CityFuzhouService;
import com.mapbar.city.todo.CityHuludaoService;
import com.mapbar.city.todo.CityJiaozuoService;
import com.mapbar.city.todo.CityJiyuanService;
import com.mapbar.city.todo.CityKaifengService;
import com.mapbar.city.todo.CityLianyungangService;
import com.mapbar.city.todo.CityLiaoyangService;
import com.mapbar.city.todo.CityNantongService;
import com.mapbar.city.todo.CityPingdingshanService;
import com.mapbar.city.todo.CityPuyangService;
import com.mapbar.city.todo.CityShangqiuService;
import com.mapbar.city.todo.CitySuqianService;
import com.mapbar.city.todo.CitySuzhouService;
import com.mapbar.city.todo.CityXiamenService;
import com.mapbar.city.todo.CityXuchangService;
import com.mapbar.city.todo.CityXuzhouService;
import com.mapbar.city.todo.CityYanbianService;
import com.mapbar.city.todo.CityYanchengService;
import com.mapbar.city.todo.CityYangzhouService;
import com.mapbar.city.todo.CityYingkouService;
import com.mapbar.city.todo.CityYiyangService;
import com.mapbar.city.todo.CityZhengzhouService;
import com.mapbar.city.todo.CityZhenjiangService;
import com.mapbar.city.todo.CityZmdService;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.transfer.citys.CityAnshanService;
import com.mapbar.traffic.score.transfer.citys.CityDalianService;
import com.mapbar.traffic.score.transfer.citys.CityDandongService;
import com.mapbar.traffic.score.transfer.citys.CityHebiService;
import com.mapbar.traffic.score.transfer.citys.CityLuoyangService;
import com.mapbar.traffic.score.transfer.citys.CityZhoukouService;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.transfer.factory.TransferFactory;

public class CityTest {

	private static final Logger logger = Logger.getLogger(CityTest.class);

	@BeforeClass
	public static void beforeClass() {
		// System.setProperty("http.proxyHost", "10.10.21.111");
		// System.setProperty("http.proxyPort", "8088");
		// System.setProperty("https.proxyHost", "10.10.21.111");
		// System.setProperty("https.proxyPort", "8088");

		// System.setProperty("http.proxyHost", "119.255.37.165");
		// System.setProperty("http.proxyPort", "18088");
		// System.setProperty("https.proxyHost", "119.255.37.165");
		// System.setProperty("https.proxyPort", "18088");
		logger.info("所有方法调用前要做的事情");
	}

	@AfterClass
	public static void afterClass() {
		logger.info("所有方法测试完后要调用的");
	}

	@Test
	public void test() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) - 20);
		Date startTime = cal.getTime();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
	}

	@Test
	public void testCityDalianService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityDalianService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 辽宁沈阳 赵泽文 210102198602076613 210101548617 20110429 6 不能用
		// 辽宁大连 郭芳 210222198206244727 210211365167 20081205 10 能用
		driverProfile.setCityPy("dalian");
		driverProfile.setCityName("大连");
		driverProfile.set("", "210222198206244727", "2007-08-02", "210211365167", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testCityDandongService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityDandongService.class);
		// 辽宁丹东,210624197505297116,孙国清,210670767884,2001.3.5,6
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setCityPy("dandong");
		driverProfile.set("孙国清", "210624197505297116", "", "210670767884", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testCityHebiService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityHebiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 河南鹤壁 张志平 410522198511173738 410600043004 20100612 6 不可用
		driverProfile.setCityPy("hebi");
		driverProfile.set("", "410522198511173738", "", "410600043004", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testCityLuoyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityLuoyangService.class);
		// 河南洛阳,410326199203093751,徐冰浩，410300961228，2011.08.29，6
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setCityPy("luoyang");
		driverProfile.set("", "410326199203093751", "", "410300961228", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testCityZhoukouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityZhoukouService.class);
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setCityPy("zhoukou");
		// 刘永成,412727198910031256
		driverProfile.set("刘永成", "412727198910031256", "", "210670767884", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	// @Test
	public void testCityAnshanService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityAnshanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 辽宁鞍山，210303199004192018，210103247337
		driverProfile.setCityPy("anshan");
		driverProfile.set("", "210303199004192018", "", "210103247337", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
		// System.exit(0);
	}

	// @Test
	public void testCityWuxiService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityWuxiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityWxSuzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityWxSuzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityAnyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityAnyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityBenxiService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityBenxiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityChangzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityChangzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityChaoyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityChaoyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityFushunService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityFushunService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityFuxinService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityFuxinService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityFuzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityFuzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityHuludaoService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityHuludaoService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityJiaozuoService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityJiaozuoService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityJiyuanService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityJiyuanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityKaifengService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityKaifengService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityLianyungangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityLianyungangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityLiaoyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityLiaoyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityNantongService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityNantongService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityPingdingshanService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityPingdingshanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityPuyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityPuyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityShangqiuService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityShangqiuService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCitySuqianService() {
		Transfer tran = TransferFactory.getInstanceByClass(CitySuqianService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCitySuzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CitySuzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityXiamenService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityXiamenService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityXuchangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityXuchangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityXuzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityXuzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityYanbianService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityYanbianService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityYanchengService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityYanchengService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityYangzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityYangzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityYingkouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityYingkouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityYiyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityYiyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityZhengzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityZhengzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityZhenjiangService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityZhenjiangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCityZmdService() {
		Transfer tran = TransferFactory.getInstanceByClass(CityZmdService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

}
