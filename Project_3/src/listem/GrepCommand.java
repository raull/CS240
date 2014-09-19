package listem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrepCommand extends FileProcessor implements Grep {
	
	private HashMap<File, List<String>> filesFound;
	private ArrayList<String> linesFoundForFile;
	private String substringPattern;
	
	public GrepCommand() {
		filesFound = new HashMap<File, List<String>>();
	}
	
	@Override
	public Map<File, List<String>> grep(File directory,
			String fileSelectionPattern, String substringSelectionPattern,
			boolean recursive) {
		
		filesFound.clear();
		substringPattern = substringSelectionPattern;
		run(directory, fileSelectionPattern, recursive);
		
		return filesFound;
	}
	
	@Override
	protected void fileMatched(File file) {
		super.fileMatched(file);
		linesFoundForFile = new ArrayList<String>();
	}
	
	@Override
	protected void fileClosed(File file) {
		filesFound.put(curFile, linesFoundForFile);
		linesFoundForFile = null;
		
		super.fileClosed(file);
	}
	
	@Override
	protected void gotNewLine(String line) {
		if (line.contains(substringPattern)) {
			linesFoundForFile.add(line);
		}
	}

}
