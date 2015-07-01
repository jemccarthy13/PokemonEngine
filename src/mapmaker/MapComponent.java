package mapmaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JViewport;

public class MapComponent extends JComponent implements MouseListener, MouseMotionListener, MapChangeListener {

	private static final long serialVersionUID = 5804948335596277170L;
	Map map;
	MapEdit mapEdit;
	private int width = 0;
	private int height = 0;
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int activeLayer = 0;
	boolean hideLayers = false;
	boolean showGrid = true;
	boolean stateChanged = false;
	Stack<Object> undoStack;
	Stack<Object> redoStack;
	int grabX = 0;
	int grabY = 0;
	boolean dragged = false;
	int offsetX = 0;
	int offsetY = 0;
	JViewport viewport;
	boolean btn1Pressed = false;
	boolean btn2Pressed = false;
	int oldX = 0;
	int oldY = 0;

	public MapComponent(Map paramMap, MapEdit paramMapEdit) {
		this.map = paramMap;
		this.mapEdit = paramMapEdit;
		this.width = paramMap.getWidth();
		this.height = paramMap.getHeight();

		this.tileWidth = paramMap.getZoomWidth();
		this.tileHeight = paramMap.getZoomHeight();

		setPreferredSize(new Dimension(this.tileWidth * this.width, this.tileHeight * this.height));
		addMouseListener(this);
		addMouseMotionListener(this);
		this.undoStack = new Stack<Object>();
		this.redoStack = new Stack<Object>();
		this.stateChanged = true;
	}

	public void setViewport(JViewport paramJViewport) {
		this.viewport = paramJViewport;
	}

	public synchronized void setMap(Map paramMap) {
		this.map = paramMap;

		this.width = paramMap.getWidth();
		this.height = paramMap.getHeight();

		this.tileWidth = paramMap.getZoomWidth();
		this.tileHeight = paramMap.getZoomHeight();

		setPreferredSize(new Dimension(this.tileWidth * this.width, this.tileHeight * this.height));
		revalidate();
		this.undoStack.clear();
		this.redoStack.clear();
		this.stateChanged = true;
	}

	void refreshZoom() {
		this.tileWidth = this.map.getZoomWidth();
		this.tileHeight = this.map.getZoomHeight();
		setPreferredSize(new Dimension(this.tileWidth * this.width, this.tileHeight * this.height));
		revalidate();
		repaint();
	}

	public synchronized void paintComponent(Graphics paramGraphics) {
		paramGraphics.setColor(Color.white);
		paramGraphics.fillRect(0, 0, this.width * this.tileWidth, this.height * this.tileHeight);
		if (this.hideLayers) {
			this.map.render(paramGraphics, this.viewport.getViewPosition(), this.viewport.getSize(), this.activeLayer);
		} else {
			this.map.render(paramGraphics, this.viewport.getViewPosition(), this.viewport.getSize());
		}
		if (this.showGrid) {
			paramGraphics.setColor(Color.gray);
			for (int i = 0; i < this.width; i++) {
				paramGraphics.drawLine(i * this.tileWidth, 0, i * this.tileWidth, this.height * this.tileHeight);
			}
			for (int i = 0; i < this.height; i++) {
				paramGraphics.drawLine(0, i * this.tileHeight, this.width * this.tileWidth, i * this.tileHeight);
			}
		}
		((Graphics2D) paramGraphics).setStroke(new BasicStroke(2.0F));
		paramGraphics.setColor(Color.black);
		paramGraphics.drawLine(0, 0, this.width * this.tileWidth, 0);
		paramGraphics.drawLine(0, 0, 0, this.height * this.tileHeight);
		paramGraphics.drawLine(this.width * this.tileWidth, 0, this.width * this.tileWidth,
				this.height * this.tileHeight);
		paramGraphics.drawLine(0, this.height * this.tileHeight, this.width * this.tileWidth,
				this.height * this.tileHeight);
	}

	public void mapClicked(int paramInt1, int paramInt2) {
		paramInt1 /= this.tileWidth;
		paramInt2 /= this.tileHeight;
		if ((paramInt1 < this.map.getWidth()) && (paramInt1 >= 0) && (paramInt2 < this.map.getHeight())
				&& (paramInt2 >= 0)) {
			if (this.mapEdit.getPaintMode() == 0) {
				this.map.setTile(paramInt1, paramInt2, this.activeLayer, this.mapEdit.getSelectedTile());
				this.stateChanged = true;
			} else if (this.mapEdit.getPaintMode() == 1) {
				recursiveFlood(paramInt1, paramInt2, this.activeLayer,
						this.map.getTile(paramInt1, paramInt2, this.activeLayer), this.mapEdit.getSelectedTile());
			} else {
				System.out.println("Invalid paint mode");
			}
		}
	}

	public void recursiveFlood(int paramInt1, int paramInt2, int paramInt3, MapTile paramTile1, MapTile paramTile2) {
		if ((paramInt1 < 0) || (paramInt1 > this.map.getWidth() - 1) || (paramInt2 < 0)
				|| (paramInt2 > this.map.getHeight() - 1)) {
			return;
		}
		MapTile localTile = this.map.getTile(paramInt1, paramInt2, paramInt3);
		if ((MapTile.areEqual(localTile, paramTile2)) || (!MapTile.areEqual(localTile, paramTile1))) {
			return;
		}
		this.stateChanged = true;
		this.map.setTile(paramInt1, paramInt2, paramInt3, paramTile2);

		int i = paramInt1 - 1;
		int j = paramInt1 + 1;
		do {
			this.map.setTile(i, paramInt2, paramInt3, paramTile2);
			i--;
			if (i < 0) {
				break;
			}
		} while (MapTile.areEqual(this.map.getTile(i, paramInt2, paramInt3), paramTile1));
		while ((j < this.map.getWidth()) && (MapTile.areEqual(this.map.getTile(j, paramInt2, paramInt3), paramTile1))) {
			this.map.setTile(j, paramInt2, paramInt3, paramTile2);
			j++;
		}
		i++;
		j--;
		for (int k = i; k <= j; k++) {
			recursiveFlood(k, paramInt2 - 1, paramInt3, paramTile1, paramTile2);
			recursiveFlood(k, paramInt2 + 1, paramInt3, paramTile1, paramTile2);
		}
	}

	public void setActiveLayer(int paramInt) {
		if ((paramInt >= 0) && (paramInt < 3)) {
			this.activeLayer = paramInt;
		}
	}

	public int getActiveLayer() {
		return this.activeLayer;
	}

	public void mousePressed(MouseEvent paramMouseEvent) {
		if (this.stateChanged) {
			saveUndoState();
			this.stateChanged = false;
		}
		switch (paramMouseEvent.getButton()) {
		case 1:
			this.btn1Pressed = true;
			mapClicked(paramMouseEvent.getX(), paramMouseEvent.getY());
			repaint();
			break;
		default:
			this.btn2Pressed = true;

			this.grabX = paramMouseEvent.getX();
			this.grabY = paramMouseEvent.getY();
		}
	}

	public void mouseReleased(MouseEvent paramMouseEvent) {
		switch (paramMouseEvent.getButton()) {
		case 1:
			this.btn1Pressed = false;
			this.oldX = paramMouseEvent.getX();
			this.oldY = paramMouseEvent.getY();
			break;
		default:
			this.btn2Pressed = false;
			this.oldX = paramMouseEvent.getX();
			this.oldY = paramMouseEvent.getY();
			if (!this.dragged) {
				Dimension localDimension = this.viewport.getSize();
				Point localPoint = new Point((int) (paramMouseEvent.getX() - localDimension.getWidth() / 2.0D),
						(int) (paramMouseEvent.getY() - localDimension.getHeight() / 2.0D));
				this.viewport.setViewPosition(localPoint);
			}
			this.dragged = false;
		}
	}

	public void mouseEntered(MouseEvent paramMouseEvent) {}

	public void mouseDragged(MouseEvent paramMouseEvent) {
		if ((this.btn1Pressed) && (this.mapEdit.getPaintMode() != 1)) {
			mapClicked(paramMouseEvent.getX(), paramMouseEvent.getY());

			repaint();
		} else if (this.btn2Pressed) {
			int i = paramMouseEvent.getX() - this.grabX;
			int j = paramMouseEvent.getY() - this.grabY;
			Point localPoint1 = this.viewport.getViewPosition();
			Point localPoint2 = new Point(localPoint1.x - i, localPoint1.y - j);
			this.viewport.setViewPosition(localPoint2);

			this.dragged = true;
		}
	}

	public void mouseExited(MouseEvent paramMouseEvent) {}

	public void mouseClicked(MouseEvent paramMouseEvent) {}

	public void mouseMoved(MouseEvent paramMouseEvent) {}

	public void setGrid(boolean paramBoolean) {
		this.showGrid = paramBoolean;
	}

	public void setHideLayers(boolean paramBoolean) {
		this.hideLayers = paramBoolean;
	}

	public void mapChanging(boolean paramBoolean) {
		if (!paramBoolean) {
			saveUndoState();
		} else {
			clearUndoInfo();
		}
	}

	public void mapChanged(boolean paramBoolean) {
		repaint();
	}

	public void clearUndoInfo() {
		this.redoStack.clear();
		this.undoStack.clear();
	}

	void saveUndoState() {
		this.redoStack.clear();
		this.undoStack.push(this.map.toIntArray());
	}

	void undo() {
		if (!this.undoStack.empty()) {
			this.redoStack.push(this.map.toIntArray());
			int[][][] arrayOfInt = (int[][][]) this.undoStack.pop();
			this.map.setAllTiles(arrayOfInt, this.mapEdit.scene.tileset);
		}
	}

	void redo() {
		if (!this.redoStack.empty()) {
			this.undoStack.push(this.map.toIntArray());
			int[][][] arrayOfInt = (int[][][]) this.redoStack.pop();
			this.map.setAllTiles(arrayOfInt, this.mapEdit.scene.tileset);
		}
	}
}