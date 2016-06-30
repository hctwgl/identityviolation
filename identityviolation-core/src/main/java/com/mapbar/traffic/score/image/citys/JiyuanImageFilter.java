package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class JiyuanImageFilter extends CityImageFilter{
	/**
	 * 处理图片去噪点
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType) {
		File ff = null;
		BufferedImage bufferedImage=null;
		BufferedImage binaryBufferedImage =null;
		try {
			ff = new File(oldPath);
	        bufferedImage=  ImageIOHelper.getImage(ff);
	        int h = bufferedImage.getHeight();
	        int w = bufferedImage.getWidth();
	        binaryBufferedImage = new BufferedImage(w, h,
						BufferedImage.TYPE_BYTE_BINARY);
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
					if(x==0 || y==0 || x==(w-1) || y==(h-1)){
						argb=-1;
					}
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					// r=204 , g=169,b=141
					//r=183 , g=166,b=146
					if(r>160 && g>140 && b<150 &&  g<200){
						argb=-16776960;
					}
					//System.out.println(" 2 x==="+x +" y="+y + "  r="+r + " , g="+g + ",b="+b + ", argb="+argb);
					//int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					gray[x][y] = argb;
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}

	        
           ImageIO.write(binaryBufferedImage,imgType, new File(newPath));
//	           File ffff = new File(newPath);
//	           String valCode = new OCR().recognizeText(ffff, "jpg");
//	           System.out.println("valCode======="+valCode);       
	          
	           //System.out.println("valCode======="+valCode);   

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
