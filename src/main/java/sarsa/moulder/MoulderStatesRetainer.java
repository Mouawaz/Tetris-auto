package sarsa.moulder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sarsa.BoardContour;

public class MoulderStatesRetainer {

	private Map<BoardContour, List<MoulderValuableAction>> _stateActionsMap;

	private static MoulderStatesRetainer _instance = null;

	private MoulderStatesRetainer() {
		_stateActionsMap = new HashMap<BoardContour, List<MoulderValuableAction>>();
	}

	public static MoulderStatesRetainer getInstance() {
		if (_instance == null) {
			_instance = new MoulderStatesRetainer();
		}

		return _instance;
	}

	public List<MoulderValuableAction> getActionsForContour(BoardContour contour) {
		if (!_stateActionsMap.containsKey(contour)) {
			List<MoulderValuableAction> list = createInitialActions();
			_stateActionsMap.put(contour, list);
			return list;
		}

		return _stateActionsMap.get(contour);
	}

	private List<MoulderValuableAction> createInitialActions() {
		List<MoulderValuableAction> list = new LinkedList<MoulderValuableAction>();
		for (int x : Moulder.piecesAllowedForBonus) {
			list.add(new MoulderValuableAction(x));
		}
		return list;
	}

	public MoulderValuableAction getSpecificAction(BoardContour contour, int pieceType) {
		List<MoulderValuableAction> actions = getActionsForContour(contour);
		for (MoulderValuableAction action : actions) {
			if (action.getPieceType() == pieceType) {
				return action;
			}
		}
		return null;
	}
}
