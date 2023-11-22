package sarsa;

import java.util.BitSet;

import tetris.BoardGUI;
import tetris.Main;
import utils.Triplet;

public class LearningTransitioner {
	private static LearningTransitioner _instance;

	private static final int REWARD_LINE_ADDED = -35;

	private static final int REWARD_LINE_REMOVED = 400;

	public static final int REWARD_LOST_GAME = -1000;

	private static final int REWARD_HOLE_DOWN = -30;

	private static final int REWARD_NO_HOLE_DOWN = 50;

	private static final int REWARD_LINE_DIFF = -20;

	private static short _boardHeight;

	private static short _boardWidth;

	private BitSet[] _currentBoard;

	private Integer _currentBoardLastLine = 0;

	private BitSet[] _boardFromState;

	private static void setBoardDimensions(short width, short height) {
		_boardWidth = width;
		_boardHeight = height;
	}

	public static LearningTransitioner getInstance() {
		if (_instance != null) {
			return _instance;
		}
		_instance = new LearningTransitioner();
		return _instance;
	}

	private LearningTransitioner() {
		setBoardDimensions(BoardGUI.cellsWidth, BoardGUI.cellsHeight);

		_currentBoard = new BitSet[_boardHeight + 8];
		_boardFromState = new BitSet[_boardHeight + 8];
		for (int i = 0; i < _currentBoard.length; i++) {
			_currentBoard[i] = new BitSet();
			_boardFromState[i] = new BitSet();
		}
	}

	public void resetCurrentBoard() {
		for (int i = 0; i < _currentBoard.length; i++) {
			_currentBoard[i].clear();
		}
		_currentBoardLastLine = 0;
	}

	public Triplet<BoardContour, Integer, Integer> exectuteActionAndReturnReward(ValuableAction action) {
		return executeAndEvaluate(_currentBoard, action, _currentBoardLastLine);
	}

	public int evaluateState(State s, ValuableAction action) {
		short contour[] = s.getBoardContour().getContour();
		int lastLine = contour[0];
		for (int i = 0; i < contour.length; i++) {
			if (contour[i] > lastLine) {
				lastLine = contour[i];
			}
		}

		for (int i = 0; i < _boardFromState.length; i++) {
			for (int j = _currentBoard[i].nextSetBit(0); j >= 0; j = _currentBoard[i].nextSetBit(j + 1)) {
				_boardFromState[i].set(j);
			}

		}

		int ret = executeAndEvaluate(_boardFromState, action, lastLine).getSnd();

		for (int i = 0; i < _boardFromState.length; i++) {
			_boardFromState[i].clear();
		}

		return ret;
	}

	// returns contour, reward, linesRemoved
	private Triplet<BoardContour, Integer, Integer> executeAndEvaluate(BitSet[] board, ValuableAction action,
			Integer boardLastLine) {
		if (Main.DEBUG) {
			System.out.println(action.getPosition());
			System.out.println(action.getPieceRepresentation());
			System.out.println();
		}
		BitSet pieceRepresentation[] = action.getPieceRepresentation().getRepresentation();
		int pieceWidth = action.getPieceRepresentation().getWidth();
		int pieceHieght = action.getPieceRepresentation().getHeight();
		int position = action.getPosition();

		// including 0
		int row = board.length - 1;
		boolean colision = false;
		for (; row >= 0; row--) {
			for (int i = 0; i <= board.length - 1 - row && !colision; i++) {
				for (int j = 0; j < pieceWidth && !colision; j++) {
					if (board[row + i].get(position + j) && pieceRepresentation[i].get(j)) {
						colision = true;
					}
				}
			}
			if (colision) {
				break;
			}
		}

		int reward = 0;
		int gapsAdded = 0;
		// adding the piece to the newBoard
		row++;
		for (int i = 0; i < pieceHieght; i++) {
			for (int j = 0; j < pieceWidth; j++) {
				if (pieceRepresentation[i].get(j)) {
					board[row + i].set(position + j);
					// penalization if hole beneath the piece!
					int index = 1;
					while (row + i >= index && !board[row + i - index].get(position + j) && index <= 2) {
						gapsAdded++;
						index++;
					}
				}

			}
		}

		reward += gapsAdded > 0 ? gapsAdded * REWARD_HOLE_DOWN : REWARD_NO_HOLE_DOWN;

		// determining last line that has at least one position set
		int lastLine = 0;
		for (int i = board.length - 1; i >= 0; i--) {
			int bitIndex = board[i].nextSetBit(0);
			if (bitIndex >= 0 && bitIndex < _boardWidth) {
				lastLine = i;
				break;
			}
		}
		if (lastLine >= _boardHeight) {
			// if the game is over, return here
			return new Triplet<BoardContour, Integer, Integer>(new BoardContour(board, lastLine >= _boardHeight),
					REWARD_LOST_GAME, 0);
		}

		// eliminate lines if necessary
		int linesCleared = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i].nextClearBit(0) >= _boardWidth) {
				linesCleared++;
				eliminateFilledLine(board, i);
				// line i need to be reevaluated
				i--;
			}
		}
		int prevLastLine = boardLastLine;
		boardLastLine = lastLine;

		BoardContour contour = new BoardContour(board);
		short contourArray[] = contour.getContour();

		short maxHeight = contourArray[0];
		double avg = maxHeight;
		for (int i = 1; i < contourArray.length; i++) {
			avg += contourArray[i];
			if (contourArray[i] > maxHeight) {
				maxHeight = contourArray[i];
			}
		}
		avg /= contourArray.length;

		reward += (maxHeight - avg) * REWARD_LINE_DIFF;

		return new Triplet<BoardContour, Integer, Integer>(contour,
				(int) (REWARD_LINE_REMOVED * linesCleared + (lastLine - prevLastLine) * REWARD_LINE_ADDED) + reward,
				linesCleared);
	}

	private void eliminateFilledLine(BitSet board[], int lineNo) {
		BitSet eliminatedLine = board[lineNo];
		for (int i = lineNo; i < board.length - 1; i++) {
			board[i] = board[i + 1];
		}
		board[board.length - 1] = eliminatedLine;
		eliminatedLine.clear();
	}

	public BitSet[] getCurrentBoard() {
		return _currentBoard;
	}

}
