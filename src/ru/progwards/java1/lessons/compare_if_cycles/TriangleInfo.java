package ru.progwards.java1.lessons.compare_if_cycles;

public class TriangleInfo {
	public static void main(String[] args) {
		System.out.println(isTriangle(2,2,3));
		System.out.println(isTriangle(2,3,4));
		System.out.println(isTriangle(3,2,5));
		System.out.println(isTriangle(2,3,6));
		System.out.println(isTriangle(3,3,9));
		System.out.println(isIsoscelesTriangle(2,2,3));
		System.out.println(isIsoscelesTriangle(2,3,2));
		System.out.println(isIsoscelesTriangle(3,2,3));
		System.out.println(isIsoscelesTriangle(2,2,2));
		System.out.println(isIsoscelesTriangle(3,3,3));
	}
	public static boolean isTriangle(int a, int b, int c){
		if ((a < b + c) && (b < a + c) && (c < a + b))
			return true;
		else
			return false;
	}
	public static boolean isRightTriangle(int a, int b, int c){
		
		int quadA = a * a;
		int quadB = b * b;
		int quadC = c * c;
		int sumAB = a * a + b * b;
		int sumAC = a * a + c * c;
		int sumCB = c * c + b * b;
		if(sumAB == quadC || sumAC == quadB || sumCB == quadA)
			return true;
		else
			return false;
	}
	public static boolean isIsoscelesTriangle(int a, int b, int c){
		if(a == b || b == c || a == c)
			return true;
		else
			return false;
	}
}
