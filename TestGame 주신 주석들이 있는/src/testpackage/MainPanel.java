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

// 처음 화면에 등장하는 메뉴 화면의 JPanel 이다.
// 치킨게임, 러닝게임 둘 중 하나를 선택할 수 있도록 화면 좌 우에 두 개의 버튼을 갖는다.
public class MainPanel extends JPanel {	
	private JButton btnChickenGame;
	private JButton btnRunningGame;
	// 각 게임을 실행할 버튼들이다.
	
	private JFrame mainFrame;
	// 생성자가 인자로 받을 JFrame 변수이다. 이를 통해 생성되있는 JFrame에 접근한다.
	
	private ImageIcon backgroundImage;
	// 메뉴 화면의 배경 이미지를 저장할 변수이다.
	
	private ChickenPanel chkPanel;
	private RunningGamePanel rngPanel;
	// 긱 게임들의 객체를 저장할 변수들이다. 두 게임 모두 JPanel을 상속하므로 클래스명, 변수명 모두 Panel로 표기했다.
	
	private MainPanel mp;
	// 자기 자신(MainPanel)을 저장할 변수이다.
	
	public MainPanel(JFrame frame) {
		mainFrame = frame;
		mp = this;
		//인자로 받은 JFrame과 자기 자신의 객체를 각자의 instance변수들에 저장한다.
		
		setLayout(null);
		setBackground(Color.cyan);
		// 패널의 레이아웃을 없애고, 바탕색을 설정한다.
		
		backgroundImage = new ImageIcon("src/images/MainMenuBackground.png");
		JLabel lblBackground = new JLabel(backgroundImage);
		lblBackground.setBounds(0, 0, 800, 800);
		add(lblBackground);
		// 바탕 이미지를 JLabel 변수를 생성하며 넣어주고, 이를 패널에 추가하여 바탕화면으로 설정한다.
		
		setPreferredSize(new Dimension(800, 800));
		// 패널의 크기를 설정한다.
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(0, 300, 800, 800);
		add(buttonPanel);
		// 버튼들이 바탕이미지를 갖고있는 JLabel 컴포넌트보다 앞으로 랜더링 되도록 패널 하나를 생성한다.
		// 이 패널에 버튼들이 추가되고, 바탕 JLabel보다 앞에 뜨도록 설정한다.
		
		btnChickenGame = new JButton(new ImageIcon("src/images/chickenGameButton.png"));
		btnChickenGame.setBounds(90, 0, 300,400);
		btnChickenGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnChickenGame.addActionListener(new GameChoiceListener());
		btnChickenGame.setBackground(new Color(150, 225, 209));
		btnChickenGame.setBorderPainted(false);
		buttonPanel.add(btnChickenGame);
		// 치킨 게임을 실행할 버튼을 생성하고 버튼 패널에 추가한다.
		// 버튼의 이미지를 설정하고, 위치, 바탕색, 테두리 유무를 설정한다.
		// 리스너를 추가하여 버튼의 동작을 가능케 한다.
		
		btnRunningGame = new JButton(new ImageIcon("src/images/runningGameButton.png"));
		btnRunningGame.setBounds(410, 0, 300, 400);
		btnRunningGame.setFont(new Font("Calibri", Font.BOLD, 25));
		btnRunningGame.addActionListener(new GameChoiceListener());
		btnRunningGame.setBackground(new Color(150, 225, 209));
		btnRunningGame.setBorderPainted(false);
		buttonPanel.add(btnRunningGame);
		// 러닝 게임을 실행할 버튼을 생성하고 버튼 패널에 추가한다.
		// 버튼의 이미지를 설정하고, 위치, 바탕색, 테두리 유무를 설정한다.
		// 리스너를 추가하여 버튼의 동작을 가능케 한다.
		
		setComponentZOrder(lblBackground, 1);
		setComponentZOrder(buttonPanel, 0);
		// 바탕이 밑으로 가게하고, 버튼 패널이 위로 가게한다.
	}
	
	// 패널 내 버튼들이 동작하도록 리스너를 내부 클래스로 구현하였다.
	// 생성자에서 생성되어 버튼에 추가된다.
	private class GameChoiceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) { // 상속한 인터페이스의 메소드를 구현한다.
			// TODO Auto-generated method stub
			Object obj = e.getSource(); // 이벤트를 발생시킨 오브젝트의 소스를 Object변수로 받는다.
			
			if(obj == btnChickenGame) { // 이벤트가 치킨게임 버튼이었다면
				chkPanel = new ChickenPanel(mainFrame);
				// 치킨 게임 패널을 생성한다.
				System.out.println("check");
				mainFrame.getContentPane().removeAll();				
				mainFrame.getContentPane().add(chkPanel);
				// frame에 있던 컴포넌트들을 모두 제거하고, 치킨 게임 패널을 추가한다.
				chkPanel.initKeyboardListener();
				// 치킨 게임 패널의 키보드 리스너를 생성 및 포커싱을 하여 키보드 입력이 가능하도록 한다.
				
			}
			else if(obj == btnRunningGame) { // 이벤트가 러닝게임 버튼이었다면
				Rectangle r = mainFrame.getBounds();				
				mainFrame.setSize(r.width, r.height - 1);
				mainFrame.setSize(r.width, r.height + 1);
				// 러닝게임을 실행할 때 repaint가 제대로 실행이 되질 않아 적용한 대안으로,
				// 처음에 프레임의 사이즈를 1픽셀 리사이징한 후 다시 원래 크기로 되돌려 강제로 repaint하도록 했다.
				// 이로써 러닝게임이 제대로 화면에 뜨도록 하였다.
				
				rngPanel = new RunningGamePanel(mainFrame);
				// 러닝 게임 패널을 생성한다.
				mainFrame.getContentPane().removeAll();
				mainFrame.getContentPane().add(rngPanel);
				// frame에 있던 컴포넌트들을 모두 제거하고, 러닝 게임 패널을 추가한다.
				rngPanel.initKeyboardListener();
				// 러닝 게임 패널의 키보드 리스너를 생성 및 포커싱을 하여 키보드 입력이 가능하도록 한다.
			}
		}		
	}
}
