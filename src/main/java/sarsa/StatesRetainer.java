package sarsa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatesRetainer {

	private Map<State, List<ValuableAction>> _statesActionsMap;

	public StatesRetainer() {
		_statesActionsMap = new HashMap<State, List<ValuableAction>>();
	}

	public void addState(State s) {
		_statesActionsMap.put(s, s.createInitialValuableActions());
	}

	public List<ValuableAction> getActionsForState(State s) {
		if (!_statesActionsMap.containsKey(s)) {
			List<ValuableAction> actions = s.createInitialValuableActions();
			_statesActionsMap.put(s, actions);
			return actions;
		}

		return _statesActionsMap.get(s);
	}

}
