import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RechnenMitVerstecktenZahlen extends JFrame {

	private JPanel contentPane;
	private JTextField tfErsterSummand;
	private JTextField tfZweiterSummand;
	private JTextField tfErgebnis;
	private JLabel lblVersteckteSumme;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RechnenMitVerstecktenZahlen frame = new RechnenMitVerstecktenZahlen();
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
	public RechnenMitVerstecktenZahlen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 412, 139);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfErsterSummand = new JTextField();
		tfErsterSummand.setBounds(10, 11, 130, 20);
		contentPane.add(tfErsterSummand);
		tfErsterSummand.setColumns(10);
		
		tfZweiterSummand = new JTextField();
		tfZweiterSummand.setBounds(150, 11, 130, 20);
		contentPane.add(tfZweiterSummand);
		tfZweiterSummand.setColumns(10);
		
		JButton btnAddieren = new JButton("Addieren");
		btnAddieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				versteckteSummeBilden();
			}
		});
		btnAddieren.setBounds(290, 10, 89, 23);
		contentPane.add(btnAddieren);
		
		tfErgebnis = new JTextField();
		tfErgebnis.setEditable(false);
		tfErgebnis.setBounds(150, 64, 86, 20);
		contentPane.add(tfErgebnis);
		tfErgebnis.setColumns(10);
		
		lblVersteckteSumme = new JLabel("Versteckte Summe:");
		lblVersteckteSumme.setBounds(10, 67, 130, 14);
		contentPane.add(lblVersteckteSumme);
	}

	private void versteckteSummeBilden() {
		String versteckteZahl1 = tfErsterSummand.getText();
		String versteckteZahl2 = tfZweiterSummand.getText();
		
		int summand1 = zahlExtrahieren(versteckteZahl1);
		int summand2 = zahlExtrahieren(versteckteZahl2);
		
		tfErgebnis.setText("" + (summand1 + summand2));
	}
	
	private int zahlExtrahieren(String input) {
		String zahl = "0";  // Strings, die keine Ziffern enthalten sollen den Wert 0 haben
		for (int i = 0; i < input.length(); i++) {
			if (Character.isDigit(input.charAt(i))) {
				zahl += input.charAt(i);
			}
		}
		return Integer.parseInt(zahl);
	}
}
