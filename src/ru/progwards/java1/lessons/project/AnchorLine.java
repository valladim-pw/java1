package ru.progwards.java1.lessons.project;

public class AnchorLine extends Line {
	
	private long anchorLineNumber;
	private int anchorMinus;
	private int anchorPlus;
	private String line;
	
	public AnchorLine() {
		super(-1, "");
		anchorLineNumber = 1;
		anchorMinus = 0;
		anchorPlus = 0;
	}
	
	public void setAnchorNumber(long anchorNumber){
		anchorLineNumber = anchorNumber;
	}
	
	public void setSignes(int minus, int plus) {
		anchorMinus = minus;
		anchorPlus = plus;
	}
	
//	public long getAnchorLineNumber() {
//		return anchorLineNumber;
//	}
//
//	public int getAnchorMinus() {
//		return anchorMinus;
//	}
//
//	public int getAnchorPlus() {
//		return anchorPlus;
//	}
	
	public String getLine() {
		line = "@@ -" + anchorLineNumber + "," + anchorMinus + " +" + anchorLineNumber + "," + anchorPlus;
		return line;
	}
}