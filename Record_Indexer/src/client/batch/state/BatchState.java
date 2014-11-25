package client.batch.state;

import java.util.ArrayList;

import shared.modal.Batch;
import shared.modal.Project;

public class BatchState {
	
	private ArrayList<BatchStateListener> listeners = new ArrayList<BatchStateListener>();
	private String[][] values;
	private Cell selectedCell;
	private Project project;
	private Batch batch;
		
	private static BatchState instance = null;
	
	public static BatchState singleton() {
		if (instance == null) {
			return new BatchState();
		} else {
			return instance;
		}
	}
	
	//Listeners
	public static void addBatchStateListener(BatchStateListener listener) {
		BatchState.singleton().listeners.add(listener);
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
