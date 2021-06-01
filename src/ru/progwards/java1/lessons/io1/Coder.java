package ru.progwards.java1.lessons.io1;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Coder {
	public static void codeFile(String inFileName, String outFileName, char[] code, String logName){
		int i = 0;
		int coef;
		int fileLength = 0;
		String msg;
		String result = "";
		try {
			FileWriter logFile = new FileWriter(logName, true);
			try {
				FileReader fileReader = new FileReader(inFileName);
				Scanner scanner = new Scanner(fileReader);
				FileWriter fileWriter = new FileWriter(outFileName, false);
				while (scanner.hasNextLine()) {
					String str = scanner.nextLine();
					fileLength += str.length();
				}
				char[] buf = new char[fileLength];
				if (code.length != 0 && code[0] > 0)
					coef = code[0];
				else
					coef = 15;
				code = new char[buf.length];
				try {
					fileReader = new FileReader(inFileName);
					for (int ch; (ch = fileReader.read()) >= 0 && i < buf.length; i++) {
						if (ch == 10 || ch == 13 || ch == 32) {
							i--;
						} else {
							ch += coef;
							code[i] = (char) ch;
							String c = Integer.toString(code[i]);
							result += c + " ";
						}
					}
					fileWriter.write(result);
				} finally {
					try {	scanner.close();} catch (Throwable ignored){}
					try { fileWriter.close();} catch (Throwable ignored){}
					try {	fileReader.close();} catch (Throwable ignored){}
				}
			} catch (Exception e) {
				msg = e.getMessage();
				try {
					logFile.write(msg + "\n");
				} finally {
					logFile.close();
				}
			}
		} catch (Exception e) {
			msg = e.getMessage();
			System.out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		char[] code = {};
		codeFile("inFile1.txt", "outFile1.txt", code, "logFile.txt");
	}
}
