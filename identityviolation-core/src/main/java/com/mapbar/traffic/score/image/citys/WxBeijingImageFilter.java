package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class WxBeijingImageFilter extends CityImageFilter {
	/**
	 * 处理图片 去边框 二值化
	 * 
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType) throws Exception {
		File ff = null;
		BufferedImage bufferedImage = null;
		BufferedImage binaryBufferedImage = null;
		try {
			ff = new File(oldPath);
			bufferedImage = ImageIOHelper.getImage(ff);
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();

			int[][] gray = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int argb = bufferedImage.getRGB(x, y);
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					gray[x][y] = argb;
				}
			}

			binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					if (x == 0 || x == (w - 1) || y == 0 || y == (h - 1)) {
						gray[x][y] = -1;
					}

					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}
			//

			ImageIO.write(binaryBufferedImage, imgType, new File(newPath));

		} catch (IOException e) {
			throw e;

		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		} finally {
			if (ff != null) {
				ff.deleteOnExit();
			}
			if (bufferedImage != null) {
				bufferedImage.flush();
			}
			if (binaryBufferedImage != null) {
				binaryBufferedImage.flush();
			}
		}

	}
}
