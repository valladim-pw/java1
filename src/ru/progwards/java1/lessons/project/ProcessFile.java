package ru.progwards.java1.lessons.project;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ProcessFile {
	
	private Path path;
	private LinkedList<Line> linesList = new LinkedList<>();
	private LinkedList<CompareLine> diffList = new LinkedList<>();

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
	
	public LinkedList<Line> getLinesList() {
		return linesList;
	}
}
