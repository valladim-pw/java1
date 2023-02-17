package ru.progwards.java1.lessons.project;

import java.util.*;

public class Patch implements Relatable {
	
	private Diff diff;
	SrcFileCurrent srcFileCurr;
	LinkedList<CompareLine> currAnchorList;
	//LinkedList<CompareLine> currDiffList;
	
	public Patch(Diff diff, SrcFileCurrent srcFileCurr) {
		this.diff = diff;
		this.srcFileCurr = srcFileCurr;
		currAnchorList = new LinkedList<>(this.srcFileCurr.getDiffList());
	}
	
	public LinkedList<CompareLine> compareSrcFileStates() throws RuntimeException {
		LinkedList<CompareLine> anchorsList = diff.getAnchorsList();
		LinkedList<CompareLine> processList = new LinkedList<>();
		int i = 0;
		int count = 0;
		int firstIndex = 0;
		int lastIndex = 0;
		int anchIdx = 0;
		String anchor = "";
		long newSrcNum = 0L;
		long newPushNum = 0L;
		long currSrcNum = 0L;
		long currPushNum = 0;
		try {
			while(i < anchorsList.size()) {
				CompareLine patchLn = anchorsList.pollFirst();
				try {
					if(patchLn.hasAnchorLine()) {
						processList.add(patchLn);
						anchor = patchLn.getAnchorLine().getLine();
						if(lastIndex != 0)
							anchIdx++;
						count--;
					}	else {
						//Relatable.checkLinesForStopAndAlignDiffList(currAnchorList, currAnchorList.get(count), patchLn);
						if(patchLn.hasPatch()) {
							String[] idxNum = Relatable.checkForMatchingLinesMarkedWithAnchor(currAnchorList, patchLn, lastIndex, anchIdx);
							long srcNum = patchLn.getSrcLine().getLineNumber();
							long pushNum = patchLn.getPushLine().getLineNumber();
							//long diffNum = srcNum - pushNum;
							if(processList.size() == 1) {
								newSrcNum = Long.parseLong(idxNum[1]);
								newPushNum = newSrcNum - (srcNum - pushNum);
								firstIndex = Integer.parseInt(idxNum[0]);
								processList.get(0).getAnchorLine().setAnchorNumber(newSrcNum);
								patchLn.getSrcLine().setLineNumber(newSrcNum);
								patchLn.getPushLine().setLineNumber(newPushNum);
								currSrcNum = newSrcNum;
								currPushNum = newPushNum;
							}
							if(processList.size() > 1) {
								if(srcNum > 0L) {
									currSrcNum++;
									patchLn.getSrcLine().setLineNumber(currSrcNum);
								}
								if(pushNum > 0L) {
									currPushNum++;
									patchLn.getPushLine().setLineNumber(currPushNum);
								}
							}
							processList.add(patchLn);
							if(processList.getLast().hasEnd()) {
								lastIndex = firstIndex + (processList.size() - 1);
								currAnchorList.addAll(firstIndex, processList);
								currSrcNum = 0L;
								currPushNum = 0L;
								processList.clear();
							}
						}
					}
				} catch(RuntimeException rte) {
					String anch = anchor;
					PatchConflictException pce = new PatchConflictException(anch);
					pce.getMessage();
					throw pce;
				}
				count++;
			}
		} catch(RuntimeException rte) {
			throw rte;
		}
		return currAnchorList;
	}
	
	public LinkedList<CompareLine> getCurrAnchorList() {
		return currAnchorList;
	}
	
	public static void main(String[] args) {
		ProcessFile srcFile = new ProcessFile("srcFile3.txt");
		ProcessFile pushFile = new ProcessFile("pushFile3.txt");
		SrcFileCurrent srcFileCurr = new SrcFileCurrent("srcFileCurr2.txt");
		printList(srcFile.getLinesList());
		System.out.println("----------------------------------------");
		printList(pushFile.getLinesList());
		System.out.println("----------------------------------------");
		try {
			Diff diff = new Diff(srcFile, pushFile);
			diff.compareFiles();
			printList(diff.getAnchorsList());
			System.out.println("----------------------------------------");
			//printTwoLists(srcFileCurr.getLinesList(), diff.getAnchorsList());
			//System.out.println("----------------------------------------");
			Patch patch = new Patch(diff, srcFileCurr);
			//patch.compareSrcFileStates();
			//printList(patch.getCurrAnchorList());
		} catch(RuntimeException rte) {
			rte.printStackTrace();
			//System.out.println(rte);
		}
	}
	
	public static void printList(List<?extends Object> list) {
		for(Object line : list){
			System.out.println(line);
		}
	}
	
	public static class PatchConflictException extends RuntimeException {
		public String className;
		public String anchor;
		public PatchConflictException(String anchor) {
			className = this.getClass().getName();
			this.anchor = anchor;
		}
		
		@Override
		public String getMessage() {
			if(className.indexOf("$") != -1 )
				className = className.substring(className.indexOf("$") + 1);
			return className + ": " +
							"Lines marked with this anchor \"" + anchor + "\" have already been changed.\n" +
							"Patch implementation is not possible.";
		}
		
		@Override
		public String toString() {
			return getMessage();
		}
	}
}
