import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class LeseThreadLK17 extends Thread {

	private InputStreamReader in;
	private BuchladenClientLK17 main;
	private int zeichen;
	private char c;

	public LeseThreadLK17(BuchladenClientLK17 main, InputStreamReader in) {
		this.main = main;
		this.in = in;
	}

	@Override
	public void run() {
		System.out.println("run(): ENTRY");
		try {
			while ((zeichen = in.read()) != -1) {
				c = (char) zeichen;
				System.out.println("run(): " + c);
//				main.tfKundenNr.setText("TEST");
				switch (c) {
				case 'L':
					buecherlisteLesen();
					break;
				case 'J':
					JOptionPane.showMessageDialog(main, "Ihre Bücher werden in Kürze versandt.");
					break;
				case 'N':
					stueckzahlZuGross();
					break;
				case 'S':
					JOptionPane.showMessageDialog(main, "Stornierung erfolgreich durchgeführt.");
					break;
				case 'F':
					JOptionPane.showMessageDialog(main, "Es ist keine Bestellung des markierten Buches vorhanden.");
					break;
				default:
					JOptionPane.showMessageDialog(main, "Protokollfehler: '" + c + "' empfangen.");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stueckzahlZuGross() {
		String stueckzahl = "";
		try {
			while ((zeichen = in.read()) != '$') {
				c = (char) zeichen;
				stueckzahl += c;
			}
			JOptionPane.showMessageDialog(main, "Ihre Bestellung kann leider nicht durchgeführt werden." 
			+ System.lineSeparator() + "Es sind nur noch " + stueckzahl + " Bücher vorhanden.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buecherlisteLesen() {
		String eintrag = "";
		try {
			while ((zeichen = in.read()) != '$') {
				c = (char) zeichen;
				eintrag += c;
				if (c == '§') {
					final String senden = eintrag.substring(0, eintrag.length() - 1);
					eintrag = "";
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							main.buecher.addElement(senden);
						}
					});
				}
			}
			final String senden = eintrag;
			eintrag = "";
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					main.buecher.addElement(senden);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
