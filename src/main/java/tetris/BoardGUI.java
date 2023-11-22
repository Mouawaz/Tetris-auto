package tetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.BitSet;

import javax.swing.JPanel;

import pieces.PieceRepresentation;
import sarsa.BoardContour;
import sarsa.ValuableAction;

@SuppressWarnings("serial")
public class BoardGUI extends JPanel {

	private static final int CELL_SIZE = 20;
	private static final int BOARD_HEIGHT_PX = 500;
	private static final int BOARD_WIDTH_PX = 300;
	private static final int BOARD_HEIGHT_CELLS = 25;

	public static short cellsWidth;
	public static short cellsHeight;

	private BitSet[] _currentBoard;
	private ValuableAction _currentAction;

	public BoardGUI() {
		setLayout(null);
		setBackground(Color.WHITE);
		setBounds(20, 30, BOARD_WIDTH_PX, BOARD_HEIGHT_PX);
		repaint();

		BoardContour.setBoardDimensions(BoardGUI.cellsWidth);
	}

	public static void setBoardLimits(short width, short height) {
		cellsWidth = width;
		cellsHeight = height;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, BOARD_WIDTH_PX - 1, BOARD_HEIGHT_PX - 1);

		if (_currentBoard != null) {
			drawCurrentState(g);
		}
		drawGrid(g);
	}

	private void drawCurrentState(Graphics g) {
		g.setColor(Color.RED);

		BitSet state[] = _currentBoard;
		for (int i = 0; i < state.length; i++) {
			for (int j = state[i].nextSetBit(0); j >= 0 && j < cellsWidth; j = state[i].nextSetBit(j + 1)) {
				fillCell(g, i, j);
			}
		}
		drawCurrentPiece(g);
	}

	private void drawCurrentPiece(Graphics g) {
		PieceRepresentation pieceRep = _currentAction.getPieceRepresentation();

		BitSet repr[] = pieceRep.getRepresentation();

		for (int i = 0; i < repr.length; i++) {
			for (int j = repr[i].nextSetBit(0); j >= 0 && j < pieceRep.getWidth(); j = repr[i].nextSetBit(j + 1)) {
				fillCell(g, BOARD_HEIGHT_CELLS - pieceRep.getHeight() + i, _currentAction.getPosition() + j);
			}
		}

	}

	private void fillCell(Graphics g, int i, int j) {
		g.fillRect(j * CELL_SIZE, BOARD_HEIGHT_PX - (i + 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
	}

	private void drawGrid(Graphics g) {
		g.setColor(Color.BLACK);
		// draw horizontal lines
		for (int i = CELL_SIZE; i <= BOARD_HEIGHT_PX; i += CELL_SIZE) {
			g.drawLine(0, i, BOARD_WIDTH_PX, i);
		}
		// draw vertical lines
		for (int i = CELL_SIZE; i <= BOARD_WIDTH_PX; i += CELL_SIZE) {
			g.drawLine(i, 0, i, BOARD_HEIGHT_PX);
		}

		// drawing board limits
		g.setColor(Color.RED);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(0, BOARD_HEIGHT_PX - cellsHeight * CELL_SIZE, cellsWidth * CELL_SIZE, BOARD_HEIGHT_PX - cellsHeight
				* CELL_SIZE);
		g2.drawLine(cellsWidth * CELL_SIZE, BOARD_HEIGHT_PX, cellsWidth * CELL_SIZE, BOARD_HEIGHT_PX - cellsHeight
				* CELL_SIZE);

	}

	public int getCellsWidth() {
		return cellsWidth;
	}

	public int getCellsHeight() {
		return cellsHeight;
	}

	public void setState(BitSet board[], ValuableAction action) {
		_currentBoard = board;
		_currentAction = action;
		repaint();
	}

}
