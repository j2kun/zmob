package backup;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ZombieSprites {
	
	int pW, pH; // dimensions of panel
	int sX, sY; //position of MainCharacterSprite on panel
	private Rectangle shape;
	private boolean dead;
	private double angle;
	
	public boolean isPoisoned = false;
	public boolean isBoss = false;
	
	int power = 0;
	final int NORMAL_ZOMBIE_POWER = 2, BAR_SPACING = 10, SPACING = 5, POISONED = 1, HEALTHY = 2;
	//spacing is for the amt that the sprite moves when it contacts the player
	//for the angles
	
	int maxHealth = 100;
	int barX, barY, barL, barW, spriteWidth, spriteHeight;
	int currentHealth;
	private final double CONVERT_TO_DEGREES = Math.PI/180;
	private int moveSpeed;
	
	SpriteInfo si;
	
	//BufferedImage currentImage;
	//int spriteWidth, spriteHeight;
	//static ImageIcon healthySprite, poisonedSprite;
	//static AffineTransform translator, rotator;
	//static AffineTransformOp translateOP, rotateOP;
//	these variables used for the translation of the image for rotation
	//int halfDiagonal, horizontalDistance, verticalDistance;

	
	//constructor for a normal zombie
	public ZombieSprites(int w, int h, int spriteWidth, int spriteHeight) {
		// constructor sent width and height of panel in which MainCharacterSprites can move
		pW=w; pH=h;
		Random rnd = new Random();
		sX = rnd.nextInt(pW);
		sY = rnd.nextInt(pH);
		
		moveSpeed = rnd.nextInt(5)+2;
		power = NORMAL_ZOMBIE_POWER;
		
		//each zombie has a health bar that floats above its head
		barX = sX; barY = sY - BAR_SPACING;
		barL = spriteWidth/2; barW = 3;
		
		shape = new Rectangle(sX,sY,spriteWidth,spriteHeight);
		currentHealth = maxHealth;
		dead = false;
		
		this.spriteHeight = spriteHeight;
		this.spriteWidth = spriteWidth;
		
		si = new SpriteInfo(sX, sY, spriteWidth, spriteHeight);
		//loadImages();
    	//translator = AffineTransform.getTranslateInstance(horizontalDistance,verticalDistance);
		//translateOP = new AffineTransformOp(translator,AffineTransformOp.TYPE_BICUBIC);
	}
	
	//constructor for a custom zombie (bosses)
	/*public ZombieSprites(int w, int h, int r, int x, int y, int speed, int pow, int health) {
		
		isBoss = true;
		pW=w; pH=h;
		sX = x;
		sY = y;
		moveSpeed = speed;
		power = pow;
		currentHealth = health;
		maxHealth = health;
		
		//each zombie has a health bar that floats above its head
		barX = sX; barY = sY - BAR_SPACING;
		barL = spriteWidth/2; barW = (int)Math.round((double)(r/12));
		
		dirX = 0; dirY = 0; // not moving
		setRectangle();
		middleOfSprite = new Point(sX + (spriteWidth/2), sY + (spriteHeight/2));
		dead = false;
	}*/
	
	public void checkBoundaries(){
		if(sX <= 0)
			sX = 0;
		else if (sX >= pW)
			sX = pW;
		if (sY <= 0)
			sY = 0;
		else if(sY >= pH)
			sY = pH;
	}
	
	public void checkOverlap(ZombieSprites w)
	{
		if(w.getRectangle().intersects(this.getRectangle())){//if they overlap
			double x = w.si.getSpriteCenterX();
			double y = w.si.getSpriteCenterY();
			double x2 = this.si.getSpriteCenterX();
			double y2 = this.si.getSpriteCenterY();
			
			if(x2 > x)
			{//if this zombie is on the right side
				this.sX += 1;//move the one on the right 1 pixel to the right
				w.sX -= 1;//move the one on the left one pixel left
				
				if(y2 > y)
				{//if its above
					this.sY += 1;//move the higher one 1 pixel higher
					w.sY -= 1;//move the lower one a pixel lower
				}
				else{
					w.sY += 1;//if its <= to other zombie, then do the
					this.sY -= 1;//opposite of the if statement
				}
			}
			
			// if either of the the zombies are poisoned
			if(this.isPoisoned) //then poison both zombies
				w.setPoisoned(true);
			else if(w.isPoisoned)
				this.setPoisoned(true);
				
		}
	}
	
	public void checkIntersectionWithBB(Player w){
		
		if(w.getRectangle().intersects(this.getRectangle()) ){
			double x = si.getSpriteCenterX(), y = si.getSpriteCenterY();
			double x2 = w.si.getSpriteCenterX(), y2 = w.si.getSpriteCenterY();
			
			if(x2 > x){//if the BB is on the right side
				this.sX =  this.sX - SPACING;//move the one on the right 1 pixel to the right
				if(y2 > y)//if it's above
					this.sY = this.sY - SPACING;//move the higher one 1 pixel higher
				else//if it's below
					this.sY = this.sY + SPACING;//move lower one 1 pixel lower
			}
			else{
				this.sX = this.sX + SPACING;//move the one on the right 1 pixel to the right
				if(y2 > y)//if its above
					this.sY = this.sY - SPACING;//move the higher one 1 pixel higher
				else
					//if its <= to other zombie, then do the
					this.sY = this.sY + SPACING;//opposite of the if statement
			}
		}
	}
	
	//x and y are the x and y of the player sprite
	//this method takes the two points, and moves the
	//sprite at an angle towards the point
	public void decideMove(int x, int y){
		
		//first, get the angle between (x,y) and (sX,sY)
		angle = 0.0;
		double xDist, yDist;
		
		//set the xdist and ydist between the two points
		xDist = (double)(sX-x);
		yDist = (double)(sY-y);
		
		if(xDist == 0)
			angle = 0.0;
		else
			angle = Math.atan((double)yDist/xDist);
		
		//convert to degrees to make unit circle work
		angle/=CONVERT_TO_DEGREES;
		
		if(y<sY) //if in 1st or 2nd quadrant
			angle = (x>sX) ? -angle : 180-angle;
		else if(y>sY)//if in 3rd or 4th quadrant
			angle = (x>sX) ? 360-angle : 180-angle;
		
		//at this point, if the angle is 180 and the y value is not 0
		//then it is either 180 or 90
		if(angle == 180)
			angle = (y>sY) ? 270 : 90;
		
		if(angle == 0 || angle == 360)
			if(x<sX)
				angle = 180;
		
		angle *= CONVERT_TO_DEGREES; //then convert back to radians (0 < angle < 2 pi)
		
		//if the zombies dont move faster than 1 space, the angle wont register
		//and zombies freeze.
		sX+=Math.round(Math.cos(angle)*moveSpeed);
		sY-=Math.round(Math.sin(angle)*moveSpeed);
		
		setRectangle();//reset rectangle every time zombie moves
	}
	
	//draws zombie + zombie health bar
	public void draw(Graphics g2, BufferedImage bi, AffineTransformOp translator, AffineTransformOp rotator) {
		
		Graphics2D g = (Graphics2D)g2;
		si.setPosn(sX, sY);
		//drawZombie
		g.setColor(Color.WHITE);
		//g.fillOval(sX, sY, radius, radius);
		
		BufferedImage translatedImage = translator.filter(bi, null);
	    BufferedImage rotatedImage = rotator.filter (translatedImage, null);
		g.drawImage(rotatedImage, si.getSXCORNER(), si.getSYCORNER(), null);
		
		//g.drawImage(bi, sX, sY, null);
		//g.fillRect(sX, sY, width, height);
		//update health bar info
		barX = sX; barY = sY - BAR_SPACING;
		//draw health bar
		g.setColor(Color.BLACK);
		g.drawRect(barX, barY, barL, barW);//draw the outside bar
		
		g.setColor(Color.RED);
		double healthProp = (double)currentHealth/maxHealth;
		g.fillRect(barX+1, barY+1, (int)(barL*healthProp)-1, barW-1 );//draw the inside bar
		//put color back to default
		g.setColor(Color.BLACK);
		
		/*g2.setColor(Color.BLUE);
		g2.fillOval(sX, sY, 5, 5);
		g2.setColor(Color.WHITE);
		g2.fillOval(si.getSXCENTER(), si.getSYCENTER(), 5, 5);
		g2.setColor(Color.GREEN);
		g2.fillOval(si.getSXCORNER(), si.getSYCORNER(), 5, 5);
		g2.setColor(Color.RED);
		g2.fillOval(si.getSpriteCenterX(), si.getSpriteCenterY(), 5, 5);*/
	}

	/*private void setCurrentImage(int poisoned) {
		if(poisoned == HEALTHY)
			currentImage = Utils.getBI(healthySprite.getImage());
		else
			currentImage = Utils.getBI(poisonedSprite.getImage());
	}*/

	public void respawnZombie(boolean dead){
		Random rnd = new Random();
		sX = rnd.nextInt(pW);
		sY = rnd.nextInt(pH);
		if(dead){
			shape = new Rectangle(sX,sY,spriteWidth,spriteHeight);
			setRectangle();
		}
	}
	
			//sets and gets//
	public Rectangle getRectangle()
	{return shape;}
	
	public void setRectangle()
	{
		checkBoundaries();
		shape = new Rectangle(sX,sY,spriteWidth, spriteHeight);
	}
	
	public void setDead(boolean dOA)
	{dead = dOA;}
	
	public boolean isDead()
	{return dead;}
	
	public void setPoisoned(boolean x)
	{
		isPoisoned = x;
		if(x)
			moveSpeed = 2;
		
		/*if(isPoisoned)
			setCurrentImage(POISONED);
		else
			setCurrentImage(HEALTHY);*/
	}
	
	public boolean isPoisoned()
	{return isPoisoned;}

	public void setHealth(int h)
	{currentHealth = h;}
	
	public void addHealth(int amt)
	{
		currentHealth+=amt;
		
		if(currentHealth > 100)
			currentHealth = 100;
	}
	
	public void subtractHealth(int amt)
	{
		currentHealth-=amt;
		
		if(currentHealth <= 0)
		{
			currentHealth = 0;
			setDead(true);
		}
	}

	public int getHealth()
	{return currentHealth;}
	
	public int getPower()
	{return power;}
	
	public boolean isBoss()
	{return isBoss;}
	
	public double getAngle()
	{return angle;}
}