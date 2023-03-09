package ru.progwards.java1.lessons.project;

import java.util.LinkedList;

public class Diff implements Relatable{
	
	private LinkedList<CompareLine> anchorsList = new LinkedList<>();
	private ProcessFile srcFile;
	private ProcessFile pushFile;
	
	public Diff(ProcessFile srcFile, ProcessFile pushFile) {
		this.srcFile = srcFile;
		this.pushFile = pushFile;
		compareFiles();
	}

	public void compareFiles() {
		LinkedList<Line> srcList = srcFile.getLinesList();
		LinkedList<Line> pushList = pushFile.getLinesList();
		LinkedList<CompareLine> processList = new LinkedList<>();
		LinkedList<CompareLine> formatList = new LinkedList<>();
		
		AnchorLine anchorLine;
		CompareLine anchor;
		int countMinus = 0;
		int countPlus = 0;
		int x = 0;
		int y = 0;
		int count = 0;
		int i = 0;
		while(i < srcList.size()) {
			
			Relatable.checkLinesComparisonCasesAndSetSignesWithChangingOrder(srcList, pushList);
			
			Line srcLn = srcList.pollFirst();
			Line pushLn = pushList.pollFirst();
			
			Relatable.checkAndSetLinesAlignmentToMaxLine(srcFile, srcLn);
			Relatable.checkAndSetLinesAlignmentToMaxLine(pushFile, pushLn);

			CompareLine compareLine = new CompareLine(srcLn, pushLn);
			compareLine.setPatch("ph.");
			processList.add(compareLine);
			
			if(Relatable.checkLineNumber(compareLine.getSrcLine()))
				countMinus++;
			if(Relatable.checkLineNumber(compareLine.getPushLine()))
				countPlus++;
			
			int proSize = Relatable.checkProcessListSize(processList);
			int checkStopNull = Relatable.checkLinesForNullStop(compareLine.getSrcLine(), compareLine.getPushLine());
			int checkSigns = Relatable.checkLinesSign(compareLine.getSrcLine(), compareLine.getPushLine());
			
			if(proSize <= 3 && checkSigns == SIGN_OR_SIGN) {
				if(count <= 3) {
					anchorLine = new AnchorLine();
					anchor = new CompareLine(anchorLine);
					if(formatList.isEmpty())
						formatList.add(anchor);
					Relatable.transferAllFromFirstListToSecond(processList, formatList);
				} else
					Relatable.transferAllFromFirstListToSecond(processList, formatList);
			}
			if(proSize == 3 && checkStopNull == NULL_STOP) {
				if(checkSigns == NULL_AND_NULL && !formatList.isEmpty()) {
					x = countMinus - 3; y = countPlus - 3;
					formatList.getFirst().getAnchorLine().setSignes(x, y);
					countMinus -= x; countPlus -= y;
					formatList.getLast().setEnd("end.");
					Relatable.transferAllFromFirstListToSecond(formatList, anchorsList);
				}
			}
			if(proSize == 4) {
				if (checkSigns == SIGN_OR_SIGN) {
					anchorLine = new AnchorLine();
					anchor = new CompareLine(anchorLine);
					anchorLine.setAnchorNumber(processList.getFirst().getSrcLine().getLineNumber());
					formatList.add(anchor);
					Relatable.transferAllFromFirstListToSecond(processList, formatList);
				}
				if(checkSigns == NULL_AND_NULL) {
					CompareLine exclude = processList.removeFirst();
					if(Relatable.checkLineNumber(exclude.getSrcLine()))
						countMinus--;
					if(Relatable.checkLineNumber(exclude.getPushLine()))
						countPlus--;
					exclude.removePatch();
					anchorsList.add(exclude);
				}
			}
			int checkOverStops = Relatable.checkLinesForOverStopToCompleteProcessing(compareLine.getSrcLine(), compareLine.getPushLine());
			int checkStops = Relatable.checkLinesForStopToCompleteProcessing(compareLine.getSrcLine(), compareLine.getPushLine());
			
			if(checkOverStops == OVER_STOP_AND_OVER_STOP) {
				if(!formatList.isEmpty()) {
					countMinus -= processList.size();
					countPlus -= processList.size();
					formatList.getFirst().getAnchorLine().setSignes(countMinus, countPlus);
				}
				if(formatList.isEmpty()) {
					Relatable.removePatches(processList);
					Relatable.transferAllFromFirstListToSecond(processList, anchorsList);
					break;
				}
			} else if(checkStops == STOP_OR_STOP) {
				Relatable.transferAllFromFirstListToSecond(processList, formatList);
				formatList.getFirst().getAnchorLine().setSignes(countMinus, countPlus);
			}	else if(checkStops == STOP_AND_STOP) {
				formatList.getLast().setEnd("end.");
				Relatable.transferAllFromFirstListToSecond(formatList, anchorsList);
				Relatable.removePatches(processList);
				Relatable.transferAllFromFirstListToSecond(processList, anchorsList);
				break;
			}
			count++;
		}
		Relatable.printList(anchorsList);
	}
	
	public LinkedList<CompareLine> getAnchorsList() {
		return anchorsList;
	}
}
