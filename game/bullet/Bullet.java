package game.bullet;

import game.Managed;
import game.common.GameInfo;
import game.info.BulletInfo;
import game.sprite.BaseSprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class Bullet extends BaseSprite<BulletInfo> implements Managed{

	public abstract int getMoveDist();
	public abstract int getStrength();
	public abstract int getRange();
	public abstract int getRadius();
	public abstract boolean isRocketType();
	public abstract String getType();
	
	protected int radius, strength, moveDist, range;
	protected Color defaultBulletColor = Color.WHITE;

	protected boolean up, down, left, right;
	protected Rectangle rectContainingImage;

	public Bullet(BulletInfo paramInfo){
		super(paramInfo);
		info.x = (int)(info.x+Math.cos(info.angle));
		info.y = (int)(info.y-Math.sin(info.angle));

		moveDist = getMoveDist();
		range = getRange();
		strength = getStrength();
		radius = getRadius();
		
		rectContainingImage = new Rectangle(info.x, info.y, radius, radius);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(defaultBulletColor);
		g.fillOval(info.x, info.y, radius, radius);
		setRectangle(info.x, info.y, radius, radius);
	}
	
	public void draw(Graphics g, Color c){
		g.setColor(c);
		g.fillOval(info.x, info.y, radius, radius);
		setRectangle(info.x, info.y, radius, radius);
	}
	
	//use simple trig to align movement of ball to mouse angle
	public void update(){
		info.x += Math.cos(info.angle)*moveDist;
		info.y -= Math.sin(info.angle)*moveDist;
	}
	
	public double getSX() {
		return info.x;
	}

	public double getSY() {
		return info.y;
	}

	public double getStartingSX() {
		return info.x0;
	}

	public double getStartingSY() {
		return info.y0;
	}

	public boolean shouldRemove() {
		return isOutsideRange() || (info.x <= 0 || info.x >= GameInfo.pW - radius*2 ||
				info.y <= 0 || info.y >= GameInfo.pH - radius*2);
	}

	public void setRectangle(int x, int y, int rad, int radi) {
		rectContainingImage = new Rectangle(x, y, rad, radi);
	}

	public Rectangle getRectangle() {
		return rectContainingImage;
	}
	
	public List<Bullet> toList(){
		ArrayList<Bullet> list = new ArrayList<Bullet>();
		list.add(this);
		return list;
	}
	
	public boolean isOutsideRange(){
		double x1 = info.x0;
		double x2 = info.x;
		double y1 = info.y0;
		double y2 = info.y;
		double hypotenuse = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
		
		if(hypotenuse>=range)//if outside allocated range
			if(this instanceof Explodable){	
				Explodable expl = (Explodable)this;
				if(!expl.isExplosion())
					expl.setExplosion(true);//if out of range, explode, will be removed later
			}
			else
				return true; //remove all other bullets
		return false;
	}
}