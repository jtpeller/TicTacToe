package tictactoe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class TicTacToeGrid extends JTable implements TableModelListener {
	/***** ATTRIBUTES *****/
	private int size,					// # rows/cols
				gridDim;				// width/height of grid
	private Object[][] data;
	private DefaultTableModel dataModel;
	
	/***** CONSTANTS *****/
	private Color backgroundColor = Color.WHITE,
				  foregroundColor = Color.BLACK,
				  borderColor = Color.BLACK;
    
	private Font f1 = new Font("Sans", Font.PLAIN, 28);
	
	/***** CONSTRUCTORS *****/
	public TicTacToeGrid(int size, int gridDim) {
		this.size = size;
		this.gridDim = gridDim;
		this.data = new Object[size][size];
		dataModel = new DefaultTableModel(size, size) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		setModel(dataModel);
		formatTable();
		getModel().addTableModelListener(this);
	}
	
	public TicTacToeGrid(int size, int gridDim, Object[][] data) {
		this.size = size;
		this.gridDim = gridDim;
		this.data = data;
		dataModel = new DefaultTableModel(size, size) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		setModel(dataModel);
		formatTable();
		getModel().addTableModelListener(this);
	}
	
	/***** OPERATIONS *****/
	public boolean checkValueAt(Object value, int row, int col) {
		if (getValueAt(row, col) == value)
			return true;
		return false;
	}
	
	// checks a single row
	public boolean checkRow(int row) {
		if (getValueAt(row, 0) == getValueAt(row, 1) && getValueAt(row, 1) == getValueAt(row, 2))
			return true;
		return false;
	}
	
	// checks a single column
	public boolean checkCol(int col) {
		if (getValueAt(0, col) == getValueAt(1, col) && getValueAt(1, col) == getValueAt(2, col))
			return true;
		return false;
	}
	
	public boolean checkTopDiag() {
		if (getValueAt(0, 0) == getValueAt(1, 1) && getValueAt(1, 1) == getValueAt(2, 2)) 
			return true;
		return false;
	}
	
	public boolean checkBottomDiag() {
		if (getValueAt(2, 0) == getValueAt(1, 1) && getValueAt(1, 1) == getValueAt(0, 2))
			return true;
		return false;
	}
	
	// checks if every row is a winner
	public char checkEveryRow() {
		for (int i = 0; i < size; i++) {
			if (getValueAt(i, 0) != null && getValueAt(i, 0) == getValueAt(i, 1) && getValueAt(i, 0) == getValueAt(i, 2))
				return (char) getValueAt(i, 0);
		}
		return ' ';
	}
	
	// checks if a col is a winner
	public char checkEveryCol() {
		for (int j = 0; j < size; j++) {
			if (getValueAt(0, j) != null && getValueAt(0, j) == getValueAt(1, j) && getValueAt(0, j) == getValueAt(2, j))
				return (char) getValueAt(0, j);
		}
		return ' ';
	}
	
	// checks if a diagonal is a winner
	public char checkEveryDiagonal() {
		if (getValueAt(0, 0) != null && getValueAt(0, 0) == getValueAt(1, 1) && getValueAt(0, 0) == getValueAt(2, 2))
			return (char) getValueAt(0, 0);
		else if (getValueAt(2, 0) != null && getValueAt(2, 0) == getValueAt(1, 1) && getValueAt(2, 0) == getValueAt(0, 2))
			return (char) getValueAt(2, 0);
		else
			return ' ';
	}
	
	// checks if a cat's game occurred. True for cat's game. False otherwise
	public boolean checkCatsGame() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (getValueAt(i, j) == null)
					return false;			// board isn't full yet.
			}
		}
		if (checkEveryRow() != ' ')			// a solution in the row exists, no cat's game
			return false;
		if (checkEveryCol() != ' ')			// a solution in a column exists, no cat's game
			return false;
		if (checkEveryDiagonal() != ' ')	// solution in a diagonal exists, no cat's game
			return false;
		return true;						// no solution exists & board is filled.
	}
	
	// checks if there's a winner
	public char checkWinner() {
		char winner = checkEveryRow();
		if (winner != ' ')
			return winner;
		winner = checkEveryCol();
		if (winner != ' ')
			return winner;
		winner = checkEveryDiagonal();
		if (winner != ' ')
			return winner;
		return ' ';
	}
	
	/***** UTILITY FUNCTIONS *****/
	// checks if the provided array is the proper size for the grid.
	private boolean isValid(Object[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr.length != arr[i].length) {
				return false;
			}
		}
		return true;
	}
	
	// Handles the formatting for the table
	private void formatTable() {
		// appearance
		setBackground(backgroundColor);
		setForeground(foregroundColor);
		
		setShowGrid(false);
		setRowHeight( (int) Math.ceil(gridDim / size));
		for (int i = 0; i < size; i++) {
			getColumnModel().getColumn(i).setPreferredWidth( (int) Math.ceil(gridDim / size));
		}
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment( JLabel.CENTER );
		setDefaultRenderer(Object.class, r);
		
		// functionality
		setRowSelectionAllowed(false);
		setDragEnabled(false);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setTableHeader(null);
	}
	
	/***** STANDARD METHODS *****/
	public void clear() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				setValueAt(null, i, j);
			}
		}
	}
	
	public String toString() {
		String foo = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				foo += getValueAt(i, j);
			}
			foo += '\n';
		}
		return foo;
	}
	
	public void printGrid() {
		System.out.println(toString());
	}
	
	/***** GETTERS & SETTERS *****/
	public int getGridSize() {
		return size;
	}
	
	public int getDimensions() {
		return gridDim;
	}
	
	public Object[][] getData() {
		int size = data.length;
		Object[][] arr = new Object[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = getValueAt(i, j);
			}
		}
		return arr;
	}
	
	public void setBackground(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		super.setBackground(backgroundColor);
	}
	
	public void setForeground(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		super.setForeground(foregroundColor);
	}
	
	public void setData(Object[][] arr) {
		if (!isValid(arr)) {
			System.err.println("setData() error. Array has incorrect dimensions");
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {		// should be a square
				if (arr[i][j] == (Object)0) {
					System.out.println("Setting cell at (" + i + ", " + j + ") via null block");
					setValueAt(null, i, j);				// empty cell to be filled in
				}
				else {
					System.out.println("Setting cell at (" + i + ", " + j + ") via non-null block");
					setValueAt(arr[i][j], i, j);		// default value
				}
			}
		}
		this.data = arr;								// store the array
	}

	@Override
	public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){  
		JComponent c = (JComponent)super.prepareRenderer(renderer, rowIndex, columnIndex);
		c.setFont(f1);
		c.setBorder(BorderFactory.createLineBorder(borderColor, 3));
		return c;
	}
}
