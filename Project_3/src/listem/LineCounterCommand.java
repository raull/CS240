package listem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LineCounterCommand extends FileProcessor implements LineCounter {
	
	private HashMap<File, Integer> filesFound;
	private int curFileLineCount;
	
	public LineCounterCommand() {
		filesFound = new HashMap<File, Integer>();
	}
	
	@Override
	public Map<File, Integer> countLines(File directory,
			String fileSelectionPattern, boolean recursive) {
		
		filesFound.clear();
		curFileLineCount = 0;
		run(directory, fileSelectionPattern, recursive);
		
		return filesFound;
	}
	
	@Override
	protected void fileClosed(File file) {
		filesFound.put(curFile, new Integer(curFileLineCount));
		curFileLineCount = 0;
		super.fileClosed(file);
	}
	
	@Override
	protected void gotNewLine(String line) {
		curFileLineCount++;
	}


}
