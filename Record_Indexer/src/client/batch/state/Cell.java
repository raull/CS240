package client.batch.state;

public class Cell {
	
	private int row;
	private int column;
	
	public Cell(int column, int row) {
		this.row = row;
		this.column = column;
	}

	
	//Getters and Setters

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}	
	
}
