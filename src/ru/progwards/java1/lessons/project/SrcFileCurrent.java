package ru.progwards.java1.lessons.project;

public class SrcFileCurrent extends ProcessFile implements Relatable {
	
	public SrcFileCurrent(String string) {
		super(string);
		this.specifyMaxLine();
		this.setLinesAlignment();
	}
	
	public void setLinesAlignment() {
		int i = 0;
		for(Line ln : this.getLinesList()) {
			Relatable.checkAndSetLinesAlignmentToMaxLine(this, ln);
			this.getLinesList().set(i, ln);
			i++;
		}
	}
}
