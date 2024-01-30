package ru.progwards.java1.lessons.project;

import java.util.*;

public interface Relatable {
	
	int SIGN_OR_SIGN = 1;
	int NULL_AND_NULL = 2;
	int STOP_OR_STOP = 1;
	int STOP_AND_STOP = 2;
	int OVER_STOP_AND_OVER_STOP = 1;
	int NULL_STOP = 0;
	
	static void checkLinesComparisonCasesAndSetSignesWithChangingOrder(LinkedList<Line> srcList, LinkedList<Line> pushList) {
		
		Line srcLn = srcList.getFirst();
		Line pushLn = pushList.getFirst();
		
		if(!srcLn.hasOverStop() && !pushLn.hasOverStop()) {
			if(!srcLn.getLine().equals(pushLn.getLine())) {
				if(srcLn.hasAnnotation() || pushLn.hasAnnotation()) {
					if(srcLn.hasAnnotation()  && !pushLn.hasAnnotation()) {
						srcList.offerFirst(new EmptyLine());
						srcList.getFirst().setEmpty(".vd.");
					} else if(pushLn.hasAnnotation() && !srcLn.hasAnnotation()) {
						pushList.offerFirst(new EmptyLine());
						pushList.getFirst().setEmpty(".vd.");
					}
				} else if(srcLn.hasMethodStart() || pushLn.hasMethodStart()) {
					if(srcLn.hasMethodStart() && !pushLn.hasMethodStart()) {
						srcList.offerFirst(new EmptyLine());
						srcList.getFirst().setEmpty(".vd.");
					} else if(pushLn.hasMethodStart() && !srcLn.hasMethodStart()) {
						pushList.offerFirst(new EmptyLine());
						pushList.getFirst().setEmpty(".vd.");
					}
				} else if(srcLn.hasMethodEnd() || pushLn.hasMethodEnd()) {
					if(srcLn.hasMethodEnd() && !pushLn.hasMethodEnd()) {
						srcList.offerFirst(new EmptyLine());
						srcList.getFirst().setEmpty(".vd.");
					} else if(pushLn.hasMethodEnd() && !srcLn.hasMethodEnd()) {
						pushList.offerFirst(new EmptyLine());
						pushList.getFirst().setEmpty(".vd.");
					}
				}
				srcLn.Sign(Line.Sign.MINUS);
				pushLn.Sign(Line.Sign.PLUS);
			} else {
				if(srcLn.hasSign() && !pushLn.hasSign())
					srcLn.removeSign();
				if(!srcLn.hasSign() && pushLn.hasSign())
					pushLn.removeSign();
			}
		} else {
			if(srcLn.hasOverStop() && !pushLn.hasOverStop()) {
				pushLn.Sign(Line.Sign.PLUS);
				srcList.offerFirst(new EmptyLine());
				srcList.getFirst().setEmpty(".vd.");
			} else if(pushLn.hasOverStop() && !srcLn.hasOverStop()) {
				srcLn.Sign(Line.Sign.MINUS);
				pushList.offerFirst(new EmptyLine());
				pushList.getFirst().setEmpty(".vd.");
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

	static int checkForMatchingLinesMarkedWithAnchor(LinkedList<Line> list, CompareLine line, int firstIdx) throws RuntimeException {
		
		boolean checkConflict = false;
		int newIdx1 = 0;
		int newIdx2 = 0;
		boolean checkNum = false;
		
		for(int i = firstIdx; i < list.size(); i++) {
			String newStr = list.get(i).getLine();
			long newNum = list.get(i).getLineNumber();
			String oldStr = line.getSrcLine().getLine();
			long oldNum = line.getSrcLine().getLineNumber();
			if (oldStr.equals(newStr)) {
				if(oldNum == newNum)
					checkNum = true;
				if(checkNum) {
					newIdx1 = i;
					list.remove(i);
					checkConflict = true;
					break;
				}	else {
					newIdx2 = i;
					checkConflict = true;
				}
			} else
				checkConflict = false;
		}
		if(!checkConflict)
			throw new RuntimeException();
		else
			if(checkNum)
				return newIdx1;
			else {
				list.remove(newIdx2);
				return newIdx2;
			}
	}
	
	static void checkPatchLineForEndAndSetEndToLines(CompareLine patchLine) {
		
		if(patchLine.hasEnd()) {
			patchLine.getSrcLine().setEnd(".ed.");
			
			
			
			patchLine.getPushLine().setEnd(".ed.");
		}
	}
	
	static void checkPatchLineAndAddLinesToProcessList(CompareLine patchLn, LinkedList<Line> processList) {
		
		if(!patchLn.getSrcLine().hasEmpty()) {
			if(patchLn.getSrcLine().hasSign())
				processList.add(patchLn.getPushLine());
			
			else
				processList.add(patchLn.getSrcLine());
		} else
			processList.add(patchLn.getPushLine());
	}
	
	static void checkProcessListEndAndTransferToPatchList(LinkedList<Line> processList, LinkedList<Line> patchList, int idx) {
		
		if(processList.getLast().hasEnd()) {
			processList.removeFirst();
			patchList.addAll(idx, processList);
			processList.clear();
		}
	}
	
	static void checkAndChangePatchListNumbersOrder(LinkedList<Line> patchList) {
		
		int count = 1;
		
		if(patchList.getLast().hasStop())
			patchList.removeLast();
		for(Line line : patchList) {
			if(line.getLineNumber() != count)
				line.setLineNumber(count);
			count++;
		}
	}
	
	static void printList(List<? extends Object> list) {
		
		for(Object line : list){
			System.out.println(line);
		}
	}
}