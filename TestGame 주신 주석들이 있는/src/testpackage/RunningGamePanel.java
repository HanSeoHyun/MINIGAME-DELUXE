package testpackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class RunningGamePanel extends JPanel implements Runnable, KeyListener{


	private JFrame 				frame;			// ������ ��ü
	public  float 				player_x, player_y;	// �÷��̾�(ĳ����)�� x,y ��ǥ
	private float 				barrier_speed; 	// player, barrier speed
	private int 				f_width, f_height;	// screen's width & height excluding window components
	private int 				back_x = 0; 	// ��ü ��� ��ũ�� �� ����

	private int 				cnt;			// ��ֹ������� �ӵ��� ������ �� �ִ� ���� 	
	private int 				player_JumpCount;	// ĳ������ ������ ī��Ʈ�ؼ�  ����x,1,2�� ���� ���¸� ��������
	private int 				game_Score; 	// ���� ���� ���
	private boolean 			KeyUp, crashed;	// �����̽��ٸ� ������ �������� �˷��ִ� ��, �浹���θ� �˷��ִ� ��
	
	private JumpThread 			jt;				// ���� �����带 ���� ��ü
	private Thread 				th;				// ���� �����带 ���� ��ü
	private Toolkit 			tk = Toolkit.getDefaultToolkit();	//�̹����� �ҷ����� ���� ��Ŷ
	private Image 				Player_img;		// �÷��̾�(ĳ����)�̹���
	private Image 				BackGround_img; // ���ȭ�� �̹���
	private Image 				Barrier_img; 	// ��ֹ� �̹���
	private ArrayList<Barrier>	Barrier_List;	// ��ֹ��� ����ؼ� ���������ִ� �迭
	private Image 				buffImage; 		// �̹����� buffImage ��ü
	private Graphics 			buffg;			// �׷��Ƚ��� buffg ��ü
	private Barrier 			en; 			// ��ֹ��� �̵��ӵ� ��ü
	private File 				jumpEfx, bgm;	// �����Ҷ�ȿ����, ���ӹ���� �� ���� ��ü
	private Clip				bgmClip;		// ����� ���� ��ü

	public void initKeyboardListener()	// Ű���� �Է��� �����ϵ��� ��Ŀ���� �����ִ� �޼ҵ��̴�.
	{
		this.addKeyListener(this);	// ���� ��ü���� ��밡���ϵ���
		this.setFocusable(true);	// Ű���� �Է��� �����ϵ��� ����
		this.requestFocusInWindow();// ��Ŀ���� ���Ƿ� �޵��� �Ѵ�
	}

	public RunningGamePanel(JFrame frame)	// RunningGamePanel Ŭ���� �������̴�. �ν��Ͻ� ���� �ʱ�ȭ�� ������ ������ �Ѵ�.
	{		
		init();		// �ʱ�ȭ �޼ҵ� ȣ��
		start();	// ������ �ٽý����ϴ� �޼ҵ� ȣ��

		this.frame = frame; 	
		this.setPreferredSize(new Dimension(f_width, f_height));
		// ���� ���������� ���ư� �� �ְ� �ϱ� ���ؼ�
	} // RunningGamePanel

	public void stop() // ���� �����带 �����Ѵ�.
	{
		th.stop();	// th��ü�� �����带 ����
	}

	public void init() // �����ʱ�ȭ�� �Ѵ�.
	{		
		System.out.println("initiating commencing");	
		cnt 		= 0;		// ��ֹ� �������ϴ� ���� ����
		back_x 		= 0;		// ����� x��ǥ
		player_x 	= 100;		// ĳ������ x��ǥ
		player_y 	= 710;		// ĳ������ y��ǥ 
		f_width 	= 800;		// ������â�� �ʺ�
		f_height 	= 800;		// ������ â�� ����

		KeyUp 		= false;	// �����̽� �ٸ� ������ ������� �߻��ϴ� ���� 
		crashed 	= false;	// ó���� �浿���θ� false�� �д�
		jt 			= new JumpThread(this);	// jt��ü�� ������ ��Ű������ ���������带 �����Ѵ�

		Barrier_List = new ArrayList<Barrier>();	// ��ֹ��� �� �迭 ����

		Barrier_img = new ImageIcon("src/images/barrier_real.png").getImage();
		// ��ֹ� �̹����� �����Ѵ�.
		
		Player_img 	= new ImageIcon("src/images/player_alive.png").getImage();
		// �÷��̾�(ĳ����) �̹����� �����Ѵ�

		BackGround_img = new ImageIcon("src/images/background_.png").getImage();
		// ��ü ���ȭ�� �̹����� �����Ѵ�.

		game_Score 	= 0;//���� ���ھ� �ʱ�ȭ
		//		player_Speed = 10; //���� ĳ���� �����̴� �ӵ� ����
		player_JumpCount = 0; // �÷��̾� ���� Ƚ�� �ʱ�ȭ

		barrier_speed = 7;	//��ֹ��� ������� �ӵ� ����	
		
		jumpEfx = new File("src/sounds/jump_sound.wav");	// �����Ҷ� ������ ���� ����
		bgm = new File("src/sounds/RunBgm.wav");			// ������� ����
		try {
			bgmClip = AudioSystem.getClip();	// ����� �����̳� ����� ��Ʈ���� ����� ����� �� �ִ� Ŭ���� ����մϴ�.
		} catch (LineUnavailableException e) {
			e.printStackTrace();	// ���� �޼����� �߻� �ٿ����� ã�Ƽ� ���ʴ�� ����Ѵ�.
		}
		playSound(bgm, bgmClip);
	}

	public void start(){	// ���� �����带 �����Ѵ�.
		th = new Thread(this);
		th.start();	// run�޼ҵ�(������ �帧)�� ȣ��
	} // start

	public void run(){ 	// ���� �����尡 �۾��� �͵��� ������ �޼ҵ��̴�.
		try{ 
			while(!crashed){	// �浹�� �ȵȻ����̸�
				KeyProcess(); 		
				BarrierProcess();

				repaint(); 	//  ȭ���� �ٽ� �׷���

				Thread.sleep(15);	// 0.015�� ��ŭ �����带 ���缭 ������ ���ǵ带 �����Ѵ� 
				cnt++;				// Ư������ ������ ��ֹ��� �����ϰ��ϴ� cnt������ ������Ų��
			}
			stopSound(bgmClip);	// �浹�� �Ǿ��ٸ� ������ �����ش�
			gameContinue();		// ���� ����� ����
		} catch (Exception e){}
	} // run

	public void gameContinue()	// ���ӿ����� �Ǹ� ������ �ٽ��� ������, �޴��� �ǵ��ư� �������� ���� â�� ���� �޼ҵ��̴�. 
	{
		int result;

		result = JOptionPane.showConfirmDialog(this, 	// ���� �����ӿ��� ���
				"YOUR SCORE IS " + game_Score + "! CONTINUE?",	// ����� ����
				"GAME OVER", 									// Ÿ��Ʋ		
				JOptionPane.YES_NO_OPTION,		// YES, NO �ΰ��� ���û���
				JOptionPane.PLAIN_MESSAGE);		// �˸�â�� �޽��� ������ ����
		
		if(result == JOptionPane.YES_OPTION) {	// Ȯ��â�� YES��ư�� ������
			System.out.println("YES");
			init();		// ���� �ʱ�ȭ �޼ҵ� ȣ��
			start();	// ������ �ٽ� �����ϴ� �޼ҵ� ȣ��
		}
		else if(result == JOptionPane.NO_OPTION) {	// Ȯ��â�� NO��ư�� ������
			System.out.println("NO");

			Rectangle r = frame.getBounds();	
			frame.setSize(r.width, r.height - 1);	// frame�� ũ������
			frame.setSize(r.width, r.height + 1);	// frame�� ũ������

			frame.getContentPane().removeAll();		// ������ ����� �Ǵ� ������ �� �ֵ��� �����̳ʿ� �ִ� ��� ������Ʈ�� �����ش�
			frame.getContentPane().add(new MainPanel(frame));	//
		} else {
			System.out.println("CANCEL");
		}
	} // gmaeContinue()

	public void BarrierProcess(){	// ���ӿ� �����ϴ� ��ֹ����� ����/�Ҹ�, ��ġ ���� ���� ����ϴ� �޼ҵ��̴�.

		for (int i = 0 ; i < Barrier_List.size() ; ++i )
		{ 
			en = (Barrier)(Barrier_List.get(i)); // �迭�� i��° �ε����� ��ü�� en ��ü�� ����ش�
			en.move(); 		// �ش� ��ֹ��� �������� �̵���Ų�� ( Bariier Ŭ��������  x��ǥ�� ���ҽ�Ų��)
			if(en.x < -200)	// ��ֹ��� Ư�� ������ �����
			{ 
				Barrier_List.remove(i); 	// ��ֹ��� �����Ѵ�
			}

			if(Crash(player_x, player_y, en.x, en.y, Player_img, Barrier_img))
			{	// �÷��̾��� x,y��, ��ֹ��� x,y��, �÷��̾� �̹���, ��ֹ� �̹����� �޾Ƽ�  
				// �浹 �����̸�
				Player_img = new ImageIcon("src/images/player_dead.png").getImage(); // change player's image when died
				crashed = true;	// �浹����
			}
		} // for

		if ( cnt % 100 == 0 )	// run���� �߻��ϴ� cnt����
		{ 
			if(cnt % 200 == 0)
				barrier_speed += 0.5f;	// ���߿� ������ ��ֹ��� ���ǵ带 �ణ ������Ų��
			int nRandom = (int)(Math.random()*12); // 0 ~ 11
			// 1�� ��ֹ� : 2�� ��ֹ� : ������ֹ�  (������ ����) = 5/12 : 4/12 : 3/12
			if(0<=nRandom && nRandom <= 2) {
				en = new Barrier(f_width + 190, 650, (int)barrier_speed); // frame�� width + 190, 700�� ù��° ��ֹ��� ������ x,y ��ǥ
			}
			else if(nRandom == 3 || nRandom == 4) {
				en = new Barrier(f_width + 115, 650, (int)barrier_speed); // frame�� width + 100, 700�� ù��° ��ֹ��� ������ x,y ��ǥ
			}
			else if(nRandom == 5 || nRandom == 6) {
				en = new Barrier(f_width + 235, 550, (int)barrier_speed); // ,, 2��° ��ֹ��� ������ x,y ��ǥ
			} 
			else if(nRandom == 7 || nRandom == 8) {
				en = new Barrier(f_width + 190, 550, (int)barrier_speed); // ,, 2��° ��ֹ��� ������ x,y ��ǥ
			} else {
				en = new Barrier(f_width + -5, 0, (int)barrier_speed); // ,, �ι�° ��ֹ��� ������ x,y ��ǥ	
			}
			Barrier_List.add(en);	// ��ֹ� �迭�� en�� add ���Ѽ� ������ ��ֹ��� �迭�� 
		} // if
	} // BarrierProcess

	public boolean Crash(float x1, float y1, float x2, float y2, Image img1, Image img2){
	// �÷��̾� ������Ʈ�� ��ֹ��� ���� ������ �����ϴ����� �Ǻ��ϰ�, �̿� ����	 �浹���θ� ��ȯ�ϴ� �޼ҵ��̴�.

		//���� �̹��� ������ �ٷ� �޾� �ش� �̹����� ����, ���̰��� �ٷ� ����ϴ� �ڵ�.
		// x1 : ĳ������ x��,	y1: ĳ������ y��
		// x2 : ��ֹ��� x��,	y2: ��ֹ��� y��
		// img1 : �÷��̾� �̹���, 	img2 : ��ֹ� �̹���
		boolean check = false;	// ��ֹ��� �⵿ �ߴ��� ���ߴ����� üũ�Ѵ�(�ʱ⿡�� üũ �ȵ� ����)

		if ( Math.abs( ( x1 + img1.getWidth(null) / 2 )  	
				- ( x2 + img2.getWidth(null) / 2 ))  
				< ( img2.getWidth(null) / 2 + img1.getWidth(null) / 2 )
				&& Math.abs( ( y1 + img1.getHeight(null) / 2 )  
						- ( y2 + img2.getHeight(null) / 2 ))  
				< ( img2.getHeight(null)/2 + img1.getHeight(null)/2 ) ) // if
		{
		// ���밪�� �����ִ� abs�Լ��� �̿��Ͽ� �̹��� ����, ���̰��� �ٷ� �޾� ����մϴ�.


			check = true;	// ĳ���Ͱ� ��ֹ��� �浹������(if�� �ȿ� ���� true��) check�� true�� �����մϴ�.
		}else{ check = false;}	// �浹 �������� ��� üũ���� false

		return check; // ��ֹ� �浹 üũ���� ������ �ִ� check�� ���� �޼ҵ忡 ���� ��ŵ�ϴ�.
	} // Crash


	public void paint(Graphics g){	
	// �� �޼ҵ�� repaint�޼ҵ尡 �Ҹ� �� �ڵ����� �Ҹ��� �Լ���, �� ������Ʈ���� ���ŵ� ���ο� ��ġ�� �°� �׸��� �׸����� �����ǵǾ���.
		Draw_Background(g); // ��� �̹��� �׸��� �޼ҵ� ����
		Draw_Player(g); 	// �÷��̾� �̹����� �׸��� �޼ҵ� ����
		Draw_Barrier(g);	// ��ֹ� �̹����� �׸��� �޼ҵ� ����
		Draw_StatusText(g);	//���� ǥ�� �ؽ�Ʈ�� �׸��� �޼ҵ� ����
	}

	public void update(Graphics g){
	// �� �޼ҵ�� repaint�޼ҵ尡 �Ҹ� �� �ڵ����� �Ҹ��� �Լ���, ������۸��� ���� �ٽ� ������ �Ǿ���.
		buffImage = createImage(f_width, f_height); 
		buffg = buffImage.getGraphics();
		paint(buffg);	
		// buffg ���ִ� �̹����� �׷��־� repaint�ÿ��� ȭ���� �������� �ʰ����ش�
		g.drawImage(buffImage, 0, 0, this); 
	}

	public void Draw_Background(Graphics g){
		//��� �̹����� �׸��� �κ�

		if(back_x > -5600) {
			// �⺻ ���� 0�� back_x(����̹����� x��ǥ)�� -5600 ���� ũ�� ����
			g.drawImage(BackGround_img, back_x, 0, this);
			// drawIamge(Image img, int x, int y,ImageObserver observer);
			g.drawImage(BackGround_img, back_x + 5600, 0, this);
		} else { 
			back_x = 0;	// ����� ó�� x��ǥ�� ������ ����� ��� �������� �Ѵ�
		}

		back_x -= 3;	//back_x�� 0���� -3��ŭ ��� ���̹Ƿ� ����̹����� x��ǥ��
						//��� �������� �̵��Ѵ�. �׷��Ƿ� ��ü ����� õõ�� �������� �����̰� �ȴ�.
		game_Score += 1;	// -3 ��ŭ �����϶� ���� game_Score ���� ������Ų��
	} // Draw_Background

	public void Draw_Player(Graphics g) { 	
	// �� �޼ҵ�� paint�޼ҵ忡�� �Ҹ���. �� �����Ӹ��� ���ŵ� ��ġ�� �÷��̾� ������Ʈ�� �׸���.
		g.drawImage(Player_img, (int)player_x, (int)player_y, this);
	}

	public void Draw_Barrier(Graphics g) { 	
	// �� �޼ҵ�� paint�޼ҵ忡�� �Ҹ���. �� �����Ӹ��� ���ŵ� ��ġ�� ��ֹ����� �׸���.
		for (int i = 0 ; i < Barrier_List.size() ; ++i ){
			en = (Barrier)(Barrier_List.get(i));
			g.drawImage(Barrier_img, en.x, en.y, this);
		}
	}

	public void Draw_StatusText(Graphics g){ 
	// �� �޼ҵ�� paint�޼ҵ忡�� �Ҹ���. �� �����Ӹ��� ���ŵ� �������̺��� �׸���. ( ���� ���ھ� ��� �ؽ�Ʈ )

		g.setFont(new Font("Defualt", Font.BOLD, 20));
		// ��Ʈ ������ �մϴ�.  �⺻��Ʈ, ����, ������ 20
		String strFormat = String.format("SCORE : %06d M", game_Score);
		// ���ӽ��ھ ��� ���ִ� ����
		g.drawString(strFormat, 600, 70);
		// ��ǥ x : 600, y : 70�� ���ھ ǥ���մϴ�.
	}

	public void KeyProcess(){	
	// �� �������� ���ŵɶ����� �Ҹ��� ��ǲ�� �޴� �޼ҵ��̴�.
	// �����̽��ٰ� ������ ��� �÷��̾� ������Ʈ�� ���� �ִϸ��̼��� �����ϸ�, 2�� �̻��� �Է��� �����ϴ� ���ҵ� �Ѵ�.
		
		// System.out.println(KeyUp + ", " + player_JumpCount);
		if(KeyUp == true && player_JumpCount<=2) 
		{	// �����̽��� ������ �� �����̰�, ������ 2�� ���� �����̸�
			if(jt.isRunning())	// �����ϰ� �ִ� ���¶��
			{
				jt.stop();	// ���������带 �����Ų��
			}
			jt = new JumpThread(this);	// ���ο� ���������� ����
			jt.start();	// ���� �����忡 �ִ� start �޼ҵ带 ȣ��
			playSound(jumpEfx);	// �����Ҷ� ����ȿ���� �ش�
		}

		KeyUp = false;	// �����̽��� ���� �ʱ�ȭ
	} // KeyProcess

	public void setJumpCount(int n)
	// �÷��̾��� ����Ƚ���� �Ķ���ͷ� ���� �� ��ŭ �ʱ�ȭ�ϴ� �޼ҵ��̴�. �÷��̾ �ٴڿ� ����� �� �ҷ� ���� Ƚ���� 0ȸ�� �ʱ�ȭ�ϴµ� ���ȴ�.
	{
		player_JumpCount = n;	//0��(��������), 1�� , 2�� ������ ���¸� �־��ش�
	}

	@Override
	public void keyPressed(KeyEvent arg0) {	
	// keyListener �������̽��� �޼ҵ带 ������ ������,�����̽��ٸ� ������ �� ����Ƚ���� key�� ���ȴ����� boolean������ �����Ѵ�.
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE)	// �����̽� Ű�� ������
		{
			player_JumpCount++;	// ����ī��Ʈ ����
			KeyUp = true;		// �����̽��� ������ �� ����
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	// keyListener �������̽��� �޼ҵ带 ������ ������, �� �޼ҵ带 �������� �ʿ䰡 �����Ƿ� ���� �ʾҴ�.
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {	
	// keyListener �������̽��� �޼ҵ带 ������ ������,�� �޼ҵ带 �������� �ʿ䰡 �����Ƿ� ���� �ʾҴ�.
		// TODO Auto-generated method stub

	}
	
	public void playSound(File sound)
	// ���������� ����ϴ� �޼ҵ��̴�. �Ķ���ͷ� ���� ���������� ����ϸ�,
	// ������ǰ� ���� �߰��� ���ߴ� ���� ������ �ʿ��� ������ ���
	// clip�� �Ķ���ͷ� �޾� �̸� �����ϵ��� �����ε��� �޼ҵ��̴�.
	{
		try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);	// sound file �κ��� ����� �Է� Stream�� ����մϴ�.
            Clip clip = AudioSystem.getClip();	//  ����� �����̳� ����� ��Ʈ���� ����� ����� �� �ִ� Ŭ���� ����մϴ�.
            clip.open(stream);		// stream ���� ������ ����, ������ �ʿ��� system resource�� ȹ���� ���� �����ϰ� �ǵ���(����) �Ѵ�.
            clip.start();		// start �޼��带 ����ϸ�, �������� ������ ��ġ�κ��� ����� �簳�ȴ�.
            
        } catch(Exception e) {            
            e.printStackTrace();	// ���� �޼����� �߻� �ٿ����� ã�Ƽ� ���ʴ�� ����Ѵ�.
        }
	}
	
	public void playSound(File sound, Clip clip) {	// ���������� ����ϴ� �޼ҵ��̴�. �Ķ���ͷ� ���� ���������� ����Ѵ�.
		try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(sound);	// sound file �κ��� ����� �Է� Stream�� ����մϴ�.
            clip.open(stream);		// stream ���� ������ ����, ������ �ʿ��� system resource�� ȹ���� ���� �����ϰ� �ǵ���(����) �Ѵ�.
            clip.start();		// start �޼��带 ����ϸ�, �������� ������ ��ġ�κ��� ����� �簳�ȴ�.
            
        } catch(Exception e) {            
            e.printStackTrace();	// ���� �޼����� �߻� �ٿ����� ã�Ƽ� ���ʴ�� ����Ѵ�.
        }
	}

	public void stopSound(Clip clip){	// ������� ���������� �����ϴ� �޼ҵ��̴�.

		if(clip!=null && clip.isRunning()){		// ���������� ����ǰ� �ְų� ���������尡 ������ �̶��
			System.out.println("just stop!");
			clip.stop();	// ���带 �����.
			clip.close();	// ������ �ݾ� �������� ����ϰ� �ִ� system resource�� �ع� �� �� �ִ� ���� ��Ÿ����.
		}
	}
}
