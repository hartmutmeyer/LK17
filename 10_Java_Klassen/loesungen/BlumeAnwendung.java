import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.Timer;

import hilfe.*;

public class BlumeAnwendung extends HJFrame {
	// globale Variablen
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 500;
	private static final Color BACKGROUND = Color.WHITE;
	private static final Color FOREGROUND = Color.BLACK;
	Blume blume1, blume2, blume3, blume4, blume5, blume6;
	Timer timer = new Timer(10, this);
	
	public BlumeAnwendung(final String title) {
		super(WIDTH, HEIGHT, BACKGROUND, FOREGROUND, title);
		// eigene Initialisierung
		timer.start();
		blume1 = new Blume(150, 570);
		blume2 = new Blume(250, 870);
		blume3 = new Blume(350, 770);
		blume4 = new Blume(500, 970);
		blume5 = new Blume(600, 870);
		blume6 = new Blume(800, 670);
	}

	@Override
	public void myPaint(Graphics g) {
		// wird aufgerufen, wenn das Fenster neu gezeichnet wird
		blume1.zeichnen(g);
		blume2.zeichnen(g);
		blume3.zeichnen(g);
		blume4.zeichnen(g);
		blume5.zeichnen(g);
		blume6.zeichnen(g);
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlumeAnwendung anwendung = new BlumeAnwendung("BlumeAnwendung");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}