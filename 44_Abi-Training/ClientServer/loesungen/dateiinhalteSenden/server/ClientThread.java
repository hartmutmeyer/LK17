import java.net.*;
import java.io.*;

public class ClientThread extends Thread {

	private Socket socket;
	private static final int NORMALER_MODUS = 0;
	private static final int GEHEIM_MODUS = 1;
	private int zustand = NORMALER_MODUS;
	private static Object monitor = new Object();
	private InputStreamReader inNet;
	private OutputStreamWriter outNet;

	public ClientThread(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		int x;
		try {
			inNet = new InputStreamReader(socket.getInputStream(), "UTF-8");
			outNet = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

			while ((x = inNet.read()) != -1) {
				char c = (char) x;
				if (c >= 'a' && c <= 'z') {
					dateiSenden(c);
				} else {
					switch (c) {
					case '$':
						passwortLesen();
						break;
					case '%':
						zustand = NORMALER_MODUS;
						outNet.write("Geheimmodus ausgeschaltet" + System.lineSeparator());
						outNet.flush();
						break;
					case '#':
						passwortAendern();
						break;
					default:
						outNet.write("Falsche Eingabe" + System.lineSeparator());
						outNet.flush();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("ClientThread: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void dateiSenden(char buchstabe) throws IOException {
		int zeichen;
		String dateiname = buchstabe + ".txt";
		URL url = getClass().getResource(dateiname);
		if (url == null) {
			outNet.write("Die Datei existiert nicht." + System.lineSeparator());
			outNet.flush();
			return;
		}
		System.out.println("Datei senden: " + dateiname);
		try (InputStreamReader inFile = new InputStreamReader(new FileInputStream(url.getFile()), "UTF-8")) {
			switch (zustand) {
			case NORMALER_MODUS:
				// Der Inhalt der Datei wird zeichenweise an den Client gesendet.
				while ((zeichen = inFile.read()) != -1) {
					outNet.write(zeichen);
				}
				break;
			case GEHEIM_MODUS:
				// In text wird der Inhalt der Datei zunächst Wort für Wort eingesammelt.
				String text = "";
				while ((zeichen = inFile.read()) != -1) {
					char c = (char) zeichen;
					// Wenn das nächste Zeichen ein Buchstabe ist, dann hänge es an text an ...
					if ((c >= 'a' && c <= 'z') || c >= 'A' && c <= 'Z') {
						text += c;
					} else { // ... ansonsten ist das Wort beendet. Wenn das eingesammelte Wort aus mindestens
						     // zwei Zeichen besteht, dann nimm den zweiten Buchstaben des Wortes und 
						     // schicke diesen an den Client.
						if (text.length() > 1) {
							outNet.write(text.charAt(1));
						}
						// text wird wieder geleert, um das nächste Wort einsammeln zu können.
						text = "";
					}
				}
			}
			outNet.write(System.lineSeparator());
			outNet.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void passwortLesen() throws IOException {
		String userPassword = "";
		int zeichen;
		while ((char) (zeichen = inNet.read()) != '$') {
			userPassword += (char) zeichen;
		}
		String storedPassword = "";
		synchronized (monitor) {
			URL url = getClass().getResource("passwort.txt");
			try (InputStreamReader inFile = new InputStreamReader(new FileInputStream(url.getFile()), "UTF-8")) {
				while ((zeichen = inFile.read()) != -1) {
					storedPassword += (char) zeichen;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (storedPassword.equals(userPassword)) {
			zustand = GEHEIM_MODUS;
			outNet.write("Geheimmodus aktiviert" + System.lineSeparator());
		} else {
			outNet.write("Falsches Passwort" + System.lineSeparator());
		}
		outNet.flush();
	}

	private void passwortAendern() throws IOException {
		String newPassword = "";
		int zeichen;
		while ((char) (zeichen = inNet.read()) != '#') {
			newPassword += (char) zeichen;
		}
		if (zustand == GEHEIM_MODUS) {
			synchronized (monitor) {
				URL url = getClass().getResource("passwort.txt");
				try (OutputStreamWriter outFile = new OutputStreamWriter(new FileOutputStream(url.getFile()), "UTF-8")) {
					outFile.write(newPassword);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			outNet.write("Password geändert" + System.lineSeparator());
		} else {
			outNet.write("Geheimmodus ist nicht aktiviert" + System.lineSeparator());
		}
		outNet.flush();
	}
}
