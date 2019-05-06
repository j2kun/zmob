package game.loader;

import static game.common.GameInfo.ammo;
import static game.common.GameInfo.halfDP;
import static game.common.GameInfo.halfDZ;
import static game.common.GameInfo.health;
import static game.common.GameInfo.horizDispP;
import static game.common.GameInfo.horizDispZ;
import static game.common.GameInfo.playerH;
import static game.common.GameInfo.playerPistol;
import static game.common.GameInfo.playerShotty;
import static game.common.GameInfo.playerW;
import static game.common.GameInfo.playerWavebeam;
import static game.common.GameInfo.poisonedZombie;
import static game.common.GameInfo.translateOpP;
import static game.common.GameInfo.translateOpZ;
import static game.common.GameInfo.vertDispP;
import static game.common.GameInfo.vertDispZ;
import static game.common.GameInfo.zombie;
import static game.common.GameInfo.zombieH;
import static game.common.GameInfo.zombieW;
import game.common.Constants;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class SpriteLoader extends Loader implements Runnable{
	
	private static AffineTransform translatorP, translatorZ;
	
	private static Map<Double, AffineTransformOp> rotators;
	
	public SpriteLoader(){
		super("Sprites");
		
		translatorP = AffineTransform.getTranslateInstance(horizDispP, vertDispP);
		translateOpP = new AffineTransformOp(translatorP ,AffineTransformOp.TYPE_BICUBIC);
		
		translatorZ = AffineTransform.getTranslateInstance(horizDispZ, vertDispZ);
		translateOpZ = new AffineTransformOp(translatorZ ,AffineTransformOp.TYPE_BICUBIC);
		
		rotators = new HashMap<Double, AffineTransformOp>();
		
		getPlayerRotator(0);
		getZombieRotator(0);
		setFinished(true);
	}
	
	protected void load(){
		//load the zombie images
		String fnm = Constants.IMAGE_FILENAME_PREFIX + "zombie.png";
		zombie = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = Constants.IMAGE_FILENAME_PREFIX + "zombie_poisoned.png";
		poisonedZombie = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
	
		zombieH = zombie.getHeight();
		zombieW = zombie.getWidth();
		
		//do the affinetransform calculations for the zombie
		halfDZ = (int)(Math.sqrt(zombieH*zombieH + zombieW*zombieW)/2);
		horizDispZ = (int)(halfDZ);
		vertDispZ = (int)(halfDZ - zombieH/6.0);
		
		//get the player sprite images
		fnm = Constants.IMAGE_FILENAME_PREFIX + "pistol.png";
		playerPistol = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = Constants.IMAGE_FILENAME_PREFIX + "shotty.png";
		playerShotty = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = Constants.IMAGE_FILENAME_PREFIX + "wavebeam.png";
		playerWavebeam = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		playerH = playerPistol.getHeight();
		playerW = playerPistol.getWidth();
		
		//do the affinetransform calculations for the player
		halfDP = (int)(Math.sqrt(playerH*playerH + playerW*playerW)/2);
		horizDispP = (int)(halfDP);
		vertDispP = (int)(halfDP - playerH/6.0);
		
		//get the pickup images
		fnm = Constants.IMAGE_FILENAME_PREFIX + "health.png";
		health = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = Constants.IMAGE_FILENAME_PREFIX + "ammo.png";
		ammo = getBI(new ImageIcon(getClass().getResource(fnm)).getImage());		
	}
	
	public AffineTransformOp getPlayerRotator(double angle){	
		double truncatedAngle = (double)Math.round((angle*100))/100.0;
		AffineTransformOp rotateOp = rotators.get(truncatedAngle);
		
		if (rotateOp == null) {
			AffineTransform atr = AffineTransform.getRotateInstance(-truncatedAngle,
					playerW / 2 + horizDispP - 10, playerH / 2 + vertDispP);
			rotateOp = new AffineTransformOp(atr, AffineTransformOp.TYPE_BICUBIC);
			rotators.put(truncatedAngle, rotateOp);
		}
		
		return  rotateOp;
	}
	
	public AffineTransformOp getZombieRotator(double angle){
		double truncatedAngle = Math.round((angle*100))/100;
		AffineTransformOp rotateOp = rotators.get(truncatedAngle);
		
		if (rotateOp == null) {
			AffineTransform atr =  AffineTransform.getRotateInstance(-truncatedAngle, 
				zombieW/2+horizDispZ-10, zombieH/2+vertDispZ);
			rotateOp = new AffineTransformOp(atr, AffineTransformOp.TYPE_BICUBIC);
			rotators.put(truncatedAngle, rotateOp);
		}
		
		return  rotateOp;
	}

	public void run() {}
}
