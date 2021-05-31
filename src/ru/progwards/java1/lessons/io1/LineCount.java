package ru.progwards.java1.lessons.io1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LineCount {
	public static int calcEmpty(String fileName){
		int count = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			Scanner scanner = new Scanner(fileReader);
			try {
				while (scanner.hasNextLine()) {
					String str = scanner.nextLine();
					if(str.length() == 0)
						count++;
				}
			} finally {
				try {fileReader.close();} catch (Throwable ignored){}
				try {scanner.close();} catch (Throwable ignored){}
			}
		} catch (IOException e){
			return -1;
		}
		return count;
	}
	public static void main(String[] args) {
		System.out.println(calcEmpty("inFile1.txt"));
	}
}
