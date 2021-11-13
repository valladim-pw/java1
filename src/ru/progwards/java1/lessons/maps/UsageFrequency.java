package ru.progwards.java1.lessons.maps;
import java.io.*;
import java.util.*;
public class UsageFrequency {
	private List<Character> list = new ArrayList<>();
	public static class WrongLoadFile extends Exception {
		public String fileName = "";
		public String msg = "";
		public String className = "";
		public WrongLoadFile(String fileName, String msg){
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
			if(className.indexOf("$") != -1 ){
				className = className.substring(className.indexOf("$") + 1);
			}
			return msg;
		}
		@Override
		public String toString() {
			String nullStr = "file name is null";
			if(msg == null)
				msg = nullStr;
			return className + ": " + "\"" + fileName + "\""  + " -> " + msg;
		}
	}
	public void processFile(String fileName) throws Exception {
		try(FileReader reader = new FileReader(fileName))	{
			Scanner scanner = new Scanner(reader);
			while(scanner.hasNextLine()){
				String string = scanner.nextLine();
				if(!string.isBlank()){
					for (char c : string.toCharArray())
						if (Character.isDigit(c) || Character.isAlphabetic(c))
							list.add(c);
						else
							list.add(Character.forDigit(32, 10));
					list.add(Character.forDigit(32, 10));
				}
			}
		} catch (Exception e){
			WrongLoadFile wf = new WrongLoadFile( fileName, e.getMessage());
			wf.getMessage();
			throw wf;
		}
	}
	public Map<Character, Integer> getLetters(){
		Map<Character, Integer> letters = new TreeMap<>();
		for(Character ch : list){
			Character key = ch;
			Integer count = 1;
			Integer newInt = 0;
			if(Character.isLetterOrDigit(key)){
				Integer oldInt = letters.putIfAbsent(key, count);
				if(oldInt != null){
					newInt = oldInt + count;
					letters.replace(key, newInt);
				}
			}
		}
		return letters;
	}
	public Map<String, Integer> getWords(){
		Map<String, Integer> words = new TreeMap<>();
		StringBuilder strBuilder = new StringBuilder();
		String word = "";
		for(Character ch : list){
			if(Character.isLetterOrDigit(ch)){
				strBuilder.append(ch);
				word = strBuilder.toString();
			} else {
				if(word != ""){
					String key = word;
					Integer count = 1;
					Integer newInt = 0;
					Integer oldInt = words.putIfAbsent(key, count);
					if(oldInt != null){
						newInt = oldInt + count;
						words.replace(key, newInt);
					}
				}
				strBuilder = strBuilder.replace(0, strBuilder.length(), "");
				word = "";
			}
		}
		return words;
	}
	public static void main(String[] args) {
		UsageFrequency usageFreq = new UsageFrequency();
		try{
			usageFreq.processFile("wiki.train.tokens");
			System.out.println(usageFreq.getLetters());
			System.out.println(usageFreq.getWords());
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
