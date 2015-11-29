package mazegenerator;

//created by Derya Sismanyazici for CMP3005 Analysis of Algorithms Programming Project in 2014

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MazeGenerator {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Maze Generator");
		frame.setSize(400, 470);

		MazePanel panel = new MazePanel();

		JTextField textfield = new JTextField(2);
		textfield.setText("20");

		ButtonHandler handler = new ButtonHandler(panel, textfield);

		JButton generate = new JButton("Generate");
		JButton restart = new JButton("Restart");
		JButton solve = new JButton("Solve");

		generate.addActionListener(handler);
		restart.addActionListener(handler);
		solve.addActionListener(handler);

		JPanel top_panel = new JPanel();
		top_panel.add(new JLabel("N: "));
		top_panel.add(textfield);

		top_panel.add(generate);
		top_panel.add(restart);
		top_panel.add(solve);

		frame.add(top_panel, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class ButtonHandler implements ActionListener {
	MazePanel panel;
	JTextField textfield;

	public ButtonHandler(MazePanel mp, JTextField tf) {
		panel = mp;
		textfield = tf;
	}

	public void actionPerformed(final ActionEvent e) {
		panel.requestFocus(true);
		new Thread() {
			public void run() {
				JButton button = (JButton) e.getSource();
				if (button.getText().equals("Generate")) {
					MazeCell.width = 1.0 / Integer
							.parseInt(textfield.getText());
					MazeCell.height = 1.0 / Integer.parseInt(textfield
							.getText());
					panel.generateMaze();
				} else if (button.getText().equals("Restart")) {
					panel.restart();
				} else if (button.getText().equals("Solve")) {
					panel.solve();
				}
			}
		}.start();
	}

}