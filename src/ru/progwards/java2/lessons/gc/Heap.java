package ru.progwards.java2.lessons.gc;

import java.util.*;

/* Пользовательское исключение OutOfMemoryException */
class OutOfMemoryException extends RuntimeException {
	public OutOfMemoryException(String message) {
		super(message);
	}
}

/* Пользовательское исключение InvalidPointerException */
class InvalidPointerException extends RuntimeException {
	public InvalidPointerException(String message) {
		super(message);
	}
}

/*
Класс Heap, имитирующий работу сборщика мусора
 */
public class Heap {
	
	static byte[] bytes; // Массив, представляющий кучу
	private static List<FreeBlock> freeBlocks; // Список свободных блоков
	private static List<AllocatedBlock> allocatedBlocks; // Список выделенных блоков
	static boolean possibleCompact = true;  // Флаг, регулирующий компактизацию между разными методами
	static boolean doneFree = false; // Флаг, указывающий состоялся ли процесс освобождения блоков
	static int countDefrag = 0;// Служебный счетчик процедур дефрагментации
	
	public Heap(int maxHeapSize) {
		this.bytes = new byte[maxHeapSize]; // Инициализация кучи заданного размера
		this.freeBlocks = new ArrayList<>();
		this.allocatedBlocks = new ArrayList<>();
		// Вначале вся куча является одним свободным блоком
		this.freeBlocks.add(new FreeBlock(0, maxHeapSize));
		// Для наглядности массив байт свободной кучи можно заполнить -1
		//Arrays.fill(this.bytes, (byte)-1);
	}
	
	enum Strategy {
		SORT_BY_ADDRESS,
		SORT_BY_SIZE,
		SORT_BY_TWO_INDEXES,
		PERIODIC_DEFRAG,
		PERIODIC_COMPACT
	}
	
	/* Вспомогательный класс для хранения информации о свободном блоке */
	
	private static class FreeBlock {
		int start; // Начало свободного блока
		int size;  // Размер свободного блока
		
		FreeBlock(int start, int size) {
			this.start = start;
			this.size = size;
		}
//		@Override
//		public String toString() {
//			String block = " i=" + start + ", size=" + size + ":\n [ FB ]";
//			for (int i = 0; i < size - 1; i++) {
//				block += "[ FB ]";
//			}
//			return block + "\n";
//		}
	}
	
	/* Вспомогательный класс для хранения информации о выделенном блоке */
	
	static class AllocatedBlock {
		int start; // Начало выделенного блока
		int size;  // Размер выделенного блока
		
		AllocatedBlock(int start, int size) {
			this.start = start;
			this.size = size;
		}
//		@Override
//		public String toString() {
//			String block = " i=" + start + ", size=" + size + ":\n [ AB ]";
//			for(int i = 0;i < size - 1; i++) {
//				block += "[ AB ]";
//			}
//			return block + "\n";
//		}
	}
	
	/*
	Метод malloc ищет свободный блок для выделения блока заданного размера.
	Если подходящий блок найден, он помечается как занятый и добавляется в список выделенных блоков.
	Если блоков нужного размера нет, вызывается компактизация.
	*/
	
	public int malloc(int size) {
		// Внутренний флаг, указывающий на необходимость компактизации
		boolean toCompact = true;
		// Массив байт, имитирующий выделяемый блок
		byte[] block = new byte[size];
		// Проверка свободных блоков на наличие блока подходящего размера
		// при наличии такого блока отменяем компактизацию, меняя значение внутреннего флага на false.
		for (FreeBlock freeBlock : freeBlocks) {
			if (freeBlock.size >= size) {
				toCompact = false;
				break;
			}
		}
		// При отсутствии блока нужного размера (т.е. значение внутреннего флага не изменилось)
		// а также если внешний флаг possibleCompact указывает, что компактизация не была проведена в другом методе,
		// проводим компактизацию, устанавливаем внешний флаг на false.
		if (toCompact && possibleCompact) {
			compact();
			possibleCompact = false;
		}
		for (int i = 0; i < freeBlocks.size(); i++) {
			
			FreeBlock freeBlock = freeBlocks.get(i);
			if (freeBlock.size >= size) {
				// Создаем новый размещаемый блок
				AllocatedBlock allocatedBlock = new AllocatedBlock(freeBlock.start, size);
				allocatedBlocks.add(allocatedBlock);
				// Для наглядности массив байт выделяемого блока можно заполинить значениями стартового индекса
				// Arrays.fill(block, (byte) freeBlock.start);
				// Копируем массив байт выделяемого блока в массив кучи
				setBytes(freeBlock.start, block);
				// Обновляем свободный блок: уменьшаем его или удаляем
				if (freeBlock.size == size) {
					freeBlocks.remove(i);
				} else {
					freeBlock.start += size;
					freeBlock.size -= size;
				}
				// Возвращаем индекс начала выделенного блока
				return allocatedBlock.start;
			}
		}
		// Если, несмотря на компактизацию нет свободнонго блока нужного размера выбрасываем исключение OutOfMemoryException
		throw new OutOfMemoryException("Недостаточно памяти для выделения блока размером " + size + " байт.");
	}
	
	/*
	Метод free освобождает блок памяти, проверяя, что указатель является началом ранее выделенного блока.
	*/
	
	public void free(int ptr) {
		// Ищем выделенный блок по указателю
		for (int i = 0; i < allocatedBlocks.size(); i++) {
			AllocatedBlock allocatedBlock = allocatedBlocks.get(i);
			if (allocatedBlock.start == ptr) {
				// Перемещаем блок в список свободных блоков
				freeBlocks.add(new FreeBlock(allocatedBlock.start, allocatedBlock.size));
				allocatedBlocks.remove(i);
				byte[] block = new byte[allocatedBlock.size];
				// Для наглядности массив байт освобождаемого блока можно заполинить значениями -1
				//Arrays.fill(block, (byte)-1);
				// Копируем массив байт освобождаемого блока в массив кучи
				setBytes(ptr, block);
				return;
			}
		}
		// Если указатель не найден или некорректен, выбрасываем исключение InvalidPointerException
		throw new InvalidPointerException("Указатель " + ptr + " некорректен.");
	}
	
	/*
	Метод defrag объединяет смежные свободные блоки.
	*/
	
	public void defrag() {
		// Сортируем свободные блоки по началу
		freeBlocks.sort((a, b) -> Integer.compare(a.start, b.start));
		// Объединяем смежные блоки
		for (int i = 0; i < freeBlocks.size() - 1; i++) {
			FreeBlock current = freeBlocks.get(i);
			FreeBlock next = freeBlocks.get(i + 1);
			// Если блоки соприкасаются, объединяем их
			if (current.start + current.size == next.start) {
				current.size += next.size;
				freeBlocks.remove(i + 1);
				i--; // Проверяем текущий блок еще раз с новым "следующим"
			}
		}
	}
	
	/*
	Метод compact сдвигает все выделенные блоки к началу кучи и освобождает память после них.
	*/
	
	public void compact() {
		int destination = 0; // Текущая позиция для копирования данных
		// Сдвигаем все выделенные блоки к началу
		for (AllocatedBlock block : allocatedBlocks) {
			// Копируем данные блока по одному байту в массив кучи
			for (int i = 0; i < block.size; i++) {
				bytes[destination + i] = bytes[block.start + i];
			}
			block.start = destination; // Обновляем начало блока
			destination += block.size; // Смещаемся на следующий свободный адрес
		}
		// Обновляем список свободных блоков
		freeBlocks.clear();
		if (destination < bytes.length) {
			freeBlocks.add(new FreeBlock(destination, bytes.length - destination));
			byte[] block = new byte[bytes.length - destination];
			//Arrays.fill(block, (byte)-1);
			// Обновляем массив кучи
			setBytes(destination, block);
		}
	}
	
	/* Метод для получения данных из кучи по указателю */
	
	public void getBytes(int ptr, byte[] bytes) {
		Arrays.fill(bytes, (byte)-1);
		if (ptr < 0 || ptr > bytes.length) {
			throw new InvalidPointerException("Invalid pointer or size.");
		}
		// Копируем данные из кучи в указанный массив
		System.arraycopy(this.bytes, ptr, bytes, 0, bytes.length);
	}
	
	/* Метод для записи данных в кучу по указателю */
	
	public void setBytes(int ptr, byte[] bytes) {
		if (ptr < 0 || ptr > this.bytes.length) {
			throw new InvalidPointerException("Invalid pointer or size.");
		}
		// Копируем данные из указанного массива в кучу
		System.arraycopy(bytes, 0, this.bytes, ptr, bytes.length);
	}
	
	/* Метод для поиска и выделения памяти используя сортировку свободных блоков по индексу*/
	
	public int sortFreeBlocksByAddress(int size, boolean doneFree) {
		// Сортируем блоки в случае, если состоялся процесс освобождения и в список добавились новые блоки
		if (doneFree) {
			freeBlocks.sort(Comparator.comparingInt(block -> block.start));
			this.doneFree = false;
		}
		// Используя отсортированный список выделяем блок нужного размера используя метод malloc(size)
		// и возвращаем индекс начала выделенного блока
		return malloc(size);
	}
	
	/* Метод для поиска и выделения памяти используя сортировку свободных блоков по размеру */
	
	public int sortFreeBlocksBySize(int size, boolean doneFree) {
		// Сортируем блоки в случае, если состоялся процесс освобождения и в список добавились новые блоки
		if (doneFree) {
			freeBlocks.sort(Comparator.comparingInt(block -> block.size));
			this.doneFree = false;
		}
		// Используя отсортированный список выделяем блок нужного размера через метод malloc(size)
		// и возвращаем индекс начала выделенного блока
		return malloc(size);
	}
	
	/* Метод для поиска и выделения памяти с использованием двух индексов */
	
	public int sortFreeBlocksByTwoIndexes(int size, boolean doneFree) {
		boolean toCompact = true;// Внутренний флаг, указывающий на необходимость компактизации
		int ptr = -1; // Заводим значение указателя как -1
		byte[] block = new byte[size];// Массив байт, имитирующий размещаемый блок
		FreeBlock freeBlock = null;
		List<FreeBlock> blocksSortedBySize = new ArrayList<>(freeBlocks);	// Дополнительный список для сортировкм по размеру
		// Проверка свободных блоков на наличие блока подходящего размера
		// при наличии такого блока отменяем компактизацию, меняя значение внутреннего флага на false.
		for (FreeBlock fBlock : freeBlocks) {
			if (fBlock.size >= size) {
				toCompact = false;
				break;
			}
		}
		// При отсутствии блока нужного размера (т.е. значение внутреннего флага не изменилось)
		// проводим компактизацию, устанавливаем внешний флаг на false.
		if (toCompact) {
			compact();
			possibleCompact = false;
		} else {
			// Если в компактизации нет необходимости и если добавились освобожденные блоки (doneFree == true)
			// сортируем список свободных блоков по индексу, а дополнительный список - по размеру
			if (doneFree) {
				freeBlocks.sort(Comparator.comparingInt(b -> b.start));
				blocksSortedBySize.sort(Comparator.comparingInt(b -> b.size));
				this.doneFree = false;
			}
		}
		// Обходим оба отсортированных списка
		for (int i = 0; i < freeBlocks.size(); i++) {
			freeBlock = freeBlocks.get(i);
			// Вначале проверяем подходит ли под заданный размер
			// текущегий блок в списке отсортированном по индексу
			if (freeBlock.size >= size) {
				// Если да, присваиваем индекс блока указателю
				ptr = freeBlock.start;
			} else {
				// Если нет, проверяем подходит ли под заданный размер
				// текущий длок в списке отсортированном по размеру
				freeBlock = blocksSortedBySize.get(i);
				if (freeBlock.size >= size) {
					// Если да, отсортированный по индексу список делаем осортированным по размеру
					// и присваиваем индекс блока указателю
					freeBlocks = blocksSortedBySize;
					ptr = freeBlock.start;
				}
			}
			// Если указатель не отрицателен
			if (ptr >= 0) {
				// Создаем новый выделенный блок
				AllocatedBlock allocatedBlock = new AllocatedBlock(ptr, size);
				allocatedBlocks.add(allocatedBlock);
				// Для наглядности массив байт размещаемого блока можно заполинить значениями полученного указателя
				//Arrays.fill(block, (byte) ptr);
				// Копируем массив байт выделенного блока в массив кучи
				setBytes(ptr, block);
				// Обновляем свободный блок: уменьшаем его или удаляем
				if (freeBlock.size == size) {
					freeBlocks.remove(i);
				} else {
					freeBlock.start += size;
					freeBlock.size -= size;
				}
				// Возвращаем индекс начала выделенного блока
				return ptr;
			}
		}
		// Если, несмотря на компактизацию нет свободнонго блока нужного размера выбрасываем исключение OutOfMemoryException
		throw new OutOfMemoryException("Недостаточно памяти для выделения блока размером " + size + " байт.");
	}
	
	/* Метод для поиска и выделения памяти применяя периодическую компактизацию */
	
	public int periodicCompact(int size) {
		// В методе периодичность компактизации определяется отсутствием в списке свободных блоков
		// блока нужного размера
		boolean toCompact = true;
		for (FreeBlock fBlock : freeBlocks) {
			if (fBlock.size >= size) {
				toCompact = false;
				break;
			}
		}
		if (toCompact) {
			compact();
			possibleCompact = false;
		}
		// Используя  метод malloc(size)
		// возвращаем индекс начала выделенного блока
		return malloc(size);
	}
	
	/* Метод для поиска и выделения памяти применяя периодическую дефрагментацию */
	
	public int periodicDefrag(int size, int frequencyDefragCoeff, boolean doneFree ) {
		// В методе периодичность дефрагментации зависит от значения внешнего флага doneFree,
		// который показывает состоялось ли освобождение блоков
		// Также можно регулировать периодичность дефрагментации, используя значение
		// аргумента frequencyDefragCoeff (чем больше значение - тем реже дефрагментация)
		
		if (doneFree) {
			countDefrag++; // Счетчик количества дефрагментаций
			
			if (countDefrag % frequencyDefragCoeff == 0) {
				defrag();
			}
			this.doneFree = false;
		}
		// Используя  метод malloc(size)
		// возвращаем индекс начала выделенного блока
		return malloc(size);
	}
	
	/* В методе определяется применение метода поиска и выделения памяти в зависимости
	от стратегии, которые определены в enum Strategy */
	
	public int getStrategy(int size,
	                       Strategy strategy,
	                       int frequencyDefragCoeff,
	                       boolean doneFree) {
		switch (strategy) {
			case SORT_BY_SIZE:
				return sortFreeBlocksBySize(size, doneFree);
			case SORT_BY_TWO_INDEXES:
				return sortFreeBlocksByTwoIndexes(size, doneFree);
			case PERIODIC_COMPACT:
				return periodicCompact(size);
			case PERIODIC_DEFRAG:
				return periodicDefrag(size, frequencyDefragCoeff, doneFree);
			default:
				return sortFreeBlocksByAddress(size, doneFree);
		}
	}
}