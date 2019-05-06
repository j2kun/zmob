package backupnoimages;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class BigBall {

	int sX = 0, sY = 0, radius = 0, pW = 0, pH = 0;
	int barL, barW, barX, barY;
	boolean up = false, down = false, left = false, right = false;//if the ball is moving
	boolean running;
	double runLeft = 100;//amount of frames the player can run
	
	//						horizontal and veritcal spacing of the bar
	final int MAX_RUN = 100, BAR_V_SPACING = 25, BAR_H_SPACING = 20;
	final double RUN_DEPLETION = 1.0, RUN_RECHARGE = .5;
	final int WALK = 4, RUN = 10;
	int moveDist = WALK; //move speed changes if running
	Polygon shape;
	Rectangle rect;
	//private final double CONVERT_TO_DEGREES = Math.PI/180;
	private int currentRange = 0;
	private Point thePoint;
	
	public BigBall(int w, int h)
	{
		pW = w;
		pH = h;
		sX = pW/2;
		sY = pH/2;
		radius = 20;
		
		running = false;
		rect = new Rectangle(sX, sY, radius, radius);
	}
	
	//draw method draws sprite, stamina bar, and range circle
	public void draw(Graphics g, boolean paused){
		
		checkBoundaries();//check bounds of panel
		
		configureMoveDist(paused);
		
		//simple moving algorithm
		if(up)
			sY-=moveDist;
		if(down)
			sY+=moveDist;
		if(left)
			sX-=moveDist;
		if(right)
			sX+=moveDist;
		
		setRectangle();//update rectangle every frame		
		setPoint();
		
		g.setColor(Color.BLACK);
		g.fillOval(sX, sY, radius, radius);//draw the image
		
		//draw the range circle//
		g.setColor(Color.BLUE);
		g.drawOval(getSX()-getRange(), getSY()-getRange(), 
				getRange()*2, getRange()*2);
	}
	
	public void configureMoveDist(boolean paused)
	{
		if (!paused) {
			//running algorithm			
			if (running && (up || down || left || right))//if running and moving any direction
			{
				moveDist = RUN;
				subtractStamina(RUN_DEPLETION);
			} else {
				//if(!(up && down && left && right))
				moveDist = WALK;
				addStamina(RUN_RECHARGE);
			}
		}		
	}
	
	//works, but may be the cause of errors
	/*public Polygon createPolygon(int sX, int sY, int radius)
	{
		shape = null; shape = new Polygon();
		int numSides = 8; //how many sides we want on the polygon
		//algorithm here
		for (double i = 0; i<(2*Math.PI); i+=(Math.PI/numSides))
		{
			int x = getSX()+(int)(Math.cos(i)*radius/2);
			int y = getSY()-(int)(Math.sin(i)*radius/2);
			shape.addPoint(x,y);
		}
		
		return shape;
	}*/
	
	public Point getPoint()
	{
		return thePoint;
	}
	
	public void setPoint(){
		thePoint = new Point(getSX(), getSY());
	}
	
	public void setRectangle(){
		checkBoundaries();
		rect = new Rectangle(sX, sY, radius, radius);
		//createPolygon(sX,sY,radius);
	}
	
	
	public Rectangle getRectangle(){
		return rect;
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
	
	//returns middle of the circle
	public int getSX()
	{return sX+(radius/2);}
	
	public int getSXCORNER()
	{return sX;}
	
	public int getSY()
	{return sY+(radius/2);}
	
	public int getSYCORNER()
	{return sY;}
	
	public int getRadius()
	{return radius;}
	
	public void resetPosition()
	{
		sX = pW/2;
		sY = pH/2;
	}
	
	public void addStamina(double amt)
	{
		runLeft += amt;
		
		if(runLeft>100)
			runLeft = 100;
	}
	
	public void subtractStamina(double amt)
	{
		runLeft-=amt;
		
		if(runLeft < 0)
		{
			runLeft = 0;
			running = false;
			moveDist = WALK;
		}
	}
	
	public void setRunning(boolean rawr)
	{
		running = rawr;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void setRange(int r)
	{
		currentRange = r;
	}
	
	public int getRange()
	{
		return currentRange;
	}
	
	public double getStamina()
	{
		return runLeft;
	}
	
	public void stopSprite()
	{
		up = false;
		down = false;
		right = false;
		left = false;
	}
}
