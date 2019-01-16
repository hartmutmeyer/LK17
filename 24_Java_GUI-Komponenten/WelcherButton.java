import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WelcherButton extends JFrame implements ActionListener {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 120;
	JButton eins, zwei, drei;
	JLabel text, text2;

	public WelcherButton(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new FlowLayout());
		setContentPane(contentPane);
		setSize(WIDTH, HEIGHT);
		eins = new JButton("1");
		contentPane.add(eins);
		eins.addActionListener(this);
		zwei = new JButton("2");
		contentPane.add(zwei);
		zwei.addActionListener(this);
		drei = new JButton("3");
		contentPane.add(drei);
		drei.addActionListener(this);
		text = new JLabel("Es wurde Button _ gedrückt.");
		contentPane.add(text);
		text2 = new JLabel("Es wurde Button _ gedrückt.");
		contentPane.add(text2);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();

		// 1. Alternative: lange Variante
		if (button == eins) {
			text.setText("Es wurde Button 1 gedrückt.");
		}
		if (button == zwei) {
			text.setText("Es wurde Button 2 gedrückt.");
		}
		if (button == drei) {
			text.setText("Es wurde Button 3 gedrückt.");
		}

		// 2. Alternative: kurze Variante
		text2.setText("Es wurde Button " + button.getText() + " gedrückt.");
	}

	public static void main(final String[] args) {
		new WelcherButton("WelcherButton");
	}
}
