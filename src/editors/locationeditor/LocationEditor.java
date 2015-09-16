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

import org.json.simple.JSONObject;

import utilities.DebugUtility;

public class LocationEditor extends JFrame {

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenuItem saveItem = new JMenuItem("Save");
	JMenuItem openItem = new JMenuItem("Open");
	JMenuItem addItem = new JMenuItem("Add Location");

	File locationFile = null;
	JSONObject locationData = null;
	LocationLibrary locLib = LocationLibrary.getInstance();

	JPanel mainPanel = new JPanel();
	JScrollPane panel = new JScrollPane();

	LocationTableModel ltm = null;

	private static final long serialVersionUID = 8629661000233778533L;

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

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

	public static void main(String[] args) {
		LocationEditor le = new LocationEditor();
		le.setVisible(true);
	}
}
