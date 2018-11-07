package com.leax_xiv.logo;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	static Map<String, Method> actions;
	static Map<String, Color> colors;
	
	Map<String, String> procedures = new HashMap<>();
	Map<String, List<String>> procArgs = new HashMap<>();
	
	String procedureDeclarationRegex = "^\\s*(TO (?<declProc>\\w+)\\s+(?<argNames>(\\s*\\:\\w+\\b)*)(\\s|$)(?<procBody>.+?)END)";
	String oneParamRegex  = "^\\s*((?<cmd1p>\\b(fd|forward|bk|backwards|rt|right|lt|left|cr|color)\\b)\\s+(?<p1>\\w+))(\\s|$)";
	String zeroParamRegex = "^\\s*(?<cmd0p>\\b(hm|home|cl|clean|cs|clearscreen|ht|hideturtle|st|showturtle|pu|penup|pd|pendown)\\b)";
	String repeatRegex = "^\\s*((?<repeat>\\b(rp|repeat)\\b)\\s+(?<nrep>\\d+)\\s+\\[(?<pattern>.*)\\])";
	String procedureCallRegex = "^\\s*((?<procName>\\w+\\b)\\s*(?<args>(\\s*\\d+)*)?$)";
	
	Pattern p1 = Pattern.compile(procedureDeclarationRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	Pattern p2 = Pattern.compile(oneParamRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	Pattern p3 = Pattern.compile(zeroParamRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	Pattern p4 = Pattern.compile(repeatRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	Pattern p5 = Pattern.compile(procedureCallRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	
	
	
	private void forward(final String n) {
		t.move(Integer.parseInt(n));
	}
	
	@SuppressWarnings("unused")
	private void backwards(final String n) {
		this.forward("-" + n);
	}
	
	private void right(final String n) {
		t.rotate(Integer.parseInt(n));
	}
	
	@SuppressWarnings("unused")
	private void left(final String n) {
		this.right("-" + n);
	}
	
	@SuppressWarnings("unused")
	private void color(final String n) {
		t.setTrailColor(colors.get(n));
	}
	
	private void home() {
		t.returnHome();
	}
	
	private void clean() {
		t.clean();
	}
	
	@SuppressWarnings("unused")
	private void clearScreen() {
		this.home();
		this.clean();
	}
	
	private void hideTurtle() {
		t.setShowHead(false);
	}
	
	private void showTurtle() {
		t.setShowHead(true);
	}
	
	@SuppressWarnings("unused")
	private void penUp() {
		t.setWriting(false);
	}
	
	@SuppressWarnings("unused")
	private void penDown() {
		t.setWriting(true);
	}
	
	@SuppressWarnings("unused")
	private void repeat(final Integer n, final String pattern) {
		boolean shown = t.isShowHead();
		this.hideTurtle();
		for(int i = 0; i < n; i++) {
			this.parse(pattern);
		}
		if(shown) {
			this.showTurtle(); 
		} else {
			this.hideTurtle();
		}
	}
	
	static {
		actions = new HashMap<>();
		try {
			actions.put("fd", Parser.class.getDeclaredMethod("forward", String.class));
			actions.put("forward", Parser.class.getDeclaredMethod("forward", String.class));
			actions.put("bk", Parser.class.getDeclaredMethod("backwards", String.class));
			actions.put("backwards", Parser.class.getDeclaredMethod("backwards", String.class));
			actions.put("rt", Parser.class.getDeclaredMethod("right", String.class));
			actions.put("right", Parser.class.getDeclaredMethod("right", String.class));
			actions.put("lt", Parser.class.getDeclaredMethod("left", String.class));
			actions.put("left", Parser.class.getDeclaredMethod("left", String.class));
			actions.put("cr", Parser.class.getDeclaredMethod("color", String.class));
			actions.put("color", Parser.class.getDeclaredMethod("color", String.class));
			actions.put("hm", Parser.class.getDeclaredMethod("home"));
			actions.put("home", Parser.class.getDeclaredMethod("home"));
			actions.put("cl", Parser.class.getDeclaredMethod("clean"));
			actions.put("clean", Parser.class.getDeclaredMethod("clean"));
			actions.put("cs", Parser.class.getDeclaredMethod("clearScreen"));
			actions.put("clearscreen", Parser.class.getDeclaredMethod("clearScreen"));
			actions.put("ht", Parser.class.getDeclaredMethod("hideTurtle"));
			actions.put("hideturtle", Parser.class.getDeclaredMethod("hideTurtle"));
			actions.put("st", Parser.class.getDeclaredMethod("showTurtle"));
			actions.put("showturtle", Parser.class.getDeclaredMethod("showTurtle"));
			actions.put("pu", Parser.class.getDeclaredMethod("penUp"));
			actions.put("penup", Parser.class.getDeclaredMethod("penUp"));
			actions.put("pd", Parser.class.getDeclaredMethod("penDown"));
			actions.put("pendown", Parser.class.getDeclaredMethod("penDown"));
			actions.put("rp", Parser.class.getDeclaredMethod("repeat", Integer.class, String.class));
			actions.put("repeat", Parser.class.getDeclaredMethod("repeat", Integer.class, String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		colors = new HashMap<>();
		colors.put("black", Color.BLACK);
		colors.put("blue", Color.BLUE);
		colors.put("gray", Color.GRAY);
		colors.put("green", Color.GREEN);
		colors.put("orange", Color.ORANGE);
		colors.put("pink", Color.PINK);
		colors.put("red", Color.RED);
		colors.put("white", Color.WHITE);
		colors.put("yellow", Color.YELLOW);
	}
	
	private Turtle t;
	public Parser(Turtle t) {
		this.t = t;
	}
	
	public void parse(String text) {		
		
		int del = 0;
		Matcher m = p1.matcher(text);
		while(m.find()) {	// Save all procedure declarations
			String declProc = m.group("declProc");
			String argNames = m.group("argNames");
			String procBody = m.group("procBody");
			procArgs.put(declProc, Arrays.asList(argNames.split("\\s")));
			procedures.put(declProc, procBody);
			text = text.substring(0, m.start() - del).concat(text.substring(m.end() - del)).trim();
			del += m.end() - m.start() + 1;
		}
		
		while(!text.trim().isEmpty()) {
			m = p2.matcher(text);
			if(m.find()) {
				String cmd1p = m.group("cmd1p");
				String n = m.group("p1");
				try {
					actions.get(cmd1p.toLowerCase()).invoke(this, n);
					text = text.substring(0, m.start()).concat(text.substring(m.end()));
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			m = p3.matcher(text);
			if(m.find()) {
				String cmd0p = m.group("cmd0p");
				try {
					actions.get(cmd0p).invoke(this);
					text = text.substring(0, m.start()).concat(text.substring(m.end()));
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			m = p4.matcher(text);
			if(m.find()) {
				String repeat = m.group("repeat");
				int n = Integer.parseInt(m.group("nrep"));
				String pattern = m.group("pattern");
				try {
					actions.get(repeat).invoke(this, n, pattern);
					text = text.substring(0, m.start()).concat(text.substring(m.end()));
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			m = p5.matcher(text);
			if(m.find()) {
				String procName = m.group("procName");
				// GET ARGS
				String[] arguments = m.group("args").split("\\s+");
				String procBody = procedures.get(procName);				
				List<String> argNames = procArgs.get(procName);
				
				if(procBody == null) {
					// Procedure doesn't exist
					break;
				}
				
				if(arguments.length != argNames.size()) {
					// Wrong number of arguments
					break;
				}
				
				for(int i = 0; i < argNames.size(); i++) {
					String argName = argNames.get(i);
					String argValue = arguments[i];
					if(!argName.equals(null) && !argName.equals("")) {
						procBody = procBody.replaceAll(argName, " " + argValue + " ");
					}
				}
				this.parse(procBody);
				text = text.substring(0, m.start()).concat(text.substring(m.end()));
				continue;
			}
			
			break;
		}
		t.drawHead();
	}

}
