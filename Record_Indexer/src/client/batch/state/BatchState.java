package client.batch.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.facade.ClientFacade;
import shared.modal.Batch;
import shared.modal.Project;

public class BatchState {
	
	private transient ArrayList<BatchStateListener> listeners = new ArrayList<BatchStateListener>();
	
	private transient String[][] values;
	private Cell selectedCell;
	private Project project;
	private Batch batch;
		
	private transient static BatchState instance = null;
	
	private static XStream stream = new XStream(new DomDriver());
	
	private BatchState() {
		
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
				listener.newBatchDownloaded(BatchState.getBatch(), BatchState.getProject());
			}
	        
		} catch (Exception e) {
			
		}
	}
	
	//Getters and Setters
	public static void setBatchSize(int numColumns, int numRows) {
		BatchState.singleton().values = new String[numColumns][numRows];
	}
	
	public static void setValue(Cell cell) {
		BatchState.singleton().values[cell.getColumn()][cell.getRow()] = cell.getValue();
	}
	
	public static String getValue(int column, int row) {
		return BatchState.singleton().values[column][row];
	}
	
	public static Cell getSelectedCell() {
		return BatchState.singleton().selectedCell;
	}
	
	public static void setSelectedCell(Cell selectedCell) {
		BatchState.singleton().selectedCell = selectedCell;
		
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
	}
	
}