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

	private final int GAMETIME = 30; // 게임 플레이 제한시간 
	
	private JFrame 		frame; //
	private JPanel 		showUpPanel, showDownPanel, preStart; // 다음 치킨들 보여주는 패널, 현재 치킨 패널, 게임 시작 전 카운트다운 표시되는 패널
	
	private JLabel 		lblBase, scoreArea, timerArea, lblScore2, //배경 이미지 라벨, 'SCORE'표시라벨, 'TIME'표시라벨, 점수표시 라벨
						lblTimer2, countDown, choosingChicken, answerFeedback, lblChickenAfterMove;
						//타이머 표시라벨, 게임 시작 전 카운트다운, 치킨 선택, 정답 확인, 치킨 옮겨주는 애니메이션 라벨
	private JLabel[] 	lblChickenArray; // 치킨들 이미지 할당 배열
	private JButton 	btnFried, btnSeasoned; // 후라이드 버튼, 양념 버튼
	private int 		nScore = 0, nReset = 0, nPreCount = 4, answer = 3; 
	// 점수, 리셋?, 프리카운트??, answer==1 이면 정답, answer==2이면 오답, answer==3이면 초기화 
	private boolean 	canPress = false; // 입력 활성화 값
	private static int 	nTimer, nNext = 0, nSetting = 0; // 시간, 치킨 배열 인덱스
	
	private ImageIcon 	backGround_Icon, ready_Icon, fried_Icon, seosoned_Icon, 
						good_Icon, bad_Icon, friedBox_Icon, seasonedBox_Icon, 
						choosing_Icon, pushLeft_Icon,
						pushRight_Icon, scoreBox_Icon, timeBox_Icon;
						// 각종 이미지들
	private int[] 		chickenArray; // 1000개짜리 크기 치킨들 배열
	private TimerTask 	m_task; // timer 클래스가 수행할 작업
	private Timer 		m_timer; // 일정 시간을 주기로 반복적으로 특정 작업 수행하기
	private File 		gameBGM; // BGM
	private Clip 		clip; // 오디오 파일 객체
	
	
	//get/set 메소드들
	public JLabel getlblChickenArray(int i) { // 치킨 이미지 배열중 하나를 반환해주는 메소드이다
		return lblChickenArray[i];
	}
	
	public JLabel getAnswerFeedBack() // 앤서피드백을 반환해주는 메소드이다
	{
		return answerFeedback;
	}
	
	public Timer getTimer() // 타이머를 반환해주는 메소드이다
	{
		return m_timer;
	}
	
	private ChickenAnimation cA; // 치킨애니메이션 객체 선언
	private arrowKeyListener key; // 키입력 메소드 변수

	public ChickenPanel(JFrame frame) { // 게임 인스턴스 변수 초기화 생성자
		
		this.frame = frame; // 프레임 생성
		
		setPreferredSize(new Dimension(800, 800)); // 프로그램 전체 크기
		setBackground(Color.black);
		setLayout(null);

		key = new arrowKeyListener(); // 키입력 메소드 선언
		cA = new ChickenAnimation(this); // 애니메이션 메소드 선언
		
		initValues(); // 게임 초기화 실행

		ready_Icon = new ImageIcon("src/images/chick1.png"); // 대기 이미지 불러오기
		fried_Icon = new ImageIcon("src/images/chick2.png"); // 튀김 이미지 불러오기
		seosoned_Icon = new ImageIcon("src/images/chick3.png"); // 양념 이미지 불러오기
		good_Icon = new ImageIcon("src/images/good.png"); // 정답 이미지 불러오기
		bad_Icon = new ImageIcon("src/images/bad.png"); // 오답 이미지 불러오기
		friedBox_Icon = new ImageIcon("src/images/후라이드박스.png"); // 후라이드 박스 이미지 불러오기
		seasonedBox_Icon = new ImageIcon("src/images/양념박스.png"); // 양념 박스 이미지 불러오기
		choosing_Icon = new ImageIcon("src/images/치킨 옮기기.png"); // 선택 화살표 이미지 불러오기
		pushLeft_Icon = new ImageIcon("src/images/치킨 옮기기(왼쪽선택).png"); // 왼쪽 활성화 화살표 이미지 불러오기
		pushRight_Icon = new ImageIcon("src/images/치킨 옮기기(오른쪽선택).png"); // 오른쪽 활성화 화살표 이미지 불러오기
		scoreBox_Icon = new ImageIcon("src/images/스코어박스.png"); // 점수칸 배경 이미지 불러오기
		timeBox_Icon = new ImageIcon("src/images/타임박스.png"); // 남은시간 배경 이미지 불러오기
		backGround_Icon = new ImageIcon("src/images/치킨게임배경.png"); // 게임 배경 이미지 불러오기

		gameBGM = new File("src/sounds/RunBGM.wav"); // 게임 음악 불러오기
		
		lblBase = new JLabel(backGround_Icon); // 게임 전체 패널
		lblBase.setBounds(00, 00, 800, 800);
		lblBase.setBackground(Color.white);
		lblBase.setLayout(null);
		add(lblBase);
		lblBase.addKeyListener(key);

		scoreArea = new JLabel(scoreBox_Icon); // 점수 표시 패널
		scoreArea.setBounds(0, 0, 200, 200);
		scoreArea.setBackground(Color.white);
		scoreArea.setLayout(null);
		lblBase.add(scoreArea);

		timerArea = new JLabel(timeBox_Icon); // 타이머 표시 패널
		timerArea.setBounds(600, 0, 200, 200);
		timerArea.setBackground(Color.white);
		timerArea.setLayout(null);
		lblBase.add(timerArea);

		lblScore2 = new JLabel("" + nScore); // SCORE 밑 점수 표시
		lblScore2.setBounds(0, 0, 200, 200);
		lblScore2.setFont(new Font("Consoals", Font.BOLD, 60));
		lblScore2.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore2.setVisible(true);
		scoreArea.add(lblScore2);

		lblTimer2 = new JLabel("" + nTimer); // TIME 밑 남은시간 표시
		lblTimer2.setBounds(0, 0, 200, 200);
		lblTimer2.setFont(new Font("Consoals", Font.BOLD, 60));
		lblTimer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimer2.setVisible(true);
		timerArea.add(lblTimer2);

		btnFried = new JButton(friedBox_Icon); // 튀김 선택 버튼
		btnFried.setBounds(0, 500, 200, 300);
		btnFried.setFont(new Font("Consoals", Font.BOLD, 30));
		btnFried.setBackground(new Color(230, 165, 46));
		btnFried.setEnabled(false);
		btnFried.setBorderPainted(false); // 버튼 테두리 제거
		btnFried.addKeyListener(key);
		lblBase.add(btnFried);

		btnSeasoned = new JButton(seasonedBox_Icon); // 양념 선택 버튼
		btnSeasoned.setBounds(600, 500, 200, 300);
		btnSeasoned.setFont(new Font("Consoals", Font.BOLD, 30));
		btnSeasoned.setBackground(new Color(170, 71, 71));
		btnSeasoned.setEnabled(false);
		btnSeasoned.setBorderPainted(false); // 버튼 테두리 제거
		btnFried.addKeyListener(key);
		lblBase.add(btnSeasoned);
		
		showUpPanel = new JPanel(); // 다음 치킨들 표시 패널
		showUpPanel.setBounds(300, 0, 200, 800);
		showUpPanel.setBackground(Color.white);
		showUpPanel.setOpaque(false); // 투명하게!
		lblBase.add(showUpPanel);

		showDownPanel = new JPanel(); // 현재 치킨 표시 패널
		showDownPanel.setBounds(300, 270, 200, 500);
		showDownPanel.setBackground(Color.white);
		showDownPanel.setOpaque(false);
		showDownPanel.setLayout(null);
		lblBase.add(showDownPanel);

		
		choosingChicken = new JLabel(choosing_Icon); // 치킨 선택 화살표 이미지 라벨
		choosingChicken.setBounds(200, 550, 400, 250);
		choosingChicken.setBackground(Color.yellow);
		choosingChicken.setLayout(null);
		lblBase.add(choosingChicken);
		
		lblChickenArray = new JLabel[5]; // 이미지 삽입해줄 라벨들 가운데 상부 하부 패널에 각각 삽입해주기
		lblChickenArray[0] = new JLabel(ready_Icon);
		lblChickenArray[0].setBounds(0, 525 - 270, 200, 300);
		showDownPanel.add(lblChickenArray[0]);

		/////////////////////////////////////////////////////////////////////////////////////////////
		
		// 레이아웃 끔
		showUpPanel.setLayout(null);
		showDownPanel.setLayout(null);
		
		// 필요에 의해서 4개를 추가로 만듬
		cA.setChickenArrayOriginPoint(0, lblChickenArray[0].getLocation());
		for (int i = 1; i < 5; i++) {
			lblChickenArray[i] = new JLabel(ready_Icon);
			lblChickenArray[i].setBounds(0, 200 - 135 * (i - 1), 200, 300);
			// 원위치 값 정해주고
			cA.setChickenArrayOriginPoint(i, lblChickenArray[i].getLocation());
			// 패널에 넣어줌
			showUpPanel.add(lblChickenArray[i]);
		}

		// chickenAfterMove
		// 키보드 입력 후 치킨이 움직임 구현을 위한 라벨
		lblChickenAfterMove = new JLabel();
		lblChickenAfterMove.setBounds(0, 0, 200, 300);
		showDownPanel.add(lblChickenAfterMove);
		// setIcon

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// -----게임 시작 전 카운트 다운 ------
		preStart = new JPanel();
		preStart.setBounds(0, 400, 600, 200);
		preStart.setBackground(Color.green);
		preStart.setOpaque(false);
		preStart.setLayout(null);
		lblBase.add(preStart);

		countDown = new JLabel(Integer.toString(nPreCount)); // 게임 시작 전 카운트다운
		countDown.setBounds(300, 00, 200, 200);
		countDown.setFont(new Font("Consoals", Font.BOLD, 100));
		countDown.setForeground(Color.blue);
		preStart.setOpaque(false);
		countDown.setHorizontalAlignment(SwingConstants.CENTER);
		countDown.setVerticalAlignment(SwingConstants.TOP);
		countDown.setVisible(true);
		preStart.add(countDown);
		// ----------------------------

		answerFeedback = new JLabel(); // 정답 판별해주는 이미지 자리 라벨
		answerFeedback.setBounds(200, 0, 400, 152);
		answerFeedback.setHorizontalAlignment(SwingConstants.CENTER);
		answerFeedback.setVerticalAlignment(SwingConstants.CENTER);
		cA.setAnswerFeedbackPoint(answerFeedback.getLocation());
		preStart.add(answerFeedback);

		gameTimer(); 			// 게임 내부 타이머
		gameReadyTimer(); 		// 게임 시작 전 준비 타이머
		chickenLabelSetting(); 	// 치킨 배열 랜덤 생성
	}
	
	public void playSound(File sound) { // BGM 재생 메소드
		try {            
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();            
        } catch(Exception e) {            
            e.printStackTrace();
        }
	}
	
	public void stopSound() { // BGM 재생을 멈춰주는 메소드        
        if(clip!=null && clip.isRunning()){
        	clip.stop();
        	clip.close();
        }
	}
	
	public void initValues() { // 게임 초기화 메소드
		nScore = nReset = nNext = nSetting = 0; 
		nPreCount = 4; 
		answer = 3;
		canPress = false;
		nTimer = GAMETIME;
	}
	
	public void initKeyboardListener() { // 키보드 입력이 가능하도록 포커스를 패널에 맞추는 메소드
		lblBase.setFocusable(true);
		lblBase.requestFocus();
	}

	public void chickenLabelSetting() { // 치킨 배열에 값을 랜덤으로 설정해주는 메소드
		chickenArray = new int[1000];
		Random r = new Random(); // Random형 객체를 만듭니다.

		for (int i = 0; i < chickenArray.length; i++) { // 배열의 크기만큼 for문을 돌립니다.
			chickenArray[i] = r.nextInt(2);
		}
	}

	public void gameContinue() { // 게임 종료 후 점수를 알려주고 다시 실행할지 물어보는 팝업창을 띄우는 메소드
		
		int result;

		result = JOptionPane.showConfirmDialog(this, 
				"YOUR GRADE IS " + nScore + "! CONTINUE?",
				"GAME OVER!!",
				JOptionPane.YES_NO_OPTION ); // 재도전 또는 메뉴 물음 팝업

		if (result == JOptionPane.YES_OPTION) {
			//게임 다시 하기
			//게임을 다시할 때 초기화 옵션
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
			//메인 메뉴로 돌아가기
			System.out.println("NO");
			
			Rectangle r = frame.getBounds();
			frame.setSize(r.width, r.height - 1);
			frame.setSize(r.width, r.height + 1);

			frame.getContentPane().removeAll();
			frame.getContentPane().add(new MainPanel(frame));
		}
	}

	public void chickenImageSetting(int nSetting, int key) { // 치킨 배열 값을 받아서 게임 내에 이미지를 맞춰 표시해주는 메소드

		if (key != 0 && nSetting != 0) {
			lblChickenAfterMove.setIcon(lblChickenArray[0].getIcon());
			lblChickenAfterMove.setLocation(lblChickenArray[0].getLocation());
		}

		for (int i = 0; i < 4; i++) {
			Icon chickenIcon = chickenArray[nSetting + i] == 0 ? fried_Icon : seosoned_Icon;
			lblChickenArray[i].setIcon(chickenIcon);
			// 위치를 한칸씩 위로
			if(i!=0)
				lblChickenArray[i].setLocation(lblChickenArray[i+1].getLocation());
		}
		cA.animatingChickenSetting(key, m_timer, lblChickenAfterMove);
	}

	public void gameReadyTimer() { // 게임 시작 전 3초 준비 타이머 메소드
		
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
					//게임 시작
					canPress = false;
					nPreCount--;
				}
				countDown.setText(Integer.toString(nPreCount));
			}
		};
		m_timer.schedule(m_task, 000, 1000);
	}// 게임 시작 전 3, 2, 1 카운트 다운 준비 함수

	public void gameTimer() { // 게임 30초 제한시간 타이머 메소드
		
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

		m_timer.schedule(m_task, 3000, 1000); // 3초 뒤 등장, 1초마다 run 실행
		System.out.println(m_timer);

	}// 게임 플레이 시간을 제어하는 타이머 함수! 30부터 시작해서 계속 줄어든다

	private class arrowKeyListener extends KeyAdapter { // 게임 진행을 위해 키 입력을 받아주는 메소드

		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			if (canPress == true) {
				if (key == KeyEvent.VK_LEFT && chickenArray[nNext] == 0) { //후라이드, 정답
					nScore++; // 점수 증가 후 갱신
					lblScore2.setForeground(Color.black);
					lblScore2.setText("" + nScore);
					System.out.println("11");
					answer = 1;
					choosingChicken.setIcon(pushLeft_Icon);
				} else if (key == KeyEvent.VK_LEFT && chickenArray[nNext] == 1) { //후라이드, 오답
					nScore--;
					answer = 2;
					lblScore2.setForeground(Color.red);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushLeft_Icon);
					System.out.println("22");
				} else if (key == KeyEvent.VK_RIGHT && chickenArray[nNext] == 0) { // 양념, 오답
					nScore--;
					answer = 2;
					lblScore2.setForeground(Color.red);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushRight_Icon);
					System.out.println("33");
				} else if (key == KeyEvent.VK_RIGHT && chickenArray[nNext] == 1) { // 양념, 오답
					nScore++; // 점수 증가 후 갱신
					answer = 1;
					lblScore2.setForeground(Color.black);
					lblScore2.setText("" + nScore);
					choosingChicken.setIcon(pushRight_Icon);
					System.out.println("44");
				}

				nSetting++;
				chickenImageSetting(nSetting, key);
				nNext++;

				if (answer == 1) { //정답일 때 good 애니메이션
					System.out.println("shouldn't see this string");
					answerFeedback.setIcon(good_Icon);
					// 애니메이션 재생
					cA.animatingAnswerFeedBack(m_timer);
					System.out.println("good");
				} else if (answer == 2) { //오답일 때 bad 애니메이션
					answerFeedback.setIcon(bad_Icon);
					// 애니메이션 재생
					cA.animatingAnswerFeedBack(m_timer);
				}
			}
		}
	} // 키보드 입력으로도 게임이 가능하게
}