import java.io.File;
import java.io.IOException;

//On my honor:
//
//- I have not discussed the Java language code in my program with
//anyone other than my instructor or the teaching assistants
//assigned to this course.
//
//- I have not used Java language code obtained from another student,
//or any other unauthorized source, including the Internet, either
//modified or unmodified.
//
//- If any Java language code or documentation used in my program
//was obtained from another source, such as a text book or course
//notes, that has been clearly noted with a proper citation in
//the comments of my program.
//
//- I have not designed this program in such a way as to defeat or
//interfere with the normal operation of the grading code.
//
//<Mansour Najah>
//<mansourn>

/**
 * Checks arguments and creates files accordingly.
 * Starts the script process.
 * @author Mansour Najah
 */
public class GIS {

	/**
	 * Main constructor
	 * 
	 * @param args: files for input
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// Checking for arguments.
		if (args.length == 3) {
			String dbFileName = args[0];
			File scriptFile = new File(args[1]);
			String scriptFileName = args[1];
			String logFileName = args[2];

			ScriptParser parser = new ScriptParser(dbFileName, scriptFile, logFileName, scriptFileName);
			parser.executeScript();
		} else if (args.length < 3){
			System.out.println("Not enough command line parameters.");
			System.out.println("\n Invoke with: java nameIndexer <script file name> <log file name>");
		} else if (args.length > 3) {
			System.out.println("Too many command line parameters.");
			System.out.println("\n Invoke with: java nameIndexer <script file name> <log file name>");
		} else {
			System.out.println("\n Invoke with: java nameIndexer <script file name> <log file name>");
		}

	}

}