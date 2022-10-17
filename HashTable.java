import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HashTable<K, V> implements HashTableInterface<K, V> {
	private int numberOfEntries;
	private static double maxLoadFactor;
	private static final int DEFAULT_CAPACITY = 31;
	private static final int maxCapacity = 100000;
	private TableEntry<K, V>[] hashTable;
	private int tableSize; // Must be prime
	private static final int maxSize = 2 * maxCapacity;
	private static final double DEFAULT_MAX_LOAD_FACTOR = 0.5;
	private static final String DEFAULT_HASH_TYPE = "PAF";
	private static final String DEFAULT_COLLISION_TYPE ="DH";
	private boolean initialized = false;
	private Scanner scan=new Scanner(System.in);
	private String collisionHandlingType;
	private String HashType;
    static int collisionCount=0;
   
 
	public HashTable() {
		this(DEFAULT_CAPACITY,DEFAULT_MAX_LOAD_FACTOR,DEFAULT_HASH_TYPE,DEFAULT_COLLISION_TYPE);
	}

	@SuppressWarnings("static-access")
	public HashTable(int initialCapacity,double maxLoadFactor,String HashType,String collisionHandlingType ) {
	   this.maxLoadFactor= maxLoadFactor;
	   this.HashType=HashType;
	   this.collisionHandlingType=collisionHandlingType;
		checkCapacity(initialCapacity);
		numberOfEntries = 0;
		tableSize = getNextPrime(initialCapacity);
		checkCapacity(tableSize);
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[tableSize];
		hashTable = temp;
		initialized = true;
	}

	public V put(K key, V value) {
		checkInitialization();
		if ((key == null) || (value == null))
			throw new IllegalArgumentException();
		else {
			V oldValue=null; 
			int index = getHashIndex(key);
			index = probe(index, key); 
			assert (index >= 0) && (index < hashTable.length);
			if ((hashTable[index] == null) || hashTable[index].isRemoved()) { 
				hashTable[index] = new TableEntry<>(key, value);
				numberOfEntries++;
				oldValue = null;
			} else { 
				oldValue = hashTable[index].getValue();
				hashTable[index].setValue(value);
			} 
			if (isHashTableTooFull())
				enlargeHashTable();
			return oldValue;
		} 
	}

	private void enlargeHashTable() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldCapacity = hashTable.length;
		int newSize = getNextPrime(oldCapacity + oldCapacity);
		tableSize = newSize;
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[newSize];
		hashTable = temp;
		numberOfEntries = 0;
		for (int i = 0; i < oldCapacity; i++) {
			if ((oldTable[i] != null) && oldTable[i].isIn())
				put(oldTable[i].getKey(), oldTable[i].getValue());
		}
	}

	private boolean isHashTableTooFull() {
		return numberOfEntries >= (maxLoadFactor * tableSize);
	}

	private int probe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1;
		int increase = 1;
		int a = index;
		int q = 31;
		while (!found && hashTable[index] != null) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey()))
					found = true;
				else if (collisionHandlingType.equalsIgnoreCase("LP")) {
					index = (index + 1) % hashTable.length; // Linear probing
					collisionCount++;
				}
				else if (collisionHandlingType.equalsIgnoreCase("DH")) {
					index = (a + (increase * (q - (a % q)))) % hashTable.length; // double hashing
					increase++;
					collisionCount++;
				}
			} else {
				if (removedStateIndex == -1)
					removedStateIndex = index;
				if (collisionHandlingType.equalsIgnoreCase("LP")) {// Linear probing
					index = (index + 1) % hashTable.length;
					collisionCount++;
				}
				else if (collisionHandlingType.equalsIgnoreCase("DH")) { // Double Hashing
					index = (a + (increase * (q - (a % q)))) % hashTable.length;
					increase++;
					collisionCount++;
				}
			}
		}
		if (found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}

	private int Polynomial_Accumulation_Function(String word) {
		int hashValue = 0;
		for (int i = 0; i < word.length(); i++) { // Horner’s rule
			hashValue = 31 * hashValue + ((int) word.charAt(i) - 96);
		}
		return hashValue;
	}

	public int Simple_Summation_Function(String word) {
		int hashValue = 0;
		for (int i = 0; i < word.length(); i++) { // Horner’s rule
			hashValue += ((int) word.charAt(i) - 96);
		}
		return hashValue;
	}

	public int getHashIndex(K key) {
		int hashIndex = 0;
		if (HashType.equalsIgnoreCase("SSF"))
			hashIndex = Simple_Summation_Function((String) key) % hashTable.length;
		else if (HashType.equalsIgnoreCase("PAF"))
			hashIndex = Polynomial_Accumulation_Function((String) key) % hashTable.length;
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;

	}

	public void remove(K key) {
		checkInitialization();
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1) {
			hashTable[index].setToRemoved();
		}
	}

	public V getValue(K key) {
		checkInitialization();
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			result = hashTable[index].getValue();
		return result;
	}

	public static int getNextPrime(int num) {
		num++;
		for (int i = 2; i < num; i++) {
			if (num % i == 0) {
				num++;
				i = 2;
			} else {
				continue;
			}
		}
		return num;
	}

	private int locate(int index, K key) {
		int increase = 1;
		int a = index;
		int q = 31;
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true; // Key found
			else if (collisionHandlingType.equalsIgnoreCase("LP")) {
				index = (index + 1) % hashTable.length; // Linear probing
				collisionCount++;
			}
			else if (collisionHandlingType.equalsIgnoreCase("DH")) {
				index = (a + (increase * (q - (a % q)))) % hashTable.length; // Double Hashing
				increase++;
				collisionCount++;
			}
		}
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	public boolean contains(K key) {
		return getValue(key) != null;
	}

	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public void clear() {
		for (int index = hashTable.length; index > 0; index--) {
			hashTable[index] = null;
		}
		numberOfEntries = 0;

	}

	public void SearchKey() {
		System.out.print(">Search: ");
		@SuppressWarnings("unchecked")
		K key = (K) scan.nextLine();
		int index = getHashIndex(key);
		try {
			@SuppressWarnings("unchecked")
			LinkedList<HashValue> List = (LinkedList<HashValue>) hashTable[index].getValue();
			System.out.println(List.size() + " documents found");
			for (HashValue hashValue : List) {
				System.out.println(hashValue.getCount() + "-" + hashValue.getDirectory());
			}
		} catch (NullPointerException e) {
			System.out.println("> Search: "+key);
			System.out.println("Not found!");
		}
	}

	public boolean SearchTXT(K key) {
		int index = getHashIndex(key);
		if (hashTable[index]!=null) {
			return true;
		}
		else {
			return false;
		}
	}

	private class KeyIterator implements Iterator<K> {
		private int currentIndex;
		private int numberLeft;

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public K next() {
			K result = null;
			if (hasNext()) {

				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private class ValueIterator implements Iterator<V> {
		private int currentIndex; // Current position in hash table
		private int numberLeft; // Number of entries left in iteration

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} // end default constructor

		public boolean hasNext() {
			return numberLeft > 0;
		} // end hasNext

		public V next() {
			V result = null;
			if (hasNext()) {
				// Skip table locations that do not contain a current entry
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} // end while
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} // end next

		public void remove() {
			throw new UnsupportedOperationException();
		} // end remove
	} // end KeyIterator

	private void checkInitialization() {
		if (!initialized)
			throw new SecurityException("Dictionary object is not initialized " + "properly.");
	}

	private void checkCapacity(int capacity) {
		if (capacity > maxCapacity)
			throw new IllegalStateException(
					"Attempt to create a list whose " + "capacity exeeds allowed " + "maximum of " + maxCapacity);
	}
	
}
