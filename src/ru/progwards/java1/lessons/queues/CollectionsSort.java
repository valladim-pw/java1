package ru.progwards.java1.lessons.queues;

import java.util.*;

public class CollectionsSort {
	static final int ELEMENTS_COUNT = 15_000;
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
		System.out.println(list);
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
		System.out.println(list);
	}
	static void collSort(Collection<Integer> data){
		List<Integer> list = new ArrayList<>(data.size());
		list.addAll(0, data);
		Collections.sort(list);
		System.out.println(list);
	}
	public static Collection<String> compareSort(){
		List<String> methods = new ArrayList<>();
		List<String> subList = new ArrayList<>();
		List<Integer> data = new ArrayList<>();
		for (int i = 0; i < ELEMENTS_COUNT; i++) {
			data.add(data.size() / 2, i);
		}
		String method1 = "mySort ";
		String method2 = "minSort ";
		String method3 = "collSort ";
		String method4 = "hisSort";
		String method5 = "logSort";
		String method6 = "downSort";
		String method7 = "beautySort";
		String method8 = "authSort";
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
		method4 += method1.substring(method1.indexOf(" "));
		methods.add(method4);
		method5 += method2.substring(method2.indexOf(" "));
		methods.add(method5);
		method6 += method2.substring(method2.indexOf(" "));
		methods.add(method6);
		method7 += method3.substring(method3.indexOf(" "));
		methods.add(method7);
		method8 += method3.substring(method3.indexOf(" "));
		methods.add(method8);
		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				Double d1 = Double.parseDouble(s1.substring(s1.indexOf(" ")));
				Double d2 = Double.parseDouble(s2.substring(s2.indexOf(" ")));
				return d1.compareTo(d2);
			}
		};
		Collections.sort(methods, comparator);
		//System.out.println(methods);
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
		return methods;
	}
	public static void main(String[] args) {
		Collection<Integer> data = List.of(11, 10, 9, 8, 33, 0, -4);
		mySort(data);
		Collection<Integer> data1 = List.of(-1, 13, -9, 6, 23, 0, 3);
		minSort(data1);
		Collection<Integer> data2 = List.of(13, 20, 5, 81, 3, 0, -7);
		collSort(data2);
		System.out.println(compareSort());
	}
}
