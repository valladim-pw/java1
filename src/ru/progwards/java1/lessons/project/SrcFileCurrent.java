package ru.progwards.java1.lessons.project;

import java.util.LinkedList;

public class SrcFileCurrent extends ProcessFile implements Relatable {
	
	private LinkedList<CompareLine> diffList = new LinkedList<>();

	public SrcFileCurrent(String string) {
		super(string);
		this.specifyMaxLine();
		this.createDiffList();
	}
	
	public void createDiffList() {
		int i = 0;
		for(Line ln : this.getLinesList()) {
			Line empty = new EmptyLine();
			Relatable.checkAndSetLinesAlignmentToMaxLine(this, ln);
			Relatable.checkAndSetLinesAlignmentToMaxLine(this, empty);
			this.getLinesList().set(i, ln);
			CompareLine compareLine = new CompareLine(ln, empty);
			diffList.add(compareLine);
			i++;
		}
		diffList.getLast().setGenStop("");
	}
	
	public LinkedList<CompareLine> getDiffList() {
		return diffList;
	}
}
