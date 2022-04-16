import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Handles the execution of commands called by the GISSearcher class.
 * @author Mansour Najah
 */
public class GISCommandParser {
	
	private RandomAccessFile rafRecords;
	private RandomAccessFile rafLog;
	
	/**
	 * Creates a new GISCommandParser object.
	 * 
	 * @param rafRecords the records we want to access
	 * @param rafLog the file that we are writing to.
	 */
	public GISCommandParser (RandomAccessFile rafRecords, RandomAccessFile rafLog) {
		this.rafRecords = rafRecords;
		this.rafLog = rafLog;
	}
	
	/**
	 * Finds the record given a valid offset, which we check for before using
	 * this function.
	 * @param offset the offset that raf will seek
	 * @return an array of strings after splitting the record of its '|' characters.
	 * @throws IOException
	 */
	public String[] findRecord(long offset) throws IOException {
		rafRecords.seek(offset);
		
		String record = rafRecords.readLine();
		String[] slicedRecord = record.split("\\|");
		
		return slicedRecord;
	}
	
	/**
	 * Checks if the offset is valid.
	 * @param offset the offset to check
	 * @param command the name of the command being executed
	 * @param commandNumber how many commands have been executed
	 * @return False if negative, larger than the file or not pointing to the beginning of the file.
	 * 		   True otherwise.
	 * @throws IOException
	 */
	public boolean isValidOffset(long offset, String command, int commandNumber) throws IOException {
		// Must be non-negative.
		if (offset < 0) {
			rafLog.writeBytes("\n" + commandNumber + ":" + "\t" + command + "\t\t" + offset);
			rafLog.writeBytes("\n\tOffset is not positive");
			
			return false;
		} else if (offset > rafRecords.length()) { // Must not be longer than the data file.
			rafLog.writeBytes("\n" + commandNumber + ":" + "\t" + command + "\t\t" + offset);
			rafLog.writeBytes("\n\tOffset is too large");
			
			return false;
		} else { // Must not be unaligned.
			String[] record = findRecord(offset);
			
			// Checking if the first part of the given line is a proper long value, this should
			// always be the first element we encounter, if not then the offset is unaligned.
			try {
				Long.parseLong(record[0]);
				return true;
			} catch (Exception e) {
				rafLog.writeBytes("\n" + commandNumber + ":" + "\t" + command + "\t\t" + offset);
				rafLog.writeBytes("\n\tOffset is unaligned");
				return false;
			}
		}
	}
	
	/**
	 * Shows the record's name.
	 * @param offset the offset to seek
	 * @param command the command being executed
	 * @param commandNumber how many commands have been executed
	 * @throws IOException
	 */
	public void showName(long offset, String command, int commandNumber) throws IOException {
		String[] record = findRecord(offset);
		
		rafLog.writeBytes(commandNumber + ":" + "\t" + command + "\t\t" + offset);
		rafLog.writeBytes("\n\t" + record[1]);
	}
	
	/**
	 * Parses latitude.
	 * @param offset the offset to seek
	 * @param command the command being executed
	 * @param commandNumber how many commands have been executed
	 * @throws IOException
	 */
	public void showLatitude(long offset, String command, int commandNumber) throws IOException {
		String[] record = findRecord(offset);
		
		rafLog.writeBytes(commandNumber + ":" + "\t" + command + "\t\t" + offset);
		String latitude = CoordinateParser.parseLatitude(record[7]);
		rafLog.writeBytes("\n\t" + latitude);
	}
	
	/**
	 * Parses longitude.
	 * @param offset the offset to seek
	 * @param command the command being executed
	 * @param commandNumber how many commands have been executed
	 * @throws IOException
	 */
	public void showLongitude(long offset, String command, int commandNumber) throws IOException {
		String[] record = findRecord(offset);
		
		rafLog.writeBytes(commandNumber + ":" + "\t" + command + "\t\t" + offset);
		String longitude = CoordinateParser.parseLongitude(record[8]);
		rafLog.writeBytes("\n\t" + longitude);
	}
	
	/**
	 * Parses elevation.
	 * @param offset
	 * @param command
	 * @param commandNumber
	 * @throws IOException
	 */
	public void showElevation(long offset, String command, int commandNumber) throws IOException {
		String[] record = findRecord(offset);
		
		rafLog.writeBytes(commandNumber + ":" + "\t" + command + "\t\t" + offset);
		
		if (record[16].equals("")) {
			rafLog.writeBytes("\n\tElevation is not given");
		} else {
			rafLog.writeBytes("\n\t" + record[16]);
		}
	}
	
	public void quit(int commandNumber) throws IOException {
		rafLog.writeBytes("\n" + commandNumber + ":" + "\tquit");
		rafLog.writeBytes("\n\tExiting");
	}

}
