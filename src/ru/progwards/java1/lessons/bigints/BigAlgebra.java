package ru.progwards.java1.lessons.bigints;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigAlgebra {
	public static BigDecimal fastPow(BigDecimal num,int pow){
		BigDecimal z = num;
		BigDecimal res = BigDecimal.ONE;
		while(pow > 0){
			int remainder = pow % 2;
			if(remainder == 1){
				res = res.multiply(z);
				z = z.pow(2);
			} else {
				z = z.pow(2);
			}
			pow /= 2;
		}
		return res;
	}
	public static BigInteger fibonacci(int n){
		BigInteger a = BigInteger.ONE;
		BigInteger b = BigInteger.ONE;
		BigInteger c;
		for(int i = 2; i < n; i++){
			c = a.add(b);
			a = b;
			b = c;
		}
		return b;
	}
	public static void main(String[] args) {
		System.out.println(BigAlgebra.fastPow(new BigDecimal("8.23"), 9));
		//21^13 : 154 472 377 739 119 461
		//21^15 : 68 122 318 582 951 682 301
		//15^10 : 576 650 390 625
		//7^20 : 79 792 266 297 612 001
		//54^8 : 72 301 961 339 136
		//8.23^9 : 173 220 192,505318905457564663
		System.out.println(fibonacci(12));
	}
}
