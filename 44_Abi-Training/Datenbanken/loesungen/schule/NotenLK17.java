package tmp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class NotenLK17 extends JFrame {

	private JPanel contentPane;
	private JLabel lblSchuelerId;
	private JTextField tfSchuelerId;
	private JButton btnLaden;
	private JLabel lblVorname;
	private JTextField tfVorname;
	private JLabel lblNachname;
	private JTextField tfNachname;
	private JLabel lblNoten;
	private JButton btnLoeschen;
	private JButton btnAendern;
	private JButton btnNeu;
	private DefaultComboBoxModel<String> noten = new DefaultComboBoxModel<String>();
	private JComboBox<String> cbNoten = new JComboBox<String>(noten);
	private Connection conn;
	private Statement stmt;
	private String schuelerID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotenLK17 frame = new NotenLK17();
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
	public NotenLK17() {
		createGUI();
		datenbankVerbinden();
		erstenSchuelerAnzeigen();
	}

	private void createGUI() {
		setTitle("Noten");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblSchuelerId = new JLabel("Schüler ID:");
		lblSchuelerId.setBounds(12, 12, 82, 15);
		contentPane.add(lblSchuelerId);

		tfSchuelerId = new JTextField();
		tfSchuelerId.setText("3");
		tfSchuelerId.setBounds(120, 10, 114, 19);
		contentPane.add(tfSchuelerId);
		tfSchuelerId.setColumns(10);

		btnLaden = new JButton("Laden");
		btnLaden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				laden();
			}
		});
		btnLaden.setBounds(340, 7, 98, 25);
		contentPane.add(btnLaden);

		lblVorname = new JLabel("Vorname:");
		lblVorname.setBounds(12, 63, 82, 15);
		contentPane.add(lblVorname);

		tfVorname = new JTextField();
		tfVorname.setEditable(false);
		tfVorname.setBounds(120, 61, 114, 19);
		contentPane.add(tfVorname);
		tfVorname.setColumns(10);

		lblNachname = new JLabel("Nachname:");
		lblNachname.setBounds(12, 101, 82, 15);
		contentPane.add(lblNachname);

		tfNachname = new JTextField();
		tfNachname.setEditable(false);
		tfNachname.setBounds(120, 99, 114, 19);
		contentPane.add(tfNachname);
		tfNachname.setColumns(10);

		lblNoten = new JLabel("Noten:");
		lblNoten.setBounds(12, 156, 82, 15);
		contentPane.add(lblNoten);

		btnLoeschen = new JButton("Löschen");
		btnLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loeschen();
			}
		});
		btnLoeschen.setBounds(12, 234, 98, 25);
		contentPane.add(btnLoeschen);

		btnAendern = new JButton("Ändern");
		btnAendern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aendern();
			}
		});
		btnAendern.setBounds(182, 234, 98, 25);
		contentPane.add(btnAendern);

		btnNeu = new JButton("Neu");
		btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				neueNote();
			}
		});
		btnNeu.setBounds(340, 234, 98, 25);
		contentPane.add(btnNeu);

		cbNoten.setBounds(120, 151, 318, 25);
		contentPane.add(cbNoten);
	}

	private void datenbankVerbinden() {
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/schule" + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true",
					"root", "root");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void laden() {
		try {
			schuelerID = tfSchuelerId.getText();
			String sql = "SELECT * FROM schueler WHERE schueler_id = '" + schuelerID + "'";
			System.out.println("laden(): " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				tfVorname.setText(rs.getString("vorname"));
				tfNachname.setText(rs.getString("nachname"));
			} else { // Schüler mit gegebener ID existiert nicht!
				JOptionPane.showMessageDialog(this, "Einen Schüler mit dieser ID gibt es nicht!");
				tfVorname.setText("");
				tfNachname.setText("");
				noten.removeAllElements();
				return;
			}
			sql = "SELECT * " + "FROM kurs, schueler, note " + "WHERE kurs_id = note_kurs_id "
					+ "AND schueler_id = note_schueler_id " + "AND schueler_id = '" + schuelerID + "'";
			System.out.println("laden(): " + sql);
			rs = stmt.executeQuery(sql);
			noten.removeAllElements();
			while (rs.next()) {
				noten.addElement(rs.getString("fach") + "(" + rs.getString("kurs_id") + "): " + rs.getString("note"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void erstenSchuelerAnzeigen() {
		String sql = "SELECT * FROM schueler";
		System.out.println("laden(): " + sql);
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				tfSchuelerId.setText(rs.getString("schueler_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		laden();
	}

	private void neueNote() {
		String neueNote = JOptionPane.showInputDialog("Gib die neue Note ein");
		String kursID = JOptionPane.showInputDialog("Gib die Kurs ID ein");
		String sql = "INSERT INTO note VALUES (" + kursID + ", " + schuelerID + ", " + neueNote + ")";
		System.out.println("neueNote(): " + sql);
		try {
			int anzahl = stmt.executeUpdate(sql);
			if (anzahl != 1) {
				JOptionPane.showMessageDialog(this, "Das Eintragen der Note hat nicht geklappt ...");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		laden();
	}

	private void aendern() {
		String neueNote = JOptionPane.showInputDialog("Gib die neue Note ein");
		System.out.println("aendern(): " + neueNote);
		String eintrag = (String) noten.getSelectedItem();
		String kursID = eintrag.substring(eintrag.indexOf('(') + 1, eintrag.indexOf(')'));
		String sql = "UPDATE note SET note = " + neueNote + " WHERE note_schueler_id = " 
				+ schuelerID + " AND  note_kurs_id = " + kursID;
		System.out.println("aendern(): " + sql);
		try {
			int anzahl = stmt.executeUpdate(sql);
			if (anzahl != 1) {
				JOptionPane.showMessageDialog(this, "Das Ändern der Note hat nicht geklappt ...");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		laden();
	}

	private void loeschen() {
		int antwort = JOptionPane.showConfirmDialog(this,
				"Sind Sie sicher, dass der Noteneintrag gelöscht werden soll?");
		if (antwort == JOptionPane.YES_OPTION) {
			String eintrag = (String) noten.getSelectedItem();
			String kursID = eintrag.substring(eintrag.indexOf('(') + 1, eintrag.indexOf(')'));
			System.out.println("loeschen(): " + kursID);
			String sql = "DELETE FROM note WHERE note_schueler_id = '" + schuelerID + "' " + "AND note_kurs_id = "
					+ kursID;
			System.out.println("loeschen(): " + sql);
			try {
				int anzahl = stmt.executeUpdate(sql);
				if (anzahl != 1) {
					JOptionPane.showMessageDialog(this, "Löschen hat nicht geklappt ...");
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			laden();
		}
	}

}
