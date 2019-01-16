import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BeleidigtesFensterMitInterface extends JFrame implements WindowListener {
	// globale Variablen
	static int width = 500;
	static int height = 500;
	private static final Color BACKGROUND = Color.YELLOW;
	private static final Color FOREGROUND = Color.BLACK;
	private JLabel zeichenflaeche;
	private boolean beleidigt;

	public BeleidigtesFensterMitInterface(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		zeichenflaeche = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				myPaint(g);
			}
		};
		zeichenflaeche.setPreferredSize(new Dimension(width, height));
		zeichenflaeche.setOpaque(true);
		zeichenflaeche.setBackground(BACKGROUND);
		zeichenflaeche.setForeground(FOREGROUND);
		zeichenflaeche.setFont(new Font("Arial", Font.PLAIN, 12));
		contentPane.add(zeichenflaeche);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		addWindowListener(this);
	}
	
	public void myPaint(Graphics g) {
		// wird aufgerufen, wenn das Fenster neu gezeichnet wird
		if (beleidigt == false) {
			g.drawString("Wehe du gehst weg.", 10, 20);
		} else {
			g.drawString("Verräter, wieso gehst du einfach weg?", 10, 20);
		}
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new BeleidigtesFensterMitInterface("Beleidigtes Fenster");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		zeichenflaeche.setBackground(Color.YELLOW);
		beleidigt = false;
	}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		zeichenflaeche.setBackground(Color.RED);
		beleidigt = true;
		width -= 100;
		height -= 100;
		if (width < 100) {
			setVisible(false);
			dispose();
			System.exit(0);
		} else {
			setSize(new Dimension(width, height));
		}
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
