package mazegenerator;

//Deya Sismanyazici

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MazePanel extends JPanel {
	static int SLEEP = 10; // to show animation
	ArrayList<MazeCell> cells;// this array list contain mazecell objects
	boolean visited[][]; // this array stores if a cell in the NxN grid is
							// visited.
	MazeCell enter; // Entrance cell of the maze
	MazeCell exit; // exit cell of the maze
	KeyHandler handler;// to handle key strokes

	public MazePanel() {
		setBackground(Color.WHITE);
	}

	public int getNumCols() {// return number of columns
		return ((int) (1 / MazeCell.width));
	}

	public int getNumRows() {// return number of rows
		return ((int) (1 / MazeCell.height));
	}

	public void generateMaze() {
		exit = null;
		enter = null;
		cells = new ArrayList();// create an array list object, which is empty
								// initially
		Random rand = new Random();// random number generator, which is
									// necessary while generating and solving
									// maze, e.g., select a neigbor randomly
		int n_col = getNumCols();
		int n_row = getNumRows();
		visited = new boolean[n_row][n_col];// Default is false

		// --------------------------------------------
		// create an empty area in the middle of the maze
		int empty_n = n_row / 4;// 1/4 area of the maze in the middle will be
								// empty
		for (int i = -empty_n / 2; i <= empty_n / 2; i++) {
			for (int j = -empty_n / 2; j <= empty_n / 2; j++) {
				visited[n_row / 2 + i][n_col / 2 + j] = true;
			}
		}
		// --------------------------------------------

		// create exit cell randomly at the last row
		int cID = rand.nextInt(n_col); // a random row between 0 and n_col-1
		int rID = n_row - 1;// this is the last row
		exit = new MazeCell(rID, cID, this);
		generate(exit);// Generates the whole maze starting from the exit cell
		enter = getCell(0, rand.nextInt(getNumCols()));// randomly get a cell at
														// the first row as
														// entrance
		repaint();// trigger the paint method of the panel so that the entrance
					// cell will also be drawn
		if (handler == null) {// if handler is not created, create it with
								// current node is the entrance node
			handler = new KeyHandler(enter);
			addKeyListener(handler);
		} else {// if we already have an handler from the previous maze, sets
				// entrance cell as its current node
			handler.current = enter;
		}
	}

	private void generate(MazeCell cell) {// use DFS to generate the maze from
											// the given cell
		draw();
		visited[cell.rowID][cell.colID] = true; // mark the grid represented as
												// the cell as visited
		cells.add(cell);// add cell to the array list
		ArrayList<MazeCell> neigs = cell.getPotentialNeigbors(visited);
		Random rand = new Random();
		while (!neigs.isEmpty()) {
			MazeCell neig = neigs.get(rand.nextInt(neigs.size()));
			neigs.remove(neig);
			if (visited[neig.rowID][neig.colID])
				continue;
			cell.setNeighbor(neig);
			generate(neig);
		}
	}

	private void draw() {// animates by invoking paint method and sleeping
		try {
			repaint();// trigger the paint method which is overriden
			Thread.sleep(SLEEP);
		} catch (Exception e) {
		}
	}

	public void restart() {
		handler.current = enter;
		for (MazeCell c : cells) {
			c.selected = false;
		}
		draw();
	}

	public void solve() {
		for (MazeCell c : cells)
			c.selected = false;// clean selected cells
		solve(enter, new ArrayList());
	}

	private boolean solve(MazeCell c, ArrayList<MazeCell> list) {// DFS to solve
																	// the maze,
																	// receive a
																	// cell and
																	// partial
																	// path to
																	// that cell
																	// from the
																	// root cell
		draw();
		list.add(c);
		if (c == exit)
			return true;
		c.selected = true;
		for (MazeCell neig : c.getNeighbors()) {
			if (list.contains(neig))
				continue;
			boolean succ = solve(neig, list);
			if (succ)
				return true;
		}
		list.remove(c);
		c.selected = false;
		return false;
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (cells == null)
			return;
		for (int i = 0; i < cells.size(); i++) {
			cells.get(i).drawOn(g);
		}
	}

	private MazeCell getCell(int r, int c) {
		for (MazeCell cell : cells) {
			if (cell.rowID == r && cell.colID == c)
				return cell;
		}
		return null;
	}

}

class MazeCell {
	static double width = 0.05;// relative to the panel width
	static double height = 0.05;// relative to the panel height
	MazePanel panel;
	boolean selected = false;

	public MazeCell(int rowID, int colID, MazePanel panel) {
		this.rowID = rowID;
		this.colID = colID;
		this.panel = panel;
	}

	public int rowID;
	public int colID;
	public MazeCell up;
	public MazeCell right;
	public MazeCell down;
	public MazeCell left;

	public int getX() {
		return getCellWidth() * colID;
	}

	public int getCellWidth() {
		return (int) (panel.getWidth() * width);
	}

	public int getY() {
		return getCellHeight() * rowID;
	}

	public int getCellHeight() {
		return (int) (panel.getWidth() * height);
	}

	public void drawOn(Graphics g) {
		g.setColor(Color.BLACK);
		if (up == null && this != panel.enter)
			g.drawLine(getX(), getY(), getX() + getCellWidth(), getY());
		if (down == null && this != panel.exit)
			g.drawLine(getX(), getY() + getCellHeight(), getX()
					+ getCellWidth(), getY() + getCellHeight());
		if (left == null)
			g.drawLine(getX(), getY(), getX(), getY() + getCellHeight());
		if (right == null)
			g.drawLine(getX() + getCellWidth(), getY(),
					getX() + getCellWidth(), getY() + getCellHeight());
		if (selected) {
			// draw selected cells as oval of 0.8x size of the cell
			// 0.1 will be empty in every side and blue ball will be in the
			// middle
			g.setColor(Color.BLUE);
			g.fillOval(getX() + (int) (getCellWidth() * 0.1), getY()
					+ (int) (getCellHeight() * 0.1),
					(int) (getCellWidth() * 0.8), (int) (getCellHeight() * 0.8));
		}
		if (this == panel.exit) {
			g.setColor(Color.GREEN);
			g.fillOval(getX(), getY(), getCellWidth(), getCellHeight());
		}
		if (this == panel.enter) {
			g.setColor(Color.RED);
			g.fillOval(getX(), getY(), getCellWidth(), getCellHeight());
		}
	}

	// determine which neighbor is this cell.
	public void setNeighbor(MazeCell c) {
		if (c.rowID == rowID && c.colID == colID + 1) {
			right = c;
			c.left = this;
		}
		if (c.rowID == rowID && c.colID == colID - 1) {
			left = c;
			c.right = this;
		}
		if (c.rowID == rowID - 1 && c.colID == colID) {
			up = c;
			c.down = this;
		}
		if (c.rowID == rowID + 1 && c.colID == colID) {
			down = c;
			c.up = this;
		}
	}

	public ArrayList<MazeCell> getNeighbors() {
		ArrayList<MazeCell> l = new ArrayList<MazeCell>();
		if (up != null)
			l.add(up);
		if (down != null)
			l.add(down);
		if (left != null)
			l.add(left);
		if (right != null)
			l.add(right);
		return l;
	}

	public ArrayList<MazeCell> getPotentialNeigbors(boolean visited[][]) {
		ArrayList<MazeCell> l = new ArrayList<MazeCell>();
		int n_col = panel.getNumCols();
		int n_row = panel.getNumRows();
		if (rowID + 1 < n_row) {
			if (!visited[rowID + 1][colID]) {
				l.add(new MazeCell(rowID + 1, colID, panel));
			}
		}
		if (colID + 1 < n_col) {
			if (!visited[rowID][colID + 1]) {
				l.add(new MazeCell(rowID, colID + 1, panel));
			}
		}

		if (rowID - 1 >= 0) {
			if (!visited[rowID - 1][colID]) {
				l.add(new MazeCell(rowID - 1, colID, panel));
			}
		}
		if (colID - 1 >= 0) {
			if (!visited[rowID][colID - 1]) {
				l.add(new MazeCell(rowID, colID - 1, panel));
			}
		}
		return l;
	}
}

class KeyHandler extends KeyAdapter {
	MazeCell current;

	public KeyHandler(MazeCell cell) {
		current = cell;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (current.left != null) {
				setCurrent(current.left);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (current.right != null) {
				setCurrent(current.right);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (current.up != null) {
				setCurrent(current.up);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (current.down != null) {
				setCurrent(current.down);
			}
		}
		current.panel.repaint();
	}

	private void setCurrent(MazeCell c) {
		if (c == c.panel.exit) {
			JOptionPane.showMessageDialog(null, "Congratulations!!!");
		} else if (c.selected || c == c.panel.enter)
			current.selected = false;// remove blue dot since you get back
		else
			c.selected = true;
		current = c;
	}
}