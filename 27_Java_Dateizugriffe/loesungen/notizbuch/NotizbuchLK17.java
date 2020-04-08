import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ListSelectionModel;

public class NotizbuchLK17 extends JFrame {

	private JPanel contentPane;
	private JTextField tfNeueZeile;
	private DefaultListModel<String> notizen = new DefaultListModel<String>();
	private JList<String> listNotizen = new JList<String>(notizen);
	private URL url = getClass().getResource("notizen.txt");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					NotizbuchLK17 frame = new NotizbuchLK17();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NotizbuchLK17() {
		erzeugeGUI();
		notizenAnzeigen();
	}

	public void erzeugeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNotizbuch = new JLabel("Notizbuch");
		lblNotizbuch.setHorizontalAlignment(SwingConstants.CENTER);
		lblNotizbuch.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNotizbuch.setBounds(10, 11, 572, 28);
		contentPane.add(lblNotizbuch);

		JLabel lblInhalt = new JLabel("Inhalt:");
		lblInhalt.setBounds(10, 50, 46, 14);
		contentPane.add(lblInhalt);

		JScrollPane scrollPaneNotizen = new JScrollPane();
		scrollPaneNotizen.setBounds(10, 75, 572, 225);
		contentPane.add(scrollPaneNotizen);

		listNotizen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneNotizen.setViewportView(listNotizen);

		JButton btnMarkierteZeileLschen = new JButton("markierte Zeile löschen");
		btnMarkierteZeileLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eintragLoeschen();
			}
		});
		btnMarkierteZeileLschen.setBounds(232, 311, 170, 23);
		contentPane.add(btnMarkierteZeileLschen);

		JButton btnNotizbuchLschen = new JButton("Notizbuch löschen");
		btnNotizbuchLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notizbuchLoeschen();
			}
		});
		btnNotizbuchLschen.setBounds(412, 311, 170, 23);
		contentPane.add(btnNotizbuchLschen);

		JLabel lblHinzufgen = new JLabel("Hinzufügen:");
		lblHinzufgen.setBounds(10, 348, 80, 14);
		contentPane.add(lblHinzufgen);

		tfNeueZeile = new JTextField();
		tfNeueZeile.setBounds(100, 345, 381, 20);
		contentPane.add(tfNeueZeile);
		tfNeueZeile.setColumns(10);

		JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eintragSpeichern();
			}
		});
		btnSpeichern.setBounds(491, 344, 91, 23);
		contentPane.add(btnSpeichern);
	}

	private void notizenAnzeigen() {
		notizen.clear();
		if (url == null) {
			JOptionPane.showMessageDialog(this, "Die Datei notizen.txt existiert nicht");
			return;
		}
		try (InputStream fileIn = new FileInputStream(url.getFile());
				InputStreamReader in = new InputStreamReader(fileIn, "UTF-8")) {
			int zeichen;
			char c;
			String notiz = "";
			while ((zeichen = in.read()) != -1) {
				c = (char) zeichen;
				if (c != '$') {
					notiz += c;
				} else {
					notizen.addElement(notiz);
					notiz = "";
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void eintragSpeichern() {
		if (url == null) {
			JOptionPane.showMessageDialog(this, "Die Datei notizen.txt existiert nicht");
			return;
		}
		try (OutputStream fileOut = new FileOutputStream(url.getFile(), true);
				OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8")) {
			out.write(tfNeueZeile.getText() + "$");
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		notizenAnzeigen();
	}

	private void notizbuchLoeschen() {
		if (url == null) {
			JOptionPane.showMessageDialog(this, "Die Datei notizen.txt existiert nicht");
			return;
		}
		try (OutputStream fileOut = new FileOutputStream(url.getFile());
				OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8")) {
			// nichts tun! Datei soll direkt wieder geschlossen werden!
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		notizenAnzeigen();
	}

	private void eintragLoeschen() {
		int index = listNotizen.getSelectedIndex();
		if (index == -1) {
			JOptionPane.showMessageDialog(this, "Kein Eintrag zum Löschen ausgewählt!");
			return;
		}
		notizen.remove(index);
		if (url == null) {
			JOptionPane.showMessageDialog(this, "Die Datei notizen.txt existiert nicht");
			return;
		}
		try (OutputStream fileOut = new FileOutputStream(url.getFile());
				OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8")) {
			int anzahlNotizen = notizen.getSize();
			for (int i = 0; i < anzahlNotizen; i++) {
				out.write(notizen.get(i) + "$");
			}			
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// Jetzt müssen die geänderten Daten aus der JList Komponente in die Datei geschrieben werden!
		notizenAnzeigen();
	}

}
