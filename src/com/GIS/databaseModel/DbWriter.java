package com.GIS.databaseModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.GIS.world.CoordinateParser;

/**
 * A class to handle writing all results from our script commands to a log file.
 * @author Mansour Najah
 */
public class DbWriter {
	
	private FileWriter fw;
	
	public DbWriter(FileWriter fw) {
		this.fw = fw;
	}
	
	// Writes out results from "world" command execution.
	public void logWorld(String dbFileName, String scriptFileName, String logFileName, long[] boundaries) throws IOException {
		fw.write("GIS Program\n\n");
        fw.write(String.format("dbFile:     %s\n",dbFileName));
        fw.write(String.format("script:     %s\n", scriptFileName));
        fw.write(String.format("log:        %s\n",logFileName));
        fw.write(String.format("Start time: %s\n", new Date().toString()));
        
        fw.write("Quadtree children are printed in the order SW  SE  NE  NW\n");
        fw.write("--------------------------------------------------------------------------------\n\n");
        
        fw.write("Latitude/longitude values in index entries are shown as signed integers, in total seconds.\n\n");
        
        fw.write("World boundaries are set to:\n");
        
        fw.write("\t\t\t" + boundaries[0] + "\n");
        fw.write("\t" + boundaries[1] + "\t\t" + boundaries[2] + "\n");
        fw.write("\t\t\t" + boundaries[3]);
	}
	
	// Writes out results from "import" command execution.
	public void logImport(long[] result) throws IOException {
		fw.write("Imported Features by name: " + result[0] + "\n");
		fw.write("Imported Locations:\t   " + result[1] + "\n");
		fw.write("Average name length:\t   " + result[2]);
	}
	
	// Writes out results from "what_is" command execution.
	public void logWhatIs(String name, String state, Map<Long, String> records) throws IOException {
		if (records.size() == 0) {
			fw.write("No records match " + name + " and " + state + "\n");
		}
		
		for (Long offset : records.keySet()) {
			if (records.get(offset) != null) {
				String[] values = records.get(offset).split("\\|");
				fw.write("\t" + offset + ":\t" + values[5] + "\t(" 
						+ CoordinateParser.parseLongitude(values[8]) + ", " 
						+ CoordinateParser.parseLatitude(values[7]) + ")");
			}
		}
	}
	
	// Writes out results from "what_is_at" command execution.
	public void logWhatIsAt(String latDMS, String longDMS, Map<Long, String> records) throws IOException {
		if (records.size() == 0) {
			fw.write("\tNothing was found at (" + CoordinateParser.parseLongitude(longDMS) + ", " 
					+ CoordinateParser.parseLatitude(latDMS) + ")");
		}
		
		fw.write("\tThe following features were found at (" + CoordinateParser.parseLongitude(longDMS) + ", " 
					+ CoordinateParser.parseLatitude(latDMS) + "):\n");
		
		for (Long offset : records.keySet()) {
			String[] values = records.get(offset).split("\\|");
			fw.write("\t " + offset + ": " + values[1] + " " + values[5] + " " + values[3]);
		}
	}
	
	// Writes out results from "what_is_in" command execution.
	public void logWhatIsIn(String centerLat, String centerLong, long halfY, long halfX, Map<Long, String> records) throws IOException {
		if (records.size() == 0) {
			fw.write("\tNothing was found in (" + CoordinateParser.parseLongitude(centerLong) + " +/- " + halfX + 
					", " + CoordinateParser.parseLatitude(centerLat) + " +/- " + halfY + "\n");
		}
		
		fw.write("\tThe following " + records.size() + " feature(s) were found in (" + CoordinateParser.parseLongitude(centerLong) + " +/- " + halfX + 
					", " + CoordinateParser.parseLatitude(centerLat) + " +/- " + halfY);
		
		for (Long offset : records.keySet()) {
			String[] values = records.get(offset).split("\\|");
			fw.write("\n\t " + offset + ":\t" + values[1] + "\t(" 
					+ CoordinateParser.parseLongitude(values[8]) + ", " 
					+ CoordinateParser.parseLatitude(values[7]) + ")");
		}
	}
	
	// Writes out results from "quit" command execution.
	public void logQuit() throws IOException {
		fw.write("Terminating execution of commands.\n");
		fw.write("End time: " + new Date().toString());
	}
	
}
