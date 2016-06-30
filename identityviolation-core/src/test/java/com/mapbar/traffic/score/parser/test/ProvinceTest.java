package com.mapbar.traffic.score.parser.test;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mapbar.city.deprecated.BeijingService;
import com.mapbar.city.deprecated.Car100Service;
import com.mapbar.city.deprecated.CarHomeService;
import com.mapbar.city.deprecated.ChengduService;
import com.mapbar.city.deprecated.OpenCarHomeService;
import com.mapbar.city.deprecated.WecarService;
import com.mapbar.city.deprecated.WxTianjinService;
import com.mapbar.city.deprecated.ZhejiangService;
import com.mapbar.provincetodo.AnhuiService;
import com.mapbar.provincetodo.CheshouyeService;
import com.mapbar.provincetodo.ChongqingService;
import com.mapbar.provincetodo.FujianService;
import com.mapbar.provincetodo.GuangzhouService;
import com.mapbar.provincetodo.GuizhouService;
import com.mapbar.provincetodo.HainanService;
import com.mapbar.provincetodo.HuBeiService;
import com.mapbar.provincetodo.JilinService;
import com.mapbar.provincetodo.LasaService;
import com.mapbar.provincetodo.NanchangService;
import com.mapbar.provincetodo.NanjingService;
import com.mapbar.provincetodo.NingxiaService;
import com.mapbar.provincetodo.QinghaiService;
import com.mapbar.provincetodo.SohuService;
import com.mapbar.provincetodo.WecheService;
import com.mapbar.provincetodo.WxShandongService;
import com.mapbar.provincetodo.XiAnService;
import com.mapbar.provincetodo.XinjiangService;
import com.mapbar.provincetodo.YunnanService;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.transfer.GansuService;
import com.mapbar.traffic.score.transfer.HebeiService;
import com.mapbar.traffic.score.transfer.HeilongjiangService;
import com.mapbar.traffic.score.transfer.HunanService;
import com.mapbar.traffic.score.transfer.ShanghaiService;
import com.mapbar.traffic.score.transfer.ShanxiService;
import com.mapbar.traffic.score.transfer.ShenyangService;
import com.mapbar.traffic.score.transfer.ShenzhenService;
import com.mapbar.traffic.score.transfer.WxBeijingService;
import com.mapbar.traffic.score.transfer.WxGuangdongService;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.transfer.factory.TransferFactory;

public class ProvinceTest {

	private static final Logger logger = Logger.getLogger(ProvinceTest.class);

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
	public void testHebeiService() {
		Transfer tran = TransferFactory.getInstanceByClass(HebeiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 河北省张家口市，131182199110102018，徐赛赛，130700762435，20121224，有效期6年
		driverProfile.setCityPy("zhangjiakou");
		driverProfile.set("", "131182199110102018", "", "130700762435", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
		// System.exit(0);
	}

	@Test
	public void testShanghaiService() {
		Transfer tran = TransferFactory.getInstanceByClass(ShanghaiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 上海，210302198212163317，林凯，310021064503，20111011，否。
		driverProfile.setLssueArchive("310021064503");
		driverProfile.setCityPy("shanghai");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testShanxiService() {
		Transfer tran = TransferFactory.getInstanceByClass(ShanxiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 山西太原 丁黎 140121198211200016 140100674335 20080108 10
		driverProfile.setCityPy("taiyuan");
		driverProfile.set("", "140121198211200016", "", "140100674335", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testShenyangService() {
		Transfer tran = TransferFactory.getInstanceByClass(ShenyangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 210104199007124323,210102808299
		driverProfile.setCityPy("shenyang");
		driverProfile.set("孙国清", "210104199007124323", "", "210102808299", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testShenzhenService() {
		Transfer tran = TransferFactory.getInstanceByClass(ShenzhenService.class);
		DriverProfile driverProfile = new DriverProfile();
		// driverProfile.setCityPy("meizhou");
		driverProfile.setCityPy("shenzhen");
		// 深圳，441426197909080031，张羽，440304152156，20150826，否
		// 深圳，450422198410241430，梁火威，440302961143，20120329，否。
		// driverProfile.set("", "441426197909080031", "", "440304152156", "", "");
		driverProfile.set("", "450422198410241430", "", "440302961143", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testWxBeijingService() {
		Transfer tran = TransferFactory.getInstanceByClass(WxBeijingService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 北京 赵峰 110102198403201553 110007515813 2007-08-02
		// 北京 李峰 612726198511023014 110003356584 20120426 6
		// 北京 赵雪 11090219780403782x 20020703 6 //信息错误
		// 北京，42011119821105552X，王黎，110005579413，2007-07-27，否
		driverProfile.setCityPy("beijing");
		// driverProfile.set("赵峰", "110102198403201553", "2007-08-02", "lssueArchive", "is_effective", "effective_date");
		// driverProfile.set("李峰", "612726198511023014", "2012-04-26", "lssueArchive", "is_effective", "effective_date");
		driverProfile.set("王黎", "42011119821105552X", "2007-07-27", "lssueArchive", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testWxGuangdongService() {
		Transfer tran = TransferFactory.getInstanceByClass(WxGuangdongService.class);
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setCityPy("meizhou");
		// 广东梅州 刘德 441481198510032256 441400883093 20070911 6
		// 深圳，441426197909080031，张羽，440304152156，20150826，否
		//driverProfile.set("", "441481198510032256", "", "441400883093", "", "");
		 driverProfile.set("", "441426197909080031", "", "440304152156", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		logger.info("ret=" + ret);
	}

	@Test
	public void testGansuService() {
		Transfer tran = TransferFactory.getInstanceByClass(GansuService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 甘肃，622927198911200517,622900308491，王一元
		driverProfile.setCityPy("lanzhou");
		driverProfile.set("王一元", "622927198911200517", "", "622900308491", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	@Test
	public void testHeilongjiangService() {
		Transfer tran = TransferFactory.getInstanceByClass(HeilongjiangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 黑龙江，牡丹江，231084199312162925,231000771359
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "231084199312162925", "", "231000771359", "", "");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testHunanService() {
		Transfer tran = TransferFactory.getInstanceByClass(HunanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testBeijingService() {
		Transfer tran = TransferFactory.getInstanceByClass(BeijingService.class);
		// 赵峰 110102198403201553 110007515813 2007-08-02
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setDriverName("赵峰");
		driverProfile.setDriverLicense("110102198403201553");
		driverProfile.setLssueDate("2007-08-02");
		driverProfile.setCityPy("beijing");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCarHomeService() {
		Transfer tran = TransferFactory.getInstanceByClass(CarHomeService.class);
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCar100Service() {
		Transfer tran = TransferFactory.getInstanceByClass(Car100Service.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testChengduService() {
		Transfer tran = TransferFactory.getInstanceByClass(ChengduService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testOpenCarHomeService() {
		Transfer tran = TransferFactory.getInstanceByClass(OpenCarHomeService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testWecarService() {
		Transfer tran = TransferFactory.getInstanceByClass(WecarService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testWxTianjinService() {
		Transfer tran = TransferFactory.getInstanceByClass(WxTianjinService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testZhejiangService() {
		Transfer tran = TransferFactory.getInstanceByClass(ZhejiangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testAnhuiService() {
		Transfer tran = TransferFactory.getInstanceByClass(AnhuiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testCheshouyeService() {
		Transfer tran = TransferFactory.getInstanceByClass(CheshouyeService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testChongqingService() {
		Transfer tran = TransferFactory.getInstanceByClass(ChongqingService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testFujianService() {
		Transfer tran = TransferFactory.getInstanceByClass(FujianService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testGuangzhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(GuangzhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testGuizhouService() {
		Transfer tran = TransferFactory.getInstanceByClass(GuizhouService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testHainanService() {
		Transfer tran = TransferFactory.getInstanceByClass(HainanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testHuBeiService() {
		Transfer tran = TransferFactory.getInstanceByClass(HuBeiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testJilinService() {
		Transfer tran = TransferFactory.getInstanceByClass(JilinService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testLasaService() {
		Transfer tran = TransferFactory.getInstanceByClass(LasaService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testNanchangService() {
		Transfer tran = TransferFactory.getInstanceByClass(NanchangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testNanjingService() {
		Transfer tran = TransferFactory.getInstanceByClass(NanjingService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testNingxiaService() {
		Transfer tran = TransferFactory.getInstanceByClass(NingxiaService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testQinghaiService() {
		Transfer tran = TransferFactory.getInstanceByClass(QinghaiService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testSohuService() {
		Transfer tran = TransferFactory.getInstanceByClass(SohuService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testWecheService() {
		Transfer tran = TransferFactory.getInstanceByClass(WecheService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testWxShandongService() {
		Transfer tran = TransferFactory.getInstanceByClass(WxShandongService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testXiAnService() {
		Transfer tran = TransferFactory.getInstanceByClass(XiAnService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testXinjiangService() {
		Transfer tran = TransferFactory.getInstanceByClass(XinjiangService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

	// @Test
	public void testYunnanService() {
		Transfer tran = TransferFactory.getInstanceByClass(YunnanService.class);
		DriverProfile driverProfile = new DriverProfile();
		// 湖南衡阳，41032619920327371X,430400772692
		driverProfile.setCityPy("mudanjiang");
		driverProfile.set("", "41032619920327371X", "", "430400772692", "is_effective", "effective_date");
		String ret = tran.checkDriverScore(driverProfile);
		System.out.println("ret=" + ret);
	}

}
