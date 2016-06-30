package com.mapbar.traffic.score.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ImageCastTest {
	@Test
	public void test() throws IOException {
		String path = "D:\\validatecode\\guangzhou\\";
		for (int i = 1; i <= 3; i++) {
			String name = String.valueOf(i) + ".jpg";
			File ff = new File(path + name);
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
					int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);
					gray[x][y] = grayPixel;
				}
			}

			int threshold = ImageCast.ostu(gray, w, h);
			BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {

					if (gray[x][y] > threshold) {
						gray[x][y] |= 0x00FFFF;
					} else {
						gray[x][y] = 0xFF0000;
					}

					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
			}

			ImageIO.write(binaryBufferedImage, "jpg", new File(path + String.valueOf(i) + "-temp.jpg"));
			// ImageIOHelper.createImage(new File(path+ String.valueOf(i)+"-temp.jpg"), "jpg");
		}
	}
}
