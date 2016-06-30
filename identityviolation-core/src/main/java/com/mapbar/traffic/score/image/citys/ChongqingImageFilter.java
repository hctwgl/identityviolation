package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class ChongqingImageFilter extends CityImageFilter{
	/**
	 * 处理深圳的图片 去噪点
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
			
			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);

					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;

					gray[x][y] = argb;

					if (r <= 90 && g <= 90 && b <= 90) {
						gray[x][y] = -1;
						if ((y - 3) >= 0 && (y + 3) < h) {
							int argb1 = bufferedImage.getRGB(x, y - 3);
							int argb2 = bufferedImage.getRGB(x, y + 3);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;

							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;

							if ((r1 > 90 && g1 < 90 && b1 < 90)
									|| (r2 > 90 && g2 < 90 && b2 < 90)) {

								gray[x][y] = -392960;

							}
						}
					} else {
						// r=250 , g=1,b=0 , argb=-392960
						if (r > 190 && g > 190 && b > 190) {
							gray[x][y] = -1;
						} else if (r > 90 && g < 60 && b < 60) {
							gray[x][y] = -392960;
						}
					}
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}

			ImageIO.write(binaryBufferedImage, imgType, new File(newPath));

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
