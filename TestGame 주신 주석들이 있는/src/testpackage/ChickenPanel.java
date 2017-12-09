package testpackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChickenPanel extends JPanel {

	private final int GAMETIME = 30; // ���� �÷��� ���ѽð� 
	
	private JFrame 		frame; //
	private JPanel 		showUpPanel, showDownPanel, preStart; // ���� ġŲ�� �����ִ� �г�, ���� ġŲ �г�, ���� ���� �� ī��Ʈ�ٿ� ǥ�õǴ� �г�
	
	private JLabel 		lblBase, scoreArea, timerArea, lblScore2, //��� �̹��� ��, 'SCORE'ǥ�ö�, 'TIME'ǥ�ö�, ����ǥ�� ��
						lblTimer2, countDown, choosingChicken, answerFeedback, lblChickenAfterMove;
						//Ÿ�̸� ǥ�ö�, ���� ���� �� ī��Ʈ�ٿ�, ġŲ ����, ���� Ȯ��, ġŲ �Ű��ִ� �ִϸ��̼� ��
	private JLabel[] 	lblChickenArray; // ġŲ�� �̹��� �Ҵ� �迭
	private JButton 	btnFried, btnSeasoned; // �Ķ��̵� ��ư, ��� ��ư
	private int 		nScore = 0, nReset = 0, nPreCount = 4, answer = 3; 
	// ����, ����?, ����ī��Ʈ??, answer==1 �̸� ����, answer==2�̸� ����, answer==3�̸� �ʱ�ȭ 
	private boolean 	canPress = false; // �Է� Ȱ��ȭ ��
	private static int 	nTimer, nNext = 0, nSetting = 0; // �ð�, ġŲ �迭 �ε���
	
	private ImageIcon 	backGround_Icon, ready_Icon, fried_Icon, seosoned_Icon, 
						good_Icon, bad_Icon, friedBox_Icon, seasonedBox_Icon, 
						choosing_Icon, pushLeft_Icon,
						pushRight_Icon, scoreBox_Icon, timeBox_Icon;
						// ���� �̹�����
	private int[] 		chickenArray; // 1000��¥�� ũ�� ġŲ�� �迭
	private TimerTask 	m_task; // timer Ŭ������ ������ �۾�
	private Timer 		m_timer; // ���� �ð��� �ֱ�� �ݺ������� Ư�� �۾� �����ϱ�
	private File 		gameBGM; // BGM
	private Clip 		clip; // ����� ���� ��ü
	
	
	//get/set �޼ҵ��
	public JLabel getlblChickenArray(int i) { // ġŲ �̹��� �迭�� �ϳ��� ��ȯ���ִ� �޼ҵ��̴�
		return lblChickenArray[i];
	}
	
	public JLabel getAnswerFeedBack() // �ؼ��ǵ���� ��ȯ���ִ� �޼ҵ��̴�
	{
		return answerFeedback;
	}
	
	public Timer getTimer() // Ÿ�̸Ӹ� ��ȯ���ִ� �޼ҵ��̴�
	{
		return m_timer;
	}
	
	private ChickenAnimation cA; // ġŲ�ִϸ��̼� ��ü ����
	private arrowKeyListener key; // Ű�Է� �޼ҵ� ����

	public ChickenPanel(JFrame frame) { // ���� �ν��Ͻ� ���� �ʱ�ȭ ������
		
		this.frame = frame; // ������ ����
		
		setPreferredSize(new Dimension(800, 800)); // ���α׷� ��ü ũ��
		setBackground(Color.black);
		setLayout(null);

		key = new arrowKeyListener(); // Ű�Է� �޼ҵ� ����
		cA = new ChickenAnimation(this); // �ִϸ��̼� �޼ҵ� ����
		
		initValues(); // ���� �ʱ�ȭ ����

		ready_Icon = new ImageIcon("src/images/chick1.png"); // ��� �̹��� �ҷ�����
		fried_Icon = new ImageIcon("src/images/chick2.png"); // Ƣ�� �̹��� �ҷ�����
		seosoned_Icon = new ImageIcon("src/images/chick3.png"); // ��� �̹��� �ҷ�����
		good_Icon = new ImageIcon("src/images/good.png"); // ���� �̹��� �ҷ�����
		bad_Icon = new ImageIcon("src/images/bad.png"); // ���� �̹��� �ҷ�����
		friedBox_Icon = new ImageIcon("src/images/�Ķ��̵�ڽ�.png"); // �Ķ��̵� �ڽ� �̹��� �ҷ�����
		seasonedBox_Icon = new ImageIcon("src/images/���ڽ�.png"); // ��� �ڽ� �̹��� �ҷ�����
		choosing_Icon = new ImageIcon("src/images/ġŲ �ű��.png"); // ���� ȭ��ǥ �̹��� �ҷ�����
		pushLeft_Icon = new ImageIcon("src/images/ġŲ �ű��(���ʼ���).png"); // ���� Ȱ��ȭ ȭ��ǥ �̹��� �ҷ�����
		pushRight_Icon = new ImageIcon("src/images/ġŲ �ű��(�����ʼ���).png"); // ������ Ȱ��ȭ ȭ��ǥ �̹��� �ҷ�����
		scoreBox_Icon = new ImageIcon("src/images/���ھ�ڽ�.png"); // ����ĭ ��� �̹��� �ҷ�����
		timeBox_Icon = new ImageIcon("src/images/Ÿ�ӹڽ�.png"); // �����ð� ��� �̹��� �ҷ�����
		backGround_Icon = new ImageIcon("src/images/ġŲ���ӹ��.png"); // ���� ��� �̹��� �ҷ�����

		gameBGM = new File("src/sounds/RunBGM.wav"); // ���� ���� �ҷ�����
		
		lblBase = new JLabel(backGround_Icon); // ���� ��ü �г�
		lblBase.setBounds(00, 00, 800, 800);
		lblBase.setBackground(Color.white);
		lblBase.setLayout(null);
		add(lblBase);
		lblBase.addKeyListener(key);

		scoreArea = new JLabel(scoreBox_Icon); // ���� ǥ�� �г�
		scoreArea.setBounds(0, 0, 200, 200);
		scoreArea.setBackground(Color.white);
		scoreArea.setLayout(null);
		lblBase.add(scoreArea);

		timerArea = new JLabel(timeBox_Icon); // Ÿ�̸� ǥ�� �г�
		timerArea.setBounds(600, 0, 200, 200);
		timerArea.setBackground(Color.white);
		timerArea.setLayout(null);
		lblBase.add(timerArea);

		lblScore2 = new JLabel("" + nScore); // SCORE �� ���� ǥ��
		lblScore2.setBounds(0, 0, 200, 200);
		lblScore2.setFont(new Font("Consoals", Font.BOLD, 60));
		lblScore2.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore2.setVisible(true);
		scoreArea.add(lblScore2);

		lblTimer2 = new JLabel("" + nTimer); // TIME �� �����ð� ǥ��
		lblTimer2.setBounds(0, 0, 200, 200);
		lblTimer2.setFont(new Font("Consoals", Font.BOLD, 60));
		lblTimer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimer2.setVisible(true);
		timerArea.add(lblTimer2);

		btnFried = new JButton(friedBox_Icon); // Ƣ�� ���� ��ư
		btnFried.setBounds(0, 500, 200, 300);
		btnFried.setFont(new Font("Consoals", Font.BOLD, 30));
		btnFried.setBackground(new Color(230, 165, 46));
		btnFried.setEnabled(false);
		btnFried.setBorderPainted(false); // ��ư �׵θ� ����
		btnFried.addKeyListener(key);
		lblBase.add(btnFried);

		btnSeasoned = new JButton(seasonedBox_Icon); // ��� ���� ��ư
		btnSeasoned.setBounds(600, 500, 200, 300);
		btnSeasoned.setFont(new Font("Consoals", Font.BOLD, 30));
		btnSeasoned.setBackground(new Color(170, 71, 71));
		btnSeasoned.setEnabled(false);
		btnSeasoned.setBorderPainted(false); // ��ư �׵θ� ����
		btnFried.addKeyListener(key);
		lblBase.add(btnSeasoned);
		
		showUpPanel = new JPanel(); // ���� ġŲ�� ǥ�� �г�
		showUpPanel.setBounds(300, 0, 200, 800);
		showUpPanel.setBackground(Color.white);
		showUpPanel.setOpaque(false); // �����ϰ�!
		lblBase.add(showUpPanel);

		showDownPanel = new JPanel(); // ���� ġŲ ǥ�� �г�
		showDownPanel.setBounds(300, 270, 200, 500);
		showDownPanel.setBackground(Color.white);
		showDownPanel.setOpaque(false);
		showDownPanel.setLayout(null);
		lblBase.add(showDownPanel);

		
		choosingChicken = new JLabel(choosing_Icon); // ġŲ ���� ȭ��ǥ �̹��� ��
		choosingChicken.setBounds(200, 550, 400, 250);
		choosingChicken.setBackground(Color.yellow);
		choosingChicken.setLayout(null);
		lblBase.add(choosingChicken);
		
		lblChickenArray = new JLabel[5]; // �̹��� �������� �󺧵� ��� ��� �Ϻ� �гο� ���� �������ֱ�
		lblChickenArray[0] = new JLabel(ready_Icon);
		lblChickenArray[0].setBounds(0, 525 - 270, 200, 300);
		showDownPanel.add(lblChickenArray[0]);

		/////////////////////////////////////////////////////////////////////////////////////////////
		
		// ���̾ƿ� ��
		showUpPanel.setLayout(null);
		showDownPanel.setLayout(null);
		
		// �ʿ信 ���ؼ� 4���� �߰��� ����
		cA.setChickenArrayOriginPoint(0, lblChickenArray[0].getLocation());
		for (int i = 1; i < 5; i++) {
			lblChickenArray[i] = new JLabel(ready_Icon);
			lblChickenArray[i].setBounds(0, 200 - 135 * (i - 1), 200, 300);
			// ����ġ �� �����ְ�
			cA.setChickenArrayOriginPoint(i, lblChickenArray[i].getLocation());
			// �гο� �־���
			showUpPanel.add(lblChickenArray[i]);
		}

		// chickenAfterMove
		// Ű���� �Է� �� ġŲ�� ������ ������ ���� ��
		lblChickenAfterMove = new JLabel();
		lblChickenAfterMove.setBounds(0, 0, 200, 300);
		showDownPanel.add(lblChickenAfterMove);
		// setIcon

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// -----���� ���� �� ī��Ʈ �ٿ� ------
		preStart = new JPanel();
		preStart.setBounds(0, 400, 600, 200);
		preStart.setBackground(Color.green);
		preStart.setOpaque(false);
		preStart.setLayout(null);
		lblBase.add(preStart);

		countDown = new JLabel(Integer.toString(nPreCount)); // ���� ���� �� ī��Ʈ�ٿ�
		countDown.setBounds(300, 00, 200, 200);
		countDown.setFont(new Font("Consoals", Font.BOLD, 100));
		countDown.setForeground(Color.blue);
		preStart.setOpaque(false);
		countDown.setHorizontalAlignment(SwingConstants.CENTER);
		countDown.setVerticalAlignment(SwingConstants.TOP);
		countDown.setVisible(true);
		preStart.add(countDown);
		// ----------------------------

		answerFeedback = new JLabel(); // ���� �Ǻ����ִ� �̹��� �ڸ� ��
		answerFeedback.setBounds(200, 0, 400, 152);
		answerFeedback.setHorizontalAlignment(SwingConstants.CENTER);
		answerFeedback.setVerticalAlignment(SwingConstants.CENTER);
		cA.setAnswerFeedbackPoint(answerFeedback.getLocation());
		preStart.add(answerFeedback);

		gameTimer(); 			// ���� ���� Ÿ�̸�
		gameReadyTimer(); 		// ���� ���� �� �غ� Ÿ�̸�
		chickenLabelSetting(); 	// ġŲ �迭 ���� ����
	}
	
	public void playSound(File sound) { // BGM ��� �޼ҵ�
		try {            
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();            
        } catch(Exception e) {            
            e.printStackTrace();
        }
	}
	
	public void stopSound() { // BGM ����� �����ִ� �޼ҵ�        
        if(clip!=null && clip.isRunning()){
        	clip.stop();
        	clip.close();
        }
	}
	
	public void initValues() { // ���� �ʱ�ȭ �޼ҵ�
		nScore = nReset = nNext = nSetting = 0; 
		nPreCount = 4; 
		answer = 3;
		canPress = false;
		nTimer = GAMETIME;
	}
	
	public void initKeyboardListener() { // Ű���� �Է��� �����ϵ��� ��Ŀ���� �гο� ���ߴ� �޼ҵ�
		lblBase.setFocusable(true);
		lblBase.requestFocus();
	}

	public void chickenLabelSetting() { // ġŲ �迭�� ���� �������� �������ִ� �޼ҵ�
		chickenArray = new int[1000];
		Random r = new Random(); // Random�� ��ü�� ����ϴ�.

		for (int i = 0; i < chickenArray.length; i++) { // �迭�� ũ�⸸ŭ for���� �����ϴ�.
			chickenArray[i] = r.nextInt(2);
		}
	}

	public void gameContinue() { // ���� ���� �� ������ �˷��ְ� �ٽ� �������� ����� �˾�â�� ���� �޼ҵ�
		
		int result;

		result = JOptionPane.showConfirmDialog(this, 
				"YOUR GRADE IS " + nScore + "! CONTINUE?",
				"GAME OVER!!",
				JOptionPane.YES_NO_OPTION ); // �絵�� �Ǵ� �޴� ���� �˾�

		if (result == JOptionPane.YES_OPTION) {
			//���� �ٽ� �ϱ�
			//������ �ٽ��� �� �ʱ�ȭ �ɼ�
			nScore = nNext = nSetting = 0;
			lblScore2.setText("" + nScore);
			nTimer = GAMETIME;
			nPreCount = 4;
			countDown.setVisible(true);
			choosingChicken.setIcon(choosing_Icon);
			countDown.setText(Integer.toString(nPreCount));
			for (nReset = 0; nReset < 4; nReset++) {
				lblChickenArray[nReset].setIcon(ready_Icon);
				lblChickenArray[nReset].updateUI();
			}
			answerFeedback.setIcon(null);
			gameTimer();
			chickenLabelSetting();
			gameReadyTimer();
		} else if (result == JOptionPane.NO_OPTION) {
			//���� �޴��� ���ư���
			System.out.println("NO");
			
			Rectangle r = frame.getBounds();
			frame.setSize(r.width, r.height - 1);
			frame.setSize(r.width, r.height + 1);

			frame.getContentPane().removeAll();
			frame.getContentPane().add(new MainPanel(frame));
		}
	}

	public void chickenImageSetting(int nSetting, int key) { // ġŲ �迭 ���� �޾Ƽ� ���� ���� �̹����� ���� ǥ�����ִ� �޼ҵ�

		if (key != 0 && nSetting != 0) {
			lblChickenAfterMove.setIcon(lblChickenArray[0].getIcon());
			lblChickenAfterMove.setLocation(lblChickenArray[0].getLocation());
		}

		for (int i = 0; i < 4; i++) {
			Icon chickenIcon = chickenArray[nSetting + i] == 0 ? fried_Icon : seosoned_Icon;
			lblChickenArray[i].setIcon(chickenIcon);
			// ��ġ�� ��ĭ�� ����
			if(i!=0)
				lblChickenArray[i].setLocation(lblChickenArray[i+1].getLocation());
		}
		cA.animatingChickenSetting(key, m_timer, lblChickenAfterMove);
	}

	public void gameReadyTimer() { // ���� ���� �� 3�� �غ� Ÿ�̸� �޼ҵ�
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			
			public void run() {
				answerFeedback.setVisible(false);
				if (nPreCount == 1) {			
					//3, 2, 1 
					countDown.setVisible(false);
					canPress = true;
					m_timer.cancel();
					// gameContinue();
				} else {
					//���� ����
					canPress = false;
					nPreCount--;
				}
				countDown.setText(Integer.toString(nPreCount));
			}
		};
		m_timer.schedule(m_task, 000, 1000);
	}// ���� ���� �� 3, 2, 1 ī��Ʈ �ٿ� �غ� �Լ�

	public void gameTimer() { // ���� 30�� ���ѽð� Ÿ�̸� �޼ҵ�
		
		m_timer = new Timer();
		m_task = new TimerTask() {
			
			public void run() {
				answerFeedback.setVisible(true);
				if (nTimer == 0) {
					btnFried.setEnabled(false);
					btnSeasoned.setEnabled(false);
					m_timer.cancel();
					stopSound();
					gameContinue();
				} else {
					if (nTimer == GAMETIME) {
						chickenImageSetting(nSetting, 0);
						playSound(gameBGM);
					}
					btnFried.setEnabled(true);
					btnSeasoned.setEnabled(true);
					nTimer--;
				}
				lblTimer2.setText("" + nTimer);
			}
		};

		m_timer.schedule(m_task, 3000, 1000); // 3�� �� ����, 1�ʸ��� run ����
		System.out.println(m_timer);

	}// ���� �÷��� �ð��� �����ϴ� Ÿ�̸� �Լ�! 30���� �����ؼ� ��� �پ���

	private class arrowKeyListener extends KeyAdapter { // ���� ������ ���� Ű �Է��� �޾��ִ� �޼ҵ�

		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			if (canPress == true) {
				if (key == KeyEvent.VK_LEFT && chickenArray[nNext] == 0) { //�Ķ��̵�, ����
					nScore++; // ���� ���� �� ����
					lblScore2.setForeground(Color.black);
					lblScore2.setText("" + nScore);
					System.out.println("11");
					answer = 1;
					choosingChicken.setIcon(pushLeft_Icon);
				} else if (key == KeyEvent.VK_LEFT && chickenArray[nNext] == 1) { //�Ķ��̵�, ����
					nScore--;
					answer = 2;
					lblScore2.setForeground(Color.red);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushLeft_Icon);
					System.out.println("22");
				} else if (key == KeyEvent.VK_RIGHT && chickenArray[nNext] == 0) { // ���, ����
					nScore--;
					answer = 2;
					lblScore2.setForeground(Color.red);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushRight_Icon);
					System.out.println("33");
				} else if (key == KeyEvent.VK_RIGHT && chickenArray[nNext] == 1) { // ���, ����
					nScore++; // ���� ���� �� ����
					answer = 1;
					lblScore2.setForeground(Color.black);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushRight_Icon);
					System.out.println("44");
				}

				nSetting++;
				chickenImageSetting(nSetting, key);
				nNext++;

				if (answer == 1) { //������ �� good �ִϸ��̼�
					System.out.println("shouldn't see this string");
					answerFeedback.setIcon(good_Icon);
					// �ִϸ��̼� ���
					cA.animatingAnswerFeedBack(m_timer);
					System.out.println("good");
				} else if (answer == 2) { //������ �� bad �ִϸ��̼�
					answerFeedback.setIcon(bad_Icon);
					// �ִϸ��̼� ���
					cA.animatingAnswerFeedBack(m_timer);
				}
			}
		}
	} // Ű���� �Է����ε� ������ �����ϰ�
}