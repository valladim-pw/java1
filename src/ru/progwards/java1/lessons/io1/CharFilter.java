package ru.progwards.java1.lessons.io1;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CharFilter {
	public static void filterFile(String inFileName, String outFileName, String filter) throws IOException{
		int i;
		String source = "";
		String result = "";
		char[] sourceArr;
		char[] filterArr;
		try {
			FileReader fileReader = new FileReader(inFileName);
			Scanner scanner = new Scanner(fileReader);
			FileWriter fileWriter = new FileWriter(outFileName);
			while (scanner.hasNextLine()) {
				source += scanner.nextLine();
			}
			sourceArr = source.toCharArray();
			filterArr = filter.toCharArray();
			for(i = 0; i < sourceArr.length; i++){
				char c = sourceArr[i];
				char n = (char)0;
				for(int j = 0; j < filterArr.length; j++){
					if(c == filterArr[j]){
						c = n;
						break;
					}
				}
				if(c != n)
					result += c;
			}
			try {
				fileWriter.write(result);
			} finally {
				try {	scanner.close();} catch (Throwable ignored){}
				try {	fileReader.close();} catch (Throwable ignored){}
				try { fileWriter.close();} catch (Throwable ignored){}
			}
		} catch (IOException e){
			e.getMessage();
			throw e;
		}
	}
	public static void main(String[] args) {
		String filter = " -,.()";
		try{
			filterFile("inFile2.txt", "outFile2.txt", filter);
		} catch(IOException e){
			System.out.println(e);
		}
	}
}

