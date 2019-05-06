package game.bullet;


import game.common.Constants;
import game.info.BulletInfo;

import java.awt.Color;
import java.awt.Graphics;

public class PoisonBullet extends Bullet{

	int frame;
	private static final int STRENGTH = 5;
	private static final int MOVE_DIST = 10;
	private static final int RANGE = 700;
	private static final int RADIUS = 6;

	public PoisonBullet(BulletInfo info){     
		super(info);
		frame = 0;
	}

	@Override
	public void update(){     
		frame++;
		info.x += moveDist*Math.cos(info.angle) - 
		Math.sin(info.angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));

		info.y -= moveDist*Math.sin(info.angle) + 
		Math.cos(info.angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));
	}

	@Override
	public void draw(Graphics g){
		super.draw(g, Color.WHITE);
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
		return Constants.POISON_GUN;
	}

	@Override
	public boolean isRocketType() {
		return false;
	}
}