package backup;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Pickup {
	
	byte me;
	public final int HEALTH = 0, AMMO = 1;
	public final int RECOVERY = 25;
	final String[] types = new String[] {"health", "ammo"};
	private int currentPickupType = 0;
	private int pW, pH, sX, sY;
	Random rand;
	
	private Rectangle rectContainingImage;
	//private Point middleOfSprite;
	
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
	}
	
	//the draw method
	public void draw(Graphics g, BufferedImage bi)
	{
		setRectangle(bi);
		
		g.drawImage(bi, sX, sY, null);
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
	
	public void setRectangle(BufferedImage bi){
		rectContainingImage = new Rectangle(sX,sY,bi.getWidth(),bi.getHeight());
	}
	
	public int getRecovery()
	{
		return RECOVERY;
	}
}
