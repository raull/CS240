package client.batch.state;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.facade.ClientFacade;
import client.spell.SpellingCorrector;
import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;

public class BatchState {
	
	private transient ArrayList<BatchStateListener> listeners = new ArrayList<BatchStateListener>();
	
	//State values to save
	private ArrayList<Cell> errorCells = new ArrayList<Cell>();

	private String[][] values;
	private Cell selectedCell;
	private Project project;
	private Batch batch;
	private Boolean highlight = true;
	private Boolean inverted = false;
	private Point mainFrameLocation;
	private Point batchImageCenter;
	private Dimension mainFrameDimension;
	private double scale;
		
	private int hDividerLocation;
	private int vDividerLoction;
			
	//Singleton Instance
	private transient static BatchState instance = null;
	
	//Serializer
	private static XStream stream = new XStream(new DomDriver());
	
	private BatchState() {
		//Nothing to do here
	}
	
	public static BatchState singleton() {
		if (instance == null) {
			instance = new BatchState();
			return instance;
		} else {
			return instance;
		}
	}
	
	
	//Listeners
	public static void addBatchStateListener(BatchStateListener listener) {
		BatchState.singleton().listeners.add(listener);
	}
	
	//Methods
	public static void save() {
		try {
			String path = "user_state/" + ClientFacade.getUsername() + ".xml";
			FileOutputStream fileOutput = new FileOutputStream(path);
			stream.toXML(BatchState.singleton(), fileOutput);
			fileOutput.close();
			
		} catch (Exception e) {
			
		}
		
	}
	
	//Load state from memory
	public static void load(String username) {
		BatchState state = null;
		try {
			String path = "user_state/" + ClientFacade.getUsername() + ".xml";
			FileInputStream fileIn = new FileInputStream(path);
	        state = (BatchState)stream.fromXML(fileIn);
	        fileIn.close();
	        
	        System.out.println("Loaded Batch: " + state.batch);
	        
	        state.listeners = BatchState.singleton().listeners;
	        instance = state;
	        	        
	        for (BatchStateListener listener : BatchState.singleton().listeners) {
				listener.newBatchLoaded(BatchState.getBatch(), BatchState.getProject());
			}
	        
	        //Select it by default
	        BatchState.setSelectedCell(new Cell(1, 0));
	        
		} catch (Exception e) {
			
		}
	}
	
	//Restart Batch State parameters when a batch has been submitted
	public static void restart() {
		BatchState.setBatchSize(0, 0);
		BatchState.setProject(null);
		BatchState.setBatch(null);
		BatchState.singleton().errorCells = new ArrayList<Cell>();
		BatchState.save();
	}
	
	//Getters and Setters
	public static void setBatchSize(int numColumns, int numRows) {
		BatchState.singleton().values = new String[numColumns][numRows];
	}
	
	public static String[][] getAllValues() {
		return BatchState.singleton().values;
	}
	
	public static void setAllValues(String[][] values) {
		BatchState.singleton().values = values;
	}
	
	public static String getValue(int column, int row) {
		String value = BatchState.singleton().values[column][row];
		
		//Return an empty string in case of null
		if (value == null) {
			return "";
		}
				
		return BatchState.singleton().values[column][row];
	}
	
	public static void setValue(int column, int row, String value) {
		if (BatchState.getBatch() == null) {
			return;
		}
				
		BatchState.singleton().values[column][row] = value;
		ArrayList<String> suggestions = null;
		
		//Check For spelling corrections
		if (!value.isEmpty()) {
			suggestions = (ArrayList<String>)BatchState.checkValue(value, new Cell(column, row));
		}
		
		if (suggestions != null) {
			BatchState.addErrorCell(new Cell(column, row));
		} else {
			BatchState.removeCellError(new Cell(column, row));
		}
		
		for (BatchStateListener listener : BatchState.singleton().listeners ) {
			listener.valueChanged(new Cell(column, row), value);
		}
	}
	
	public static Cell getSelectedCell() {
		return BatchState.singleton().selectedCell;
	}
	
	public static void setSelectedCell(Cell selectedCell) {
		BatchState.singleton().selectedCell = selectedCell;
		
		//Notify listeners for synchronization
		for (BatchStateListener listener : BatchState.singleton().listeners ) {
			listener.selectedCellChanged(selectedCell);
		}
	}

	/**
	 * @return the project
	 */
	public static Project getProject() {
		return BatchState.singleton().project;
	}

	/**
	 * @param project the project to set
	 */
	public static void setProject(Project project) {
		BatchState.singleton().project = project;
	}

	/**
	 * @return the batch
	 */
	public static Batch getBatch() {
		return BatchState.singleton().batch;
	}

	/**
	 * @param batch the batch to set
	 */
	public static void setBatch(Batch batch) {
		BatchState.singleton().batch = batch;
				
		//Notify listeners that a Batch has been either loaded or downloaded
		for (BatchStateListener listener : BatchState.singleton().listeners) {
			listener.newBatchLoaded(BatchState.getBatch(), BatchState.getProject());
		}
		
		BatchState.setSelectedCell(new Cell(1, 0));
	}
	
	public static void setHighlight(Boolean highlight) {
		BatchState.singleton().highlight = highlight;
	}
	
	public static Boolean getHighlight() {
		return BatchState.singleton().highlight;
	}
	
	public static void setInverted(Boolean inverted) {
		BatchState.singleton().inverted = inverted;
	}
	
	public static Boolean getInverted() {
		return BatchState.singleton().inverted;
	}
	
	public static void setMainFrameLocation(Point location) {
		BatchState.singleton().mainFrameLocation = location;
	}
	
	public static Point getMainFrameLocation() {
		return BatchState.singleton().mainFrameLocation;
	}
	
	public static void setMainFrameDimension(Dimension dimension) {
		BatchState.singleton().mainFrameDimension = dimension;
	}
	
	public static Dimension getMainFrameDimension() {
		return BatchState.singleton().mainFrameDimension;
	}
	
	public static void setScale(double scale) {
		BatchState.singleton().scale = scale;
	}
	
	public static double getScale() {
		return BatchState.singleton().scale;
	}
	
	public static void setBatchImageCenter(Point center) {
		BatchState.singleton().batchImageCenter = center;
	}
	
	public static Point getBatchImageCenter() {
		return BatchState.singleton().batchImageCenter;
	}
	
	//Add a unique error cell
	private static void addErrorCell(Cell errorCell) {
		Boolean alreadyExists = isCellError(errorCell);
		
		if (!alreadyExists) {
			BatchState.singleton().errorCells.add(errorCell);
		}
	}
	
	//Perform spell check on the specified cell and value
	public static List<String> checkValue(String value, Cell cell) {
		
		SpellingCorrector corrector = new SpellingCorrector();
		ArrayList<Field> fields = BatchState.getProject().getFields();
		
		for (Field field : fields) {
			if (field.getColNumber() == cell.getColumn() + 1) {
				try {
					if (field.getKnownData() == null) {
						return null;
					} 
					
					URL url = new URL(field.getKnownData());
					
					corrector.useDictionary(url);
				} catch (Exception e) {
					System.out.println("Error loading Dictionary: " + e.getLocalizedMessage());
				}
				
				break;
			}
		}
		
		try {
			ArrayList<String> suggestions = (ArrayList<String>)corrector.suggestSimilarWords(value);
			return suggestions;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
		
	}
	
	//Return if the cell has a spell error or not
	public static Boolean isCellError(Cell errorCell) {
		Boolean alreadyExists = false;
		for (Cell cell : BatchState.singleton().errorCells) {
			if (errorCell.getColumn() == cell.getColumn() && errorCell.getRow() == cell.getRow()) {
				alreadyExists = true;
				break;
			}
		}
		
		return alreadyExists;
	}
	
	//remove cell error, if it is not an erro cell nothing will happen
	private static void removeCellError(Cell errorCell) {
		ArrayList<Cell> cells = BatchState.singleton().errorCells;
		int removeIndex = -1;
		for (int i = 0; i < cells.size(); i++) {
			if (cells.get(i).getColumn() == errorCell.getColumn() && cells.get(i).getRow() == errorCell.getRow()) {
				removeIndex = i;
			}
		}
		
		if (removeIndex >= 0) {
			BatchState.singleton().errorCells.remove(removeIndex);
		}
	}

	/**
	 * @return the hDividerLocation
	 */
	public static int getHorizontalDividerLocation() {
		return BatchState.singleton().hDividerLocation;
	}

	/**
	 * @param hDividerLocation the hDividerLocation to set
	 */
	public static void setHorizaontalDividerLocation(int hDividerLocation) {
		BatchState.singleton().hDividerLocation = hDividerLocation;
	}

	/**
	 * @return the vDividerLoction
	 */
	public static int getVerticalDividerLoction() {
		return BatchState.singleton().vDividerLoction;
	}

	/**
	 * @param vDividerLoction the vDividerLoction to set
	 */
	public static void setVerticalDividerLoction(int vDividerLoction) {
		BatchState.singleton().vDividerLoction = vDividerLoction;
	}
}
