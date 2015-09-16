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

	String[] columns = { "name", "canFlyOutOf", "north", "east", "south", "west", "pokemon", "probabilities",
			"minLevels", "maxLevels" };
	Class<?>[] columnTypes = { String.class, Boolean.class, Integer.class, Integer.class, Integer.class, Integer.class,
			String.class, String.class, String.class, String.class };
	Object[] selection;

	private static final long serialVersionUID = 2210111264024740816L;
	private final List<Object> objects = new ArrayList<Object>();

	public void addObject(Object obj) {
		addObject(obj, objects.size());
	}

	public void addObject(Object obj, int index) {
		objects.add(index, obj);
		fireTableRowsInserted(index, index);
	}

	public void removeObject(Object obj) {
		int index = objects.indexOf(obj);
		objects.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public Object getObject(int rowIndex) {
		return objects.get(rowIndex);
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		LocationData obj = (LocationData) objects.get(row);
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
			obj.pokemon = new ArrayList<String>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.pokemon.add(s);
			}
			break;
		case 7:
			obj.probabilities = new ArrayList<Integer>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.probabilities.add(Integer.parseInt(s));
			}
			break;
		case 8:
			obj.minLevels = new ArrayList<Integer>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.minLevels.add(Integer.parseInt(s));
			}
		case 9:
			obj.maxLevels = new ArrayList<Integer>();
			for (String s : value.toString().replace("[", "").replace("]", "").split(",")) {
				obj.maxLevels.add(Integer.parseInt(s));
			}
		default:
			throw new IndexOutOfBoundsException();
		}

		fireTableCellUpdated(row, col);
	}

	@Override
	public int getRowCount() {
		return objects.size();
	}

	@Override
	public int getColumnCount() {
		if (columns.length != columnTypes.length) {
			DebugUtility.error("column length != columnTypes length");
		}
		return columns.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex > columns.length) {
			throw new IndexOutOfBoundsException();
		}
		return columns[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex > columnTypes.length) {
			throw new IndexOutOfBoundsException();
		}
		return columnTypes[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LocationData obj = (LocationData) objects.get(rowIndex);

		Object returned;
		switch (columnIndex) {
		case 0:
			returned = obj.name;
			break;
		case 1:
			returned = obj.canFlyOutOf;
			break;
		case 2:
			returned = obj.boundaries.get(DIR.NORTH);
			break;
		case 3:
			returned = obj.boundaries.get(DIR.EAST);
			break;
		case 4:
			returned = obj.boundaries.get(DIR.WEST);
			break;
		case 5:
			returned = obj.boundaries.get(DIR.SOUTH);
			break;
		case 6:
			returned = obj.pokemon;
			break;
		case 7:
			returned = obj.probabilities;
			break;
		case 8:
			returned = obj.minLevels;
			break;
		case 9:
			returned = obj.maxLevels;
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
		return returned;
	}
}