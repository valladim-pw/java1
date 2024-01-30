package ru.progwards.java1.lessons.bitsworld;

public class CheckBit {
	public static void main(String[] args) {
		System.out.println(checkBit((byte)10, 7));
	}
	public static int checkBit(byte value, int bitNumber){
		int compare;
		int result = -1;
		for(int i = 0; i < 8; i++){
			compare = value & 1;
			if(bitNumber == i)
				return compare;
			value >>= 1;
		}
		return result;
	}
}
