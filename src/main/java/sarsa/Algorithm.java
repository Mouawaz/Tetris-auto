package sarsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sarsa.moulder.Moulder;
import tetris.BoardGUI;
import tetris.Main;
import utils.Triplet;

public class Algorithm {

	private static final int PIECES_PER_GAME = 18;

	StatesRetainer _statesRetainer;

	private static final double EQ_EPS = 0.00001;

	private double _eps = .25;

	private double _GAMA = .95;

	private Random _rand = new Random();

	private State _currentState;
	private ValuableAction _currentAction;
	private LearningTransitioner _transitioner;

	private BoardGUI _boardGUI;

	private int _piecesPlayed;
	private int _score = 0;

	private List<ValuableAction> _selectedActions;

	public Algorithm(BoardGUI boardGUI) {
		_boardGUI = boardGUI;
		_selectedActions = new ArrayList<ValuableAction>();
		_statesRetainer = new StatesRetainer();
		// computeInitialStates();
		setupNewGame(false, true);
	}

	public void setupNewGame(boolean updateGUI, boolean allowExploration) {
		Moulder.resetIndex();
		BoardContour contour = new BoardContour();
		_currentState = new State(contour, Moulder.getNextBrickType(contour));
		_currentAction = getAction(_currentState, updateGUI, allowExploration);
		_transitioner = LearningTransitioner.getInstance();
		_transitioner.resetCurrentBoard();
		if (updateGUI) {
			paintCurrentState();
		}
		_piecesPlayed = 0;
		Main.currentPieceValue.setText("0");

		_score = 0;
		Main.scoreValue.setText("0");
	}

	/**
	 * 
	 * @param updateGUI
	 * @return true if the game ended
	 */
	public boolean runStep(boolean updateGUI, boolean allowExploration) {
		if (_currentState.getBoardContour().isGameFinished || _piecesPlayed == PIECES_PER_GAME) {
			if (_piecesPlayed != PIECES_PER_GAME) {
				_currentAction.setValue(LearningTransitioner.REWARD_LOST_GAME);
				Moulder.updateActionWithReward(_currentState.getBoardContour(), _currentAction.getPieceType(),
						-LearningTransitioner.REWARD_LOST_GAME, null);
			}
			setupNewGame(updateGUI, allowExploration);
			return true;
		}

		_piecesPlayed++;

		LearningTransitioner transitioner = LearningTransitioner.getInstance();
		Triplet<BoardContour, Integer, Integer> p = transitioner.exectuteActionAndReturnReward(_currentAction);

		// Inverting reward here, so a positive reward for builder is negative
		// for moulder and vice versa
		Moulder.updateActionWithReward(_currentState.getBoardContour(), _currentAction.getPieceType(), -p.getSnd(),
				p.getFst());

		_currentState = new State(p.getFst(), Moulder.getNextBrickType(p.getFst()));
		int reward = p.getSnd();
		if (updateGUI) {
			System.out.println("Got reward: " + reward);
		}
		ValuableAction nextAction = getAction(_currentState, updateGUI, allowExploration);

		// update value for previous action
		double currentActionValue = _currentAction.getValue();
		_currentAction.setValue(currentActionValue + _currentAction.getAlpha()
				* (reward + _GAMA * nextAction.getValue() - currentActionValue));
		// slightly decreasing alpha in order to better preserve gained value
		_currentAction.updateAlpha();

		_currentAction = nextAction;

		int linesRemoved = p.getTrd();
		if (linesRemoved > 0) {
			_score += Math.pow(3, linesRemoved - 1);
		}
		if (_currentState.getBoardContour().isGameFinished) {
			_score -= 20;
		}

		if (updateGUI) {
			paintCurrentState();
			Main.currentPieceValue.setText("" + _piecesPlayed);
			Main.scoreValue.setText("" + _score);
		}

		return false;
	}

	public void paintCurrentState() {
		_boardGUI.setState(_transitioner.getCurrentBoard(), _currentAction);
		Main.currentPieceValue.setText("" + _piecesPlayed);
		Main.scoreValue.setText("" + _score);
	}

	/**
	 * 
	 * @param allowExploration
	 * @return score of the game
	 */
	public int playGame(boolean allowExploration) {
		int score = 0;
		setupNewGame(false, allowExploration);
		while (!runStep(false, allowExploration)) {
			score = _score;
		}
		return score;
	}

	// eps - greedy
	private ValuableAction getAction(State s, boolean printDebugInfo, boolean allowExploration) {
		List<ValuableAction> actions = _statesRetainer.getActionsForState(s);
		if (printDebugInfo) {
			System.out.println("===========STATE===========");
			for (ValuableAction action : actions) {
				System.out.println("pos: " + action.getPosition() + "; value: " + action.getValue() + "; alpha: "
						+ action.getAlpha());
				System.out.println(action.getPieceRepresentation());
			}
		}

		double p = _rand.nextDouble();
		if (allowExploration && p < _eps) {
			// not doing something random anymore;
			// return actions.get(_rand.nextInt(actions.size()));
			// chosing the leas selected action instead in order to gain
			// knowledge

			ValuableAction bestAction = actions.get(0);
			_selectedActions.clear();
			_selectedActions.add(bestAction);
			for (ValuableAction action : actions) {
				if (dabs(action.getAlpha() - bestAction.getAlpha()) < EQ_EPS) {
					_selectedActions.add(action);
					continue;
				}

				if (action.getAlpha() > bestAction.getAlpha()) {
					bestAction = action;
					_selectedActions.clear();
					_selectedActions.add(bestAction);
				}
			}

			if (_selectedActions.size() > 1) {
				bestAction = _selectedActions.get(_rand.nextInt(_selectedActions.size()));
			}

			if (printDebugInfo) {
				System.out.println("++++++++++++SELECTED - eps++++++++++++");
				System.out.println("pos: " + bestAction.getPosition() + "; value: " + bestAction.getValue()
						+ "; alpha: " + bestAction.getAlpha());
				System.out.println(bestAction.getPieceRepresentation());
			}

			return bestAction;
		}

		ValuableAction bestAction = actions.get(0);
		for (ValuableAction action : actions) {
			if (action.getValue() > bestAction.getValue()) {
				bestAction = action;
			}
		}
		// System.out.println("best action value: " + bestAction._value);
		if (printDebugInfo) {
			System.out.println("++++++++++++SELECTED - pol++++++++++++");
			System.out.println("pos: " + bestAction.getPosition() + "; value: " + bestAction.getValue() + "; alpha: "
					+ bestAction.getAlpha());
			System.out.println(bestAction.getPieceRepresentation());
		}

		return bestAction;
	}

	private static double dabs(double a) {
		return a >= 0 ? a : -a;
	}
}
