package editors.locationeditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import location.LocationData;
import trainers.Actor.DIR;
import utilities.DebugUtility;

/**
 * Simple table model for displaying properties common to all objects. Note that
 * all methods must run inside the event dispatch thread.
 */
public class LocationTableModel extends AbstractTableModel {

	/**
	 * The names of the columns
	 */
	String[] columns = { "name", "canFlyOutOf", "north", "east", "south", "west", "pokemon", "probabilities",
			"minLevels", "maxLevels" };
	/**
	 * The data types of the columns
	 */
	Class<?>[] columnTypes = { String.class, Boolean.class, Integer.class, Integer.class, Integer.class, Integer.class,
			String.class, String.class, String.class, String.class };
	/**
	 * A representation of the current selection
	 */
	Object[] selection;

	/**
	 * Serialization variable
	 */
	private static final long serialVersionUID = 2210111264024740816L;
	/**
	 * A list of the objects in the table
	 */
	private final List<Object> objects = new ArrayList<>();

	/**
	 * Add an object to the table
	 * 
	 * @param obj
	 *            - the object to add
	 */
	public void addObject(Object obj) {
		addObject(obj, this.objects.size());
	}

	/**
	 * Add an object at the given index
	 * 
	 * @param obj
	 *            - the object to add
	 * @param index
	 *            - the location to add the object
	 */
	public void addObject(Object obj, int index) {
		this.objects.add(index, obj);
		fireTableRowsInserted(index, index);
	}

	/**
	 * Remove an object from the table
	 * 
	 * @param obj
	 *            - the object to remove
	 */
	public void removeObject(Object obj) {
		int index = this.objects.indexOf(obj);
		this.objects.remove(index);
		fireTableRowsDeleted(index, index);
	}

	/**
	 * Get an object from the table
	 * 
	 * @param rowIndex
	 *            the row the object is at
	 * @return a representation of the object in the table
	 */
	public Object getObject(int rowIndex) {
		return this.objects.get(rowIndex);
	}

	/**
	 * All cells are editable
	 * 
	 * @param row
	 *            the row of the cell
	 * @param col
	 *            the column of the cell
	 * @return true always
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/**
	 * Set the value of a given cell
	 * 
	 * @param value
	 *            - the value to set
	 * @param row
	 *            - the row of the cell
	 * @param col
	 *            - the column of the cell
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		LocationData obj = (LocationData) this.objects.get(row);
		switch (col) {
		case 0:
			obj.name = (String) value;
			break;
		case 1:
			obj.canFlyOutOf = (Boolean) value;
			break;
		case 2:
			obj.boundaries.put(DIR.NORTH, (Integer) value);
			break;
		case 3:
			obj.boundaries.put(DIR.EAST, (Integer) value);
			break;
		case 4:
			obj.boundaries.put(DIR.WEST, (Integer) value);
			break;
		case 5:
			obj.boundaries.put(DIR.SOUTH, (Integer) value);
			break;
		case 6:
			obj.pokemon = new ArrayList<>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.pokemon.add(s);
			}
			break;
		case 7:
			obj.probabilities = new ArrayList<>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.probabilities.add(Integer.valueOf(s));
			}
			break;
		case 8:
			obj.minLevels = new ArrayList<>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.minLevels.add(Integer.valueOf(s));
			}
			break;
		case 9:
			obj.maxLevels = new ArrayList<>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.maxLevels.add(Integer.valueOf(s));
			}
			break;
		default:
			throw new IndexOutOfBoundsException();
		}

		fireTableCellUpdated(row, col);
	}

	/**
	 * The number of rows in this table
	 */
	@Override
	public int getRowCount() {
		return this.objects.size();
	}

	/**
	 * The number of columns in this table
	 */
	@Override
	public int getColumnCount() {
		if (this.columns.length != this.columnTypes.length) {
			DebugUtility.error("column length != columnTypes length");
		}
		return this.columns.length;
	}

	/**
	 * Get the name of a column
	 * 
	 * @param columnIndex
	 *            the column to get the name of
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex > this.columns.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.columns[columnIndex];
	}

	/**
	 * Get the type of the given column
	 * 
	 * @param columnIndex
	 *            the column to get the type of
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex > this.columnTypes.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.columnTypes[columnIndex];
	}

	/**
	 * Retrieve a value from the table
	 * 
	 * @param rowIndex
	 *            the row to get
	 * @param colIndex
	 *            the column to get
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LocationData obj = (LocationData) this.objects.get(rowIndex);
		return obj.toArray()[columnIndex];
	}
}