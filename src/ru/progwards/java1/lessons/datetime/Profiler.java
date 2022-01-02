package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.util.*;

public class Profiler {
	public static List<StatisticInfo> listInfo = new LinkedList<>();
	public static void code(int elementsCount){
		List<Integer> arrayList = new ArrayList();
		for (int i = 0; i < elementsCount; i++) {
			arrayList.add(0, i);
		}
	}
	public static void enterSection(String name){
		long start = Instant.now().toEpochMilli();
		StatisticInfo section = new StatisticInfo();
		section.start = start;
		section.sectionName = name;
		listInfo.add(listInfo.size(), section);
		if(listInfo.size() > 1){
			for(StatisticInfo info : listInfo){
				if(info.start != 0){
					String s1 = section.sectionName;
					String s2 = info.sectionName;
					if(s1.equals(s2) && !section.equals(info))
						section.count++;
				}
			}
		}
	}
	public static void exitSection(String name){
		long finish = Instant.now().toEpochMilli();
		long diff = 0;
		StatisticInfo section = new StatisticInfo();
		section.finish = finish;
		section.sectionName = name;
		listInfo.add(listInfo.size(), section);
		for(StatisticInfo info : listInfo){
			String s1 = section.sectionName;
			String s2 = info.sectionName;
			if(s1.equals(s2) && !section.equals(info)){
				section.count = 0;
				diff = section.finish - info.start;
				section.fullTime = (int)diff;
			}
		}
		StatisticInfo next;
		ListIterator<StatisticInfo> iter1 = listInfo.listIterator(listInfo.size() - 2);
		next = iter1.next();
		if(iter1.hasNext()){
			int self = next.selfTime;
			if(section.sectionName.equals(next.sectionName)){
				section.selfTime = (int)diff;
				section.wrapper = false;
			} else {
				section.selfTime = self;
			}
		}
		ListIterator<StatisticInfo> iter2 = listInfo.listIterator();
		while(iter2.hasNext()){
			next = iter2.next();
			if(section.sectionName.equals(next.sectionName) && !section.equals(next)){
				iter2.remove();
				section.count += next.count;
				section.fullTime += next.fullTime;
				if(section.wrapper == false)
					section.selfTime += next.selfTime;
			}
		}
		ListIterator<StatisticInfo> iter3 = listInfo.listIterator(listInfo.size() - 2);
		next = iter3.next();
		int full = section.fullTime;
		int fullNext = next.fullTime;
		int self = next.selfTime;
		int res = 0;
		if(iter3.hasNext()){
			if(section.selfTime == next.selfTime && next.wrapper == false){
				res = full - self;
				section.selfTime = res;
			} else if(section.selfTime == next.selfTime && next.wrapper == true){
				res = full - fullNext;
				section.selfTime = res;
			}
		}
	}
	public static List getStatisticInfo(){
		Comparator<StatisticInfo> comparator = new Comparator<>(){
			@Override
			public int compare(StatisticInfo o1, StatisticInfo o2) {
				return Integer.compare(Integer.parseInt(o1.sectionName), Integer.parseInt(o2.sectionName));
			}
		};
		int self = 0;
		for(int i = 0; i < listInfo.size(); i++){
			StatisticInfo sect = listInfo.get(i);
			StatisticInfo sectStart = listInfo.get(listInfo.size() - 1);
			sectStart.selfTime = 0;
			self += sect.selfTime;
			sectStart.selfTime = sectStart.fullTime - self;
		}
		listInfo.sort(comparator);
		String caption = "name fullTime selfTime count";
		System.out.println(caption);
		for(StatisticInfo section : listInfo){
			System.out.println(section.toString());
		}
		return listInfo;
	}
	public static void main(String[] args) {
		enterSection("1");
		code(5000);
		enterSection("2");
		code(5000);
		exitSection("2");
		enterSection("3");
		code(5000);
		for(int i = 0; i < 12; i++){
			enterSection("4");
		 		enterSection("5");
					enterSection("6");
					code(5000);
					exitSection("6");
					code(5000);
				exitSection("5");
				code(5000);
			exitSection("4");
		}
		exitSection("3");
		for(int i = 0; i < 22; i++){
			enterSection("7");
				enterSection("8");
					enterSection("9");
					code(5000);
					exitSection("9");
				code(5000);
				exitSection("8");
			code(5000);
			exitSection("7");
		}
		enterSection("10");
		code(5000);
		exitSection("10");
		code(5000);
		exitSection("1");
		getStatisticInfo();
	}
}
