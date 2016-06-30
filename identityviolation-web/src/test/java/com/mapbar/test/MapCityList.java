package com.mapbar.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

public class MapCityList {
	private static final Logger logger = Logger.getLogger(MapCityList.class);

	@Test
	public void test() {
		try {
			// InputStreamReader in=new InputStreamReader(new FileInputStream(
			// "H:\\\\WFworkspace\\CarViolation-1.4.x\\data\\citycode.txt"), "utf-8");
			// BufferedReader reader = new BufferedReader(in);
			// String list = reader.readLine();
			// Map map=new HashMap();
			// while (list != null) {
			// String[] a=list.split(",");
			// map.put(a[1],a[0]);
			// list = reader.readLine();
			// }
			//
			// InputStreamReader ins=new InputStreamReader(new FileInputStream(
			// "H:\\\\WFworkspace\\CarViolation-1.4.x\\data\\MapCityList.txt"), "utf-8");
			// BufferedReader readers = new BufferedReader(ins);
			// String lists = readers.readLine();
			// while (lists != null) {
			// String[] a=lists.split(",");
			// if(map.get(a[0])!=null){
			//
			// logger.info(lists+","+map.get(a[0]));
			// }else{
			// logger.info(lists);
			// }
			// lists = readers.readLine();
			// }

			InputStreamReader in = new InputStreamReader(new FileInputStream("H:\\\\WFworkspace\\CarViolation-1.4.x\\data\\MapCityList.txt"), "utf-8");
			BufferedReader reader = new BufferedReader(in);
			String list = reader.readLine();
			Map<String, String> map = new HashMap<String, String>();
			while (list != null) {
				String[] a = list.split(",");
				map.put(a[0], a[2]);
				list = reader.readLine();
			}

			InputStreamReader ins = new InputStreamReader(new FileInputStream("H:\\\\WFworkspace\\CarViolation-1.4.x\\data\\citycode.txt"), "utf-8");
			BufferedReader readers = new BufferedReader(ins);
			String lists = readers.readLine();
			while (lists != null) {
				String[] a = lists.split(",");
				if (map.get(a[1]) != null) {

					// logger.info(a[0]+","+a[1]+","+a[2]+","+map.get(a[1]));
				} else {
					logger.info(a[0] + "," + a[1] + "," + a[2]);
				}
				lists = readers.readLine();
			}
			reader.close();
			readers.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
