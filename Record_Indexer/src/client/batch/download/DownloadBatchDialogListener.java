package client.batch.download;

import shared.modal.Batch;
import shared.modal.Project;

public interface DownloadBatchDialogListener {
	
	public void newBatchDownloaded(Project project, Batch batch);
}
