package pieces;

import java.util.BitSet;

public class PieceRepresentation {
	private BitSet _levels[];
	private int _width = 0;
	private int _height = 0;

	private final boolean DEBUG = false;

	public PieceRepresentation(char matrix[][]) {
		if (DEBUG) {
			System.out.println("creating rep");
			for (int i = 3; i >= 0; i--) {
				for (int j = 0; j < 4; j++) {
					if (matrix[i][j] == 'x')
						System.out.print('x');
					else
						System.out.print('0');
				}
				System.out.println();
			}
			System.out.println("finised creating rep");
		}
		_levels = new BitSet[4];
		for (int i = 0; i < 4; i++) {
			_levels[i] = new BitSet();
			for (int j = 0; j < 4; j++) {
				if (matrix[i][j] == 'x') {
					_levels[i].set(j);
					_width = Math.max(_width, j + 1);
					_height = Math.max(_height, i + 1);
				}
			}
		}
	}

	public BitSet[] getRepresentation() {
		return _levels;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		// buf.append("width: " + _width + "\n");
		for (int i = _height - 1; i >= 0; i--) {
			for (int j = 0; j < _width; j++) {
				if (_levels[i].get(j)) {
					buf.append('x');
				} else
					buf.append(' ');
			}
			buf.append('\n');
		}
		return buf.toString();
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

}
