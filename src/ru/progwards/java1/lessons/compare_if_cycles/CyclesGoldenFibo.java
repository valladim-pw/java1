package ru.progwards.java1.lessons.compare_if_cycles;

public class CyclesGoldenFibo {
	static final double GOLD_MIN = 1.61703;
	static final double GOLD_MAX = 1.61903;
	public static void main(String[] args) {
		for(int i = 1; i <= 15; i++){
			System.out.println(i + ": " + fiboNumber(i));
		}
		System.out.println("Сторона меньше основания");
		for(int i = 2; i < 15; i++){
			String res = "";
			int side = fiboNumber(i);
			int base = fiboNumber(i + 1);
			if(base > 1 && base < 100){
				for(int j = 0; j < i;j++) {
					if (isGoldenTriangle(side, side, base) == true)
						res =  "Cторона: " + side + " Основание: " + base;
					else if (isGoldenTriangle(base, side, side) == true)
						res =  "Cторона: " + side + " Основание: " + base;
					else
						res =  "Золотой треугольник не найден";
				}
				System.out.println(res);
			}
		}
		System.out.println("Сторона больше основания");
		for(int i = 3; i < 16; i++){
			String res = "";
			int side = fiboNumber(i);
			int base = fiboNumber(i - 1);
			if(side > 1 && side < 100){
				for(int j = 0; j < i;j++) {
					if (isGoldenTriangle(side, side, base) == true)
						res =  "Cторона: " + side + " Основание: " + base;
					else if (isGoldenTriangle(base, side, side) == true)
						res =  "Cторона: " + side + " Основание: " + base;
					else
						res =  "Золотой треугольник не найден";
				}
				System.out.println(res);
			}
		}
		System.out.println(containsDigit(892340356, 2));
	}
	public static boolean containsDigit(int number, int digit){
		int i = 0;
		int a = 10;
		int b = 1;
		int res;
		do{
			i++;
			res = (number % a) / b;
			a *= 10;
			b *= 10;
			if(digit == res)
				return true;
		} while(b < number);
		return false;
	}
	public static int fiboNumber(int n){
		int a = 1;
		int b = 1;
		int c;
		for(int i = 2; i < n; i++){
			c = a + b;
			a = b;
			if (i < 2)
				b = 1;
			else
				b = c;
		}
		return b;
	}
	public static boolean isGoldenTriangle(int a, int b, int c){
		double ad = a;
		double bd = b;
		double cd = c;
		double ratioAC = ad / cd;
		double ratioBA = bd / ad;
		double ratioCB = cd / bd;
		if(a == b && ratioAC >= GOLD_MIN && ratioAC <= GOLD_MAX)
		  return  true;
		else if(b == c && ratioBA >= GOLD_MIN && ratioBA <= GOLD_MAX)
			return true;
		else if(c == a && ratioCB >= GOLD_MIN && ratioCB <= GOLD_MAX)
		  return true;
		else
			return false;
	}
}
