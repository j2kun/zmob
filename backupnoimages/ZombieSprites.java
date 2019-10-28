package backupnoimages;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;

public class ZombieSprites {

	int radius = 0; // radius of our zombie
	int pW, pH; // dimensions of panel
	Color sColor = Color.cyan;
	int sX, sY; //position of MainCharacterSprite on panel
	int dirX, dirY; // direction controls of MainCharacterSprite
	private Rectangle shape;
	private Point middleOfSprite;
	private boolean dead;

	public boolean isPoisoned = false;
	public boolean isBoss = false;

	int power = 0;
	final int NORMAL_ZOMBIE_POWER = 2, BAR_SPACING = 10;

	int maxHealth = 100;
	int barX, barY, barL, barW;
	int currentHealth;
	//spacing is for the amt that the sprite moves when it contacts the player
	private final int SPACING = 5;
	//for the angles
	private final double CONVERT_TO_DEGREES = Math.PI/180;
	private int moveSpeed;
	private Color zColor = new Color(0,0,128);
	final private Color POISONED = Color.white, HEALTHY = zColor;

	//constructor for a normal zombie
	public ZombieSprites(int width, int height) {
		// constructor sent width and height of panel in which MainCharacterSprites can move
		pW=width; pH=height;
		Random rnd = new Random();
		sX = rnd.nextInt(pW);
		sY = rnd.nextInt(pH);
		radius = 16;

		moveSpeed = rnd.nextInt(5)+2;
		power = NORMAL_ZOMBIE_POWER;

		//each zombie has a health bar that floats above its head
		barX = sX; barY = sY - BAR_SPACING;
		barL = radius; barW = 3;

		dirX = 0; dirY = 0; // not moving
		shape = new Rectangle(sX,sY,radius,radius);
		middleOfSprite = new Point(sX + (radius/2), sY + (radius/2));
		currentHealth = maxHealth;
		dead = false;
	}

	//constructor for a custom zombie (bosses)
	public ZombieSprites(int width, int height, int r, int x, int y, int speed, int pow, int health) {

		isBoss = true;
		pW=width; pH=height;
		sX = x;
		sY = y;
		moveSpeed = speed;
		power = pow;
		radius = r;
		currentHealth = health;
		maxHealth = health;

		//each zombie has a health bar that floats above its head
		barX = sX; barY = sY - BAR_SPACING;
		barL = radius; barW = (int)Math.round((double)(r/12));

		dirX = 0; dirY = 0; // not moving
		shape = new Rectangle(sX,sY,radius, radius);
		middleOfSprite = new Point(sX + (radius/2), sY + (radius/2));
		dead = false;
	}

	public Polygon createPolygon(int sX, int sY, int radius)
	{
		Polygon B = new Polygon();
		int numSides = 8; //how many sides we want on the polygon
		//algorithm here
		for (int i = 0; i<(2*Math.PI); i+=(Math.PI/numSides))
		{
			int x = sX+(int)(Math.cos(i)*radius);
			int y = sY-(int)(Math.sin(i)*radius);
			B.addPoint(x,y);
		}

		return B;
	}

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

	public void checkOverlap(ZombieSprites q, ZombieSprites w)
	{
		if(w.getRectangle().intersects(q.getRectangle())){//if they overlap
			double x = w.getPoint().getX();
			double y = w.getPoint().getY();
			double x2 = q.getPoint().getX();
			double y2 = q.getPoint().getY();
			if(x2 > x){//if the zombie is on the right side
				q.sX = 1 + q.sX;//move the one on the right 1 pixel to the right
				w.sX = w.sX -1;//move the one on the left one pixel left
				if(y2 > y){//if its above
					q.sY = 1 + q.sY;//move the higher one 1 pixel higher
					w.sY = w.sY -1;//move the lower one a pixel lower
				}
				else{
					w.sY = 1 + w.sY;//if its <= to other zombie, then do the
					q.sY = q.sY -1;//opposite of the if statement
				}
			}
			q.setPoint();
			w.setPoint();

			// if either of the the zombies are poisoned
			if(q.isPoisoned || w.isPoisoned)
			{ //then poison both zombies
				q.setPoisoned(true);
				w.setPoisoned(true);
			}
		}
	}

	public void checkIntersectionWithBB(ZombieSprites q, BigBall w){
		if(w.getRectangle().intersects(q.getRectangle()) ){
			double x = q.getPoint().getX();
			double y = q.getPoint().getY();
			double x2 = w.getPoint().getX();
			double y2 = w.getPoint().getY();


			if(x2 > x){//if the BB is on the right side
				q.sX =  q.sX - SPACING;//move the one on the right 1 pixel to the right
				if(y2 > y){//if it's above
					q.sY = q.sY - SPACING;//move the higher one 1 pixel higher
				}
				else{//if it's below
					q.sY = q.sY + SPACING;//move lower one 1 pixel lower
				}
			}
			else{
				q.sX = q.sX + SPACING;//move the one on the right 1 pixel to the right
				if(y2 > y){//if its above
					q.sY = q.sY - SPACING;//move the higher one 1 pixel higher
				}
				else{
					//if its <= to other zombie, then do the
					q.sY = q.sY + SPACING;//opposite of the if statement
				}

			}
			q.setPoint();
		}
	}

	//x and y are the x and y of the player sprite
	//this method takes the two points, and moves the
	//sprite at an angle towards the point
	public void decideMove(int x, int y){

		//first, get the angle between (x,y) and (sX,sY)
		double angle = 0.0, xDist, yDist;

		//set the xdist and ydist between the two points
		xDist = (double)(sX-x);
		yDist = (double)(sY-y);

		if(xDist == 0)
			angle = 0.0;
		else
			angle = Math.atan((double)yDist/xDist);

		//convert to degrees to make unit circle work
		angle/=CONVERT_TO_DEGREES;

		//arctan only gives values -pi/2 < x < pi/2
		//get angles to work as a normal unit cicle
		if(y<sY)//if in 1st or 2nd quadrant
		{
			if(x>=sX)//if in 1st quadrant
				angle = -angle;
			else if(xDist<=sX)//if in 2nd quadrant
			{
				angle += 180;
				angle = 360-angle;
			}
		}
		else if(y>=sY)//if in 3rd or 4th quadrant
		{
			if(x>=sX)//if in 4th quadrant
				angle = 360-angle;
			else if(x<sX)//if in 3rd quadrant
			{
				angle += 180;
				angle = 360-angle;
			}
		}//end making unit circle

		//check for exact 90 or 270 degree angles
		if(angle == 0 || angle == 360)
		{
			if(y<sY)
				angle = 90;
			else if(y>sY)
				angle = 270;
		}

		angle *= CONVERT_TO_DEGREES; //then convert back to radians (0 < angle < 2 pi)

		//if the zombies dont move faster than 1 space, the angle wont register
		//and zombies freeze.
		sX+=Math.round(Math.cos(angle)*moveSpeed);
		sY-=Math.round(Math.sin(angle)*moveSpeed);

		setRectangle();//reset rectangle every time zombie moves
	}

	//draws zombie + zombie health bar
	public void draw(Graphics g) {

		//sets color based on poison setting
		if(isPoisoned())
			g.setColor(POISONED);
		else
			g.setColor(HEALTHY);

		g.fillOval(sX, sY, radius, radius);

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
	}

	public void respawnZombie(boolean dead){
		Random rnd = new Random();
		sX = rnd.nextInt(pW);
		sY = rnd.nextInt(pH);
		if(dead){
			shape = new Rectangle(sX,sY,radius,radius);
			middleOfSprite = new Point(sX + (radius/2), sY + (radius/2));
			setRectangle();
			setPoint();
		}
	}

	//dunno what this is for
	public void hideZombie(Graphics dbg, int x, int y){
		dbg.clearRect(x, y, radius, radius);
		sX = -100; sY = -100;
		dirX = 0; dirY = 0;
		draw(dbg);
		setRectangle();
		setPoint();
	}

			//sets and gets//
	public Rectangle getRectangle(){
		return shape;
	}

	public void setRectangle(){
		checkBoundaries();
		shape = new Rectangle(sX,sY,radius, radius);
	}

	public void setPoint(){
		middleOfSprite = new Point(sX + (radius/2), sY + (radius/2));
	}

	public void setDead(boolean dOA){
		dead = dOA;
	}

	public Point getPoint(){
		return middleOfSprite;
	}

	public boolean isDead(){
		return dead;
	}

	public void setPoisoned(boolean x)
	{
		isPoisoned = x;
		if(x)
			moveSpeed = 2;
	}

	public boolean isPoisoned()
	{
		return isPoisoned;
	}

	public void setHealth(int h)
	{
		currentHealth = h;
	}

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
	{
		return currentHealth;
	}

	public int getPower()
	{
		return power;
	}

	public boolean isBoss()
	{
		return isBoss;
	}
}
