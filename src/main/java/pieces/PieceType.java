package pieces;

public enum PieceType {
	I(new PieceI()), // 0
	O(new PieceO()), // 1
	J(new PieceJ()), // 2
	L(new PieceL()), // 3
	S(new PieceS()), // 4
	T(new PieceT()), // 5
	Z(new PieceZ()); // 6
	private Piece _piece;

	private static final PieceType _pieces[];

	static {
		_pieces = new PieceType[7];
		int index = 0;
		for (PieceType pieceType : PieceType.values()) {
			_pieces[index++] = pieceType;
		}
	}

	private PieceType(Piece p) {
		_piece = p;
	}

	public Piece getPiece() {
		return _piece;
	}

	public static PieceType getPieceTypeByNumber(int no) {
		return _pieces[no];
	}

}
