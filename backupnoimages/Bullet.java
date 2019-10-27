package backupnoimages;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class Bullet {

	int frame = 0, radius = 0, explosionFrame = 0, pW = 0, pH = 0;
	double sX = 0, sY = 0, sX1 = 0, sY1 = 0;
	//sx1 and sy1 store the first sx and sy of bullet for range purposes
	double angle = 0; //shottyAngle for displacement of angles for shotty
	boolean up = false, down = false, left = false, right = false;//if the ball is moving

	//pistol, machine gun, shotty, wave beam, rocket, chain gun, test
	final int[] MOVE_DIST = new int[]{10,10,7,10,5,10,10}; //move distance for each frame
	final int[] STRENGTH = new int[]{8,10,60,30,100,100,5}; //strength of each weapon
	final int[] RANGES = new int[]{282,380,180,480,640,700,700};
	//range of each weapon is 20 pixels less than the ranges array in the game panel class
		// and corresponds with currentGun number
	double shottyAngle;

	int currentGun;
	private final int PISTOL = 0, MACHINE_GUN = 1, SHOTGUN = 2, WAVE_GUN = 3,
						ROCKET = 4, CHAIN_GUN = 5, POISON_GUN = 6;
	private Rectangle rectContainingImage;

	int reflection;
	final int AMPLITUDE = 7, MAX_EXPLOSION_RADIUS = 100;
	//amplitude of the sine wave for wave beam & max radius for rocket explosion to expand to
	boolean explosion = false; //explosion for rocket launcher
	boolean removeThisBullet = false;

	private Random rand;
	private Color rndExplosionColor;
	//sends x and y postion for the ball to start in,
	//and the angle, in radians, that the ball will follow, in respect to 0
	//on a normal unit circle
	//dislpacement is the radius of the small circle, so that the bullet
	//starts at the edge of the little circle
	public Bullet(int x, int y, double ang, int displacement, int type, int w, int h)
	{
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle)*displacement);
		sY = (int)(y-Math.sin(angle)*displacement);

		sX1 = sX; sY1 = sY;//NEEDED FOR THE RANGE CALCULATIONS

		radius = 3;
		currentGun = type;//set bullet type for entire class
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);

		//do random Color for the chain gun explosion
		rand = new Random();
		int rR = rand.nextInt(125);
		int rG = rand.nextInt(125);
		int rB = rand.nextInt(75)+175;
		rndExplosionColor = new Color(rR,rG,rB);
	}

	//second constructor specifically for the shotgun
	public Bullet(int x, int y, double ang, int displacement, int type, int w, int h,
			int bulletNum)
	{
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle)*displacement);
		sY = (int)(y-Math.sin(angle)*displacement);
		currentGun = type;//set bullet type for entire class
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

	//third constructor for the wave beam
	public Bullet(int x, int y, double ang, int displacement, int type, int w, int h, boolean reflec)
	{
		angle = ang;//angle in radians
		pW = w; pH = h;//pass over boundaries
		sX = (int)(x+Math.cos(angle)*displacement);
		sY = (int)(y-Math.sin(angle)*displacement);

		sX1 = sX; sY1 = sY;

		radius = 3;
		currentGun = type;//set bullet type for entire class
		rectContainingImage = new Rectangle((int) sX, (int) sY, radius, radius);

		reflection = reflec ? 1 : -1;
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

	public void checkBoundaries(){
		if(sX <= 0){
			sX = 0;
		}
		else if (sX >= pW - radius){
			sX = pW - radius;
		}
		if (sY <= 0){
			sY = 0;
		}
		else if(sY >= pH - radius){
			sY = pH - radius;
		}
	}

	public void draw(Graphics g, Container container)
	{
		g.setColor(Color.BLACK);
		if(currentGun==PISTOL || currentGun == MACHINE_GUN)
		{
			g.fillOval((int)getSX(), (int)getSY(), radius, radius);
			setRectangle((int)getSX(), (int)getSY(), radius, radius);
		}
		else if(currentGun==SHOTGUN)
		{
			g.fillOval((int)getSX(), (int)getSY(),
					radius+2, radius+2);
			setRectangle((int)getSX(), (int)getSY(),
					radius+2, radius+2);
		}
		else if(currentGun==WAVE_GUN)
		{
			int i = 0;
			if(reflection == 1)
				i = 1;

			g.fillOval((int)getSX(), (int)getSY(),
					radius+i*3, radius+i*3);
			setRectangle((int)getSX(), (int)getSY(),
					radius+i*3, radius+i*3);
		}
		else if(currentGun==ROCKET || currentGun == CHAIN_GUN)
		{
			if(!explosion)
			{
				g.fillOval((int)getSX(), (int)getSY(), radius+4, radius+4);
				setRectangle((int)getSX(), (int)getSY(), radius+4, radius+4);
			}
			else
			{
				if(currentGun == CHAIN_GUN)
					g.setColor(rndExplosionColor);
				else
					g.setColor(Color.ORANGE);
				//draw the explosion as an expanding oval
				g.fillOval((int)getSX()-explosionFrame/2, (int)getSY()-explosionFrame/2,
					    radius+explosionFrame, radius+explosionFrame);
				//explosion frame increases every time move() is called
				setRectangle((int)getSX()-explosionFrame/2, (int)getSY()-explosionFrame/2,
						    radius+explosionFrame, radius+explosionFrame);
			}
		}
		if(currentGun==POISON_GUN)
		{
			g.setColor(Color.WHITE);
			g.fillOval((int)getSX(), (int)getSY(), radius+3, radius+3);
			setRectangle((int)getSX(), (int)getSY(), radius+3, radius+3);
		}
	}

	//this is the method that contains the various algorithms for moving
	//the bullets.
	public void move()
	{
		frame++;
		double tempX = 0, tempY = 0;
		//this is for the regular shooting algorithm

		if(currentGun == PISTOL || currentGun==MACHINE_GUN)//for pistol and machine gun
		{
			tempX = getSX();
			tempY = getSY();

			//use simple trig to align movement of ball to mouse angle
			tempX += Math.cos(angle)*MOVE_DIST[currentGun];
			tempY -= Math.sin(angle)*MOVE_DIST[currentGun];

			sX = tempX;
			sY = tempY;
		}

		if(currentGun == SHOTGUN)//for shotty
		{
			tempX = getSX();
			tempY = getSY();

			//use simple trig to align movement of ball to mouse angle
			tempX+=Math.cos(angle+(shottyAngle))*MOVE_DIST[currentGun];
			tempY-=Math.sin(angle+(shottyAngle))*MOVE_DIST[currentGun];

			sX = tempX;
			sY = tempY;
		}

		//the stuff below is testing new weapon algorithms


        /**
         * Wave beam
         *
		 * To rotate a point x,y to an angle theta, multiply the matrices
		 * [cos(theta), -sin(th)] * [t] where t is the frame #
		 * [sin(theta),  cos(th)]   [y] where y = sin(t)
		 *
		 * to get x(rotated) = t*cos(theta) - sin(t)*sin(theta)
		 *        y(rotated) = t*sin(theta) + sin(t)*cos(theta)
		 *
		 * the following is the derivative of the above parametric function
		 * to get a rotated sine curve to a fixed angle
		 */
		if(currentGun == WAVE_GUN)
		{
			tempX = getSX();
			tempY = getSY();

			//use trig to align movement of ball to mouse angle
			tempX+= MOVE_DIST[currentGun]*Math.cos(angle) -
		 	  AMPLITUDE*reflection*Math.sin(angle)*(Math.cos(frame));

			tempY-= MOVE_DIST[currentGun]*Math.sin(angle) +
			  AMPLITUDE*reflection*Math.cos(angle)*(Math.cos(frame));

			sX = tempX;
			sY = tempY;
		}

		if(currentGun == ROCKET)
		{
			if(explosion)
				explosionFrame+=5;
			else //if exploding, do not move the bullet anymore, instead just iterate
			{
				tempX = getSX();
				tempY = getSY();

				//use simple trig to align movement of ball to mouse angle
				tempX += Math.cos(angle)*MOVE_DIST[currentGun];
				tempY -= Math.sin(angle)*MOVE_DIST[currentGun];

				sX = tempX;
				sY = tempY;
			}
		}

		if(currentGun == CHAIN_GUN)
		{
			if(explosion)
				explosionFrame+=3;
			else //if exploding, do not move the bullet anymore, instead just iterate
			{
				tempX = getSX();
				tempY = getSY();

				//use simple trig to align movement of ball to mouse angle
				tempX += Math.cos(angle)*MOVE_DIST[currentGun];
				tempY -= Math.sin(angle)*MOVE_DIST[currentGun];

				sX = tempX;
				sY = tempY;
			}
		}

		if(currentGun == POISON_GUN)
		{
			tempX = getSX();
			tempY = getSY();

			/*
			 * the function moves along a rotated sin curve,
			 * whose amplitude increases as a function of
			 * 4*t^2/3, where t time
			 */

			tempX+=MOVE_DIST[currentGun]*Math.cos(angle) -
				Math.sin(angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));
		 	 // Math.sin(angle)*(Math.pow(Math.cbrt(frame),2))*(Math.cos(frame));

			tempY-=MOVE_DIST[currentGun]*Math.sin(angle) +
				Math.cos(angle)*(4*Math.pow(frame,2.0/3.0))*(Math.cos(frame));
			  //Math.cos(angle)*(Math.pow(Math.cbrt(frame),2))*(Math.cos(frame));

			sX = tempX;
			sY = tempY;
		}
	}

	public int getStrength()
	{return STRENGTH[currentGun];}

	public double getSX()
	{return sX;}

	public double getSY()
	{return sY;	}

	public double getStartingSX()
	{return sX1;}

	public double getStartingSY()
	{return sY1;}

	public int getRadius()
	{return radius;}

	public int getRange()
	{return RANGES[currentGun];}

	public void setExplosion(boolean e)
	{
		explosion = e;
		explosionFrame = 0;
		//start iterating variable with each frame
	}

	public boolean isExplosion()
	{return explosion;}

	public boolean checkRemove()
	{
		boolean b = false;

		//if bullet is out of the frame
		if(getSX()>pW || getSX()< 0-getRadius()*2 || getSY()>pH || getSY()<0-getRadius()*2)
			b = true;
		else if(explosionFrame > MAX_EXPLOSION_RADIUS)
		{	//or if the explosion from a rocket is finished
			b = true;
		}

		return b;
	}

	public int getBulletType()
	{
		return currentGun;
	}

}
