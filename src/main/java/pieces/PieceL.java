package pieces;

public class PieceL extends Piece {

	public PieceL() {
		super(4);
	}

	@Override
	protected void generateRepresentations() {
		_matrix[1][2] = _matrix[0][0] = _matrix[0][1] = _matrix[0][2] = 'x';
		_representations[0] = new PieceRepresentation(_matrix);
		_matrix[1][2] = _matrix[0][0] = _matrix[0][1] = _matrix[0][2] = 0;
		
		_matrix[0][0] = _matrix[1][0] = _matrix[1][1] = _matrix[1][2] = 'x';
		_representations[1] = new PieceRepresentation(_matrix);
		_matrix[0][0] = _matrix[1][0] = _matrix[1][1] = _matrix[1][2] = 0;

		_matrix[0][1] = _matrix[1][1] = _matrix[2][1] = _matrix[2][0] = 'x';
		_representations[2] = new PieceRepresentation(_matrix);
		_matrix[0][1] = _matrix[1][1] = _matrix[2][1] = _matrix[2][0] = 0;

		_matrix[0][0] = _matrix[1][0] = _matrix[2][0] = _matrix[0][1] = 'x';
		_representations[3] = new PieceRepresentation(_matrix);
		_matrix[0][0] = _matrix[1][0] = _matrix[2][0] = _matrix[0][1] = 0;
	}

}
