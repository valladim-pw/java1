package ru.progwards.java1.lessons.io1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Coder {
	public static void toLog(String logName, String msg)	{
		System.out.println(msg);
		try	{
			FileWriter log = new FileWriter(logName, true);
			try	{
				log.write(msg + "\n");
			} finally	{
				log.close();
			}
		} catch (Exception e)	{
			System.out.println(e.getMessage());
		}
	}
	
	public static void codeFile(String inFileName, String outFileName, char[] code, String logName)	{
		try	{
			FileInputStream reader = new FileInputStream(inFileName);
			FileOutputStream writer = new FileOutputStream(outFileName);
			
			try	{
				byte[] bytes = reader.readAllBytes();
				for (int i = 0; i < bytes.length; i++)
					bytes[i] = (byte) code[bytes[i]];
				writer.write(bytes);
				
			} finally	{
				writer.close();
				reader.close();
			}
		} catch (Exception e)	{
			toLog(logName, e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		char[] code = new char[256];
		Arrays.fill(code, '!');
		for (int i = 48; i < 58; i++)
			code[i] = (char) (i + 16);
		codeFile("inFile1.txt", "outFile1.txt", code, "logFile.txt");
	}
}
