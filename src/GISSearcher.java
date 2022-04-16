import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Performs the functions needed to perform record searches.
 * @author Mansour Najah
 */
public class GISSearcher {
	
	private RandomAccessFile rafRecords;
	private RandomAccessFile rafCommands;
	private File logFile;
	private RandomAccessFile rafLog;
	
	/**
	 * This constructor takes three files that will be used to make the searches.
	 * 
	 * @param recordFile: contains records to search
	 * @param commandFile: contains commands to run
	 * @param logFile: stores the output of resulting commands
	 * @throws FileNotFoundException 
	 */
	public GISSearcher(File recordFile, File commandFile, String logFileName) throws FileNotFoundException {
		rafRecords = new RandomAccessFile(recordFile, "r");
		rafCommands = new RandomAccessFile(commandFile, "r");
		logFile = new File(logFileName);
		rafLog = new RandomAccessFile(logFile, "rw");
	}
	
	/**
	 * Runs each command in the given command file.
	 */
	public void executeCommands() {
		try {
			GISCommandParser commandP = new GISCommandParser(rafRecords, rafLog);
			String commandLine;
			String[] values;
			
			long offset = 0;
			String command;
			int commandNumber = 0;
			
			// Reading each line in the command file.
			while ((commandLine = rafCommands.readLine()) != null) {
				if (commandLine.contains("quit")) {
					commandNumber++;
					commandP.quit(commandNumber);
					break;
				}
				// We can skip any lines with ';'.
				if (commandLine.charAt(0) != ';') {
					values = commandLine.split("\t");
					offset = Long.parseLong(values[1]);
					command = values[0];
					commandNumber++;
					
					// Check if it's a valid offset before executing the command.
					if (commandP.isValidOffset(offset, values[0], commandNumber)) {
						if (commandNumber > 1) {
							rafLog.writeBytes("\n");
						}
						
						switch(command) {
							case "show_name":
								commandP.showName(offset, command, commandNumber);
								break;
							case "show_latitude":
								commandP.showLatitude(offset, command, commandNumber);
								break;
							case "show_longitude":
								commandP.showLongitude(offset, command, commandNumber);
								break;
							case "show_elevation":
								commandP.showElevation(offset, command, commandNumber);
								break;
						}
					}
					
				}
			}

			rafCommands.close();
			rafRecords.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
