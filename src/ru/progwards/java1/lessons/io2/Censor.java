package ru.progwards.java1.lessons.io2;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Censor {
	public static class CensorException extends Exception {
		public String fileName = "";
		public String msg = "";
		public String className = "";
		public CensorException(String fileName, String msg){
			super(msg);
			this.msg = msg;
			this.fileName = fileName;
			className = this.getClass().getName();
		}
		@Override
		public String getMessage() {
			if(msg != null && msg.indexOf("(") != -1  ){
				msg = msg.substring(msg.indexOf("("), msg.indexOf(")"));
				msg = msg.substring(1);
			}
			if(msg == null){
				msg = "File name is null";
			}
			if(className.indexOf("$") != -1 ){
				className = className.substring(className.indexOf("$") + 1);
			}
			return className + " : " + fileName == null ? "null" :  "\"" + fileName  + "\""  + " : " + msg;
		}
		@Override
		public String toString() {
			return className + " : " + "\"" + fileName  + "\"" + " : " + msg;
		}
	}
	public static void censorFile(String inoutFileName, String[] obscene) throws Exception{
		int i = 0;
		int end;
		int start;
		try(FileReader fileReader = new FileReader(inoutFileName)) {
			RandomAccessFile randomFile = new RandomAccessFile(inoutFileName, "rw");
			String compare = "";
			String stars = "";
			int ch;
			while((ch = randomFile.read()) >= 0) {
				if (Character.isLetter(ch)) {
					if(i == 0){
						i++;
						randomFile.seek((long) i);
					}
					String read = Character.toString(ch);
					String c;
					c = read.replace(read, "*");
					compare += read;
					stars += c;
				} else {
					if(compare != ""){
						end = i;
						start = end - compare.length();
						for(int j = 0; j < obscene.length; j++) {
							if(compare.equals(obscene[j])){
								if(start > 0){
									i--;
									end = i;
									start = end - compare.length();
									randomFile.seek((long)start);
								} else {
									randomFile.seek((long)start);
								}
								randomFile.writeBytes(stars);
							}
						}
					}
					compare = "";
					stars = "";
				}
				i++;
			}
		} catch (Exception e){
			CensorException ce = new CensorException( inoutFileName, e.getMessage());
			ce.getMessage();
			throw ce;
		}
	}
	public static void main(String[] args) {
		String[] words = { "Sun", "Java", "Oracle", "Microsystems"};
		try{
			censorFile("inoutFile.txt", words);
		}catch(Exception e){
			System.out.println(e);
		}
	}
}