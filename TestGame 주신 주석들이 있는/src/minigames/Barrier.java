package minigames;

class Barrier{ 
	int x;
	int y;
	
	double speed; // ��ֹ� �̵� �ӵ� ������ �߰�

	Barrier(int x, int y, int speed ) { // Barrier ��ü �����ڷ�, ��ֹ��� ��ġ, �ӵ��� �Ķ���ͷ� �޾� �ν��Ͻ� ������ �����Ѵ�
		this.x = x;		// ��ֹ��� x��ǥ
		this.y = y;		// ��ֹ��� y��ǥ
		
		this.speed = speed;
		// ��ü ������ �ӵ� ���� �޴´�
	}
	public void move(){ 
		x -= speed;// ��ֹ��̵��ӵ���ŭ �̵�
	}
}
