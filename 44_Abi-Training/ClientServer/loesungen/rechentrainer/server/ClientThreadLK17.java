
// Thread für eine Client-Verbindung
import java.net.*;
import java.util.Random;
import java.io.*;

public class ClientThreadLK17 extends Thread {
	private Socket socket;
	private InputStreamReader inNet;
	private OutputStreamWriter outNet;
	private String name = "";
	private int zeichen;
	private char c;
	private int zahl1 = 5;
	private int zahl2 = 45;
	private int ergebnis;
	private Random zufall;

	public ClientThreadLK17(Socket sock) {
		this.socket = sock;
		try {
			InputStream is = socket.getInputStream();
			inNet = new InputStreamReader(is, "UTF-8");
			outNet = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		zufall = new Random();
	}

	@Override
	public void run() {
		try {
			name = nameLesen();
			aufgabenSenden();
			ergebnisAuswerten();

			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Die Verbindung (InputStream des Servers bzw. OutputStream des
		// Clients wurde vom Client getrennt --> in.read() == -1
		// und damit die while-Schleife oben verlassen.
		// Jetzt muss nur noch aufgeräumt werden (der Socket - und indirekt
		// damit auch die Streams - werden geschlossen).
	}

	private String nameLesen() throws IOException {
		name = "";
		while ((zeichen = inNet.read()) != '$') {
			name += (char) zeichen;
		}
		System.out.println("nameLesen(): " + name);
		return name;
	}

	private void aufgabenSenden() throws IOException {
		for (int i = 0; i < 5; i++) {
			zahl1 = zufall.nextInt(8) + 2;
			zahl2 = zufall.nextInt(90) + 10;
			ergebnis = zahl1 * zahl2;
			String aufgabe = "?" + zahl1 + " * " + zahl2 + "$";
			outNet.write(aufgabe);
			outNet.flush();
			String loesungString = "";
			int loesungClient = 0;
			while (loesungClient != ergebnis) {
				loesungString = "";
				while ((zeichen = inNet.read()) != '$') {
					loesungString += (char) zeichen;
				}
				loesungClient = Integer.parseInt(loesungString);
				if (loesungClient == ergebnis) {
					System.out.println("RICHTIG");
				} else {
					System.out.println("FALSCH");
					outNet.write("%Falsche Antwort. Probier es noch einmal.$");
					outNet.flush();
				}
			}
		}
	}

	private void ergebnisAuswerten() {
		// TODO Auto-generated method stub

	}
}
