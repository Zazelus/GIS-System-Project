import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Performs the functions needed to compute record file offsets.
 * @author Mansour Najah
 */
public class GISIndexer {
	
	private File recordFile;
	private String logFileName;
	
	/**
	 * This constructor takes two files that will be used to compute offsets.
	 * 
	 * @param recordFile: contains records to search
	 * @param logFile: stores the output of resulting commands
	 */
	public GISIndexer(File recordFile, String logFileName) {
		this.recordFile = recordFile;
		this.logFileName = logFileName;
	}
	
	/**
	 * Reads offsets from our records file and writes the corresponding feature name.
	 */
	public void readAndWriteOffsets() {
		try {
			RandomAccessFile rafRecords = new RandomAccessFile(recordFile, "r");
			File logFile = new File(logFileName);
			RandomAccessFile rafLog = new RandomAccessFile(logFile, "rw");
			
			rafLog.writeBytes(logFileName + " contains the following records:\n");
			
			String record = rafRecords.readLine(); // Skip past the first line.
			String[] values;
			
			long offset = rafRecords.getFilePointer();
			
			while ((record = rafRecords.readLine()) != null) {
				values = record.split("\\|");
				writeOffsets(offset, values, rafLog);
				offset = rafRecords.getFilePointer();
			}

			rafRecords.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes offsets to log file.
	 * @param offset the given offset
	 * @param values array of values representing the record
	 * @param log the file to be written to
	 */
	public void writeOffsets(long offset, String[] values, RandomAccessFile log) {
		try {
			log.writeBytes("\n\t" + offset + "\t" + values[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
