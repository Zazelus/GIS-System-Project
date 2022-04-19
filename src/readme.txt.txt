Requirements: Java 11

1. Setup and Compilation:
	* Extract the archive contents into a new folder/directory.
	* Using the command line or shell equivalent, navigate to the directory containing the 'com' folder and the GIS and ScriptParser java files.
	* Invoke the java compiler: 'javac *.java' (without the single quotes)
		-> This will also compile all the .java files in the com folder. If it doesn't, proceed to invoke javac *.java in all of the folders within 'com'.

2. Running The Project:
	* In the command line or shell equivalent, run the program using: 'java GIS <database_file> <script_file> <log_file>' (without the single quotes, replace carroted <> words including the <> characters)
	* <database_file>: Should be the name of the database file to be created, make sure to specify .txt
	* <script_file>: Should already exist within the current directory, or one must be made by the user. Also specify .txt
	* <log_file>: Should be the name of the log file to be created, make sure to specify .txt

3. The Main Components:
	* You can find the hash table implementation within com.GIS.hashTable along with relevant classes.
	* You can find the PRQuadtree implementation within com.GIS.world as well as Point.java which is the data type used in the tree. You will also find relevant classes here.
	* You can find the Bufferpool implementation within com.GIS.databaseModel.
	* GIS records are parsed through DbController.java based on specific script commands, as such there is no GIS class implementation. DbController.java is found within com.GIS.databaseModel
	* Feature name and location indexes are dealt with by using nameEntry.java and hashTable.java which reside in com.GIS.hashTable

Enjoy :)