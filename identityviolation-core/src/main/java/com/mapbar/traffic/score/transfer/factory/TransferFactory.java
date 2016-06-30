package com.mapbar.traffic.score.transfer.factory;

import java.util.HashMap;
import java.util.Map;

public class TransferFactory {

	private static Map<String, Transfer> map = new HashMap<String, Transfer>();

	protected TransferFactory() {
	}

	// 静态工厂方法,返还此类惟一的实例
	public static Transfer getInstance(String clsName) {
		if (map.get(clsName) == null) {
			synchronized (TransferFactory.class) {
				if (map.get(clsName) == null) {
					try {
						map.put(clsName, (Transfer) Class.forName(clsName).newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return map.get(clsName);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Transfer> T getInstanceByClass(Class<T> c) {
		if (map.get(c.getName()) == null) {
			synchronized (TransferFactory.class) {
				if (map.get(c.getName()) == null) {
					try {
						map.put(c.getName(), (T) Class.forName(c.getName()).newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (T) map.get(c.getName());
	}

}
