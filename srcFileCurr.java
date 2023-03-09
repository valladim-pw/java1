package ru.progwards.java1.lessons.project;

public class Line {
	
	private String line;
	private long lineNumber;
	private String sign;
	
	public Line() {}
	
	public Line(long lineNumber, String line) {
		this.lineNumber = lineNumber;
		this.line = line;
	}
	
	enum Sign {
		PLUS,
		MINUS
	}
	
	public void setSign(String signValue) {
		if(lineNumber != 0L)
			sign = signValue;
	}
	
	public void Sign(Sign sign) {
		switch(sign) {
			case PLUS:
				this.setSign("+");
				break;
			case MINUS:
				this.setSign("-");
				break;
			default:
				this.setSign("");
		}
	}
	
	public String getSign(){
		return sign;
	}
	
	public boolean hasSign() {
		if(sign != null)
			return true;
		return false;
	}
	
	public void removeSign() {
		sign = null;
	}
	
	public String getLine() {
		if(stop != null)
			return stop + line;
		else
			return line;
	}
	
	public int lineIndent() {
		int indent = getLine().length() - getLine().trim().length();
		if(indent == 0)
			return 1;
		indent *= 2;
		return indent;
	}
	
	public int lineLength(){
		return getLine().trim().length();
	}
	
	public void setLineNumber(long newNumber) {
		if(lineNumber >= 0L)
			lineNumber = newNumber;
	}
	
	public long getLineNumber(){
		return lineNumber;
	}
	
	public boolean hasLineNumber() {
		if(lineNumber > 0L)
			return true;
		return false;
	}
	
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	
	public String getEmpty() {
		return empty;
	}
	
	public boolean hasEmpty() {
		if(empty != null)
			return true;
		return false;
	}
	
	public void setAlignment(int len) {
		alignment = "";
		for(int i = 0; i < len ; i++){
			alignment += ".";
		}
	}
	
	public boolean hasAlignment() {
		if(alignment != null)
			return true;
		return false;
	}
}