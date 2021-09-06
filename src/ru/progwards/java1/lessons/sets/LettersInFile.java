package ru.progwards.java1.lessons.sets;
import java.io.*;
import java.util.*;

public class LettersInFile {
	private String result;
	private String fileName;
	public LettersInFile(String fileName){
		this.fileName = fileName;
		try {
			this.result = process(fileName);
		} catch(Exception e){
			System.out.println(e);
		}
	}
	@Override
	public String toString() {
		if(fileName == null)
			return "Имя файла = null";
		else if(result == null)
			return "";
		else
			return "" + result;
	}
	public static String process(String fileName) throws Exception	{
	  String result = "";
		try(FileReader reader = new FileReader(fileName))	{
			String chars = "";
			List<Character> letters = new ArrayList<>();
			for(int ch; (ch = reader.read()) >= 0;){
				if(Character.isLetter(ch))
					chars += Character.toString(ch);
			}
			chars.toCharArray();
			for (Character ch : chars.toCharArray()){
				letters.add(ch);
			}
			TreeSet<Character> treeSet = new TreeSet<Character>(new Comparator<Character>() {
				@Override
				public int compare(Character ch1, Character ch2) {
					return Character.compare(ch1, ch2);
				}
			});
			treeSet.addAll(letters);
			Iterator<Character> iterator = treeSet.iterator();
			while (iterator.hasNext())
				result += iterator.next();
		} catch (Exception e)	{
			e.getMessage();
			throw e;
		}
		return result;
	}
	public static void main(String[] args) {
			LettersInFile letters = new LettersInFile("inFileLetters.txt");
			System.out.println(letters);
	}
}

