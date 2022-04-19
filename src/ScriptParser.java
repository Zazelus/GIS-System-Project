import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import com.GIS.databaseModel.DbController;
import com.GIS.databaseModel.DbWriter;

/**
 * Handles all commands in provided script files when running the program.
 * 
 * @author Mansour Najah
 */
public class ScriptParser {

	private String dbFileName;
	
	private RandomAccessFile rafScript;
	private String scriptName;
	
	private String logName;
	private FileWriter fwLog;
	
	/**
	 * Creates a new ScriptParser object.
	 * 
	 * @param scriptFile: the script file we're accessing and running commands from.
	 * @param logFile:    the file we're writing to after processing commands.
	 * @throws IOException 
	 */
	public ScriptParser(String dbFileName, File scriptFile, String logFileName, String scriptFileName) throws IOException {
		this.dbFileName = dbFileName;
		rafScript = new RandomAccessFile(scriptFile, "r");
		scriptName = scriptFileName;
		logName = logFileName;
		fwLog = new FileWriter(logFileName);
	}

	/**
	 * Executes each script command, running specific functions by switching on the command name.
	 */
	public void executeScript() {
		try {
			DbController dbController = new DbController(dbFileName);
			DbWriter dbWriter = new DbWriter(fwLog);
			String commandLine;
			String[] values;
			
			String command;
			int commandNumber = 0;
			
			fwLog.write("Processing script file: " + scriptName + "\n\n");
			
			// Reading each line in the command file.
			while ((commandLine = rafScript.readLine()) != null) {
				commandLine = commandLine.trim();
				
				// Check for semicolons and empty lines, just write them to log as is.
				while (commandLine.startsWith(";") || commandLine.length() == 0) {
					commandLine = rafScript.readLine();
				}
				
				values = commandLine.split("\t");
				command = values[0];
				
				if (command.equals("world")) {
					fwLog.write(commandLine + "\n\n");
				} else {
					commandNumber++;
					fwLog.write("\nCommand " + commandNumber + ": " + commandLine + "\n\n");
				}
				
				switch(command) {
					case "world":
						long[] boundaries = dbController.getWorldBoundaries(values[1], values[2], values[3], values[4]);
						dbWriter.logWorld(dbFileName, scriptName, logName, boundaries);
						break;
					case "import":
						long[] result = dbController.importRecords(values[1]);
						dbWriter.logImport(result);
						break;
					case "what_is":
						Map<Long, String> records = dbController.whatIs(values[1], values[2]);
						dbWriter.logWhatIs(values[1], values[2], records);
						break;
					case "what_is_at":
						records = dbController.whatIsAt(values[1], values[2]);
						dbWriter.logWhatIsAt(values[1], values[2], records);
						break;
					case "what_is_in":
						records = dbController.whatIsIn(values[1], values[2], Long.parseLong(values[3]), Long.parseLong(values[4]));
						dbWriter.logWhatIsIn(values[1], values[2], Long.parseLong(values[3]), Long.parseLong(values[4]), records);
						break;
					case "show":
						dbController.show(values[1], fwLog);
						break;
					case "quit":
						dbWriter.logQuit();
						break;
						
				}
				fwLog.write("\n--------------------------------------------------------------------------------");
			}
			
			fwLog.close();
			rafScript.close();
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
