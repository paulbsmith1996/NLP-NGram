/**
 * @author - Paul Baird-Smith, February 2018.
 *
 * A Formatter object is used to format a file in such a way that it can be used
 * as training data for an NGram object. Formatter objects take a fileName as a 
 * parameter in their constructor, and can then be used to format this file correctly
 * using the format() method.
 *
 * Formatting a file involves placing a sentence start token at the beginning of every
 * sentence in the text and a sentence end token at the end of every sentence, denoted
 * <s> and </s> repsectively. The Formatter also removes miscellaneous punctuation,
 * notably leaving in apostrophes and parentheses. These are not removed because we
 * believe they may convey extra meaning, as in "we're" vs. "were".
 *
 * Once the given file has been read, and a correctly formatted String has been 
 * generated, this String is writtn to a new file. Given an original file with name
 * "foo.ext", the formatted String is written to the file
 *
 *     "foo_formatted.ext",
 *
 * which is then used as the training data for an NGram object.
 *
 * @email - ppb366@cs.utexas.edu
 */


// Imports
import java.io.File;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Formatter {

    /**
     * Maintain constants for the file name, prefix, and extenstion, as well as
     * the File object itself. Given a fileName "foo.ext", these variables would
     * be set as:
     *
     * fileName = "foo.ext"
     * filePrefix = "foo"
     * fileExt = ".ext"
     */
    private final String fileName;
    private String filePrefix, fileExt;
    private final File file;

    /**
     * Scanner that is used to iterate through the Formatter's file
     */
    private Scanner fileScan;

    /**
     * Tag used in the name of a new formatted file, to denote that it is correctly
     * formatted.
     */
    private static final String FORMAT_TAG = "_formatted";

    /**
     * A list of miscellaneous punctuation that should be removed from the text of
     * the file. Notably missing from the list are apostrophes and parentheses
     */
    private static final String MISC_PNC = ",`~-&^%$#@:;\"";


    /**
     * Constructor for a Formatter object. Initializes all the necessary global vars
     *
     * @param fileName - The name of the training file that the Formatter should 
     * format to fit the necessary properties of the training data for an n-gram
     */
    public Formatter(String fileName) {
	
	// Assign fileName, filePrefix, and fileExt to the appropriate substrings
	this.fileName = fileName;
	int prefEnd = fileName.indexOf(".");
	this.filePrefix = fileName.substring(0, prefEnd);
	this.fileExt = fileName.substring(prefEnd);

	// Find the file with fileName, if it exists
	this.file = new File(fileName);

	// Verify the file is not null
	if(file == null) {
	    System.err.println("ERROR: No readable file with name " + fileName);
	}

	try {
	    
	    // Assign Scanner to scan the training file
	    if(file != null) {
		this.fileScan = new Scanner(file);
	    }
	} catch(FileNotFoundException fe) {
	    System.err.println("ERROR: Invalid file name: " + this.fileName);
	}

    }
    

    /**
     * Formats the file with path fileName, and creates a new formatted file, with
     * the correct style for training data for an NGram
     *
     * @return - A formatted String to be used as training data in the learn()
     * method of an NGram object
     */
    public String format() {
	
	System.out.println("Formatting file: " + this.fileName);
	
	// Create new StringBuffer object, that holds formatted String, and
	// append a start token to the front
	StringBuffer sb = new StringBuffer();
	sb.append(NGram.START_TOKEN + " ");
	
	// Iterate through the words in the training file
	while(fileScan.hasNext()) {

	    // Get next word, determine if it ends a sentence, and remove 
	    // miscellaneous punctuation
	    String nextWord = fileScan.next();
	    if(nextWord.contains(".") 
	       || nextWord.contains("!") 
	       || nextWord.contains("?")) {
		nextWord = nextWord.substring(0, nextWord.length() - 1);
		nextWord += " </s> <s>";
	    }
	    for(int index = 0; index < Formatter.MISC_PNC.length(); index++) {
		String regex = "" + Formatter.MISC_PNC.charAt(index);
		nextWord = nextWord.replaceAll(regex, "");
	    }

	    // Add reformatted word to the formatted String
	    sb.append(nextWord + " ");
	}


	// Create a new file path and retain the location of stdout
	String newFileName = this.filePrefix + Formatter.FORMAT_TAG +  this.fileExt;
	PrintStream stdOut = System.out;

	// Set output stream to the new file path
	try {
	    System.setOut(new PrintStream(newFileName));
	} catch(FileNotFoundException fe) {
	    System.err.println("ERROR: Could not write to file: " + newFileName);
	    return null;
	} catch(SecurityException se) {
	    System.err.println("ERROR: Permission denied for writing to: " + newFileName);
	    return null;
	}

	// Write the formatted String to the new file path
	String text = sb.toString();
	System.out.print(text);

	// Reset output stream to stdout and return the formatted String
	System.setOut(stdOut);
	return text;
	
    }



    // Test performance of Formatter on a file
    public static void main(String[] args) {
	String fileName = "alice.txt";
	Formatter f = new Formatter(fileName);
	f.format();
    }

}