package ru.progwards.java1.lessons.io2;

public class PhoneNumber {
	public static String format(String phone) throws Exception{
		String msg = "В номере должно быть 10 или 11 (если код 7 или 8 впереди) цифр";
	  String source = "";
		String sourceDel = "";
	  String result = "";
	  StringBuilder stringBuilder = new StringBuilder();
		for(char c : phone.toCharArray()){
			if(Character.isDigit(c)) {
				stringBuilder.append(c);
			}
		}
		if((stringBuilder.indexOf("7") == 0 || stringBuilder.indexOf("8") == 0)){
			stringBuilder.delete(0, 1);
			sourceDel = stringBuilder.toString();
		} else {
			source = stringBuilder.toString();
		}
		try{
			if(source.length() == 10 || sourceDel.length() == 10) {
				stringBuilder.insert(0, "+7");
				stringBuilder.insert(2, "(");
				stringBuilder.insert(6, ")");
				stringBuilder.insert(10, "-");
				result = stringBuilder.toString();
			} else {
				int length = source.length() / 0;
			}
		}catch(Exception e){
			e = new Exception(msg);
			throw e;
		}
		return result;
	}
	
	public static void main(String[] args) {
		try{
			System.out.println(format("+n8m, 98 8 111 223 3"));
			System.out.println(format("7999111223"));
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
