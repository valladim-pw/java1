package ru.progwards.java1.lessons.files;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
public class FilesSelect {
	final static String pattern = "glob:**/*.txt";
	public void selectFiles(String inFolder, String outFolder, List<String> keys){
		try{
			Path inDir = Paths.get(inFolder);
			Path outDir = Paths.get(outFolder);
			PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(pattern);
			Files.walkFileTree(inDir, new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					if(pathMatcher.matches(file)){
						String fileStr = Files.readString(file);
						for(String key : keys){
							if(fileStr.contains(key)){
								Path newInDir = outDir.resolve(key);
								if(!Files.exists(newInDir))
									Files.createDirectory(newInDir);
								Path dstFile = newInDir.resolve(file.getFileName());
								Files.copy(file, dstFile, StandardCopyOption.REPLACE_EXISTING);
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
	}
	public static void main(String[] args) {
		FilesSelect fs = new FilesSelect();
		List<String> list = List.of("property", "conversion");
		fs.selectFiles("c:/in_files_dir/", "c:/out_files_dir", list);
	}
}
