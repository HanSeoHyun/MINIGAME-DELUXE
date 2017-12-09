package minigames;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


public class ChickenAnimation extends JPanel {

	private Point answerFeedbackPoint; //굿, 배드의 초기 위치 저장
	private Point[] chickenArrayOriginPoint; //lblChickenArray의 초기 위치 저장
	private TimerTask m_task, answerFeedbackAnimation, chickenSettingAnimation;
	private ChickenPanel gamePanel;
	//private Timer m_timer;
	
	
	//get/set 메소드
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
	
	public void animatingAnswerFeedBack(Timer timer) { // 정답, 오답 이미지 표시해주는 애니메이션 메소드
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
				// 카운트 변수만큼 위 위치 조정
				answerFeedback.setLocation(answerFeedback.getLocation().x,
						(int) (answerFeedback.getLocation().y - (count * 0.3f)));

			}
		};
		// 0.016초마다 바로 재생 (60프레임)
		m_timer.schedule(answerFeedbackAnimation, 0, 17);
	}

	public void animatingChickenSetting(int key, Timer timer, JLabel lblChickenAfterMove) { // 치킨 이미지들을 한 칸씩 당기고 불러오면서 이동시키는 애니메이션 메소드
		Timer m_timer = timer;
		if (chickenSettingAnimation != null)
			chickenSettingAnimation.cancel();

		chickenSettingAnimation = new TimerTask() {
			private int count = 0;
			private int max = 10;

			// AA,지점에서 BB지점으로 이동
			private void forewordTo(Point goal, Point now, JLabel label) {
				Point A = goal;
				Point B = now;
				
				Point cPoint = new Point(A.x - B.x, A.y - B.y); //이동해야 할 거리 계산
					
				cPoint.x = (int) (cPoint.x / (float) max * count); //현재 주기에 얼만큼 이동해야하는지 계산
				cPoint.y = (int) (cPoint.y / (float) max * count); 

				label.setLocation(B.x + cPoint.x, B.y + cPoint.y);
			}

			public void run() {

				if (count != max)
					count++;
					
					// 치킨 좌우로 넣기
				Point now = chickenArrayOriginPoint[0].getLocation();
				if (key == KeyEvent.VK_LEFT)
					forewordTo(new Point(now.x - 200, now.y), now, lblChickenAfterMove);
				else if (key == KeyEvent.VK_RIGHT)
					forewordTo(new Point(now.x + 200, now.y), now, lblChickenAfterMove);
				else {
				}

				// 위치킨 당기기
				for (int i = 0; i < 4; i++) {
					// 목표지점
					Point A = chickenArrayOriginPoint[i].getLocation();
					// 이전지점
					Point B;
					if (i == 0)
						B = new Point(A.x, A.y - 250);
					else
						B = chickenArrayOriginPoint[i + 1].getLocation();

					forewordTo(A, B, gamePanel.getlblChickenArray(i));
				}  
			}
		};
		// 0.016초마다 바로 재생 (60프레임)
		try {
			m_timer.schedule(chickenSettingAnimation, 0, 17);
		} catch(Exception e) {
			
		}
	}
		
	public ChickenAnimation(ChickenPanel chickenPanel) { // 인스턴수 변수들을 초기화하는 생성자
		gamePanel = chickenPanel;
		chickenArrayOriginPoint = new Point[5];
	}
}