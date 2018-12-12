package com.main.kparhar;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

public class Output {
	
	public static File saveImage(Mat img, String fileLoc, String fileName, String fileFormat) {
		return saveImage(imageFromMatrix(img), fileLoc, fileName, fileFormat);
	}
	
	public static File saveImage(BufferedImage img, String fileLoc, String fileName, String fileFormat) {
		File outputFile = new File(fileLoc+fileName+"."+fileFormat); 
		try {
			outputFile.createNewFile();
			ImageIO.write(img, fileFormat, outputFile);
			return outputFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage imageFromMatrix(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte[bufferSize];
		m.get(0,0,b);
		BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);  
		return image;
	}
	
}
