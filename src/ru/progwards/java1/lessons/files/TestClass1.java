//Задача 1, класс FindDuplicates
/*package ru.progwards.java1.lessons.files;

import java.io.ProcessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestClass1 {
	// массив с данными для процессинга результата теста
	// 0: идентификатор теста, - имя функции @Test
	// 1: имя теста словами, если == "", то возмется идентификатор
	// 2: баллы за эту часть теста
	// 3: * отмечены обязательные части теста
	private static String[][] testData = {
					// Первая строка [0] содержит данные о всем тесте.
					// Оценка отражает проходной балл
					//       - это информация для утилиты проверки
					
					{"task1", "Задача 1, класс FindDuplicates", "15", ""},
					{"test1", "Метод findDuplicates(String startPath)", "15", "*"},
	};
	
	private String sDir = "/data/test_files/ru.progwards.java1.lessons.files/1";
	private String dir = TestClass1.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "tmp/1";
	
	private String conditions = "В целевом каталоге находятся файлы file1 и file2.\n"
					+ "Идентичные им файлы находятся в подкаталогах file1: dir1 и dir2/dir3, file2: dir2.\n"
					+ "В подкаталоге dir1 находится файл file3, идентичный которому файл находится в подкаталоге dir2/dir3.\n"
					+ "В подкаталоге dir2 находится файл file1, который отличается от ранее упомянутого файла file1 только содержимым, но не размером и временем.\n"
					+ "В подкаталоге dir1 находится файл file2, который отличается от ранее упомянутого файла file2 только временем.\n";
	
	// Если надо - инициализация и завершение теста
	@Before
	public void init() {
		try {
			FileUtils.deleteDirectory(new ProcessFile(dir + "/.."));
		} catch (IOException e) {}
		
		ProcessFile d = new ProcessFile(dir);
		d.mkdirs();
		
		try {
			FileUtils.copyDirectory(new ProcessFile(sDir), d);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void done() {
	}
	
	@Test
	public void test1() {
		
		
		FindDuplicates fd = new FindDuplicates();
		
		List<List<String>> lr = findDuplicates(dir);
		List<List<String>> lu = fd.findDuplicates(dir);
		
		boolean b = compare(lr, lu);
		
		String us = getString(lu);
		String rs = getString(lr);
		
		Assert.assertTrue("Метод работает неправльно.\n"
						+ conditions
						+ "Возвращены списки строк, содержащие:\n" + us
						+ "Ожидалось:\n" + rs, b);
		
	}
	
	private List<String> added = new ArrayList<>();
	
	public List<List<String>> findDuplicates(String startPath){
		List<List<String>> result = new ArrayList<>();
		
		ProcessFile dir = new ProcessFile(startPath);
		
		Set<String> files = allFiles(dir);
		
		for (String s : files){
			if (added.contains(s)) continue;
			
			Set<String> ls = new HashSet<>();
			ProcessFile f = new ProcessFile(s);
			
			ls.add(f.getAbsolutePath());
			
			for (String s1 : files){
				if (s.equals(s1)) continue;
				ProcessFile f1 = new ProcessFile(s1);
				
				try {
					if (f.getName().equals(f1.getName()) && f.lastModified() == f1.lastModified() && FileUtils.contentEquals(f, f1)){
						ls.add(f1.getAbsolutePath());
						added.add(f1.getAbsolutePath());
					}
				} catch (IOException e) {}
				
			}
			
			if (ls.size() > 1){
				List<String> sl = new ArrayList<>();
				sl.addAll(ls);
				result.add(sl);
			}
		}
		
		return result;
	}
	
	private Set<String> allFiles(ProcessFile dir){
		Set<String> result = new HashSet<>();
		for (String s : dir.list())
		{
			ProcessFile f = new ProcessFile(dir + "/" + s);
			if (f.isDirectory()) result.addAll(allFiles(f));
			else result.add(f.getAbsolutePath());
		}
		return result;
	}
	
	private String getString(List<List<String>> ll)
	{
		String result = "";
		for (List<String> l : ll){
			if (l.isEmpty()) result += "<пустой список>";
			for (int n = 0; n < l.size(); n++){
				result += l.get(n).replace(dir + "/", "target_dir_path/").replace(".txt", "");
				if (n < (l.size() - 1)) result += ", ";
			}
			result += "\n";
		}
		
		return result;
	}
	
	private boolean compare(List<List<String>> l1, List<List<String>> l2){
		
		for (List<String> l : l1){
			Collections.sort(l);
		}
		
		for (List<String> l : l2){
			Collections.sort(l);
		}
		
		if (l1.size() != l2.size() || !l1.containsAll(l2)) return false;
		
		return true;
	}
	
	// это обязательная функция, она возвращает данные теста утилите
	public static String[][] getData() {
		return testData;
	}
}
*/