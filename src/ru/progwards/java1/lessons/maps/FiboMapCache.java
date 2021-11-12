package ru.progwards.java1.lessons.maps;
import java.math.BigDecimal;
import java.util.*;
public class FiboMapCache {
	private Map<Integer, BigDecimal> fiboCache;
	public FiboMapCache(boolean cacheOn){
		if(cacheOn == true)
			fiboCache = new HashMap<>();
		else{
			this.clearCahe();
		}
	}
	public BigDecimal fiboNumber(int n){
		BigDecimal a = new BigDecimal("1");
		BigDecimal b = new BigDecimal("1");
		BigDecimal c;
		BigDecimal out = null;
		if(fiboCache != null){
			BigDecimal res = fiboCache.get(n);
			if(res == null){
				for(int i = 3; i <= n; i++){
					c = a.add(b);
					a = b;
					b = c;
				}
				fiboCache.put(n, b);
			} else {
				out  = res;
			}
			out = fiboCache.get(n);
		} else {
			for(int i = 3; i <= n; i++){
				c = a.add(b);
				a = b;
				b = c;
			}
			out = b;
		}
		return out;
	}
	public void clearCahe(){
		fiboCache = null;
	}
	public static void test(){
		String time1 = "fiboNumber cacheOn=true время выполнения ";
		String time2 = "fiboNumber cacheOn=false время выполнения ";
		FiboMapCache fibo1 = new FiboMapCache(true);
		long start = System. currentTimeMillis();;
		for(int i = 1; i <= 1000; i++){
			fibo1.fiboNumber(i);
		}
		time1 += System. currentTimeMillis() - start;
		System.out.println(time1);
		FiboMapCache fibo2 = new FiboMapCache(false);
		start = System. currentTimeMillis();
		for(int i = 1; i <= 1000; i++){
			fibo2.fiboNumber(i);
		}
		time2 += System. currentTimeMillis() - start;
		System.out.println(time2);
	}
	public static void main(String[] args) {
		test();
	}
}
