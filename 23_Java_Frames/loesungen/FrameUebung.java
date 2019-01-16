package java_23_Frames.loesungen;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrameUebung extends JFrame {
	// globale Variablen
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	private static final Color BACKGROUND = Color.YELLOW;
	JPanel contentPane;

	public FrameUebung(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setBackground(BACKGROUND);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		addWindowListener(new FensterSchliesser());
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameUebung anwendung = new FrameUebung("Schön, dass du da bist!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

class FensterSchliesser extends WindowAdapter {
	@Override
	public void windowClosing(final WindowEvent event) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int erg = JOptionPane.showConfirmDialog(null,
						"Möchtest du die Anwendung wirklich schließen?");
				if (erg == JOptionPane.YES_OPTION) {
					event.getWindow().setVisible(false);
					event.getWindow().dispose();
					System.exit(0);
				}
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent e) {
		((FrameUebung) e.getWindow()).setTitle("Schön, dass du wieder da bist!");
		((FrameUebung) e.getWindow()).contentPane.setBackground(Color.YELLOW);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		((FrameUebung) e.getWindow()).setTitle("Warum hast du mich verlassen?");
		((FrameUebung) e.getWindow()).contentPane.setBackground(Color.BLUE);
	}
}
