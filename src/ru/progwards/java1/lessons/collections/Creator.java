package ru.progwards.java1.lessons.collections;

import java.util.*;

public class Creator {
	public static Collection<Integer> fillEven(int n){
		Collection<Integer> list = new ArrayList<>();
		for(int i = 2; i <= n * 2; i++){
			if(i % 2 == 0)
				list.add(i);
		}
		return list;
	}
	public static Collection<Integer> fillOdd(int n){
		Collection<Integer> list = new ArrayList<>();
		for (int i = n * 2 - 1; i >= 0; i--)
			if(i % 2 != 0)
				list.add(i);
		return list;
	}
	public static Collection<Integer> fill3(int n){
		Collection<Integer> list = new ArrayList<>();
		List<Integer> triple = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			triple.add(i);
		}
		for(int i = 0; i < n * 3; i++){
			if(i % 3 == 0){
				int id = i;
				for(int j = 0; j < triple.size(); j++){
					if(j == 0)
						triple.set(j, id);
					if(j == 1)
						triple.set(j, id * id);
					if(j == 2)
						triple.set(j, id * id * id);
					list.add(triple.get(j));
				}
			}
		}
		return list;
	}
	public static void main(String[] args) {
		System.out.println(fillEven(14));
		System.out.println(fillOdd(12));
		System.out.println(fill3(3));
	}
}
