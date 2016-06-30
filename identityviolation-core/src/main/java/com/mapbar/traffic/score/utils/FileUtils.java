package com.mapbar.traffic.score.utils;

import java.io.File;

public class FileUtils {

	public static String checkDerictor(String path, String cityPy, String date) {
		File file = new File(path + cityPy + "/" + date);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath();
	}
}
