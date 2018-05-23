import java.awt.*;

public class Blume {
	private int x = 0;
	private int y = 0;
	private static int anzahlBlumen;
	private Color farbeblatt = Color.RED;
	private Color farbehonig = Color.YELLOW;
	private Color farbestange = Color.GREEN;
	private int zeitZumVerbluehen = 200;

	public Blume(int xpos, int ypos) {
		x = xpos;
		y = ypos;
		switch (anzahlBlumen++ % 3) {
		case 0:
			farbeblatt = Color.RED;
			break;
		case 1:
			farbeblatt = Color.BLUE;
			break;
		case 2:
			farbeblatt = Color.CYAN;
		}
	}

	public void zeichnen(Graphics g) {
		if (y >= 100) {
			g.setColor(farbestange);
			g.fillRect(x + 29, y + 60, 3, 1000);
			g.setColor(farbehonig);
			g.fillOval(x + 20, y + 20, 20, 20);
			g.setColor(farbeblatt);
			g.fillOval(x + 20, y, 20, 20);
			g.fillOval(x, y + 20, 20, 20);
			g.fillOval(x + 40, y + 20, 20, 20);
			g.fillOval(x + 20, y + 40, 20, 20);
			y--;
		} else {
			if (zeitZumVerbluehen > 0) {
				g.setColor(farbestange);
				g.fillRect(x + 29, y + 40, 3, 1000);
				g.setColor(farbehonig);
				g.fillOval(x + 20, y + 20, 20, 20);
				g.setColor(farbeblatt);
				g.fillOval(x + 20, y, 20, 20);
				g.fillOval(x, y + 20, 20, 20);
				g.fillOval(x + 40, y + 20, 20, 20);
				g.fillOval(x + 20, y + 40, 20, 20);
				zeitZumVerbluehen--;
			} else {
				g.setColor(farbestange);
				g.fillRect(x + 29, y + 40, 3, 1000);
				g.setColor(farbehonig);
				g.fillOval(x + 20, y + 20, 20, 20);
			}
		}
	}
}
