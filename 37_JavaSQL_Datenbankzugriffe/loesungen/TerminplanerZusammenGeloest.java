import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.sql.*;

public class TerminplanerZusammenGeloest extends JFrame {

	private JPanel contentPane;
	private JTextField tfDatum;
	private JTextField tfZeit;
	private JTextField tfText;
	private JTextField tfNummer;
	private DefaultListModel<String> termine = new DefaultListModel<String>();
	private JList<String> listTermine = new JList<String>(termine);
	private ResultSet rs;
	private String sql;
	private Statement stmt;

	public TerminplanerZusammenGeloest() {
		super("Termin-Verwaltung");
		createGUI();
		datenbankVerbindungHerstellen();
		datenbankTermineAnzeigen();
	}

	private void datenbankVerbindungHerstellen() {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/terminplaner?serverTimezone=UTC&useSSL=false", "root", "root");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void datenbankTermineAnzeigen() {
		try {
			String sql = "SELECT * FROM termin";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			termine.clear();
			while (rs.next()) {
				termine.addElement(rs.getString("termin_id") + ") " + rs.getString("datum") + ", " + rs.getString("zeit") + ", " + rs.getString("text"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void terminHinzufuegen() {
		datenbankTerminHinzufuegen();
		datenbankTermineAnzeigen();
	}

	private void terminLoeschen() {
		datenbankTerminLoeschen();
		datenbankTermineAnzeigen();
	}

	public void datenbankTerminHinzufuegen() {
		String datum = tfDatum.getText();
		String zeit = tfZeit.getText();
		String text = tfText.getText();
		try {
			String sql = "INSERT INTO termin VALUES "
					+ "(NULL, '" + datum + "', '" + zeit + "', '" + text + "')";
			System.out.println(sql);
			int anzahl = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void datenbankTerminLoeschen() {
		int ergebnis = 0;
		String id = tfNummer.getText();
		try {
			String sql = "DELETE FROM termin WHERE termin_id = " + id;
			System.out.println("datenbankTerminLoeschen(): " + sql);
			ergebnis = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (ergebnis == 1) {
			JOptionPane.showMessageDialog(this, "Der Termin wurde erfolgreich gelöscht.");
		} else {
			JOptionPane.showMessageDialog(this, "Der Termin konnte NICHT gelöscht werden!");
		}
	}

	private void createGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Vorhandene Termine:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 12, 132, 15);
		contentPane.add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 33, 364, 192);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(listTermine);

		lblNewLabel.setLabelFor(listTermine);

		JLabel lblNeuerTermin = new JLabel("Neuer Eintrag");
		lblNeuerTermin.setBounds(12, 248, 80, 15);
		contentPane.add(lblNeuerTermin);

		JLabel lblDatum = new JLabel("Datum:");
		lblDatum.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDatum.setBounds(12, 275, 55, 15);
		contentPane.add(lblDatum);

		tfDatum = new JTextField();
		lblDatum.setLabelFor(tfDatum);
		tfDatum.setText("YYYY-MM-DD");
		tfDatum.setBounds(65, 273, 87, 19);
		contentPane.add(tfDatum);
		tfDatum.setColumns(10);

		JLabel lblZeit = new JLabel("Zeit:");
		lblZeit.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblZeit.setBounds(185, 275, 42, 15);
		contentPane.add(lblZeit);

		tfZeit = new JTextField();
		lblZeit.setLabelFor(tfZeit);
		tfZeit.setText("HH:MM:SS");
		tfZeit.setBounds(223, 273, 72, 19);
		contentPane.add(tfZeit);
		tfZeit.setColumns(10);

		JLabel lblText = new JLabel("Text:");
		lblText.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblText.setBounds(12, 302, 42, 15);
		contentPane.add(lblText);

		tfText = new JTextField();
		lblText.setLabelFor(tfText);
		tfText.setBounds(65, 302, 293, 19);
		contentPane.add(tfText);
		tfText.setColumns(10);

		JButton btnHinzufgen = new JButton("Hinzufügen");
		btnHinzufgen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				terminHinzufuegen();
			}
		});
		btnHinzufgen.setBounds(12, 329, 98, 25);
		contentPane.add(btnHinzufgen);

		JLabel lblEintragLoeschen = new JLabel("Eintrag löschen");
		lblEintragLoeschen.setBounds(12, 386, 98, 15);
		contentPane.add(lblEintragLoeschen);

		JLabel lblNummerDesEintrags = new JLabel("Nummer des Eintrags:");
		lblNummerDesEintrags.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNummerDesEintrags.setBounds(12, 413, 134, 15);
		contentPane.add(lblNummerDesEintrags);

		tfNummer = new JTextField();
		lblNummerDesEintrags.setLabelFor(tfNummer);
		tfNummer.setBounds(162, 411, 42, 19);
		contentPane.add(tfNummer);
		tfNummer.setColumns(10);

		JButton btnLoeschen = new JButton("Löschen");
		btnLoeschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				terminLoeschen();
			}
		});
		btnLoeschen.setBounds(12, 440, 98, 25);
		contentPane.add(btnLoeschen);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TerminplanerZusammenGeloest frame = new TerminplanerZusammenGeloest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
