package com.main.kparhar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;

@SuppressWarnings("serial")
public class Display extends JFrame{
	
	JLabel videoDisplay;
	
	public Display(String title) {
		super(title);
		videoDisplay = new JLabel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(videoDisplay);
	    setVisible(true);
	}
	
	public void renderFrame(Mat frame) {
		setSize(Output.imageFromMatrix(frame).getWidth(null), Output.imageFromMatrix(frame).getHeight(null));
        ImageIcon image = new ImageIcon(Output.imageFromMatrix(frame));
        videoDisplay.setIcon(image);
        videoDisplay.repaint();
	}
}
