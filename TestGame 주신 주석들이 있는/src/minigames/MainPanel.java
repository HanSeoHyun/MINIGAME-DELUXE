package minigames;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainPanel extends JPanel {	
	private JButton btnChickenGame;
	private JButton btnRunningGame;
	
	private JFrame mainFrame;
	
	private ImageIcon backgroundImage;
	
	
	private ChickenPanel chkPanel;
	private RunningGamePanel rngPanel;
	
	private MainPanel mp;
	
	public MainPanel(JFrame frame) {
		mainFrame = frame;
		mp = this;
		setLayout(null);
		setBackground(Color.cyan);
		
		backgroundImage = new ImageIcon("src/images/MainMenuBackground.png");
		JLabel lblBackground = new JLabel(backgroundImage);
		lblBackground.setBounds(0, 0, 800, 800);
		add(lblBackground);
		
		setPreferredSize(new Dimension(800, 800));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(0, 300, 800, 800);
		add(buttonPanel);
		
		btnChickenGame = new JButton(new ImageIcon("src/images/chickenGameButton.png"));
		btnChickenGame.setBounds(90, 0, 300,400);
		btnChickenGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnChickenGame.addActionListener(new GameChoiceListener());
		btnChickenGame.setBackground(new Color(150, 225, 209));
		btnChickenGame.setBorderPainted(false);
		buttonPanel.add(btnChickenGame);
		
		btnRunningGame = new JButton(new ImageIcon("src/images/runningGameButton.png"));
		btnRunningGame.setBounds(410, 0, 300, 400);
		btnRunningGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnRunningGame.addActionListener(new GameChoiceListener());
		btnRunningGame.setBackground(new Color(150, 225, 209));
		btnRunningGame.setBorderPainted(false);
		buttonPanel.add(btnRunningGame);
		
		setComponentZOrder(lblBackground, 1);
		setComponentZOrder(buttonPanel, 0);
	}
	
	private class GameChoiceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			
			if(obj == btnChickenGame) {
				chkPanel = new ChickenPanel(mainFrame);
				System.out.println("check");
				mainFrame.getContentPane().removeAll();				
				mainFrame.getContentPane().add(chkPanel);
				chkPanel.initKeyboardListener();
			}
			else if(obj == btnRunningGame) {
				Rectangle r = mainFrame.getBounds();				
				mainFrame.setSize(r.width, r.height - 1);
				mainFrame.setSize(r.width, r.height + 1);
				
				rngPanel = new RunningGamePanel(mainFrame);
				mainFrame.getContentPane().removeAll();
				mainFrame.getContentPane().add(rngPanel);
				rngPanel.initKeyboardListener();
			}
		}		
	}
}
