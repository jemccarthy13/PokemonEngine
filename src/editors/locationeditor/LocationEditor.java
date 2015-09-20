package editors.locationeditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import location.LocationData;
import location.LocationLibrary;
import utilities.DebugUtility;

/**
 * A class that allows for easier editing of the location data
 */
public class LocationEditor extends JFrame {

	/**
	 * The frame's menu bar
	 */
	JMenuBar menuBar = new JMenuBar();
	/**
	 * The frame's file menu item
	 */
	JMenu fileMenu = new JMenu("File");
	/**
	 * The frame's save menu item
	 */
	JMenuItem saveItem = new JMenuItem("Save");
	/**
	 * The frame's open menu item
	 */
	JMenuItem openItem = new JMenuItem("Open");
	/**
	 * The frame's add menu item
	 */
	JMenuItem addItem = new JMenuItem("Add Location");

	/**
	 * The file of location data
	 */
	File locationFile = null;

	/**
	 * An instance of the location data storage
	 */
	LocationLibrary locLib = LocationLibrary.getInstance();
	/**
	 * The main display panel
	 */
	JPanel mainPanel = new JPanel();
	/**
	 * The scrollable panel that holds the table
	 */
	JScrollPane panel = new JScrollPane();
	/**
	 * The model for the table
	 */
	LocationTableModel ltm = null;

	/**
	 * Serialization variable
	 */
	private static final long serialVersionUID = 8629661000233778533L;

	/**
	 * Read the given location file
	 * 
	 * @param path
	 *            - the file to read
	 * @param encoding
	 *            - the encoding of the file
	 * @return - a string representing the contents of the file
	 * @throws IOException
	 *             if the file cannot be read
	 */
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * Listens for actions on the frame
	 */
	public ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(openItem)) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new File("./resources/data"));
				int returnVal = j.showOpenDialog(LocationEditor.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					locationFile = j.getSelectedFile();
					DebugUtility.printMessage("opening " + locationFile.getName() + ".");

					locLib.populateMap(locationFile.getAbsolutePath());

					for (String loc : locLib.keySet()) {
						DebugUtility.printMessage(loc);
					}

					ltm = new LocationTableModel();
					for (LocationData locData : locLib.values()) {
						ltm.addObject(locData);
					}
					JTable table = new JTable(ltm);
					panel = new JScrollPane(table);
					mainPanel.setLayout(new GridLayout());
					mainPanel.add(panel);
					repaint();
					validate();
					// for (Entry<?> : entries){

					// }
				} else {}
			}
			if (e.getSource().equals(saveItem)) {
				DebugUtility.printError("TODO - save data here");
			}
			if (e.getSource().equals(addItem)) {
				DebugUtility.printMessage("adding item");
				locLib.put("", new LocationData());
				ltm.addObject(locLib.get(""));
				repaint();
				validate();
			}
		}

	};

	/**
	 * Construct a new frame
	 */
	public LocationEditor() {
		setSize(1000, 700);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		fileMenu.add(saveItem);
		fileMenu.add(openItem);
		fileMenu.add(addItem);

		openItem.addActionListener(listener);
		addItem.addActionListener(listener);

		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);
		this.add(mainPanel);
	}

	/**
	 * Main entry point for location editing
	 * 
	 * @param args
	 *            - command line arguments
	 */
	public static void main(String[] args) {
		LocationEditor le = new LocationEditor();
		le.setVisible(true);
	}
}
