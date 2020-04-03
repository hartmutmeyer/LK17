import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class RechentrainerClientLeseThreadLK17 extends Thread {
	private RechentrainerClientLK17 clientFrame;
	private InputStreamReader in;

	public RechentrainerClientLeseThreadLK17(RechentrainerClientLK17 clientFrame, InputStreamReader in) {
		this.clientFrame = clientFrame;
		this.in = in;
	}
	
	@Override
	public void run() {
		System.out.println("run(): ENTRY");
		int zeichen; 
		char c;
		try {
			while ((zeichen = in.read()) != -1) {
				c = (char) zeichen;
				switch (c) {
				case '?':
					aufgabeLesen();
					break;
				case '%':
					meldungAusgeben();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientFrame.btnStarten.setEnabled(true);
		clientFrame.btnLoesungSenden.setEnabled(false);
	}

	private void aufgabeLesen() throws IOException {
		System.out.println("aufgabeLesen(): ENTRY");
		int zeichen; 
		char c;
		String aufgabe = "";
		while ((zeichen = in.read()) != '$') {
			c = (char) zeichen;
			aufgabe += c;
		}	
		System.out.println("aufgabeLesen(): " + aufgabe);
		clientFrame.tfAufgabe.setText(aufgabe);
		clientFrame.tfLoesung.setText("");
	}

	private void meldungAusgeben() throws IOException {
		System.out.println("meldungAusgeben(): ENTRY");
		int zeichen; 
		char c;
		String meldung = "";
		while ((zeichen = in.read()) != '$') {
			c = (char) zeichen;
			meldung += c;
		}	
		System.out.println("meldungAusgeben(): " + meldung);
		JOptionPane.showMessageDialog(clientFrame, meldung);
	}

}
