import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import hilfe.*;

public class Dartspiel extends HJFrame implements KeyListener {
	// globale Variablen
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private static final Color BACKGROUND = Color.WHITE;
	private static final Color FOREGROUND = Color.BLACK;
	private Pfeil pfeil;

	public Dartspiel(final String title) {
		super(WIDTH, HEIGHT, BACKGROUND, FOREGROUND, title);
		addKeyListener (this);
		pfeil = new Pfeil (100,100); 
		Timer timer = new Timer (20, this);
		timer.start();	
	}
	
	private void dartscheibe(Graphics g) {
		g.drawOval (400,200,30,30);
		g.drawOval (385,185,60,60);
		g.drawOval(370, 170, 90, 90);
		g.drawOval(355, 155, 120,120);
	}

	@Override
	public void myPaint(Graphics g) {
		dartscheibe(g);
		pfeil.zeichnen(g);
		pfeil.bewegen();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_N:
			pfeil.fallenLassen();
			break;
		case KeyEvent.VK_S:
			pfeil.schiessen();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dartspiel anwendung = new Dartspiel("Dart-Spiel");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}