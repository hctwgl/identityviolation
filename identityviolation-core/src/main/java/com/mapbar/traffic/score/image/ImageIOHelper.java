package com.mapbar.traffic.score.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JOptionPane;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class ImageIOHelper {

	public ImageIOHelper() {
	}

	public static File changeFileBmpToTiff(File imageFile, String newPath) {
		File tempFile = new File(newPath);
		try {
			System.out.println(imageFile.getPath());
			RenderedOp src2 = JAI.create("fileload", imageFile.getPath());
			OutputStream os2 = new FileOutputStream(newPath);
			TIFFEncodeParam param2 = new TIFFEncodeParam();
			// 指定格式类型，jpg 属于 JPEG 类型
			ImageEncoder enc2 = ImageCodec.createImageEncoder("TIFF", os2, param2);
			enc2.encode(src2);
			os2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempFile;
	}

	public static File createImage(File imageFile, String imageFormat, String newPath) {
		File tempFile = new File(newPath);
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
			ImageReader reader = readers.next();

			ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
			reader.setInput(iis);
			// Read the stream metadata
			IIOMetadata streamMetadata = reader.getStreamMetadata();

			// Set up the writeParam
			JPEGImageWriteParam jpgWriteParam = new JPEGImageWriteParam(Locale.US);
			// TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(
			// Locale.US);
			// tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);

			// Get tif writer and set output to file
			// jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("JPG");
			ImageWriter writer = writers.next();

			BufferedImage bi = reader.read(0);
			IIOImage image = new IIOImage(bi, null, reader.getImageMetadata(0));
			// tempFile = tempImageFile(imageFile);
			ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile);
			writer.setOutput(ios);
			writer.write(streamMetadata, image, jpgWriteParam);
			ios.close();

			writer.dispose();
			reader.dispose();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return tempFile;
	}

	public static File createImage(File imageFile, String imageFormat) {
		File tempFile = null;
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
			ImageReader reader = readers.next();

			ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
			reader.setInput(iis);
			// Read the stream metadata
			IIOMetadata streamMetadata = reader.getStreamMetadata();

			// Set up the writeParam
			TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.US);
			tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);

			// Get tif writer and set output to file
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
			ImageWriter writer = writers.next();

			BufferedImage bi = reader.read(0);
			IIOImage image = new IIOImage(bi, null, reader.getImageMetadata(0));
			tempFile = tempImageFile(imageFile);
			ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile);
			writer.setOutput(ios);
			writer.write(streamMetadata, image, tiffWriteParam);
			ios.close();

			writer.dispose();
			reader.dispose();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return tempFile;
	}

	public static File createImage(BufferedImage bi) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("tempImageFile", ".tif");
			tempFile.deleteOnExit();
			TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.US);
			tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);

			// Get tif writer and set output to file
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
			ImageWriter writer = writers.next();

			IIOImage image = new IIOImage(bi, null, null);
			tempFile = tempImageFile(tempFile);
			ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile);
			writer.setOutput(ios);
			writer.write(null, image, tiffWriteParam);
			ios.close();
			writer.dispose();
		} catch (Exception exc) {

		}
		return tempFile;
	}

	public static File tempImageFile(File imageFile) {
		String path = imageFile.getPath();
		StringBuffer strB = new StringBuffer(path);
		strB.insert(path.lastIndexOf('.'), 0);
		return new File(strB.toString().replaceFirst("(?<=\\.)(\\w+)$", "tif"));
	}

	public static BufferedImage getImage(File imageFile) {
		BufferedImage al = null;
		try {
			String imageFileName = imageFile.getName();
			String imageFormat = imageFileName.substring(imageFileName.lastIndexOf('.') + 1);
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
			ImageReader reader = readers.next();

			if (reader == null) {
				JOptionPane.showConfirmDialog(null, "Need to install JAI Image I/O package. https://jai-imageio.dev.java.net");
				return null;
			}

			ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
			reader.setInput(iis);

			al = reader.read(0);

			reader.dispose();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return al;
	}

	public static BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),

		image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		return bufferedImage;
	}

	public static BufferedImage imageProducerToBufferedImage(ImageProducer imageProducer) {
		return imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(imageProducer));
	}

	public static byte[] image_byte_data(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}
}
