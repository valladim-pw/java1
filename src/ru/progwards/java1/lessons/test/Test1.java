package ru.progwards.java1.lessons.test;

public class Test1 {
	public static void main(String[] args){
		String str = "System.out.println(Arrays.toString(nums.sieve));";
		String strT = "\t\tSystem.out.println(Arrays.toString(nums.sieve));";
		String strW = "    System.out.println(Arrays.toString(nums.sieve));";
		byte[] strB = str.getBytes();
		int countStrB = strB.length;
		char[] strCh = str.toCharArray();
		int countStrCh = strCh.length;
		String[] strS = str.split("");
		int countStrS = strS.length;
		byte[] strTB = strT.getBytes();
		int countStrTB = strTB.length;
		char[] strTCh = strT.toCharArray();
		int countStrTCh = strTCh.length;
		String[] strTS = strT.split("");
		int countStrTS = strTS.length;
		System.out.println("str.length(): " + str.length());
		System.out.println("bytes.length: " + countStrB);
		System.out.println("chars.length: " + countStrCh);
		System.out.println("split.length: " + countStrS);
		System.out.println("strT.length(): " + strT.length());
		System.out.println("bytesT.length: " + countStrTB);
		System.out.println("charsT.length: " + countStrTCh);
		System.out.println("splitT.length: " + countStrTS);
		System.out.println("2-tabs:      " + strT.length());
		System.out.println("2-tabs trim: " + strT.trim().length());
		System.out.println("4-ws:        " + strW.length());
		System.out.println("4-ws trim:   " + strW.trim().length());
		System.out.println(strT.indexOf("\t"));
	}
}
