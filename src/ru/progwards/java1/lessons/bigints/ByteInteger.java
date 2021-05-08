package ru.progwards.java1.lessons.bigints;
import java.math.BigInteger;

public class ByteInteger extends AbsInteger {
	Byte number;
	Byte maxVal;
	Byte minVal;
	public ByteInteger(Byte number){
		this.number = number;
	}
	@Override
	public boolean getRange(BigInteger range){
		maxVal = number.MAX_VALUE;
		minVal = number.MIN_VALUE;
		BigInteger bigMax = new BigInteger(maxVal.toString());
		BigInteger bigMin = new BigInteger(minVal.toString());
		int compareMax = range.compareTo(bigMax);
		int compareMin = range.compareTo(bigMin);
		if((compareMax == -1 || compareMax == 0) && (compareMin == 1 || compareMin == 0))
			return true;
		return false;
	}
	public static NumberType getType(){
		numberType = NumberType.BYTE_INT;
		return numberType;
	}
	@Override
	public String toString() {
		return Byte.toString(number);
	}
}
