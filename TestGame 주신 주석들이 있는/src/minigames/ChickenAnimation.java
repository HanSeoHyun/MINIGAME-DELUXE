package minigames;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


public class ChickenAnimation extends JPanel {

	private Point answerFeedbackPoint; //��, ����� �ʱ� ��ġ ����
	private Point[] chickenArrayOriginPoint; //lblChickenArray�� �ʱ� ��ġ ����
	private TimerTask m_task, answerFeedbackAnimation, chickenSettingAnimation;
	private ChickenPanel gamePanel;
	//private Timer m_timer;
	
	
	//get/set �޼ҵ�
	public Point[] getChickenArrayOriginPoint() {
		return chickenArrayOriginPoint;	
	}
	
	public void setChickenArrayOriginPoint(Point[] pointArray)
	{
		chickenArrayOriginPoint = pointArray;
	}
	
	public void setAnswerFeedbackPoint(Point p) {
		answerFeedbackPoint = p;
	}
	
	public void setChickenArrayOriginPoint(int i, Point n)
	{
		chickenArrayOriginPoint[i] = n;
	}
	
	public void animatingAnswerFeedBack(Timer timer) { // ����, ���� �̹��� ǥ�����ִ� �ִϸ��̼� �޼ҵ�
		System.out.println("feedback animation called");
		Timer m_timer = timer;
		if (answerFeedbackAnimation != null)
			answerFeedbackAnimation.cancel();

		answerFeedbackAnimation = new TimerTask() {

			private int count = 0;
			private JLabel answerFeedback = gamePanel.getAnswerFeedBack();

			public void run() {
				if (count == 0) {
					answerFeedback.setLocation(answerFeedbackPoint);
				}

				count++;
				// ī��Ʈ ������ŭ �� ��ġ ����
				answerFeedback.setLocation(answerFeedback.getLocation().x,
						(int) (answerFeedback.getLocation().y - (count * 0.3f)));

			}
		};
		// 0.016�ʸ��� �ٷ� ��� (60������)
		m_timer.schedule(answerFeedbackAnimation, 0, 17);
	}

	public void animatingChickenSetting(int key, Timer timer, JLabel lblChickenAfterMove) { // ġŲ �̹������� �� ĭ�� ���� �ҷ����鼭 �̵���Ű�� �ִϸ��̼� �޼ҵ�
		Timer m_timer = timer;
		if (chickenSettingAnimation != null)
			chickenSettingAnimation.cancel();

		chickenSettingAnimation = new TimerTask() {
			private int count = 0;
			private int max = 10;

			// AA,�������� BB�������� �̵�
			private void forewordTo(Point goal, Point now, JLabel label) {
				Point A = goal;
				Point B = now;
				
				Point cPoint = new Point(A.x - B.x, A.y - B.y); //�̵��ؾ� �� �Ÿ� ���
					
				cPoint.x = (int) (cPoint.x / (float) max * count); //���� �ֱ⿡ ��ŭ �̵��ؾ��ϴ��� ���
				cPoint.y = (int) (cPoint.y / (float) max * count); 

				label.setLocation(B.x + cPoint.x, B.y + cPoint.y);
			}

			public void run() {

				if (count != max)
					count++;
					
					// ġŲ �¿�� �ֱ�
				Point now = chickenArrayOriginPoint[0].getLocation();
				if (key == KeyEvent.VK_LEFT)
					forewordTo(new Point(now.x - 200, now.y), now, lblChickenAfterMove);
				else if (key == KeyEvent.VK_RIGHT)
					forewordTo(new Point(now.x + 200, now.y), now, lblChickenAfterMove);
				else {
				}

				// ��ġŲ ����
				for (int i = 0; i < 4; i++) {
					// ��ǥ����
					Point A = chickenArrayOriginPoint[i].getLocation();
					// ��������
					Point B;
					if (i == 0)
						B = new Point(A.x, A.y - 250);
					else
						B = chickenArrayOriginPoint[i + 1].getLocation();

					forewordTo(A, B, gamePanel.getlblChickenArray(i));
				}  
			}
		};
		// 0.016�ʸ��� �ٷ� ��� (60������)
		try {
			m_timer.schedule(chickenSettingAnimation, 0, 17);
		} catch(Exception e) {
			
		}
	}
		
	public ChickenAnimation(ChickenPanel chickenPanel) { // �ν��ϼ� �������� �ʱ�ȭ�ϴ� ������
		gamePanel = chickenPanel;
		chickenArrayOriginPoint = new Point[5];
	}
}