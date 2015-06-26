package mapmaker;

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

public class TileChooser extends JPanel implements ActionListener, GraphicsBankChangeListener {
	private static final long serialVersionUID = 1L;
	ArrayList<Object> tiles;
	GridLayout layout;
	GraphicsBank gfx;
	int tileWidth = 32;
	MapTile selectedTile;
	ButtonGroup group;
	JPanel tilePanel;
	JPanel spacer;
	JDialog propertiesDialog;
	MapTile propertyTile;
	JTextField userText;
	JSpinner tileNumber;
	JTextField tileName;
	JTextField tileType;
	JLabel tileImg;
	JButton applyBtn;
	JButton cancelBtn;
	JButton deleteBtn;
	JTextField imageFile;
	FileDropHandler fileDrop;

	public TileChooser(GraphicsBank paramGraphicsBank) {
		this.tilePanel = new JPanel();
		this.layout = new GridLayout(0, 5);
		this.tilePanel.setLayout(this.layout);

		setLayout(new BorderLayout());
		add(this.tilePanel, "North");

		this.spacer = new JPanel();
		this.spacer.add(new JLabel("  Drop new tiles here"));
		this.spacer.setToolTipText("Drop image files here to create more tiles.");

		add(this.spacer, "Center");

		this.gfx = paramGraphicsBank;
		reset();

		this.fileDrop = new FileDropHandler();
		setTransferHandler(this.fileDrop);
		paramGraphicsBank.addChangeListener(this);
		this.propertiesDialog = null;
	}

	public TileChooser(GraphicsBank paramGraphicsBank, JFrame paramJFrame) {
		this(paramGraphicsBank);
		createPropertiesDialog(paramJFrame);
	}

	void createPropertiesDialog(JFrame paramJFrame) {
		this.propertiesDialog = new JDialog(paramJFrame, "Tile Properties");
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
		JPanel localJPanel1 = (JPanel) this.propertiesDialog.getContentPane();
		localJPanel1.setLayout(new BorderLayout());
		JPanel localJPanel2 = new JPanel(new BorderLayout());
		localJPanel1.add(localJPanel2, "Center");
		localJPanel1.add(this.tileImg, "North");
		JPanel localJPanel3 = new JPanel(new GridBagLayout());
		GridBagConstraints localGridBagConstraints = new GridBagConstraints();
		localGridBagConstraints.fill = 2;
		localGridBagConstraints.insets = new Insets(3, 3, 3, 3);

		localGridBagConstraints.gridx = 0;
		localGridBagConstraints.gridy = 0;
		localJPanel3.add(new JLabel("ID"), localGridBagConstraints);
		localGridBagConstraints.gridx = 1;
		localGridBagConstraints.ipadx = 30;
		localGridBagConstraints.fill = 0;
		localGridBagConstraints.anchor = 17;
		localJPanel3.add(this.tileNumber, localGridBagConstraints);
		localGridBagConstraints.ipadx = 0;

		localGridBagConstraints.fill = 2;

		localGridBagConstraints.gridx = 0;
		localGridBagConstraints.gridy = 1;
		localJPanel3.add(new JLabel("Type"), localGridBagConstraints);
		localGridBagConstraints.gridx = 1;
		localJPanel3.add(this.tileType, localGridBagConstraints);
		localGridBagConstraints.gridx = 0;
		localGridBagConstraints.gridy = 2;

		localJPanel3.add(new JLabel("Name"), localGridBagConstraints);
		localGridBagConstraints.gridx = 1;
		localJPanel3.add(this.tileName, localGridBagConstraints);

		localGridBagConstraints.gridx = 0;
		localGridBagConstraints.gridy = 3;
		localJPanel3.add(new JLabel("User Text"), localGridBagConstraints);
		localGridBagConstraints.gridx = 1;
		localJPanel3.add(this.userText, localGridBagConstraints);

		localJPanel2.add(localJPanel3, "North");

		this.applyBtn = new JButton("Save");
		this.deleteBtn = new JButton("Delete Tile");
		this.cancelBtn = new JButton("Cancel");
		this.applyBtn.addActionListener(this);
		this.cancelBtn.addActionListener(this);
		this.deleteBtn.addActionListener(this);

		JPanel localJPanel4 = new JPanel(new GridLayout(1, 3));
		localJPanel4.add(this.deleteBtn);
		localJPanel4.add(this.applyBtn);
		localJPanel4.add(this.cancelBtn);

		localGridBagConstraints.gridx = 0;
		localGridBagConstraints.gridy = 4;
		localGridBagConstraints.gridwidth = 2;

		localJPanel3.add(localJPanel4, localGridBagConstraints);

		this.propertiesDialog.setSize(300, 500);
		this.propertiesDialog.setResizable(false);
	}

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

	public void tilesetUpdated(GraphicsBank paramGraphicsBank) {
		System.out.println("tilset updated");
		if (paramGraphicsBank == this.gfx) {
			reset();
		}
	}

	public void tileRemoved(GraphicsBank paramGraphicsBank, MapTile paramTile) {
		System.out.println("tilset updated");
		if (paramGraphicsBank == this.gfx) {
			reset();
		}
	}

	public void tileAdded(GraphicsBank paramGraphicsBank, MapTile paramTile) {
		System.out.println("tilset updated");
		TileButton localTileButton = new TileButton(paramTile);
		this.tilePanel.add(localTileButton);
		this.group.add(localTileButton);

		this.spacer.setPreferredSize(new Dimension(1, 30));

		this.tilePanel.revalidate();
		repaint();
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		if ((paramActionEvent.getSource() == this.applyBtn) && (this.propertyTile != null)) {
			this.propertyTile.name = this.tileName.getText();
			this.propertyTile.type = this.tileType.getText();
			this.propertyTile.number = ((Integer) this.tileNumber.getValue()).intValue();
			this.propertyTile.info = this.userText.getText();
			this.propertiesDialog.dispose();
			this.propertyTile = null;
		} else if (paramActionEvent.getSource() == this.cancelBtn) {
			this.propertiesDialog.dispose();
			this.propertyTile = null;
		} else if (paramActionEvent.getSource() == this.deleteBtn) {
			if (this.propertyTile != null) {
				this.gfx.remove(this.propertyTile);
				this.propertyTile = null;
			}
			this.propertiesDialog.dispose();
		} else {
			System.err.println("Unknown button fired action. " + paramActionEvent);
		}
	}

	public void setWidth(int paramInt) {
		if (paramInt >= this.tileWidth + 8) {
			this.layout.setColumns(paramInt / (this.tileWidth + 15));
			this.tilePanel.revalidate();
		}
	}

	public MapTile getSelectedTile() {
		return this.selectedTile;
	}

	void showProperties(MapTile paramTile) {
		this.propertyTile = paramTile;
		if (paramTile != null) {
			this.userText.setText(paramTile.getInfo());
			this.tileNumber.setValue(new Integer(paramTile.getNumber()));
			this.tileName.setText(paramTile.getName());
			this.tileType.setText(paramTile.getType());
			this.tileImg.setIcon(new ImageIcon(paramTile.getImage()));

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

	public void importImageAsTile(File paramFile) throws IOException {
		importImageAsTile(paramFile, 0);
	}

	public void importImageAsTile(File paramFile, int paramInt) throws IOException {
		if (paramFile.isDirectory()) {
			File[] arrayOfFile = paramFile.listFiles();
			for (int j = 0; j < arrayOfFile.length; j++) {
				importImageAsTile(arrayOfFile[j]);
			}
		}
		System.out.println("Import " + paramFile);
		try {
			ImageIO.read(paramFile);
		} catch (Exception localException) {
			System.out.println("FAIL");
			return;
		}
		System.out.println("getbasedir.... ahuh!");
		System.out.println("?1");
		int i = this.gfx.getUnusedNumber();
		System.out.println("?2");
		MapTile localTile = new MapTile(i, paramFile.getAbsolutePath(), "New Tile " + i, "No Type");

		System.out.println("Adding " + paramFile);
		this.gfx.add(localTile);
		if (this.propertiesDialog != null) {
			showProperties(localTile);
		}
	}

	public void MapTileRemoved(GraphicsBank paramGraphicsBank, MapTile paramTile) {}

	public void MapTileAdded(GraphicsBank paramGraphicsBank, MapTile paramTile) {}

	class FileDropHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		FileDropHandler() {}

		public boolean canImport(JComponent paramJComponent, DataFlavor[] paramArrayOfDataFlavor) {
			for (int i = 0; i < paramArrayOfDataFlavor.length; i++) {
				if (paramArrayOfDataFlavor[i].equals(DataFlavor.javaFileListFlavor)) {
					return true;
				}
			}
			return false;
		}

		public boolean importData(JComponent paramJComponent, Transferable paramTransferable) {
			try {
				List<?> localList = (List<?>) paramTransferable.getTransferData(DataFlavor.javaFileListFlavor);
				if (localList.size() > 4) {}
				Iterator<?> localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					File localFile = (File) localIterator.next();

					TileChooser.this.importImageAsTile(localFile);
				}
			} catch (UnsupportedFlavorException localUnsupportedFlavorException) {
				System.err.println("Unsupported drop content: " + localUnsupportedFlavorException);
			} catch (IOException localIOException) {
				System.err.println("Unexpected IO Exception while importing tile: " + localIOException);
				localIOException.printStackTrace();
			}
			return true;
		}
	}

	class TileButton extends JToggleButton implements ActionListener, MouseListener {
		private static final long serialVersionUID = 1L;
		MapTile tile;

		public TileButton(MapTile arg2) {
			BufferedImage localBufferedImage = new BufferedImage(TileChooser.this.gfx.getBaseMapTileSize().width,
					TileChooser.this.gfx.getBaseMapTileSize().height, 2);
			MapTile localObject = arg2;
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

		public void actionPerformed(ActionEvent paramActionEvent) {
			TileChooser.this.selectedTile = this.tile;
		}

		public void mouseEntered(MouseEvent paramMouseEvent) {}

		public void mouseExited(MouseEvent paramMouseEvent) {}

		public void mousePressed(MouseEvent paramMouseEvent) {}

		public void mouseReleased(MouseEvent paramMouseEvent) {}

		public void mouseClicked(MouseEvent paramMouseEvent) {
			if (SwingUtilities.isRightMouseButton(paramMouseEvent)) {
				TileChooser.this.showProperties(this.tile);
			}
		}

		public MapTile getTile() {
			return this.tile;
		}
	}
}