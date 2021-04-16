package ru.progwards.java1.lessons.interfaces;
import java.util.Arrays;

public class ArraySort {
	public static void sort(CompareWeight[] a){
		for(int i = 0; i < a.length; i++){
			CompareWeight obj;
			for(int j = i + 1; j < a.length; j++){
				if(a[i].compareWeight(a[j]) == CompareWeight.CompareResult.GREATER){
					obj = a[i];
					a[i] = a[j];
					a[j] = obj;
				}
			}
		}
		System.out.println(Arrays.toString(a));
	}
}
