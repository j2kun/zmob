package backup;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class SpriteLoader {
	
	static AffineTransform translatorP, translatorZ, rotator;
	static AffineTransformOp translateOpP, translateOpZ, rotateOp;
	static BufferedImage playerPistol, playerShotty, playerWavebeam, zombie, poisonedZombie, ammo, health;
	int playerW, playerH, zombieW, zombieH;
	int horizDispP, vertDispP, halfDP;//player variables
	int horizDispZ, vertDispZ, halfDZ;//zombie variables
	
	public SpriteLoader()
	{
		loadImages();
		
		translatorP = AffineTransform.getTranslateInstance(horizDispP, vertDispP);
		translateOpP = new AffineTransformOp(translatorP ,AffineTransformOp.TYPE_BICUBIC);
		
		translatorZ = AffineTransform.getTranslateInstance(horizDispZ, vertDispZ);
		translateOpZ = new AffineTransformOp(translatorZ ,AffineTransformOp.TYPE_BICUBIC);
	}
	
	void loadImages()
	{
		//load the zombie images
		String fnm = "img/zombie.png";
		zombie = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = "img/zombie_poisoned.png";
		poisonedZombie = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
	
		zombieH = zombie.getHeight();
		zombieW = zombie.getWidth();
		
		//do the affinetransform calculations for the zombie
		halfDZ = (int)(Math.sqrt(zombieH*zombieH + zombieW*zombieW)/2);
		horizDispZ = (int)(halfDZ);
		vertDispZ = (int)(halfDZ - zombieH/6.0);
		
		//get the player sprite images
		fnm = "img/pistol.png";
		playerPistol = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = "img/shotty.png";
		playerShotty = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = "img/wavebeam.png";
		playerWavebeam = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		playerH = playerPistol.getHeight();
		playerW = playerPistol.getWidth();
		
		//do the affinetransform calculations for the player
		halfDP = (int)(Math.sqrt(playerH*playerH + playerW*playerW)/2);
		horizDispP = (int)(halfDP);
		vertDispP = (int)(halfDP - playerH/6.0);
		
		//get the pickup images
		fnm = "img/health.png";
		health = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());
		
		fnm = "img/ammo.png";
		ammo = Utils.getBI(new ImageIcon(getClass().getResource(fnm)).getImage());		
	}
	
	public BufferedImage getPistolImage()
	{	return playerPistol;}
	
	public BufferedImage getShottyImage()
	{	return playerShotty;}
	
	public BufferedImage getWavebeamImage()
	{	return playerWavebeam;}
	
	public BufferedImage getZombieImage()
	{	return zombie;}
	
	public BufferedImage getPoisonedZombieImage()
	{	return poisonedZombie;}
	
	public BufferedImage getHealthImage()
	{	return health;}
	
	public BufferedImage getAmmoImage()
	{	return ammo;}
	
	public int getHorizDispP()
	{	return horizDispP;}
	
	public int getVertDispP()
	{	return vertDispP;}
	
	public int getHalfDP()
	{	return halfDP;}
	
	public int getHorizDispZ()
	{	return horizDispZ;}
	
	public int getVertDispZ()
	{	return vertDispZ;}
	
	public int getHalfDZ()
	{	return halfDZ;}
	
	public int getPlayerW()
	{	return playerW;}
	
	public int getPlayerH()
	{	return playerH;}
	
	public int getZombieW()
	{	return zombieW;}
	
	public int getZombieH()
	{	return zombieH;}
	
	public AffineTransformOp getPlayerTranslator()
	{	return translateOpP;}
	
	public AffineTransformOp getZombieTranslator()
	{	return translateOpZ;}
	
	public AffineTransformOp getPlayerRotator(double angle)
	{	
		rotator =  AffineTransform.getRotateInstance(-angle, 
				playerW/2+horizDispP-10, playerH/2+vertDispP);
		rotateOp =  new AffineTransformOp (rotator, AffineTransformOp.TYPE_BICUBIC);
		return rotateOp;
	}
	
	public AffineTransformOp getZombieRotator(double angle)
	{	
		rotator =  AffineTransform.getRotateInstance(-angle, 
				zombieW/2+horizDispZ-10, zombieH/2+vertDispZ);
		rotateOp =  new AffineTransformOp (rotator, AffineTransformOp.TYPE_BICUBIC);
		return rotateOp;
	}
}
