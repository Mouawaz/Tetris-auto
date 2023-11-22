package pieces;

public class PieceO extends Piece {

	public PieceO() {
		super(1);

	}

	@Override
	protected void generateRepresentations() {
		_matrix[0][0] = _matrix[0][1] = _matrix[1][0] = _matrix[1][1] = 'x';
		_representations[0] = new PieceRepresentation(_matrix);
		_matrix[0][0] = _matrix[0][1] = _matrix[1][0] = _matrix[1][1] = 0;
	}

}
