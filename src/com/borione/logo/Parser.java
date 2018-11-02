package com.borione.logo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	static Map<String, Method> actions;
	
	private static void forward(Turtle t, Integer n) {
		t.move(n);
	}
	
	private static void backwards(Turtle t, Integer n) {
		Parser.forward(t, -n);
	}
	
	private static void right(Turtle t, Integer n) {
		t.rotate(n);
	}
	
	private static void left(Turtle t, Integer n) {
		Parser.right(t, -n);
	}
	
//	private static void home(Turtle t) {
//		t.returnHome();
//	}
//	
//	private static void clean(Turtle t) {
//		t.clean();
//	}
//	
//	private static void clearScreen(Turtle t) {
//		Parser.home(t);
//		Parser.clearScreen(t);
//	}
	
	private static void hideTurtle(Turtle t) {
		t.setShowHead(false);
	}
	
	private static void showTurtle(Turtle t) {
		t.setShowHead(true);
	}
	
	private static void penUp(Turtle t) {
		t.setWriting(false);
	}
	
	private static void penDown(Turtle t) {
		t.setWriting(true);
	}
	
	private static void repeat(Turtle t, Integer n, String pattern) {
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
			actions.put("fd", Parser.class.getDeclaredMethod("forward", Turtle.class, Integer.class));
			actions.put("forward", Parser.class.getDeclaredMethod("forward", Turtle.class, Integer.class));
			actions.put("bk", Parser.class.getDeclaredMethod("backwards", Turtle.class, Integer.class));
			actions.put("backwards", Parser.class.getDeclaredMethod("backwards", Turtle.class, Integer.class));
			actions.put("rt", Parser.class.getDeclaredMethod("right", Turtle.class, Integer.class));
			actions.put("right", Parser.class.getDeclaredMethod("right", Turtle.class, Integer.class));
			actions.put("lt", Parser.class.getDeclaredMethod("left", Turtle.class, Integer.class));
			actions.put("left", Parser.class.getDeclaredMethod("left", Turtle.class, Integer.class));
//			actions.put("hm", Parser.class.getDeclaredMethod("home", Turtle.class));
//			actions.put("home", Parser.class.getDeclaredMethod("home", Turtle.class));
//			actions.put("cl", Parser.class.getDeclaredMethod("clean", Turtle.class));
//			actions.put("clean", Parser.class.getDeclaredMethod("clean", Turtle.class));
//			actions.put("cs", Parser.class.getDeclaredMethod("clearScreen", Turtle.class));
//			actions.put("clearscreen", Parser.class.getDeclaredMethod("clearScreen", Turtle.class));
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
	}
	
	public static void parse(Turtle t, String text) {
		String regex = "(((?<cmd1p>fd|forward|bk|backwaeds|rt|right|lt|left) (?<p1>\\d+))|(?<cmd0p>hm|home|cl|clean|cs|clearscreen|ht|hidetrutle|st|showturtle|pu|penup|pd|pendown)|((?<repeat>rp|repeat) (?<nrep>\\d+) \\[(?<pattern>.*)\\]))";
		
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find()) {
			String cmd1p = m.group("cmd1p");
			String cmd0p = m.group("cmd0p");
			String repeat = m.group("repeat");
			
			if(cmd1p != null) {
				int n = Integer.parseInt(m.group("p1"));
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
