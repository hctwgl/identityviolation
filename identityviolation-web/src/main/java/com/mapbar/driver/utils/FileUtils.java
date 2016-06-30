package com.mapbar.driver.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

	public static int getNextIndex(String name, int max) throws IOException {
		File temp = new File("./tmp/" + name);
		if (!temp.getParentFile().exists()) {
			temp.getParentFile().mkdirs();
		}
		if (!temp.exists()) {
			temp.createNewFile();
		}
		int i = 0;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(temp));
		try {
			i = Integer.parseInt(bufferedReader.readLine().trim());
		} catch (Exception e) {
			i = 0;
		}
		bufferedReader.close();
		if (i + 1 < max) {
			i++;
		} else {
			i = 0;
		}
		FileOutputStream fs = new FileOutputStream(temp);
		fs.write(String.valueOf(i).getBytes());
		fs.close();
		return i;
	}

	public static ArrayList<File> listDirs(File file, FilenameFilter filter) {
		ArrayList<File> collection = new ArrayList<File>();
		LinkedList<File> dirs = new LinkedList<File>();
		dirs.add(file);
		while (dirs.size() > 0) {
			File current = (File) dirs.pollFirst();
			if (current.isFile())
				continue;
			if (current.isDirectory()) {
				File[] files = current.listFiles(filter);
				if ((files != null) && (files.length > 0)) {
					collection.add(current);
				} else {
					File[] sub = current.listFiles();
					for (int index = 0; (sub != null) && (index < sub.length); ++index) {
						if (sub[index].isDirectory()) {
							dirs.add(sub[index]);
						}
					}
				}
			}
		}
		return collection;
	}

	public static ArrayList<File> listFiles(File root, FilenameFilter filter) {
		ArrayList<File> selected = new ArrayList<File>();
		selected.add(root);
		for (int pos = 0; pos < selected.size();) {
			File file = (File) selected.get(pos);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int index = 0; index < files.length; ++index) {
					selected.add(files[index]);
				}
				selected.remove(pos);
			} else if (filter.accept(file, file.getName())) {
				++pos;
			} else {
				selected.remove(pos);
			}
		}

		return selected;
	}

	public static String getRegex(String urlhtml, String regex, int i) {
		String title = "";
		Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher ma = pa.matcher(urlhtml);
		while (ma.find()) {
			title = ma.group(i);
		}
		return title;
	}

	public ArrayList<String> getRegexList(String urlhtml, String regex, int i) {
		ArrayList<String> list = new ArrayList<String>();
		Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher ma = pa.matcher(urlhtml);
		while (ma.find()) {
			list.add(ma.group(i));
		}
		return list;
	}
}
