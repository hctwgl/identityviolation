package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class AnhuiImageFilter extends CityImageFilter {
	/**
	 * 处理深圳的图片 去噪点
	 * 
	 * @param oldPath
	 * @param newPath
	 * @param imgType
	 */
	public void dealImage(String oldPath, String newPath, String imgType) {
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

					int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					gray[x][y] = grayPixel;
				}
			}

			int threshold = ostu(gray, w, h);
			binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int n = 0;
					if (x == 0 || x == (w - 1) || y == 0 || y == (h - 1)) {
						gray[x][y] = 0x00FFFF;
					} else {
						if (gray[x][y] > threshold) {
							gray[x][y] |= 0x00FFFF; // 白色
						} else {
							gray[x][y] = 0xFF0000; // 黑色

							if ((x - 1) >= 0 && (y - 1) >= 0 && (x + 1) < w && (y + 1) < h) {
								int argb1 = gray[x - 1][y];
								int argb2 = gray[x + 1][y];
								int argb3 = gray[x][y - 1];
								int argb4 = gray[x][y + 1];

								if (argb1 > threshold) {
									n++;
								}
								if (argb2 > threshold) {
									n++;
								}
								if (argb3 > threshold)
									n++;
								if (argb4 > threshold)
									n++;
								if (n >= 3) {
									gray[x][y] = 0x00FFFF;
								}
							}
						}
					}

					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}

			ImageIO.write(binaryBufferedImage, imgType, new File(newPath));

		} catch (Exception e) {
			e.printStackTrace();
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
