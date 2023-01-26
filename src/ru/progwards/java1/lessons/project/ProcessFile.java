package ru.progwards.java1.lessons.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ProcessFile {
	
	private Path path;
	private LinkedList<Line> linesList = new LinkedList<>();
	private LinkedList<CompareLine> diffList = new LinkedList<>();
	private int maxLnLength;
	private int maxLnIndent;
	public Comparator lengthComparator = new Comparator<Line>() {
		@Override
		public int compare(Line l1, Line l2) {
			return Integer.compare(l1.getLine().length(), l2.getLine().length());
		}
	};

	public ProcessFile() {}
	
	public ProcessFile(String file) {
		path = Paths.get(file);
		long num = 1;
		String line = "";
		try(BufferedReader bfr = Files.newBufferedReader(path)) {
			while ((line = bfr.readLine()) != null) {
				Line ln = new Line(num, line);
				linesList.add(ln);
				num++;
			}
			linesList.getLast().setLastMark("");
			Line stopLine = new Line(0L, "");
			stopLine.setStop("stop");
			linesList.add(stopLine);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Path getPath() {
		return path;
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
	public int setIncrLength(ProcessFile file, Line line){
		int newLen = file.maxLnLength() - line.lineLength();
		newLen = (newLen - line.lineIndent()) + file.maxLnIndent();
		return newLen;
	}
	public LinkedList<Line> getLinesList() {
		return linesList;
	}
}
