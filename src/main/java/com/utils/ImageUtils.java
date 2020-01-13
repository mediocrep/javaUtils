package com.apiTest;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * 大小图合并工具类
 * @author Administrator
 */
public class ImageUtilTest {
	private static final Logger log4j = Logger.getLogger(ImageUtilTest.class);
	
	private final int WIDTH =220;
	
	private final int HEIGHT = 165;
	
	BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	Graphics g = image.getGraphics();
	
	//缩图
	public void zoom(String oldFile,String zoomFile) throws IOException{
		
		BufferedImage srcImage = ImageIO.read(new File(oldFile));
		
		g.drawImage(srcImage,0,0,WIDTH,HEIGHT,null);
		
		ImageIO.write(image,"jpeg", new File(zoomFile));
	}
	
	//合并图片
	public void HBF(String oldFile,String zoomFile,String nowFile) throws Exception{
		FileInputStream fileOneDa = new FileInputStream(oldFile);//原图
		FileInputStream fileTwoXiao= new FileInputStream(zoomFile);//缩图
		FileOutputStream xinFile = new FileOutputStream(nowFile);//合并图
		
		byte[] bytes = new byte[1024];
		int i;
		while((i = fileOneDa.read(bytes)) != -1){
			xinFile.write(bytes,0,i);
		}
		 
		byte[] bytess = new byte[1024];
		int j;
		while((j = fileTwoXiao.read(bytess)) != -1){
		    xinFile.write(bytess,0,j);
		}
		
		fileOneDa.close();
		fileTwoXiao.close();
		xinFile.close();
	}
	
	public static void main(String[] args) {
		ImageUtilTest img = new ImageUtilTest();
		try {
			long b = new Date().getTime();
			String oldFile = "D:\\test\\pics\\shamo.jpg";
			String zoomFile ="D:\\test\\pics\\shamo_z.jpg";
			String nowFile = "D:\\test\\pics\\shamo_n.jpg";
			img.zoom(oldFile, zoomFile);
			img.HBF(oldFile, zoomFile, nowFile);
			log4j.info("用时："+(new Date().getTime()-b)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("pneumonoultramicroscopicsilicovolcanoconiosis".length());
		
	}
}
