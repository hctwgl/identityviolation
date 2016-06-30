package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class XiamenImageFilter  extends CityImageFilter{
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
			int[][] gray = new int[w][h];
			// int[][] gray2 =  new int[w][h];
			// 灰度化
		
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
                    if(x<3 || x>(w-3) || y<3 || y>(h-3) || x<=20 || x>=75){
                    	argb = -1;
                    	gray[x][y] = argb;
                    	binaryBufferedImage.setRGB(x, y, gray[x][y]);
                    	continue;
					}
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					
					if(r<=90 && g<=90 && b<=90){
						gray[x][y] = -1;
						
						if ((y - 1) >= 0 && (y + 1) < h) {
							int argb1 = bufferedImage.getRGB(x, y - 1);
							int argb2 = bufferedImage.getRGB(x, y + 1);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;

							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							
							if ((r1>90 && g1<90 && b1<90)
									|| (r2>90 && g2<90 && b2<90)) {
								
								gray[x][y] = -916442;
								
							}
						}
						
						 
					}else{
						if(r>90 && g>90 && b>90){
							   gray[x][y] = -1;
						   }else if(r>90 && g<60 && b<60){
							   gray[x][y]=-916442;
						   }  
					}	   
 
					 
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
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
