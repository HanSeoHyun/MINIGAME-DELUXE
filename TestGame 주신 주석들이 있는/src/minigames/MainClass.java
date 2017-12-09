package minigames;

import java.awt.Graphics;

import javax.swing.JFrame;

public class MainClass {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("MINIGAME DELUXE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame.setResizable(false);
		
		MainPanel mainPanel = new MainPanel(frame);
		
		frame.getContentPane().add(mainPanel);
		
		frame.pack();
		frame.setVisible(true);
	}

}
