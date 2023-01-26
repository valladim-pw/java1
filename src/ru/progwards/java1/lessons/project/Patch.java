package ru.progwards.java1.lessons.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Patch {
	
	private Diff diff;
	private List<Line> patchList = new ArrayList<>();
	
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
		while(i < size) {
			AnchorLine anchorLine;
			CompareLine anchor;
			CompareLine ln = diffList.pollFirst();
			indexMinus = i - 3;
			long num1 = ln.getSrcLine().getLineNumber();
			long num2 = ln.getPushLine().getLineNumber();
			String mark1 = ln.getSrcLine().getMark();
			String mark2 = ln.getPushLine().getMark();
			ln.setPatch(".ph.");
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
					}	else {
						x = countMinus;
						y = countPlus;
						x -= 3;
						y -= 3;
						formation.getFirst().getAnchorLine().setSignes(x, y);
						countMinus -= x;
						countPlus -= y;
						x = 0; y = 0;
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
			if(ln.getGenStop() != null) {
				if(!formation.isEmpty()) {
					if(formation.getLast().getSrcLine().hasMark()) {
						formation.getFirst().getAnchorLine().setSignes(countMinus, countPlus);
						formation.addAll(process);
						process.clear();
					} else {
						countMinus -= process.size();
						countPlus -= process.size();
						formation.getFirst().getAnchorLine().setSignes(countMinus, countPlus);
					}
					diffList.addAll(formation);
					formation.clear();
				}
				if(formation.isEmpty()) {
					removePatches(process);
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
						patchList.add(line.getSrcLine());
				} else if(line.getPatch() != null) {
					if(line.getPushLine().getLineNumber() != 0L)
						patchList.add(line.getPushLine());
				}
			}
		}
	}
	
	public void applyPatch(Path path) {
		try (BufferedWriter bfw = Files.newBufferedWriter(path, CREATE, WRITE, TRUNCATE_EXISTING)) {
			String line = "";
			int count = 1;
			for(Line ln : getPatchList()) {
				line = ln.getLine();
				bfw.write(line, 0, line.length());
				if(count < getPatchList().size())
					bfw.newLine();
				count++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public List<Line> getPatchList() {
		return patchList;
	}
	
	public static void removePatches(LinkedList<CompareLine> list) {
		for(CompareLine line : list){
			line.removePatch();
		}
	}
	
	public static void printList(List<?extends Object> list) {
		for(Object line : list){
			System.out.println(line);
		}
	}
	public static void printLists(List<?extends Object> list1, List<?extends Object> list2, List<?extends Object> list3 ) {
		for(int i = 0; i < list1.size() && i < list2.size() && i < list3.size(); i++){
			System.out.println(list1.get(i).toString() + list2.get(i).toString() + list3.get(i).toString());
		}
	}
	
	public static void main(String[] args) {
		SourceFile srcFile = new SourceFile("srcFile.txt");
		PushFile pushFile1 = new PushFile("Nick1","pushFile1.txt");
		PushFile pushFile2 = new PushFile("Nick2","pushFile2.txt");
		
		Push push1 = new Push(srcFile, pushFile1);
		System.out.println("After push1: ");
		System.out.println("diff1:\n" );
		printList(pushFile1.getDiffList());
		System.out.println("----------------------------------------");
		for(int i = 0; i < 1000000; i++){
			int a = i * i;
		}
		Push push2 = new Push(srcFile, pushFile2);
		System.out.println("After push2: ");
		System.out.println("diff2:\n");
		printList(pushFile2.getDiffList());
		System.out.println("----------------------------------------");
		
		System.out.println("lists:\n");
		printLists(srcFile.getLinesList(), pushFile1.getLinesList(), pushFile2.getLinesList());
		System.out.println("----------------------------------------");
		try {
			Diff diff = new Diff(srcFile, push1, push2);
			System.out.println("finalDiff:\n");
			printList(diff.getFinalDiffList());
			Patch patch = new Patch(diff);
			System.out.println("finalDiff with anchors:\n");
			printList(diff.getFinalDiffList());
			System.out.println("patchList:\n");
			printList(patch.getPatchList());
			Path path = Paths.get("srcPatchFile.txt");
			patch.applyPatch(path);
		} catch(RuntimeException rt) {
			System.out.println(rt);
		}
		
	}
}
