package ru.progwards.java2.lessons.recursion;

import java.util.*;

public class HanoiTower {
	
	int size;
	int pos;
	int fromRod;
	int toRod;
	List<Integer> availPos = List.of(0, 1, 2);
	int posSum = availPos.stream().reduce(0,(a, x) -> a + x);
	boolean on;
	String ring;
	String rod = "  I   ";
	String base = ("").concat("=").repeat(17);
	static LinkedList<Integer> sizes = new LinkedList<>();
	static List<String> tower = new ArrayList<>();
	static TreeSet<String> towerSet = new TreeSet<>();
	
	public HanoiTower(int size, int pos) {
		this.size = size;
		this.pos = pos;
		ring = "<" + String.valueOf((double)size / 1000).substring(2) + "> ";
		sizes.add(size);
		if(size > 0)
			towerSet.add(this.toString());
	}
	
	public void move(int from, int to) {
		if(size == 0) {
			return;
		}
		int inter = posSum - from - to;
		new HanoiTower(size - 1, pos).move(from, inter);
		if(sizes.getLast() == 0) {
			fromRod = from;
			toRod = to;
			if(sizes.size() == sizes.getFirst() + 1)
				printBasicTower();
			on = true;
			setTrace(on);
		}
		new HanoiTower(size - 1, pos).move(inter, to);
	}
	
	public void setTrace(boolean on) {
		if(on)
			print();
	}
	
	public void print() {
		replaceFrom(fromRod);
		replaceTo(toRod);
		tower.forEach(System.out::println);
		System.out.println(base);
	}
	
	@Override
	public String toString() {
		switch(pos) {
			case 1 :
				return rod + ring + rod;
			case 2 :
				return rod + rod + ring;
			default:
				return ring + rod + rod;
		}
	}
	
	public void printBasicTower() {
		tower.addAll(towerSet);
		tower.forEach(System.out::println);
		System.out.println(base);
	}
	
	public int strPos(int pos) {
		switch(pos) {
			case 1 :
				return ring.length();
			case 2 :
				return ring.length()*2;
			default:
				return 0;
		}
	}
	
	public void replaceFrom(int from) {
		for(int i = 0; i < tower.size(); i++) {
			StringBuilder sb = new StringBuilder();
			String str = tower.get(i);
			if(str.substring(strPos(from), strPos(from) + ring.length()).equals(ring)) {
				sb.append(str);
				sb.replace(strPos(from), strPos(from) + ring.length(), rod);
				tower.set(i, sb.toString());
				break;
			}
		}
	}
	
	public void replaceTo(int to) {
		for(int i = tower.size() - 1; i >= 0; i--) {
			StringBuilder sb = new StringBuilder();
			String str = tower.get(i);
			if(str.substring(strPos(to), strPos(to) + 1).equals(" ")) {
				sb.append(str);
				sb.replace(strPos(to), strPos(to) + ring.length(), ring);
				tower.set(i, sb.toString());
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		HanoiTower tower = new HanoiTower(4, 0);
		tower.move(0, 1);
	}
}