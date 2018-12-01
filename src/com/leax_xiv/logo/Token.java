package com.leax_xiv.logo;

enum TokenType {
	REFERENCE,
	NUMBER,
	PLUS,
	MINUS,
	ASTERISK,
	SLASH,
	LEFT_BRACKET,
	RIGHT_BRACKET,
	STRING;
	
	public boolean requireString() {
		return this.ordinal() == REFERENCE.ordinal() || this.ordinal() == STRING.ordinal();
	}
	
	public boolean requireNumber() {
		return this.ordinal() == NUMBER.ordinal();
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}

public class Token {
	
	private TokenType tt;
	private String ref;
	private Integer num;
	
	public Token(TokenType tt) {
		if(tt.requireString()) {
			throw new IllegalArgumentException("Wrong constructor for token REFERENCE.");
		}
		if(tt.requireNumber()) {
			throw new IllegalArgumentException("Wrong constructor for token NUMBER.");
		}
		
		this.tt = tt;
		this.ref = null;
		this.num = null;
	}
	
	public Token(String ref) {
		this.tt = TokenType.REFERENCE;
		this.ref = ref;
		this.num = null;
	}
	
	public Token(Integer num) {
		this.tt = TokenType.NUMBER;
		this.ref = null;
		this.num = num;
	}
	
	public Token(TokenType tt, String str) {
		if(!tt.equals(TokenType.STRING)) {
			throw new IllegalArgumentException("Wrong constructor for token " + tt + ".");
		}
		
		this.tt = tt;
		this.ref = str;
		this.num = null;
	}
	
	public TokenType getType() {
		return this.tt;
	}
	
	public String getString() {
		return this.ref;
	}
	
	public Integer getNumber() {
		return this.num;
	}
	
	public boolean requireString() {
		return this.tt.requireString();
	}
	
	public boolean requireNumber() {
		return this.tt.requireNumber();
	}
	
	@Override
	public String toString() {
		String after = this.tt.requireString() ? "(" + this.ref + ")" : this.tt.requireNumber() ? "(" + this.num + ")" : "";
		return this.tt.toString() + after;
	}

}
