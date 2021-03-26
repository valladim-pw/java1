package ru.progwards.java1.lessons.arrays;

import java.util.Arrays;

public class DIntArray {
	public static void main(String[] args) {
		DIntArray newInt = new DIntArray();
		System.out.println(Arrays.toString(newInt.integers));
		newInt.add(57);
		System.out.println(Arrays.toString(newInt.integers));
		newInt.atInsert(3, 99);
		System.out.println(Arrays.toString(newInt.integers));
		newInt.atDelete(7);
		System.out.println(Arrays.toString(newInt.integers));
		System.out.println(newInt.at(5));
	}
	private int[] integers = {11, -10, 9, 8, 33, 20, -4};
	public DIntArray(){
	}
	public void add(int num){
		int[] copyIntegers = new int[integers.length + 1];
		integers  = Arrays. copyOf(integers, copyIntegers.length);
		integers[integers.length - 1] = num;
	}
	public void atInsert(int pos, int num){
		int intLength;
		int[] copyIntegers;
		if(pos < integers.length)
			intLength = integers.length + 1;
		else
			intLength = pos + 1;
		copyIntegers = new int[intLength];
		integers  = Arrays. copyOf(integers, copyIntegers.length);
		System.arraycopy(integers, pos, copyIntegers, pos + 1, intLength - pos - 1);
		System.arraycopy(copyIntegers, pos, integers, pos, copyIntegers.length - pos);
		integers[pos] = num;
	}
	public void atDelete(int pos){
		if(pos == 0){
			int[] copyIntegers = new int[integers.length];
			System.arraycopy(integers, pos + 1, copyIntegers, pos, integers.length - 1);
			System.arraycopy(copyIntegers, pos, integers, pos, copyIntegers.length);
			integers  = Arrays. copyOf(integers, copyIntegers.length - 1);
		} else {
			int[] copyIntegers = new int[integers.length - 1];
			System.arraycopy(integers, pos, copyIntegers, pos - 1, integers.length - pos);
			System.arraycopy(copyIntegers, pos, integers, pos, copyIntegers.length - pos);
			integers  = Arrays. copyOf(integers, copyIntegers.length);
		}
	}
	public int at(int pos){
		for(int i = 0; i < integers.length; i++){
			if(pos == i)
				return integers[i];
		}
		return -1;
	}
}
