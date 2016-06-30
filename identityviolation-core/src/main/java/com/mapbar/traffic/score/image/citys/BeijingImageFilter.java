package com.mapbar.traffic.score.image.citys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mapbar.traffic.score.image.ImageIOHelper;
 

public class BeijingImageFilter {
	
	
	public   boolean dealImage(String oldPath,String forder,String imgType,String uuid){
		File ff = new File(oldPath);
		String newFile1 = forder+uuid+"-1.jpg";
		String newFile2= forder+uuid+"-2.jpg";
		File f1 = new File(newFile1);
		File f2=  new File(newFile2);
		try {
			 
			BufferedImage bufferedImage = ImageIOHelper.getImage(ff);
			if(bufferedImage==null){
				return false;
			}
			int h = bufferedImage.getHeight();
			int w = bufferedImage.getWidth();
			
			BufferedImage image2 = new BufferedImage(w-205, h,
					BufferedImage.TYPE_3BYTE_BGR);

			
			int[][] gray = new int[w][h];
			
	 
			List<Integer> intlist = new ArrayList<Integer>();
			for (int y = 0; y < h; y++) {
				int a1=0;
				int a2=0;
				for (int x = 0; x < w; x++) {
					if(x<205){
                        int argb = bufferedImage.getRGB(x, y);
					    
					    int r = (argb >> 16) & 0xFF;
					    int g = (argb >> 8) & 0xFF;
					    int b = (argb >> 0) & 0xFF;
					   
						if(r<220 && g<220 && b<220 && a1==0){
							a1=x;
						}
						if(r<220 && g<220 && b<220 && a1>0){
							a2=x;
						}
					}
				}
				int dd = a2-a1;
				if(dd > 1 && dd<80){
					 
					intlist.add(y);
				}
			}
			String max = "";
			StringBuffer bf = new StringBuffer();
			for(int i=0;i<intlist.size();i++){
				int a=intlist.get(i);
                if(i==(intlist.size()-1)){
                	bf.append(a).append(";");
                	if(bf.toString().split(";").length>max.split(";").length){
						max = bf.toString();
					}
                	 
				}
				if((i+1)<intlist.size() && (a+1)==intlist.get(i+1)){
					bf.append(a).append(";");
				}
				if((i+1)<intlist.size() && (a+1)!=intlist.get(i+1)){
					bf.append(a);
					 
					if(bf.toString().split(";").length>max.split(";").length){
						max = bf.toString();
					}
					bf = new StringBuffer();
					 
				}
				
			}
			
			 
			 
			String str[] = max.split(";");
			int miny = new Integer(str[0]);
			int maxy= new Integer(str[str.length-1]);
			 
			int minx = 205;
			int maxx = 0;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					
					int argb = bufferedImage.getRGB(x, y);
					int r = (argb >> 16) & 0xFF;
					int g = (argb >> 8) & 0xFF;
					int b = (argb >> 0) & 0xFF;
					
					if(x<205){
						if(y>=miny && y<=maxy && x<205){
							if(r<220 && g<220 && b<220){
								if(minx>x){
									minx=x;
								}
								if(maxx<x){
									maxx=x;
								}
							}
						}else{
							argb = -1;
						}
						gray[x][y]=argb;
						//image1.setRGB(x, y, argb);
					}else{
						image2.setRGB(x-205, y, argb);
					}
				}
			}
			 
			BufferedImage image1 = new BufferedImage((maxx-minx+1), (maxy-miny+1),
					BufferedImage.TYPE_3BYTE_BGR);
			for(int i=0;i<(maxx-minx+1);i++){
				for(int j=0;j<(maxy-miny+1);j++){
					image1.setRGB(i, j, gray[i+minx][j+miny]);
				}
			}
			ImageIO.write(image1, "jpg",f1);
			ImageIO.write(image2, "jpg",f2);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return true;
	}

}
