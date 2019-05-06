package game.sprite;
import static game.common.Constants.RUN;
import static game.common.Constants.RUN_DEPLETION;
import static game.common.Constants.RUN_RECHARGE;
import static game.common.Constants.WALK;
import game.common.Constants;
import game.common.GameInfo;
import game.gun.Gun;
import game.info.PlayerInfo;
import game.loader.SpriteLoader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends BaseSprite<PlayerInfo>{

	final int BAR_V_SPACING = 25, BAR_H_SPACING = 20;

	public Player(PlayerInfo paramInfo){
		super(paramInfo);

		info.running = false;
		setRectangle();
	}

	public void update(){
		configureMoveDist();

		if(info.up)
			info.y-=info.moveDist;
		if(info.down)
			info.y+=info.moveDist;
		if(info.left)
			info.x-=info.moveDist;
		if(info.right)
			info.x+=info.moveDist;

		info.setPosn(info.x, info.y);
		checkBoundaries();//check bounds of panel
		setRectangle();//info.update rectangle and point every frame	
	}
	
	//draw method draws sprite and range circle
	public void draw(Graphics g, Gun currentGun, SpriteLoader loader) {

		Graphics2D g2 = (Graphics2D)g;

		//draw the range circle
		g2.setColor(Color.BLUE);
		g2.drawOval(info.getSpriteCenterX()-getRange(), 
				info.getSpriteCenterY()-getRange(), 
				getRange()*2, 
				getRange()*2);

		//AffineTransformation
		//first, we translate the image so that it can rotate without being cut off
		BufferedImage translatedImage = 
			GameInfo.translateOpP.filter(currentGun.getImage(), null);
		//then we rotate it to the correct angle
		BufferedImage rotatedImage = 
			loader.getPlayerRotator(GameInfo.mouseAngle).filter (translatedImage, null);

		//draw the player sprite
		g2.drawImage(rotatedImage, info.getCornerX(), info.getCornerY(), null);
	}

	public void configureMoveDist(){
		if (!GameInfo.paused) {
			//info.running algorithm			
			if (info.running && (info.up || info.down || info.left || info.right))
			{
				info.moveDist = RUN;
				subtractStamina(RUN_DEPLETION);
			} else {
				//if(!(info.up && info.down && info.left && info.right))
				info.moveDist = WALK;
				addStamina(RUN_RECHARGE);
			}
		}		
	}	

	//**************GET AND SET METHODS***************\\
	public void setRectangle(){
		checkBoundaries();
		setRectangle(info.getCenterX()-info.width/2, 
				info.getCenterY()-info.height/2, 
				info.width, info.height);
	}

	public int getRadius(){
		return info.width;
	}

	public void resetPosition(){
		info.x = GameInfo.pW / 2;
		info.y = GameInfo.pH / 2;
	}

	public void addStamina(double amt){
		info.runLeft += amt;

		if(info.runLeft>100)
			info.runLeft = 100;
	}

	public void subtractStamina(double amt){
		info.runLeft-=amt;

		if(info.runLeft < 0)
		{
			info.runLeft = 0;
			info.running = false;
			info.moveDist = WALK;
		}
	}

	public void setRunning(boolean rawr){
		info.running = rawr;
	}

	public boolean isRunning(){
		return info.running;
	}

	public void setRange(int r){
		info.range = r;
	}

	public int getRange(){
		return info.range;
	}

	public double getStamina(){
		return info.runLeft;
	}

	public void stopSprite(){
		info.up = false;
		info.down = false;
		info.right = false;
		info.left = false;
	}

	public int getHealth(){
		return info.health;
	}

	public void setHealth(int i) {
		info.health = i;
		checkHealth();
	}
	
	private void checkHealth(){
		if(info.health > Constants.MAX_HEALTH)
			info.health = Constants.MAX_HEALTH;
		if(info.health <= 0)
			GameInfo.gameOver = true;
	}
	
	public boolean fullHealth(){
		return info.health >= Constants.MAX_HEALTH;
	}
	
	public void addHealth(){
		info.health++;
		checkHealth();
	}
	
	public void addHealth(int amount){
		info.health += amount;
		checkHealth();
	}
	
	public void subtractHealth(){
		info.health--;
		checkHealth();
	}
}
