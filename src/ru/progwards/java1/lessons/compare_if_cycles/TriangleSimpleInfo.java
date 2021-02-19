package ru.progwards.java1.lessons.compare_if_cycles;

public class TriangleSimpleInfo {
	public static void main(String[] args) {
		System.out.println(maxSide(7, 10, 8));
		System.out.println(minSide(5, 6, 4));
		System.out.println(isEquilateralTriangle(2, 2, 3));
		System.out.println(isEquilateralTriangle(2, 3, 2));
		System.out.println(isEquilateralTriangle(3, 2, 2));
		System.out.println(isEquilateralTriangle(2, 2, 2));
		System.out.println(isEquilateralTriangle(3, 3, 3));
	}
	public static int maxSide(int a, int b, int c){
		if(a != b && a != c && b != c){
			if(a > b && a > c)
				return a;
			else if(b > a && b > c)
				return b;
			else
				return c;
		} else if(a == b || a == c || b == c) {
			if(a > b || a > c)
				return a;
			else if(b > a || b > c)
				return b;
			else
				return c;
		} else
			return a;
	}
	public static int minSide(int a, int b, int c) {
		if(a != b && a != c && b != c){
			if(a < b && a < c)
				return a;
			else if(b < a && b < c)
				return b;
			else
				return c;
		} else if(a == b || a == c || b == c) {
			if(a < b || a < c)
				return a;
			else if(b < a || b < c)
				return b;
			else
				return c;
		} else
			return a;
	}
	public static boolean isEquilateralTriangle(int a, int b, int c){
		if(a == b && a == c && b == c)
			return true;
		else
			return false;
	}
}
