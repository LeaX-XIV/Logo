package com.borione.logo;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	static Map<String, Method> actions;
	static Map<String, Color> colors;
	
	private static void forward(final Turtle t, final String n) {
		t.move(Integer.parseInt(n));
	}
	
	@SuppressWarnings("unused")
	private static void backwards(final Turtle t, final String n) {
		Parser.forward(t, "-" + n);
	}
	
	private static void right(final Turtle t, final String n) {
		t.rotate(Integer.parseInt(n));
	}
	
	@SuppressWarnings("unused")
	private static void left(final Turtle t, final String n) {
		Parser.right(t, "-" + n);
	}
	
	@SuppressWarnings("unused")
	private static void color(final Turtle t, final String n) {
		t.setTrailColor(colors.get(n));
	}
	
	private static void home(final Turtle t) {
		t.returnHome();
	}
	
	private static void clean(final Turtle t) {
		t.clean();
	}
	
	@SuppressWarnings("unused")
	private static void clearScreen(final Turtle t) {
		Parser.home(t);
		Parser.clean(t);
	}
	
	private static void hideTurtle(final Turtle t) {
		t.setShowHead(false);
	}
	
	private static void showTurtle(final Turtle t) {
		t.setShowHead(true);
	}
	
	@SuppressWarnings("unused")
	private static void penUp(final Turtle t) {
		t.setWriting(false);
	}
	
	@SuppressWarnings("unused")
	private static void penDown(final Turtle t) {
		t.setWriting(true);
	}
	
	@SuppressWarnings("unused")
	private static void repeat(final Turtle t, final Integer n, final String pattern) {
		boolean shown = t.isShowHead();
		Parser.hideTurtle(t);
		for(int i = 0; i < n; i++) {
			Parser.parse(t, pattern);
		}
		if(shown) {
			Parser.showTurtle(t); 
		} else {
			Parser.hideTurtle(t);
		}
	}
	
	static {
		actions = new HashMap<>();
		try {
			actions.put("fd", Parser.class.getDeclaredMethod("forward", Turtle.class, String.class));
			actions.put("forward", Parser.class.getDeclaredMethod("forward", Turtle.class, String.class));
			actions.put("bk", Parser.class.getDeclaredMethod("backwards", Turtle.class, String.class));
			actions.put("backwards", Parser.class.getDeclaredMethod("backwards", Turtle.class, String.class));
			actions.put("rt", Parser.class.getDeclaredMethod("right", Turtle.class, String.class));
			actions.put("right", Parser.class.getDeclaredMethod("right", Turtle.class, String.class));
			actions.put("lt", Parser.class.getDeclaredMethod("left", Turtle.class, String.class));
			actions.put("left", Parser.class.getDeclaredMethod("left", Turtle.class, String.class));
			actions.put("cr", Parser.class.getDeclaredMethod("color", Turtle.class, String.class));
			actions.put("color", Parser.class.getDeclaredMethod("color", Turtle.class, String.class));
			actions.put("hm", Parser.class.getDeclaredMethod("home", Turtle.class));
			actions.put("home", Parser.class.getDeclaredMethod("home", Turtle.class));
			actions.put("cl", Parser.class.getDeclaredMethod("clean", Turtle.class));
			actions.put("clean", Parser.class.getDeclaredMethod("clean", Turtle.class));
			actions.put("cs", Parser.class.getDeclaredMethod("clearScreen", Turtle.class));
			actions.put("clearscreen", Parser.class.getDeclaredMethod("clearScreen", Turtle.class));
			actions.put("ht", Parser.class.getDeclaredMethod("hideTurtle", Turtle.class));
			actions.put("hideturtle", Parser.class.getDeclaredMethod("hideTurtle", Turtle.class));
			actions.put("st", Parser.class.getDeclaredMethod("showTurtle", Turtle.class));
			actions.put("showturtle", Parser.class.getDeclaredMethod("showTurtle", Turtle.class));
			actions.put("pu", Parser.class.getDeclaredMethod("penUp", Turtle.class));
			actions.put("penup", Parser.class.getDeclaredMethod("penUp", Turtle.class));
			actions.put("pd", Parser.class.getDeclaredMethod("penDown", Turtle.class));
			actions.put("pendown", Parser.class.getDeclaredMethod("penDown", Turtle.class));
			actions.put("rp", Parser.class.getDeclaredMethod("repeat", Turtle.class, Integer.class, String.class));
			actions.put("repeat", Parser.class.getDeclaredMethod("repeat", Turtle.class, Integer.class, String.class));
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
	
	public static void parse(Turtle t, String text) {
		String regex = "(((?<cmd1p>fd|forward|bk|backwaeds|rt|right|lt|left|cr|color) (?<p1>\\w+))(\\s|$)|(?<cmd0p>hm|home|cl|clean|cs|clearscreen|ht|hideturtle|st|showturtle|pu|penup|pd|pendown)|((?<repeat>rp|repeat) (?<nrep>\\d+) \\[(?<pattern>.*)\\]))";
		
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find()) {
			String cmd1p = m.group("cmd1p");
			String cmd0p = m.group("cmd0p");
			String repeat = m.group("repeat");
			
			if(cmd1p != null) {
				String n = m.group("p1");
				try {
					actions.get(cmd1p.toLowerCase()).invoke(null, t, n);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(cmd0p != null) {
				try {
					actions.get(cmd0p).invoke(null, t);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(repeat != null) {
				int n = Integer.parseInt(m.group("nrep"));
				String pattern = m.group("pattern");
				try {
					actions.get(repeat).invoke(null, t, n, pattern);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		t.drawHead();
	}

}
