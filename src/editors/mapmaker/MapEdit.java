package editors.mapmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utilities.DebugUtility;

/**
 * The main map editor JFrame
 */
public class MapEdit implements ActionListener, ChangeListener, KeyListener {
	/**
	 * Represents normal paint
	 */
	public static final int PAINT_NORMAL = 0;
	/**
	 * Represents paint fill
	 */
	public static final int PAINT_FILL = 1;

	private boolean compactToolbars = true;
	private boolean borderedButtons = true;
	private JFrame mainFrame;
	private MapComponent mapPanel;
	private JFileChooser chooser, tschooser;
	static TileChooser tileChooser;
	static JSplitPane split;
	private JScrollPane mapScroll;
	private JPanel chooserPanel, settingsPanel, tilesetSettingsPanel, colorDialog;
	private JSlider r, g, b, h, s;
	private boolean ignoreEffects = false;
	private File openFile;
	private Map map;
	Scene scene;
	private GraphicsBank gfx;
	private JToolBar outerToolBar, innerToolBar;
	private JButton newBtn, openBtn, saveBtn, clearBtn;
	private JToggleButton[] layerButtons;
	private JToggleButton hideBtn, gridBtn;
	private JButton shiftRightBtn, shiftLeftBtn, shiftUpBtn, shiftDownBtn;
	private JButton increaseWidthBtn, decreaseWidthBtn;
	private JButton increaseHeightBtn, decreaseHeightBtn;
	private JToggleButton palletteBtn;
	private JButton zoomInBtn, zoomOutBtn, zoomFullBtn;
	private JToggleButton fillBtn;
	static JButton undoBtn;
	static JButton redoBtn;
	private float zoomLevel;
	private JMenuBar menuBar;
	private JMenu fileMenu, editMenu, toolMenu, helpMenu;
	private JMenuItem undoMI, redoMI, openMI, newMI;
	private JMenuItem saveMI, saveAsMI, exitMI;
	private JMenuItem about, howToUse;
	private JPanel tilesetInfoPane;
	private JLabel tilesetFileLabel;
	private JButton tilesetOpenBtn, tilesetNewBtn, tilesetSaveBtn;
	private JSpinner tilesetGridWField, tilesetGridHField;
	private JButton effectsResetBtn;

	/**
	 * Default constructor
	 */
	public MapEdit() {
		this.zoomLevel = 1.0F;
		this.openFile = null;
		try {
			this.scene = Scene.loadScene("lastOpenScene.dat");
		} catch (IOException localIOException) {
			this.scene = new Scene();
		}
		this.map = this.scene.getMap();
		this.gfx = this.scene.getTileset();

		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		this.mainFrame = new JFrame();
		this.mainFrame.setTitle("Tile-Based Map Editor");

		tileChooser = new TileChooser(this.gfx, this.mainFrame);
		this.chooser = new JFileChooser("scenes");
		this.tschooser = new JFileChooser("gfx");

		this.tschooser.setSelectedFile(new File("C:/Users/caitr/git/PokemonEngine/mapmaker/Tiles.mapeditor"));
		this.chooser.setSelectedFile(new File("C:/Users/caitr/git/PokemonEngine/mapmaker/Maps/Johto.map"));
		JPanel localJPanel1 = (JPanel) this.mainFrame.getContentPane();
		JPanel localJPanel2 = new JPanel(new BorderLayout());
		JPanel localJPanel3 = new JPanel(new BorderLayout());

		localJPanel1.setLayout(new BorderLayout());
		localJPanel1.add(localJPanel2, "Center");
		localJPanel2.add(localJPanel3, "Center");

		this.colorDialog = createColorDialog();

		setupMenus();

		setupToolbars();

		localJPanel3.setLayout(new BorderLayout());

		localJPanel2.add(this.innerToolBar, "North");
		localJPanel1.add(this.outerToolBar, "North");

		this.chooserPanel = new JPanel(new BorderLayout());

		JScrollPane localJScrollPane = new JScrollPane(tileChooser);
		this.chooserPanel.add(localJScrollPane, "Center");
		JTabbedPane localJTabbedPane = new JTabbedPane();
		localJTabbedPane.add("Tiles", this.chooserPanel);

		this.tilesetFileLabel = new JLabel("Tileset: * unsaved *");
		this.tilesetInfoPane = new JPanel(new BorderLayout());

		JPanel localJPanel4 = new JPanel(new FlowLayout());

		this.tilesetInfoPane.add(localJPanel4, "Center");
		this.chooserPanel.add(this.tilesetInfoPane, "South");
		this.tilesetInfoPane.add(this.tilesetFileLabel, "North");

		this.tilesetOpenBtn = makeBtn("Open", "icons/opents.gif", "Load Tileset");
		this.tilesetNewBtn = makeBtn("New", "icons/newts.gif", "New Tileset");
		this.tilesetSaveBtn = makeBtn("Save", "icons/savets.gif", "Save Tileset");
		localJPanel4.add(this.tilesetOpenBtn);
		localJPanel4.add(this.tilesetNewBtn);
		localJPanel4.add(this.tilesetSaveBtn);
		this.settingsPanel = new JPanel(new BorderLayout());
		this.settingsPanel.setBorder(new TitledBorder("Settings"));
		this.settingsPanel.add(this.colorDialog, "Center");

		localJTabbedPane.add("Settings", this.settingsPanel);
		this.mapPanel = new MapComponent(this.map, this);
		this.mapScroll = new JScrollPane(this.mapPanel);
		this.mapPanel.setViewport(this.mapScroll.getViewport());

		split = new JSplitPane();
		split.setDividerLocation(250);
		split.setLeftComponent(localJTabbedPane);
		split.setRightComponent(this.mapScroll);
		localJPanel3.add(split, "Center");

		split.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
				MapEdit.tileChooser.setWidth(MapEdit.split.getDividerLocation());
			}
		});
		this.mainFrame.setSize(new Dimension(830, 650));
		this.mainFrame.setLocationRelativeTo(null);
		this.mainFrame.setVisible(true);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (;;) {
			this.mapPanel.repaint();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException localInterruptedException) {
				// do nothing
			}
		}
	}

	private void setupMenus() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		setToolMenu(new JMenu("Tools"));
		this.editMenu = new JMenu("Edit");
		this.helpMenu = new JMenu("Help");
		this.openMI = new JMenuItem("Open...");
		this.newMI = new JMenuItem("New");
		this.saveMI = new JMenuItem("Save");
		this.saveAsMI = new JMenuItem("Save As...");
		this.exitMI = new JMenuItem("Exit");
		this.undoMI = new JMenuItem("Undo");
		this.redoMI = new JMenuItem("Redo");
		this.about = new JMenuItem("About...(N/A)");
		this.howToUse = new JMenuItem("How to use LevelEdit (N/A)");

		this.openMI.addActionListener(this);
		this.newMI.addActionListener(this);
		this.saveMI.addActionListener(this);
		this.saveAsMI.addActionListener(this);
		this.exitMI.addActionListener(this);
		this.undoMI.addActionListener(this);
		this.redoMI.addActionListener(this);
		this.about.addActionListener(this);
		this.howToUse.addActionListener(this);

		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.editMenu);
		this.menuBar.add(this.helpMenu);

		this.fileMenu.add(this.openMI);
		this.fileMenu.add(this.newMI);
		this.fileMenu.add(this.saveMI);
		this.fileMenu.add(this.saveAsMI);
		this.fileMenu.add(this.exitMI);
		this.editMenu.add(this.undoMI);
		this.editMenu.add(this.redoMI);
		this.helpMenu.add(this.about);
		this.helpMenu.add(this.howToUse);
		this.mainFrame.setJMenuBar(this.menuBar);
	}

	private void setupToolbars() {
		this.outerToolBar = new JToolBar();
		this.innerToolBar = new JToolBar();

		this.saveBtn = makeBtn("Save", "icons/save.gif", "Save map");
		this.openBtn = makeBtn("Open...", "icons/open.gif", "Open map...");
		this.newBtn = makeBtn("New", "icons/new.gif", "New map");
		this.clearBtn = makeBtn("Clear", "icons/clear.gif", "Reset map (Delete all tiles)");

		ButtonGroup localButtonGroup = new ButtonGroup();
		this.layerButtons = new JToggleButton[3];
		this.layerButtons[2] = makeToggleBtn("Layer 3", "icons/top.gif", "Edit the top layer");
		this.layerButtons[1] = makeToggleBtn("Layer 2", "icons/mid.gif", "Edit the middle layer");
		this.layerButtons[0] = makeToggleBtn("Layer 1", "icons/bottom.gif", "Edit the bottom layer");
		localButtonGroup.add(this.layerButtons[0]);
		localButtonGroup.add(this.layerButtons[1]);
		localButtonGroup.add(this.layerButtons[2]);

		this.gridBtn = makeToggleBtn("Grid", "icons/grid.gif", "Show/Hide Grid");
		this.hideBtn = makeToggleBtn("Hide other layers", "icons/hideoth.gif", "Hide other layers");

		this.zoomInBtn = makeBtn("Zoom in", "icons/zoomin.gif", "Zoom in");
		this.zoomFullBtn = makeBtn("Zoom 100%", "icons/zoomfull.gif", "Zoom to 100%");
		this.zoomOutBtn = makeBtn("Zoom out", "icons/zoomout.gif", "Zoom out");

		this.shiftRightBtn = makeBtn("->", "icons/shiftRight.gif", "Move tiles right");
		this.shiftLeftBtn = makeBtn("<-", "icons/shiftLeft.gif", "Move tiles left");
		this.shiftUpBtn = makeBtn("^", "icons/shiftUp.gif", "Move tiles up");
		this.shiftDownBtn = makeBtn("\\/", "icons/shiftDown.gif", "Move tiles down");
		this.increaseWidthBtn = makeBtn("<- ->", "icons/increaseWidth.gif", "Increase field width");
		this.decreaseWidthBtn = makeBtn("-> <-", "icons/decreaseWidth.gif", "Decrease field width");
		this.increaseHeightBtn = makeBtn("\\/ +", "icons/increaseHeight.gif", "Increase field height");
		this.decreaseHeightBtn = makeBtn("^^ -", "icons/decreaseHeight.gif", "Decrease field height");

		this.fillBtn = makeToggleBtn("Flood Fill", "icons/fill.gif", "Flood fill mode");
		undoBtn = makeBtn("Undo", "icons/undo.gif", "Undo");
		redoBtn = makeBtn("Redo", "icons/redo.gif", "Redo");

		this.undoMI.setAccelerator(
				KeyStroke.getKeyStroke(new Character('Z'), Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.redoMI.setAccelerator(
				KeyStroke.getKeyStroke(new Character('Y'), Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		((JPanel) this.mainFrame.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(90, 2), "Undo");
		((JPanel) this.mainFrame.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(89, 2), "Redo");
		((JPanel) this.mainFrame.getContentPane()).getActionMap().put("Undo", new undoAction());
		((JPanel) this.mainFrame.getContentPane()).getActionMap().put("Redo", new redoAction());

		this.outerToolBar.add(this.newBtn);
		this.outerToolBar.add(this.openBtn);
		this.outerToolBar.add(this.saveBtn);
		this.outerToolBar.add(this.clearBtn);

		this.outerToolBar.addSeparator();
		this.outerToolBar.add(undoBtn);
		this.outerToolBar.add(redoBtn);
		this.outerToolBar.addSeparator();
		this.outerToolBar.add(this.fillBtn);

		this.outerToolBar.addSeparator();
		this.outerToolBar.add(this.layerButtons[2]);
		this.outerToolBar.add(this.layerButtons[1]);
		this.outerToolBar.add(this.layerButtons[0]);

		this.outerToolBar.addSeparator();
		this.outerToolBar.add(this.hideBtn);
		this.outerToolBar.add(this.gridBtn);

		this.outerToolBar.addSeparator();
		this.outerToolBar.add(this.zoomInBtn);
		this.outerToolBar.add(this.zoomFullBtn);
		this.outerToolBar.add(this.zoomOutBtn);
		if (this.compactToolbars) {
			this.innerToolBar = this.outerToolBar;
			this.outerToolBar.addSeparator();
		}
		this.innerToolBar.add(this.shiftLeftBtn);
		this.innerToolBar.add(this.shiftRightBtn);
		this.innerToolBar.add(this.shiftUpBtn);
		this.innerToolBar.add(this.shiftDownBtn);

		this.innerToolBar.addSeparator();
		this.innerToolBar.add(this.increaseWidthBtn);
		this.innerToolBar.add(this.decreaseWidthBtn);
		this.innerToolBar.add(this.increaseHeightBtn);
		this.innerToolBar.add(this.decreaseHeightBtn);

		this.innerToolBar.addSeparator();

		this.gridBtn.setSelected(true);
		this.layerButtons[0].setSelected(true);
	}

	/**
	 * Event that occurs when a key is pressed
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		// do nothing
	}

	/**
	 * Event that occurs when a key is relesed
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		// do nothing
	}

	/**
	 * Event that occurs when a key is typed
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	@Override
	public void keyTyped(KeyEvent event) {
		// do nothing
	}

	/**
	 * Method that is called when an action is performed
	 * 
	 * @param event
	 *            - the event triggering this method
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object localObject = event.getSource();
		if (localObject == this.zoomInBtn) {
			if (this.zoomLevel < 5.0F) {
				this.zoomLevel = ((float) (this.zoomLevel * 1.2D));
			}
		} else if (localObject == this.zoomOutBtn) {
			if (this.zoomLevel > 0.05D) {
				this.zoomLevel = ((float) (this.zoomLevel * 0.8D));
			}
		} else if (localObject == this.zoomFullBtn) {
			this.zoomLevel = 1.0F;
		} else if ((localObject == undoBtn) || (localObject == this.undoMI)) {
			this.mapPanel.undo();
			this.mapPanel.repaint();
		} else if ((localObject == redoBtn) || (localObject == this.redoMI)) {
			this.mapPanel.redo();
			this.mapPanel.repaint();
		} else if ((localObject == this.openBtn) || (localObject == this.openMI)) {
			int i = this.chooser.showOpenDialog(this.mainFrame);
			if (i == 0) {
				openFile(this.chooser.getSelectedFile());
			}
		} else if ((localObject == this.saveBtn) || (localObject == this.saveMI)) {
			if (this.openFile == null) {
				actionPerformed(new ActionEvent(this.saveAsMI, event.getID(), event.getActionCommand()));
			} else {
				saveFile(this.openFile);
			}
		} else if ((localObject == this.newBtn) || (localObject == this.newMI)) {
			newFile();
			this.openFile = null;
		} else if (localObject == this.clearBtn) {
			this.map.clear();
			this.mapPanel.repaint();
		} else if (localObject == this.layerButtons[0]) {
			this.mapPanel.setActiveLayer(0);
		} else if (localObject == this.layerButtons[1]) {
			this.mapPanel.setActiveLayer(1);
		} else if (localObject == this.layerButtons[2]) {
			this.mapPanel.setActiveLayer(2);
		} else if (localObject == this.hideBtn) {
			this.mapPanel.setHideLayers(this.hideBtn.isSelected());
			this.mapPanel.repaint();
		} else if (localObject == this.gridBtn) {
			this.mapPanel.setGrid(this.gridBtn.isSelected());
			this.mapPanel.repaint();
		} else if (localObject == this.shiftRightBtn) {
			this.map.shift(1, 0);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.shiftLeftBtn) {
			this.map.shift(-1, 0);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.shiftUpBtn) {
			this.map.shift(0, -1);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.shiftDownBtn) {
			this.map.shift(0, 1);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.increaseWidthBtn) {
			this.map.resize(this.map.getWidth() + 1, this.map.getHeight());
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.decreaseWidthBtn) {
			this.map.resize(this.map.getWidth() - 1, this.map.getHeight());
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.increaseHeightBtn) {
			this.map.resize(this.map.getWidth(), this.map.getHeight() + 1);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.decreaseHeightBtn) {
			this.map.resize(this.map.getWidth(), this.map.getHeight() - 1);
			this.mapPanel.setMap(this.map);
			this.mapPanel.repaint();
		} else if (localObject == this.palletteBtn) {
			this.colorDialog.setVisible(!this.colorDialog.isVisible());
			this.palletteBtn.setSelected(this.colorDialog.isVisible());
		} else if (localObject == this.effectsResetBtn) {
			setIgnoreEffectChanges(true);
			this.r.setValue(100);
			this.g.setValue(100);
			this.b.setValue(100);
			this.h.setValue(0);
			this.s.setValue(100);
			setIgnoreEffectChanges(false);
		} else if (localObject == this.saveAsMI) {
			int i = this.chooser.showSaveDialog(this.mainFrame);
			if (i == 0) {
				saveFile(this.chooser.getSelectedFile());
			}
		} else if (localObject == this.exitMI) {
			this.mainFrame.dispose();
			DebugUtility.quit();
		} else if (localObject == this.tilesetNewBtn) {
			newTileset();
		} else if (localObject == this.tilesetOpenBtn) {
			openTileset();
		} else if (localObject == this.tilesetSaveBtn) {
			saveTileset();
		} else {
			DebugUtility.printError("Unknown source of actionEvent. (The button you just clicked does nothing)");
		}
		if ((localObject == this.zoomInBtn) || (localObject == this.zoomOutBtn) || (localObject == this.zoomFullBtn)
				|| (localObject == this.effectsResetBtn)) {
			float[] fParams = { this.r.getValue() / 100.0F, this.g.getValue() / 100.0F, this.b.getValue() / 100.0F,
					this.h.getValue() / 360.0F, this.s.getValue() / 100.0F, this.zoomLevel };
			this.scene.setEffect(fParams);

			this.map.setZoom(this.zoomLevel);
			this.mapPanel.refreshZoom();
		}
	}

	private JButton makeBtn(String paramString1, String paramString2, String paramString3) {
		JButton localJButton;
		try {
			localJButton = new JButton(new ImageIcon(getClass().getResource(paramString2)));
		} catch (Exception localException) {
			localJButton = new JButton(paramString1);
		}
		localJButton.setToolTipText(paramString3);
		localJButton.addActionListener(this);
		if (this.borderedButtons) {
			localJButton.setBorder(new LineBorder(Color.gray, 1, false));
		} else if (this.compactToolbars) {
			localJButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		return localJButton;
	}

	JToggleButton makeToggleBtn(String paramString1, String paramString2, String paramString3) {
		JToggleButton localJToggleButton;
		try {
			localJToggleButton = new JToggleButton(new ImageIcon(getClass().getResource(paramString2)));
		} catch (Exception localException) {
			localJToggleButton = new JToggleButton(paramString1);
		}
		localJToggleButton.setToolTipText(paramString3);
		localJToggleButton.addActionListener(this);
		if (this.borderedButtons) {
			localJToggleButton.setBorder(new LineBorder(Color.gray, 1, false));
		} else if (this.compactToolbars) {
			localJToggleButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		return localJToggleButton;
	}

	/**
	 * Retrieve the TileChooser's selected tile
	 * 
	 * @return a maptile
	 */
	public static MapTile getSelectedTile() {
		return tileChooser.getSelectedTile();
	}

	private void setGraphicsBank(GraphicsBank paramGraphicsBank) {
		this.gfx = paramGraphicsBank;
		this.scene.setTileset(paramGraphicsBank);
		this.chooserPanel.removeAll();
		tileChooser = new TileChooser(paramGraphicsBank, this.mainFrame);
		this.chooserPanel.add(this.tilesetInfoPane, "South");
		JScrollPane localJScrollPane = new JScrollPane(tileChooser);
		this.chooserPanel.add(localJScrollPane, "Center");
		this.mainFrame.repaint();
		if (paramGraphicsBank.getFile() != null) {
			this.tilesetFileLabel.setText("Tileset: " + paramGraphicsBank.getFile().getName());
			this.tilesetFileLabel.setToolTipText(paramGraphicsBank.getFile().toString());
		} else {
			this.tilesetFileLabel.setText("Tileset: * Unsaved *");
			this.tilesetFileLabel.setToolTipText("");
		}
	}

	/**
	 * Retrieve the current graphics bank
	 * 
	 * @return a GraphicsBank being used
	 */
	public GraphicsBank getCurrentGraphicsBank() {
		return this.gfx;
	}

	/**
	 * Save the map to a file
	 * 
	 * @param fileToSave
	 *            the file to save
	 */
	public void saveFile(File fileToSave) {
		if (this.scene.getTileset().isUnsaved()) {
			PromptDialog.tell("Please save your tileset first");
			return;
		}
		try {
			this.scene.saveScene(fileToSave);
			this.openFile = fileToSave;
			this.mainFrame.validate();
		} catch (Exception localException) {
			PromptDialog.tell("Could not save: " + localException);
			localException.printStackTrace();
		}
	}

	/**
	 * Open a map file
	 * 
	 * @param fileToOpen
	 *            the file to open
	 */
	public void openFile(File fileToOpen) {
		try {
			this.zoomLevel = 1.0F;
			this.scene = Scene.loadScene(fileToOpen);
			this.map = this.scene.getMap();
			setGraphicsBank(this.scene.getTileset());

			this.mapPanel.setMap(this.map);

			setIgnoreEffectChanges(true);
			this.r.setValue((int) (this.scene.effect_rScale * 100.0F));
			this.g.setValue((int) (this.scene.effect_gScale * 100.0F));
			this.b.setValue((int) (this.scene.effect_bScale * 100.0F));
			this.h.setValue((int) (this.scene.effect_hue * 360.0F));
			setIgnoreEffectChanges(false);
			this.s.setValue((int) (this.scene.effect_sat * 100.0F));

			this.openFile = fileToOpen;
			this.mainFrame.validate();
			this.mapPanel.validate();
			this.mainFrame.repaint();
		} catch (IOException localIOException) {
			DebugUtility.printError("Invalid Map File. " + localIOException);
		}
	}

	/**
	 * Create a new map scene
	 */
	public void newFile() {
		this.scene = new Scene(new Map(10, 10), new ArrayList<Object>(), this.gfx);
		this.zoomLevel = 1.0F;
		this.map = this.scene.getMap();
		setGraphicsBank(this.scene.getTileset());
		this.mapPanel.setMap(this.map);
		this.mapPanel.repaint();
		this.mainFrame.validate();
	}

	JPanel createColorDialog() {
		JPanel localJPanel1 = new JPanel();

		this.r = new JSlider(1, 0, 400, 100);
		this.g = new JSlider(1, 0, 400, 100);
		this.b = new JSlider(1, 0, 400, 100);
		this.h = new JSlider(1, 0, 360, 0);
		this.s = new JSlider(1, 0, 400, 100);

		this.r.setBackground(Color.red);
		this.g.setBackground(Color.green);
		this.b.setBackground(Color.blue);
		this.s.setBackground(Color.gray);

		this.r.setBorder(new TitledBorder("R"));
		this.g.setBorder(new TitledBorder("G"));
		TitledBorder localTitledBorder = new TitledBorder("B");
		localTitledBorder.setTitleColor(Color.white);
		this.b.setBorder(localTitledBorder);
		this.h.setBorder(new TitledBorder("H"));
		this.s.setBorder(new TitledBorder("S"));

		this.r.setToolTipText("Red channel");

		this.r.setPaintTrack(false);
		this.g.setPaintTrack(false);
		this.b.setPaintTrack(false);

		localJPanel1.setLayout(new BorderLayout());
		JPanel localJPanel2 = new JPanel(new FlowLayout());
		localJPanel1.add(localJPanel2, "Center");
		localJPanel2.add(this.r);
		localJPanel2.add(this.g);
		localJPanel2.add(this.b);
		localJPanel2.add(this.h);
		localJPanel2.add(this.s);

		this.r.addChangeListener(this);
		this.g.addChangeListener(this);
		this.b.addChangeListener(this);
		this.h.addChangeListener(this);
		this.s.addChangeListener(this);
		this.effectsResetBtn = new JButton("Reset");
		this.effectsResetBtn.addActionListener(this);
		localJPanel1.add(this.effectsResetBtn, "South");

		return localJPanel1;
	}

	/**
	 * Method that occurs when the JFrames' state has changed
	 * 
	 * @param event
	 *            - the event that caused state to change
	 */
	@Override
	public void stateChanged(ChangeEvent event) {
		if (!this.ignoreEffects) {
			float[] fParams = { this.r.getValue() / 100.0F, this.g.getValue() / 100.0F, this.b.getValue() / 100.0F,
					this.h.getValue() / 360.0F, this.s.getValue() / 100.0F, this.zoomLevel };
			this.scene.setEffect(fParams);
		}
		this.mapPanel.refreshZoom();
	}

	int getPaintMode() {
		if (this.fillBtn.isSelected()) {
			return 1;
		}
		return 0;
	}

	private void setIgnoreEffectChanges(boolean paramBoolean) {
		this.ignoreEffects = paramBoolean;
	}

	void newTileset() {
		this.gfx = new GraphicsBank();
		setGraphicsBank(this.gfx);
	}

	void openTileset() {
		try {
			int i = this.tschooser.showOpenDialog(this.mainFrame);
			if (i == 0) {
				GraphicsBank localGraphicsBank = new GraphicsBank();
				localGraphicsBank.loadMapTileset(this.tschooser.getSelectedFile());
				setGraphicsBank(localGraphicsBank);
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			PromptDialog.tell("Selected file could not be found");
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			PromptDialog.tell("Could not read the file");
			localIOException.printStackTrace();
		}
	}

	void saveTileset() {
		try {
			int i = this.tschooser.showSaveDialog(this.mainFrame);
			if (i == 0) {
				this.gfx.saveMapTileset(this.tschooser.getSelectedFile());
				setGraphicsBank(this.gfx);
			}
		} catch (IOException localIOException) {
			PromptDialog.tell("Could not read the file");
			localIOException.printStackTrace();
		}
	}

	/**
	 * Main entry point for editing map files
	 * 
	 * @param args
	 *            - command line arguments
	 */
	public static void main(String[] args) {
		MapEdit editor = new MapEdit();
		editor.mainFrame.setVisible(true);
	}

	/**
	 * Get the tileset settings panel
	 * 
	 * @return a JPanel for tileset settings
	 */
	public JPanel getTilesetSettingsPanel() {
		return this.tilesetSettingsPanel;
	}

	/**
	 * Set the tileset settings panel
	 * 
	 * @param tilesetSettingsPanel
	 *            -a JPanel to set the tileset settings panel as
	 */
	public void setTilesetSettingsPanel(JPanel tilesetSettingsPanel) {
		this.tilesetSettingsPanel = tilesetSettingsPanel;
	}

	/**
	 * Get the tool menu
	 * 
	 * @return a JMenu that is the tool menu
	 */
	public JMenu getToolMenu() {
		return this.toolMenu;
	}

	/**
	 * Set the tool menu
	 * 
	 * @param toolMenu
	 *            - the JMenu to set the tool menu as
	 */
	public void setToolMenu(JMenu toolMenu) {
		this.toolMenu = toolMenu;
	}

	/**
	 * Get the tilset grid height field
	 * 
	 * @return a JSpinner for the tileset grid height
	 */
	public JSpinner getTilesetGridHField() {
		return this.tilesetGridHField;
	}

	/**
	 * Set the tileset grid height field
	 * 
	 * @param tilesetGridHField
	 *            - the JSpinner to set the grid height field to
	 */
	public void setTilesetGridHField(JSpinner tilesetGridHField) {
		this.tilesetGridHField = tilesetGridHField;
	}

	/**
	 * Get the tileset grid width field
	 * 
	 * @return a JSpinner for the tileset grid width
	 */
	public JSpinner getTilesetGridWField() {
		return this.tilesetGridWField;
	}

	/**
	 * Set the tileset grid width field
	 * 
	 * @param tilesetGridWField
	 *            - the JSpinner to set the grid width field to
	 */
	public void setTilesetGridWField(JSpinner tilesetGridWField) {
		this.tilesetGridWField = tilesetGridWField;
	}

	class redoAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		redoAction() {}

		@Override
		public void actionPerformed(ActionEvent paramActionEvent) {
			MapEdit.redoBtn.doClick();
		}
	}

	class undoAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		undoAction() {}

		@Override
		public void actionPerformed(ActionEvent paramActionEvent) {
			MapEdit.undoBtn.doClick();
		}
	}
}