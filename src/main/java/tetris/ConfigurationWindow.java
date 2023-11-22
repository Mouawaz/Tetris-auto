package tetris;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ConfigurationWindow implements ActionListener {

	private JFrame _frame;
	private JTextField _boardHeight;
	private JTextField _boardWidth;
	private JCheckBox _evaluateStateWhenCreatedCheckBox;

	public static boolean evaluateStatesWhenCreated = false;
	public static int selectedMoulder = 0;

	public ConfigurationWindow() {
		_frame = new JFrame("Configurations");
		_frame.setSize(800, 600);
		_frame.setLocationRelativeTo(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		init();

		_frame.setVisible(true);
	}

	private void init() {
		Container pane = _frame.getContentPane();
		pane.setLayout(new GridBagLayout());

		_boardWidth = addTextField(pane, "Board Width: ", "6", 4);
		_boardHeight = addTextField(pane, "Board Height: ", "10", 4);
		addBricoStoreOptions(pane);
		_evaluateStateWhenCreatedCheckBox = new JCheckBox("Evaluare stare la creare");
		pane.add(_evaluateStateWhenCreatedCheckBox);

		JButton startButton = new JButton("Start");
		pane.add(startButton);

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				_frame.setVisible(false);
				short boardWidth = Short.parseShort(_boardWidth.getText());
				short boardHeight = Short.parseShort(_boardHeight.getText());

				evaluateStatesWhenCreated = _evaluateStateWhenCreatedCheckBox.isSelected();

				BoardGUI.setBoardLimits(boardWidth, boardHeight);

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Main.startTetris();
					}
				});
			}
		});
	}

	private void addBricoStoreOptions(Container pane) {
		JLabel label = new JLabel("Optiune caramidar: ");

		JRadioButton randomButton = new JRadioButton("random");
		randomButton.setActionCommand("0");
		randomButton.setSelected(true);

		JRadioButton config1Button = new JRadioButton("Configuratie tema 1");
		config1Button.setActionCommand("1");

		JRadioButton config2Button = new JRadioButton("Configuratie tema 2");
		config2Button.setActionCommand("2");

		JRadioButton config3Button = new JRadioButton("Configuratie tema 3");
		config3Button.setActionCommand("3");

		JRadioButton knownButton = new JRadioButton("Configuratie prestabilita");
		knownButton.setActionCommand("4");

		JRadioButton bonusButton = new JRadioButton("Bonus");
		bonusButton.setActionCommand("5");

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(randomButton);
		group.add(config1Button);
		group.add(config2Button);
		group.add(config3Button);
		group.add(knownButton);
		group.add(bonusButton);

		// Register a listener for the radio buttons.
		randomButton.addActionListener(this);
		config1Button.addActionListener(this);
		config2Button.addActionListener(this);
		config3Button.addActionListener(this);
		knownButton.addActionListener(this);
		bonusButton.addActionListener(this);

		// Put the radio buttons in a column in a panel.
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

		radioPanel.add(label);
		radioPanel.add(randomButton);
		radioPanel.add(config1Button);
		radioPanel.add(config2Button);
		radioPanel.add(config3Button);
		radioPanel.add(knownButton);
		radioPanel.add(bonusButton);

		pane.add(radioPanel);
	}

	private JTextField addTextField(Container pane, String labelText, String defaultValue, int textFieldWidth) {
		JLabel label = new JLabel(labelText);
		JTextField textField = new JTextField(defaultValue, textFieldWidth);

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(label);
		panel.add(textField);
		panel.setSize(100, 500);

		pane.add(panel);
		return textField;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		selectedMoulder = Integer.parseInt(arg0.getActionCommand());
	}
}
