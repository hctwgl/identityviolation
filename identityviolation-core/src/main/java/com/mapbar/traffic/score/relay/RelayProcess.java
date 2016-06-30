package com.mapbar.traffic.score.relay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.base.ServiceConfig;
import com.mapbar.traffic.score.redis.RedisDBUtil;
import com.mapbar.traffic.score.task.DriverQueryManager;
import com.mapbar.traffic.score.task.DriverQueryThread;
import com.mapbar.traffic.score.task.DriverTaskQueue;
import com.mapbar.traffic.score.transfer.FreshYzm;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.transfer.factory.TransferFactory;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class RelayProcess {

	private static final Logger logger = Logger.getLogger(RelayProcess.class);

	private static volatile RelayProcess instance = null;
	private final static int THREADNUMBER = Integer.parseInt(PropertiesUtils.getProValue("THREADNUMBER"));
	private final static String PROVINCEPACKAGE = PropertiesUtils.getProValue("PROVINCEPACKAGE");
	private final static String CITYPACKAGE = PropertiesUtils.getProValue("CITYPACKAGE");

	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(THREADNUMBER);

	private RelayProcess() {
		for (int i = 0; i < THREADNUMBER; i++) {
			EXECUTOR.submit(new DriverQueryThread(true));
			// new Thread(new DriverQueryThread(true)).start();
		}
	}

	public static RelayProcess getInstance() {
		if (instance == null) {
			synchronized (RelayProcess.class) {
				if (instance == null) {
					instance = new RelayProcess();
				}
			}
		}
		return instance;
	}

	public String processPush(DriverProfile driverProfile) {
		long time = System.currentTimeMillis() / 1000;
		String jsonresult = ResultCache.toErrJsonResult("系统繁忙, 请稍候再试.");
		String citypy = driverProfile.getCityPy();
		String obj = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime");
		Long lastTime = (obj == null ? null : new Long(obj));
		if (lastTime == null || (time - lastTime) > (60 * 60 * 24)) {
			String sourceclasses = ConfigUtils.getCitySources(citypy) == null ? "" : ConfigUtils.getCitySources(citypy);

			if (!StringUtil.isNotEmpty(sourceclasses)) {
				return jsonresult;
			}
			String classes[] = sourceclasses.split(",");
			for (String className : classes) {
				if ("OpenCarHomeService".equalsIgnoreCase(className)) {
					continue;
				}
				String cn;
				if (className.startsWith("City")) {
					cn = CITYPACKAGE + className;
				} else {
					cn = PROVINCEPACKAGE + className;
				}

				Transfer trans = TransferFactory.getInstance(cn);
				String ret = trans.checkDriverScore(driverProfile);
				if (ret != null && !"".equals(ret)) {
					jsonresult = ret;
					break;
				}
			}
			if (jsonresult.contains("\"status\":\"ok\"")) {
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey(), jsonresult);

				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));

			} else if (!jsonresult.contains("系统繁忙") && !jsonresult.contains("服务器繁忙")) {
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_err", jsonresult);
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
			}
		} else {
			if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey()) != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey());
			} else if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err") != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err");
			}
			if (obj != null && RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey()) == null && RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err") == null) {
				RedisDBUtil.del("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime");
			}
		}
		return jsonresult;
	}

	public String process(DriverProfile driverProfile) {
		logger.info(driverProfile.toCacheKey());
		if ("1".equals(driverProfile.getIsShowYzm())) {
			return processWithYzm(driverProfile);
		}
		logger.info("driverProfile====================" + driverProfile.toString());
		String jsonresult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		long time = System.currentTimeMillis() / 1000;
		String obj = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime");
		Long lastTime = (obj == null ? null : new Long(obj));
		logger.info("lastTime=======" + lastTime);
		ServiceConfig.htCitylist.get(driverProfile.getCityPy());

		if (lastTime == null || (time - lastTime) > (60 * 60 * 24) || driverProfile.isRefresh()) {
			logger.info("time-lastTime ====" + (time - (lastTime == null ? 0 : lastTime)));
			boolean ifok = DriverTaskQueue.getInstance().addTask(driverProfile);
			if (!ifok) {
				return jsonresult;
			}
			if (DriverQueryManager.getInstance().hasTaskKey(driverProfile.toCacheKey())) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				DriverQueryManager.getInstance().addTaskKey(driverProfile.toCacheKey());
			}

			int i = 0;
			while (i < 15) {
				if (DriverQueryManager.getInstance().isTaskOk(driverProfile.toCacheKey())) {
					break;
				}
				try {
					Thread.sleep(1000);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey()) != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey());
				return jsonresult;
			} else if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err") != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err");
			}
		} else {
			if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey()) != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey());
				return jsonresult;
			} else if (RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err") != null) {
				jsonresult = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err");
			}
			if (obj != null && RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey()) == null && RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey() + "_err") == null) {
				RedisDBUtil.del("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime");
			}
		}

		long time2 = System.currentTimeMillis() / 1000;
		logger.info(driverProfile.toCacheKey() + "的请求耗时：" + (time2 - time));
		return jsonresult;
	}

	public void processForLocal(DriverProfile driverProfile) {
		String jsonresult = ResultCache.toErrJsonResult("系统繁忙, 请稍候再试！");
		String citypy = driverProfile.getCityPy();

		String sourceclasses = ConfigUtils.getCitySources(citypy) == null ? "" : ConfigUtils.getCitySources(citypy);

		if (!StringUtil.isNotEmpty(sourceclasses)) {
			DriverQueryManager.getInstance().removeTaskKey(driverProfile.toCacheKey());
			return;
		}
		boolean isCarHome = false;
		logger.info(" sourceclasses=====" + sourceclasses);
		String classes[] = sourceclasses.split(",");
		for (String className : classes) {
			String cn;
			if (className.startsWith("City")) {
				cn = CITYPACKAGE + className;
			} else {
				cn = PROVINCEPACKAGE + className;
			}

			Transfer trans = TransferFactory.getInstance(cn);
			String ret = trans.checkDriverScore(driverProfile);

			if (ret != null && !"".equals(ret)) {
				logger.info(className + " processForLocal result:=====" + ret);
				jsonresult = ret;
				if (className.equals("OpenCarHomeService") || className.equals("SohuService")) {
					isCarHome = true;
				}
				break;
			}
		}
		String cache = RedisDBUtil.getValue("jiashizheng_" + driverProfile.toCacheKey());
		if (jsonresult.contains("\"status\":\"ok\"")) {
			if (jsonresult.contains("没有违章信息") && isCarHome) {

				if (StringUtil.isNotEmpty(cache) && !cache.contains("没有违章信息")) {
					RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
				} else {
					RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey(), jsonresult);

					RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
				}
			} else {
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey(), jsonresult);

				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
			}
		} else if (!jsonresult.contains("系统繁忙") && !jsonresult.contains("服务器繁忙") && !jsonresult.contains("非法访问") && !jsonresult.contains("拒绝处理请求")) {
			if (StringUtil.isNotEmpty(cache) && cache.contains("\"status\":\"ok\"")) {
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
			} else {
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
				RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_err", jsonresult);
			}
		}

		DriverQueryManager.getInstance().removeTaskKey(driverProfile.toCacheKey());
	}

	public String processWithYzm(DriverProfile driverProfile) {
		String jsonresult = ResultCache.toErrJsonResult("验证码获取失败.");
		String className = driverProfile.getClassName();

		if (StringUtil.isNotEmpty(className)) {
			String cn;
			cn = "com.mapbar.traffic.score.transfer.yzm." + className;
			Transfer trans = TransferFactory.getInstance(cn);
			String ret = trans.checkDriverScore(driverProfile);

			if (ret != null && !"".equals(ret)) {
				logger.info(className + " result:=====" + ret);
				jsonresult = ret;
			}
		} else {
			String citypy = driverProfile.getCityPy();
			String sourceclasses = ConfigUtils.getCitySources(citypy) == null ? "" : ConfigUtils.getCitySources(citypy);
			String classes[] = sourceclasses.split(",");
			String cn = "com.mapbar.traffic.score.transfer.yzm." + classes[0];

			try {
				Transfer trans = (Transfer) Class.forName(cn).newInstance();

				String ret = trans.checkDriverScore(driverProfile);

				if (ret != null && !"".equals(ret)) {
					jsonresult = ret;
				}

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		if (jsonresult.contains("\"status\":\"ok\"")) {
			RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey(), jsonresult);
			RedisDBUtil.setValue("jiashizheng_" + driverProfile.toCacheKey() + "_lasttime", String.valueOf(System.currentTimeMillis() / 1000));
		}
		return jsonresult;
	}

	public String prcessRefreshYzm(String cookie, String className) {
		String jsonresult = ResultCache.toErrJsonResult("验证码获取失败.");
		if (StringUtil.isNotEmpty(className)) {
			String cn;
			cn = "com.mapbar.traffic.score.transfer.yzm." + className;
			FreshYzm fresh = (FreshYzm) TransferFactory.getInstance(cn);
			String ret = fresh.refreshYzm(cookie);
			if (ret != null && !"".equals(ret)) {
				jsonresult = ret;
			}
		}
		return jsonresult;
	}
}
