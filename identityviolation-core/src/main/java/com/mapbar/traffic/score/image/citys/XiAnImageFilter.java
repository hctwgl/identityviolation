package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class XiAnImageFilter extends CityImageFilter{
	/**
	 * 处理图片 去噪点
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
					
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					if(r>=85 && r<=160 && g>=85 && g<=160 && b>=85 && b<=160){
						//System.out.println(" 1 x==="+x +" y="+y + "  r="+r + " , g="+g + ",b="+b);
						gray[x][y] = -1;
					}else{
						gray[x][y] = argb;
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
	}
}
