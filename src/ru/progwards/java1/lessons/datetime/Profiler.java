package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.util.*;

public class Profiler {
	public static LinkedList<StatisticInfo> listInfo = new LinkedList<>();
	public static LinkedList<StatisticInfo> listTest = new LinkedList<>();
	public static int num = 1;
	public static int sum;
	public static void code(int elementsCount){
		List<Integer> arrayList = new ArrayList();
		for (int i = 0; i < elementsCount; i++) {
			arrayList.add(0, i);
		}
	}
	public static void enterSection(String name){
		long start = Instant.now().toEpochMilli();
		StatisticInfo section = new StatisticInfo(name);
		section.start = start;
		listInfo.push(section);
		if(listInfo.size() > 1){
			for(StatisticInfo sect : listInfo) {
				if (sect.start != 0) {
					String s1 = section.sectionName;
					String s2 = sect.sectionName;
					if (s1.equals(s2) && !section.equals(sect)){
						section.count++;
					}
				}
			}
		}
		if(section.count == 1 && section.fullTime == 0) {
			section.number = num;
			num++;
		}
	}
	public static void exitSection(String name){
		long finish = Instant.now().toEpochMilli();
		long diff;
		StatisticInfo section = new StatisticInfo(name);
		section.finish = finish;
		StatisticInfo peek = listInfo.peek();
		String s1 = section.sectionName;
		String s2 = peek.sectionName;
		listInfo.push(section);
		if(s2.equals(s1)){
			diff = section.finish - peek.start;
			peek.fullTime = (int) diff;
			peek.selfTime = (int) diff;
			listInfo.removeFirst();
			for(StatisticInfo sect : listInfo) {
				if (sect.sectionName.equals(s2) && !sect.equals(peek)) {
					if(sect.number != 0){
						sect.fullTime += peek.fullTime;
						sect.selfTime += peek.selfTime;
						sect.count = peek.count;
					}
				}
			}
		}	else {
			section.selfTime = peek.fullTime;
			for(StatisticInfo sect : listInfo) {
				if (sect.sectionName.equals(s1) && !sect.equals(section)) {
					if(section.fullTime == 0){
						diff = section.finish - sect.start;
						section.fullTime = (int)diff;
						section.count = sect.count;
						section.selfTime = section.fullTime - section.selfTime;
					}
					if(sect.number != 0 ){
						sect.count = section.count;
						sect.fullTime += section.fullTime;
						if(sect.count > 1){
							listTest.push(sect);
							if(sect.sectionName.equals(listTest.peek().sectionName)){
								StatisticInfo first = listTest.peek();
								StatisticInfo last = listTest.peekLast();
								if(first.number > last.number){
									listTest.clear();
									listTest.push(sect);
								} else {
									if (first.count == last.count) {
										sect.mark = 0;
										sum = sect.fullTime;
									} else if (first.count < last.count) {
										sect.mark = sum;
										sum = sect.fullTime;
									}
								}
							}
						} else {
							sect.mark = sum;
						}
						sect.selfTime += section.selfTime;
						if(sect.number == 1){
							sect.selfTime = 0;
							sect.mark = 0;
						}
					}
				}
			}
		}
	}
	public static List<StatisticInfo> getStatisticInfo(){
		int self = 0;
		Iterator<StatisticInfo> iter = listInfo.iterator();
		while(iter.hasNext()){
			StatisticInfo next = iter.next();
			if(next.number == 0){
				iter.remove();
			} else {
				if (next.mark != 0)
					next.selfTime = next.fullTime - next.mark;
				self += next.selfTime;
				if (next.number == 1) {
					if (self == next.fullTime)
						next.selfTime = self;
					else
						next.selfTime = next.fullTime - self;
				}
			}
		}
		Comparator<StatisticInfo> comparator = new Comparator<>(){
			@Override
			public int compare(StatisticInfo o1, StatisticInfo o2) {
				return Integer.compare(o1.number, o2.number);
			}
		};
		listInfo.sort(comparator);
		String caption = "name         fullTime     selfTime     count";
		System.out.println(caption);
		for(StatisticInfo sect : listInfo)
			System.out.println(sect.toString());
		return listInfo;
	}
	public static void main(String[] args) {
		enterSection("Process1");
		code(5000);
		enterSection("2");
		code(5000);
		exitSection("2");
		enterSection("3");
		for(int i = 0; i < 2; i++) {
			enterSection("4");
			for (int j = 0; j < 3; j++) {
				enterSection("5");
					enterSection("6");
						enterSection("7");
						code(5000);
						exitSection("7");
					code(5000);
					exitSection("6");
				code(5000);
				exitSection("5");
			}
			code(5000);
			exitSection("4");
		}
		code(5000);
		exitSection("3");
		enterSection("8");
		for(int i = 0; i < 3; i++){
			enterSection("9");
				enterSection("10");
					enterSection("11");
					code(5000);
					exitSection("11");
				code(5000);
				exitSection("10");
			code(5000);
			exitSection("9");
		}
		code(5000);
		exitSection("8");
		enterSection("12");
		code(5000);
		exitSection("12");
		exitSection("Process1");
//		enterSection("Process1");
//		code(11000);
//		exitSection("Process1");
//		enterSection("Process1");
//		code(11000);
//		exitSection("Process1");
//		enterSection("Process1");
//		code(11000);
//		enterSection("Process2");
//		code(11000);
//		enterSection("Process3");
//		code(11000);
//		exitSection("Process3");
//		exitSection("Process2");
//		enterSection("Process2");
//		code(11000);
//		enterSection("Process3");
//		code(11000);
//		exitSection("Process3");
//		exitSection("Process2");
//		enterSection("Process2");
//		code(11000);
//		enterSection("Process3");
//		code(11000);
//		exitSection("Process3");
//		exitSection("Process2");
//		exitSection("Process1");
//		enterSection("Process1");
//		code(11000);
//		exitSection("Process1");
		getStatisticInfo();
	}
}
