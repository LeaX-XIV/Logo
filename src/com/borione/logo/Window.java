package com.borione.logo;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Window extends JFrame {
	
	private static final BorderLayout DEFAULT_LAYOUT = new BorderLayout();
	
	private JScrollPane scroll;
	private JTextArea commands;
	private JPanel canvasPanel;
	private Canvas canvas;
	
	public Window() {
		super("LOGO");
		super.setBounds(100, 100, 500, 500);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 2, 20, 20));
		
		scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		canvasPanel = new JPanel(DEFAULT_LAYOUT) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				// TODO: add real time parsing from text in text area
				g.drawLine(0, 0, 100, 100);
			}
		};
		getContentPane().add(scroll);
		getContentPane().add(canvasPanel);
		
		commands = new JTextArea();
		commands.setTabSize(4);
		commands.setWrapStyleWord(true);
		commands.setLineWrap(true);
		scroll.setViewportView(commands);		
		
//		super.pack();
		super.setVisible(true);		
	}
	
	public static void main(String[] args) {
		Window w = new Window();
	}

}
