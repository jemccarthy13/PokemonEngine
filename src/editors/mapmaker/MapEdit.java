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
	private static TileChooser tileChooser;
	private static JSplitPane split;
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
	private static JButton undoBtn, redoBtn;
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
		zoomLevel = 1.0F;
		openFile = null;
		try {
			scene = Scene.loadScene("lastOpenScene.dat");
		} catch (IOException localIOException) {
			scene = new Scene();
		}
		map = scene.getMap();
		gfx = scene.getTileset();

		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		mainFrame = new JFrame();
		mainFrame.setTitle("Tile-Based Map Editor");

		tileChooser = new TileChooser(gfx, mainFrame);
		chooser = new JFileChooser("scenes");
		tschooser = new JFileChooser("gfx");

		tschooser.setSelectedFile(new File("C:/Users/John/git/PokemonEngine/mapmaker/Tiles.mapeditor"));
		chooser.setSelectedFile(new File("C:/Users/John/git/PokemonEngine/mapmaker/Maps/Johto.map"));
		JPanel localJPanel1 = (JPanel) mainFrame.getContentPane();
		JPanel localJPanel2 = new JPanel(new BorderLayout());
		JPanel localJPanel3 = new JPanel(new BorderLayout());

		localJPanel1.setLayout(new BorderLayout());
		localJPanel1.add(localJPanel2, "Center");
		localJPanel2.add(localJPanel3, "Center");

		colorDialog = createColorDialog();

		setupMenus();

		setupToolbars();

		localJPanel3.setLayout(new BorderLayout());

		localJPanel2.add(innerToolBar, "North");
		localJPanel1.add(outerToolBar, "North");

		chooserPanel = new JPanel(new BorderLayout());

		JScrollPane localJScrollPane = new JScrollPane(tileChooser);
		chooserPanel.add(localJScrollPane, "Center");
		JTabbedPane localJTabbedPane = new JTabbedPane();
		localJTabbedPane.add("Tiles", chooserPanel);

		tilesetFileLabel = new JLabel("Tileset: * unsaved *");
		tilesetInfoPane = new JPanel(new BorderLayout());

		JPanel localJPanel4 = new JPanel(new FlowLayout());

		tilesetInfoPane.add(localJPanel4, "Center");
		chooserPanel.add(tilesetInfoPane, "South");
		tilesetInfoPane.add(tilesetFileLabel, "North");

		tilesetOpenBtn = makeBtn("Open", "icons/opents.gif", "Load Tileset");
		tilesetNewBtn = makeBtn("New", "icons/newts.gif", "New Tileset");
		tilesetSaveBtn = makeBtn("Save", "icons/savets.gif", "Save Tileset");
		localJPanel4.add(tilesetOpenBtn);
		localJPanel4.add(tilesetNewBtn);
		localJPanel4.add(tilesetSaveBtn);
		settingsPanel = new JPanel(new BorderLayout());
		settingsPanel.setBorder(new TitledBorder("Settings"));
		settingsPanel.add(colorDialog, "Center");

		localJTabbedPane.add("Settings", settingsPanel);
		mapPanel = new MapComponent(map, this);
		mapScroll = new JScrollPane(mapPanel);
		mapPanel.setViewport(mapScroll.getViewport());

		split = new JSplitPane();
		split.setDividerLocation(250);
		split.setLeftComponent(localJTabbedPane);
		split.setRightComponent(mapScroll);
		localJPanel3.add(split, "Center");

		split.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
				MapEdit.tileChooser.setWidth(MapEdit.split.getDividerLocation());
			}
		});
		mainFrame.setSize(new Dimension(830, 650));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (;;) {
			mapPanel.repaint();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException localInterruptedException) {}
		}
	}

	private void setupMenus() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		setToolMenu(new JMenu("Tools"));
		editMenu = new JMenu("Edit");
		helpMenu = new JMenu("Help");
		openMI = new JMenuItem("Open...");
		newMI = new JMenuItem("New");
		saveMI = new JMenuItem("Save");
		saveAsMI = new JMenuItem("Save As...");
		exitMI = new JMenuItem("Exit");
		undoMI = new JMenuItem("Undo");
		redoMI = new JMenuItem("Redo");
		about = new JMenuItem("About...(N/A)");
		howToUse = new JMenuItem("How to use LevelEdit (N/A)");

		openMI.addActionListener(this);
		newMI.addActionListener(this);
		saveMI.addActionListener(this);
		saveAsMI.addActionListener(this);
		exitMI.addActionListener(this);
		undoMI.addActionListener(this);
		redoMI.addActionListener(this);
		about.addActionListener(this);
		howToUse.addActionListener(this);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

		fileMenu.add(openMI);
		fileMenu.add(newMI);
		fileMenu.add(saveMI);
		fileMenu.add(saveAsMI);
		fileMenu.add(exitMI);
		editMenu.add(undoMI);
		editMenu.add(redoMI);
		helpMenu.add(about);
		helpMenu.add(howToUse);
		mainFrame.setJMenuBar(menuBar);
	}

	private void setupToolbars() {
		outerToolBar = new JToolBar();
		innerToolBar = new JToolBar();

		saveBtn = makeBtn("Save", "icons/save.gif", "Save map");
		openBtn = makeBtn("Open...", "icons/open.gif", "Open map...");
		newBtn = makeBtn("New", "icons/new.gif", "New map");
		clearBtn = makeBtn("Clear", "icons/clear.gif", "Reset map (Delete all tiles)");

		ButtonGroup localButtonGroup = new ButtonGroup();
		layerButtons = new JToggleButton[3];
		layerButtons[2] = makeToggleBtn("Layer 3", "icons/top.gif", "Edit the top layer");
		layerButtons[1] = makeToggleBtn("Layer 2", "icons/mid.gif", "Edit the middle layer");
		layerButtons[0] = makeToggleBtn("Layer 1", "icons/bottom.gif", "Edit the bottom layer");
		localButtonGroup.add(layerButtons[0]);
		localButtonGroup.add(layerButtons[1]);
		localButtonGroup.add(layerButtons[2]);

		gridBtn = makeToggleBtn("Grid", "icons/grid.gif", "Show/Hide Grid");
		hideBtn = makeToggleBtn("Hide other layers", "icons/hideoth.gif", "Hide other layers");

		zoomInBtn = makeBtn("Zoom in", "icons/zoomin.gif", "Zoom in");
		zoomFullBtn = makeBtn("Zoom 100%", "icons/zoomfull.gif", "Zoom to 100%");
		zoomOutBtn = makeBtn("Zoom out", "icons/zoomout.gif", "Zoom out");

		shiftRightBtn = makeBtn("->", "icons/shiftRight.gif", "Move tiles right");
		shiftLeftBtn = makeBtn("<-", "icons/shiftLeft.gif", "Move tiles left");
		shiftUpBtn = makeBtn("^", "icons/shiftUp.gif", "Move tiles up");
		shiftDownBtn = makeBtn("\\/", "icons/shiftDown.gif", "Move tiles down");
		increaseWidthBtn = makeBtn("<- ->", "icons/increaseWidth.gif", "Increase field width");
		decreaseWidthBtn = makeBtn("-> <-", "icons/decreaseWidth.gif", "Decrease field width");
		increaseHeightBtn = makeBtn("\\/ +", "icons/increaseHeight.gif", "Increase field height");
		decreaseHeightBtn = makeBtn("^^ -", "icons/decreaseHeight.gif", "Decrease field height");

		fillBtn = makeToggleBtn("Flood Fill", "icons/fill.gif", "Flood fill mode");
		undoBtn = makeBtn("Undo", "icons/undo.gif", "Undo");
		redoBtn = makeBtn("Redo", "icons/redo.gif", "Redo");

		undoMI.setAccelerator(KeyStroke.getKeyStroke(new Character('Z'), Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		redoMI.setAccelerator(KeyStroke.getKeyStroke(new Character('Y'), Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));

		((JPanel) mainFrame.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(90, 2), "Undo");
		((JPanel) mainFrame.getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(89, 2), "Redo");
		((JPanel) mainFrame.getContentPane()).getActionMap().put("Undo", new undoAction());
		((JPanel) mainFrame.getContentPane()).getActionMap().put("Redo", new redoAction());

		outerToolBar.add(newBtn);
		outerToolBar.add(openBtn);
		outerToolBar.add(saveBtn);
		outerToolBar.add(clearBtn);

		outerToolBar.addSeparator();
		outerToolBar.add(undoBtn);
		outerToolBar.add(redoBtn);
		outerToolBar.addSeparator();
		outerToolBar.add(fillBtn);

		outerToolBar.addSeparator();
		outerToolBar.add(layerButtons[2]);
		outerToolBar.add(layerButtons[1]);
		outerToolBar.add(layerButtons[0]);

		outerToolBar.addSeparator();
		outerToolBar.add(hideBtn);
		outerToolBar.add(gridBtn);

		outerToolBar.addSeparator();
		outerToolBar.add(zoomInBtn);
		outerToolBar.add(zoomFullBtn);
		outerToolBar.add(zoomOutBtn);
		if (compactToolbars) {
			innerToolBar = outerToolBar;
			outerToolBar.addSeparator();
		}
		innerToolBar.add(shiftLeftBtn);
		innerToolBar.add(shiftRightBtn);
		innerToolBar.add(shiftUpBtn);
		innerToolBar.add(shiftDownBtn);

		innerToolBar.addSeparator();
		innerToolBar.add(increaseWidthBtn);
		innerToolBar.add(decreaseWidthBtn);
		innerToolBar.add(increaseHeightBtn);
		innerToolBar.add(decreaseHeightBtn);

		innerToolBar.addSeparator();

		gridBtn.setSelected(true);
		layerButtons[0].setSelected(true);
	}

	/**
	 * Event that occurs when a key is pressed
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	public void keyPressed(KeyEvent event) {}

	/**
	 * Event that occurs when a key is relesed
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	public void keyReleased(KeyEvent event) {}

	/**
	 * Event that occurs when a key is typed
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 * @param event
	 *            - the event triggering this method
	 */
	public void keyTyped(KeyEvent event) {}

	/**
	 * Method that is called when an action is performed
	 * 
	 * @param event
	 *            - the event triggering this method
	 */
	public void actionPerformed(ActionEvent event) {
		Object localObject = event.getSource();
		if (localObject == zoomInBtn) {
			if (zoomLevel < 5.0F) {
				zoomLevel = ((float) (zoomLevel * 1.2D));
			}
		} else if (localObject == zoomOutBtn) {
			if (zoomLevel > 0.05D) {
				zoomLevel = ((float) (zoomLevel * 0.8D));
			}
		} else if (localObject == zoomFullBtn) {
			zoomLevel = 1.0F;
		} else if ((localObject == undoBtn) || (localObject == undoMI)) {
			mapPanel.undo();
			mapPanel.repaint();
		} else if ((localObject == redoBtn) || (localObject == redoMI)) {
			mapPanel.redo();
			mapPanel.repaint();
		} else if ((localObject == openBtn) || (localObject == openMI)) {
			int i = chooser.showOpenDialog(mainFrame);
			if (i == 0) {
				openFile(chooser.getSelectedFile());
			}
		} else if ((localObject == saveBtn) || (localObject == saveMI)) {
			if (openFile == null) {
				actionPerformed(new ActionEvent(saveAsMI, event.getID(), event.getActionCommand()));
			} else {
				saveFile(openFile);
			}
		} else if ((localObject == newBtn) || (localObject == newMI)) {
			newFile();
			openFile = null;
		} else if (localObject == clearBtn) {
			map.clear();
			mapPanel.repaint();
		} else if (localObject == layerButtons[0]) {
			mapPanel.setActiveLayer(0);
		} else if (localObject == layerButtons[1]) {
			mapPanel.setActiveLayer(1);
		} else if (localObject == layerButtons[2]) {
			mapPanel.setActiveLayer(2);
		} else if (localObject == hideBtn) {
			mapPanel.setHideLayers(hideBtn.isSelected());
			mapPanel.repaint();
		} else if (localObject == gridBtn) {
			mapPanel.setGrid(gridBtn.isSelected());
			mapPanel.repaint();
		} else if (localObject == shiftRightBtn) {
			map.shift(1, 0);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == shiftLeftBtn) {
			map.shift(-1, 0);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == shiftUpBtn) {
			map.shift(0, -1);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == shiftDownBtn) {
			map.shift(0, 1);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == increaseWidthBtn) {
			map.resize(map.getWidth() + 1, map.getHeight());
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == decreaseWidthBtn) {
			map.resize(map.getWidth() - 1, map.getHeight());
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == increaseHeightBtn) {
			map.resize(map.getWidth(), map.getHeight() + 1);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == decreaseHeightBtn) {
			map.resize(map.getWidth(), map.getHeight() - 1);
			mapPanel.setMap(map);
			mapPanel.repaint();
		} else if (localObject == palletteBtn) {
			colorDialog.setVisible(!colorDialog.isVisible());
			palletteBtn.setSelected(colorDialog.isVisible());
		} else if (localObject == effectsResetBtn) {
			setIgnoreEffectChanges(true);
			r.setValue(100);
			g.setValue(100);
			b.setValue(100);
			h.setValue(0);
			s.setValue(100);
			setIgnoreEffectChanges(false);
		} else if (localObject == saveAsMI) {
			int i = chooser.showSaveDialog(mainFrame);
			if (i == 0) {
				saveFile(chooser.getSelectedFile());
			}
		} else if (localObject == exitMI) {
			mainFrame.dispose();
			DebugUtility.quit();
		} else if (localObject == tilesetNewBtn) {
			newTileset();
		} else if (localObject == tilesetOpenBtn) {
			openTileset();
		} else if (localObject == tilesetSaveBtn) {
			saveTileset();
		} else {
			DebugUtility.printError("Unknown source of actionEvent. (The button you just clicked does nothing)");
		}
		if ((localObject == zoomInBtn) || (localObject == zoomOutBtn) || (localObject == zoomFullBtn)
				|| (localObject == effectsResetBtn)) {
			float[] fParams = { r.getValue() / 100.0F, g.getValue() / 100.0F, b.getValue() / 100.0F,
					h.getValue() / 360.0F, s.getValue() / 100.0F, zoomLevel };
			scene.setEffect(fParams);

			map.setZoom(zoomLevel);
			mapPanel.refreshZoom();
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
		if (borderedButtons) {
			localJButton.setBorder(new LineBorder(Color.gray, 1, false));
		} else if (compactToolbars) {
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
		if (borderedButtons) {
			localJToggleButton.setBorder(new LineBorder(Color.gray, 1, false));
		} else if (compactToolbars) {
			localJToggleButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		return localJToggleButton;
	}

	/**
	 * Retrieve the TileChooser's selected tile
	 * 
	 * @return a maptile
	 */
	public MapTile getSelectedTile() {
		return tileChooser.getSelectedTile();
	}

	private void setGraphicsBank(GraphicsBank paramGraphicsBank) {
		gfx = paramGraphicsBank;
		scene.setTileset(paramGraphicsBank);
		chooserPanel.removeAll();
		tileChooser = new TileChooser(paramGraphicsBank, mainFrame);
		chooserPanel.add(tilesetInfoPane, "South");
		JScrollPane localJScrollPane = new JScrollPane(tileChooser);
		chooserPanel.add(localJScrollPane, "Center");
		mainFrame.repaint();
		if (paramGraphicsBank.getFile() != null) {
			tilesetFileLabel.setText("Tileset: " + paramGraphicsBank.getFile().getName());
			tilesetFileLabel.setToolTipText(paramGraphicsBank.getFile().toString());
		} else {
			tilesetFileLabel.setText("Tileset: * Unsaved *");
			tilesetFileLabel.setToolTipText("");
		}
	}

	/**
	 * Retrieve the current graphics bank
	 * 
	 * @return a GraphicsBank being used
	 */
	public GraphicsBank getCurrentGraphicsBank() {
		return gfx;
	}

	/**
	 * Save the map to a file
	 * 
	 * @param fileToSave
	 *            the file to save
	 */
	public void saveFile(File fileToSave) {
		if (scene.getTileset().isUnsaved()) {
			PromptDialog.tell("Please save your tileset first.", "OK");
			return;
		}
		try {
			scene.saveScene(fileToSave);
			openFile = fileToSave;
			mainFrame.validate();
		} catch (Exception localException) {
			PromptDialog.tell("Could not save: " + localException, "OK");
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
			zoomLevel = 1.0F;
			scene = Scene.loadScene(fileToOpen);
			map = scene.getMap();
			setGraphicsBank(scene.getTileset());

			mapPanel.setMap(map);

			setIgnoreEffectChanges(true);
			r.setValue((int) (scene.effect_rScale * 100.0F));
			g.setValue((int) (scene.effect_gScale * 100.0F));
			b.setValue((int) (scene.effect_bScale * 100.0F));
			h.setValue((int) (scene.effect_hue * 360.0F));
			setIgnoreEffectChanges(false);
			s.setValue((int) (scene.effect_sat * 100.0F));

			openFile = fileToOpen;
			mainFrame.validate();
			mapPanel.validate();
			mainFrame.repaint();
		} catch (IOException localIOException) {
			DebugUtility.printError("Invalid Map File. " + localIOException);
		}
	}

	/**
	 * Create a new map scene
	 */
	public void newFile() {
		scene = new Scene(new Map(10, 10), new ArrayList<Object>(), gfx);
		zoomLevel = 1.0F;
		map = scene.getMap();
		setGraphicsBank(scene.getTileset());
		mapPanel.setMap(map);
		mapPanel.repaint();
		mainFrame.validate();
	}

	JPanel createColorDialog() {
		JPanel localJPanel1 = new JPanel();

		r = new JSlider(1, 0, 400, 100);
		g = new JSlider(1, 0, 400, 100);
		b = new JSlider(1, 0, 400, 100);
		h = new JSlider(1, 0, 360, 0);
		s = new JSlider(1, 0, 400, 100);

		r.setBackground(Color.red);
		g.setBackground(Color.green);
		b.setBackground(Color.blue);
		s.setBackground(Color.gray);

		r.setBorder(new TitledBorder("R"));
		g.setBorder(new TitledBorder("G"));
		TitledBorder localTitledBorder = new TitledBorder("B");
		localTitledBorder.setTitleColor(Color.white);
		b.setBorder(localTitledBorder);
		h.setBorder(new TitledBorder("H"));
		s.setBorder(new TitledBorder("S"));

		r.setToolTipText("Red channel");

		r.setPaintTrack(false);
		g.setPaintTrack(false);
		b.setPaintTrack(false);

		localJPanel1.setLayout(new BorderLayout());
		JPanel localJPanel2 = new JPanel(new FlowLayout());
		localJPanel1.add(localJPanel2, "Center");
		localJPanel2.add(r);
		localJPanel2.add(g);
		localJPanel2.add(b);
		localJPanel2.add(h);
		localJPanel2.add(s);

		r.addChangeListener(this);
		g.addChangeListener(this);
		b.addChangeListener(this);
		h.addChangeListener(this);
		s.addChangeListener(this);
		effectsResetBtn = new JButton("Reset");
		effectsResetBtn.addActionListener(this);
		localJPanel1.add(effectsResetBtn, "South");

		return localJPanel1;
	}

	/**
	 * Method that occurs when the JFrames' state has changed
	 * 
	 * @param event
	 *            - the event that caused state to change
	 */
	public void stateChanged(ChangeEvent event) {
		if (!ignoreEffects) {
			float[] fParams = { r.getValue() / 100.0F, g.getValue() / 100.0F, b.getValue() / 100.0F,
					h.getValue() / 360.0F, s.getValue() / 100.0F, zoomLevel };
			scene.setEffect(fParams);
		}
		mapPanel.refreshZoom();
	}

	int getPaintMode() {
		if (fillBtn.isSelected()) {
			return 1;
		}
		return 0;
	}

	private void setIgnoreEffectChanges(boolean paramBoolean) {
		ignoreEffects = paramBoolean;
	}

	void newTileset() {
		gfx = new GraphicsBank();
		setGraphicsBank(gfx);
	}

	void openTileset() {
		try {
			int i = tschooser.showOpenDialog(mainFrame);
			if (i == 0) {
				GraphicsBank localGraphicsBank = new GraphicsBank();
				localGraphicsBank.loadMapTileset(tschooser.getSelectedFile());
				setGraphicsBank(localGraphicsBank);
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			PromptDialog.tell("Selected file could not be found", "OK");
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			PromptDialog.tell("Could not read the file", "OK");
			localIOException.printStackTrace();
		}
	}

	void saveTileset() {
		try {
			int i = tschooser.showSaveDialog(mainFrame);
			if (i == 0) {
				gfx.saveMapTileset(tschooser.getSelectedFile());
				setGraphicsBank(gfx);
			}
		} catch (IOException localIOException) {
			PromptDialog.tell("Could not read the file", "OK");
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
		new MapEdit();
	}

	/**
	 * Get the tileset settings panel
	 * 
	 * @return a JPanel for tileset settings
	 */
	public JPanel getTilesetSettingsPanel() {
		return tilesetSettingsPanel;
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
		return toolMenu;
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
		return tilesetGridHField;
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
		return tilesetGridWField;
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

		public void actionPerformed(ActionEvent paramActionEvent) {
			MapEdit.redoBtn.doClick();
		}
	}

	class undoAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		undoAction() {}

		public void actionPerformed(ActionEvent paramActionEvent) {
			MapEdit.undoBtn.doClick();
		}
	}
}