package com.main.kparhar;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class VideoSource {

	VideoCapture vSource;
	Mat currFrame;
	
	public VideoSource(int source) {
		vSource = new VideoCapture(source);
		if(init()) { System.out.println("Video source initialized successfully"); }
		else { System.out.println("Video source could not be initialized successfully"); }
	}
	
	public VideoSource(String source) {
		vSource = new VideoCapture(source);
		if(init()) { System.out.println("Video source initialized successfully"); }
		else { System.out.println("Video source could not be initialized successfully"); }
	}
	
	private boolean init() {
		try {
			currFrame = new Mat();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	

	public boolean scanFrame() {
		return vSource.read(currFrame);
	}
	
	public Mat getFrame() {
		return currFrame;
	}
	
}
