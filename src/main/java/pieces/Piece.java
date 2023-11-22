package pieces;

public abstract class Piece {

	protected char[][] _matrix;
	protected PieceRepresentation _representations[];

	public Piece(int noOfRepresentations) {
		_representations = new PieceRepresentation[noOfRepresentations];

		_matrix = new char[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				_matrix[i][j] = 0;
			}
		}

		generateRepresentations();
	}

	protected abstract void generateRepresentations();

	public PieceRepresentation[] getRepresentations() {
		return _representations;
	}

}
