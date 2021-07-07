package ru.progwards.java1.lessons.io2;

public class Translator {
	private String[] inLang;
	private String[] outLang;
	public Translator(String[] inLang, String[] outLang){
		this.inLang = new String[inLang.length];
		this.outLang = new String[outLang.length];
		for(int i = 0; i < inLang.length;i++){
			this.inLang[i] = inLang[i];
			this.outLang[i] = outLang[i];
		}
		
	}
	public String translate(String sentence){
		String result = "";
		int start;
		int end;
		int enLen;
		int ruLen;
		StringBuilder stringBuilder = new StringBuilder(sentence);
		for(int i = 0; i < inLang.length; i++){
			enLen = inLang[i].length();
			ruLen = outLang[i].length();
			String enWord = inLang[i];
			String enWordUp = Character.toUpperCase(inLang[i].charAt(0)) + inLang[i].substring(1);
			String enWordAllUp = inLang[i].toUpperCase();
			String ruWord = outLang[i];
			String ruWordUp = Character.toUpperCase(outLang[i].charAt(0)) + outLang[i].substring(1);
			String ruWordAllUp = outLang[i].toUpperCase();
			int i1 = stringBuilder.indexOf(enWordUp);
			int i2 = stringBuilder.indexOf(enWordAllUp);
			int i3 = stringBuilder.indexOf(enWord);
			if(i1 != -1 || i2 != -1 || i3 != -1){
				if(i1 != -1){
					start = stringBuilder.indexOf(enWordUp);
					end = start + (ruLen - (ruLen - enLen));
					stringBuilder.replace(start, end, ruWordUp);
				}
				if(i2 != -1){
					start = stringBuilder.indexOf(enWordAllUp);
					end = start + (ruLen - (ruLen - enLen));
					stringBuilder.replace(start, end, ruWordAllUp);
				}
				if(i3 != -1){
					start = stringBuilder.indexOf(enWord);
					end = start + (ruLen - (ruLen - enLen));
					stringBuilder.replace(start, end, ruWord);
				}
				i--;
			}
		}
		result = stringBuilder.toString();
		return result;
	}
	public static void main(String[] args) {
		String[] en = {"mad","world","hello","father","beautiful","dear"};
		String[] rus = {"безумный","мир","привет","папа","прекрасный","дорогой"};
		Translator translator = new Translator(en, rus);
		System.out.println(translator.translate("Hello Mad, mad, mad world - beautiful WORLD! Hello dear FATHER!!!"));
	}
}
