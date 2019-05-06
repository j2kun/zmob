package backup;
import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player {
	
	//height and width of the sprite
	int height, width;
	
	//three points to pick from, center, corner of image, corner of bounding box for image
	int sX = 0, sY = 0, pW = 0, pH = 0;
	int barL, barW, barX, barY;
	boolean up = false, down = false, left = false, right = false;//if the ball is moving
	boolean running;
	double runLeft = 100;//amount of frames the player can run
	
	//						horizontal and veritcal spacing of the bar
	final int MAX_RUN = 100, BAR_V_SPACING = 25, BAR_H_SPACING = 20;
	final double RUN_DEPLETION = 1.0, RUN_RECHARGE = .5;
	final int WALK = 4, RUN = 10;
	final int PISTOL = 1, SHOTGUN = 2, WAVEBEAM = 3;
	int moveDist = WALK; //move speed changes if running
	Polygon shape;
	Rectangle rect;
	private int currentRange = 0;
	
	SpriteInfo si;
	
	public Player(int w, int h, int spriteW, int spriteH)
	{
		pW = w; pH = h;
		sX = pW/2; sY = pH/2;
		
		running = false;
		//loadImages();
		width = spriteW;
		height = spriteH;
		si = new SpriteInfo(sX, sY, width, height);
		setRectangle();
	}
	
	//draw method draws sprite and range circle
	public void draw(Graphics g, boolean paused, BufferedImage bi, 
			AffineTransformOp translator, AffineTransformOp rotator) {
		
		Graphics2D g2 = (Graphics2D)g;
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
		
		si.setPosn(sX, sY);
		setRectangle();//update rectangle and point every frame	

		//draw the range circle//
		g2.setColor(Color.BLUE);
		g2.drawOval(si.getSpriteCenterX()-getRange(), si.getSpriteCenterY()-getRange(), getRange()*2, getRange()*2);
		
		//AffineTransform info:
		//first, we translate the image so that it can rotate without being cut off
		BufferedImage translatedImage = translator.filter(bi, null);
		//then we rotate it to the correct angle
	    BufferedImage rotatedImage = rotator.filter (translatedImage, null);
	    
		//draw the player sprite
		g2.drawImage(rotatedImage, si.getSXCORNER(), si.getSYCORNER(), null);
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
	
	//**************GET AND SET METHODS***************\\
	public void setRectangle(){
		checkBoundaries();
		rect = new Rectangle(si.getSXCENTER()-width/2, si.getSYCENTER()-height/2, si.width, si.height);
	}
	
	public Rectangle getRectangle(){
		return rect;
	}
	
	public void checkBoundaries(){
		if(sX <= 0){
			sX = 0;
		}
		else if (sX >= pW - width){
			sX = pW - width;
		}
		if (sY <= 0){
			sY = 0;
		}
		else if(sY >= pH - height){
			sY = pH - height;
		}
	}
	
	//radius of circle is equal to the width of the sprite image
	public int getRadius()
	{return width;}
	
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
