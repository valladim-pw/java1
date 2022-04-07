package ru.progwards.java1.lessons.arrays;

import java.util.Arrays;

public class ArraySort {
	public static void main(String[] args) {
		int[] arr = {11, 10, 9, 8, 33, 0,-4};
		sort(arr);
	}
	public static void sort(int[] a){
		for(int i = 0; i < a.length; i++){
			int num;
			for(int j = i + 1; j < a.length; j++){
				if(a[i] > a[j]){
					num = a[i];
					a[i] = a[j];
					a[j] = num;
					//System.out.println(num);
				}
			}
		}
		System.out.println(Arrays.toString(a));
	}
}
