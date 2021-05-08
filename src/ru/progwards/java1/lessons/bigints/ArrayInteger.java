package ru.progwards.java1.lessons.bigints;
import java.util.Arrays;
import java.math.BigInteger;

public class ArrayInteger {
	public byte[] digits;
	ArrayInteger(int n){
		digits = new byte[n];
	}
	public void fromInt(BigInteger value){
		BigInteger res;
		for (int i = 0; i < digits.length; i++) {
			res = value.remainder(BigInteger.TEN);
			value = value.divide(BigInteger.TEN);
			digits[i] = res.byteValueExact();
		}
		//System.out.println(Arrays.toString(digits));
	}
	public BigInteger toInt(){
		String strNum = "";
		for (int i = 0; i < digits.length; i++) {
			strNum = digits[i] + strNum;
		}
		return new BigInteger(strNum);
	}
	public boolean add(ArrayInteger num){
		byte remainder = 0;
		int i;
		if(digits.length >= num.digits.length) {
			for (i = 0; i < num.digits.length; i++) {
				digits[i] += num.digits[i];
				if (digits[i] > 9) {
					remainder = 1;
					digits[i] -= 10;
					digits[i + 1] += remainder;
				}
				if(digits[digits.length - 1] > 9){
					for (i = 0; i < digits.length; i++){
						digits[i] = 0;
					}
					return false;
				}
			}
		} else {
			for (i = 0; i < digits.length; i++){
				digits[i] = 0;
			}
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		ArrayInteger arr1 = new ArrayInteger(4);
		arr1.fromInt(new BigInteger("9999"));
		ArrayInteger arr2 = new ArrayInteger(4);
		arr2.fromInt(new BigInteger("9999"));
		System.out.println(arr1.toInt());
		System.out.println(arr2.toInt());
		System.out.println(arr1.add(arr2));
		System.out.println(Arrays.toString(arr1.digits));
		System.out.println(arr1.toInt());
	}
}
