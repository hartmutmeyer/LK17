import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.ListSelectionModel;

public class BuchladenClientLK17 extends JFrame {

	private JPanel contentPane;
	private JTextField tfServer;
	JTextField tfKundenNr;
	private JTextField tfStueckzahl;
	private Socket socket;
	private OutputStreamWriter out;
	private JButton btnStornieren;
	private JButton btnBestellen;
	private JButton btnVerbinden;
	DefaultListModel<String> buecher = new DefaultListModel<String>();
	private JList<String> listBuecher = new JList<String>(buecher);

	public BuchladenClientLK17() {
		super("Buchladen Client");
		createGUI();
	}

	private void createGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 524, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServer = new JLabel("Server:");
		lblServer.setBounds(12, 12, 55, 15);
		contentPane.add(lblServer);

		tfServer = new JTextField();
		tfServer.setBounds(85, 10, 114, 19);
		contentPane.add(tfServer);
		tfServer.setColumns(10);

		btnVerbinden = new JButton("verbinden");
		btnVerbinden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verbinden();
			}
		});
		btnVerbinden.setBounds(211, 7, 98, 25);
		contentPane.add(btnVerbinden);

		JLabel lblKundennr = new JLabel("KundenNr:");
		lblKundennr.setBounds(12, 39, 70, 15);
		contentPane.add(lblKundennr);

		tfKundenNr = new JTextField();
		tfKundenNr.setBounds(85, 37, 114, 19);
		contentPane.add(tfKundenNr);
		tfKundenNr.setColumns(10);

		JLabel lblBuecherliste = new JLabel("Bücherliste:");
		lblBuecherliste.setBounds(12, 85, 90, 15);
		contentPane.add(lblBuecherliste);

		JLabel lblStueckzahl = new JLabel("Stückzahl:");
		lblStueckzahl.setBounds(12, 353, 70, 15);
		contentPane.add(lblStueckzahl);

		tfStueckzahl = new JTextField();
		tfStueckzahl.setBounds(85, 351, 114, 19);
		contentPane.add(tfStueckzahl);
		tfStueckzahl.setColumns(10);

		btnBestellen = new JButton("markiertes Buch bestellen");
		btnBestellen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bestellen();
			}
		});
		btnBestellen.setEnabled(false);
		btnBestellen.setBounds(211, 348, 292, 25);
		contentPane.add(btnBestellen);

		btnStornieren = new JButton("Bestellung des markierten Buches stornieren");
		btnStornieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stornieren();
			}
		});
		btnStornieren.setEnabled(false);
		btnStornieren.setBounds(211, 385, 292, 25);
		contentPane.add(btnStornieren);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 113, 486, 224);
		contentPane.add(scrollPane);

		listBuecher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listBuecher);
	}

	private void verbinden() {
		try {
			String servername = tfServer.getText();
			socket = new Socket(servername, 44444);
			LeseThreadLK17 thread = new LeseThreadLK17(this, new InputStreamReader(socket.getInputStream(), "UTF-8"));
			thread.start();
			out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			btnVerbinden.setEnabled(false);
			btnBestellen.setEnabled(true);
			btnStornieren.setEnabled(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private void bestellen() {
		// prüfen ob Kundennummer und Stückzahl angegeben sind
		if (tfStueckzahl.getText().isEmpty() || tfKundenNr.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Stückzahl und Kundennummer müssen angegeben werden!");
			return;
		}
		try {
			if (Integer.parseInt(tfStueckzahl.getText()) < 0) {
				JOptionPane.showMessageDialog(this, "Stückzahl muss positive ganze Zahl sein!");
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Stückzahl muss positive ganze Zahl sein!");
			return;
		}
		if (listBuecher.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(this, "Es muss ein Buch aus der Liste ausgewählt sein!");
			return;
		}
		String eintrag = listBuecher.getSelectedValue();
		String isbn = eintrag.substring(0, eintrag.indexOf(' '));
		System.out.println("bestellen(): Gefundene ISBN: \"" + isbn + "\"");
		String nachrichtAnServer = "B" + tfKundenNr.getText() + "$" + isbn + "§" + tfStueckzahl.getText() + "%";
		try {
			out.write(nachrichtAnServer);
			out.flush();
			System.out.println("bestellen(): " + nachrichtAnServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stornieren() {
		if (tfKundenNr.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Kundennummer fehlt!");
			return;
		}
		if (listBuecher.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(this, "Es muss ein Buch aus der Liste ausgewählt sein!");
			return;
		}
		String kundennummer = tfKundenNr.getText();
		String isbn = listBuecher.getSelectedValue();
		int indexOfSpace = isbn.indexOf(' ');
		String stornierung = "S" + kundennummer + "$" + isbn.substring(0, indexOfSpace) + "§";
		try {
			System.out.println("stornierung(): " + stornierung);
			out.write(stornierung);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BuchladenClientLK17 frame = new BuchladenClientLK17();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
