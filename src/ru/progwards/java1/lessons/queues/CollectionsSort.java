package ru.progwards.java1.lessons.queues;

import java.util.*;

public class CollectionsSort {
	static final int ELEMENTS_COUNT = 5_000;
	public static void mySort(Collection<Integer> data) {
		List<Integer> list = new ArrayList<>(data.size());
		list.addAll(0, data);
		for(int i = 0; i < list.size(); i++){
			Integer num ;
			for(int j = i + 1; j < list.size(); j++){
				if(Integer.compare(list.get(i), list.get(j)) == 1){
					num = list.get(i);
					list.set(i, list.get(j));
					list.set(j, num);
				}
			}
		}
		data.clear();
		for(Integer val : list){
			data.add(val);
		}
	}
	public static void minSort(Collection<Integer> data){
		List<Integer> list = new ArrayList<>(data.size());
		List<Integer> listCopy = new ArrayList<>();
		list.addAll(0, data);
		int i = 0;
		while(!list.isEmpty()){
			listCopy.add(i, Collections.min(list));
			list.remove(list.indexOf(Collections.min(list)));
			i++;
		}
		list.addAll(0, listCopy);
		data.clear();
		for(Integer val : list){
			data.add(val);
		}
	}
	public static void collSort(Collection<Integer> data){
		List<Integer> list = new ArrayList<>(data.size());
		list.addAll(0, data);
		Collections.sort(list);
		data.clear();
		for(Integer val : list){
			data.add(val);
		}
	}
	public static Collection<String> compareSort(){
		Collection<String> result = new ArrayList<>();
		List<String> methods = new ArrayList<>();
		List<String> subList = new ArrayList<>();
		Collection<Integer> data = new ArrayList<>();
		List<Integer> dataList = new ArrayList<>();
		for (int i = 0; i < ELEMENTS_COUNT; i++) {
			dataList.add(dataList.size() / 2, i);
		}
		for(Integer i : dataList){
			data.add(i);
		}
		String method1 = "mySort ";
		String method2 = "minSort ";
		String method3 = "collSort ";
		String last = " -1";
		int end;
		int begin;
		long start = System. currentTimeMillis();
		mySort(data);
		method1 += (double)(System.currentTimeMillis() - start) / 1000;
		methods.add(method1);
		start = System. currentTimeMillis();
		minSort(data);
		method2 += (double)(System.currentTimeMillis() - start) / 1000;
		methods.add(method2);
		start = System. currentTimeMillis();
		collSort(data);
		method3 += (double)(System.currentTimeMillis() - start) / 1000;
		methods.add(method3);
		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				Double d1 = Double.parseDouble(s1.substring(s1.indexOf(" ")));
				Double d2 = Double.parseDouble(s2.substring(s2.indexOf(" ")));
				return d1.compareTo(d2);
			}
		};
		Collections.sort(methods, comparator);
		System.out.println(methods);
		methods.add(last);
		for(int i = 0; i < methods.size() - 1; i++){
			if(comparator.compare(methods.get(i), methods.get(i + 1)) != 0){
				methods.set(i, methods.get(i).substring(0, methods.get(i).indexOf(" ")));
			} else {
				subList.add(methods.get(i));
				do{
					i++;
					subList.add(methods.get(i));
				}	while(comparator.compare(methods.get(i), methods.get(i + 1)) == 0);
				subList.sort(null);
				end = i + 1;
				begin = end - subList.size();
				i = begin;
				for(int j = 0; j < subList.size(); j++){
					methods.set(i, subList.get(j).substring(0, subList.get(j).indexOf(" ")));
					i++;
				}
				i--;
				subList.clear();
			}
		}
		methods.remove(methods.size() - 1);
		for(String str : methods){
			result.add(str);
		}
		return result;
	}
	public static void main(String[] args) {
		Collection<Integer> data = new ArrayList<>();
		data.add(87);
		data.add(77);
		data.add(20);
		data.add(93);
		data.add(6);
		data.add(56);
		data.add(75);
		mySort(data);
		System.out.println(data);
		Collection<Integer> data1 = new ArrayList<>();
		data1.add(62);
		data1.add(83);
		data1.add(66);
		data1.add(80);
		data1.add(56);
		data1.add(84);
		minSort(data1);
		System.out.println(data1);
		Collection<Integer> data2 = new ArrayList<>();
		data2.add(90);
		data2.add(7);
		data2.add(56);
		data2.add(50);
		data2.add(52);
		data2.add(1);
		data2.add(77);
		data2.add(33);
		data2.add(82);
		collSort(data2);
		System.out.println(data2);
		System.out.println(compareSort());
	}
}
