package sarsa;

import pieces.PieceRepresentation;

public class ValuableAction {

	private PieceRepresentation _pieceRepresentation;

	// the column from where the piece is dropped
	private int _position;

	private double _value;

	private int _pieceType;

	private double _alpha = 1;

	public ValuableAction(PieceRepresentation pr, int pos, int pieceType) {
		_pieceRepresentation = pr;
		_position = pos;
		_value = 0;
		_pieceType = pieceType;
	}

	public int getPosition() {
		return _position;
	}

	public PieceRepresentation getPieceRepresentation() {
		return _pieceRepresentation;
	}

	public void setValue(double value) {
		_value = value;
	}

	public double getValue() {
		return _value;
	}

	public double getAlpha() {
		return _alpha;
	}

	public void updateAlpha() {
		_alpha -= 0.05;
		if (_alpha <= 0) {
			_alpha = 0;
		}
	}

	public int getPieceType() {
		return _pieceType;
	}
}
