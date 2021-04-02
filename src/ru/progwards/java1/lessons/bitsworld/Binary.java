package ru.progwards.java1.lessons.bitsworld;

public class Binary {
	public static void main(String[] args) {
		Binary num1 = new Binary((byte)127);
		System.out.println(num1.toString());
	}
	
	byte num ;
	public Binary(byte num){
		this.num = num;
	}
	@Override
	public String toString(){
		int compare;
		String result = "";
		for(int i = 0; i < 8; i++){
			compare = num & 1;
			result = Integer.toString(compare) + result;
			num >>= 1;
		}
		if(result.isEmpty())
			return "0";
		return result;
	}
}
