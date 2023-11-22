package sarsa;

import java.util.BitSet;

public class BoardContour {

	private short[] _contour;

	private static short _boardWidth;

	public boolean isGameFinished = false;

	public static void setBoardDimensions(short width) {
		_boardWidth = width;
	}

	public BoardContour(short[] contour) {
		_contour = contour;
	}

	public BoardContour() {
		_contour = new short[_boardWidth];
		for (int i = 0; i < _boardWidth; i++) {
			_contour[i] = 0;
		}
	}

	public BoardContour(BitSet[] board) {
		this();
		for (int i = board.length - 1; i >= 0; i--) {
			for (int j = board[i].nextSetBit(0); j >= 0 && j < _boardWidth; j = board[i].nextSetBit(j + 1)) {
				if (_contour[j] == 0) {
					_contour[j] = (short) (i + 1);
				}
			}
		}
	}

	public BoardContour(BitSet[] _completeBoard, boolean finishedGame) {
		this(_completeBoard);
		isGameFinished = finishedGame;
	}

	public short[] getContour() {
		return _contour;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof BoardContour)) {
			return false;
		}
		BoardContour bc2 = (BoardContour) o;
		if (_contour.length != bc2._contour.length) {
			return false;
		}

		for (int i = 0; i < _contour.length; i++) {
			if (_contour[i] != bc2._contour[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (int i = 0; i < _contour.length; i++) {
			hashCode += _contour[i] * PRIME_NUMBERS[i + 10];
		}
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("contour: \n");
		for (short c : _contour) {
			buff.append("" + c + " ");
		}
		buff.append("\n");
		return buff.toString();
	}

	private static final int PRIME_NUMBERS[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61,
			67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
			181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
			307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431,
			433, 439, 443, 449, 457, 461 };
}
