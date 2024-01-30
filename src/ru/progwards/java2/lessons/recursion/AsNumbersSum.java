package ru.progwards.java2.lessons.recursion;

public class AsNumbersSum {
	
	public static String asNumbersSum(int number) {
		return number + findCombination(number - 1, 1, "");
	}
	
	public static String findCombination(int N, int i, String str) {
			String res = "";
			if (N <= 0) {
				return "";
			}
			if (i > N) {
				res = findCombination(N, i - N, str + N + "+") +
								findCombination(N - 1, i + 1, str);
				return res;
			}	else {
				res = "=" + str + N + "+" + i +
								findCombination(i - 1, 1, str + N + "+") +
								findCombination(N - 1, i + 1, str);
				return res;
			}
	}
	
	public static void main(String[] args) {
		
		System.out.println(asNumbersSum(5));
	}
}
