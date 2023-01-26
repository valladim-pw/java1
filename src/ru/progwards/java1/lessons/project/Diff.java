package ru.progwards.java1.lessons.project;

import java.time.LocalDateTime;
import java.util.LinkedList;

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
		long lnNum = 1;
		String ln = "";
		int size = srcList.size();
		int pushSize1 = pushList1.size();
		int pushSize2 = pushList2.size();
		try{
			while(i < size){
				Line line;
				CompareLine compareLn = new CompareLine(srcList.get(i), new Line(0L, ""));
				Line stop = new Line(0L, "stop");
				Line pushLn1 = pushList1.pollFirst();
				Line pushLn2 = pushList2.pollFirst();
				if(size > pushSize1){
					pushList1.offer(new Line(0L, ""));
					pushList1.getLast().setStop("stop");
				}
				if(size > pushSize2){
					pushList2.offer(new Line(0L, ""));
					pushList2.getLast().setStop("stop");
				}
				if(pushLn1.hasMark() || pushLn2.hasMark()){
					if(pushLn1.hasMark() && !pushLn2.hasMark())
						ln = pushLn1.getLine();
					else if(!pushLn1.hasMark() && pushLn2.hasMark())
						ln = pushLn2.getLine();
					else if(pushLn1.hasMark() && pushLn2.hasMark()) {
						try{
							pushLn1.checkConflict(pushLn2);
							ln = pushLn1.getLine();
						} catch(RuntimeException rt) {
							Line lne = srcList.get(i);
							String str = pushLn1.getLine();
							String nick = push1.getGitNick();
							LocalDateTime ldt = push1.getDatetime();
							LinesConflictException lce = new LinesConflictException(lne, str, nick, ldt);
							lce.getMessage();
							throw lce;
						}
					}
					line = new Line(lnNum, ln);
					line.setMark("+");
				} else {
					if(pushLn1.hasStop() && !pushLn2.hasStop())
						ln = pushLn2.getLine();
					else if (pushLn2.hasStop() && !pushLn1.hasStop())
						ln = pushLn1.getLine();
					else
						ln = pushLn1.getLine();
					line = new Line(lnNum, ln);
					if((srcList.get(i).hasMark() && pushLn1.hasLineNumber()) ||
								(srcList.get(i).hasMark() && pushLn2.hasLineNumber())
					)
						line.setMark("+");
				}
				if((pushLn1.hasStop() && !pushLn2.hasLineNumber()) ||
						(pushLn2.hasStop() && !pushLn1.hasLineNumber())
				)
					compareLn.setPushLine(stop);
				else
					compareLn.setPushLine(line);
				finalDiffList.add(compareLn);
				i++;
				lnNum++;
			}
		} catch(RuntimeException rt) {
			throw rt;
		}
		CompareLine remove = finalDiffList.removeLast();
		finalDiffList.getLast().setGenStop(".gs.");
		finalDiffList.offerLast(remove);
		return finalDiffList;
	}

	public LinkedList<CompareLine> getFinalDiffList() {
		return finalDiffList;
	}

	public static class LinesConflictException extends RuntimeException {
		public String className;
		public Line line;
		public String str;
		public String gitNick;
		public LocalDateTime ldtime;
		public LinesConflictException(Line ln, String change, String nick, LocalDateTime time) {
			className = this.getClass().getName();
			line = ln;
			str = change;
			gitNick = nick;
			ldtime = time;
		}

		@Override
		public String getMessage() {
			if(className.indexOf("$") != -1 )
				className = className.substring(className.indexOf("$") + 1);
			return className + ": " +
							"Source file (" +  line.getLineNumber() + ") \"" + line.getLine() + "\" has already been changed by another user:\n" +
							"change: \"" + str + "\"" + "\n" +
							"by user: " + gitNick + "\n" +
							"at time: " + ldtime;
		}

		@Override
		public String toString() {
			return getMessage();
		}
	}
}
