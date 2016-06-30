package com.mapbar.driver.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.mapbar.driver.utils.ConfigUtil;
import com.mapbar.traffic.score.redis.RedisDBUtil;

/**
 * @进行ip检测、apache、nginx检测等常规过滤服务
 */
public class IpcheckFilter implements Filter {
	private FilterConfig filterConfig;

	private Set<String> ignoreIps = new HashSet<String>();
	private Set<String> interceptIps = new HashSet<String>();
	private Integer controlCount = 200;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
		/*************************** 获取ip数据 *********************************/
		HttpServletRequest req = (HttpServletRequest) request;
		String user_Ip = req.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(user_Ip)) {
			user_Ip = request.getRemoteAddr();
		}
		if (StringUtils.isNotEmpty(user_Ip) && user_Ip.indexOf(",") >= 0) {
			String[] forwarded_ip = user_Ip.split(",");
			user_Ip = forwarded_ip[0];
		}

		// 任务队列
		if (interceptIps.contains(user_Ip)) {
			return;
		}
		if (ignoreIps.contains(user_Ip) || user_Ip.startsWith("116.213.115.") || user_Ip.startsWith("10.0.1.")) {
			filterchain.doFilter(request, response);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String sysdate = sdf.format(new Date());
			String key = "jiashizheng_ipcontrol_" + user_Ip;
			String value = RedisDBUtil.getValue(key);
			if (StringUtils.isEmpty(value)) {
				RedisDBUtil.setExpireValue(key, sysdate + "," + "1", 24 * 60 * 60);
				filterchain.doFilter(request, response);
			} else {
				String num = value.split(",")[1];
				String time = value.split(",")[0];
				if (sysdate.equals(time)) {
					Integer currentNum = new Integer(num) + 1;
					if (currentNum < controlCount) {
						RedisDBUtil.setExpireValue(key, sysdate + "," + String.valueOf(currentNum), 24 * 60 * 60);
						filterchain.doFilter(request, response);
					} else {
						response.getWriter().print("to many requests per date");
						return;
					}
				} else {
					RedisDBUtil.setExpireValue(key, sysdate + ",1", 24 * 60 * 60);
					filterchain.doFilter(request, response);
				}

			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.setFilterConfig(filterConfig);
		String ips = ConfigUtil.bundle.getString("ip_check_ignore");
		if (StringUtils.isNotEmpty(ips)) {
			for (String s : ips.trim().split(",")) {
				ignoreIps.add(s);
			}
		}
		String count = ConfigUtil.bundle.getString("ip_per_date_query");
		if (StringUtils.isNotEmpty(count)) {
			controlCount = new Integer(count.trim());
		}

		String ip_check_intercept = ConfigUtil.bundle.getString("ip_check_intercept");
		if (StringUtils.isNotEmpty(count)) {
			for (String s : ip_check_intercept.trim().split(",")) {
				interceptIps.add(s);
			}
		}

	}

	@Override
	public void destroy() {
		setFilterConfig(null);
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

}
