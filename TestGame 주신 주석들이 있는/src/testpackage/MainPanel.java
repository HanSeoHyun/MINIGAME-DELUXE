package testpackage;

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

// ó�� ȭ�鿡 �����ϴ� �޴� ȭ���� JPanel �̴�.
// ġŲ����, ���װ��� �� �� �ϳ��� ������ �� �ֵ��� ȭ�� �� �쿡 �� ���� ��ư�� ���´�.
public class MainPanel extends JPanel {	
	private JButton btnChickenGame;
	private JButton btnRunningGame;
	// �� ������ ������ ��ư���̴�.
	
	private JFrame mainFrame;
	// �����ڰ� ���ڷ� ���� JFrame �����̴�. �̸� ���� �������ִ� JFrame�� �����Ѵ�.
	
	private ImageIcon backgroundImage;
	// �޴� ȭ���� ��� �̹����� ������ �����̴�.
	
	private ChickenPanel chkPanel;
	private RunningGamePanel rngPanel;
	// �� ���ӵ��� ��ü�� ������ �������̴�. �� ���� ��� JPanel�� ����ϹǷ� Ŭ������, ������ ��� Panel�� ǥ���ߴ�.
	
	private MainPanel mp;
	// �ڱ� �ڽ�(MainPanel)�� ������ �����̴�.
	
	public MainPanel(JFrame frame) {
		mainFrame = frame;
		mp = this;
		//���ڷ� ���� JFrame�� �ڱ� �ڽ��� ��ü�� ������ instance�����鿡 �����Ѵ�.
		
		setLayout(null);
		setBackground(Color.cyan);
		// �г��� ���̾ƿ��� ���ְ�, �������� �����Ѵ�.
		
		backgroundImage = new ImageIcon("src/images/MainMenuBackground.png");
		JLabel lblBackground = new JLabel(backgroundImage);
		lblBackground.setBounds(0, 0, 800, 800);
		add(lblBackground);
		// ���� �̹����� JLabel ������ �����ϸ� �־��ְ�, �̸� �гο� �߰��Ͽ� ����ȭ������ �����Ѵ�.
		
		setPreferredSize(new Dimension(800, 800));
		// �г��� ũ�⸦ �����Ѵ�.
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(0, 300, 800, 800);
		add(buttonPanel);
		// ��ư���� �����̹����� �����ִ� JLabel ������Ʈ���� ������ ������ �ǵ��� �г� �ϳ��� �����Ѵ�.
		// �� �гο� ��ư���� �߰��ǰ�, ���� JLabel���� �տ� �ߵ��� �����Ѵ�.
		
		btnChickenGame = new JButton(new ImageIcon("src/images/chickenGameButton.png"));
		btnChickenGame.setBounds(90, 0, 300,400);
		btnChickenGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnChickenGame.addActionListener(new GameChoiceListener());
		btnChickenGame.setBackground(new Color(150, 225, 209));
		btnChickenGame.setBorderPainted(false);
		buttonPanel.add(btnChickenGame);
		// ġŲ ������ ������ ��ư�� �����ϰ� ��ư �гο� �߰��Ѵ�.
		// ��ư�� �̹����� �����ϰ�, ��ġ, ������, �׵θ� ������ �����Ѵ�.
		// �����ʸ� �߰��Ͽ� ��ư�� ������ ������ �Ѵ�.
		
		btnRunningGame = new JButton(new ImageIcon("src/images/runningGameButton.png"));
		btnRunningGame.setBounds(410, 0, 300, 400);
		btnRunningGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnRunningGame.addActionListener(new GameChoiceListener());
		btnRunningGame.setBackground(new Color(150, 225, 209));
		btnRunningGame.setBorderPainted(false);
		buttonPanel.add(btnRunningGame);
		// ���� ������ ������ ��ư�� �����ϰ� ��ư �гο� �߰��Ѵ�.
		// ��ư�� �̹����� �����ϰ�, ��ġ, ������, �׵θ� ������ �����Ѵ�.
		// �����ʸ� �߰��Ͽ� ��ư�� ������ ������ �Ѵ�.
		
		setComponentZOrder(lblBackground, 1);
		setComponentZOrder(buttonPanel, 0);
		// ������ ������ �����ϰ�, ��ư �г��� ���� �����Ѵ�.
	}
	
	// �г� �� ��ư���� �����ϵ��� �����ʸ� ���� Ŭ������ �����Ͽ���.
	// �����ڿ��� �����Ǿ� ��ư�� �߰��ȴ�.
	private class GameChoiceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) { // ����� �������̽��� �޼ҵ带 �����Ѵ�.
			// TODO Auto-generated method stub
			Object obj = e.getSource(); // �̺�Ʈ�� �߻���Ų ������Ʈ�� �ҽ��� Object������ �޴´�.
			
			if(obj == btnChickenGame) { // �̺�Ʈ�� ġŲ���� ��ư�̾��ٸ�
				chkPanel = new ChickenPanel(mainFrame);
				// ġŲ ���� �г��� �����Ѵ�.
				System.out.println("check");
				mainFrame.getContentPane().removeAll();				
				mainFrame.getContentPane().add(chkPanel);
				// frame�� �ִ� ������Ʈ���� ��� �����ϰ�, ġŲ ���� �г��� �߰��Ѵ�.
				chkPanel.initKeyboardListener();
				// ġŲ ���� �г��� Ű���� �����ʸ� ���� �� ��Ŀ���� �Ͽ� Ű���� �Է��� �����ϵ��� �Ѵ�.
				
			}
			else if(obj == btnRunningGame) { // �̺�Ʈ�� ���װ��� ��ư�̾��ٸ�
				Rectangle r = mainFrame.getBounds();				
				mainFrame.setSize(r.width, r.height - 1);
				mainFrame.setSize(r.width, r.height + 1);
				// ���װ����� ������ �� repaint�� ����� ������ ���� �ʾ� ������ �������,
				// ó���� �������� ����� 1�ȼ� ������¡�� �� �ٽ� ���� ũ��� �ǵ��� ������ repaint�ϵ��� �ߴ�.
				// �̷ν� ���װ����� ����� ȭ�鿡 �ߵ��� �Ͽ���.
				
				rngPanel = new RunningGamePanel(mainFrame);
				// ���� ���� �г��� �����Ѵ�.
				mainFrame.getContentPane().removeAll();
				mainFrame.getContentPane().add(rngPanel);
				// frame�� �ִ� ������Ʈ���� ��� �����ϰ�, ���� ���� �г��� �߰��Ѵ�.
				rngPanel.initKeyboardListener();
				// ���� ���� �г��� Ű���� �����ʸ� ���� �� ��Ŀ���� �Ͽ� Ű���� �Է��� �����ϵ��� �Ѵ�.
			}
		}		
	}
}
