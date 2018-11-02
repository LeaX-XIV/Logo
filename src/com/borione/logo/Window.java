package com.borione.logo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Window extends JFrame {
	
	private static final BorderLayout DEFAULT_LAYOUT = new BorderLayout();
	
	private JScrollPane scroll;
	private JTextArea commands;
	private JPanel canvasPanel;
	
	Turtle t;
	
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
				
				t = new Turtle(g, canvasPanel.getWidth(), canvasPanel.getHeight());				
				String text = commands.getText();
	        	Parser.parse(t, text);

			}
		};
		super.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		        canvasPanel.repaint();
		    }
		});
		getContentPane().add(scroll);
		getContentPane().add(canvasPanel);
		
		commands = new JTextArea();
		commands.setTabSize(4);
		commands.setWrapStyleWord(true);
		commands.setLineWrap(true);
		commands.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	canvasPanel.repaint();
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	canvasPanel.repaint();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {

	        }
	    });
		scroll.setViewportView(commands);		
		
//		super.pack();
//		super.setVisible(true);		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
