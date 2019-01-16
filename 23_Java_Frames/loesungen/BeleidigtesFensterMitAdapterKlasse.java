import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BeleidigtesFensterMitAdapterKlasse extends JFrame {
	// globale Variablen
	static int width = 500;
	static int height = 500;
	private static final Color BACKGROUND = Color.YELLOW;
	private static final Color FOREGROUND = Color.BLACK;
	JLabel zeichenflaeche;
	JPanel contentPane;
	boolean beleidigt;

	public BeleidigtesFensterMitAdapterKlasse(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
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
		setResizable(true);
		setVisible(true);
		addWindowListener(new FensterEventHandler());
	}

	public void myPaint(Graphics g) {
		// wird aufgerufen, wenn das Fenster neu gezeichnet wird
		String nachricht="";
		if (beleidigt == false) {
			nachricht = "Wehe du gehst weg.";
		} else {
			nachricht = "Verräter, wieso gehst du einfach weg?";
		}
		g.drawString(nachricht, 10, 20);
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new BeleidigtesFensterMitAdapterKlasse("Beleidigtes Fenster");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

class FensterEventHandler extends WindowAdapter {
	@Override
	public void windowClosing(final WindowEvent event) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				event.getWindow().setVisible(false);
				event.getWindow().dispose();
				System.exit(0);
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent e) {
		((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).zeichenflaeche.setBackground(Color.YELLOW);
		((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).beleidigt = false;
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).zeichenflaeche.setBackground(Color.RED);
		((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).beleidigt = true;
		BeleidigtesFensterMitAdapterKlasse.width -= 100;
		BeleidigtesFensterMitAdapterKlasse.height -= 100;
		if (BeleidigtesFensterMitAdapterKlasse.width < 100) {
			((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).setVisible(false);
			((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).dispose();
			System.exit(0);
		} else {
			((BeleidigtesFensterMitAdapterKlasse) e.getWindow()).setSize(new Dimension(BeleidigtesFensterMitAdapterKlasse.width, BeleidigtesFensterMitAdapterKlasse.height));
		}
	}
}
