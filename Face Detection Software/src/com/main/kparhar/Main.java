package com.main.kparhar;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.oracle.tools.packager.IOUtils;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		VideoSource input = new VideoSource(0);
		FacialDetector detector = new FacialDetector("C:/OpenCV/opencv/build/etc/haarcascades/haarcascade_frontalface_default.xml");
		Display display = new Display("Facial detection? More like racial detection...");
		
		while(true) {
			if(input.scanFrame()) {
				Mat frame = input.getFrame().clone();
				detector.detectFaces(frame);
				for(int i = 0; i < detector.getFacesMats().toArray().length; i++) {
					int X = detector.getFacesMats().toArray()[i].x;
					int Y = detector.getFacesMats().toArray()[i].y;
					int W = detector.getFacesMats().toArray()[i].width;
					int H = detector.getFacesMats().toArray()[i].height;
					Imgproc.rectangle(frame, new Point(X, Y), new Point(X+W, Y+H), new Scalar(0, 255, 0), 3);
				}
				display.renderFrame(frame);
				
				if(detector.getFramesUnchanged() > 48 && detector.getNumFaces() == 1) {
					String fileName = "" + System.currentTimeMillis();
					File outputImage = Output.saveImage(input.getFrame(), "./res/temp/", fileName, "png");
					String base64Output = "Image encoding failed";
					try {
						base64Output = new String(Base64.getEncoder().encode(IOUtils.readFully(outputImage)));
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(base64Output);
					System.exit(0);
				}
			}
		}
	}

}
