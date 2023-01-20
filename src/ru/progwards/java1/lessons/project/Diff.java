package ru.progwards.java1.lessons.project;

import java.time.LocalDateTime;
import java.util.*;

public class Diff {
	
	public LinkedList<CompareLine> finalDiffList = new LinkedList<>();
	private SourceFile srcFile;
	private Push push1;
	private Push push2;
	
	public Diff(SourceFile srcFile, Push push1, Push push2) {
		this.srcFile = srcFile;
		if(push1.num < push2.num) {
			this.push1 = push1;
			this.push2 = push2;
		} else {
			this.push1 = push2;
			this.push2 = push1;
		}
		finalDiffList = comparePushFiles();
	}
	
	public LinkedList<CompareLine> comparePushFiles() {
		LinkedList<Line> srcList = new LinkedList<>(srcFile.getLinesList());
		LinkedList<Line> pushList1 = new LinkedList<>(push1.getLinesList());
		LinkedList<Line> pushList2 = new LinkedList<>(push2.getLinesList());
		int i = 0;
		int size = srcList.size();
		try{
			while(i < size){
				Line empty = new Line(0L, "");
				CompareLine compareLn = new CompareLine(srcList.get(i), empty);
				Line ln = pushList2.pollFirst();
				if(ln == null)
					break;
				if(ln.hasStop()) {
					pushList2.offer(empty);
					empty.setStop("stop");
				}
				if(ln.getLineNumber() == 0L)
					ln = pushList2.pollFirst();
				if(ln.hasMark()){
					if(pushList1.get(i).hasMark()) {
						try{
							pushList1.get(i).checkConflict(ln);
						} catch(RuntimeException rt) {
							long num = pushList1.get(i).getLineNumber();
							String str = pushList1.get(i).getLine();
							String nick = push1.getGitNick();
							LocalDateTime ldt = push1.getDatetime();
							LinesConflictException lce = new LinesConflictException(num, str, nick, ldt);
							lce.getMessage();
							throw lce;
						}
						compareLn.setPushLine(pushList1.get(i));
					} else {
						compareLn.setPushLine(ln);
					}
					if(pushList1.get(i).hasStop() && !ln.hasStop()) {
						pushList1.offer(empty);
						empty.setStop("stop");
						compareLn.setPushLine(ln);
					}
				} else {
					compareLn.setPushLine(pushList1.get(i));
				}
				finalDiffList.add(compareLn);
				i++;
			}
		} catch(RuntimeException rt) {
			System.out.println(rt);
		}
		finalDiffList.getLast().setStop("STOP");
		return finalDiffList;
	}
	
	public LinkedList<CompareLine> getFinalDiffList() {
		return finalDiffList;
	}
	
	public static class LinesConflictException extends RuntimeException {
		public String className;
		public long lineNumber;
		public String str;
		public String gitNick;
		public LocalDateTime ldtime;
		public LinesConflictException(long number, String ln, String nick, LocalDateTime time) {
			className = this.getClass().getName();
			lineNumber = number;
			str = ln;
			gitNick = nick;
			ldtime = time;
		}
		
		@Override
		public String getMessage() {
			if(className.indexOf("$") != -1 )
				className = className.substring(className.indexOf("$") + 1);
			return className + ": " +
							"Line" + lineNumber + " has already been changed by another user:\n" +
							"changed string: \"" + str + "\"" + "\n" +
							"by user: " + gitNick + "\n" +
							"at time: " + ldtime;
		}
		
		@Override
		public String toString() {
			return getMessage();
		}
	}
}
