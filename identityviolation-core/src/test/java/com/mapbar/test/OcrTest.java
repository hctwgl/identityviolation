package com.mapbar.test;

import java.io.File;

import com.mapbar.traffic.score.image.orc.OCR;

public class OcrTest {
	//@Test
	public void testOcr() {
		OCR ocr = new OCR();
		String resultPath = "D:\\Program Files (x86)\\Tesseract-OCR";
		String imageFormat = "jpg";
		File imageFile = new File("D:\\Program Files (x86)\\Tesseract-OCR\\CpQ7.jpg");
		try {
			String text = ocr.recognizeText(imageFile, imageFormat, resultPath);
			System.out.println(text);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
