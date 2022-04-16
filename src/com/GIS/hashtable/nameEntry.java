package com.GIS.hashtable;
import java.util.ArrayList;

/**
 * Represents an entry to be used in our hashtable to store feature names and their offsets.
 * @author Mansour Najah
 */
public class nameEntry implements Hashable<nameEntry> {
	String key; // GIS feature name
	private ArrayList<Long> locations; // file offsets of matching records

	/**
	 * Initialize a new nameEntry object with the given feature name and a single
	 * file offset.
	 */
	public nameEntry(String name, Long offset) {
		key = name;
		setLocations(new ArrayList<Long>());
		getLocations().add(offset);
	}

	/**
	 * Return feature name.
	 */
	public String key() {
		return key;
	}

	/**
	 * Return list of file offsets.
	 */
	public ArrayList<Long> locations() {
		return getLocations();
	}

	/**
	 * Append a file offset to the existing list.
	 */
	public boolean addLocation(Long offset) {
		if (!getLocations().contains(offset)) {
			return getLocations().add(offset);
		}
		return false;
	}

	/** Fowler/Noll/Vo hash function is mandatory for this assignment. **/
	public int Hash() {
		final int fnvPrime = 0x01000193; // Constant values for FNV
		final int fnvBasis = 0x811c9dc5; // hash algorithm
		int hashValue = fnvBasis;

		for (int i = 0; i < key.length(); i++) {
			hashValue ^= key.charAt(i);
			hashValue *= fnvPrime;
		}

		return Math.abs(hashValue);
	}

	/**
	 * Two nameEntry objects are considered equal iff they hold the same feature
	 * name.
	 */
	public boolean equals(Object other) {
		if (!other.getClass().equals(nameEntry.class)) {
			return false;
		}
		
		return (this.key().equals(((nameEntry) other).key())); 
	}

	/**
	 * Return a String representation of the nameEntry object in the format needed
	 * for this assignment.
	 */
	public String toString() {
		return ("[" + this.key + ", " + this.getLocations().toString() + "]");
	}

	public ArrayList<Long> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Long> locations) {
		this.locations = locations;
	}
}
