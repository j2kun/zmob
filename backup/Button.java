package backup;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Button {
	
	String[] buttonString = new String[]{"PAUSE"};
	Rectangle rect;
	private int sX, sY, rectL, rectW;
	private boolean isMouseOver = false;
	
	public Button()
	{
		sX = 20;
		sY = 30;
		rectL = 50; rectW = 15;
		setRect();
	}
	
	public void setRect()
	{
		rect = new Rectangle(sX, sY-15, rectL, rectW);
	}
	
	public void draw(Graphics g)
	{		
		//decide color based on mouseover
		g.setColor(Color.BLACK);
		//g.fillRect(sX, sY-15, rectL, rectW);
		
		if(isMouseOver)
			g.setColor(Color.RED);
		
		g.drawString(buttonString[0], sX, sY);
		g.setColor(Color.BLACK);//reset color when done
	}
	
	public Rectangle getRect()
	{
		return rect;
	}
	
	public void setMouseOver(boolean x)
	{
		isMouseOver = true;
	}
	
	public boolean isMouseOver()
	{
		return isMouseOver;
	}
	
}
