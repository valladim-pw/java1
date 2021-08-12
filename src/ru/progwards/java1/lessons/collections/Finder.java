package ru.progwards.java1.lessons.collections;
import java.util.*;

public class Finder {
	public static Collection<Integer> findMinSumPair(Collection<Integer> numbers){
		Collection<Integer> ids = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		for(Integer num : numbers){
			list.add(num);
		}
		for(int i = 0; i < list.size() - 1; i++) {
			int sum = list.get(i) + list.get(i + 1);
			list.set(i, sum);
		}
		list.remove(list.size() - 1);
		int i = 0;
		for(Integer val : list){
			if(val == Collections.min(list)){
				ids.add(i);
				ids.add(i + 1);
			}
			i++;
		}
		return ids;
	}
	public static Collection<Integer> findLocalMax(Collection<Integer> numbers){
		Collection<Integer> locMax = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		for(Integer num : numbers){
			list.add(num);
		}
		for(int i = 1; i < list.size() - 1; i++) {
			if(list.get(i) > list.get(i - 1) && list.get(i) > list.get(i + 1))
				locMax.add(list.get(i));
		}
		return locMax;
	}
	public static boolean findSequence(Collection<Integer> numbers){
		List<Integer> list = new ArrayList<>();
		for(Integer num : numbers){
			list.add(num);
		}
		for(int i = 1; i < numbers.size(); i++) {
			if(numbers.contains(list.get(i)))
				return true;
		}
		return false;
	}
	public static String findSimilar(Collection<String> names){
		List<String> list = new ArrayList<>();
		for(String name : names){
			list.add(name);
		}
		list.add("");
		int amount = 1;
		int strVal = 1;
		String strName = list.get(0);
		for(int i = 0; i < list.size() - 1; i++) {
			if(list.get(i).equals(list.get(i + 1))){
				amount += 1;
			} else {
				if(amount > strVal){
					strVal = amount;
					strName = list.get(i);
				}
				amount = 1;
			}
		}
		return strName + ":" + strVal;
	}
	public static void main(String[] args) {
		Collection<Integer> nums = new ArrayList<>();
		nums.add(-10);
		nums.add(5);
		nums.add(4);
		nums.add(8);
		nums.add(7);
		nums.add(12);
		nums.add(1);
		nums.add(-34);
		nums.add(9);
		nums.add(6);
		Collection<String> names = new ArrayList<>();
		names.add("Александр");
		names.add("Василий");
		names.add("Семен");
		names.add("Олег");
		names.add("Олег");
		names.add("Сергей");
		names.add("Вениамин");
		names.add("Иван");
		names.add("Иван");
		System.out.println(findMinSumPair(nums));
		System.out.println(findLocalMax(nums));
		System.out.println(findSequence(nums));
		System.out.println(findSimilar(names));
	}
}
