package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class FindDuplicates {
	public List<List<String>> findDuplicates(String startPath){
		LinkedList<Path> fileList = new LinkedList<>();
		List<String> strList = new LinkedList<>();
		TreeSet<String> fileSet = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s2.compareTo(s1);
			}
		});
		List<List<String>> duplicates = new LinkedList<>();
		try{
			Path dir = Paths.get(startPath);
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					fileList.add(file);
					Path fileName = file.getFileName();
					FileTime fileTime = attrs.lastModifiedTime();
					long fileSize = attrs.size();
					byte[] fileContent = Files.readAllBytes(file);
					for(Path f : fileList){
						if(f.getFileName().equals(fileName) && !f.equals(file)){
							if(Files.getLastModifiedTime(f).equals(fileTime) &&
											Files.size(f) == fileSize &&
											Arrays.equals(Files.readAllBytes(f), fileContent)){
								fileSet.add(f.getFileName() + f.toString()  + ", " + file.toString());
							}
						}
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{
					if (e == null) {
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch(SecurityException se){
			System.out.println(se);
		} catch(IOException e){
			System.out.println(e);
		}
		for(String str : fileSet){
			str = str.substring(str.indexOf(":") - 1);
			strList.add(str);
		}
		duplicates.add(strList);
		return duplicates;
	}
	public static void main(String[] args) {
		FindDuplicates fd = new FindDuplicates();
		fd.findDuplicates("c:/test_dir/");
		for(List list : fd.findDuplicates("c:/test_dir/")){
			for(Object str : list)
				System.out.println(str + "\n");
		}
	}
}
