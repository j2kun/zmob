package game.bullet;


import game.common.Constants;
import game.info.BulletInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class ChainBullet extends Bullet implements Explodable{

	private boolean explosion;
	private int frame;
	private Random rand;
	private Color rndExplosionColor;
	private static final int MAX_EXPLOSION_RADIUS = 100;
	private static final int STRENGTH = 100;
	private static final int MOVE_DIST = 10;
	private static final int RANGE = 700;
	private static final int RADIUS = 7;

	public ChainBullet(BulletInfo info)
	{     
		super(info);		
		explosion = false;
		frame = 0;

		//    do random Color for the chain gun explosion
		rand = new Random();
		int rR = rand.nextInt(125);
		int rG = rand.nextInt(125);
		int rB = rand.nextInt(75)+175;
		rndExplosionColor = new Color(rR,rG,rB);  
	}

	@Override
	public void update()
	{
		frame++;    
		if(explosion)//expand when exploding
			frame+=3;
		else
			super.update();
	}

	@Override
	public void draw(Graphics g){
		if(!explosion)
			super.draw(g);
		else
		{
			g.setColor(rndExplosionColor);
			//draw the explosion as an expanding oval 
			g.fillOval(info.x-frame/2, info.y-frame/2, radius+frame, radius+frame);  
			//explosion frame increases every time move() is called
			setRectangle(info.x-frame/2, info.y-frame/2, radius+frame, radius+frame);
		}
	}

	public void setExplosion(boolean e){
		explosion = e;
		frame = 0;
	}
	
	public boolean isExplosion(){
		return explosion;
	}

	@Override
	public boolean shouldRemove(){
		return super.shouldRemove()	|| (frame > MAX_EXPLOSION_RADIUS);
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
		return Constants.CHAIN_GUN;
	}

	@Override
	public boolean isRocketType() {
		return true;
	}
}
