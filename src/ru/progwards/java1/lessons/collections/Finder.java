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
		list.sort(null);
		boolean answer = true;
		for(int i = 0; i < list.size() - 1; i++) {
			if(list.get(i + 1) - list.get(i) != 1){
				answer = false;
				break;
			}
		}
		return answer;
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
		Integer[] arrNums = {18,1,18,14,13,5,2,16,10,16,12,6,16,14,8,6,17,6,2};
		for(int i = 0; i < arrNums.length; i++){
			nums.add(arrNums[i]);
		}
		Collection<String> names = new ArrayList<>();
		String[] arrNames = {"Александр","Василий","Семен","Олег","Олег","Сергей","Вениамин","Иван","Иван"};
		for(int i = 0; i < arrNames.length; i++){
			names.add(arrNames[i]);
		}
		System.out.println(findMinSumPair(nums));
		System.out.println(findLocalMax(nums));
		System.out.println(findSequence(nums));
		System.out.println(findSimilar(names));
	}
}
