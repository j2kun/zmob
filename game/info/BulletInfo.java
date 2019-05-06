package game.info;

public class BulletInfo extends SpriteInfo{

	public double angle;
	public int x0, y0;
	
	public BulletInfo(int x, int y, double angle){
		super(x, y, -1, -1);
		this.angle = angle;
		this.x0 = x;
		this.y0 = y;
	}
}
