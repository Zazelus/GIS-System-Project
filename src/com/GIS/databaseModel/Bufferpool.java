package com.GIS.databaseModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A Bufferpool with an LRU implementation.
 * Should make searching for records by cutting down on the amount of disk accesses.
 * 
 * @author Mansour Najah
 */
public class Bufferpool {
	
	/**
	 * Internal node class to represent the items in the Bufferpool.
	 * 
	 * @author Mansour Najah
	 */
	class Node {
		String val;
		long key;
		
		/**
		 * Each node has a string value and key associated with it.
		 * 
		 * @param val: string representing our object in the pool.
		 * @param key: a number to let us fetch the object.
		 */
		public Node(String val, long key) {
			this.val = val;
			this.key = key;
		}
	}
	
	// Objects that represent the attributes of our Bufferpool.
	private Map<Long, Node> cacheMap;
	private LinkedList<Node> elemOrder;
	private int capacity;
	
	/**
	 * Default constructor, creates a Bufferpool with a default capacity
	 * of 15.
	 */
	public Bufferpool() {
		cacheMap = new HashMap<>();
		elemOrder = new LinkedList<>();
		capacity = 15;
	}
	
	/**
	 * Bufferpool that allows client to assign a value to capacity.
	 * 
	 * @param capacity: the amount of objects that the Bufferpool can cache.
	 */
	public Bufferpool(int capacity) {
		cacheMap = new HashMap<>();
		elemOrder = new LinkedList<>();
		this.capacity = capacity;
	}
	
	/**
	 * Moves target node to head.
	 * 
	 * @param node: the node to be moved.
	 */
	private void goToHead(Node node) {
		elemOrder.remove(node);
		elemOrder.addFirst(node);
	}
	
	/**
	 * Fetch specific object using a key to search in our pool.
	 * 
	 * @param key: the key to target our object.
	 * @return: the object's string value.
	 */
	public String getElem(long key) {
		Node node = cacheMap.get(key);
		
		if (node == null)
			return null;
		
		goToHead(node);
		
		return node.val;
	}
	
	/**
	 * Inserts a new element into our pool.
	 * 
	 * @param val: the string value of this new element.
	 * @param key: the key value for this new element.
	 */
	public void insertElem(String val, long key) {
		Node node = cacheMap.get(key);
		
		// Check to see if we have an empty place to insert our object.
		if (node == null) {
			Node newNode = new Node(val, key);
			
			cacheMap.put(key, newNode);
			elemOrder.addFirst(newNode);
			
			// Resize if necessary.
			if (cacheMap.size() > capacity) {
				Node tail = elemOrder.removeLast();
				cacheMap.remove(tail.key);
			}
		}
		
		// If not, we'll apply LRU policy.
		else {
			node.val = val;
			goToHead(node);
		}
	}
	
	/**
	 * Displays the current content of the Bufferpool.
	 * 
	 * @param fw: FileWriter object that will write to our log.
	 * @throws IOException: throws an exception if the file isn't found.
	 */
	public void display(FileWriter fw) throws IOException {
		fw.write("MRU\n");
		
		for (int i = 0; i < elemOrder.size(); i++)
			fw.write("\t" + elemOrder.get(i).key + " " + elemOrder.get(i).val + "\n");
		
		fw.write("LRU");
	}

}
