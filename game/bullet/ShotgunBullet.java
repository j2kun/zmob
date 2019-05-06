package game.bullet;


import game.common.Constants;
import game.info.BulletInfo;

import java.awt.Graphics;

public class ShotgunBullet extends Bullet{

	private double shottyAngle;
	private static int bulletNum = 0;
	private static final int STRENGTH = 60;
	private static final int MOVE_DIST = 7;
	private static final int RANGE = 180;
	private static final int RADIUS = 5;

	public ShotgunBullet(BulletInfo info){     
		super(info);
		
		switch(bulletNum++)
		{
		case 0:
			shottyAngle = -0.4;
			break;
		case 1:
			shottyAngle = -0.2;
			break;
		case 2:
			shottyAngle = 0.0;
			break;
		case 3:
			shottyAngle = 0.2;
			break;
		case 4:
			shottyAngle = 0.4;
			break;
		default:
			shottyAngle = 0.0;
		}

		if(bulletNum > 4)
			bulletNum = 0;
	}

	@Override
	public void update()
	{     
		//use simple trig to align movement of ball to mouse angle
		info.x += Math.cos(info.angle+(shottyAngle))*moveDist;
		info.y -= Math.sin(info.angle+(shottyAngle))*moveDist;
	}

	@Override
	public void draw(Graphics g){
		super.draw(g);
	}
	
	@Override
	public int getMoveDist() {
		return MOVE_DIST;
	}

	@Override
	public int getStrength() {
		return STRENGTH;
	}

	@Override
	public int getRange() {
		return RANGE;
	}

	@Override
	public int getRadius() {
		return RADIUS;
	}
	
	@Override
	public String getType() {
		return Constants.SHOTGUN;
	}

	@Override
	public boolean isRocketType() {
		return false;
	}
}