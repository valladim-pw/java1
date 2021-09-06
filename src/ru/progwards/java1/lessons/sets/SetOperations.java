package ru.progwards.java1.lessons.sets;
import java.util.*;

public class SetOperations {
	public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2){
		HashSet unionSet = new HashSet(set1);
		unionSet.addAll(set2);
		return unionSet;
	}
	public static Set<Integer> intersection(Set<Integer> set1, Set<Integer> set2){
		HashSet interSet = new HashSet(set1);
		interSet.retainAll(set2);
		return interSet;
	}
	public static Set<Integer> difference(Set<Integer> set1, Set<Integer> set2){
		HashSet interSet = new HashSet(set1);
		HashSet diffSet = new HashSet(set1);
		interSet.retainAll(set2);
		diffSet.removeAll(interSet);
		return diffSet;
	}
	public static Set<Integer> symDifference(Set<Integer> set1, Set<Integer> set2){
		HashSet unionSet = new HashSet(set1);
		unionSet.addAll(set2);
		HashSet interSet = new HashSet(set1);
		interSet.retainAll(set2);
		unionSet.removeAll(interSet);
		return unionSet;
	}
	public static void main(String[] args) {
		Set<Integer> intSet1 = Set.of(1, 2, 3, 4, 5, 6, 7);
		Set<Integer> intSet2 = Set.of(3, 4, 5, 6, 7, 8);
		SetOperations set = new SetOperations();
		System.out.println(set.union(intSet1, intSet2));
		System.out.println(set.intersection(intSet1, intSet2));
		System.out.println(set.difference(intSet1, intSet2));
		System.out.println(set.symDifference(intSet1, intSet2));
	}
}
