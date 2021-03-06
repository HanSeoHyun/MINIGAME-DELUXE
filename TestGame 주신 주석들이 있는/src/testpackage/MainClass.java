package testpackage;

import java.awt.Graphics;

import javax.swing.JFrame;

// main메소드가 있는 클래스로, jframe 객체를 생성하여 프로그램이 실행되도록 한다.
public class MainClass {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("MINIGAME DELUXE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JFrame 객체 생성 및 닫기 동작 설정
		
		MainPanel mainPanel = new MainPanel(frame);
		// 처음 메인 메뉴 역할을 하는 패널의 생성. JFrame에 접근하기 위해 frame을 인자로 전달한다.
		
		frame.getContentPane().add(mainPanel);
		// frame에 mainPanel을 추가한다.
		
		frame.pack();
		frame.setVisible(true);
	}

}
