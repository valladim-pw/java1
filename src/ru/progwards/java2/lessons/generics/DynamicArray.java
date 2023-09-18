package ru.progwards.java2.lessons.generics;

import java.util.Arrays;

public class DynamicArray<T> {
	private T[] array;
	private int count;
	
	public DynamicArray() {
		array = (T[]) new Object[1];
		count = 0;
	}
	
	public void add(T t){
		array[count++] = t;
		//System.out.println("arr-0: " + Arrays.toString(array));
		T[] copyArray = (T[])new Object[count * 2];
		//System.out.println("copyAr: " + Arrays.toString(copyArray));
		array = Arrays.copyOf(array, copyArray.length);
		//System.out.println("arr-1: " + Arrays.toString(array));
		//array[array.length - 1] = t;
		//System.out.println("arr-2: " + Arrays.toString(array));
	}
	
	public void insert(int pos, T t){
		int arrLength;
		if(pos < size())
			arrLength = size() + 1;
		else
			arrLength = pos + 1;
		T[] copyArray = (T[])new Object[arrLength];
		//System.out.println("copyAr-0: " + Arrays.toString(copyArray));
		array  = Arrays.copyOf(array, copyArray.length + 1);
		//System.out.println("arr-1: " + Arrays.toString(array));
		//System.out.println("copyAr-1: " + Arrays.toString(copyArray));
		System.arraycopy(array, pos, copyArray, pos + 1, arrLength - pos - 1);
		//System.out.println("arr-2: " + Arrays.toString(array));
		//System.out.println("copyAr-2: " + Arrays.toString(copyArray));
		System.arraycopy(copyArray, pos, array, pos, copyArray.length - pos);
		//System.out.println("arr-3: " + Arrays.toString(array));
		//System.out.println("copyAr-3: " + Arrays.toString(copyArray));
		array[pos] = t;
		//System.out.println("arr-4: " + Arrays.toString(array));
	}
	
	public void remove(int pos){
		if(pos == 0){
			T[] copyArray = (T[])new Object[size()];
			System.arraycopy(array, pos + 1, copyArray, pos, size() - 1);
			System.arraycopy(copyArray, pos, array, pos, copyArray.length);
			array  = Arrays. copyOf(array, copyArray.length - 2);
		} else {
			T[] copyArray = (T[])new Object[size() - 1];
			System.arraycopy(array, pos, copyArray, pos - 1, size() - pos);
			System.arraycopy(copyArray, pos, array, pos, copyArray.length - pos);
			array  = Arrays. copyOf(array, copyArray.length - 1);
		}
	}
	
	public T get(int pos){
		if(pos < count)
			return array[pos];
		return null;
	}
	
	public int size() {
		if(!array.equals(null))
			return array.length;
		return -1;
	}
	
	public static void main(String[] args) {
		DynamicArray<Integer> ints = new DynamicArray<>();
		System.out.println(ints.size());
		ints.add(3);
		ints.add(5);
		ints.add(4);
		System.out.println(Arrays.toString(ints.array));
		System.out.println(ints.size());
		ints.insert(0, 10);
		System.out.println(Arrays.toString(ints.array));
		System.out.println(ints.size());
		ints.remove(0);
		System.out.println(Arrays.toString(ints.array));
		System.out.println(ints.size());
		System.out.println(ints.get(2));
		DynamicArray<String> str = new DynamicArray<>();
		str.add("B");
		str.add("C");
		str.add("D");
		System.out.println(Arrays.toString(str.array));
		str.insert(0, "A");
		System.out.println(Arrays.toString(str.array));
		str.remove(2);
		System.out.println(Arrays.toString(str.array));
		System.out.println(str.get(2));
	}
}
