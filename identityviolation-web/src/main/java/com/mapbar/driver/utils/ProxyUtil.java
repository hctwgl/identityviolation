package com.mapbar.driver.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtil {

	// private static final String DATA_FILE ="/mapbar/data2/crawdata/youlaopo/proxydata/data_proxy.txt";
	private static final String DATA_FILE = "./proxydata/data_proxy.txt";
	protected List<String> proxies = new ArrayList<String>(0);

	private static ProxyUtil proxyStore = new ProxyUtil();

	private ProxyUtil() {
	}

	public static ProxyUtil getProxyStore() {
		try {
			proxyStore.proxies = proxyStore.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxyStore;
	}

	protected List<String> load() throws IOException {
		List<String> proxies = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(DATA_FILE)));
		String proxyLine;
		while ((proxyLine = reader.readLine()) != null) {
			if (proxyLine == null || proxyLine.length() <= 0) {
				continue;
			}
			proxies.add(proxyLine);
		}
		reader.close();
		return proxies;
	}

	public String getNextProxy() throws IOException {
		int i = FileUtils.getNextIndex("proxy", proxies.size());
		return proxies.get(i).split(":")[0];
	}

}
