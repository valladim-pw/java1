package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class FindDuplicates {
	public List<List<String>> findDuplicates(String startPath){
		List<Path> fileList = new LinkedList<>();
		List<String> strList = new LinkedList<>();
		List<List<String>> duplicates = new LinkedList<>();
		try{
			Files.walkFileTree(Paths.get(startPath),new SimpleFileVisitor<Path>(){
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
								strList.add(f.toString() + ", " + file.toString());
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
		duplicates.add(strList);
		return duplicates;
	}
	public static void main(String[] args) {
		FindDuplicates fd = new FindDuplicates();
		for(List list : fd.findDuplicates("c:/test/")){
			for(Object str : list)
				System.out.println(str);
		}
	}
}
