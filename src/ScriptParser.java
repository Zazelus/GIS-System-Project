import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Handles all commands in provided script files when running the program.
 * 
 * @author Mansour Najah
 */
public class ScriptParser {

	private RandomAccessFile rafScript;
	private String recordFileName;
	private String scriptName;
	private FileWriter fwLog;
	
	private hashTable<nameEntry> table;

	/**
	 * Creates a new ScriptParser object.
	 * 
	 * @param scriptFile: the script file we're accessing and running commands from.
	 * @param logFile:    the file we're writing to after processing commands.
	 * @throws IOException 
	 */
	public ScriptParser(File scriptFile, String logFileName, String scriptFileName) throws IOException {
		rafScript = new RandomAccessFile(scriptFile, "r");
		scriptName = scriptFileName;
		fwLog = new FileWriter(logFileName);
	}

	/**
	 * Executes each script command, running specific functions by switching on the command name.
	 */
	public void executeScript() {
		try {
			String commandLine;
			String[] values;
			
			String command;
			int commandNumber = 0;
			
			fwLog.write("Processing script file: " + scriptName + "\n\n");
			
			// Reading each line in the command file.
			while ((commandLine = rafScript.readLine()) != null) {
				if (commandLine.contains("quit")) {
					commandNumber++;
					quit(commandNumber);
					break;
				}
				// We can skip any lines with ';'.
				if (commandLine.length() > 0) {
					if (commandLine.charAt(0) != ';') {
						values = commandLine.split("\t");
						command = values[0];
						commandNumber++;
							
						if (commandNumber > 1) {
							fwLog.write("\n");
						}
												
						switch(command) {
							case "index":
								index(commandNumber, values);
								break;
							case "what_is":
								whatIs(commandNumber, values);
								break;
							case "show":
								show(commandNumber, values);
								break;
						}
					}
				}
			}
			
			fwLog.close();
			rafScript.close();
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds a specific GIS record based on its offset.
	 * 
	 * @param rafRecords: the file to be accessed.
	 * @param offset: the offset to seek.
	 * @return: our target record.
	 * @throws IOException
	 */
	public String[] findRecord(RandomAccessFile rafRecords, long offset) throws IOException {
		rafRecords.seek(offset);
		
		String record = rafRecords.readLine();
		String[] slicedRecord = record.split("\\|");
		
		return slicedRecord;
	}
	
	/**
	 * Separator for the log file.
	 * @param fw: writes to our file.
	 * @throws IOException
	 */
	public void separate(FileWriter fw) throws IOException {
		fw.write("-----------------------------------------------------");
	}

	/**
	 * Populates our hashtable with GIS records.
	 * @param commandNumber: current command number on script file.
	 * @param values: our string containing the current line in the script.
	 * @throws IOException
	 */
	public void index(int commandNumber, String[] values) throws IOException {
		recordFileName = values[1];
		
		try (RandomAccessFile rafRecords = new RandomAccessFile(values[1], "r")) {
			fwLog.write("Cmd " + commandNumber + " :\tindex\t" + values[1] + "\n");
			separate(fwLog);
			
			String record = rafRecords.readLine();
			String[] line;
			
			long offset  = rafRecords.getFilePointer();
			
			table = new hashTable<nameEntry>(null, null);
			
			// Reading each line in the command file.
			while ((record = rafRecords.readLine()) != null) {
				line = record.split("\\|");
				
				nameEntry entry = new nameEntry(line[1], offset);
				table.insert(entry);
				
				offset = rafRecords.getFilePointer();
			}
		}
	}

	/**
	 * Tells us the type of feature.
	 * @param commandNumber: current command number on script file.
	 * @param values: our string containing the current line in the script.
	 * @throws IOException
	 */
	public void whatIs(int commandNumber, String[] values) throws IOException {
		fwLog.write("Cmd " + commandNumber + " :\twhat_is\t" + values[1] + "\n");
		
		nameEntry target = table.find(new nameEntry(values[1], 4L));
		
		if (target == null) {
			fwLog.write("No record matches " + values[1] + "\n");
		} else {
			for (int i = 0; i < target.locations.size(); i++) {
				RandomAccessFile rafRecords = new RandomAccessFile(recordFileName, "r");
				long offset = target.locations.get(i);
				String[] record = findRecord(rafRecords, offset);
				
				fwLog.write(offset + ":\t" + record[2] + "\t(" 
						+ CoordinateParser.parseLongitude(record[8]) + ", " 
						+ CoordinateParser.parseLatitude(record[7]) + ")\n");
			}
		}
		
		separate(fwLog);
	}

	/**
	 * Shows all slots of the hashtables and their contents.
	 * @param commandNumber: current command number on script file.
	 * @param values: our string containing the current line in the script.
	 * @throws IOException
	 */
	public void show(int commandNumber, String[] values) throws IOException {
		fwLog.write("Cmd " + commandNumber + " :\tshow\t" + values[1] + "\n");
		table.display(fwLog);
		separate(fwLog);
	}

	/**
	 * Ends the log file and stops processing the script.
	 * @param commandNumber: current command number on script file.
	 * @throws IOException
	 */
	public void quit(int commandNumber) throws IOException {
		fwLog.write("\nCmd " + commandNumber + " :\tquit\t" + "\n");
		fwLog.write("Found quit command... ending processing...\n");
		separate(fwLog);
	}

}
