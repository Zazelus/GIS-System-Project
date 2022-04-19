package com.GIS.databaseModel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.GIS.hashtable.hashTable;
import com.GIS.hashtable.nameEntry;
import com.GIS.world.Point;
import com.GIS.world.prQuadTree;

/**
 * A class to manage all commands in the back-end when dealing with our database.
 * 
 * @author Mansour Najah
 */
public class DbController {
	
	private final String dbFileName;
	
	// Essential objects to run our queries on.
	private prQuadTree<Point> world;
	private hashTable<nameEntry> table;
	private Bufferpool bufferPool;
	
	/**
	 * Create a new DbController object.
	 * Initialize hash table to a size of 1024 with a load factor of 70%.
	 * Our buffer pool will call the default constructor, creating a cache of size 15.
	 * 
	 * @param dbFileName: the name of our db file.
	 */
	public DbController(String dbFileName) {
		this.dbFileName = dbFileName;
		world = new prQuadTree<>(0, 0, 0, 0);
		table = new hashTable<nameEntry>(1024, 0.7);
		bufferPool = new Bufferpool();
	}
	
	/**
	 * Inserts a feature and uses its name and state abbreviation as a key for the hash table.
	 * 
	 * @param values: the record being inserted.
	 * @param offset: the file offset of our record.
	 * @return: returns true after successfully inserting.
	 */
	private boolean tableInsert(String[] values, long offset) {
		nameEntry entry = new nameEntry(values[1] + "|" + values[3], offset);
		nameEntry target = table.find(entry);
		
		// If the location is not empty, add a location offset. Else, we'll just insert.
		if (target != null) {
			return target.addLocation(offset);
		} else {
			table.insert(entry);
		}
		
		return true;
	}
	
	 /**
     * Converts DMS format into an integer to be used by the Point class.
     * @param dms coverted string
     * @return Radians in seconds
     * */
    private int DMSToInt(String DMS) {

    	// Checks the Unknown case for certain records.
        if (DMS == null || DMS.equals("Unknown")) {
            return 181 * 3600;

        }

        char dir = DMS.charAt(DMS.length()-1);
        int result = 0;
        
        // Depending on coord. direction, we'll do some string math for DMS to integer conversion.
        if (dir=='N') {
        	String n1 = DMS.substring(0, 2);
            String n2 = DMS.substring(2,4);
            String n3 = DMS.substring(4,6);
            
            result += Integer.parseInt(n1)*3600;
            result += (Integer.parseInt(n2) * 60);
            result += Integer.parseInt(n3);

        } else if (dir=='W') {
            String w1 = DMS.substring(0, 3);
            String w2 = DMS.substring(3,5);
            String w3 = DMS.substring(5,7);
            
            result -= Integer.parseInt(w1)*3600;
            result -= (Integer.parseInt(w2) * 60);
            result -= Integer.parseInt(w3);
        }

        return result;

    }
	
    /**
     * Inserts a feature into our world. Entries are stored with their coordinates and file offsets
     * of matching records from our db file.
     * @param values: is our record where we'll access relevant DMS info.
     * @param offset: the offset at which the record occurs.
     * @return: returns true after successfully inserting.
     */
	private boolean treeInsert(String[] values, long offset) {
		Point loc = new Point(DMSToInt(values[8]), DMSToInt(values[7]));
		loc.addOffset(offset);
		
		Point locInWorld = world.find(loc);
		
		// If the location in the tree is empty, we can just insert.
		if (locInWorld == null) {
			return world.insert(loc);
		} 
		
		// If not, we'll add it to an offset list as they are still different features.
		locInWorld.addOffset(offset);
		return true;
	}
	
	/**
	 * Import each record from a given data file.
	 * @param file: file to be importing records from.
	 * @return an array list with relevant information from importing.
	 * @throws IOException: throws exception if file is not found.
	 */
	public long[] importRecords(String file) throws IOException {
		int worldSize = 0, tableSize = 0, nameLengths = 0;
		
		// Create a new RAF to access our GIS record data and open our database file.
		try (
				RandomAccessFile dataFile = new RandomAccessFile(file, "r");
				DbFileModel newDbFile = DbFileModel.getInstance(dbFileName).open();
		) {
			String line = dataFile.readLine();
			List<String> records = new ArrayList<>();
			long offset = newDbFile.length();
			
			// Go through each line in the data file and populate our table and world.
			while ((line = dataFile.readLine()) != null) {
				String[] values = line.trim().split("\\|");
				records.add(line);
				nameLengths += values[1].length();
				
				if(tableInsert(values, offset)) {
					tableSize++;
				}
				
				if (treeInsert(values, offset)) {
					worldSize++;
				}
				
				offset += (line.length() + 1);
			}
			
			newDbFile.importRecords(records);
			
			// Return an array list with relevant information for our log file.
			return new long[] {tableSize, worldSize, nameLengths / records.size()};
		}
	}
	
	/**
	 * Set the boundaries of our world.
	 * 
	 * @param nLatitude: Horizontal boundary, our Y max.
	 * @param sLatitude: Horizontal boundary, our Y min.
	 * @param wLongitude: Vertical boundary, our X min.
	 * @param eLongitude: Vertical boundary, our X max.
	 * @return: the boundaries of our world in a list format.
	 */
	public long[] getWorldBoundaries(String wLongitude, String eLongitude, String sLatitude, String nLatitude) {
		long yMax = DMSToInt(nLatitude);
		long yMin = DMSToInt(sLatitude);
		
		long xMax = DMSToInt(eLongitude);
		long xMin = DMSToInt(wLongitude);
		
		world = new prQuadTree<>(xMin, xMax, yMin, yMax);
		
		return new long[] {yMax, xMin, xMax, yMin};
	}
	
	/**
	 * Writes the contents of the specified data structure into our log file.
	 * 
	 * @param structure: data structure to view.
	 * @param fw: open to write to our log.
	 * @throws IOException: throws an error if the file isn't found.
	 */
	public void show(String structure, FileWriter fw) throws IOException {
		switch(structure) {
			case "quad":
				world.printTree(world, "\t", fw);
				break;
			case "hash":
				table.display(fw);
				break;
			case "pool":
				bufferPool.display(fw);
				break;
		}
	}
	
	/**
	 * Fetches a record in the db file using its offset.
	 * 
	 * @param dbFile: the database file we're searching in.
	 * @param offset: the offset location of the record we're looking for.
	 * @return: returns the target record.
	 * @throws IOException
	 */
	private String getRecord(DbFileModel dbFile, long offset) throws IOException {
		String record = bufferPool.getElem(offset);
		
		if (record == null) {
			record = dbFile.seekRecord(offset);
			bufferPool.insertElem(record, offset);
		}
		
		return record;
	}
	
	/**
	 * Get every record from our database file that matches a given feature name and state.
	 * 
	 * @param name: name for our target record.
	 * @param state: state for our target record.
	 * @return: return the fetched record(s).
	 * @throws IOException
	 */
	public Map<Long, String> whatIs(String name, String state) throws IOException {
		Map<Long, String> result = new TreeMap<>();
		
		String key = name + "|" + state;
		nameEntry targetEntry = table.find(new nameEntry(key, 0L));
		
		// If we've found our target record, we'll go through all of its locations and fetch them.
		if (targetEntry != null) {
			try (@SuppressWarnings("resource") DbFileModel newDbFile = DbFileModel.getInstance(dbFileName).open()) {
				for (int i = 0; i < targetEntry.locations().size(); i++) {
					long offset = targetEntry.locations().get(i);
					String record = getRecord(newDbFile, offset);
					
					result.put(offset, record);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Get every record from our database file that matches a given set of coordinates.
	 * 
	 * @param latDMS: Latitude DMS coordinate.
	 * @param longDMS: Longitude DMS coordinate.
	 * @return: return the fetched record(s).
	 * @throws IOException
	 */
	public Map<Long, String> whatIsAt(String latDMS, String longDMS) throws IOException {
		Map<Long, String> result = new TreeMap<>();
		
		Point loc = new Point(DMSToInt(longDMS), DMSToInt(latDMS));
		Point locInWorld = world.find(loc);
		
		// If we've found our location, we'll go through all offsets for records that have a specific set of coordinates and fetch them.
		if (locInWorld != null) {
			try (@SuppressWarnings("resource") DbFileModel newDbFile = DbFileModel.getInstance(dbFileName).open()) {
				for (int i = 0; i < locInWorld.getOffsets().size(); i++) {
					long offset = locInWorld.getOffsets().get(i);
					String record = getRecord(newDbFile, offset);
					
					result.put(offset, record);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Get every record from our database file whose coordinates are within a specific region.
	 * 
	 * @param centerLat: Latitude DMS coordinate of the region's center.
	 * @param centerLong: Longitude DMS coordinate of the region's center.
	 * @param halfY: half of the height of the region.
	 * @param halfX: half of the width of the region.
	 * @return: return the fetched record(s).
	 * @throws IOException
	 */
	public Map<Long, String> whatIsIn(String centerLat, String centerLong, long halfY, long halfX) throws IOException {
		Map<Long, String> result = new TreeMap<>();
		
		long centerX = DMSToInt(centerLong);
		long centerY = DMSToInt(centerLat);
		
		ArrayList<Point> locations = world.find(centerX - halfX, centerX + halfX, centerY - halfY, centerY + halfY);
		
		// Go through each location and then get all the offsets within the region.
		if (locations != null) {
			try (DbFileModel newDbFile = DbFileModel.getInstance(dbFileName).open()) {
				for (Point loc : locations) {
					for (Long offset : loc.getOffsets()) {
						String record = getRecord(newDbFile, offset);
						
						result.put(offset, record);
					}
				}
			}
		}
		
		return result;
	}

}
