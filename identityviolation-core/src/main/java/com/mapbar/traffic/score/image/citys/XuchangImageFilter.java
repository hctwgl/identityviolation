package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class XuchangImageFilter extends CityImageFilter {
	/**
	 * 处理图片 去边框 二值化
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

			binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			int[][] gray = new int[w][h];
			int[][] gray2 = new int[w][h];

			// 灰度化

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					int argb = bufferedImage.getRGB(x, y);
					// 图像加亮（调整亮度识别率非常高）
					// int r = (argb & 0xff0000) >> 16;
					// int g = (argb & 0xff00) >> 8;
					// int b = (argb & 0xff);
					gray[x][y] = argb;

				}
			}
			// 二值化

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					if (gray[x][y] == -592667) {
						gray2[x][y] = -1;
					} else {
						gray2[x][y] = -916442;
						if ((y + 1) < h && (y - 1) >= 0 && (x + 1) < w && (x - 1) >= 0) {
							int flag = 0;
							if (gray[x - 1][y] == -592667) {
								flag++;
							}
							if (gray[x + 1][y] == -592667) {
								flag++;
							}
							if (gray[x][y + 1] == -592667) {
								flag++;
							}
							if (gray[x][y - 1] == -592667) {
								flag++;
							}
							if (flag > 3) {
								gray2[x][y] = -1;
							}
						}
					}

					binaryBufferedImage.setRGB(x, y, gray2[x][y]);
				}
			}

			ImageIO.write(binaryBufferedImage, imgType, new File(newPath));
		} catch (IOException e) {
			e.printStackTrace();
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
