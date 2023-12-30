package ru.progwards.java2.lessons.basetypes;
import java.util.*;

class DoubleHashTable<K, V> implements UtilityFunctions, Iterable<V> {
	class IntKey<K> implements HashValue {
		K key;
		public IntKey(K key) {
			this.key = key;
		}
		public int getHash() {
			return (int)key;
		}
	}
	class StringKey<K> implements HashValue {
		K key;
		public StringKey(K key) {
			this.key = key;
		}
		public int getHash() {
			return stringHashFunction((String)key);
		}
		public int stringHashFunction(String str) {
			int hashCode = 0;
			for (int i = 0; i < str.length(); i++) {
				hashCode += str.charAt(i);
			}
			return hashCode;
		}
	}
	class HashItem<K, V> {
		private V value;
		private K key;
		private HashItem<K, V> next;
		private int hashIndex = 0;

		HashItem(K key, V value) {
			this.key = key;
			this.value = value;
		}
		K getKey() {
			return key;
		}
		V getValue() {
			return value;
		}
		void setNext(HashItem<K, V> item) {
			next = item;
		}
		void setIndex(int index) {
			hashIndex = index;
		}
		public int getHashIndex() {
			return hashIndex;
		}
		void setOtherValue(V otherValue) {
			this.value = otherValue;
		}
		void setOtherKey(K otherKey) {
			this.key = otherKey;
		}
		HashItem<K, V> getNext() {
			return next;
		}
		@Override
		public String toString() {
			return "{key: " + key + ", value: " + value + "}";
		}
	}
	class HashItemList<T> extends LinkedList<T> {
		public int index;
		HashItemList() {
			super();
		}
		HashItemList(int index) {
			this();
			this.index = index;
		}
		int getIndex() {
			return index;
		}
		@Override
			public String toString() {
				LinkedList<T> list = new LinkedList<>(this);
				return "basket " + index + ": " + list;
			}
	}
	class TableIterator<T> implements Iterator<T> {
		int index = 0;
		HashItem<K, V> current;
		HashItem<K, V> remove;
		
		public TableIterator(HashItem<K, V>[] table) {
			current = table[index];
		}
		public boolean hasNext() {
			if(index < capacity)
				return true;
			return false;
		}
		public T next() {
			T item = (T)current;
			remove = (HashItem<K, V>) item;
			if(current != null && current.getNext() != null) {
				current = current.getNext();
			} else {
				index++;
				if(index == capacity)
					return item;
				current = table[index];
			}
			return item;
		}
		public void remove() {
			try {
				remove.setOtherKey((K)Integer.valueOf("-1"));
			} catch(UnsupportedOperationException e) {
				throw e;
			}
		}
	}
	public Iterator<V> iterator() {
		return new TableIterator(table);
	}
	private int capacity;
	private int size;
	private double loadFactor = 0.1;
	private int threshold;
	private boolean rehash;
	private int removeCount = 0;
	public HashItem<K, V>[] table;
	private int primeSize;
	private LinkedList<HashItem<K, V>> copyList;
	
	public DoubleHashTable() {
		this(101);
	}
	
	public DoubleHashTable(int n) {
		size = 0;
		capacity = n;
		table = new HashItem[capacity];
		threshold = UtilityFunctions.getThreshold(capacity, loadFactor);
		primeSize = UtilityFunctions.prevPrime(capacity);
		copyList = new LinkedList<>();
	}
	
	public int size() {
		return size;
	}

	public int getHash(K key) {
		int k = 0;
		if(key instanceof String)
			k = new StringKey(key).getHash();
		else
			k = new IntKey(key).getHash();
		return k;
	}
	
	private int hashFunc1(K key) {
		int hashVal1 = UtilityFunctions.hash(getHash(key), capacity);
		if(hashVal1 < 0)
			hashVal1 += capacity;
		return hashVal1;
	}
	private int hashFunc2(K key) {
		int hashVal2 = getHash(key);
		hashVal2 %= capacity;
		if (hashVal2 < 0)
			hashVal2 += capacity;
		return primeSize - hashVal2 % primeSize;
	}
	
	public void add(K key, V value) {
		rehash = false;
		size++;
		int index = hashFunc1(key);
		int stepSize = hashFunc2(key);
		int templ = index;
		DoubleHashTable<K, V> newDHTable;
		HashItem<K, V> li = new HashItem<>(key, value);
		
		if(removeCount > 0) {
			insertRemove(key, value);
		} else {
			while(table[index] != null && size <= capacity) {
				index += stepSize;
				index %= capacity;
				if(templ == index) {
					templ += 1;
					index += 1;
				}
			}
			HashItem<K, V> head = table[index];
			li.setIndex(index);
			table[index] = li;
			if(head != null)
				li.setNext(head);
			copyList.add(li);
		}
		if(size == threshold) {
			rehash = true;
			int newCapacity = UtilityFunctions.nextPrime(capacity * 2);
			newDHTable = new DoubleHashTable<>(newCapacity);
			capacity = newCapacity;
			threshold = newDHTable.threshold;
			table = newDHTable.table;
			primeSize = newDHTable.primeSize;
			size = 0;
			createNewHashTable(copyList);
			size = copyList.size();
		}
	}
	void insertRemove(K key, V value) {
		TableIterator<HashItem<K, V>> iter = new TableIterator<>(table);
		while(iter.hasNext()) {
			HashItem<K, V> item = iter.next();
			K k = item.getKey();
			if(item != null && k.equals((K)Integer.valueOf("-1"))) {
				item.setOtherValue(value);
				item.setOtherKey(key);
				removeCount--;
				break;
			}
		}
	}
	void createNewHashTable(LinkedList<HashItem<K, V>> copyList) {
		LinkedList<HashItem<K, V>> utilList = new LinkedList<>();
		if(rehash) {
			utilList.addAll(copyList);
			copyList.clear();
		}
		for(int i = 0; i < utilList.size(); i++) {
			K key = utilList.get(i).getKey();
			V value = utilList.get(i).getValue();
			this.add(key, value);
		}
	}
	
	public V get(K key) {
		TableIterator<HashItem<K, V>> iter = new TableIterator<>(table);
		while(iter.hasNext()) {
			HashItem<K, V> item = iter.next();
			if(item != null) {
				if(item.getKey() == key && !item.getKey().equals((K)Integer.valueOf("-1")))
					return item.getValue();
			}
		}
		return null;
	}
	
	public void remove(K key) {
		TableIterator<HashItem<K, V>> iter = new TableIterator<>(table);
		while(iter.hasNext()) {
			HashItem<K, V> item = iter.next();
			K k = item.getKey();
			if(k.equals(key)) {
				iter.remove();
				size--;
				removeCount++;
			}
		}
	}
	
	public void change(K key1, K key2) {
		TableIterator<HashItem<K, V>> iter = new TableIterator<>(table);
		while(iter.hasNext()) {
			HashItem<K, V> item = iter.next();
			if(item != null) {
				if(item.getKey() == key1 && !item.getKey().equals((K)Integer.valueOf("-1")))
					item.setOtherKey(key2);
			}
		}
	}
	
	public void displayTable() {
		System.out.println("display table********");
		HashItemList<HashItem<K, V>>[] pTable = new HashItemList[capacity];
		for(int i = 0; i < pTable.length; i++) {
			pTable[i] = new HashItemList<>(i);
			pTable[i].add(null);
		}
		HashItem<K, V> item = null;
		int index = 0;
    for(int i = 0; i < copyList.size(); i++) {
	    item = copyList.get(i);
      index = item.getHashIndex();
	    for(int j = 0; j < pTable.length; j++) {
        if(pTable[j].getIndex() == index) {
          if(pTable[j].get(0) == null) {
	          pTable[j].set(0, item);
	          break;
          } else {
	          pTable[j].add(0, item);
	          break;
          }
        }
      }
    }
	  for(HashItemList<HashItem<K, V>> value : pTable) {
		  System.out.println(value);
	  }
	}
	
	public static void main(String[] args) {
		System.out.println("Hash Table Testing");
		String txt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id leo auctor, laoreet" +
						             " sem in, lobortis metus. Sed a magna quam. Maecenas ornare quam dui. Ut ipsum tellus," +
						             " porta eu tempus eget, sagittis in velit. Suspendisse a sodales odio. Quisque a rhoncus" +
						             " sapien. Nunc tempor iaculis risus in semper. Nulla finibus sagittis auctor. Vivamus" +
						             " mollis nunc nec arcu sollicitudin luctus Proin quis facilisis justo. Nulla luctus a leo" +
						             " ut pretium. Nam nec volutpat ex. Phasellus vitae sodales mi. Cras porttitor dui et " +
						             "interdum pharetra. Mauris dapibus luctus consectetur. Quisque quis nulla mi. Ut id " +
						             "rhoncus nisl, feugiat commodo odio. Proin interdum sapien rutrum ipsum lobortis vulputate." +
						             " Integer in.";
		String[] strs = txt.split(" ");
		DoubleHashTable dht = new DoubleHashTable(99);
		for(int i = 0; i < strs.length; i++) {
			dht.add(strs[i], i * 2);
		}
		System.out.println("......create hash table.........................");
		dht.displayTable();
		
		dht.remove("sapien.");
		dht.remove("arcu");
		dht.remove("nec");
		dht.remove("vitae");
		System.out.println("......after remove.........................");
		dht.displayTable();
		
		dht.add("ADD1", 1000);
		dht.add("ADD2", 1000);
		dht.add("ADD3", 1000);
		dht.add("ADD4", 1000);
		dht.add("ADD5", 1000);
		System.out.println("......add after remove.........................");
		dht.displayTable();
		
		
		dht.add("REHASH", 3000);
		System.out.println("......create new rehash table.........................");
		dht.displayTable();
		
		dht.change("REHASH", "CHANGE");
		System.out.println("......after change.........................");
		dht.displayTable();
		
		int[] ints = new Random().ints(18, 0, 125).toArray();
//		for(int i = 0; i < ints.length; i++) {
//			dht.add(ints[i], strs[i]);
//			dht.displayTable();
//		}
	}
}
