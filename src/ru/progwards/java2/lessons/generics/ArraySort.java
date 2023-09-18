package ru.progwards.java2.lessons.generics;

import java.util.Arrays;

public class ArraySort {
	public static<T extends Comparable> void sort(T[] arr) {
		
		for(int i = 0; i < arr.length; i++) {
			T tmp;
			for(int j = i + 1; j < arr.length; j++) {
				if(arr[i].compareTo(arr[j]) == 1) {
					tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Integer[] ints = {9, 2, 4, 1};
		Double[] doubles = {9.0, 2.0, 4.0, 1.0};
		sort(ints);
		sort(doubles);
		System.out.println(Arrays.toString(ints));
		System.out.println(Arrays.toString(doubles));
	}
}
