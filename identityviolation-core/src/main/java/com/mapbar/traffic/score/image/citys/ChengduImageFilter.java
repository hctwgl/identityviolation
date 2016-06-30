package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;

public class ChengduImageFilter extends CityImageFilter{
	 public  void erzhiA(int[][] gray, int w, int h,int threshold,BufferedImage binaryBufferedImage){
		
		 for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					 
					if (gray[x][y] > threshold) {
						gray[x][y] |= 0x00FFFF; // 白色
					} else {
						gray[x][y] |= 0x00FFFF;  // 白色
//						if ((x + 3) < w && (x - 3) >= 0) {
//							if (gray[x + 3][y] <= threshold
//									|| gray[x - 3][y] <= threshold) {
//								//System.out.println("a x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000; //黑色
//							}
//						}
						if ((y + 1) < h && (y - 1) >= 0) {
							if (gray[x][y + 1] <= threshold || gray[x][y - 1] <= threshold) {
							
								gray[x][y] = 0xFF0000;
								
							}
						}
//						if((y + 1) < h && (x + 1) < w ){
//							if (gray[x+1][y + 1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//								
//							}
//						}
//						if((y - 1) >= 0 && (x - 1) >= 0 ){
//							if (gray[x-1][y-1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//							}
//						}
//						if((y + 1) < h && (x - 1) >=0 ){
//							if (gray[x-1][y + 1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//								
//							}
//						}
//						if((y - 1) >= 0 && (x + 1) < w ){
//							if (gray[x+1][y-1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//							}
//						}
						
//						if((x + 2) < w && (y + 2) < h && (y - 2) >= 0 && (x - 2) >= 0 ){
//							if (gray[x+2][y]> threshold && gray[x+2][y+2]> threshold &&  gray[x+2][y-2]> threshold && gray[x][y+2] > threshold && gray[x][y-2]  > threshold 
//									&& gray[x-2][y+2] > threshold  && gray[x-2][y-2] > threshold && gray[x-2][y]  > threshold) {
//								System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x00FFFF;
//							}
//						}
						
					}

					
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
		 }
	 }
	 public  void erzhiB(int[][] gray, int w, int h,int threshold,BufferedImage binaryBufferedImage){
		
		 for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					//System.out.println("x=="+x + "  y=="+y);
					if (gray[x][y] > threshold) {
						gray[x][y] |= 0x00FFFF; // 白色
					} else {
						gray[x][y] |= 0x00FFFF;  // 白色
//						if ((x + 3) < w && (x - 3) >= 0) {
//							if (gray[x + 3][y] <= threshold
//									|| gray[x - 3][y] <= threshold) {
//								//System.out.println("a x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000; //黑色
//							}
//						}
						if ((y + 1) < h && (y - 1) >= 0) {
							if (gray[x][y + 1] <= threshold || gray[x][y - 1] <= threshold) {
								
								gray[x][y] = 0xFF0000;
								
							}
						}
						if((y + 1) < h && (x + 1) < w ){
							if (gray[x+1][y + 1] <= threshold) {
								//System.out.println("b x==" + x + "  y==" + y);
								gray[x][y] = 0x000000;
								
							}
						}
					if ((x + 3) < w && (x - 3) >= 0) {
						if (gray[x + 3][y] <= threshold
								|| gray[x - 3][y] <= threshold) {
							// System.out.println("a x==" + x + "  y==" + y);
							gray[x][y] = 0x000000; // 黑色
						}
					}
						
//						if((y + 1) < h && (x + 1) < w ){
//							if (gray[x+1][y + 1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//								
//							}
//						}
//						if((y - 1) >= 0 && (x - 1) >= 0 ){
//							if (gray[x-1][y-1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//							}
//						}
//						if((y + 1) < h && (x - 1) >=0 ){
//							if (gray[x-1][y + 1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//								
//							}
//						}
//						if((y - 1) >= 0 && (x + 1) < w ){
//							if (gray[x+1][y-1] <= threshold) {
//								//System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x000000;
//							}
//						}
						
//						if((x + 2) < w && (y + 2) < h && (y - 2) >= 0 && (x - 2) >= 0 ){
//							if (gray[x+2][y]> threshold && gray[x+2][y+2]> threshold &&  gray[x+2][y-2]> threshold && gray[x][y+2] > threshold && gray[x][y-2]  > threshold 
//									&& gray[x-2][y+2] > threshold  && gray[x-2][y-2] > threshold && gray[x-2][y]  > threshold) {
//								System.out.println("b x==" + x + "  y==" + y);
//								gray[x][y] = 0x00FFFF;
//							}
//						}
						
					}

					
					binaryBufferedImage.setRGB(x, y, gray[x][y]);
				}
		 }
	 }
}
