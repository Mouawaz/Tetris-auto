package sarsa;

import java.util.LinkedList;
import java.util.List;

import pieces.PieceRepresentation;
import pieces.PieceType;
import tetris.ConfigurationWindow;
import tetris.Main;

public class State {

	// since the state does not change, it makes sense to persist the hash value
	private Integer _hashCode = null;

	int _currentPieceTypeNo;
	BoardContour _board;

	// A state is created from the previous state with the
	public State(BoardContour board, int pieceType) {
		_board = board;
		_currentPieceTypeNo = pieceType;
	}

	public List<ValuableAction> createInitialValuableActions() {
		// initialize valuable actions
		List<ValuableAction> valuableActions = new LinkedList<ValuableAction>();
		PieceRepresentation pieceRep[] = PieceType.getPieceTypeByNumber(_currentPieceTypeNo).getPiece()
				.getRepresentations();

		int cellsWidth = Main.getBoardGUI().getCellsWidth();
		for (int i = 0; i < pieceRep.length; i++) {
			int pieceWidth = pieceRep[i].getWidth();
			for (int j = 0; j <= cellsWidth - pieceWidth; j++) {
				ValuableAction action = new ValuableAction(pieceRep[i], j, _currentPieceTypeNo);
				if (ConfigurationWindow.evaluateStatesWhenCreated) {
					action.setValue(LearningTransitioner.getInstance().evaluateState(this, action));
				}
				valuableActions.add(action);
			}
		}
		return valuableActions;
	}

	public BoardContour getBoardContour() {
		return _board;
	}

	@Override
	public int hashCode() {
		if (_hashCode != null) {
			return _hashCode;
		}

		_hashCode = _board.hashCode() * 7 + _currentPieceTypeNo * 113;

		return _hashCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof State)) {
			return false;
		}
		State state2 = (State) o;
		if (state2._currentPieceTypeNo != _currentPieceTypeNo) {
			return false;
		}

		return _board.equals(state2._board);
	}

	public int getCurrentPieceType() {
		return _currentPieceTypeNo;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(_board.toString());
		PieceRepresentation pr = PieceType.getPieceTypeByNumber(_currentPieceTypeNo).getPiece().getRepresentations()[0];
		buff.append(pr.toString());
		return buff.toString();
	}
}
