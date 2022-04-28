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
		TreeSet<String> fileSet = new TreeSet<>();
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
								fileSet.add( f.toString()  + ", " + file.toString());
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
			strList.add(str);
		}
		duplicates.add(strList);
		return duplicates;
	}
	public static void main(String[] args) {
		FindDuplicates fd = new FindDuplicates();
		System.out.println(fd.findDuplicates("c:/test_dir/"));
	}
}