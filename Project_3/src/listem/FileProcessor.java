package listem;

import java.io.File;

public class FileProcessor {
	
	private boolean _recursion = false;
	private File _directory;
	private String _fileNamePattern = "";
	
	
	protected FileProcessor(File directory, String fileNamePattern, boolean recursive) {
		_recursion = recursive;
		_directory = directory;
		_fileNamePattern = fileNamePattern;
	}
	
	protected FileProcessor() {
		
	}
	
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
			System.out.println(file.getAbsolutePath());
			if (file.isDirectory() && _recursion) {
				processDirectory(file);
			}
			else {
				if (file.getName().matches(_fileNamePattern)) {
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
		System.out.println(file.getAbsolutePath() + " found");
	}
	
	// main Method
	public static void main(String[] args) {
		FileProcessor fileProcessor = new FileProcessor();
		File directory = new File("/Users/raull/Documents/CS240/Project_3/src/listem");
		fileProcessor.processDirectory(directory);
	}
}
