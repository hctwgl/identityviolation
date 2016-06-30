package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;


public class NanjingImageFilter extends CityImageFilter{
	
	
	/**
	 * 处理南京的图片 去掉干扰线
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType){
		File ff = null;
		BufferedImage bufferedImage  = null;
		BufferedImage binaryBufferedImage = null;
		try {
			ff = new File(oldPath);
			bufferedImage = ImageIOHelper.getImage(ff);
			
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();

			// 灰度化
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);

					gray[x][y] = argb;
					
				}
			}

			// 二值化
			
			binaryBufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			erzhi(gray, w, h, binaryBufferedImage);

			ImageIO.write(binaryBufferedImage, imgType, new File(
					newPath));
//			ImageIOHelper.createImage(new File(
//					newFile), "png");
//			 File ffff = new File(newFile);
//			 String valCode = new OCR().recognizeText(ffff, "tif");
		//	 System.out.println("valCode======="+valCode);
			//
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(ff!=null){
				ff.deleteOnExit();
			}
			if(bufferedImage!=null){
				bufferedImage.flush();
			}
			if(binaryBufferedImage!=null){
				binaryBufferedImage.flush();
			}
			
		}
	}
	public static void erzhi(int[][] gray, int w, int h,
			BufferedImage binaryBufferedImage) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if(gray[x][y]==-4144960){
					gray[x][y] = -1;
				}
				binaryBufferedImage.setRGB(x, y, gray[x][y]);
			}
		}
	}

}
