package com.mapbar.traffic.score.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.http.HttpHost;

public class HttpClientProxyManager implements Runnable {
	private static int PROXY_REFRESH_TIME = Integer.parseInt(System.getProperty("PROXY_REFRESH_TIME", "600000"));
	private HttpHost[] proxys = null;
	private String strProxyFile = null;
	private long lastModified = 0L;

	private int iProxy = 0;

	private Hashtable<String, HttpHost> htProxies = new Hashtable<String, HttpHost>();

	public HttpClientProxyManager(String strProxyFile) {
		this.strProxyFile = strProxyFile;
		try {
			check();
			new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int size() {
		return ((this.proxys == null) ? 0 : this.proxys.length);
	}

	public String checkStatus() {
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < this.proxys.length; ++i) {
				sb.append("[" + this.proxys[i].toURI() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private synchronized void check() {
		try {
			File file = new File(this.strProxyFile);

			if (!(file.exists())) {
				return;
			}

			long lastUpdated = file.lastModified();

			if (this.lastModified == lastUpdated)
				return;
			this.lastModified = lastUpdated;

			BufferedReader br = new BufferedReader(new FileReader(file));

			Vector<HttpHost> vProxyServers = new Vector<HttpHost>();
			for (String strLine = null; (strLine = br.readLine()) != null;) {
				if ((!(StringUtil.isNotEmpty(strLine))) || (strLine.startsWith("#")))
					continue;
				String strIP = strLine;
				int iPort = 80;
				int idx = strLine.indexOf(":");
				if (idx > 0) {
					strIP = strLine.substring(0, idx).trim();
					String strPort = strLine.substring(idx + 1).trim();
					iPort = Integer.parseInt(strPort);
				}

				HttpHost currproxy = new HttpHost(strIP, iPort);
				vProxyServers.addElement(currproxy);
			}

			br.close();

			System.out.println("[ProxyLoaded]" + vProxyServers.size());

			if (vProxyServers.size() <= 0)
				return;
			HttpHost[] tmps = new HttpHost[vProxyServers.size()];
			for (int i = 0; i < vProxyServers.size(); ++i) {
				tmps[i] = ((HttpHost) vProxyServers.elementAt(i));
			}

			this.proxys = tmps;
			this.setiProxy((RegUtils.getNumberInRange(0, tmps.length) % tmps.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long lastUpdated() {
		return this.lastModified;
	}

	public synchronized HttpHost next() {
		return next(false);
	}

	public synchronized HttpHost next(boolean bForced) {
		HttpHost proxy = null;

		String strCurrThread = Thread.currentThread().getName();

		if ((!(bForced)) && (this.htProxies.containsKey(strCurrThread))) {
			proxy = (HttpHost) this.htProxies.get(strCurrThread);
		} else if (this.proxys != null) {
			proxy = this.proxys[(RegUtils.getNumberInRange(0, 2147483647) % this.proxys.length)];
			this.htProxies.put(strCurrThread, proxy);
		}

		return proxy;
	}

	public void run() {
		try {
			long lstartime = System.currentTimeMillis();

			if (System.currentTimeMillis() - lstartime >= PROXY_REFRESH_TIME) {
				lstartime = System.currentTimeMillis();
				File file = new File(this.strProxyFile);
				if (file.exists()) {
					file.setLastModified(lstartime);
				}
			}

			try {
				check();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Thread.sleep(1000L);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setiProxy(int iProxy) {
		this.iProxy = iProxy;
	}

	public int getiProxy() {
		return iProxy;
	}
}