package sarsa.moulder;

import java.util.List;
import java.util.Random;

import sarsa.BoardContour;
import tetris.ConfigurationWindow;

public class Moulder {
	private static Random _rand = new Random();

	private static int knownAarray[] = { 0, 1, 2, 2 };
	private static int index = -1;

	public static int piecesAllowedForBonus[] = { 0, 4, 5, 6 };

	private static double EPS = 0.2;
	private static double GAMA = .95;

	private static MoulderStatesRetainer _statesRetainer = MoulderStatesRetainer.getInstance();

	public static int getNextBrickType(BoardContour contour) {
		if (ConfigurationWindow.selectedMoulder == 0) {
			return randomPiece();
		}

		if (ConfigurationWindow.selectedMoulder == 1) {
			return homeworkConfiguration1();
		}

		if (ConfigurationWindow.selectedMoulder == 2) {
			return homeworkConfiguration2();
		}

		if (ConfigurationWindow.selectedMoulder == 3) {
			return homeworkConfiguration3();
		}

		if (ConfigurationWindow.selectedMoulder == 4) {
			return knownSequence();
		}

		return bonus(contour).getPieceType();
	}

	private static MoulderValuableAction bonus(BoardContour contour) {
		double eps = _rand.nextDouble();
		if (eps <= EPS) {
			int pieceType = piecesAllowedForBonus[_rand.nextInt(piecesAllowedForBonus.length)];
			return _statesRetainer.getSpecificAction(contour, pieceType);
		}

		List<MoulderValuableAction> actions = _statesRetainer.getActionsForContour(contour);
		MoulderValuableAction selectedAction = actions.get(0);

		for (MoulderValuableAction action : actions) {
			if (action.getValue() > selectedAction.getValue()) {
				selectedAction = action;
			}
		}

		return selectedAction;
	}

	public static void updateActionWithReward(BoardContour contour, int pieceType, int reward,
			BoardContour newBoardContour) {
		if (ConfigurationWindow.selectedMoulder != 5) {
			return;
		}
		MoulderValuableAction currentAction = _statesRetainer.getSpecificAction(contour, pieceType);
		if (newBoardContour == null) {
			// the game ended;
			currentAction.setValue(reward);
			return;
		}

		MoulderValuableAction nextAction = bonus(newBoardContour);

		int value = currentAction.getValue();
		value += currentAction.getAlpha() * (reward + GAMA * nextAction.getValue() - value);

		currentAction.setValue(value);
		currentAction.updateAlpha();
	}

	private static int randomPiece() {
		return _rand.nextInt(6);
	}

	private static int homeworkConfiguration1() {
		return 0;
	}

	private static int homeworkConfiguration2() {
		double eps = _rand.nextDouble();
		if (eps <= 0.2d) {
			return 0;
		}
		if (eps <= 0.6d) {
			return 2;
		}
		return 3;
	}

	private static int homeworkConfiguration3() {
		double eps = _rand.nextDouble();
		if (eps <= 0.16667d) {
			return 0;
		}
		eps -= 0.16667;

		if (eps <= 0.66665d) {
			return 5;
		}
		eps -= 0.66665;

		if (eps <= 0.08334d) {
			return 4;
		}

		return 6;
	}

	private static int knownSequence() {
		index++;
		if (index >= knownAarray.length) {
			index = 0;
		}

		return knownAarray[index];
	}

	public static void resetIndex() {
		index = -1;
	}

}
