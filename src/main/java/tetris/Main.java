package tetris;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sarsa.Algorithm;

public class Main {

	private static tetris.TetrisWindow _tetrisWindow;
	private static tetris.BoardGUI _board;
	private static Algorithm _algorithm;

	public static JLabel currentPieceValue;
	public static JLabel scoreValue;

	public static boolean DEBUG = false;

	public static final int TRAINING_ROUNDS = 50;
	public static final int GAMES_PER_TRAINING_ROUND = 3000;
	private static final int GAMES_PER_EVALUATION_ROUND = 250;

	public static void main(String args[]) {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	public static void createAndShowGUI() {
		new tetris.ConfigurationWindow();
	}

	public static tetris.BoardGUI getBoardGUI() {
		return _board;
	}

	public static tetris.TetrisWindow getTetrisWindow() {
		return _tetrisWindow;
	}

	public static void startTetris() {
		currentPieceValue = new JLabel("0");
		scoreValue = new JLabel("0");

		_tetrisWindow = new tetris.TetrisWindow();
		_board = new tetris.BoardGUI();
		_algorithm = new Algorithm(_board);

		_tetrisWindow.getContentPane().add(_board);

		Insets insets = _tetrisWindow.getInsets();

		JPanel stepPanel = new JPanel();
		stepPanel.setLayout(new GridLayout());
		stepPanel.setBounds(350 + insets.left, 30 + insets.top, 100, 40);
		JButton stepButton = new JButton();
		stepButton.setText("step");
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// DEBUG = true;
				_algorithm.runStep(true, false);
			}
		});
		_algorithm.paintCurrentState();

		stepButton.setSize(100, 30);
		stepPanel.add(stepButton);

		JPanel learnPanel = new JPanel();
		learnPanel.setLayout(new GridLayout());
		learnPanel.setBounds(350 + insets.left, 90 + insets.top, 100, 40);

		JButton learnButton = new JButton();
		learnButton.setText("learn");
		learnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DEBUG = false;
				playGame(50000, true);
				_algorithm.setupNewGame(true, false);
			}
		});

		learnButton.setSize(100, 30);
		learnPanel.add(learnButton);

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new GridLayout());
		chartPanel.setBounds(350 + insets.left, 150 + insets.top, 100, 40);

		JButton chartButton = new JButton();
		chartButton.setText("chart");
		chartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DEBUG = false;

				double[] scores = new double[TRAINING_ROUNDS];
				for (int i = 0; i < TRAINING_ROUNDS; i++) {
					scores[i] = playGame(GAMES_PER_EVALUATION_ROUND, false);
					playGame(GAMES_PER_TRAINING_ROUND, true);
				}
				tetris.ChartWrapper.createChart(scores);
				_algorithm.setupNewGame(true, false);
			}
		});

		chartButton.setSize(100, 30);
		chartPanel.add(chartButton);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0, 1));
		infoPanel.setBounds(550 + insets.left, 30 + insets.top, 200, 100);

		JPanel currentPiecePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel currentPieceLabel = new JLabel("Piese asezate: ");
		currentPiecePanel.add(currentPieceLabel);
		currentPiecePanel.add(currentPieceValue);
		infoPanel.add(currentPiecePanel);

		JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel scoreLabel = new JLabel("Scor: ");
		scorePanel.add(scoreLabel);
		scorePanel.add(scoreValue);
		infoPanel.add(scorePanel);

		_tetrisWindow.add(stepPanel);
		_tetrisWindow.add(learnPanel);
		_tetrisWindow.add(chartPanel);
		_tetrisWindow.add(infoPanel);
		_tetrisWindow.setVisible(true);
	}

	private static double playGame(int noGames, boolean allowExploration) {
		double min = 0, max = 0;
		double avgScore = 0;
		for (int i = 0; i < noGames; i++) {
			int score = _algorithm.playGame(allowExploration);
			avgScore += score;
		}
		avgScore /= (double) noGames;
		min = Math.min(min, avgScore);
		max = Math.max(max, avgScore);

		if (min < -19 || max > 49) {
			System.out.println(min + " " + max);
		}
		return avgScore;
	}
}
