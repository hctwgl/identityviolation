package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class GuizhouImageFilter {
	/**
	 * 处理贵州的图片 去噪点
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public boolean dealImage(String oldPath, String newPath, String imgType) {
		File ff = null;
		BufferedImage bufferedImage=null;
		BufferedImage binaryBufferedImage=null;
		try {
            ff = new File(oldPath);
			bufferedImage = ImageIOHelper.getImage(ff);
			if(bufferedImage==null){
				return false;
			}
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();
			binaryBufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			 
			// 灰度化
			int[][] gray2 = new int[w][h];
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
					
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					 
					
					//int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					if( r==255 && g==255 && b==0){
						gray[x][y] = -1;
						gray2[x][y]= -1;
					}else{
						gray[x][y] = argb;
						gray2[x][y]=argb;
					}
					
				}
			}

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
                     
					int n=0;
					int argbt = gray[x][y];
					//System.out.println("  x==="+x +" y="+y + "  r="+r + " , g="+g + ",b="+b + ", argbt="+argbt);
					if(x==0 || y==0 || x== (w-1) || y==(h-1)){
						 gray[x][y]=-1;
					}
					if(argbt!=-1 && (x-1)>=0 && (y-1)>=0 && (x+1)<w && (y+1) < h){
						int argb1 = gray2[x-1][y];
						int argb2 = gray2[x+1][y];
						int argb3 = gray2[x][y-1];
						int argb4 = gray2[x][y+1];
						
						if(argb1==-1){
							n++;
						}
						if(argb2==-1){
							n++;
						}
						if(argb3==-1) n++;
						if(argb4==-1) n++;
						if(n>=3){
							gray[x][y]=-1;
						}
					}
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}
			ImageIO.write(binaryBufferedImage, imgType, new File(
					newPath));

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
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
		return true;
	}
}
