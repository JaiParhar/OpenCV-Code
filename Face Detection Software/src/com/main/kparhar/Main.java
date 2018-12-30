package com.main.kparhar;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.oracle.tools.packager.IOUtils;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//FDFromImage("C:/Users/Kush/Desktop/Facial Dectection Testing Images/.jpg", "attractive");
		FDFromVideo("expression");
	}
	
	public static void FDFromVideo(String fD) {
		VideoSource input = new VideoSource(0);
		FacialDetector detector = new FacialDetector("C:/OpenCV/opencv/build/etc/haarcascades/haarcascade_frontalface_default.xml");
		Display display = new Display("Facial detection? More like racial detection...");
		
		while(true) {
			if(input.scanFrame()) {
				Mat frame = input.getFrame().clone();
				detector.detectFaces(frame, 1.1, 12);
				for(int i = 0; i < detector.getFacesMats().toArray().length; i++) {
					int X = detector.getFacesMats().toArray()[i].x;
					int Y = detector.getFacesMats().toArray()[i].y;
					int W = detector.getFacesMats().toArray()[i].width;
					int H = detector.getFacesMats().toArray()[i].height;
					Imgproc.rectangle(frame, new Point(X, Y), new Point(X+W, Y+H), new Scalar(0, 255, 0), 3);
				}
				Core.flip(frame, frame, 1);
				display.renderFrame(frame);
				if(detector.getFramesUnchanged() > 24 && detector.getNumFaces() == 1) {
					String fileName = "" + System.currentTimeMillis();
					File outputImage = Output.saveImage(input.getFrame(), "./res/temp/", fileName, "png");
					String base64Output = "Image encoding failed";
					try {
						base64Output = new String(Base64.getEncoder().encode(IOUtils.readFully(outputImage)));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Map<String, String> testParams = new LinkedHashMap<String, String>();
					testParams.put("api_key", "d45fd466-51e2-4701-8da8-04351c872236");
					testParams.put("file_base64", base64Output);
					testParams.put("detection_flags", "bestface, classification");
					
					String responseString = "";
					try {
						responseString = Networker.sendPOST("http://www.betafaceapi.com/api/v2/media", testParams);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if(responseString.length() == 0) {
						System.out.println("Could not get facial data");
						System.exit(-1);
					}
					
					String findData = fD;
					try {
						String dataSearchString = "\"name\": \"" + findData + "\"";
						int cutTo = responseString.indexOf(dataSearchString);
						responseString = responseString.substring(cutTo);
						
						int cutFrom = responseString.indexOf("}");
						responseString = responseString.substring(0, cutFrom);
						
						responseString = responseString.replaceAll(" ", "");
						responseString = responseString.replaceAll("\"", "");
						responseString = responseString.replaceAll(",", "");
					} catch (StringIndexOutOfBoundsException e) { e.printStackTrace(); }
					System.out.println(responseString);
					
					System.exit(0);
				}
			}
		}
	}
	
	public static void FDFromImage(String filePath, String fD) {
		Mat image = Imgcodecs.imread(filePath);
		FacialDetector detector = new FacialDetector("C:/OpenCV/opencv/build/etc/haarcascades/haarcascade_frontalface_default.xml");
		Display display = new Display("Facial detection? More like racial detection...");
		
		while(true) {
			Mat frame = image.clone();
			detector.detectFaces(frame, 1.1, 12);
			for(int i = 0; i < detector.getFacesMats().toArray().length; i++) {
				int X = detector.getFacesMats().toArray()[i].x;
				int Y = detector.getFacesMats().toArray()[i].y;
				int W = detector.getFacesMats().toArray()[i].width;
				int H = detector.getFacesMats().toArray()[i].height;
				Imgproc.rectangle(frame, new Point(X, Y), new Point(X+W, Y+H), new Scalar(0, 255, 0), 3);
			}
			Core.flip(frame, frame, 1);
			display.renderFrame(frame);
			if(detector.getFramesUnchanged() > 24) {
				System.out.println("Image taken");
				String fileName = "" + System.currentTimeMillis();
				File outputImage = Output.saveImage(image, "./res/temp/", fileName, "png");
				String base64Output = "Image encoding failed";
				try {
					base64Output = new String(Base64.getEncoder().encode(IOUtils.readFully(outputImage)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Map<String, String> testParams = new LinkedHashMap<String, String>();
				testParams.put("api_key", "d45fd466-51e2-4701-8da8-04351c872236");
				testParams.put("file_base64", base64Output);
				testParams.put("detection_flags", "bestface, classification");
				
				String responseString = "";
				try {
					responseString = Networker.sendPOST("http://www.betafaceapi.com/api/v2/media", testParams);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(responseString.length() == 0) {
					System.out.println("Could not get facial data");
					System.exit(-1);
				}
				
				String findData = fD;
				try {
					String dataSearchString = "\"name\": \"" + findData + "\"";
					int cutTo = responseString.indexOf(dataSearchString);
					responseString = responseString.substring(cutTo);
					
					int cutFrom = responseString.indexOf("}");
					responseString = responseString.substring(0, cutFrom);
					
					responseString = responseString.replaceAll(" ", "");
					responseString = responseString.replaceAll("\"", "");
					responseString = responseString.replaceAll(",", "");
				} catch (StringIndexOutOfBoundsException e) { e.printStackTrace(); }
				System.out.println(responseString);
				
				System.exit(0);
			}
		}
	}
	
}
