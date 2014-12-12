package client.batch.info;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;
import client.batch.state.BatchState;
import client.batch.state.BatchStateListener;
import client.batch.state.Cell;

@SuppressWarnings("serial")
public class FieldInfoComponent extends JComponent {

	private JEditorPane htmlViewer;
	
	public FieldInfoComponent() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setupGUI();
		
		BatchState.addBatchStateListener(new FieldInfoBatchStateListener());
		
		
	}
	
	//Set up a JeditPane for HTML display
	private void setupGUI() {
		this.removeAll();
		
		htmlViewer = new JEditorPane();
		htmlViewer.setContentType("text/html");
		
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		this.add(scrollPane);

	}
	
	
	//Update the HTML Page
	public void setURL(String url) {
		try {
			htmlViewer.setPage(url);
		} catch (Exception e) {
			System.out.println("Error trying to load field info: " + e.getLocalizedMessage());
		}
	}
	
	
	private class FieldInfoBatchStateListener implements BatchStateListener {

		@Override
		public void selectedCellChanged(Cell newSelectedCell) {
			
			//Error checking
			if (BatchState.getBatch() != null) {
				Cell cell = new Cell(newSelectedCell.getColumn(), newSelectedCell.getRow());
				
				
				//Determine the field
				if (cell.getColumn() == 0) {
					cell.setColumn(cell.getColumn() + 1);
				}
				
				for (Field field : BatchState.getProject().getFields()) {
					if (field.getColNumber() == cell.getColumn()) {
						//Update HTML
						String html =  field.getHelpHtml();
						setURL(html);
						break;
					}
				}
			} else {
				htmlViewer = new JEditorPane();
			}
			
			
		}

		@Override
		public void newBatchLoaded(Batch newBatch, Project newProject) {
			
			//Reset the GUI
			setupGUI();
			repaint();
			
		}

		@Override
		public void valueChanged(Cell editedCell, String value) {
			// Do nothing
			
		}
		
	}
	
	
}
