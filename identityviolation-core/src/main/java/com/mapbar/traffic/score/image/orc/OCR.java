package com.mapbar.traffic.score.image.orc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.mapbar.traffic.score.image.ImageIOHelper;
import com.mapbar.traffic.score.utils.PropertiesUtils;

import org.jdesktop.swingx.util.OS;

public class OCR {
	 
	public final static String LANG_OPTION = "-l"; 
	public final static String EOL = "";
	public static String tessPath =PropertiesUtils.getProValue("TESSERACT_ORC_PATH");
	//new File("tesseract").getAbsolutePath(); 

	public String recognizeText(File imageFile, String imageFormat,
			String resultPath) throws Exception {
		File tempImage = null;
		if (imageFormat.equalsIgnoreCase("tif") || imageFormat.equalsIgnoreCase("gif")
				|| imageFormat.equalsIgnoreCase("jpg") || imageFormat.equalsIgnoreCase("png")) {
			tempImage = imageFile;
		} else {
			tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
		}

		File outputFile = new File(resultPath);
		StringBuffer strB = new StringBuffer();

		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(tessPath + "tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(tessPath + "tesseract.exe");
		}
		cmd.add("");
		cmd.add(outputFile.getPath());
		//

		cmd.add("-psm");
		cmd.add("6");
		cmd.add(LANG_OPTION);
		cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());

		cmd.set(1, tempImage.getPath());

		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		// [D:\ProgramFile\Tesseract-OCR\tesseract.exe,
		// D:\validatecode\validatecode0.tif, D:\validatecode\output]
		int w = process.waitFor();
		

		// delete temp working files
		// tempImage.delete();

		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(new

			FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));

			String str;

			while ((str = in.readLine()) != null) {
				strB.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's  filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			tempImage.delete();
			throw new RuntimeException(msg);
		}

		new File(outputFile.getAbsolutePath() + ".txt").delete();

		return strB.toString();
	}
	
	
	public String recognizeText(File imageFile, String imageFormat,
			String resultPath,List<String> cmd) throws Exception {
		File tempImage = null;
		if (imageFormat.equals("tif") || imageFormat.equals("gif")
				|| imageFormat.equals("jpg")) {
			tempImage = imageFile;
		} else {
			tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
		}

		File outputFile = new File(resultPath);
		StringBuffer strB = new StringBuffer();
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		// [D:\ProgramFile\Tesseract-OCR\tesseract.exe,
		// D:\validatecode\validatecode0.tif, D:\validatecode\output]
		int w = process.waitFor();
		 

		// delete temp working files
		// tempImage.delete();

		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(new

			FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));

			String str;

			while ((str = in.readLine()) != null) {
				strB.append(str).append(" ");
			}
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's  filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			tempImage.delete();
			throw new RuntimeException(msg);
		}

		new File(outputFile.getAbsolutePath() + ".txt").delete();

		return strB.toString();
	}
	
	
}
