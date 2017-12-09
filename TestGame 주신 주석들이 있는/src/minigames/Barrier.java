package minigames;

class Barrier{ 
	int x;
	int y;
	
	double speed; // 장애물 이동 속도 변수를 추가

	Barrier(int x, int y, int speed ) { // Barrier 객체 생성자로, 장애물의 위치, 속도를 파라미터로 받아 인스턴스 변수에 저장한다
		this.x = x;		// 장애물의 x좌표
		this.y = y;		// 장애물의 y과표
		
		this.speed = speed;
		// 객체 생성시 속도 값을 받는다
	}
	public void move(){ 
		x -= speed;// 장애물이동속도만큼 이동
	}
}
