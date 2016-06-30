package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class HeilongjiangImageFilter {
	/**
	 * 处理深圳的图片 去噪点
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType) {
		File ff = new File(oldPath);
		try {
			
			BufferedImage bufferedImage = ImageIOHelper.getImage(ff);
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();

			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					
					
					if(r>25 && g>25 && b>25){
						argb = -1;
						gray[x][y]=-1;
						continue;
					}
					//int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					if(y<=3 || y>29){
						gray[x][y] = -1;
					}else{
						gray[x][y] = argb;
					    if((x-1)>=0 && (x+1)<w){
					    	int argb1 = bufferedImage.getRGB(x-1, y);
					    	int argb2 = bufferedImage.getRGB(x+1, y);
					    	int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;
							
							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if(r1>25 && g1>25 && b1>25 && r2>25 && b2>25 && g2>25){
								gray[x][y] = -1;
								continue;
							}
					    }
						if((y + 1) < h && (y - 1) >= 0){
							int argb1 = bufferedImage.getRGB(x, y-1);
					    	int argb2 = bufferedImage.getRGB(x, y+1);
					    	int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;
							
							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if(r1>25 && g1>25 && b1>25 && r2>25 && b2>25 && g2>25){
								if((x+1)<w && (x-1)>=0){
									int argb3 = bufferedImage.getRGB(x+1, y-1);
							    	int argb4 = bufferedImage.getRGB(x-1, y+1);
							    	int r3 = (argb3 >> 16) & 0xFF;
									int g3 = (argb3 >> 8) & 0xFF;
									int b3 = (argb3 >> 0) & 0xFF;
									
									int r4 = (argb4 >> 16) & 0xFF;
									int g4 = (argb4 >> 8) & 0xFF;
									int b4 = (argb4 >> 0) & 0xFF;
									
									
									int argb5 = bufferedImage.getRGB(x+1, y+1);
							    	//int argb6 = bufferedImage.getRGB(x-1, y-1);
							    	int r5 = (argb5 >> 16) & 0xFF;
									int g5 = (argb5 >> 8) & 0xFF;
									int b5 = (argb5 >> 0) & 0xFF;
//									
//									int r6 = (argb6 >> 16) & 0xFF;
//									int g6 = (argb6 >> 8) & 0xFF;
//									int b6 = (argb6 >> 0) & 0xFF;
//									
									
									if(r3>25 && g3>25 && b3>25 && r4>25 && b4>25 && g4>25
											&& r5>25 && g5>25 && b5>25 
											//&& r6>25 && b6>25 && g6>25
											){
										gray[x][y] = -1;
										continue;
									}
								}
							}
						}
					}
					
				}
			}
			BufferedImage binaryBufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
//					
//					if( gray[x-1][y]==-1 && gray[x+1][y]==-1 &&  gray[x][y]!=-1){
//						gray[x][y]=-1;
//					}else{
//						if ((y + 1) < h && (y - 1) >= 0) {
//							if (gray[x][y + 1] == -1 && gray[x][y - 1] == -1) {
//							
//								if((x+1)<w && gray[x+1][y] == -1 && gray[x][y - 1] == -1){
//									gray[x][y] = -1;
//								}
//								
//								
//							}
//						}
//						
//					}
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}
//	           
	        
               ImageIO.write(binaryBufferedImage, imgType, new File(newPath));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			ff.deleteOnExit();
		}
		 
	}
		 
}
