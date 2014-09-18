package listem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
	
	private boolean _recursion = false;
	private File _directory;
	private String _fileNamePattern = "";
	
	protected void File(File directory, String fileNamePattern, boolean recursive) {
		_recursion = recursive;
		_directory = directory;
		_fileNamePattern = fileNamePattern;
	}
	
	protected final void processDirectory(File directory) {
		ArrayList<File> foundFiles = new ArrayList<File>();
		File[] filesInDirectoryFiles = directory.listFiles();
		
		for (File file : filesInDirectoryFiles) {
			System.out.println(file.getAbsolutePath());
			if (file.isDirectory() && _recursion) {
				processDirectory(file);
			}
			else {
				if (file.getName().matches(_fileNamePattern)) {
					foundFiles.add(file);
				}
			}
		}
	}
	
	protected final void processFile(File file) {
		
	}
	
	public static void main(String[] args) {
		FileProcessor fileProcessor = new FileProcessor();
		File directory = new File("/Users/raull/Documents/CS240/Project_3/src/listem");
		fileProcessor.processDirectory(directory);
	}
}
