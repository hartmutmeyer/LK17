import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import toolbox.DebugStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class RechentrainerClientLK17 extends JFrame {

	private JPanel contentPane;
	private JLabel lblServer;
	private JTextField tfServer;
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblAufgabe;
	JTextField tfAufgabe;
	private JLabel lblLoesung;
	JTextField tfLoesung;
	JButton btnStarten;
	JButton btnLoesungSenden;
	private OutputStreamWriter out;
	private RechentrainerClientLeseThreadLK17 lt;
	private InputStreamReader in;

	/**
	 * Create the frame.
	 */
	public RechentrainerClientLK17() {
		createGUI();
	}

	private void createGUI() {
		setTitle("Rechentrainer Client LK17");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 225);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblServer = new JLabel("Server:");
		lblServer.setBounds(12, 12, 69, 15);
		contentPane.add(lblServer);
		
		tfServer = new JTextField();
		tfServer.setBounds(99, 10, 114, 19);
		contentPane.add(tfServer);
		tfServer.setColumns(10);
		
		lblName = new JLabel("Name:");
		lblName.setBounds(12, 39, 69, 15);
		contentPane.add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(99, 37, 114, 19);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		lblAufgabe = new JLabel("Aufgabe:");
		lblAufgabe.setBounds(12, 125, 69, 15);
		contentPane.add(lblAufgabe);
		
		tfAufgabe = new JTextField();
		tfAufgabe.setEditable(false);
		tfAufgabe.setBounds(99, 123, 114, 19);
		contentPane.add(tfAufgabe);
		tfAufgabe.setColumns(10);
		
		lblLoesung = new JLabel("Lösung:");
		lblLoesung.setBounds(12, 152, 69, 15);
		contentPane.add(lblLoesung);
		
		tfLoesung = new JTextField();
		tfLoesung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loesungSenden();
			}
		});
		tfLoesung.setBounds(99, 150, 114, 19);
		contentPane.add(tfLoesung);
		tfLoesung.setColumns(10);
		
		btnStarten = new JButton("Starten");
		btnStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verbinden();
			}
		});
		btnStarten.setBounds(238, 34, 153, 25);
		contentPane.add(btnStarten);
		
		btnLoesungSenden = new JButton("Lösung Senden");
		btnLoesungSenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loesungSenden();
			}
		});
		btnLoesungSenden.setEnabled(false);
		btnLoesungSenden.setBounds(238, 147, 153, 25);
		contentPane.add(btnLoesungSenden);
		
	}

	protected void verbinden() {
		Socket s;
		if (tfName.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Du hast keinen Namen angegben!");
			return;
		} // else unnötig wegen return
		try {
			String server = tfServer.getText();
			s = new Socket(server, 33333);
			in = new InputStreamReader(s.getInputStream(), "UTF-8");
			out = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
			lt = new RechentrainerClientLeseThreadLK17(this, in);
			lt.start();
			String ausgabe = tfName.getText() + "$";
			System.out.println("verbinden(): " + ausgabe);
			out.write(ausgabe);
			out.flush();
			btnStarten.setEnabled(false);
			btnLoesungSenden.setEnabled(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Verbindung zum Server konnte nicht aufgebaut werden");
			e.printStackTrace();
		}
	}

	protected void loesungSenden() {
		String ausgabe = tfLoesung.getText() + "$";
		System.out.println("loesungSenden(): " + ausgabe);
		try {
			out.write(ausgabe);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebugStream.activate();
					RechentrainerClientLK17 frame = new RechentrainerClientLK17();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}



