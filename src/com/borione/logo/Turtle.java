package com.borione.logo;

import java.awt.Color;
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
			g.drawPolygon(new int[] {0, -5, 0, 5}, new int[] {7, -5, 0, -5}, 4);
	}
	
	public void move(int y) {
		if(writing)
			g.drawLine(0, 0, 0, y);
		g.translate(0, y);
	}
	
	public void rotate(int degrees) {
		g.rotate(Math.toRadians(degrees));
	}
	
	public void returnHome() {
		// XXX: figure it out
	}
	
	public void clean() {
		// XXX: figure it out
	}
	
	public void setWriting(boolean w) {
		this.writing = w;
	}
	
	public void setShowHead(boolean s) {
		this.showHead = s;
	}

	public boolean isShowHead() {
		return this.showHead;
	}
	
	public void setTrailColor(Color c) {
		this.g.setColor(c);
	}

}
