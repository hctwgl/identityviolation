package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class FujianImageFilter extends CityImageFilter{
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

			// 灰度化
			 
					 
						for (int x = 0; x < w; x++) {
							for (int y = 0; y < h; y++) {
								int argb = bufferedImage.getRGB(x, y);
								
							

								if(y<=10 || x<10 || x>(w-10)){
									argb = -1;
								}else if((y+1)<h && (y-1)>0){
									int argby1 = bufferedImage.getRGB(x, y+1);
									int argby2 = bufferedImage.getRGB(x, y-1);
									if(argby2==-1 ||  argby1==-1){
										argb = -1;
									}
								}else{
									argb = -1;
								}
								gray[x][y]=argb;
								//binaryBufferedImage.setRGB(x, y, gray[x][y]);
							}
						}
						for (int x = 0; x < w; x++) {
							for (int y = 0; y < h; y++) {
								//int argb = gray[x][y];
								if((x-1)>0 && (x+1)<w && (y-1)>0 && (y+1)<h){
									int f = 0;
								    if(gray[x+1][y] == -1){
										f++;
									}
								    if(gray[x-1][y] == -1){
										f++;
									}
								    if(gray[x][y+1] == -1){
										f++;
									}
								    if(gray[x][y-1] == -1){
										f++;
									}
								    if(gray[x+1][y+1] == -1){
										f++;
									}
								    if(gray[x-1][y-1] == -1){
										f++;
									}
								    if(gray[x-1][y+1] == -1){
										f++;
									}
								    if(gray[x+1][y-1] == -1){
										f++;
									}
								    if(f>=7){
								    	gray[x][y] = -1;
								    }
								}
								binaryBufferedImage.setRGB(x, y,gray[x][y]);
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
