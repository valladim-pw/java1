package ru.progwards.java1.lessons.collections;

import java.util.*;

public class ArrayIterator<T> implements Iterator<T> {
	private T[] array;
	public int i;
	ArrayIterator(T[] array) {
		i = 0;
		this.array = array;
	}
	@Override
	public boolean hasNext() {
		if(array != null && i < array.length)
			return true;
		return false;
	}
	@Override
	public T next() {
		return array[i++];
	}
	public static void main(String[] args) {
		Object[] arr = {"String1", 3, "String2", 10.9, "String3", 56, "String4"};
		ArrayIterator arrIterator = new ArrayIterator(arr);
		while(arrIterator.hasNext())
			System.out.println("Value = " + arrIterator.next());
	}
}
