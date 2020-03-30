package tmpQ2;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FahrzeugeAbmeldenLK17 extends JFrame {

	private JPanel contentPane;
	private JLabel lblFahrzeugliste;
	private DefaultListModel<String> fahrzeuge = new DefaultListModel<String>();
	private JList<String> listFahrzeuge = new JList<String>(fahrzeuge);
	private JScrollPane scrollPane;
	private JLabel lblAbmeldedatum;
	private JTextField tfAbmeldedatum;
	private JButton btnKfzAbmelden;
	private Statement stmt;
	private ResultSet rs;
	private String sql;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FahrzeugeAbmeldenLK17 frame = new FahrzeugeAbmeldenLK17();
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
	public FahrzeugeAbmeldenLK17() {
		createGUI();
		datenbankVerbinden();
		fahrzeugeAnzeigen();
	}

	private void datenbankVerbinden() {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/fahrzeuge"
					+ "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fahrzeugeAnzeigen() {
		fahrzeuge.clear();
		sql = "SELECT * FROM fahrzeughalter WHERE abgemeldet IS NULL";
		System.out.println("fahrzeugeAnzeigen(): " + sql);
		try {
			rs = stmt.executeQuery(sql);
			String eintrag;
			while (rs.next()) {
				eintrag = rs.getString("kfz_zeichen") + ", " + rs.getString("vorname") + " " + rs.getString("nachname");
				eintrag += " (" + rs.getString("geburtstag") + ")";
				fahrzeuge.addElement(eintrag);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void kfzAbmelden() {
		if (tfAbmeldedatum.getText().isEmpty() || listFahrzeuge.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(this,	"Bitte korrekte Eingaben machen");
			return;
		}
		String abmeldedatum = tfAbmeldedatum.getText();
		String s = listFahrzeuge.getSelectedValue();
		String kfzZeichen = s.substring(0, s.indexOf(','));
		s = s.substring(s.indexOf(',') + 1).trim();
		System.out.println("eintrag = '" + s + "'");
		String vorname = s.substring(0, s.indexOf(' '));
		s = s.substring(s.indexOf(' ')).trim();
		String nachname = s.substring(0, s.indexOf(' '));
		sql = "UPDATE fahrzeughalter SET abgemeldet = '" + abmeldedatum + "' WHERE vorname = '" + vorname
				+ "' AND nachname = '" + nachname + "' AND kfz_zeichen = '" + kfzZeichen + "'";
		try {
			System.out.println("kfzAbmelden(): " + sql);
			int anzahlGeaenderterDatensaetze = stmt.executeUpdate(sql);
			if (anzahlGeaenderterDatensaetze != 1) {  // Überprüfung war durch die Aufgabenstellung nicht verlangt ... trotzdem ...
				JOptionPane.showMessageDialog(this,
						"Es wurden " + anzahlGeaenderterDatensaetze + " Datensätze geändert");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		fahrzeugeAnzeigen();
	}

	private void createGUI() {
		setTitle("Fahrzeuge Abmelden");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblFahrzeugliste = new JLabel("Fahrzeugliste:");
		lblFahrzeugliste.setBounds(22, 12, 104, 15);
		contentPane.add(lblFahrzeugliste);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 39, 416, 183);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(listFahrzeuge);

		lblAbmeldedatum = new JLabel("Abmeldedatum:");
		lblAbmeldedatum.setBounds(22, 244, 104, 15);
		contentPane.add(lblAbmeldedatum);

		tfAbmeldedatum = new JTextField();
		tfAbmeldedatum.setBounds(144, 242, 114, 19);
		contentPane.add(tfAbmeldedatum);
		tfAbmeldedatum.setColumns(10);

		btnKfzAbmelden = new JButton("Kfz Abmelden");
		btnKfzAbmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kfzAbmelden();
			}
		});
		btnKfzAbmelden.setBounds(311, 239, 127, 25);
		contentPane.add(btnKfzAbmelden);
	}

}
