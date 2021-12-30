package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.util.*;

public class Profiler {
	public static List<StatisticInfo> listInfo = new ArrayList<>();
	public static class StatisticInfo{
		public String sectionName;
		public int fullTime = 0;
		public int selfTime = 0;
		public int count;
		public long start = 0;
		public long finish = 0;
		boolean wrapper = true;
	}
	public static void code(int elementsCount){
		List<Integer> arrayList = new ArrayList();
		for (int i = 0; i < elementsCount; i++) {
			arrayList.add(0, i);
		}
	}
	public static void enterSection(String name){
		long start = Instant.now().toEpochMilli();
		StatisticInfo section = new StatisticInfo();
		section.count = 1;
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
		ListIterator<StatisticInfo> iter1 = listInfo.listIterator(listInfo.size() - 2);
		StatisticInfo next = iter1.next();
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
			StatisticInfo next2 = iter2.next();
			if(section.sectionName.equals(next2.sectionName) && !section.equals(next2)){
				iter2.remove();
				section.count += next2.count;
				section.fullTime += next2.fullTime;
				if(section.wrapper == false)
					section.selfTime += next2.selfTime;
			}
		}
		ListIterator<StatisticInfo> iter3 = listInfo.listIterator(listInfo.size() - 2);
		StatisticInfo next3 = iter3.next();
		int full = section.fullTime;
		int fullNext = next3.fullTime;
		int self = next3.selfTime;
		int res = 0;
		if(iter3.hasNext()){
			if(section.selfTime == next3.selfTime && next3.wrapper == false){
				res = full - self;
				section.selfTime = res;
			} else if(section.selfTime == next3.selfTime && next3.wrapper == true){
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
		List<String> list = new ArrayList<>();
		List<String> result = new ArrayList<>();
		for(StatisticInfo sect : listInfo){
			String line = "";
			String n = sect.sectionName;
			String f = Integer.valueOf(sect.fullTime).toString();
			String s = Integer.valueOf(sect.selfTime).toString();
			String c = Integer.valueOf(sect.count).toString();
			list.add(list.size(), "name ".replace("name ".substring(0, n.length()), n));
			list.add(list.size(), "fullTime ".replace("fullTime ".substring(0, f.length()), f));
			list.add(list.size(), "selfTime ".replace("selfTime ".substring(0, s.length()), s));
			list.add(list.size(), "count ".replace("count ".substring(0, c.length()), c));
			for(int i = 0; i < list.size(); i++){
				String str = list.get(i);
				for(Character ch : str.toCharArray()){
					if(Character.isLetter(ch)){
						str = str.replace(Character.toString(ch)," " );
					}
				}
				line += str;
			}
			result.add(result.size(), line);
			line = "";
			list.clear();
		}
		for(String str : result){
			System.out.println(str);
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
		for(int i = 0; i < 23; i++){
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
		for(int i = 0; i < 13; i++){
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
