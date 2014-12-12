package client.batch.input;


import javax.swing.table.AbstractTableModel;

import shared.modal.Field;
import client.batch.state.BatchState;

@SuppressWarnings("serial")
public class BatchTableModel extends AbstractTableModel {

	@Override
	public int getRowCount() {
		if (BatchState.getProject() == null) {
			return 0;
		}
		
		return BatchState.getProject().getRecordsPerBatch();
	}

	@Override
	public int getColumnCount() {
		if (BatchState.getProject() == null) {
			return 0;
		}
		
		//Add an extra column for the row count column
		return BatchState.getProject().getFields().size() + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		//Determine if the column is the row column, otherwise get the value from Batch State
		if (columnIndex == 0) {
			return "" + (rowIndex + 1);
		} else {
			String value = BatchState.getValue(columnIndex - 1, rowIndex);
			if (value == null) {
				return "";
			}
			
			return value;
		}
		
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String value = (String)aValue;
		
		//Decrement one because of the row count column
		BatchState.setValue(columnIndex -1, rowIndex, value);
	}
	
	@Override
	public String getColumnName(int column) {
		//Determine if it is the Row count column or a Field
		if (column == 0) {
			return "Record #";
		} else {
			for (Field field : BatchState.getProject().getFields()) {
				if (field.getColNumber() == column) {
					return field.getTitle();
				}
			}
			return "Unknown";
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		//Do not edit for row count column
		if (columnIndex == 0) {
			return false;
		}
		
		return true;
	}

}
