import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayMP3 extends JFrame {

	private JPanel contentPane;

	public PlayMP3() {
		super("PlayMP3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		playClip();
	}

	public void playClip() {
		Media clip = new Media(getClass().getResource("Every_OS_Sucks.mp3").toString());  // Auch *.wav
		MediaPlayer mediaPlayer = new MediaPlayer(clip);
		mediaPlayer.play();
	}

	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);
		EventQueue.invokeLater(new Runnable() {
		    public void run() {
		        new JFXPanel(); // initializes JavaFX environment
		        latch.countDown();
		    }
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayMP3 frame = new PlayMP3();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
