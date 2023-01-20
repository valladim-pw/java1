package ru.progwards.java1.lessons.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Patch {
	
	private Diff diff;
	private List<String> patchList = new ArrayList<>();
	//private LinkedList<CompareLine> finalAnchorList = new LinkedList<>();
	
	public Patch(Diff diff) {
		this.diff = diff;
		setAnchors(diff.getFinalDiffList());
		getPatchLines();
	}
	
	public static void setAnchors(LinkedList<CompareLine> diffList) {
		LinkedList<CompareLine> process = new LinkedList<>();
		LinkedList<CompareLine> formation = new LinkedList<>();
		int size = diffList.size();
		int countMinus = 0;
		int countPlus = 0;
		int i = 0;
		int x = 0;
		int y = 0;
		int indexMinus = 0;
		int indexPlus = 0;
		while(i < size) {
			AnchorLine anchorLine;
			CompareLine anchor;
			CompareLine ln = diffList.pollFirst();
			indexMinus = i - 3;
			indexPlus = size - 3;
			long num1 = ln.getSrcLine().getLineNumber();
			long num2 = ln.getPushLine().getLineNumber();
			String mark1 = ln.getSrcLine().getMark();
			String mark2 = ln.getPushLine().getMark();
			ln.setPatch("ptch");
			if (mark1 == null && mark2 == null && i >= indexPlus)
				ln.removePatch();
			process.add(ln);
			if(num1 > 0L)
				countMinus++;
			if(num2 > 0L)
				countPlus++;
			if (process.size() <= 3) {
				if (mark1 != null || mark2 != null) {
					if (indexMinus < 0) {
						anchorLine = new AnchorLine();
						anchor = new CompareLine(anchorLine);
						if(formation.isEmpty())
							formation.add(anchor);
						formation.addAll(process);
						process.clear();
					} else {
						formation.addAll(process);
						process.clear();
					}
				}
			}
			if(process.size() == 3) {
				if(mark1 == null && mark2 == null && !formation.isEmpty()) {
					if(formation.getLast().getEnd() == null) {
						formation.addAll(process);
						process.clear();
						formation.getLast().setEnd("");
					} else {
						x = countMinus;
						y = countPlus;
						x -= 3;
						y -= 3;
						formation.getFirst().getAnchorLine().setSignes(x, y);
						countMinus -= x;
						countPlus -= y;
						diffList.addAll(formation);
						formation.clear();
					}
				}
			}
			if(process.size() == 4) {
				if(mark1 != null || mark2 != null) {
					anchorLine = new AnchorLine();
					anchor = new CompareLine(anchorLine);
					anchorLine.setAnchorNumber(process.getFirst().getSrcLine().getLineNumber());
					formation.add(anchor);
					formation.addAll(process);
					process.clear();
				}
				if(mark1 == null && mark2 == null) {
					CompareLine exclude = process.removeFirst();
					if(exclude.getSrcLine().getLineNumber() != 0L)
						countMinus--;
					if(exclude.getPushLine().getLineNumber() != 0L)
						countPlus--;
					exclude.removePatch();
					diffList.add(exclude);
				}
			}
			if(ln.getStop() != null) {
				if(!formation.isEmpty()) {
					formation.getFirst().getAnchorLine().setSignes(countMinus, countPlus);
					diffList.addAll(formation);
					formation.clear();
				} else {
					diffList.addAll(process);
					process.clear();
				}
			}
			i++;
		}
	}
	
	public void getPatchLines() {
		LinkedList<CompareLine> diffList = diff.getFinalDiffList();
		for(CompareLine line : diffList) {
			if(line.getSrcLine() != null) {
				if(line.getPatch() == null) {
					if (line.getSrcLine().getLineNumber() != 0L)
						patchList.add(line.getSrcLine().toString());
				} else if(line.getPatch() != null) {
					if(line.getPushLine().getLineNumber() != 0L)
						patchList.add(line.getPushLine().toString());
				}
			}
		}
	}

	public List<String> getPatchList() {
		return patchList;
	}
	
	public static void printList(List<?extends Object> list) {
		for(Object line : list){
			System.out.println(line);
		}
	}
	
	public static void main(String[] args) {
		SourceFile srcFile = new SourceFile("SrcFile.txt");
		PushFile pushFile1 = new PushFile("Nick1","PushFile1.txt");
		PushFile pushFile2 = new PushFile("Nick2","PushFile2.txt");
//		System.out.println("Before: ");
//		System.out.println("src:\n" + srcFile.toString());
//		System.out.println("push1:\n" + pushFile1.toString());
//		System.out.println("push2:\n" + pushFile2.toString());
//		System.out.println("----------------------------------------");
		Push push1 = new Push(srcFile, pushFile1);
		System.out.println("After push1: ");
//		System.out.println("src:\n" + srcFile.toString());
//		System.out.println("push1:\n" + pushFile1.toString());
//		System.out.println("push2:\n" + pushFile2.toString());
		System.out.println("diff1:\n" );
		printList(pushFile1.getDiffList());
		System.out.println("----------------------------------------");
		for(int i = 0; i < 1000000; i++){
			int a = i * i;
		}
		Push push2 = new Push(srcFile, pushFile2);
		System.out.println("After push2: ");
//		System.out.println("src:\n" + srcFile.toString());
//		System.out.println("push1:\n" + pushFile1.toString());
//		System.out.println("push2:\n" + pushFile2.toString());
		System.out.println("diff2:\n");
		printList(pushFile2.getDiffList());
		System.out.println("----------------------------------------");
		Diff diff = new Diff(srcFile, push1, push2);
		//diff.comparePushFiles();
		System.out.println("finalDiff:\n");
		printList(diff.getFinalDiffList());
		Patch patch = new Patch(diff);
		System.out.println("finalDiff with anchors:\n");
		printList(diff.getFinalDiffList());
		System.out.println("patchList:\n");
		printList(patch.getPatchList());
	}
}
