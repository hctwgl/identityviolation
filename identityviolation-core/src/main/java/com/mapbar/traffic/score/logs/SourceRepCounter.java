package com.mapbar.traffic.score.logs;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import com.mapbar.traffic.score.mail.MailTaskQueue;
import com.mapbar.traffic.score.utils.DateUtils;

public class SourceRepCounter {

	private static final Logger logger = Logger.getLogger(SourceRepCounter.class);

	/**
	 * 城市数据源 成功率计数器
	 */
	public static Map<String, Map<String, Integer>> sourceRepMap = new HashMap<String, Map<String, Integer>>();

	public static void addSuccessCount(SourceLogBean bean) {
		String source = bean.getSource();

		if (sourceRepMap.get(source) == null) {
			Map<String, Integer> num = new HashMap<String, Integer>();
			num.put("err_c", 0);
			sourceRepMap.put(source, num);
		} else {
			Map<String, Integer> map = sourceRepMap.get(source);
			if (0 != map.get("err_c")) {
				map.put("err_c", 0);
			}
		}
	}

	public static void addErrCount(SourceLogBean bean) {
		String source = bean.getSource();
		if (sourceRepMap.get(source) == null) {
			Map<String, Integer> num = new HashMap<String, Integer>();
			num.put("err_c", 1);
			sourceRepMap.put(source, num);
		} else {
			Map<String, Integer> map = sourceRepMap.get(source);
			Integer num = map.get("sent_date") == null ? 0 : map.get("sent_date");
			Integer date = DateUtils.getCurrentDate();

			if (null != num || num != date) {
				Integer err_c = map.get("err_c");
				err_c++;
				if (err_c >= 15) {
					err_c = 0;
					map.put("sent_date", date);
					logger.info(date + ",驾驶证积分数据源邮件告警：" + bean.getCityName() + "," + bean.getCityPy() + "," + bean.getProvince() + "," + bean.getPro() + "," + (LogUtil.sourceCNMap.containsKey(bean.getSource()) ? LogUtil.sourceCNMap.get(bean.getSource()) : (bean.getSource())) + ",查询 " + " 出现大量异常，请尽快处理! ");
					MailTaskQueue.getInstance().addTask(date + ",驾驶证积分数据源：" + (LogUtil.sourceCNMap.containsKey(bean.getSource()) ? LogUtil.sourceCNMap.get(bean.getSource()) : (bean.getSource()) + " 查询 " + " 出现大量异常，请尽快处理! "));
				}
				map.put("err_c", err_c);
			}
		}
	}

}
