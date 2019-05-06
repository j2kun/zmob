package backup;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

abstract class Bullet {
	
	public Color defaultBulletColor = Color.WHITE;
	public int radius, pW, pH, strength, moveDist, range;
	double sX, sY, //current posn of the bullet
		sX1, sY1, //original posn of the bullet
		angle; //angle at which the bullet travels
	//sx1 and sy1 store the first sx and sy of bullet for range purposes
	boolean up = false, down = false, left = false, right = false;//if the ball is moving
	
	public final int PISTOL = 0, MACHINE_GUN = 1, SHOTGUN = 2, 
	WAVE_BEAM = 3, ROCKET_LAUNCHER = 4, CHAIN_GUN = 5, POISON_GUN = 6;	
	public Rectangle rectContainingImage;
	
	public double getSX()
	{return sX;}
	
	public double getSY()
	{return sY;	}
	
	public double getStartingSX()
	{return sX1;}
	
	public double getStartingSY()
	{return sY1;}
	
	public void checkBoundaries()
	{
		if(sX <= 0)
			sX = 0;
		else if (sX >= pW - radius)
			sX = pW - radius;
		
		if (sY <= 0)
			sY = 0;
		else if(sY >= pH - radius)
			sY = pH - radius;
	}	
	
	public boolean checkRemove()
	{
		//if bullet is out of the frame or explosion is finished
		return (getSX()>pW || getSX()< 0-getRadius()*2 || getSY()>pH || getSY()<0-getRadius()*2);
	}
	
	public void setExplosion(boolean e)
	{
		System.out.println("set explosion called on a non-rocket");
		throw new UnsupportedOperationException("not a chain gun or rocket launcher!(setExplosion)");
	}
	
	public boolean isExplosion()
	{
		System.out.println("set explosion called on a non-rocket");
		throw new UnsupportedOperationException("not a chain gun or rocket launcher!(isExplosion)");
	}

	public void setRectangle(int x, int y, int rad, int radi)
	{
		checkBoundaries();
		rectContainingImage = new Rectangle(x, y, rad, radi);
		//System.out.println(rectContainingImage);
	}
	
	public Rectangle getRectangle(){
		return rectContainingImage;
	}

	public int getRadius()
	{return radius;}
	
	public abstract void move();
	public abstract void draw(Graphics g, Container container);
	public abstract int getType();
	public abstract int getStrength();
	public abstract int getRange();
}

//for pistol
class PistolBullet extends Bullet{
	
	int moveDist = 10;
	int strength = 9;
	int range = 282;
	
	public PistolBullet(int x, int y, double ang, int w, int h)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS
		
		radius = 3;
		rectContainingImage = new Rectangle((int)sX, (int) sY, radius, radius);
	}
	
	public void move()
	{	
		//use simple trig to align movement of ball to mouse angle
		sX += Math.cos(angle)*moveDist;
		sY -= Math.sin(angle)*moveDist;
	}
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(defaultBulletColor);
		g.fillOval((int)sX, (int)sY, radius, radius);
		setRectangle((int)sX, (int)sY, radius, radius);
	}
	
	public int getType()
	{return PISTOL;}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}

//for machine gun
class MGBullet extends Bullet{
	
	int moveDist = 10;
	int strength = 10;
	int range = 382;
	
	public MGBullet(int x, int y, double ang, int w, int h)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS
		
		radius = 3;
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
	}
	
	public void move()
	{	
		//use simple trig to align movement of ball to mouse angle
		sX += Math.cos(angle)*moveDist;
		sY -= Math.sin(angle)*moveDist;
	}
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(defaultBulletColor);
		g.fillOval((int)sX, (int)sY, radius, radius);
		setRectangle((int)sX, (int)sY, radius, radius);
	}
	
	public int getType()
	{return MACHINE_GUN;}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}

//for shotgun
class ShotgunBullet extends Bullet{
	
	double shottyAngle;
	int moveDist = 7;
	int strength = 60;
	int range = 180;
	
	public ShotgunBullet(int x, int y, double ang, int w, int h, 
			int bulletNum)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
		radius = 3;
		
		sX1 = sX; sY1 = sY;
		
		switch(bulletNum)
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
		}		
	}
	
	public void move()
	{		
		//use simple trig to align movement of ball to mouse angle
		sX += Math.cos(angle+(shottyAngle))*moveDist;
		sY -= Math.sin(angle+(shottyAngle))*moveDist;
	}
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(defaultBulletColor);
		g.fillOval((int)sX, (int)sY,radius+2, radius+2);
		setRectangle((int)sX, (int)sY, radius+2, radius+2);
	}
	
	public int getType()
	{return SHOTGUN;}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}

//for wave beam
class WavebeamBullet extends Bullet{
	
	int reflection;
	final int AMPLITUDE = 7;
	int moveDist = 10;
	int strength = 40;
	int range = 480;
	int frame;
	Random rand;
	Color rndColor;
	
	public WavebeamBullet(int x, int y, double ang, int w, int h, boolean reflec)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;
		
		frame = 0;
		radius = 3;
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
		
		reflection = reflec ? 1 : -1;
		
		rand = new Random();
		int rR = 255;
		int rG = rand.nextInt(255);
		int rB = 1;
		rndColor = new Color(rR,rG,rB);
	}
	
	public void move()
	{
		frame++;
		//use matrices to align movement of ball to mouse angle
		sX += moveDist*Math.cos(angle) - 
	 	  		AMPLITUDE*reflection*Math.sin(angle)*(Math.sin(frame));
		sY -= moveDist*Math.sin(angle) + 
		  		AMPLITUDE*reflection*Math.cos(angle)*(Math.sin(frame));
	}
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(rndColor);
		
		int i = 0;
		if(reflection == 1)
			i = 1;
		
		g.fillOval((int)sX, (int)sY,radius+i*3, radius+i*3);
		setRectangle((int)sX, (int)sY,radius+i*3, radius+i*3);
	}
	
	public int getType()
	{return WAVE_BEAM;}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}

class RocketBullet extends Bullet{
	
	boolean explosion;
	int frame;
	int moveDist = 5;
	int strength = 100;
	int range = 640;
	final int MAX_EXPLOSION_RADIUS = 100;
	
	public RocketBullet(int x, int y, double ang, int w, int h)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS
		
		radius = 3;
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
		explosion = false;
		frame = 0;
	}
	
	public void move()
	{		
		//use simple trig to align movement of ball to mouse angle
		if(explosion)
			frame+=5;
		else //if not exploding, do not move the bullet anymore, instead just iterate
		{
			sX += Math.cos(angle)*moveDist;
			sY -= Math.sin(angle)*moveDist;
		}
	}
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(defaultBulletColor);
		if(!explosion)
		{
			g.fillOval((int)sX, (int)sY, radius+4, radius+4);
			setRectangle((int)sX, (int)sY, radius+4, radius+4);
		}
		else
		{
			g.setColor(Color.ORANGE);
			//draw the explosion as an expanding oval 
			g.fillOval((int)sX-frame/2, (int)sY-frame/2, radius+frame, radius+frame);	
			//explosion frame increases every time move() is called
			setRectangle((int)sX-frame/2, (int)sY-frame/2, radius+frame, radius+frame);
		}
	}
	
	@Override
	public void setExplosion(boolean e)
	{
		explosion = e;
		frame = 0;
		//start iterating variable with each frame
	}
	
	@Override
	public boolean isExplosion()
	{return explosion;}
	
	public int getType()
	{return ROCKET_LAUNCHER;}
	
	public boolean checkRemove()
	{
		//if bullet is out of the frame or explosion is finished
		return ((getSX()>pW || getSX()< 0-getRadius()*2 || getSY()>pH || getSY()<0-getRadius()*2) 
			|| (frame > MAX_EXPLOSION_RADIUS));
	}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
	
}

class ChainBullet extends Bullet{
	
	boolean explosion;
	int frame;
	Random rand;
	Color rndExplosionColor;
	final int MAX_EXPLOSION_RADIUS = 100;
	
	int moveDist = 10;
	int strength = 100;
	int range = 700;
	
	public ChainBullet(int x, int y, double ang, int w, int h)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS
		
		radius = 3;
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
		explosion = false;
		frame = 0;
		
		//		do random Color for the chain gun explosion
		rand = new Random();
		int rR = rand.nextInt(125);
		int rG = rand.nextInt(125);
		int rB = rand.nextInt(75)+175;
		rndExplosionColor = new Color(rR,rG,rB);	
	}
	
	
	public void move()
	{
		frame++;		
		//use simple trig to align movement of ball to mouse angle
		if(explosion)
			frame+=3;
		else //if exploding, do not move the bullet anymore, instead just iterate
		{
			sX += Math.cos(angle)*moveDist;
			sY -= Math.sin(angle)*moveDist;
		}
	}
	
	
	public void draw(Graphics g, Container container)
	{
		g.setColor(defaultBulletColor);
		if(!explosion)
		{
			g.fillOval((int)sX, (int)sY, radius+4, radius+4);
			setRectangle((int)sX, (int)sY, radius+4, radius+4);
		}
		else
		{
			g.setColor(rndExplosionColor);
			//draw the explosion as an expanding oval 
			g.fillOval((int)sX-frame/2, (int)sY-frame/2, radius+frame, radius+frame);	
			//explosion frame increases every time move() is called
			setRectangle((int)sX-frame/2, (int)sY-frame/2, radius+frame, radius+frame);
		}
	}
	
	@Override
	public void setExplosion(boolean e)
	{
		explosion = e;
		frame = 0;
		//start iterating variable with each frame
	}
	
	@Override
	public boolean isExplosion()
	{return explosion;}
	
	public int getType()
	{return CHAIN_GUN;}
	
	public boolean checkRemove()
	{
		//if bullet is out of the frame or explosion is finished
		return ((getSX()>pW || getSX()< 0-getRadius()*2 
				|| getSY()>pH || getSY()<0-getRadius()*2) 
					|| (frame > MAX_EXPLOSION_RADIUS));
	}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}

class PoisonBullet extends Bullet{
	
	int moveDist = 10;
	int strength = 5;
	int range = 700;
	int frame;
	
	public PoisonBullet(int x, int y, double ang, int w, int h)
	{		
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle));
		sY = (int)(y-Math.sin(angle));
		
		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS
		frame = 0;
		radius = 3;
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);
	}
	
	public void move()
	{		
		frame++;
		sX += moveDist*Math.cos(angle) - 
			Math.sin(angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));
		// Math.sin(angle)*(Math.pow(Math.cbrt(frame),2))*(Math.cos(frame));
	
		sY -= moveDist*Math.sin(angle) + 
			Math.cos(angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));
	  //Math.cos(angle)*(Math.pow(Math.cbrt(frame),2))*(Math.cos(frame));
	}

	
	public void draw(Graphics g, Container container)
	{
		g.setColor(Color.WHITE);
		g.fillOval((int)sX, (int)sY, radius+3, radius+3);
		setRectangle((int)sX, (int)sY, radius+3, radius+3);
	}
	
	
	public int getType()
	{
		return POISON_GUN;
	}
	
	public int getStrength()
	{return strength;}
	
	public int getRange()
	{return range;}
}