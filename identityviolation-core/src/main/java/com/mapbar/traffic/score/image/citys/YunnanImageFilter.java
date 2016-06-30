package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class YunnanImageFilter extends CityImageFilter{

	/**
	 * 处理的图片 去噪点
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

					gray[x][y] = argb;

					if (g > 90 && r <= 90 && b <= 90) {
						gray[x][y] = -1;
					}
					if (r <= 90 && b > 90) {
						gray[x][y] = -1;
					}

					if (r > 80) {
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
							if ((r1 < 85 && b1 < 85 && g1 < 85)
									|| (r2 < 85 && b2 < 85 && g2 < 85)) {
								gray[x][y] = -16773632;
							}
						}
						if ((x - 1) >= 0 && (x + 1) < w) {
							int argb1 = bufferedImage.getRGB(x - 1, y);
							int argb2 = bufferedImage.getRGB(x + 1, y);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;

							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if ((r1 < 85 && b1 < 85 && g1 < 85)
									|| (r2 < 85 && b2 < 85 && g2 < 85)) {
								gray[x][y] = -16773632;
							}
						}
					}
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}

			ImageIO.write(binaryBufferedImage, imgType, new File(newPath));

		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
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
