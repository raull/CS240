package shared.communication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;
/**
 * Download Batch Object so the client and Server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class DownloadBatch_Response {
	
	//-------------------Instance Fields-------------------------
	/**
	 * Batch returned from the Database, containing the fields as well
	 */
	private Batch batch;
	/**
	 * Message for the request
	 */
	private String output = "FAILED";
	/**
	 * The list of fields for the same project as the batch
	 */
	private List<Field> fields;
	/**
	 * The project id where everything belongs to
	 */
	private Project project;
	
	
	//-----------------Constructors----------------------------
	public DownloadBatch_Response(Batch batch, String output, Project project) {
		this.batch = batch;
		this.output = output;
		this.project = project;
	}
	
	public DownloadBatch_Response() {
		
	}
	
	//-----------------Setters and Getters--------------------
	/**
	 * @return the batch
	 */
	public Batch getBatch() {
		return batch;
	}
	/**
	 * @param batch the batch to set
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}


	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}


	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * @return the projectId
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	
	//------------------To String----------------
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (output.equals("TRUE")) {
			builder.append(batch.getId() + "\n");
			builder.append(project.getId() + "\n");
			builder.append(batch.getFilePath() + "\n");
			builder.append(project.getFirstYCood() + "\n");
			builder.append(project.getRecordHeight() + "\n");
			builder.append(project.getRecordsPerBatch() + "\n");
			builder.append(fields.size() + "\n");
			
			for (Field field : fields) {
				builder.append(field.getId() + "\n");
				builder.append(field.getColNumber() + "\n");
				builder.append(field.getTitle() + "\n");
				builder.append(field.getHelpHtml() + "\n");
				builder.append(field.getxCoord() + "\n");
				builder.append(field.getWidth() + "\n");
				if (field.getKnownData() != null && !field.equals("")) {
					builder.append(field.getKnownData() + "\n");
				}
			}
		} else {
			builder.append(output + "\n");
		}
		
		return builder.toString();
	}
	
	
	public void attachURLToOutput(String url) {
		ArrayList<Field> newFields = new ArrayList<Field>();
		for (Field field : fields) {
			field.setHelpHtml(url + File.separator + field.getHelpHtml());
			if (field.getKnownData() != null) {
				field.setKnownData(url + File.separator + field.getKnownData());
			}
			newFields.add(field);
		}
		fields = newFields;
		batch.setFilePath(url + File.separator + batch.getFilePath());
	}
	
}
