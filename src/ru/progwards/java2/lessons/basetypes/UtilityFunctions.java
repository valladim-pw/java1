package ru.progwards.java2.lessons.basetypes;

public interface UtilityFunctions {
	static int hash(int key, int capacity) {
		double A = 0.6180339887;
		double d = A * key;
		int result = (int)(capacity * (d - Math.floor(d)));
		return result;
	}
	static int prevPrime(int capacity) {
		for(int i = capacity - 1; i >= 1; i--) {
			int count = 0;
			for(int j = 2; j * j <= i; j++)
				if (i % j == 0)
					count++;
			if(count == 0)
				return i;
		}
		return 3;
	}
	static int nextPrime(int start){
		int nextPrime = 0;
		int limit = 100;
		boolean simple = false;
		int step = start + limit;
		for(int i = start; i < step; i++) {
			int currNum = i;
			for(int j = 2; j < i; j++) {
				if(currNum % j == 0) {
					simple = false;
					break;
				}	else
					simple = true;
			}
			if(simple) {
				nextPrime = currNum;
				break;
			}
			if(i == step - 1)
				step += limit;
		}
    return nextPrime;
  }
	static int getThreshold(int capacity, double loadFactor) {
		double c = Integer.valueOf(capacity).doubleValue();
		Double th = Math.ceil(c + (c * loadFactor));
		return th.intValue();
  }
}