package com.mapbar.traffic.score.utils;

import java.net.Proxy;
import java.net.SocketAddress;

public class HttpsProxy extends Proxy {
	private int nTotals = 0;
	private int nOKs = 0;

	public HttpsProxy(Type type, SocketAddress addr) {
		super(type, addr);
	}

	public synchronized void setStatus(boolean bOK) {
		this.nTotals += 1;
		if (!(bOK))
			return;
		this.nOKs += 1;
	}

	public String checkStatus() {
		return this.nOKs + "/" + this.nTotals;
	}
}