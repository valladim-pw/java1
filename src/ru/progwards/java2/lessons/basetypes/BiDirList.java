package ru.progwards.java2.lessons.basetypes;
import java.util.*;

public class BiDirList<T> implements Iterable<T> {
	
	public class ListItem<T> {
		private T item;
		private ListItem<T> next;
		private ListItem<T> prev;
		
		ListItem(T item) {
			this.item = item;
		}
		T getItem() {
			return item;
		}
		void setNext(ListItem<T> item) {
			next = item;
		}
		public ListItem<T> getNext() {
			return next;
		}
		void setPrev(ListItem<T> item) {
			prev = item;
		}
		public ListItem<T> getPrev() {
			return prev;
		}
		
		@Override
		public String toString() {
			return "" + item;
		}
	}
	public class ListIterator<T> implements Iterator<T> {
		BiDirList<T>.ListItem<T> current;
	
		public ListIterator(BiDirList<T> list) {
			current = list.getStart();
		}
		public boolean hasNext() {
			return current != null;
		}
		public T next() {
			T value = current.getItem();
			current = current.getNext();
			return value;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	private ListItem<T> start;
	private ListItem<T> end;
	private T[] arr;
	int size = 0;
	
	public Iterator<T> iterator() {
		return new ListIterator(this);
	}
	
	public ListItem<T> getStart() {
		return start;
	}
	
	public ListItem<T> getEnd() {
		return end;
	}
	
	public void addLast(T item) {
		ListItem<T> el = new ListItem<T>(item);
		if(start == null) {
			start = el;
			end = el;
		} else {
			end.setNext(el);
			end = el;
		}
	}
	
	public void addFirst(T item) {
		ListItem<T> el = new ListItem<T>(item);
		if(start == null) {
			start = el;
			end = el;
		} else {
			start.setPrev(el);
			el.setNext(start);
			start = el;
		}
	}
	
	public int size() {
		size = 0;
		for(T item : this)
			size++;
		return size;
	}
	
	public T at(int i) {
		int c = 0;
		for(T value : this) {
			if(c == i)
				return value;
			c++;
		}
		return null;
	}
	
	public void remove(T item) {
		BiDirList<T> list = this;
		BiDirList<T> copyList = new BiDirList<T>();
		for(T value : this) {
			if(!value.equals(item)) {
				copyList.addLast(value);
				start = null;
			}
		}
		for(T value : copyList)
			list.addLast(value);
	}
	
	public static<T> BiDirList<T> from(T[] array) {
		BiDirList<T> list = new BiDirList<T>();
		for(T elem: array) {
			list.addLast(elem);
		}
		return list;
	}
	
	public static<T> BiDirList<T> of(T...array) {
		return from(array);
	}
	
	public void toArray(T[] array) {
		int sz = size();
		int arrLen = array.length;
		arr = (T[])new Object[arrLen + sz];
		ListItem<T> current = this.getStart();
		int i = 0;
		for(T elem : arr) {
			if(i < arrLen)
				arr[i] = array[i];
			else {
				arr[i] = current.getItem();
				current = current.getNext();
			}
			i++;
		}
	}
	
	public T[] copyInArr(T[] array) {
		this.toArray(array);
		return arr;
	}
	
	static<T> void print(BiDirList<T> list) {
		LinkedList<T> ls = new LinkedList<>();
		BiDirList<T>.ListItem<T> current = list.getStart();
		while(current != null) {
			ls.add(current.getItem());
			current = current.getNext();
		}
		System.out.println(ls);
	}
	
	public static void main(String[] args) {
		BiDirList<Integer> bdl = new BiDirList<>();
		bdl.addLast(2);
		bdl.addLast(10);
		bdl.addFirst(20);
		bdl.addFirst(22);
		bdl.addLast(11);
		bdl.addFirst(23);
		print(bdl);
		bdl.remove(20);
		print(bdl);
		
		BiDirList<String> bdl2 = new BiDirList<>();
		bdl2.addLast("second");
		bdl2.addLast("ten");
		bdl2.addFirst("twenty");
		bdl2.addFirst("twenty two");
		bdl2.addLast("eleven");
		
		//print(bdl2);
		bdl2.remove("second");
		//print(bdl2);
		
		Integer[] ints = {16, 15, 19, 18};
		//print(from(ints));
		System.out.println("array1: " + Arrays.toString(ints));
		//ints = new Integer[5];
		//System.out.println("array2: " + Arrays.toString(ints));
		bdl.toArray(ints);
		System.out.println("array2: " + Arrays.toString(bdl.copyInArr(ints)));
	}
}
