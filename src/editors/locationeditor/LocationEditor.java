package editors.locationeditor;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

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

	DefaultComboBoxModel<Object> dcbm;
	JComboBox<Object> locations;
	JTextField nameField;

	File locationFile = null;
	JSONObject locationData = null;
	LocationLibrary locLib = LocationLibrary.getInstance();

	JPanel mainPanel = new JPanel();
	JScrollPane panel = new JScrollPane();

	private static final long serialVersionUID = 8629661000233778533L;

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public ItemListener update = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

		}

	};

	public void updateFields() {
		LocationData selection = (LocationData) locLib.get(locations.getSelectedItem());
		nameField.setText(selection.name);
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

					mainPanel.setLayout(new GridLayout(10, 20, 30, 30));

					// Allow selection of the location
					locations = new JComboBox<Object>();
					locations.setModel(new DefaultComboBoxModel<Object>(locLib.keySet().toArray()));
					locations.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent arg0) {
							updateFields();
						}

					});
					mainPanel.add(locations);

					// Allow edit the name
					nameField = new JTextField(locations.getSelectedItem().toString());
					nameField.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) {}

						@Override
						public void focusLost(FocusEvent arg0) {
							Object selection = locations.getSelectedItem();
							System.out.println(selection);
							String previous_name = locLib.get(locations.getSelectedItem()).name;
							String new_name = nameField.getText();

							LocationData d = locLib.get(previous_name);
							locLib.remove(previous_name);

							d.name = new_name;
							locLib.put(new_name, d);
							dcbm = new DefaultComboBoxModel<Object>(locLib.keySet().toArray());
							locations.setModel(dcbm);
							updateFields();
						}

					});
					mainPanel.add(nameField);

					LocationData selection = (LocationData) locLib.get(locations.getSelectedItem());

					// Allow edit canFly
					JLabel flyLbl = new JLabel("Can fly out of: ");
					JRadioButton canFly = new JRadioButton("YES", selection.canFlyOutOf);
					JRadioButton cantFly = new JRadioButton("NO", selection.canFlyOutOf);
					ButtonGroup flyButtons = new ButtonGroup();
					flyButtons.add(canFly);
					flyButtons.add(cantFly);

					JPanel flyPanel = new JPanel();
					FlowLayout layout = new FlowLayout();
					layout.setAlignment(FlowLayout.LEADING);
					flyPanel.setLayout(new FlowLayout());
					flyPanel.add(flyLbl);
					flyPanel.add(canFly);
					flyPanel.add(cantFly);
					mainPanel.add(flyPanel);

					JLabel boundaryLbl = new JLabel("Boundaries:");
					mainPanel.add(boundaryLbl);

					JPanel boundaries = new JPanel();
					boundaries.add(new JLabel("North"));
					boundaries.add(new JLabel("East"));
					boundaries.add(new JLabel("South"));
					boundaries.add(new JLabel("West"));
					mainPanel.add(boundaries);

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
