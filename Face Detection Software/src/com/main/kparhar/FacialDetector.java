package com.main.kparhar;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class FacialDetector {

	CascadeClassifier detector;
	
	int numFaces;
	int lastNumFaces;
	int framesUnchanged;
	
	MatOfRect faceDetections;
	
	public FacialDetector(String cascadeClassifier) {
		detector = new CascadeClassifier(cascadeClassifier);
		faceDetections = new MatOfRect();
		lastNumFaces = 0;
		framesUnchanged = 0;
	}
	
	public void detectFaces(Mat frame, double scaleTestSize, int minNeighbours) {
		detector.detectMultiScale(frame, faceDetections, scaleTestSize, minNeighbours);
		numFaces = faceDetections.toArray().length;
		
		if(numFaces == lastNumFaces) {
			framesUnchanged++;
		} else {
			lastNumFaces = numFaces;
			framesUnchanged = 0;
		}
		
	}
	
	public MatOfRect getFacesMats() {
		return faceDetections;
	}
	
	public int getNumFaces() {
		return numFaces;
	}
	
	public int getFramesUnchanged() {
		return framesUnchanged;
	}
	
}
