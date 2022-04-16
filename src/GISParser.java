import java.io.File;
import java.io.FileNotFoundException;

/**
 * The Runner class will take two or three command parameters in order to find records
 * by index (two parameters) or to search for certain features of records (three parameters).
 * 
 * @author Mansour Najah
 */
public class GISParser {

	/**
	 * Main constructor
	 * 
	 * @param args: files for input
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		// Checking for arguments.
		if (args[0].equals("-index")) {
			File recordFile = new File(args[1]);
			String logFileName = args[2];
			
			GISIndexer indexer = new GISIndexer(recordFile, logFileName);
			indexer.readAndWriteOffsets();
		} 
		
		else if (args[0].equals("-search")) {
			File recordFile = new File(args[1]);
			File commandFile = new File(args[2]);
			String logFileName = args[3];
			
			GISSearcher searcher = new GISSearcher(recordFile, commandFile, logFileName);
			searcher.executeCommands();
		} 
		
		else {
			System.out.println("Not enough command line parameters, or too many.");
			System.out.println("\n Invoke with -index <data file> <output file> or "
					+ "with -search <data file> <command file> <output file>");
		}
	}
}

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
//interfere with the normal operation of the supplied grading code.
//
// Mansour Najah
// mansourn@vt.edu
