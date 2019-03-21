import java.awt.*;
import java.awt.event.*;

public class OldtimerTimer extends Thread {
	Oldtimer app;
	public boolean anhalten = false;

	public OldtimerTimer(Oldtimer app) {
		this.app = app;
	}

	public void run() {
		while (!anhalten) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// nichts
			}
			app.repaint();
		}
	}
}
