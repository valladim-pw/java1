package ru.progwards.java1.lessons.project;

public class Line {
	
	private String line;
	private long lineNumber;
	private String sign;
	private String overStop;
	private String stop;
	private String empty;
	private String end;
	private String alignment;
	private String methodEnd;
	private String methodStart;
	private String annotation;
	
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
	
	public void setOverStop(String overStopValue) {
		if(lineNumber != 0L)
			overStop = overStopValue;
	}
	
	public boolean hasOverStop() {
		if(overStop != null)
			return true;
		return false;
	}
	
	public void setStop(String stopValue){
		stop = stopValue;
	}
	
	public boolean hasStop() {
		if(stop != null)
			return true;
		return false;
	}
	
	public void setEmpty(String empty) {
		this.empty = empty;
	}

	public boolean hasEmpty() {
		if(empty != null)
			return true;
		return false;
	}
	
	public void setEnd(String endValue){
		end = endValue;
	}
	
	public boolean hasEnd() {
		if(end != null)
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
	
	public void setMethodEnd(String methodEnd) {
		this.methodEnd = methodEnd;
	}
	
	public boolean hasMethodEnd() {
		if(methodEnd != null)
			return true;
		return false;
	}
	
	public void setMethodStart(String methodStart) {
		this.methodStart = methodStart;
	}
	
	public boolean hasMethodStart() {
		if(methodStart != null)
			return true;
		return false;
	}
	
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public boolean hasAnnotation() {
		if(annotation != null)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		String strInfo = "";
		String strContent = "";
		String addStr5 = "     ";
		String addStr4 = "    ";
		String addStr3 = "   ";
		String addStr2 = "  ";
		String addStr1 = " ";
		if(sign != null){
			strInfo += lineNumber + sign;
			if(lineNumber < 10L)
				strInfo += addStr3;
			else if(lineNumber >= 10L && lineNumber < 100L)
				strInfo += addStr2;
			else
				strInfo += addStr1;
		} else if(lineNumber <= 0L) {
			strInfo += addStr5;
		} else {
			strInfo += lineNumber ;
			if(lineNumber < 10L)
				strInfo += addStr4;
			else if(lineNumber >= 10L && lineNumber < 100L)
				strInfo += addStr3;
			else
				strInfo += addStr1;
		}
		if(alignment != null)
			strContent += getLine() + alignment;
		else
			strContent += getLine();
		return strInfo + strContent;
	}
}
