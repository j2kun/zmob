package game.info;

import static game.common.Constants.WALK;
import game.common.Constants;
import game.common.GameInfo;

public class PlayerInfo extends RotatingSpriteInfo{
	
	public boolean running;
	public double runLeft;
	public boolean up, down, left, right;
	public int range;
	public int moveDist;
	public int health;
	
	public PlayerInfo(int x, int y, int width, int height){
		super(x, y, width, height);
		runLeft = 100;
		range = 0;
		moveDist = WALK;
		health = Constants.MAX_HEALTH;
	}
	
	public int getGunX(){
		return getSpriteCenterX() + (int)Math.cos(GameInfo.mouseAngle);
	}
	
	public int getGunY(){
		return getSpriteCenterY() - (int)Math.sin(GameInfo.mouseAngle);
	}
}
