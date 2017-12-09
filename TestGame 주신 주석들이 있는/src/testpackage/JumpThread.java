package testpackage;

public class JumpThread implements Runnable{
	
	private RunningGamePanel gf; // game frame instance
	private Thread myThread;
	private volatile boolean running = false; // threads see this variables straight from memory
	
	private float valueJump;
	
	public JumpThread(RunningGamePanel gf)	// 점프스레드의 생성자로, 게임 패널 객체를 인스턴스변수에 저장한다.
	{
		this.gf = gf;	
	}
	
	public void start()	// 점프스레드를 실행한다.
	{
		if(myThread==null)	// 스레드가 null 일땐
			myThread = new Thread(this); // 스레드를 생성한다
		myThread.start();	// run메소드(점프) 호출
	}
	
	public void stop()	// 점프스레드의 작동을 멈춘다.
	{
		myThread.stop();
	}
	
	public void join()	// 하나의 스레드가 다른 스레드가 하는 일이 완료될 때 까지 기다리도록 할 때 쓰인다.
	{
		try {
			myThread.join();	// 현재 돌아가고 있는 스레드는  myThread가 완료 될 때 까지 기다리게 만든다. 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	// 에러 메세지의 발생 근원지를 찾아서 차례대로 출력한다.
		}
	}
	
	public boolean isRunning()	// 점프스레드가 동작중인지의 여부를 반환한다.
	{
		return running;
	}
	
	public void run()	// 점프스레드의 스레드 동작을 구현한 메소드이다. 플레이어의 프레임 별 위치를 갱신한다.
	{
		running = true;	// 점프스레드를 작동 상태로 만들어줌
		valueJump = 19;	// 초기 valueJump값 설정
		while(running)	
		{
			try {
				while(true)
				{
					gf.player_y = gf.player_y - valueJump;	// 플레이어의 y값이 점프 하는것 처럼 보이도록 한다.
					valueJump -= 0.8;		// 플레이어의 점프를 조정하기 위해 valueJump를 감소시킨다
					if(gf.player_y > 710)	// 플레이어의 크기는 (90,90)이기 때문에, 플레이어의 y 좌표가 710보다 크면 화면에서 벗어나기 때문에 
					{
						gf.player_y = 710;	// 플레이어를 지면에 밀착 시킨다 (플레이어의 y좌표의 최대값을 710로 해준다)
						gf.setJumpCount(0); // 점프 카운트를 0 (점프안함) 상태로 만들어준다
						running = false;	// 점프가 끝났기 때문에 점프스레드는 작동이 끝난상태이다
						break;
					}
					Thread.sleep(15);
				}
			}
			catch(Exception e) {	// 예외일때는
				running = false;	// 점프가 안되는 상태로 만든다
			}	
		}
	}
}
