package com.GIS.hashtable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A hash table implementation using chaining to resolve collisions.
 * @author Mansour Najah
 *
 * @param <T>: a generic type.
 */
public class hashTable<T extends Hashable<T>> {

	private ArrayList<LinkedList<T>> table; // physical basis for the hash table
	private Integer numElements = 0; // number of elements in all the chains
	private Double loadLimit = 0.7; // table resize trigger
	private final Integer defaultTableSize = 256; // default number of table slots
	private Integer currentTableSize;
	private int maxSlotLength = 0;

	/**
	 * Constructs an empty hash table with the following properties: Pre: - size is
	 * the user's desired number of lots; null for default - ldLimit is user's
	 * desired load factor limit for resizing the table; null for the default Post:
	 * - table is an ArrayList of size LinkedList objects, 256 slots if size == null
	 * - loadLimit is set to default (0.7) if ldLimit == null
	 */
	public hashTable(Integer size, Double ldLimit) {
		if (currentTableSize != null) {
			currentTableSize = size;
		} else {
			currentTableSize = defaultTableSize;
		}

		if (ldLimit != null) {
			loadLimit = ldLimit;
		}
		
		table = newList(currentTableSize);
	}


	/**
	 * Creates a new ArrayList of LinkedList with a set size.
	 * @param size: the size of the new array list.
	 * @return: the new array list.
	 */
	private ArrayList<LinkedList<T>> newList(int size) {
		ArrayList<LinkedList<T>> newList = new ArrayList<LinkedList<T>>();

		/**
		 * Fill list with null values.
		 */
		for (int i = 0; i < size; i++) {
			newList.add(null);
		}

		return newList;
	}

	/**
	 * A function to resize our table.
	 */
	public void reallocate() {
		ArrayList<LinkedList<T>> newList = table;
		
		// Double size.
		currentTableSize *= 2;
		numElements = 0;
		table = newList(currentTableSize);

		/**
		 * Repopulate the table.
		 */
		for (LinkedList<T> entry : newList) {
			if (entry != null) {
				for (T val : entry) {
					insert(val);
				}
			}
		}
	}

	/**
	 * Get current table load, used to check against the load limit.
	 * @return: current table load.
	 */
	public double getLoad() {
		return (double) numElements / (double) currentTableSize;
	}

	/**
	 * Inserts elem at the front of the elem's home slot, unless that slot already
	 * contains a matching element (according to the equals() method for the user's
	 * data type. Pre: - elem is a valid user data object Post: - elem is inserted
	 * unless it is a duplicate - if the resulting load factor exceeds the load
	 * limit, the table is rehashed with the size doubled Returns: true iff elem has
	 * been inserted
	 */
	public void insert(T elem) {

		int slot = elem.Hash() % currentTableSize;

		if (table.get(slot) == null) {
			table.set(slot, new LinkedList<T>());
		}
		
		boolean duplicate = false;
		
		if (table.get(slot) != null) {
			for (T val: table.get(slot)) {
				if (val.equals(elem)) {
					for (int i = 0; i < ((nameEntry) elem).locations().size(); i++) {
						((nameEntry) val).addLocation(((nameEntry) elem).locations().get(i));
					}
					duplicate = true;
				}
			}
		}
		
		if (duplicate != true) {
			table.get(slot).add(elem);
			numElements++;
		}

		int max = table.get(slot).size() - 1;
		
		if (max > maxSlotLength) {
			maxSlotLength = max;
		}
		
		if (getLoad() > loadLimit) {
			reallocate();
		}
	}

	/**
	 * Searches the table for an element that matches elem (according to the
	 * equals() method for the user's data type). Pre: - elem is a valid user data
	 * object Returns: reference to the matching element; null if no match is found
	 */
	public T find(T elem) {
		int slot = elem.Hash() % currentTableSize;
		
		if (table.get(slot) != null) {
			for (T val: table.get(slot)) {
				if (val.equals(elem)) {
					return val;
				}
			}
		}
		
		return null;
	}

	/**
	 * Writes a formatted display of the hash table contents. Pre: - fw is open on
	 * an output file
	 */
	public void display(FileWriter fw) throws IOException {
		fw.write("Number of elements: " + numElements + "\n");
		fw.write("Number of slots: " + table.size() + "\n");
		fw.write("Maximum elements in a slot: " + maxSlotLength + "\n");
		fw.write("Load limit: " + loadLimit + "\n");
		fw.write("\n");

		fw.write("Slot Contents\n");
		for (int idx = 0; idx < table.size(); idx++) {
			LinkedList<T> curr = table.get(idx);

			if (curr != null && !curr.isEmpty()) {
				fw.write(String.format("%5d: %s\n", idx, curr.toString()));
			}
		}
		// Implementation above conforms to the suggested hash table design; if
		// you make changes to the class design above, you must supply a display
		// function that writes the same information as this one.
	}

}
