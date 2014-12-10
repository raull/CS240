package client.batch.state;

import shared.modal.Batch;
import shared.modal.Project;

public interface BatchStateListener {
	
	public void selectedCellChanged(Cell newSelectedCell);
	
	public void newBatchLoaded(Batch newBatch, Project newProject);
	
	public void valueChanged(Cell editedCell, String value);
}
