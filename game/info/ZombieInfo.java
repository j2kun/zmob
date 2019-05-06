package game.info;

public class ZombieInfo extends RotatingSpriteInfo{

	public int health;
	public boolean dead;
	public double angle;
	
	public ZombieInfo(int x, int y, int width, int height){
		super(x, y, width, height);
		dead = false;
		angle = 0;
	}
	
}
