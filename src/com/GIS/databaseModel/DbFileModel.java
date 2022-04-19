package com.GIS.databaseModel;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * A class to model our database file.
 * Should make importing and accessing it a little more intuitive.
 * @author Mansour Najah
 */
public class DbFileModel implements Closeable {
	
	private static DbFileModel dbInstance;
	
	// Attributes for our model.
	private RandomAccessFile rafController;
	private String filePath;
	
	public static DbFileModel getInstance(String file) throws IOException {
		if (dbInstance == null) {
			dbInstance = new DbFileModel(file);
		}
		
		return dbInstance;
	}
	
	/**
	 * Creates a new DbFileModel object and writes a label for each data member.
	 * 
	 * @param filePath: path to the database file.
	 * @throws IOException: throw exception if file not found.
	 */
	private DbFileModel(String filePath) throws IOException {
		this.filePath = filePath;
		
		try (FileWriter fw = new FileWriter(filePath, false)) {
			fw.write("FEATURE_ID|FEATURE_NAME|FEATURE_CLASS|STATE_ALPHA|STATE_NUMERIC|COUNTY_NAME|"
					+ "COUNTY_NUMERIC|PRIMARY_LAT_DMS|PRIM_LONG_DMS|PRIM_LAT_DEC|PRIM_LONG_DEC|SOURCE_LAT_DMS|"
					+ "SOURCE_LONG_DMS|SOURCE_LAT_DEC|SOURCE_LONG_DEC|ELEV_IN_M|ELEV_IN_FT|MAP_NAME|DATE_CREATED|DATE_EDITED\n");
		}
	}
	
	/**
	 * Opens a RandomAccessFile on our db file path.
	 * 
	 * @return: this object.
	 * @throws IOException: throw exception if file not found.
	 */
	public DbFileModel open() throws IOException {
		rafController = new RandomAccessFile(filePath, "rw");
		return this;
	}

	/**
	 * Closes the file as long as the RandomAccessFile returns null.
	 */
	@Override
	public void close() throws IOException {
		if (rafController != null)
			rafController.close();
	}
	
	/**
	 * Imports records from a GIS data file.
	 * 
	 * @param records: records to be imported.
	 * @throws IOException: throw exception if file not found.
	 */
	public void importRecords(List<String> records) throws IOException {
		rafController.seek(rafController.length());
		
		for (int i = 0; i < records.size(); i++)
			rafController.write((records.get(i) + "\n").getBytes());
	}
	
	/**
	 * Seeks to specific record given its offset.
	 * 
	 * @param offset: the offset where the record is located.
	 * @return: the record at our specific offset.
	 * @throws IOException: throw exception if file not found.
	 */
	public String seekRecord(long offset) throws IOException {
		rafController.seek(offset);
		return rafController.readLine();
	}
	
	/**
	 * Gets the current size of the database file.
	 * 
	 * @return: current size of our db file.
	 * @throws IOException: throw exception if file not found.
	 */
	public int length() throws IOException {
		return (int) rafController.length();
	}
	
	
}
