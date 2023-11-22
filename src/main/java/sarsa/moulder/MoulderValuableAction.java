package sarsa.moulder;


public class MoulderValuableAction {

	// action acumulated value
	private int _value;

	// the action
	private int _pieceType;

	private double _alpha = 1;

	public MoulderValuableAction(int pieceType) {
		_pieceType = pieceType;
	}

	public void setValue(int value) {
		_value = value;
	}

	public int getValue() {
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
