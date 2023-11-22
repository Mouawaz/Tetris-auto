package pieces;

public class PieceI extends Piece {

	public PieceI() {
		super(2);
	}

	@Override
	protected void generateRepresentations() {
		// horizontal bar (xxxx)
		_matrix[0][0] = _matrix[0][1] = _matrix[0][2] = _matrix[0][3] = 'x';
		_representations[0] = new PieceRepresentation(_matrix);
		_matrix[0][0] = _matrix[0][1] = _matrix[0][2] = _matrix[0][3] = 0;

		// vertical bar
		_matrix[0][0] = _matrix[1][0] = _matrix[2][0] = _matrix[3][0] = 'x';
		_representations[1] = new PieceRepresentation(_matrix);
		_matrix[0][0] = _matrix[1][0] = _matrix[2][0] = _matrix[3][0] = 0;
	}
}
