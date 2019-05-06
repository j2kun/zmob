package game.bullet;


import game.common.Constants;
import game.info.BulletInfo;

import java.awt.Color;
import java.awt.Graphics;

public class RocketBullet extends Bullet{

	boolean explosion;
	int frame;
	final int MAX_EXPLOSION_RADIUS = 100;
	private static final int STRENGTH = 100;
	private static final int MOVE_DIST = 5;
	private static final int RANGE = 640;
	private static final int RADIUS = 7;

	public RocketBullet(BulletInfo info){     
		super(info);	
		explosion = false;
		frame = 0;
	}

	@Override
	public void update(){     
		if(explosion)
			frame+=5;
		else
			super.update();
	}

	@Override
	public void draw(Graphics g){
		if(!explosion){
			super.draw(g);
		}
		else{
			g.setColor(Color.ORANGE);
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
		return super.shouldRemove() || 	frame > MAX_EXPLOSION_RADIUS;
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
		return Constants.ROCKET_LAUNCHER;
	}

	@Override
	public boolean isRocketType() {
		return true;
	}
}
