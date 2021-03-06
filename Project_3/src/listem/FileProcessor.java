package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FileProcessor {
	
	protected boolean recursion = false;
	protected File directory;
	protected String fileNamePattern = "";
	protected File curFile;
	
	protected final void processDirectory(File directory) {
		if (!directory.isDirectory()) {
			notDirectory(directory);
			return;
		}
		
		if (!directory.canRead()) {
			cannotReadDirectory(directory);
		}
		
		File[] filesInDirectoryFiles = directory.listFiles();
		
		for (File file : filesInDirectoryFiles) {
			if (file.isDirectory() && recursion) {
				processDirectory(file);
			}
			else {
				Pattern pattern = Pattern.compile(fileNamePattern);
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.matches()) {
					fileMatched(file);
					processFile(file);
				}
			}
		}
		return;
	}
	
	protected final void processFile(File file) {
		if (!file.isFile()) {
			notFile(file);
		}
		try {
			Scanner scanner = new Scanner(file);
						
			while (scanner.hasNextLine()) {
				
				String line = scanner.nextLine();
				gotNewLine(line);
				
			}
			
			fileClosed(file);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} 
	}
	
	protected void run(File baseDirectory, String fileNamePattern, boolean recursive) {
		this.recursion = recursive;
		this.fileNamePattern = fileNamePattern;
		this.directory = baseDirectory;
		
		processDirectory(baseDirectory);
	}
	
	//Call backs to Subclasses
	protected void cannotReadDirectory(File directory) {
		System.out.println("You do not have permission to read directory" + directory.getAbsolutePath());
	}
	
	protected void cannotReadFile(File file) {
		System.out.println("You do not have permission to read file " + file.getAbsolutePath());
	}
	
	protected void notDirectory(File directory) {
		System.out.println(directory.getName() + " is not a directory");
	}
	
	protected void notFile(File file) {
		System.out.println(file.getName() + " is not a file");
	}
	
	protected void fileMatched(File file) {
		curFile = file;
	}
	
	protected void fileClosed(File file) {
		curFile = null;
	}
	
	protected void gotNewLine(String line) {
	}
	
	// main Method
	public static void main(String[] args) {
		GrepCommand fileProcessor = new GrepCommand();
		LineCounterCommand lineCounter = new LineCounterCommand();
		File directory = new File("/Users/raull/Documents/CS240");
		System.out.println(fileProcessor.grep(directory, ".*\\.(java|txt)", "c.*s", true));
		System.out.println(lineCounter.countLines(directory, ".*\\.java", true));
	}
}
