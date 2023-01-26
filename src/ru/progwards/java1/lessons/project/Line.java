package ru.progwards.java1.lessons.project;

public class Line {
	
	private String line;
	private long lineNumber;
	private String mark;
	private String lastMark;
	private String increment;
	private String stop;
	
	public Line() {}
	
	public Line(long lineNumber, String line) {
		this.lineNumber = lineNumber;
		this.line = line;
	}
	
	public void setIncrement(int len) {
		increment = "";
		for(int i = 0; i < len ; i++){
			increment += ".";
		}
	}
	
	public void setMark(String markValue) {
		if(lineNumber != 0L)
			mark = markValue;
	}
	
	public void removeMark() {
			mark = null;
	}
	
	public void setLastMark(String markValue) {
		if(lineNumber != 0L)
			lastMark = markValue;
	}
	
	public void setStop(String stopValue){
		stop = stopValue;
	}
	
	public long getLineNumber(){
		return lineNumber;
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
	
	public String getMark(){
		return mark;
	}
	
	public String getLastMark(){
		return lastMark;
	}
	
	public String getStop(){
		return stop;
	}
	
	public boolean hasMark() {
		if(mark != null)
			return true;
		return false;
	}
	
	public boolean hasLastMark() {
		if(lastMark != null)
			return true;
		return false;
	}
	
	public boolean hasStop() {
		if(stop != null)
			return true;
		return false;
	}
	public boolean hasIncrement() {
		if(increment != null)
			return true;
		return false;
	}
	
	public boolean hasLineNumber() {
		if(lineNumber > 0L)
			return true;
		return false;
	}
	
	public boolean checkConflict(Line otherLine) throws RuntimeException {
		if(getLine().equals(otherLine.getLine()))
			return true;
		else
			throw new RuntimeException();
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
		if(mark != null){
			strInfo += lineNumber + mark;
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
		if(increment != null)
			strContent += getLine() + increment;
		else
			strContent += getLine();
		return strInfo + strContent ;
	}
}
