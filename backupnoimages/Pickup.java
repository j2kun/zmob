package backupnoimages;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Pickup {
	
	byte me;
	public final int HEALTH = 0, AMMO = 1;
	public final int RECOVERY = 25;
	final String[] types = new String[] {"health", "ammo"};
	private final int S = 21;
	private int currentPickupType = 0;
	private int pW, pH, sX, sY;
	Random rand;
	
	private Rectangle rectContainingImage;
	private Point middleOfSprite;
	
	public Pickup()
	{
		rand = new Random();
		currentPickupType = rand.nextInt(1);
	}
	
	public Pickup(int type, int w, int h)
	{
		currentPickupType = type;
		pW = w; pH = h;
		rand = new Random();
		
		//random position for new pickup
		sX = rand.nextInt(pW); 
		sY = rand.nextInt(pH);
		
		setRectangle();
		middleOfSprite = new Point(sX + (S/2), sY + (S/2));
	}
	
	//the draw method
	public void draw(Graphics g)
	{
		setRectangle();
		
		if(getType()==HEALTH)
		{
			g.setColor(Color.RED);
			g.fillRect((int)(sX+(S*4.0/10)), sY, S/4, S);
			g.fillRect(sX, (int)(sY+(S*4.0/10)), S, S/4);
		}
		else if(getType() == AMMO)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(sX, sY, S, S);
		}
	}
	
			//sets and gets//
	public void setType(int type)
	{currentPickupType = type;}
	
	public int getType()
	{return currentPickupType;}
	
	public String toString()
	{return types[currentPickupType];}
	
	public Rectangle getRectangle(){
		return rectContainingImage;
	}
	
	public void setRectangle(){
		rectContainingImage = new Rectangle(sX,sY,S,S);
	}
	
	public Point getPoint(){
		return middleOfSprite;
	}
	
	public int getRecovery()
	{
		return RECOVERY;
	}
}
