package game.bullet;


import game.common.Constants;
import game.info.BulletInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class WavebeamBullet extends Bullet{

	private static final Random rand = new Random();
	private int reflection;
	private static final int AMPLITUDE = 7;
	private static final int STRENGTH = 40;
	private static final int MOVE_DIST = 10;
	private static final int RANGE = 480;
	private static final int RADIUS = 3;
	private Color rndColor;
	private int frame;

	public WavebeamBullet(BulletInfo info, boolean reflec){     
		super(info);

		frame = 0;
		reflection = reflec ? 1 : -1;

		int rR = 255;
		int rG = rand.nextInt(255);
		int rB = 1;
		rndColor = new Color(rR,rG,rB);
	}

	@Override
	public void update(){
		frame++;
		//use matrices to align movement of ball to mouse angle
		info.x += moveDist*Math.cos(info.angle) - 
			AMPLITUDE*reflection*Math.sin(info.angle)*(Math.sin(frame));
		info.y -= moveDist*Math.sin(info.angle) +
			AMPLITUDE*reflection*Math.cos(info.angle)*(Math.sin(frame));
	}

	@Override
	public void draw(Graphics g){
		g.setColor(rndColor);
		int radius = getRadius();
		
		g.fillOval(info.x, info.y, radius, radius);
		setRectangle(info.x, info.y, radius, radius);
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
		return RADIUS + 3 * ((reflection == 1) ? 1 : 0) ;
	}
	
	@Override
	public String getType() {
		return Constants.WAVE_BEAM;
	}

	@Override
	public boolean isRocketType() {
		return false;
	}
}
