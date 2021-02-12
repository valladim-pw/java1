package ru.progwards.java1.lessons.basics;

public class ReverseDigits {
	public static void main(String[] args) {
		System.out.println(reverseDigits(123));
		System.out.println(reverseDigits(456));
		System.out.println(reverseDigits(789));
	}
	public static int reverseDigits(int number){
		String s1 = Integer.toString(number % 10);
		String s2 = Integer.toString((number % 100) / 10);
		String s3 = Integer.toString(number / 100);
		
		return Integer.parseInt(s1 + s2 + s3);
	}
}
