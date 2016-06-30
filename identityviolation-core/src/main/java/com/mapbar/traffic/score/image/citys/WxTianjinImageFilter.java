package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

 

import com.mapbar.traffic.score.image.ImageIOHelper;

public class WxTianjinImageFilter  extends CityImageFilter{

	/**
	 * 处理图片 去边框 二值化
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType) {
		File ff = null;
		BufferedImage bufferedImage=null;
		BufferedImage binaryBufferedImage=null;
		try {
			ff = new File(oldPath);
			bufferedImage = ImageIOHelper.getImage(ff);
			
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();
			binaryBufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_BYTE_BINARY);
			 
			// 灰度化
			 
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
                    if(x==0 || y==0 || x==(w-1) || y==(h-1)){
                    	argb=-4275519;
                    } 
                    int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					//System.out.println("  x==="+x + ",y==="+y + ", r="+r+", g="+g + ", b="+b +", argb="+argb);
					int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					gray[x][y] = grayPixel;
					 
				}
			}
			 //二值化
			int threshold = ostu(gray, w, h);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					// System.out.println("x=="+x + "  y=="+y);
					if (gray[x][y] > threshold) {
						gray[x][y] = 0x00FFFF; // 白色// 白色
					} else {
						gray[x][y] = 0xFF0000; // 白色// 白色
						if ((y + 1) < h && (y - 1) >= 0 && (x + 1) < w && (x - 1) >= 0) { 
							int flag = 0;
							if(gray[x-1][y]>threshold){
								flag++;
							}
							if(gray[x+1][y]>threshold){
								flag++;
							}
							if(gray[x][y+1]>threshold){
								flag++;
							}
							if(gray[x][y-1]>threshold){
								flag++;
							}
							if(flag>=3){
								gray[x][y] = 0x00FFFF; 
							}
						}
					}
					binaryBufferedImage.setRGB(x, y, gray[x][y] );
				}
			}
			ImageIO.write(binaryBufferedImage, imgType, new File(
					newPath));
		 
			 
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
}
