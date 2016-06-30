package com.mapbar.traffic.score.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapbar.traffic.score.image.ImageHelper;

public class BeijingImageHash {

	private BeijingImageHash() {
		init();
	}

	private static BeijingImageHash instance = new BeijingImageHash();

	private Map<String, String> hashCodeMap = new HashMap<String, String>();

	public static BeijingImageHash getInstance() {
		return instance;
	}

	private void init() {
		InputStream inputStream = null;
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new FileInputStream(new File("D:/jtbz/hashCodeConfig.txt"));
			read = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String str[] = lineTxt.split("=");
				hashCodeMap.put(str[1], str[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getImageDes(String code) {
		String msg = "";
		List<String> result0 = new ArrayList<String>();
		List<String> result1 = new ArrayList<String>();
		for (Map.Entry<String, String> en : hashCodeMap.entrySet()) {
			String hashCode = en.getKey();
			int difference = hammingDistance(code, hashCode);
			if (difference <= 5) {
				// System.out.println(difference);
				result0.add(difference + "&&" + en.getValue());
			} else if (difference <= 7) {
				// System.out.println(difference);
				result1.add(difference + "&&" + en.getValue());
			}
		}

		if (result0.size() > 0) {
			Collections.sort(result0, this.new SimpleCompare());
		}
		if (result1.size() > 0) {
			Collections.sort(result1, this.new SimpleCompare());
		}

		//System.out.println(result0);
		//System.out.println(result1);
		if (result0.size() > 0) {
			msg = result0.get(0);
		} else {
			msg = result1.size() > 0 ? result1.get(0) : "";
		}
		//System.out.println(msg);
		if ("".equals(msg)) {
			return null;
		} else {
			return msg.split("&&")[1];
		}

	}

	public int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();

		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference++;
			}
		}

		return difference;
	}

	public class SimpleCompare implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			String a[] = o1.split("&&");
			String b[] = o2.split("&&");
			if (new Integer(a[0]) < new Integer(b[0])) {
				return -1;
			} else {
				return 1;
			}

		}
	}

	public String produceFingerPrint(String filename) {
		BufferedImage source = ImageHelper.readJPEGImage(filename);

		int width = 8;
		int height = 8;

		// 第一步，缩小尺寸。
		// 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
		BufferedImage thumb = ImageHelper.thumb(source, width, height, false);

		// 第二步，简化色彩。
		// 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
		int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i * height + j] = ImageHelper.rgbToGray2(thumb.getRGB(i, j));
			}
		}

		// 第三步，计算平均值。
		// 计算所有64个像素的灰度平均值。
		int avgPixel = ImageHelper.average(pixels);

		// 第四步，比较像素的灰度。
		// 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
		int[] comps = new int[width * height];
		for (int i = 0; i < comps.length; i++) {
			if (pixels[i] >= avgPixel) {
				comps[i] = 1;
			} else {
				comps[i] = 0;
			}
		}

		// 第五步，计算哈希值。
		// 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i += 4) {
			int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
			hashCode.append(binaryToHex(result));
		}

		// 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
		return hashCode.toString();
	}

	private static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary) {
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}
}
