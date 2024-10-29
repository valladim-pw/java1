package ru.progwards.java2.lessons.gc;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class HeapTest {
	
	static int maxSize = 0;
	static final int maxSmall = 10;
	static final int maxMedium = 100;
	static final int maxBig = 1000;
	static final int maxHuge = 10000;
	static int minSize = 0;
	static int allocated = 0; //
	static int frequencyDefragCoeff = 0;
	static int repeatStrategy = 1;
	static int freeIterationsNumber = 0;
	// Служебные свойства для более наглядного отражения процесса тестирования
	static int countStrategy = 1;
	static String testProcess = "Test Process:\n";
	
	public HeapTest(int repStrategy,
	                int mxSize,
	                int mnSize,
	                int frequencyDefragCoef,
	                int freeIteratesNum) {
		// В аргументах конструктора задаются параметры цикла тестирования,
		// которые влияют на время работы алгоритмов стратегий тестирования:
		repeatStrategy = repStrategy; // количество повторений каждой стратегии в цикле
		maxSize = mxSize; // максимальный размер кучи
		minSize = mnSize;	// минимальный размер кучи
		// коэффициент, определяющий частоту дефрагментации в стратегии с периодической дефрагментацией
		frequencyDefragCoeff = frequencyDefragCoef;
		// количество итераций в цикле освобождения памяти в тестовом цикле
		freeIterationsNumber = freeIteratesNum;
	}
	
	/* Класс, определяющий параметры блока памяти для тестирования */
	
	static class Block {
		public int start;
		public int size;
		
		public Block(int start, int size) {
			this.start = start;
			this.size = size;
		}

//		@Override
//		public String toString() {
//			String block = " i=" + start + ", size=" + size + ":\n [ BB ]";
//			for (int i = 0; i < size - 1; i++) {
//				block += "[ BB ]";
//			}
//			return block + "\n";
//		}
	}
	
	/* Класс хранящий результаты цикла тестирования стратегии */
	
	static class TestResult {
		String strategyName; // Наименование стартегии
		long averageTime; // Общее среднее время работы стратегии
		long allocTime; // Время выделения памяти в общем времени
		long freeTime; // Время освобождения памяти в общем времени
		long freeCycles; // Количество циклов освобождения памяти
		long allocCycles; // Количество циклов выделения памяти
		
		public TestResult(String strategyName,
		                  long averageTime,
		                  long allocTime,
		                  long freeTime,
		                  long allocCycles,
		                  long freeCycles) {
			this.strategyName = strategyName;
			this.averageTime = averageTime;
			this.allocTime = allocTime;
			this.freeTime = freeTime;
			this.allocCycles = allocCycles;
			this.freeCycles = freeCycles;
		}
	}
	
	/* Метод генерирования и классификации случайных чисел
	имитирующх входные данные с размерами блоков для выделения памяти */
	
	static int getRandomSize() {
		int n = Math.abs(ThreadLocalRandom.current().nextInt() % 10);
		int size = Math.abs(ThreadLocalRandom.current().nextInt());
		
		if (n < 6)
			size %= maxSmall;
		else if (n < 8)
			size %= maxMedium;
		else if (n < 9)
			size %= maxBig;
		else
			size %= maxHuge;
		return size > (maxSize - allocated) - 1 ? (maxSize - allocated) / 2 + 1 : size + 1;
	}
	
	/* Метод определяющий результат тестирования каждой стратегии заданное параметром
	конструктора тестового класса количество раз (repeatStrategy)
	возвращающий результат тестирования для каждой стратегии с усредненными данными */
	
	private TestResult testStrategy(String name, Heap.Strategy strategy) {
		long totalTime = 0;
		long fullAllocTime = 0;
		long fullFreeTime = 0;
		long allAllocCycles = 0;
		long allFreeCycles = 0;
		
		for (int i = 0; i < repeatStrategy; i++) {
			long params[] = performMemoryOperations(strategy);
			
			totalTime += params[0];
			fullAllocTime += params[1];
			fullFreeTime += params[2];
			allAllocCycles += params[3];
			allFreeCycles += params[4];
		}
		
		long averageTime = TimeUnit.NANOSECONDS.toMillis(totalTime / repeatStrategy);
		long averageAllocTime = TimeUnit.NANOSECONDS.toMillis(fullAllocTime / repeatStrategy);
		long averageFreeTime = TimeUnit.NANOSECONDS.toMillis(fullFreeTime / repeatStrategy);
		long averageAllocCycles = allAllocCycles / repeatStrategy;
		long averageFreeCycles = allFreeCycles / repeatStrategy;
		
		return new TestResult(name,
						averageTime,
						averageAllocTime,
						averageFreeTime,
						averageAllocCycles,
						averageFreeCycles);
	}
	
	/* Метод для переопределения индексов блоков тестовой очереди в случае если
	в процессе тестирования стратегии использовалась компактизация */
	
	static void compactBlocks(Collection<Block> blocks) {
		int destination = 0; // Текущая позиция для копирования данных
		// Сдвигаем все занятые блоки к началу
		for (Block block : blocks) {
			block.start = destination; // Обновляем начало блока
			destination += block.size; // Смещаемся на следующий свободный адрес
		}
		Heap.possibleCompact = true;
	}
	
	/* Метод в котором в тестовых целях имитируется цикл операций с памятью (выделение и освобождение) в куче
	до момента заполнения памяти выделенными блоками до установленного минимального уровня (minSize).
	При этом могут использоваться различные стратегии выделения и освобождения памяти. */
	
	private static long[] performMemoryOperations(Heap.Strategy strategy) {
		long params[] = new long[5];
		// Инициируем новую кучу с максимальным размером, который можно задать
		// в параметре конструктора класса TestHeap
		Heap heap = new Heap(maxSize);
		allocated = 0;
		// Для операций с блоками используем очередь
		ArrayDeque<Block> blocks = new ArrayDeque<>();
		
		long allocCycles = 0;
		long freeCycles = 0;
		long allocTime = 0;
		long freeTime = 0;
		
		// Начало отсчета общего времени тестового цикла
		long start = System.nanoTime();
		// alloc and free 30% random
		
		// Цикл опреаций выделения и освобождения памяти продолжается
		// пока размер выделенных блоков меньше минимального размера minSize, который можно задать
		// в параметре конструктора класса TestHeap
		while ((maxSize - allocated) > minSize) {
			long lstart, lstop;
			// Получаем сгенерированный размер выделенного блока
			int size = getRandomSize();
			
			allocated += size;
			// Подсчитываем количество циклов выделения памяти
			allocCycles++;
			// Начало отсчета времени для выделения памяти
			lstart = System.nanoTime();
			// Получаем индекс выделенного блока используя определенные
			// параметрами стратегию, размер выделенного блока, коэффициент частоты
			// дефрагментации и значение флага, определяющего выполнение цикла освобождения
			int ptr = heap.getStrategy(size,
							strategy,
							frequencyDefragCoeff,
							Heap.doneFree);
			// Конец отсчета времени для выделения памяти
			lstop = System.nanoTime();
			// Компактизируем блоки в очереди
			// если состоялась компактизация выделенных блоков кучи
			if (!Heap.possibleCompact)
				compactBlocks(blocks);
			// Подсчет времени для выделения памяти
			allocTime += lstop - lstart;
			// Добавление в тестовую очередь выделенного блока
			blocks.offer(new Block(ptr, size));
			
			int n = Math.abs(ThreadLocalRandom.current().nextInt() % 25);
			// Условие для начала цикла освобождения памяти (генерируется также случайным образом)
			if (n == 0) {
				// Изменения флага из класса кучи, показывающее, что выполнен цикл освобождения памяти
				Heap.doneFree = true;
				// Подсчет циклов освобождения памяти в тестовом цикле
				freeCycles++;
				// Цикл освобождения памяти
				for (int i = 0; i < freeIterationsNumber; i++) {
					// Блок в тестовой очереди при освобождении удаляется
					Block block = blocks.poll();
					if (block == null)
						break;
					// Начало времени для освобождения памяти
					lstart = System.nanoTime();
					// В куче выделенный блок заменяется освобожденным
					// по указателю удаляемого из очереди блока
					heap.free(block.start);
					// Конец времени для освобождения памяти
					lstop = System.nanoTime();
					// Подсчет времени для освобождения памяти
					freeTime += lstop - lstart;
					// Уменьшаем размер выделенной памяти
					allocated -= block.size;
				}
				//blocks.remove(n);
			}
			//n = Math.abs(ThreadLocalRandom.current().nextInt() % 100000);
			//System.out.println("n2 = ThreadLocalRandom.current().nextInt() % 10: " + n);
			//if (n == 0)
			//System.out.println(maxSize - allocated);
		}
		// Конец и подсчет общего времени тестового цикла
		long stop = System.nanoTime();
		long totalTime = stop - start;
		// Вывод строки с наименованием стратегии и количеством
		// ее повторений на консоль в процессе тестирования
		if (countStrategy == 1) {
			System.out.print(" >>" + strategy + ".." + countStrategy);
			// Эта же строка для записи в файл
			testProcess += " >>" + strategy + ".." + countStrategy;
		} else {
			System.out.print(".." + countStrategy);
			testProcess +=  ".." + countStrategy;
		}
		countStrategy++;
		if (countStrategy > repeatStrategy)
			countStrategy = 1;
		
		params[0] = totalTime;
		params[1] = allocTime;
		params[2] = freeTime;
		params[3] = allocCycles;
		params[4] = freeCycles;
		// Метод возвращает массив с временными и количественными параметрами тестового цикла
		return params;
	}
	
	/* Метод запускает тестовый цикл в рамках параметров заданных конструктором тестового класса
	и формирует таблицу с временными и количественными показателями стратегий	выделения и освобождения памяти,
  отсортированную по общему среднему времени выполнения алгоритма стратегии */
	
	public String getLeaderBoard() throws IOException {
		List<TestResult> results = new ArrayList<>();
		String board = "";
		String result = "";
		// Тестируем каждую стратегию
		results.add(testStrategy("Sort by Address", Heap.Strategy.SORT_BY_ADDRESS));
		results.add(testStrategy("Sort by Size", Heap.Strategy.SORT_BY_SIZE));
		results.add(testStrategy("Sort by Two Indexes", Heap.Strategy.SORT_BY_TWO_INDEXES));
		results.add(testStrategy("Compact Periodically", Heap.Strategy.PERIODIC_COMPACT));
		results.add(testStrategy("Defrag Periodically", Heap.Strategy.PERIODIC_DEFRAG));
		// Сортируем результаты по среднему времени выполнения
		results.sort((r1, r2) -> Long.compare(r1.averageTime, r2.averageTime));
		// Выводим лидерборд
		board += "\n\nPerformance Leaderboard \n(Strategy repeat: " + repeatStrategy +
						         ", Heap max size: " + maxSize +
						         ", Heap min size: " + minSize +
						         ", Defrag periodically coeff: " + frequencyDefragCoeff +
						         ", Number of iterations in free cycle: " + freeIterationsNumber +
						         "):\n\n";
		for (int i = 0; i < results.size(); i++) {
			result = String.format("%d. %s - Average Time: %d ms " + //Allocate Cycles: %d, " +
							                       "(Allocate Time: %d ms, " + //Average Free Cycles: %d, " +
							                       "Free Time: %d ms), " +
							                       "Average Allocate Cycles: %d, " +
							                       "Average Free Cycles: %d%n",
							i + 1,
							results.get(i).strategyName,
							results.get(i).averageTime,
							results.get(i).allocTime,
							results.get(i).freeTime,
							results.get(i).allocCycles,
							results.get(i).freeCycles) ;
			board += result;
		}
		board += "\n\n";
		writeHeapTestInFile(board);
		testProcess = "Test Process:\n";
		return board;
	}
	
	/* Метод для записи результатов всех тестов в файл */
	
	public void writeHeapTestInFile(String board) throws IOException {
		int countTest = 0;
		FileWriter fileWriter = null;
		File file = new File("heaptests.txt");
		if (!file.exists())
			file.createNewFile();
		FileReader reader = new FileReader("heaptests.txt");
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String str = scanner.nextLine();
			if (str.contains("Leaderboard"))
				countTest++;
		}
		reader.close();
		Date date = new Date();
		String time = (countTest + 1) + ") " + date.toString() + "\n";
		
		fileWriter = new FileWriter(file, true);
		fileWriter.write(time);
		fileWriter.write(testProcess);
		fileWriter.write(board);
		
		if (fileWriter != null)
			fileWriter.close();
	}
	
	public static void main(String[] args) throws InvalidPointerException, OutOfMemoryException, IOException {
		System.out.println(new HeapTest(
						2,
						150735283,
						50000,
						60,
						3).getLeaderBoard());
	}
}
