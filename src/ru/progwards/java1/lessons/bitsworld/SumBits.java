package ru.progwards.java1.lessons.bitsworld;

public class SumBits {
	public static void main(String[] args) {
		System.out.println(sumBits((byte)-1));
	}
	public static int sumBits(byte value){
		int compare;
		int result = 0;
	  for(int i = 0; i < 8; i++){
	  	compare = value & 1;
			result += compare;
			value >>= 1;
		}
	  return result;
	}
}
