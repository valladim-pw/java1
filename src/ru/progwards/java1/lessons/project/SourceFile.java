package ru.progwards.java1.lessons.project;

import java.util.*;

public class SourceFile extends ProcessFile {
	
	private int maxLnLength;
	private int maxLnIndent;
	public Comparator lengthComparator = new Comparator<Line>() {
		@Override
		public int compare(Line l1, Line l2) {
			return Integer.compare(l1.getLine().length(), l2.getLine().length());
		}
	};
	
	public SourceFile(String string) {
		super(string);
		this.specifyMaxLine();
	}
	
	public int maxLnLength() {
		return maxLnLength;
	}
	
	public int maxLnIndent() {
		return maxLnIndent;
	}
	
	public void specifyMaxLine() {
		Line maxLine = Collections.max(getLinesList(), lengthComparator);
		maxLnLength = maxLine.getLine().trim().length();
		maxLnIndent = maxLine.getLine().length() - maxLine.getLine().trim().length();
		maxLnIndent *= 2;
	}
}
