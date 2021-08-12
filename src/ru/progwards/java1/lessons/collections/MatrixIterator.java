package ru.progwards.java1.lessons.collections;
import java.util.*;

public class MatrixIterator<T> implements Iterator<T> {
	
	private T[][] array;
	public int i;
	public int j;
	MatrixIterator(T[][] array) {
		i = 0;
		j = 0;
		this.array = array;
	}
	@Override
	public boolean hasNext() {
		if(array != null && i < array.length - 1)
			return true;
		if(array[i] != null && j < array[i].length)
			return true;
		return false;
	}
	@Override
	public T next() {
		if(j < array[i].length){
			if(j == 0 && i != 0)
				j++;
			return array[i][j++];
		} else {
			j = 0;
			i++;
			return array[i][j];
		}
	}
	public static void main(String[] args) {
		Integer[] arr1 = {5, 6};
		Double[] arr2 = {13.3, 14.7, 15.0, 16.78, 17.11};
		Integer[] arr3 = {9, 10, 11, 12};
		String[] arr4 = {"String1", "String2", "String3", "String4"};
		Object[][] arr = {arr1, arr2, arr3, arr4};
		MatrixIterator matrixIterator = new MatrixIterator(arr);
		while(matrixIterator.hasNext())
			System.out.println("Value = " + matrixIterator.next());
	}
}
