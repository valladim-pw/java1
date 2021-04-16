package ru.progwards.java1.lessons.interfaces;

public class CalculateFibonacci {
	public static void main(String[] args) {
		fiboNumber(10);
		System.out.println(getLastFibo());
	}
	public static class CacheInfo {
		public int n;
		public int fibo;
		@Override
		public String toString() {
			return "n:" + lastFibo.n + ", lastFibo:" + lastFibo.fibo;
		}
	}
	private static CacheInfo lastFibo;
	public static CacheInfo getLastFibo(){
		return lastFibo;
	}
	public static void clearLastFibo(){
		lastFibo = null;
	}
	public static int fiboNumber(int n){
		int a = 1;
		Integer b = 1;
		int c;
		for(int i = 0; i < n; i++){
			c = a + b;
			a = b;
			if (i < 2)
				b = 1;
			else
				b = c;
			lastFibo = new CacheInfo();
			lastFibo.n = n;
			lastFibo.fibo = b;
		}
		if(lastFibo.n == n)
			return lastFibo.fibo;
		else
			return b;
	}
}
