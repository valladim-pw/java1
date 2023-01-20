package ru.progwards.java1.lessons.project;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Push {
	
	private SourceFile srcFile;
	private PushFile pushFile;
	int num = 0;
	
	public Push(SourceFile srcFile, PushFile pushFile) {
		this.srcFile = srcFile;
		this.pushFile = pushFile;
		pushFile.push(srcFile);
		num = pushFile.number;
	}
	
	public LinkedList<Line> getLinesList() {
		return pushFile.getLinesList();
	}
	
	public LinkedList<CompareLine> getDiffList() {
		return pushFile.getDiffList();
	}
	
	public String getGitNick() {
		return pushFile.getGitNick();
	}
	
	public LocalDateTime getDatetime() {
		return pushFile.getDatetime();
	}
}