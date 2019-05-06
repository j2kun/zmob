package game.sprite;
import game.Managed;
import game.collision.PlayerToZombieCollision;
import game.common.Constants;
import game.common.GameInfo;
import game.info.ZombieInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Zombie extends BaseSprite<ZombieInfo> implements Managed{

	final int BAR_SPACING = 10, BAR_WIDTH = 3;

	private boolean poisoned = false;
	private int barX, barY, barL;
	private int health;
	private int moveSpeed;

	public Zombie(ZombieInfo paramInfo) {
		super(paramInfo);
		Random rnd = new Random();
		info.x = rnd.nextInt(GameInfo.pW-20);
		info.y = rnd.nextInt(GameInfo.pH-20);

		moveSpeed = rnd.nextInt(5)+2;

		//each zombie has a health bar that floats above its head
		barX = info.x; barY = info.y - BAR_SPACING;
		barL = info.width/2;
		
		health = Constants.MAX_HEALTH;

		setRectangle();
	}

	public void update(){
		if(poisoned && GameInfo.frame%2==0)
			subtractHealth(1);
		if(isDead())
			return;
		//if the zombies dont move faster than 1 space, the angle wont register
		//and zombies freeze.
		info.x+=Math.round(Math.cos(info.angle)*moveSpeed);
		info.y-=Math.round(Math.sin(info.angle)*moveSpeed);
		checkBoundaries();
		setRectangle();//reset rectangle every time zombie moves
	}

	public void checkPlayerCollision(Player player) {
		new PlayerToZombieCollision(player, this).evaluate();
	}

	//draws zombie + zombie health bar
	public void draw(Graphics g2, BufferedImage bi, 
			AffineTransformOp translator, AffineTransformOp rotator) {

		Graphics2D g = (Graphics2D)g2;
		info.setPosn(info.x, info.y);
		g.setColor(Color.WHITE);

		BufferedImage translatedImage = translator.filter(bi, null);
		BufferedImage rotatedImage = rotator.filter (translatedImage, null);
		g.drawImage(rotatedImage, info.getCornerX(), info.getCornerY(), null);

		barX = info.x; barY = info.y - BAR_SPACING;
		g.setColor(Color.BLACK);
		g.drawRect(barX, barY, barL, BAR_WIDTH);//draw the outinfode bar

		g.setColor(Color.RED);
		double healthProp = (double)health/Constants.MAX_HEALTH;
		g.fillRect(barX+1, barY+1, (int)(barL*healthProp)-1, BAR_WIDTH-1 );//draw the ininfode bar
		//put color back to default
		g.setColor(Color.BLACK);
	}

	public void respawnZombie(boolean dead){
		Random rnd = new Random();
		info.x = rnd.nextInt(GameInfo.pW);
		info.y = rnd.nextInt(GameInfo.pH);
		if(dead){
			setRectangle();
		}
	}

	public void setDead(boolean dOA)
	{info.dead = dOA;}

	public boolean isDead()
	{return info.dead;}

	public void setPoisoned(boolean x){
		poisoned = x;
		if(x)
			moveSpeed = 2;
	}

	public boolean isPoisoned(){
		return poisoned;
	}

	public void setHealth(int h){
		health = h;
	}

	public void addHealth(int amt){
		health+=amt;

		if(health > 100)
			health = 100;
	}

	public void subtractHealth(int amt){
		health-=amt;

		if(health <= 0)	{
			health = 0;
			setDead(true);
		}
	}

	public int getHealth(){
		return health;
	}

	public double getAngle(){
		return info.angle;
	}

	public boolean shouldRemove() {
		return isDead();
	}
}