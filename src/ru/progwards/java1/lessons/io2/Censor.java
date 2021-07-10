package ru.progwards.java1.lessons.io2;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Censor {
	public static class CensorException extends IOException {
		public String fileName = "";
		public String msg = "";
		public CensorException(String fileName, String msg){
			super(msg);
			this.msg = msg;
			this.fileName = fileName;
		}
		@Override
		public String getMessage() {
			msg = msg.substring(msg.indexOf("("), msg.indexOf(")"));
			msg = msg.substring(1);
			return fileName == null ? "" :  "\"" + fileName  + "\""  + " : " + msg;
		}
		@Override
		public String toString() {
			return "\"" + fileName  + "\"" + " : " + msg;
		}
	}
	public static void censorFile(String inoutFileName, String[] obscene) throws IOException{
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
		} catch (IOException e){
			CensorException ce = new CensorException( inoutFileName, e.getMessage());
			ce.getMessage();
			throw ce;
		}
	}
	public static void main(String[] args) {
		String[] words = { "Sun", "Java", "Oracle", "Microsystems"};
		try{
			censorFile("inoutFile.txt", words);
		}catch(IOException e){
			System.out.println(e);
		}
	}
}