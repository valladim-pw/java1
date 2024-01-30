package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class OrderTest {
	public static void main(String[] args) {
		//Path path = Paths.get("");
		Path path = Paths.get("src\\ru\\progwards\\java1\\lessons\\files");
		Path absPath = path.toAbsolutePath();
		System.out.println(absPath.toUri());
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					String files = "";
					String name = file.getFileName().toString();
					files += name;
					System.out.println(name);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e){
					e.printStackTrace();
					return FileVisitResult.CONTINUE;
				}
			});
		}catch (IOException e){
			throw new UncheckedIOException(e);
		}
	}
}
