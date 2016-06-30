package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class XuzhouImageFilter extends CityImageFilter {
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
					BufferedImage.TYPE_3BYTE_BGR);
			 
			// 灰度化
			  int[][] gray = new int[w][h];
		       
			  for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						int argb = bufferedImage.getRGB(x, y);
						if (x >=49 || y==0 || y==(h-1) || x==0) {
							argb = -196868;
						}
						gray[x][y]=argb;

//						System.out.println("  x==="+x + ",y==="+y + ", argb="+argb);
//						binaryBufferedImage.setRGB(x, y, argb);
					}
				}
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						
						if((y - 1) >= 0 && (y + 1) < h && (x-1)>=0 && (x+1) <w){
							int f = 0;
						    if(gray[x+1][y] == -196868){
								f++;
							}
						    if(gray[x-1][y] == -196868){
								f++;
							}
						    if(gray[x][y+1] == -196868){
								f++;
							}
						    if(gray[x][y-1] == -196868){
								f++;
							}
						    if(f==4){
						    	gray[x][y]=-196868;
						    }
						   
						} 
						binaryBufferedImage.setRGB(x, y, gray[x][y]);
					}
				}
			 //二值化
 
			 
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
