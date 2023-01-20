package ru.progwards.java1.lessons.project;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class PushFile extends ProcessFile {
	
	private String gitNick;
	private LinkedList<CompareLine> diffList = new LinkedList<>();
	private LocalDateTime datetime;
	public int number;
	public static int count = 1;
	
	public PushFile(String nick, String string) {
		super(string);
		gitNick = nick;
	}
	
	public LinkedList<CompareLine> push(SourceFile srcFile) {
		number = count;
		count++;
		LinkedList<Line> srcList = srcFile.getLinesList();
		LinkedList<Line> pushList = getLinesList();
		int srcSize = srcList.size();
		int pushSize = pushList.size();
		
		for(int i = 0; i < srcList.size() && i < pushList.size(); i++){
			if(srcList.get(i).hasLastMark() && !pushList.get(i).hasLastMark()) {
				srcList.get(i).setMark("-");
				pushList.get(i).setMark("+");
			}
			if(!srcList.get(i).getLine().equals(pushList.get(i).getLine())) {
				if(pushList.get(i).getLine().isBlank() && !pushList.get(i).hasStop()) {
					srcList.add(i, new Line(0L, ""));
					pushList.get(i).setMark("+");
				}else	if(srcList.get(i).getLine().isBlank() && !srcList.get(i).hasStop()) {
					pushList.add(i, new Line(0L, ""));
					srcList.get(i).setMark("-");
				}
				pushList.get(i).setMark("+");
				srcList.get(i).setMark("-");
				Line empty;
				if(srcList.get(i).hasStop() && !pushList.get(i).hasStop()) {
					empty = new Line(0L, "");
					if(srcList.size() < pushSize){
						pushList.get(i).setMark("+");
						srcList.offer(empty);
						empty.setStop(".");
					}
				} else if(pushList.get(i).hasStop() && !srcList.get(i).hasStop()) {
					empty = new Line(0L, "");
					if(pushList.size() < srcSize) {
						srcList.get(i).setMark("-");
						pushList.offer(empty);
						empty.setStop(".");
					}
				}
			}
			if(srcList.get(i).getStop() == ".")
				srcList.get(i).setStop("");
			if(pushList.get(i).getStop() == ".")
				pushList.get(i).setStop("");
			int newLen = srcFile.maxLnLength() - srcList.get(i).lineLength();
			newLen = (newLen - srcList.get(i).lineIndent()) + srcFile.maxLnIndent();
			if(!srcList.get(i).hasIncrement()) {
				srcList.get(i).setIncrement(newLen);
			}
			CompareLine compareLine = new CompareLine(srcList.get(i), pushList.get(i));
			diffList.add(compareLine);
		}
		datetime = LocalDateTime.now(ZoneId.systemDefault());
		return diffList;
	}
	
	public String getGitNick() {
		return gitNick;
	}
	
	public LinkedList<CompareLine> getDiffList() {
		return diffList;
	}
	
	public LocalDateTime getDatetime(){
		return datetime;
	}
}
