package editors.mapmaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import utilities.DebugUtility;

/**
 * The side panel for choosing tiles
 */
public class TileChooser extends JPanel implements ActionListener, GraphicsBankChangeListener {
	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 5090621430335043404L;
	/**
	 * The tiles in the chooser
	 */
	ArrayList<Object> tiles;
	/**
	 * Layout for this GUI
	 */
	GridLayout layout;
	/**
	 * Storage of the images
	 */
	GraphicsBank gfx;
	/**
	 * The width of tiles
	 */
	int tileWidth = 32;
	/**
	 * The currently selected tile
	 */
	MapTile selectedTile;
	/**
	 * Button grouping
	 */
	ButtonGroup group;
	/**
	 * The panel to hold tiles
	 */
	JPanel tilePanel;
	/**
	 * Another GUI panel
	 */
	JPanel spacer;
	/**
	 * A dialog for properties
	 */
	JDialog propertiesDialog;
	/**
	 * A tile for properties
	 */
	MapTile propertyTile;
	/**
	 * User entered text filed
	 */
	JTextField userText;
	/**
	 * Spinner representing the tile number
	 */
	JSpinner tileNumber;
	/**
	 * Text field representing the tile name
	 */
	JTextField tileName;
	/**
	 * Text field represeting the tile type
	 */
	JTextField tileType;
	/**
	 * The tile image
	 */
	JLabel tileImg;
	/**
	 * Apply button
	 */
	JButton applyBtn;
	/**
	 * Cancel button
	 */
	JButton cancelBtn;
	/**
	 * Delete button
	 */
	JButton deleteBtn;
	/**
	 * A text field representing the image file
	 */
	JTextField imageFile;
	/**
	 * A handler for dropping files into the tile chooser
	 */
	FileDropHandler fileDrop;

	/**
	 * Construct a TileChooser given a graphics bank
	 * 
	 * @param bank
	 *            - the graphics bank to use
	 */
	public TileChooser(GraphicsBank bank) {
		this.tilePanel = new JPanel();
		this.layout = new GridLayout(0, 5);
		this.tilePanel.setLayout(this.layout);

		setLayout(new BorderLayout());
		add(this.tilePanel, "North");

		this.spacer = new JPanel();
		this.spacer.add(new JLabel("  Drop new tiles here"));
		this.spacer.setToolTipText("Drop image files here to create more tiles.");

		add(this.spacer, "Center");

		this.gfx = bank;
		reset();

		this.fileDrop = new FileDropHandler();
		setTransferHandler(this.fileDrop);
		bank.addChangeListener(this);
		this.propertiesDialog = null;
	}

	/**
	 * Create a TileChooser given a graphics bank and a frame
	 * 
	 * @param bank
	 *            - the graphics bank to use
	 * @param frame
	 *            - the JFrame to create properties from
	 */
	public TileChooser(GraphicsBank bank, JFrame frame) {
		this(bank);
		createPropertiesDialog(frame);
	}

	/**
	 * Construct a properties dialog
	 * 
	 * @param parentFrame
	 *            - the parent container for the dialog
	 */
	void createPropertiesDialog(JFrame parentFrame) {
		this.propertiesDialog = new JDialog(parentFrame, "Tile Properties");
		this.propertiesDialog.setSize(300, 300);
		this.propertiesDialog.setLocationRelativeTo(null);
		this.tileName = new JTextField("", 20);
		this.tileType = new JTextField("", 20);
		this.imageFile = new JTextField("", 20);
		this.imageFile.setEditable(false);
		this.tileNumber = new JSpinner();
		this.userText = new JTextField("", 20);
		this.propertyTile = null;
		this.tileImg = new JLabel();
		this.tileImg.setHorizontalAlignment(0);
		this.tileImg.setBorder(new TitledBorder("Image"));
		JPanel tileImagePanel = (JPanel) this.propertiesDialog.getContentPane();
		tileImagePanel.setLayout(new BorderLayout());
		JPanel localJPanel2 = new JPanel(new BorderLayout());
		tileImagePanel.add(localJPanel2, "Center");
		tileImagePanel.add(this.tileImg, "North");
		JPanel tileInformationPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = 2;
		constraints.insets = new Insets(3, 3, 3, 3);

		constraints.gridx = 0;
		constraints.gridy = 0;
		tileInformationPanel.add(new JLabel("ID"), constraints);
		constraints.gridx = 1;
		constraints.ipadx = 30;
		constraints.fill = 0;
		constraints.anchor = 17;
		tileInformationPanel.add(this.tileNumber, constraints);
		constraints.ipadx = 0;

		constraints.fill = 2;

		constraints.gridx = 0;
		constraints.gridy = 1;
		tileInformationPanel.add(new JLabel("Type"), constraints);
		constraints.gridx = 1;
		tileInformationPanel.add(this.tileType, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;

		tileInformationPanel.add(new JLabel("Name"), constraints);
		constraints.gridx = 1;
		tileInformationPanel.add(this.tileName, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		tileInformationPanel.add(new JLabel("User Text"), constraints);
		constraints.gridx = 1;
		tileInformationPanel.add(this.userText, constraints);

		localJPanel2.add(tileInformationPanel, "North");

		this.applyBtn = new JButton("Save");
		this.deleteBtn = new JButton("Delete Tile");
		this.cancelBtn = new JButton("Cancel");
		this.applyBtn.addActionListener(this);
		this.cancelBtn.addActionListener(this);
		this.deleteBtn.addActionListener(this);

		JPanel applyDeletePanel = new JPanel(new GridLayout(1, 3));
		applyDeletePanel.add(this.deleteBtn);
		applyDeletePanel.add(this.applyBtn);
		applyDeletePanel.add(this.cancelBtn);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;

		tileInformationPanel.add(applyDeletePanel, constraints);

		this.propertiesDialog.setSize(300, 500);
		this.propertiesDialog.setResizable(false);
	}

	/**
	 * Reset the TileChooser
	 */
	public void reset() {
		int i = 0;
		this.tilePanel.removeAll();
		this.group = new ButtonGroup();

		TileButton localTileButton = new TileButton(null);
		this.tilePanel.add(localTileButton);
		this.group.add(localTileButton);
		i++;

		Iterator<?> localIterator = this.gfx.iterator();
		while (localIterator.hasNext()) {
			localTileButton = new TileButton((MapTile) localIterator.next());
			this.tilePanel.add(localTileButton);
			this.group.add(localTileButton);
			i++;
		}
		if (i <= 0) {
			this.spacer.setPreferredSize(new Dimension(1, 100));
		} else {
			this.spacer.setPreferredSize(new Dimension(1, 30));
		}
		this.tilePanel.revalidate();
		repaint();
	}

	/**
	 * Graphics bank change listener tile set updated
	 * 
	 * @param bank
	 *            - the graphics bank to check against
	 */
	@Override
	public void tilesetUpdated(GraphicsBank bank) {
		DebugUtility.printMessage("tilset updated");
		if (bank == this.gfx) {
			reset();
		}
	}

	/**
	 * Graphics bank change listener tile removed
	 * 
	 * @param bank
	 *            - the graphics bank
	 * @param removedTile
	 *            - tile that was removed
	 */
	public void tileRemoved(GraphicsBank bank, MapTile removedTile) {
		DebugUtility.printMessage("tilset updated");
		if (bank == this.gfx) {
			reset();
		}
	}

	/**
	 * Graphics bank change listener tile added
	 * 
	 * @param bank
	 *            - the graphics bank
	 * @param addedTile
	 *            - tile that was added
	 */
	public void tileAdded(GraphicsBank bank, MapTile addedTile) {
		DebugUtility.printMessage("tilset updated");
		TileButton localTileButton = new TileButton(addedTile);
		this.tilePanel.add(localTileButton);
		this.group.add(localTileButton);

		this.spacer.setPreferredSize(new Dimension(1, 30));

		this.tilePanel.revalidate();
		repaint();
	}

	/**
	 * Function that is fired every time the TileChooser records an action
	 * 
	 * @param event
	 *            - the event that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if ((event.getSource() == this.applyBtn) && (this.propertyTile != null)) {
			this.propertyTile.name = this.tileName.getText();
			this.propertyTile.type = this.tileType.getText();
			this.propertyTile.number = ((Integer) this.tileNumber.getValue()).intValue();
			this.propertyTile.info = this.userText.getText();
			this.propertiesDialog.dispose();
			this.propertyTile = null;
		} else if (event.getSource() == this.cancelBtn) {
			this.propertiesDialog.dispose();
			this.propertyTile = null;
		} else if (event.getSource() == this.deleteBtn) {
			if (this.propertyTile != null) {
				this.gfx.remove(this.propertyTile);
				this.propertyTile = null;
			}
			this.propertiesDialog.dispose();
		} else {
			DebugUtility.printError("Unknown button fired action. " + event);
		}
	}

	/**
	 * Set the width of this chooser
	 * 
	 * @param newWidth
	 *            - the width to set
	 */
	public void setWidth(int newWidth) {
		if (newWidth >= this.tileWidth + 8) {
			this.layout.setColumns(newWidth / (this.tileWidth + 15));
			this.tilePanel.revalidate();
		}
	}

	/**
	 * Get the selected tile
	 * 
	 * @return selected tile
	 */
	public MapTile getSelectedTile() {
		return this.selectedTile;
	}

	/**
	 * Show the properties of a given tile
	 * 
	 * @param tileToShow
	 *            - the tile to get the properties of
	 */
	void showProperties(MapTile tileToShow) {
		this.propertyTile = tileToShow;
		if (tileToShow != null) {
			this.userText.setText(tileToShow.getInfo());
			this.tileNumber.setValue(new Integer(tileToShow.getNumber()));
			this.tileName.setText(tileToShow.getName());
			this.tileType.setText(tileToShow.getType());
			this.tileImg.setIcon(new ImageIcon(tileToShow.getImage()));

			this.applyBtn.setEnabled(true);
			this.deleteBtn.setEnabled(true);
			this.userText.setEditable(true);
			this.tileNumber.setEnabled(true);
			this.tileName.setEditable(true);
			this.tileType.setEditable(true);
		} else {
			this.userText.setText("");
			this.tileNumber.setValue(new Integer(0));
			this.tileName.setText("Null (Erases existing tiles)");
			this.tileType.setText("");
			this.tileImg.setIcon(null);

			this.userText.setEditable(false);
			this.tileNumber.setEnabled(false);
			this.tileName.setEditable(false);
			this.tileType.setEditable(false);
			this.applyBtn.setEnabled(false);
			this.deleteBtn.setEnabled(false);
		}
		this.propertiesDialog.pack();
		this.propertiesDialog.setVisible(true);
	}

	/**
	 * Get an image and translate into a tile
	 * 
	 * @param fileToRead
	 *            - the file to read
	 * @throws IOException
	 *             if the file cannot be read
	 */
	public void importImageAsTile(File fileToRead) throws IOException {
		if (fileToRead.isDirectory()) {
			File[] arrayOfFile = fileToRead.listFiles();
			for (int j = 0; j < arrayOfFile.length; j++) {
				importImageAsTile(arrayOfFile[j]);
			}
		}
		DebugUtility.printMessage("Import " + fileToRead);
		try {
			ImageIO.read(fileToRead);
		} catch (Exception localException) {
			DebugUtility.printMessage("FAIL\n" + localException.getMessage());
			return;
		}
		DebugUtility.printMessage("getbasedir.... ahuh!");
		DebugUtility.printMessage("?1");
		int i = this.gfx.getUnusedNumber();
		DebugUtility.printMessage("?2");
		MapTile localTile = new MapTile(i, fileToRead.getAbsolutePath(), "New Tile " + i, "No Type");

		DebugUtility.printMessage("Adding " + fileToRead);
		this.gfx.add(localTile);
		if (this.propertiesDialog != null) {
			showProperties(localTile);
		}
	}

	/**
	 * Change listener implementation for a map tile being removed
	 * 
	 * @param bank
	 *            - the graphics bank
	 * @param removedTile
	 *            - the tile that was removed
	 */
	@Override
	public void MapTileRemoved(GraphicsBank bank, MapTile removedTile) {
		// do nothing
	}

	/**
	 * Change listener implementation for a map tile being added
	 * 
	 * @param bank
	 *            - the graphics bank
	 * @param addedTile
	 *            - the tile that was added
	 */
	@Override
	public void MapTileAdded(GraphicsBank bank, MapTile addedTile) {
		// do nothing
	}

	/**
	 * Class that represents a file drop
	 */
	class FileDropHandler extends TransferHandler {
		/**
		 * Serialization information
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Default constructor
		 */
		FileDropHandler() {}

		/**
		 * Return whether or not the data can be imported
		 * 
		 * @param ignored
		 *            - a JComponent that gets passed in to this function
		 * @param data
		 *            - the data to be imported
		 * @return whether or not the data can be imported
		 */
		@Override
		public boolean canImport(JComponent ignored, DataFlavor[] data) {
			for (int i = 0; i < data.length; i++) {
				if (data[i].equals(DataFlavor.javaFileListFlavor)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Perform a data import
		 * 
		 * @param ignored
		 *            - a JComponent that gets passed in to this function
		 * @param dataholder
		 *            - the data to be imported
		 * @return whether or not the data can be imported
		 */
		@Override
		public boolean importData(JComponent ignored, Transferable dataholder) {
			try {
				List<?> localList = (List<?>) dataholder.getTransferData(DataFlavor.javaFileListFlavor);
				if (localList.size() > 4) {
					// do nothing
				}

				Iterator<?> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					File localFile = (File) localIterator.next();

					TileChooser.this.importImageAsTile(localFile);
				}
			} catch (UnsupportedFlavorException localUnsupportedFlavorException) {
				DebugUtility.printError("Unsupported drop content: " + localUnsupportedFlavorException);
			} catch (IOException localIOException) {
				DebugUtility.printError("Unexpected IO Exception while importing tile: " + localIOException);
				localIOException.printStackTrace();
			}
			return true;
		}
	}

	/**
	 * A tile is also a pressable button
	 */
	class TileButton extends JToggleButton implements ActionListener, MouseListener {
		/**
		 * Serialization information
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * This class holds a representation of a tile
		 */
		MapTile tile;

		/**
		 * Construct a TileButton given an encompassed map tile
		 * 
		 * @param tile
		 *            - the tile this button should represent
		 */
		public TileButton(MapTile tile) {
			BufferedImage localBufferedImage = new BufferedImage(TileChooser.this.gfx.getBaseMapTileSize().width,
					TileChooser.this.gfx.getBaseMapTileSize().height, 2);
			MapTile localObject = tile;
			if (localObject != null) {
				Image localImage = localObject.getImage();
				localBufferedImage.getGraphics().drawImage(localImage, 0, 0, 32, 32, null);
				setToolTipText(localObject.getName());
			}
			setIcon(new ImageIcon(localBufferedImage));

			setMargin(new Insets(2, 2, 2, 2));
			this.tile = localObject;

			addMouseListener(this);
			addActionListener(this);
		}

		/**
		 * On an action, update the selected tile.
		 * 
		 * @param event
		 *            - the event that triggered an action
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			TileChooser.this.selectedTile = this.tile;
		}

		/**
		 * Required no-op mouse listener method
		 * 
		 * @param ignored
		 *            an event that is ignored
		 */
		@Override
		public void mouseEntered(MouseEvent ignored) {
			// do nothing
		}

		/**
		 * Required no-op mouse listener method
		 * 
		 * @param ignored
		 *            an event that is ignored
		 */
		@Override
		public void mouseExited(MouseEvent ignored) {
			// do nothing
		}

		/**
		 * Required no-op mouse listener method
		 * 
		 * @param ignored
		 *            an event that is ignored
		 */
		@Override
		public void mousePressed(MouseEvent ignored) {
			// do nothing
		}

		/**
		 * Required no-op mouse listener method
		 * 
		 * @param ignored
		 *            an event that is ignored
		 */
		@Override
		public void mouseReleased(MouseEvent ignored) {
			// do nothing
		}

		/**
		 * On a right click, display the tile's properties
		 * 
		 * @param mouseClick
		 *            - the mouse click event
		 */
		@Override
		public void mouseClicked(MouseEvent mouseClick) {
			if (SwingUtilities.isRightMouseButton(mouseClick)) {
				TileChooser.this.showProperties(this.tile);
			}
		}

		/**
		 * Get the tile of this button
		 * 
		 * @return a map tile
		 */
		public MapTile getTile() {
			return this.tile;
		}
	}
}