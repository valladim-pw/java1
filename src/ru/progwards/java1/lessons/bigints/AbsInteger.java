package ru.progwards.java1.lessons.bigints;

import java.math.BigInteger;

public class AbsInteger {
	public BigInteger number;
	static NumberType numberType;
	enum NumberType{
		BYTE_INT,
		SHORT_INT,
		INTEGER_INT,
		BIG_INT
	}
	public static NumberType getType(){
		numberType = NumberType.BYTE_INT;
		switch(numberType){
			case BYTE_INT:
				numberType = NumberType.BYTE_INT;
				break;
			case SHORT_INT:
				numberType = NumberType.SHORT_INT;
				break;
			case INTEGER_INT:
				numberType = NumberType.INTEGER_INT;
				break;
		}
		return numberType;
	}
	public static NumberType getBigIntType(){
		numberType = NumberType.BIG_INT;
		return numberType;
	}
	public boolean getRange(BigInteger range){
		return false;
	}
	public static AbsInteger add(AbsInteger num1, AbsInteger num2){
		BigInteger x = new BigInteger(num1.toString());
		BigInteger y = new BigInteger(num2.toString());
		AbsInteger sum = new AbsInteger();
		BigInteger result = x.add(y);
			switch (getType()) {
				case BYTE_INT:
					sum = new ByteInteger(Byte.MAX_VALUE);
					if (sum.getRange(result)) {
						sum = new ByteInteger(result.byteValueExact());
						System.out.println(ByteInteger.getType() + ": " +  sum.toString());
						return sum;
					}
				case SHORT_INT:
					sum = new ShortInteger(Short.MAX_VALUE);
					if (sum.getRange(result)){
						sum = new ShortInteger(result.shortValueExact());
						System.out.println(ShortInteger.getType() + ": " + sum.toString());
						return sum;
					}
				case INTEGER_INT:
					sum = new IntInteger(Integer.MAX_VALUE);
					if(sum.getRange(result)){
						sum = new IntInteger(result.intValueExact());
						System.out.println(IntInteger.getType() + ": " + sum.toString());
						return sum;
					}
				default:
					System.out.println(getBigIntType() + ": " + result.toString());
			}
			return sum;
	 }
	@Override
	public String toString() {
		return number.toString();
	}
	public static void main(String[] args) {
		ByteInteger b = new ByteInteger((byte)-57);
		ByteInteger b2 = new ByteInteger((byte)100);
		ShortInteger s = new ShortInteger((short)32767);
		IntInteger i = new IntInteger( -2147483647);
		AbsInteger a = new AbsInteger();
		a.number = new BigInteger("2147483697");
		a.add(b2, b);
		b.add(s, b);
		add(i, b);
		add(b, a);
	}
}
