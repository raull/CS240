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
		htmlViewer = new JEditorPane();
		htmlViewer.setContentType("text/html");
		
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		
		BatchState.addBatchStateListener(new FieldInfoBatchStateListener());
		
		this.add(scrollPane);
	}
	
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
			
			Cell cell = new Cell(newSelectedCell.getColumn(), newSelectedCell.getRow());
			
			if (cell.getColumn() == 0) {
				cell.setColumn(cell.getColumn() + 1);
			}
			
			for (Field field : BatchState.getProject().getFields()) {
				if (field.getColNumber() == cell.getColumn()) {
					String html =  field.getHelpHtml();
					setURL(html);
					break;
				}
			}
		}

		@Override
		public void newBatchLoaded(Batch newBatch, Project newProject) {
			// Do nothing
			
		}

		@Override
		public void valueChanged(Cell editedCell, String value) {
			// Do nothing
			
		}
		
	}
	
	
}
