package testpackage;

import java.awt.Graphics;

import javax.swing.JFrame;

// main�޼ҵ尡 �ִ� Ŭ������, jframe ��ü�� �����Ͽ� ���α׷��� ����ǵ��� �Ѵ�.
public class MainClass {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("MINIGAME DELUXE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JFrame ��ü ���� �� �ݱ� ���� ����
		
		MainPanel mainPanel = new MainPanel(frame);
		// ó�� ���� �޴� ������ �ϴ� �г��� ����. JFrame�� �����ϱ� ���� frame�� ���ڷ� �����Ѵ�.
		
		frame.getContentPane().add(mainPanel);
		// frame�� mainPanel�� �߰��Ѵ�.
		
		frame.pack();
		frame.setVisible(true);
	}

}
