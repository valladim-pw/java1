package ru.progwards.java1.lessons.project;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface Relatable {
	
	int SIGN_OR_SIGN = 1;
	int NULL_AND_NULL = 2;
	int STOP_OR_STOP = 1;
	int STOP_AND_STOP = 2;
	int OVER_STOP_AND_OVER_STOP = 1;
	int NULL_STOP = 0;
	
	static LinkedList<String> createListWithIdenticalStringsRetainFromPushFile(ProcessFile srcFile, ProcessFile pushFile) {
		LinkedList<String> srcList = srcFile.getStringsList();
		LinkedList<String> pushList = pushFile.getStringsList();
		Set<String> srcSet = new LinkedHashSet<>(srcList);
		Set<String> pushSet = new LinkedHashSet<>(pushList);
		
		Set<String> similar = new LinkedHashSet<>(srcSet);
		
		similar.retainAll(pushSet);
		LinkedList<String> simList = new LinkedList<>(similar);
		return simList;
	}
	
	static LinkedList<String> createListWithIdenticalStringsRetainFromSrcFile(ProcessFile pushFile, ProcessFile srcFile) {
		LinkedList<String> srcList = srcFile.getStringsList();
		LinkedList<String> pushList = pushFile.getStringsList();
		Set<String> srcSet = new LinkedHashSet<>(srcList);
		Set<String> pushSet = new LinkedHashSet<>(pushList);
		
		Set<String> similar = new LinkedHashSet<>(pushSet);
		
		similar.retainAll(srcSet);
		LinkedList<String> simList = new LinkedList<>(similar);
		return simList;
	}
	
	static void checkAddedRemovedEmptyLinesAndSetSigns(LinkedList<Line> srcList, LinkedList<Line> pushList) {
		if(!srcList.isEmpty() && !pushList.isEmpty()) {
			Line srcLn = srcList.getFirst();
			Line pushLn = pushList.getFirst();
			
			if(srcLn.getLine().isBlank() && !srcLn.hasStop()) {
				if(!pushLn.getLine().isBlank()) {
					srcLn.Sign(Line.Sign.MINUS);
					//pushList.offerFirst(new EmptyLine());
					//pushList.getFirst().setEmpty(".");
				}
			} else	if(pushLn.getLine().isBlank() && !pushLn.hasStop()) {
				if(!srcLn.getLine().isBlank()) {
					pushLn.Sign(Line.Sign.PLUS);
					//srcList.offerFirst(new EmptyLine());
					//srcList.getFirst().setEmpty(".");
				}
			}
		}
	}
	
	static int checkLinePresenceInIdenticalListsAndGetMaxIndex(LinkedList<String> strList1, LinkedList<String> strList2, Line line) {
		int maxIdx = -1;
		if(!line.hasOverStop()) {
			if(strList1.contains(line.getLine()) && strList2.contains(line.getLine())) {
				int idx1 = strList1.indexOf(line.getLine());
				int idx2 = strList2.indexOf(line.getLine());
				maxIdx = Math.max(idx1, idx2);
			}
		}
		return maxIdx;
	}

	static void checkLinesComparisonCasesAndSetSignesWithChangingOrder(LinkedList<Line> srcList, LinkedList<Line> pushList, int srcIdx, int pushIdx) {
		Line srcLn = srcList.getFirst();
		Line pushLn = pushList.getFirst();
		if(!srcLn.getLine().equals(pushLn.getLine())) {
			if (!srcLn.hasOverStop() && !pushLn.hasOverStop()) {
				if(!srcLn.getLine().isBlank() && srcIdx == -1)
					srcLn.Sign(Line.Sign.MINUS);
				if(!pushLn.getLine().isBlank() && pushIdx == -1)
					pushLn.Sign(Line.Sign.PLUS);
				if (srcIdx != -1 && pushIdx != -1) {
					if (srcIdx > pushIdx) {
						srcLn.Sign(Line.Sign.MINUS);
						if(!pushLn.hasSign())
							pushList.offerFirst(new EmptyLine());
					} else if (pushIdx > srcIdx) {
						pushLn.Sign(Line.Sign.PLUS);
						if(!srcLn.hasSign())
							srcList.offerFirst(new EmptyLine());
					} else {
						srcLn.Sign(Line.Sign.MINUS);
						pushLn.Sign(Line.Sign.PLUS);
					}
				}
				if (srcIdx != -1 && pushIdx == -1 ) {
					if(!srcLn.hasSign())
						srcList.offerFirst(new EmptyLine());
				}
				if (srcIdx == -1 && pushIdx != -1) {
					if(!pushLn.hasSign())
						pushList.offerFirst(new EmptyLine());
				}
			} else {
				if ((srcIdx != -1 || srcIdx == -1 ) && pushLn.hasOverStop()) {
					srcLn.Sign(Line.Sign.MINUS);
					pushList.offerFirst(new EmptyLine());
				} else if ((pushIdx != -1 || pushIdx == -1 ) && srcLn.hasOverStop()) {
					pushLn.Sign(Line.Sign.PLUS);
					srcList.offerFirst(new EmptyLine());
				}
			}
		}
	}
	
	static Line checkAndSetLinesAlignmentToMaxLine(ProcessFile file, Line line) {
		int alignLen = file.setAlignLength(file, line);
		if(!line.hasAlignment()) {
			line.setAlignment(alignLen);
		}
		return line;
	}
	
	static boolean checkLineNumber(Line line) {
		if(line.getLineNumber() > 0L)
			return true;
		return false;
	}
	
	static int checkProcessListSize(LinkedList<CompareLine> list) {
		return list.size();
	}
	
	static void checkLinesOverStopToSetSigns(Line line1, Line line2) {
		if((line1.hasOverStop() && !line2.hasOverStop()) ||
						(!line1.hasOverStop() && line2.hasOverStop())
		) {
			if(!line1.hasSign())
				line1.Sign(Line.Sign.MINUS);
			if(!line2.hasSign())
				line2.Sign(Line.Sign.PLUS);
		}
	}
	
	static int checkLinesForNullStop(Line line1, Line line2) {
		int check = -1;
		if(!line1.hasStop() && !line2.hasStop())
			check = NULL_STOP;
		return check;
	}
	
	static int checkLinesSign(Line line1, Line line2) {
		int check = 0;
		if(line1.hasSign() || line2.hasSign()) {
			check = SIGN_OR_SIGN;
		}
		if(!line1.hasSign() && !line2.hasSign()) {
			check = NULL_AND_NULL;
		}
		return check;
	}
	
	static void transferAllFromFirstListToSecond(LinkedList<CompareLine> list1, LinkedList<CompareLine> list2) {
		list2.addAll(list1);
		list1.clear();
	}
	
	static int checkLinesForOverStopToCompleteProcessing(Line line1, Line line2) {
		int check = -1;
		if(line1.hasOverStop() && line2.hasOverStop())
			check = OVER_STOP_AND_OVER_STOP;
		return check;
	}
	
	static int checkLinesForStopToCompleteProcessing(Line line1, Line line2) {
		int check = -1;
		if((line1.hasStop() && !line2.hasStop()) ||
						(!line1.hasStop() && line2.hasStop())
		)
			check = STOP_OR_STOP;
		if(line1.hasStop() && line2.hasStop())
			check = STOP_AND_STOP;
		return check;
	}
	
	static void removePatches(LinkedList<CompareLine> list) {
		for(CompareLine line : list){
			line.removePatch();
		}
	}
	
	static void checkLinesForStopAndAlignDiffList(LinkedList<CompareLine> list, CompareLine line1, CompareLine line2) {
		Line empty1;
		Line empty2;
		if((line1.hasGenStop() && !line2.hasGenStop()) ||
						(!line1.hasGenStop() && line2.hasGenStop())
		) {
			empty1 = new EmptyLine();
			empty2 = new EmptyLine();
			CompareLine comprLine = new CompareLine(empty1, empty2);
			list.offer(comprLine);
			comprLine.setGenStop("");
		}
	}
	
	static String[] checkForMatchingLinesMarkedWithAnchor(LinkedList<CompareLine> list, CompareLine line, int lastIdx, int anchIdx) throws RuntimeException {
		long newNum = 0L;
		int firstIdx = 0;
		boolean checkConflict = false;
		String[] idxNum = new String[2];
		for(int i = lastIdx; i < list.size(); i++) {
			String newStr = list.get(i).getSrcLine().getLine();
			Line newLn = list.get(i).getSrcLine();
			String oldStr = line.getSrcLine().getLine();
			Line oldLn = line.getSrcLine();
			if(lastIdx != 0) {
				lastIdx++;
			}
			if(!oldLn.hasEmpty() && !oldLn.hasStop()) {
				if (oldStr.equals(newStr)) {
					if(lastIdx == 0)
						firstIdx = i;
					else
						firstIdx = lastIdx - 1;
					newNum = list.get(i).getSrcLine().getLineNumber();
					idxNum[0] = String.valueOf(firstIdx);
					idxNum[1] = String.valueOf(newNum);
					list.remove(list.get(i));
					checkConflict = true;
					break;
				} else
				//if(!newLn.hasSign())
					//newLn.setLineNumber(lastIdx - anchIdx);
				checkConflict = false;
			} else {
				idxNum[0] = String.valueOf(firstIdx);
				idxNum[1] = String.valueOf(newNum);
				checkConflict = true;
				break;
			}
			
		}
		if(!checkConflict)
			throw new RuntimeException();
		else
			return idxNum;
	}
	
	static void printList(List<? extends Object> list) {
		for(Object line : list){
			System.out.println(line);
		}
	}
}