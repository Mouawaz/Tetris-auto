package tetris;

import java.awt.Container;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TetrisWindow {

	private JFrame _frame;

	public TetrisWindow() {
		_frame = new JFrame("Tetris window");

		Container pane = _frame.getContentPane();
		pane.setLayout(null);

		_frame.setSize(800, 600);
		_frame.setLocationRelativeTo(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setVisible(boolean visible) {
		_frame.setVisible(visible);
	}

	public Container getContentPane() {
		return _frame.getContentPane();
	}

	public Insets getInsets() {
		return _frame.getInsets();
	}

	public void add(JPanel p) {
		_frame.add(p);
		_frame.repaint();
	}

}
