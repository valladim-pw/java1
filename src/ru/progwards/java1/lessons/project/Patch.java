package ru.progwards.java1.lessons.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class Patch implements Relatable {

	private Path path;
	private Diff diff;
	SrcFileCurrent srcFileCurr;
	LinkedList<Line> patchLinesList;

	public Patch(Diff diff, SrcFileCurrent srcFileCurr, String patchFile) {
		this.diff = diff;
		this.srcFileCurr = srcFileCurr;
		patchLinesList = new LinkedList<>(srcFileCurr.getLinesList());
		compareSrcFileStates();
		path = Paths.get(patchFile.trim());
		applyPatch(path);
	}

	public void compareSrcFileStates() throws RuntimeException {
		
		LinkedList<CompareLine> anchorsList = diff.getAnchorsList();
		LinkedList<Line> processList = new LinkedList<>();
		
		int i = 0;
		int firstIndex = 0;
		int newIndex = 0;
		String anchor = "";
		try {
			while(i < anchorsList.size()) {
				CompareLine patchLn = anchorsList.pollFirst();
				try {
					if(patchLn.hasAnchorLine()) {
						processList.add(patchLn.getAnchorLine());
						anchor = patchLn.getAnchorLine().getLine();
					}	else {
						if(patchLn.hasPatch()) {
							if(!patchLn.getSrcLine().hasEmpty()) {
								newIndex = Relatable.checkForMatchingLinesMarkedWithAnchor(patchLinesList, patchLn, firstIndex);
							}
							if(processList.size() == 1) {
								firstIndex = newIndex;
							}
							Relatable.checkPatchLineForEndAndSetEndToLines(patchLn);
							Relatable.checkPatchLineAndAddLinesToProcessList(patchLn, processList);
							Relatable.checkProcessListEndAndTransferToPatchList( processList, patchLinesList, firstIndex);
						}
					}
				} catch(RuntimeException rte) {
					String anch = anchor;
					PatchConflictException pce = new PatchConflictException(anch);
					pce.getMessage();
					throw pce;
				}
			}
		} catch(RuntimeException rte) {
			throw rte;
		}
		Relatable.checkAndChangePatchListNumbersOrder(patchLinesList);
		Relatable.printList(patchLinesList);
	}

	public LinkedList<Line> getPatchLinesList() {
		return patchLinesList;
	}
	
	public void applyPatch(Path path) {
		try (BufferedWriter bfw = Files.newBufferedWriter(path, CREATE, WRITE, TRUNCATE_EXISTING)) {
			String line = "";
			String sign = " /* + */";
			int count = 1;
			for(Line ln : getPatchLinesList()) {
				line = ln.getLine();
				if(ln.hasSign())
					line += sign;
				bfw.write(line, 0, line.length());
				if(count < getPatchLinesList().size())
					bfw.newLine();
				count++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static class PatchConflictException extends RuntimeException {
		public String className;
		public String anchor;
		public PatchConflictException(String anchor) {
			className = this.getClass().getName();
			this.anchor = anchor;
		}

		@Override
		public String getMessage() {
			if(className.indexOf("$") != -1 )
				className = className.substring(className.indexOf("$") + 1);
			return className + ": " +
							"Lines marked with this anchor \"" + anchor + "\" have already been changed.\n" +
							"Patch implementation is not possible.";
		}

		@Override
		public String toString() {
			return getMessage();
		}
	}
	
	public static void main(String[] args) {
		/*
		* Создаются экземпляры класса ProcessFile
		* srcFile - исходный файл
		* pushFile - файл, который вносит изменения в исходный файл, которые пока в этом файле не зафиксированы
		* Выводятся в виде упорядоченных пронумерованных списков строк файлов.
		* Создается экземпляр класса SrcFileCurrent
		* srcFileCurr - исходный файл в текущем состоянии, уже получивший зафиксированные в нем изменения из других источников
		* Выводится также в виде упорядоченного пронумерованного списка строк файла.
		 */
		ProcessFile srcFile = new ProcessFile("srcFile.java");
		ProcessFile pushFile = new ProcessFile("pushFile.java");
		SrcFileCurrent srcFileCurr = new SrcFileCurrent("srcFileCurr.java");
		
		try {
			/*
			* При создании экемпляра Diff	выводится объединенный список построчного сравнения
			* списков строк объектов исходного файла (srcFile)
			 * и файла который вносит пока не зафиксированные изменения в исходный файл (pushFile).
			 * В этот сравнительный список внедрены анкоры, которые обозначают места (патчи), где строки исходного
			 * файла (со знаком "-" ) должны быть заменены строками файла вносящего изменения (со знаком "+")
			 * Также создается файл (в данном случае diffFile.txt) куда записывается объединенный список построчного сравнения.
			*/
			System.out.println("DiffList with Anchors:");
			Diff diff = new Diff(srcFile, pushFile, "diffFile.txt");
			
			System.out.println("----------------------------------------");
			/*
			* При создании экземпляра Patch:
			* - В случае успешной проверки выводится список строк объекта srcFileCurr с уже внедренными строками объекта pushFile (со знаком "+") из патчей,
			* а также создается новый файл (в данном случае patchFile.java) куда записывается исходный файл в текущем состоянии с внедренными патчами.
			* Строки из патча в файле помечены комментами со знаком "+".
			* - В случае если при проверке выяснится, что строки объекта srcFile из патча, подлежащие замене строками объекта pushFile,
			* уже изменены в списке объекта srcFileCurr выбрасывается пользовательское исключение PatchConflictException.
			*/
			System.out.println("PatchList with Signs:");
			Patch patch = new Patch(diff, srcFileCurr,"patchFile.java");
			
		} catch(RuntimeException rte) {
			System.out.println(rte);
		}
	}
}
