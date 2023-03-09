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
		path = Paths.get(file.trim());
		long num = 1;
		String line = "";
		int methodStartInd = 0;
		int methodEndLen = 0;
		try(BufferedReader bfr = Files.newBufferedReader(path)) {
			while ((line = bfr.readLine()) != null) {
				if(line.indexOf("{") != -1) {
					if(methodStartInd == 0)
						methodStartInd = line.length() - line.trim().length();
				}
				if(line.trim().equals("}")) {
					if(line.length() - line.trim().length() == methodStartInd && methodEndLen == 0)
						methodEndLen = line.length();
				}
				Line ln = new Line(num, line);
				if(ln.getLine().indexOf("{") != -1 &&
								ln.getLine().length() - ln.getLine().trim().length() == methodStartInd)
					ln.setMethodStart(".ms.");
				if(ln.getLine().trim().equals("}") && ln.getLine().length() == methodEndLen)
					ln.setMethodEnd(".me.");
				if(ln.getLine().indexOf("@") != -1 &&
								ln.getLine().length() - ln.getLine().trim().length() == methodStartInd)
					ln.setAnnotation(".a.");
				linesList.add(ln);
				num++;
			}
			linesList.getLast().setOverStop(".os.");
			Line stopLine = new EmptyLine();
			stopLine.setStop("stop");
			linesList.add(stopLine);
		} catch(IOException e) {
			e.printStackTrace();
		}
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
	
	public int setAlignLength(ProcessFile file, Line line){
		int newLen = file.maxLnLength() - line.lineLength();
		newLen = (newLen - line.lineIndent()) + file.maxLnIndent();
		return newLen;
	}
	
	public LinkedList<Line> getLinesList() {
		return linesList;
	}
}
