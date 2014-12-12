package client.batch.download;

import shared.modal.Batch;
import shared.modal.Project;

public interface DownloadBatchDialogListener {
	
	//Notifies the listeners that a new Batch has been downloaded
	public void newBatchDownloaded(Project project, Batch batch);
}
