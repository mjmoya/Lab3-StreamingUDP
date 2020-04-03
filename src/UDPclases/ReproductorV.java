package UDPclases;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class ReproductorV extends javax.swing.JFrame {

	private final JFXPanel jfxPanel;
	Icon iconPlay = new ImageIcon ("./data/play.png");
	JButton playButton = new JButton(iconPlay);
	Icon iconPause = new ImageIcon ("./data/pause.png");
	JButton pauseButton = new JButton(iconPause);
	JPanel buttonPanel = new JPanel();
	JComboBox<String> comboBox1 = new JComboBox<String>();
	
	MediaPlayer video;

	/**
	 * Creates new form JFrameVideo
	 * @throws InterruptedException 
	 */
	public ReproductorV() {

		jfxPanel = new JFXPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		
		comboBox1.addItem("Morado");
		comboBox1.addItem("Azul");
		comboBox1.addItem("Amarillo");
		
		buttonPanel.add(comboBox1);
		buttonPanel.add(pauseButton);
		buttonPanel.add(playButton);
		
		
		playButton.addActionListener(new playButtonListener());
		pauseButton.addActionListener(new pauseButtonListener());
		
		setTitle("Java Swing Video con FX");
		setResizable(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		add(jfxPanel,BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		setSize(new Dimension(400, 600));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public String selecciónVideo(){
		String color = (String) comboBox1.getSelectedItem();
		return color;
		
	}
	
	
	public void createScene(File file){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {                                                  
				video = new MediaPlayer(new Media(file.toURI().toString()));
				
				//se añade video al jfxPanel
				jfxPanel.setScene(new Scene(new Group(new MediaView(video))));                    
				//Ajustar el volumen
				video.setVolume(0.7);
				//play video
				video.play();
			}
		});
	}

	class playButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			video.play();
		}
	}
	class pauseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			video.pause();
		}
	}
}