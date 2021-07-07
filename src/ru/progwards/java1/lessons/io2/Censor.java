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
		try(FileReader fileReader = new FileReader(inoutFileName)) {
			RandomAccessFile randomFile = new RandomAccessFile(inoutFileName, "rw");
			for (int ch; (ch = randomFile.read()) >= 0; i++){
				randomFile.seek((long)i);
				for(int j = 0; j < obscene.length; j++){
					char[] word = obscene[j].toCharArray();
					if(ch == word[0]){
						String c = Character.toString(ch);
						c = c.replace(c, "*");
						if(i == 0){
							end = i + word.length;
						} else {
							i--;
							randomFile.seek((long)i);
							String first = Character.toString(randomFile.read());
							if(first.equals(c))
								break;
							end = i + word.length;
						}
						randomFile.seek((long)end);
						char last = (char)randomFile.read();
						if(!Character.isLetter(last)){
							randomFile.seek((long)i);
							while(i < end) {
								randomFile.writeBytes(c);
								i++;
							}
						} else{
							i++;
						}
					}
				}
			}
		}catch (IOException e){
			CensorException ce = new CensorException( inoutFileName, e.getMessage());
			ce.getMessage();
			throw ce;
		}
	}
	
	public static void main(String[] args) {
		String[] words = {"Java", "Oracle", "Sun", "Systems","Microsystems", };
		try{
			censorFile("inoutFile.txt", words);
		}catch(IOException e){
			System.out.println(e);
		}
	}
}