package ru.progwards.java1.lessons.arrays;
import java.util.Arrays;

public class Eratosthenes {
	
	public static void main( String[] args ) {
		Eratosthenes nums = new Eratosthenes(1000);
		//System.out.println(Arrays.toString( nums.sieve ));
		System.out.println( nums.isSimple(105));
	}
	private boolean[] sieve;
	public Eratosthenes( int N ){
		sieve = new boolean [N];
		Arrays.fill(sieve, true);
		sift();
	}
	private void sift() {
		sieve[0] = false;
		sieve[1] = false;
		for(int i = 2; i < sieve.length;i++){
			if(sieve[i] == true){
				for(int j = 2; i*j < sieve.length; j++){
					sieve[i * j] = false;
				}
			}
		}
	}
	public boolean isSimple(int n){
		if(n >= 0 && n < sieve.length)
			return sieve[ n ];
		else
			return false;
	}
}
//comment