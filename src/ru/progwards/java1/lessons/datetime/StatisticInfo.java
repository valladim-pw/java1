package ru.progwards.java1.lessons.datetime;

import java.util.*;

public class StatisticInfo{
	public String sectionName;
	public int fullTime;
	public int selfTime;
	public int count;
	public long start;
	public long finish;
	boolean wrapper;
	public StatisticInfo(String name) {
		super();
		this.sectionName = name;
		this.fullTime = 0;
		this.selfTime = 0;
		this.count = 1;
		this.start = 0;
		this.finish = 0;
		this.wrapper = true;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StatisticInfo section = (StatisticInfo) o;
		return fullTime == section.fullTime &&
						selfTime == section.selfTime &&
						count == section.count &&
						start == section.start &&
						finish == section.finish &&
						wrapper == section.wrapper &&
						Objects.equals(sectionName, section.sectionName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(sectionName, fullTime, selfTime, count, start, finish, wrapper);
	}
	
	@Override
	public String toString() {
		List<String> list = new ArrayList<>();
		String result = "";
		String line = "";
		String n = sectionName;
		String f = "" + fullTime;
		String s = "" + selfTime;
		String c = "" + count;
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
		result = line;
		line = "";
		list.clear();
		return result;
	}
}
