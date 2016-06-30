package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;

public class ZhejiangImageFilter  extends CityImageFilter{

	public int dealImage(String oldPath, String newPath, String imgType) {
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
			
			boolean isred=false;
			
			boolean isblue=false;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {

					int argb = bufferedImage.getRGB(x, y);
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					if (r < 90 && g < 90 && b > 180) {
						isblue = true;
						break;
					}
					if (r > 180 && g < 90 && b < 90) {
						isred = true;
						break;
					}
				}
				if(isblue){
					break;
				}
				if(isred){
					break;
				}
			}

			if(isred){
				//System.out.println("red");
				dealRed(bufferedImage,binaryBufferedImage,gray,w,h,newPath);
				return 1;
			}
			if(isblue){
				//System.out.println("blue");
				dealBlue(bufferedImage,binaryBufferedImage,gray,w,h,newPath);
				return 2 ;
			}
			//System.out.println("black");
			
			dealBlack(bufferedImage,binaryBufferedImage,gray,w,h,newPath);
			return 3;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(ff!=null && ff.exists()){
				ff.deleteOnExit();
			}
			if(bufferedImage!=null){
				bufferedImage.flush();
			}
			if(binaryBufferedImage!=null){
				binaryBufferedImage.flush();
			}
		}
		return 0;
	}
	
	public  int dealBlack2(String oldPath, String newPath, String imgType) {
		File ff = null;
		try {
			ff = new File(oldPath);
			BufferedImage bufferedImage = ImageIOHelper.getImage(ff);
			
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();
			BufferedImage binaryBufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			// 灰度化
			int[][] gray = new int[w][h];
			
			
			dealBlack2(bufferedImage,binaryBufferedImage,gray,w,h,newPath);
			return 3;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(ff!=null){
				ff.delete();
			}
		}
		return 0;
	}
	
	 private static void dealBlack(BufferedImage bufferedImage,
				BufferedImage binaryBufferedImage, int[][] gray, int w, int h,
				String newFile) throws IOException {
			// TODO Auto-generated method stub
			 for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						int argb = bufferedImage.getRGB(x, y);
						int r = (argb >> 16) & 0xFF;
						int g = (argb >> 8) & 0xFF;
						int b = (argb >> 0) & 0xFF;
						if((x<=5 && y<=7) || (x<=5 && y>=27) || (x>=69 && y<=7) || (x>=69 && y>=27)){
							gray[x][y]= -1;
							bufferedImage.setRGB(x, y, -1);
							binaryBufferedImage.setRGB(x, y, gray[x][y]);
							continue;
						}
						if(r>=90 && g>=90 && b>=90){
							gray[x][y]= -1;
							//bufferedImage.setRGB(x, y, -1);
							
						}else {
							
							gray[x][y]= -16777216;

							if ((y - 1) >= 0 && (y + 1) < h) {
								int argb1 = bufferedImage.getRGB(x, y - 1);
								int argb2 = bufferedImage.getRGB(x, y + 1);
								int r1 = (argb1 >> 16) & 0xFF;
								int g1 = (argb1 >> 8) & 0xFF;
								int b1 = (argb1 >> 0) & 0xFF;

								int r2 = (argb2 >> 16) & 0xFF;
								int g2 = (argb2 >> 8) & 0xFF;
								int b2 = (argb2 >> 0) & 0xFF;
								if ((r1>90 && g1>90 && b1>90) || (r2>90 && g2>90 && b2>90)) {
									
									gray[x][y] = -1;

								}
							}
							 
							if ((x - 1) >= 0 && (x + 1) < w) {
								int argb3 = bufferedImage.getRGB(x - 1, y);
								int argb4 = bufferedImage.getRGB(x + 1, y);
								int r3 = (argb3 >> 16) & 0xFF;
								int g3 = (argb3 >> 8) & 0xFF;
								int b3 = (argb3 >> 0) & 0xFF;

								int r4 = (argb4 >> 16) & 0xFF;
								int g4 = (argb4 >> 8) & 0xFF;
								int b4 = (argb4 >> 0) & 0xFF;
								if ((r3 <90 && b3 < 90 && g3 < 90)
										&& (r4 <90 && b4 < 90 && g4 < 90)) {
									
									gray[x][y] = -16777216;
									continue;
								}
							}
							if ((y - 1) >= 0 && (y + 1) < h && (x - 1) >= 0 && (x + 1) < w) {
								int argb1 = bufferedImage.getRGB(x-1, y - 1);
								int argb2 = bufferedImage.getRGB(x+1, y + 1);
								int r1 = (argb1 >> 16) & 0xFF;
								int g1 = (argb1 >> 8) & 0xFF;
								int b1 = (argb1 >> 0) & 0xFF;

								int r2 = (argb2 >> 16) & 0xFF;
								int g2 = (argb2 >> 8) & 0xFF;
								int b2 = (argb2 >> 0) & 0xFF;
								if (r1<90 && g1<90 && b1<90 && r2<90 && g2<90 && b2<90) {
									
									gray[x][y] = -16777216;;
									continue;
								}
								
								int argb3 = bufferedImage.getRGB(x-1, y + 1);
								int argb4 = bufferedImage.getRGB(x+1, y - 1);
								int r3 = (argb3 >> 16) & 0xFF;
								int g3 = (argb3 >> 8) & 0xFF;
								int b3 = (argb3 >> 0) & 0xFF;

								int r4 = (argb4 >> 16) & 0xFF;
								int g4 = (argb4 >> 8) & 0xFF;
								int b4 = (argb4 >> 0) & 0xFF;
								if (r3<90 && g3<90 && b3<90 && r4<90 && g4<90 && b4<90) {
									
									gray[x][y] = -16777216;;
									continue;
								}
								
							}
						}
						binaryBufferedImage.setRGB(x, y, gray[x][y]);
					}
				}
				ImageIO.write(binaryBufferedImage, "jpg", new File(
						newFile));
		}
	
	private static void dealRed(BufferedImage bufferedImage,
			BufferedImage binaryBufferedImage, int[][] gray, int w, int h,
			String newFile) throws IOException {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int argb = bufferedImage.getRGB(x, y);
				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb >> 0) & 0xFF;
				
				if (r > 90 && g > 90 && b > 90) {
					gray[x][y] = -1;
					bufferedImage.setRGB(x, y, -1);
				} else if (r > 90 && g < 90 && b < 90) {
					gray[x][y] = -3403476;
					bufferedImage.setRGB(x, y, -3403476);
				} else {
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
							if ((r1 > 90 && b1 < 90 && g1 < 90)
									&& (r2 > 90 && b2 < 90 && g2 < 90)) {
							 
								gray[x][y] = -3403476;
								bufferedImage.setRGB(x, y, -3403476);
							}
						}
						if((y-2)>=0 && (y+2)<h){
							int argb1 = bufferedImage.getRGB(x, y-2);
							int argb2 = bufferedImage.getRGB(x, y+2);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;
							
							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if ((r1 > 90 && b1 < 90 && g1 < 90)
									&& (r2 > 90 && b2 < 90 && g2 < 90)) {
								 
								gray[x][y] = -3403476;
								 
							}
						}
					}
				binaryBufferedImage.setRGB(x, y, gray[x][y]);
			}
		}
		ImageIO.write(binaryBufferedImage, "jpg", new File(
					newFile));
	}
	private static void dealBlue(BufferedImage bufferedImage,
			BufferedImage binaryBufferedImage, int[][] gray, int w, int h,String newFile) throws IOException {
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int argb = bufferedImage.getRGB(x, y);
				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb >> 0) & 0xFF;
				
				if (r > 90 && g > 90 && b > 90) {
					gray[x][y] = -1;
					bufferedImage.setRGB(x, y, -1);
				} else if (b > 130 && g < 90 && r < 90) {
					gray[x][y] = -16777005;
					bufferedImage.setRGB(x, y, -16777005);
				} else {
					gray[x][y] = -1;

					if ((r == 0 || g == 0) && b > 25) {
						gray[x][y] = -16777005;
						bufferedImage.setRGB(x, y, -16777005);
					} else {
						if ((y - 1) >= 0 && (y + 1) < h) {
							int argb1 = bufferedImage.getRGB(x, y - 1);
							int argb2 = bufferedImage.getRGB(x, y + 1);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;

							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if ((b1 > 90 && r1 < 90 && g1 < 90)
									&& (b2 > 90 && r2 < 90 && g2 < 90)) {
								 
								gray[x][y] = -16777005;
								bufferedImage.setRGB(x, y, -16777005);
							}
						}
						if((y-2)>=0 && (y+2)<h){
							int argb1 = bufferedImage.getRGB(x, y-2);
							int argb2 = bufferedImage.getRGB(x, y+2);
							int r1 = (argb1 >> 16) & 0xFF;
							int g1 = (argb1 >> 8) & 0xFF;
							int b1 = (argb1 >> 0) & 0xFF;
							
							int r2 = (argb2 >> 16) & 0xFF;
							int g2 = (argb2 >> 8) & 0xFF;
							int b2 = (argb2 >> 0) & 0xFF;
							if ((b1 > 90 && r1 < 90 && g1 < 90)
									&& (b2 > 90 && r2 < 90 && g2 < 90)) {
								 
								gray[x][y] = -16777005;
							}
						}
					}
				}
				binaryBufferedImage.setRGB(x, y, gray[x][y]);
			}
		}
		ImageIO.write(binaryBufferedImage, "jpg", new File(
					newFile));
		
		
	}
	 private static void dealBlack2(BufferedImage bufferedImage,
				BufferedImage binaryBufferedImage, int[][] gray, int w, int h,
				String newFile) throws IOException {
			// TODO Auto-generated method stub
			 for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						int argb = bufferedImage.getRGB(x, y);
						int r = (argb >> 16) & 0xFF;
						int g = (argb >> 8) & 0xFF;
						int b = (argb >> 0) & 0xFF;
						if((x<=5 && y<=7) || (x<=5 && y>=27) || (x>=69 && y<=7) || (x>=69 && y>=27)){
							gray[x][y]= -1;
							bufferedImage.setRGB(x, y, -1);
							binaryBufferedImage.setRGB(x, y, gray[x][y]);
							continue;
						}
						if(r>=90 && g>=90 && b>=90){
							gray[x][y]= -1;
							bufferedImage.setRGB(x, y, -1);
						}else {
							
							gray[x][y]= -16777216;

							if ((y - 1) >= 0 && (y + 1) < h) {
								int argb1 = bufferedImage.getRGB(x, y - 1);
								int argb2 = bufferedImage.getRGB(x, y + 1);
								int r1 = (argb1 >> 16) & 0xFF;
								int g1 = (argb1 >> 8) & 0xFF;
								int b1 = (argb1 >> 0) & 0xFF;

								int r2 = (argb2 >> 16) & 0xFF;
								int g2 = (argb2 >> 8) & 0xFF;
								int b2 = (argb2 >> 0) & 0xFF;
								if (r1>90 && g1>90 && b1>90 && r2>90 && g2>90 && b2>90) {
									bufferedImage.setRGB(x, y, -1);
									gray[x][y] = -1;

								}
							}
							if ((y - 2) >= 0 && (y + 2) < h) {
								int argb1 = bufferedImage.getRGB(x, y - 2);
								int argb2 = bufferedImage.getRGB(x, y + 2);
								int r1 = (argb1 >> 16) & 0xFF;
								int g1 = (argb1 >> 8) & 0xFF;
								int b1 = (argb1 >> 0) & 0xFF;

								int r2 = (argb2 >> 16) & 0xFF;
								int g2 = (argb2 >> 8) & 0xFF;
								int b2 = (argb2 >> 0) & 0xFF;
								if (r1>90 && g1>90 && b1>90 && r2>90 && g2>90 && b2>90) {
									gray[x][y] = -1;
									if ((x - 2) >= 0 && (x + 2) < w &&  (y-2)>=0 && (y+2)<h) {
										int argb3 = bufferedImage.getRGB(x - 2, y-2);
										int argb4 = bufferedImage.getRGB(x + 2, y+2);
										int r3 = (argb3 >> 16) & 0xFF;
										int g3 = (argb3 >> 8) & 0xFF;
										int b3 = (argb3 >> 0) & 0xFF;

										int r4 = (argb4 >> 16) & 0xFF;
										int g4 = (argb4 >> 8) & 0xFF;
										int b4 = (argb4 >> 0) & 0xFF;
										if ((r3 <90 && b3 < 90 && g3 < 90)
												&& (r4 <90 && b4 < 90 && g4 < 90)) {
											
											gray[x][y] = -16777216;
										}
										
										int argb5 = bufferedImage.getRGB(x - 2, y+2);
										int argb6 = bufferedImage.getRGB(x + 2, y-2);
										int r5 = (argb5 >> 16) & 0xFF;
										int g5 = (argb5 >> 8) & 0xFF;
										int b5 = (argb5 >> 0) & 0xFF;

										int r6 = (argb6 >> 16) & 0xFF;
										int g6 = (argb6 >> 8) & 0xFF;
										int b6 = (argb6 >> 0) & 0xFF;
										if ((r5 <90 && b5 < 90 && g5 < 90)
												&& (r6 <90 && b6 < 90 && g6 < 90)) {
											
											gray[x][y] = -16777216;
										}
									}
								}
							}
						}
						binaryBufferedImage.setRGB(x, y, gray[x][y]);
					}
				}
				ImageIO.write(binaryBufferedImage, "jpg", new File(
						newFile));
		}
	 
	
}
