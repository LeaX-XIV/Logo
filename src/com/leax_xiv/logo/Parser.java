package com.leax_xiv.logo;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Parser {
	
	static Map<String, Method> actions;
	static Map<String, Color> colors;
	
	Map<String, String> procedures = new HashMap<>();
	Map<String, List<String>> procArgs = new HashMap<>();
	
	String procedureDeclarationRegex = "^\\s*(TO (?<declProc>\\w+)\\s+(?<argNames>(\\s*\\:\\w+\\b)*)(\\s|$)(?<procBody>.+?)END)";
	
	Pattern p1 = Pattern.compile(procedureDeclarationRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

	
	private void forward(final Integer n) {
		t.move(n);
	}
	
	@SuppressWarnings("unused")
	private void backwards(final Integer n) {
		this.forward(-n);
	}
	
	private void right(final Integer n) {
		t.rotate(n);
	}
	
	@SuppressWarnings("unused")
	private void left(final Integer n) {
		this.right(-n);
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
			actions.put("fd", Parser.class.getDeclaredMethod("forward", Integer.class));
			actions.put("forward", Parser.class.getDeclaredMethod("forward", Integer.class));
			actions.put("bk", Parser.class.getDeclaredMethod("backwards", Integer.class));
			actions.put("backwards", Parser.class.getDeclaredMethod("backwards", Integer.class));
			actions.put("rt", Parser.class.getDeclaredMethod("right", Integer.class));
			actions.put("right", Parser.class.getDeclaredMethod("right", Integer.class));
			actions.put("lt", Parser.class.getDeclaredMethod("left", Integer.class));
			actions.put("left", Parser.class.getDeclaredMethod("left", Integer.class));
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
		// Add declared procedures to map
		text = populateProcedures(text);
		List<Token> tokens = extractTokens(text);
		
//		for (Token token : tokens) {
//			System.out.println(token + ", ");
//		}
		
		while(tokens.size() > 0) {
			expandTokens(tokens);
		}
		t.drawHead();
	}
	
	private String populateProcedures(String text) {
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
		
		// Return text without procedure declarations
		return text;
	}
	
	private List<Token> extractTokens(final String text) {
		StringBuilder buffer = new StringBuilder(text);
		List<Token> tokens = new ArrayList<>();
		StringBuilder token = new StringBuilder();
		
		String[] patterns = new String[] {
				"[A-Za-z]",
				"\\d",
				"\\+",
				"\\-",
				"\\*",
				"\\/",
				"\\[",
				"\\]",
				"\\s"
		};
		int lastMatched = -1;
		
		while(buffer.length() > 0) {
			char c = buffer.charAt(0);
			buffer.deleteCharAt(0);
			
			if(lastMatched >= 0 && Pattern.matches(patterns[lastMatched], ""+c)) {
				token.append(c);
			} else {
				// New token
				if(lastMatched >= 0) {
					// Save previous token, if exists
					if(lastMatched < patterns.length - 1) {
						// Don't save whitespace
						Token t;
						if(lastMatched == 0) {	// Reference
							t = new Token(token.toString());
						} else if(lastMatched == 1) {	// Number
							t = new Token(Integer.parseInt(token.toString()));
						} else {
							t = new Token(TokenType.values()[lastMatched]);
						}
						tokens.add(t);
					}
					token = new StringBuilder();
				}
				for(int i = 0; i < patterns.length; i++) {
					String pattern = patterns[i];
					if(Pattern.matches(pattern, ""+c)) {
						token.append(c);
						lastMatched = i;
						break;
					}
				}
			}
		}
		
		// Save last token
		if(lastMatched >= 0) {
			// Save previous token, if exists
			if(lastMatched < patterns.length - 1) {
				// Don't save whitespace
				Token t;
				if(lastMatched == 0) {	// Reference
					t = new Token(token.toString());
				} else if(lastMatched == 1) {	// Number
					t = new Token(Integer.parseInt(token.toString()));
				} else {
					t = new Token(TokenType.values()[lastMatched]);
				}
				tokens.add(t);
			}
			token = new StringBuilder();
		}
		return tokens;
	}
	
	private Token expandTokens(List<Token> tokens) {		
		Token t = tokens.remove(0);
		
		switch(t.getType()) {
		case REFERENCE: {
			String function = t.getString();
			if(actions.containsKey(function)) {
				Method m = actions.get(function);
				int numArgs = m.getParameterCount();
				List<Token> args = new ArrayList<>(numArgs);
				for(int i = 0; i < numArgs; i++) {
					args.add(expandTokens(tokens));
				}
				// Execute and exit
				
				Object[] argv = new Object[numArgs];
				for(int i = 0; i < numArgs; i++) {
					Token token = args.get(i);
					if(token.requireString()) {
						argv[i] = token.getString();
					} else if(token.requireNumber()) {
						argv[i] = token.getNumber();
					}
				}
				try {
					m.invoke(this, argv);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			} else if(procedures.containsKey(function)) {
				String body = procedures.get(function);
				List<String> argNames = procArgs.get(function);
				int numArgs = argNames.size();
				List<Token> args = new ArrayList<>(numArgs);
				for(int i = 0; i < numArgs; i++) {
					args.add(expandTokens(tokens));
				}
				for(int i = 0; i < argNames.size(); i++) {
					String argName = argNames.get(i);
					Token argToken = args.get(i);
					String argValue = "";
					if(argToken.requireString()) {
						argValue = argToken.getString();
					} else if(argToken.requireNumber()) {
						argValue = "" + argToken.getNumber();
					}
					if(!argName.equals(null) && !argName.equals("")) {
						body = body.replaceAll(argName, " " + argValue + " ");
					}
				}
				List<Token> equivalent = extractTokens(body);
				tokens.addAll(0, equivalent);
				// Exit
			} else if(colors.containsKey(function)) {
				return new Token(TokenType.STRING, t.getString());
			}
		} break;
		case NUMBER: {
			Integer result;
			
			List<Token> expression = new ArrayList<>();
			// Create a math expression with consecutive numbers and operators
			expression.add(t);
			while(tokens.size() > 0) {
				if(tokens.get(0).getType().equals(TokenType.PLUS) ||
						tokens.get(0).getType().equals(TokenType.MINUS) ||
						tokens.get(0).getType().equals(TokenType.ASTERISK) ||
						tokens.get(0).getType().equals(TokenType.SLASH)) {
					
					if(tokens.get(1).getType().equals(TokenType.NUMBER)) {
						expression.add(tokens.remove(0));
						expression.add(tokens.remove(0));
					} else {
						break;
					}
				} else {
					break;
				}
			}
			// Evaluate the expression down to a single token
			StringBuilder sb = new StringBuilder();
			for(Token token : expression) {
				if(token.getType().equals(TokenType.NUMBER)) {
					sb.append(token.getNumber());
				} else if(token.getType().equals(TokenType.PLUS)) {
					sb.append('+');
				} else if(token.getType().equals(TokenType.MINUS)) {
					sb.append('-');
				} else if(token.getType().equals(TokenType.ASTERISK)) {
					sb.append('*');
				} else if(token.getType().equals(TokenType.SLASH)) {
					sb.append('/');
				}
			}
			ScriptEngineManager mgr = new ScriptEngineManager();
		    ScriptEngine engine = mgr.getEngineByName("JavaScript");
		    try {
				result = (int) Double.parseDouble("" + engine.eval(sb.toString()));
			} catch (ScriptException e) {
				result = new Integer(0/0);
			}
		    
		    return new Token(result);
		}
		case PLUS:
		case MINUS: {	// Add a preceding zero
			tokens.add(0, t);
			tokens.add(0, new Token(0));
			// Exit
		} break;
		case LEFT_BRACKET: {
			int level = 1;
			StringBuilder sb = new StringBuilder();
			while(level > 0 && tokens.size() > 0) {
				Token token = tokens.remove(0);
				if(token.requireString()) {
					sb.append(token.getString());
				} else if(token.requireNumber()) {
					sb.append(token.getNumber());
				} else if(token.getType().equals(TokenType.PLUS)) {
					sb.append('+');
				} else if(token.getType().equals(TokenType.MINUS)) {
					sb.append('-');
				} else if(token.getType().equals(TokenType.ASTERISK)) {
					sb.append('*');
				} else if(token.getType().equals(TokenType.SLASH)) {
					sb.append('/');
				} else if(token.getType().equals(TokenType.LEFT_BRACKET)) {
					level++;
					sb.append('[');
				} else if(token.getType().equals(TokenType.RIGHT_BRACKET)) {
					if(--level == 0) {
						return new Token(TokenType.STRING, sb.toString());
					}
					sb.append(']');
				}
			}
			
		}
		}
		
		return null;
	}
}
