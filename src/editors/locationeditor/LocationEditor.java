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
			if (e.getSource().equals(LocationEditor.this.openItem)) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new File("./resources/data"));
				int returnVal = j.showOpenDialog(LocationEditor.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					LocationEditor.this.locationFile = j.getSelectedFile();
					DebugUtility.printMessage("opening " + LocationEditor.this.locationFile.getName() + ".");

					LocationEditor.this.locLib.populateMap(LocationEditor.this.locationFile.getAbsolutePath());

					for (String loc : LocationEditor.this.locLib.keySet()) {
						DebugUtility.printMessage(loc);
					}

					LocationEditor.this.ltm = new LocationTableModel();
					for (LocationData locData : LocationEditor.this.locLib.values()) {
						LocationEditor.this.ltm.addObject(locData);
					}
					JTable table = new JTable(LocationEditor.this.ltm);
					LocationEditor.this.panel = new JScrollPane(table);
					LocationEditor.this.mainPanel.setLayout(new GridLayout());
					LocationEditor.this.mainPanel.add(LocationEditor.this.panel);
					repaint();
					validate();
					// for (Entry<?> : entries){

					// }
				} else {
					// do nothing
				}
			}
			if (e.getSource().equals(LocationEditor.this.saveItem)) {
				DebugUtility.printError("todo - save data here");
			}
			if (e.getSource().equals(LocationEditor.this.addItem)) {
				DebugUtility.printMessage("adding item");
				LocationEditor.this.locLib.put("", new LocationData());
				LocationEditor.this.ltm.addObject(LocationEditor.this.locLib.get(""));
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

		this.fileMenu.add(this.saveItem);
		this.fileMenu.add(this.openItem);
		this.fileMenu.add(this.addItem);

		this.openItem.addActionListener(this.listener);
		this.addItem.addActionListener(this.listener);

		this.menuBar.add(this.fileMenu);

		this.setJMenuBar(this.menuBar);
		this.add(this.mainPanel);
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
