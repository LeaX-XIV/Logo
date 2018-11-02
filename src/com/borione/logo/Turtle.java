package com.borione.logo;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Turtle {
	
	private Graphics2D g;

	// Not really sure what to use these for
	private int w;
	private int h;
	
	private boolean writing;
	private boolean showHead;
	
	public Turtle(Graphics g, int w, int h) {
		this.g = (Graphics2D) g;
		this.w = w;
		this.h = h;
		g.translate(w / 2, h / 2);
		this.g.rotate(Math.PI);
		
		this.writing = true;
		this.showHead = true;
	}
	
	public void drawHead() {
		if(showHead)
			g.drawPolygon(new int[] {0, -6, 6}, new int[] {3, -3, -3}, 3);
	}
	
	public void move(int y) {
		if(writing)
			g.drawLine(0, 0, 0, y);
		g.translate(0, y);
	}
	
	// right +
	// left -
	public void rotate(int degrees) {
		g.rotate(Math.toRadians(degrees));
	}
	
	public void returnHome() {
		// XXX: figure it out
	}
	
	public void setWriting(boolean w) {
		this.writing = w;
	}
	
	public void setShowHead(boolean s) {
		this.showHead = s;
	}

}
